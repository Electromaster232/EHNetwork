package ehnetwork.game.microgames.game.games.searchanddestroy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.TeamGame;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.minecraft.game.core.combat.CombatComponent;
import ehnetwork.minecraft.game.core.combat.CombatManager.AttackReason;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class SearchAndDestroy extends TeamGame
{

    private KitEvolve _evolve;
    private HashMap<String, Float> _kills = new HashMap<String, Float>();
    private HashMap<String, Integer> _evolves = new HashMap<String, Integer>();
    private KitManager _kitManager = new KitManager(this);
    private ArrayList<TeamBomb> _teamBombs = new ArrayList<TeamBomb>();

    public SearchAndDestroy(MicroGamesManager manager)
    {
        super(manager, GameType.SearchAndDestroy, new Kit[0], new String[]
            {
                    "Detonate the enemies TNT", 
                    "No respawns",
                    "Last team alive wins!"
            });

        setKits(_kitManager.get_kits());

        _evolve = new KitEvolve(manager.getPlugin(), this, _kitManager.getUpgradeKits());
        InventoryClick = true;
        WorldTimeSet = -1;
        WorldBoundaryKill = false;
        HungerSet = 20;
        AnnounceJoinQuit = false;
        DisableKillCommand = false;
        AllowParticles = false;
        DamageSelf = false;
        
        Manager.GetDamage().GetCombatManager().setUseWeaponName(AttackReason.DefaultWeaponName);
    }

    public ArrayList<TeamBomb> getBombs()
    {
        return _teamBombs;
    }

    public boolean canEvolve(Player player)
    {
        return (!_evolves.containsKey(player.getName()) || _evolves.get(player.getName()) < 2)
        		&& _kills.containsKey(player.getName()) && _kills.get(player.getName()) >= 2;
    }

    public void onEvolve(Player player)
    {
        final int newTier = (_evolves.containsKey(player.getName()) ? _evolves.get(player.getName()) : 0) + 1;

        player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1.3F, 0);
        final Location center = player.getLocation();
        new BukkitRunnable()
        {
            double y = 2.2;
            double rotAngle;

            public void run()
            {
                if (y <= 0)
                {
                    cancel();
                }
                else
                {
                    for (int i = 0; i < 8; i++)
                    {
                        for (int a = 0; a < 2; a++)
                        {
                            double angle = ((2 * Math.PI) / 360D) * ((rotAngle + ((360D / 2) * a)) % 360);
                            double x = 0.9 * Math.cos(angle);
                            double z = 0.9 * Math.sin(angle);
                            Location l = center.clone().add(x, y, z);
                            l.getWorld().spigot()
                                    .playEffect(l, newTier == 2 ? Effect.FLAME : Effect.WATERDRIP, 0, 0, 0, 0, 0, 0, 1, 30);
                        }
                        y -= 0.008;
                        rotAngle += 8;
                    }
                }
            }
        }.runTaskTimer(Manager.getPlugin(), 0, 0);
        
        _evolves.put(player.getName(), newTier);
        
        if (_kills.containsKey(player.getName()))
	       _kills.put(player.getName(), _kills.get(player.getName()) - 2);
    }

    @EventHandler
    public void onBombsPacketListener(GameStateChangeEvent event)
    {
        if (event.GetState() == GameState.Prepare)
        {
            for (TeamBomb bomb : getBombs())
            {
                bomb.setupHologram();
            }
        }
    }

    @EventHandler
    public void onBombTick(UpdateEvent event)
    {
        if (this.GetState() != GameState.Live)
        {
            return;
        }
        if (event.getType() == UpdateType.FASTEST)
        {
            for (TeamBomb bomb : getBombs())
            {
                bomb.updateBombHologram();
            }
        }
        if (event.getType() == UpdateType.SEC)
        {
            boolean teamDied = false;
            for (int a = 0; a < getBombs().size(); a++)
            {
                TeamBomb bomb = getBombs().get(a);
                if (!bomb.getTeam().IsTeamAlive())
                {
                    bomb.removeBomb();
                    getBombs().remove(a--);
                    continue;
                }
                bomb.checkArmers();
                bomb.tickBomb();
                GameTeam bombOwner = bomb.getTeam();
                if (bomb.isArmed())
                {
                    int timeLeft = bomb.getTimeUntilExplode();
                    if (((timeLeft <= 30 && timeLeft % 10 == 0) || (timeLeft <= 5)) && timeLeft >= 1)
                    {
                        Bukkit.broadcastMessage(C.cGold + "" + timeLeft + " seconds left until " + bombOwner.GetColor()
                                + bombOwner.GetName() + " Team's" + C.cGold + " bomb goes off!");
                        for (Player player : bombOwner.GetPlayers(true))
                        {
                            player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 30, 3);
                        }
                    }
                    else if (timeLeft == 0)
                    {
                        teamDied = true;
                        // It exploded
                        getBombs().remove(a--);
                        bomb.getBlockLocation().getWorld().playSound(bomb.getBlockLocation(), Sound.EXPLODE, 1000, 0);
                        bomb.getBlockLocation().getWorld().playEffect(bomb.getBomb().getLocation(), Effect.EXPLOSION_HUGE, 0);
                        bomb.removeBomb();
                        DeathMessages = false;
                        for (Player player : this.GetPlayers(true))
                        {
                            GameTeam pTeam = GetTeam(player);
                            if (bombOwner == pTeam)
                            {
                                Manager.GetDamage().NewDamageEvent(player, null, null, DamageCause.CUSTOM, 999, false, true,
                                        true, "Bomb", "Bomb");
                                if (IsAlive(player))
                                {
                                    Manager.addSpectator(player, true);
                                }
                            }
                        }
                        DeathMessages = true;

                        // TODO The code below could be used when a team is defeated.
                        /*       if (getBombs().size() == 1)
                               {
                                   GameTeam winning = getBombs().get(0).getTeam();
                                   Bukkit.broadcastMessage(bombOwner.GetColor() + bombOwner.GetName() + " Team's" + ChatColor.RESET
                                           + ChatColor.GOLD + " bomb exploded! " + winning.GetColor() + winning.GetName()
                                           + ChatColor.RESET + ChatColor.GOLD + " wins!");
                               }
                               else
                               {
                                   Bukkit.broadcastMessage(bombOwner.GetColor() + bombOwner.GetName() + " Team was defeated!");
                               }*/
                    }
                    if (timeLeft > 0)
                    {
                        int b = timeLeft % 3;
                        if (b == 1)
                        {
                            bomb.respawnBomb();
                            /*try
                            {
                                PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(bomb.getBomb()
                                        .getEntityId());
                                final EntityTrackerEntry entityTrackerEntry = (EntityTrackerEntry) ((CraftWorld) bomb.getBomb()
                                        .getWorld()).getHandle().tracker.trackedEntities.get(bomb.getBomb().getEntityId());
                                if (entityTrackerEntry != null)
                                {
                                    HashSet trackedPlayers = (HashSet) entityTrackerEntry.trackedPlayers;
                                    HashSet<EntityPlayer> cloned = (HashSet) trackedPlayers.clone();
                                    for (final EntityPlayer p : cloned)
                                    {
                                        entityTrackerEntry.clear(p);
                                        p.playerConnection.sendPacket(destroyPacket);
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(getArcadeManager().getPlugin(),
                                                new Runnable()
                                                {
                                                    @Override
                                                    public void run()
                                                    {
                                                        try
                                                        {
                                                            entityTrackerEntry.updatePlayer(p);
                                                        }
                                                        catch (Exception ex)
                                                        {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                }, 4);
                                    }
                                }
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }*/
                        }
                    }
                }
            }
            if (teamDied)
            {
                GameTeam aliveTeam = this.getBombs().get(0).getTeam();
                for (TeamBomb bomb : getBombs())
                {
                    if (!bomb.getTeam().equals(aliveTeam))
                    {
                        aliveTeam = null;
                        break;
                    }
                }
                if (aliveTeam != null)
                {
                    this.endGame(WinnerTeam);
                }
            }
        }
    }

    @EventHandler
    public void onGameState(GameStateChangeEvent event)
    {
        if (event.GetState() == GameState.End || event.GetState() == GameState.Live || event.GetState() == GameState.Prepare)
        {
            drawScoreboard();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDeath(CombatDeathEvent event)
    {
        if (GetState() != GameState.Live)
        {
            return;
        }

        if (event.GetLog().GetKiller() == null)
            return;

        if (!event.GetLog().GetKiller().IsPlayer())
            return;

        Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
        if (player == null)
            return;
        
 
        addKill(player, 1);
        
        for (CombatComponent log : event.GetLog().GetAttackers())
        {
            if (event.GetLog().GetKiller() != null && log.equals(event.GetLog().GetKiller()))
                continue;

            Player assist = UtilPlayer.searchExact(log.GetName());

            // Assist
            if (assist != null)
                addKill(assist, 0.5F);
        }
    }

    private void addKill(Player player, float amount)
    {
        boolean canEvolve = this.canEvolve(player);
        
        this._kills.put(player.getName(), (_kills.containsKey(player.getName()) ? _kills.get(player.getName()) : 0) + 1);
        
        if (canEvolve(player) != canEvolve)
        {
            UtilTextMiddle.display(null, ChatColor.GREEN + "Evolution Available", 10, 60, 10, player);
            UtilPlayer.message(player, ChatColor.GREEN + "Evolution Available");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        }
    }

    private boolean _flickerOn;

    @EventHandler
    public void onUpdate(UpdateEvent event)
    {
        if (GetState() != GameState.Live || event.getType() != UpdateType.FAST)
        {
            return;
        }

        _flickerOn = !_flickerOn;
        for (Player player : this.GetPlayers(true))
        {
            if (canEvolve(player))
            {
                for (int slot = 0; slot < player.getInventory().getSize(); slot++)
                {
                    ItemStack item = player.getInventory().getItem(slot);
                    if (item != null && item.getType() == Material.LEATHER_CHESTPLATE && item.getItemMeta().hasDisplayName()
                            && item.getItemMeta().getDisplayName().contains("Evolve"))
                    {
                        String message = C.Bold + "Evolve Kit!";
                        if (_flickerOn)
                        {
                            UtilInv.addDullEnchantment(item);
                            message = C.cDGreen + message;
                        }
                        else
                        {
                            UtilInv.removeDullEnchantment(item);
                            message = C.cBlue + message;
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(message);
                        ((LeatherArmorMeta) meta).setColor(_flickerOn ? Color.WHITE : GetTeam(player).GetColorBase());
                        item.setItemMeta(meta);
                    }
                }
            }
        }
    }

    private void drawScoreboard()
    {
        GetScoreboard().Reset();

        GetScoreboard().WriteBlank();
        ArrayList<GameTeam> aliveTeams = new ArrayList<GameTeam>();
        for (GameTeam team : GetTeamList())
        {
            if (team.IsTeamAlive())
            {
                aliveTeams.add(team);
            }
        }

        Iterator<GameTeam> itel = aliveTeams.iterator();

        ArrayList<TeamBomb> bombs = new ArrayList<TeamBomb>(this._teamBombs);

        while (itel.hasNext())
        {
            GameTeam team = itel.next();
            if (!team.IsTeamAlive())
                continue;
            GetScoreboard().Write(team.GetColor() + C.Bold + team.GetName() + " Team");
            GetScoreboard().Write(team.GetPlayers(true).size() + " alive");
            int bombsArmed = 0;

            for (TeamBomb bomb : bombs)
            {
                if (bomb.getTeam() == team && bomb.isArmed())
                {
                    bombsArmed++;
                }
            }
            if (bombsArmed > 0)
            {
                GetScoreboard().Write(team.GetColor() + "" + bombsArmed + " Bomb" + (bombsArmed > 1 ? "s" : "") + " Armed");
            }
            else
            {
                GetScoreboard().Write("Bombs Safe");
            }

            if (itel.hasNext())
            {
                GetScoreboard().WriteBlank();
            }
        }

        GetScoreboard().Draw();
    }

    @Override
    @EventHandler
    public void ScoreboardUpdate(UpdateEvent event)
    {
        if (event.getType() != UpdateType.FAST || this.GetState() != GameState.Live)
        {
            return;
        }

        drawScoreboard();
    }

    @EventHandler
    public void onGameEnd(GameStateChangeEvent event)
    {
        if (event.GetState() == GameState.End)
        {
            HandlerList.unregisterAll(_evolve);
            for (TeamBomb bomb : getBombs())
            {
                if (bomb.getTeam().IsTeamAlive())
                {
                    bomb.restoreBomb();
                }
                else
                {
                    bomb.removeBomb();
                }
            }
            getBombs().clear();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteractBombBlock(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getItem() != null && event.getItem().getType() == Material.BLAZE_POWDER)
            {
                for (TeamBomb bomb : this.getBombs())
                {
                    if (!bomb.isArmed() && bomb.getBlockLocation().getBlock().equals(event.getClickedBlock()))
                    {
                        GameTeam team = GetTeam(p);
                        if (team == bomb.getTeam())
                        {
                            p.sendMessage(ChatColor.RED + "That's your bomb!");
                        }
                        else
                        {
                            bomb.onInteractWithFuse(p);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteractBombEntity(PlayerInteractEntityEvent event)
    {
        Player p = event.getPlayer();
        if (event.getRightClicked() instanceof TNTPrimed)
        {
            ItemStack item = p.getItemInHand();
            if (item != null && item.getType() == Material.BLAZE_POWDER)
            {
                for (TeamBomb bomb : this.getBombs())
                {
                    if (bomb.isArmed() && bomb.getBomb().equals(event.getRightClicked()))
                    {
                        GameTeam team = GetTeam(p);
                        if (team != bomb.getTeam())
                        {
                            p.sendMessage(ChatColor.RED + "That's not your bomb!");
                        }
                        else if (team != null)
                        {
                            bomb.onInteractWithFuse(p);
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void ParseData()
    {
        for (GameTeam team : GetTeamList())
        {
            for (Location loc : WorldData.GetDataLocs(team.GetColor() == ChatColor.AQUA ? "BLUE" : team.GetColor().name()))
            {
                getBombs().add(new TeamBomb(this.Manager, this, team, loc));
                loc.getBlock().setType(Material.TNT);
            }
        }
    }

}
