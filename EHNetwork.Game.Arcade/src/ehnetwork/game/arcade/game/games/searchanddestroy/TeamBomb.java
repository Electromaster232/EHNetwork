package ehnetwork.game.arcade.game.games.searchanddestroy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import net.minecraft.server.v1_7_R4.EntityTNTPrimed;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.hologram.Hologram;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.GameTeam;

class TeamBomb implements Comparable<TeamBomb>
{
    private class ArmInfo
    {
        private ArrayList<Player> _armers = new ArrayList<Player>();
        private long _firstArmed = System.currentTimeMillis();
        private long _lastInteract;
        private GameTeam _owningTeam;
        private int _timeToArmBomb = 4;

        public ArmInfo(GameTeam team)
        {
            this._owningTeam = team;
        }

        public void addArmer(Player gamer)
        {
            if (!_armers.contains(gamer))
            {
                _timeToArmBomb -= 1;
                _armers.add(gamer);
                /*       ItemStack item = gamer.getPlayer().getItemInHand();
                if (item != null && item.getType() != Material.AIR)
                {
                    int timeToTake = 0;
                    if (item.containsEnchantment(Enchants.BOMB_SPEED)
                            && item.getEnchantmentLevel(Enchants.BOMB_SPEED) > timeToTake)
                        timeToTake = item.getEnchantmentLevel(Enchants.BOMB_SPEED);
                    if (item.containsEnchantment(Enchants.BOMB_ARMING)
                            && item.getEnchantmentLevel(Enchants.BOMB_ARMING) > timeToTake && _bombEntity == null)
                        timeToTake = item.getEnchantmentLevel(Enchants.BOMB_ARMING);
                    if (item.containsEnchantment(Enchants.BOMB_DEFUSING)
                            && item.getEnchantmentLevel(Enchants.BOMB_DEFUSING) > timeToTake && _bombEntity != null)
                        timeToTake = item.getEnchantmentLevel(Enchants.BOMB_DEFUSING);
                    _timeToArmBomb -= timeToTake;
                }*/
            }
        }

        public float getPercentageOfBombDone()
        {
            float done = (float) (System.currentTimeMillis() - _firstArmed) / (1000 * _timeToArmBomb);
            return Math.min(done, 1);
        }

        public boolean isFused()
        {
            return isValid() && _firstArmed + (1000 * _timeToArmBomb) < System.currentTimeMillis();
        }

        public boolean isValid()
        {
            return _owningTeam.IsTeamAlive() && _lastInteract + 1000 > System.currentTimeMillis();
        }

    }

    private ArrayList<ArmInfo> _armers = new ArrayList<ArmInfo>();
    private TNTPrimed _bombEntity;
    private Location _bombLocation;
    private final int _entityId = UtilEnt.getNewEntityId();
    private SearchAndDestroy _game;
    private Hologram _hologram;
    private long _lastHiss;
    private GameTeam _owningTeam;
    private long _timeBombArmed;
    private int _timeUntilExplode;

    public TeamBomb(ArcadeManager manager, SearchAndDestroy game, GameTeam owningTeam, Location bombLocation)
    {
        this._game = game;
        this._owningTeam = owningTeam;
        this._bombLocation = bombLocation;
    }

    public void respawnBomb()
    {
        Location loc = getBlockLocation();
        if (_bombEntity != null)
        {
            _bombEntity.remove();
            loc = _bombEntity.getLocation();
        }
        _game.CreatureAllowOverride = true;
        EntityTNTPrimed entity = new EntityTNTPrimed(((CraftWorld) getBlockLocation().getWorld()).getHandle());
        _game.CreatureAllowOverride = false;
        double x = loc.getX();
        double y = loc.getY() + 0.2;
        double z = loc.getZ();
        entity.setPosition(x, y, z);
        entity.motY = 0.20000000298023224D;
        entity.fuseTicks = 120000;
        entity.lastX = x;
        entity.lastY = y;
        entity.lastZ = z;
        ((CraftWorld) getBlockLocation().getWorld()).getHandle().addEntity(entity, SpawnReason.CUSTOM);
        _bombEntity = (TNTPrimed) entity.getBukkitEntity();
        _hologram.setLocation(getBomb().getLocation().add(0, .6, 0));
        _hologram.setFollowEntity(getBomb());
    }

    private void activateBomb(GameTeam gameTeam)
    {
        if (!isArmed())
        {
            _timeBombArmed = System.currentTimeMillis();
            getBlockLocation().getBlock().setType(Material.AIR);
            respawnBomb();
            _timeUntilExplode = 40;
            UtilTextMiddle.display("", gameTeam.GetColor() + gameTeam.GetName() + ChatColor.RESET + " armed "
                    + getTeam().GetColor() + getTeam().GetName() + "'s" + ChatColor.RESET + " bomb");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                GameTeam hisTeam = _game.GetTeam(player);
                if (hisTeam == getTeam())
                    player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 10000, 0);
                else
                    player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0.8F + new Random().nextFloat() * 0.2F);
            }
            Collections.sort(_game.getBombs());
            _hologram.setText(getTeam().GetColor() + C.Bold + "Exploding in " + ChatColor.DARK_RED + ChatColor.BOLD
                    + getTimeUntilExplode());
        }
        else
        {
            UtilTextMiddle.display("", _owningTeam.GetColor() + _owningTeam.GetName() + ChatColor.RESET + " defused their bomb");
            restoreBomb();
        }
        _armers.clear();
    }

    public void checkArmers()
    {
        Iterator<ArmInfo> armers = _armers.iterator();
        while (armers.hasNext())
        {
            ArmInfo info = armers.next();
            if (!info.isValid())
            {
                armers.remove();
            }
        }
    }

    @Override
    public int compareTo(TeamBomb o)
    {
        if (o._timeBombArmed == _timeBombArmed)
        {
            return 0;
        }
        if (o._timeBombArmed < _timeBombArmed)
        {
            return 1;
        }
        return -1;
    }

    public Location getBlockLocation()
    {
        return _bombLocation;
    }

    public TNTPrimed getBomb()
    {
        return _bombEntity;
    }

    public int getFakeBombId()
    {
        return _entityId;
    }

    public GameTeam getTeam()
    {
        return _owningTeam;
    }

    public int getTimeUntilExplode()
    {
        return _timeUntilExplode;
    }

    public boolean isArmed()
    {
        return _bombEntity != null;
    }

    private void onInteract(GameTeam gTeam, Player p)
    {
        if (_lastHiss + 500 <= System.currentTimeMillis())
        {
            p.getWorld().playSound(p.getLocation(), Sound.FIZZ, 1, 0);
            _lastHiss = System.currentTimeMillis();
        }
        p.getWorld()
                .spigot()
                .playEffect(_bombEntity == null ? _bombLocation.clone().add(0, 1.1, 0) : _bombEntity.getLocation().add(0, 1, 0),
                        Effect.LARGE_SMOKE, 0, 0, 0.1F, 0.05F, 0.1F, 0, 3, 30);
        ArmInfo info = null;
        for (ArmInfo arm : _armers)
        {
            if (arm._owningTeam == gTeam)
            {
                info = arm;
                break;
            }
        }
        if (info != null && !info.isValid())
        {
            _armers.remove(info);
            info = null;
        }
        if (info == null)
        {
            info = new ArmInfo(gTeam);
            _armers.add(info);
        }
        info.addArmer(p);
        info._lastInteract = System.currentTimeMillis();
        if (info.isFused())
        {
            activateBomb(gTeam);
            return;
        }
    }

    public void onInteractWithFuse(Player player)
    {
        GameTeam hisTeam = _game.GetTeam(player);
        if (hisTeam != null)
        {
            if (_owningTeam == hisTeam)
            {
                if (_bombEntity == null)
                {
                    player.sendMessage(C.cRed + "You cannot arm your bomb");
                }
                else
                {
                    onInteract(hisTeam, player);
                }
            }
            else
            {
                if (_bombEntity != null)
                {
                    player.sendMessage(C.cRed + "You cannot disarm their bomb");
                }
                else
                {
                    onInteract(hisTeam, player);
                }
            }
        }
    }

    /**
     * Sets the bomb block to air and removes the hologram and bomb entity
     */
    public void removeBomb()
    {
        _armers.clear();
        _hologram.stop();
        getBlockLocation().getBlock().setType(Material.AIR);
        if (getBomb() != null)
        {
            this.getBomb().remove();
            _bombEntity = null;
        }
    }

    /**
     * Sets the bomb block to tnt, updates the hologram and removes the bomb entity
     */
    public void restoreBomb()
    {
        _armers.clear();
        getBlockLocation().getBlock().setType(Material.TNT);
        _hologram.setFollowEntity(null);
        _hologram.setLocation(getBlockLocation().clone().add(0, 1, 0));
        _hologram.setText(getTeam().GetColor() + C.Bold + getTeam().GetName() + " Team's Bomb");
        if (getBomb() != null)
        {
            this.getBomb().remove();
            _bombEntity = null;
        }
    }

    public void setupHologram()
    {
        _hologram = new Hologram(this._game.getArcadeManager().getHologramManager(), getBlockLocation().clone().add(0, 1, 0));
        _hologram.setText(getTeam().GetColor() + C.Bold + getTeam().GetName() + " Team's Bomb");
        _hologram.start();
    }

    public void tickBomb()
    {
        if (_bombEntity != null)
        {
            _timeUntilExplode--;
            _bombEntity.getWorld().playSound(_bombEntity.getLocation(), Sound.CREEPER_DEATH, 1.85F, 1.2F);
            if (this._armers.isEmpty())
            {
                _hologram.setText(getTeam().GetColor() + C.Bold + "Exploding in " + ChatColor.DARK_RED + C.Bold
                        + this.getTimeUntilExplode());
            }
        }
    }

    public void updateBombHologram()
    {
        if (!_armers.isEmpty())
        {
            Iterator<ArmInfo> itel = _armers.iterator();
            ArrayList<String> text = new ArrayList<String>();
            while (itel.hasNext())
            {
                ArmInfo info = itel.next();
                if (!info.isValid())
                {
                    itel.remove();
                }
                else
                {
                    float percent = info.getPercentageOfBombDone();
                    String message = "";
                    for (double i = 0; i < 1; i += .1)
                    {
                        message += (percent >= i ? info._owningTeam.GetColor() : ChatColor.DARK_RED) + "█";
                    }
                    text.add(message);
                }
            }
            if (_armers.isEmpty())
            {
                _hologram.setText(getTeam().GetColor() + C.Bold + getTeam().GetName() + " Team's Bomb");
            }
            else
            {
                _hologram.setText(text.toArray(new String[0]));
            }
        }
    }

}
