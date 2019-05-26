package nautilus.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkBarrage extends SmashPerk
{
    private WeakHashMap<Player, Integer> _charge = new WeakHashMap<Player, Integer>();
    private WeakHashMap<Player, Long> _chargeLast = new WeakHashMap<Player, Long>();

    private HashSet<Player> _firing = new HashSet<Player>();
    private HashSet<Projectile> _arrows = new HashSet<Projectile>();

    private int _max;
    private long _tick;
    private boolean _remove;
    private boolean _noDelay;
    private boolean _useExp;

    public PerkBarrage(int max, long tick, boolean remove, boolean noDelay)
    {
        this(max, tick, remove, noDelay, false);
    }

    public PerkBarrage(int max, long tick, boolean remove, boolean noDelay, boolean useExpAndBar)
    {
        super("Barrage", new String[]
            {
                C.cYellow + "Charge" + C.cGray + " your Bow to use " + C.cGreen + "Barrage"
            });
        _useExp = useExpAndBar;
        _max = max;
        _tick = tick;
        _remove = remove;
        _noDelay = noDelay;
    }

    @EventHandler
    public void BarrageDrawBow(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.BOW)
            return;
        
        if (isSuperActive(player))
        	return;

        if (!Kit.HasKit(player))
            return;

        if (!player.getInventory().contains(Material.ARROW))
            return;

        if (event.getClickedBlock() != null)
            if (UtilBlock.usable(event.getClickedBlock()))
                return;

        // Start Charge
        _charge.put(player, 0);
        _chargeLast.put(player, System.currentTimeMillis());
        _firing.remove(player);
    }

    @EventHandler
    public void BarrageCharge(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        for (Player cur : UtilServer.getPlayers())
        {
            // Not Charging
            if (!_charge.containsKey(cur))
                continue;

            if (_firing.contains(cur))
                continue;

            // Max Charge
            if (_charge.get(cur) >= _max)
                continue;

            // Charge Interval
            if (_charge.get(cur) == 0)
            {
                if (!UtilTime.elapsed(_chargeLast.get(cur), 1000))
                    continue;
            }
            else
            {
                if (!UtilTime.elapsed(_chargeLast.get(cur), _tick))
                    continue;
            }

            // No Longer Holding Bow
            if (cur.getItemInHand() == null || cur.getItemInHand().getType() != Material.BOW)
            {
                if (_useExp)
                {
                	cur.setExp(0f);
                }
                _charge.remove(cur);
                _chargeLast.remove(cur);
                continue;
            }

            // Increase Charge
            _charge.put(cur, _charge.get(cur) + 1);

            if (_useExp)
            {
            	cur.setExp(Math.min(0.9999f, (float)_charge.get(cur) / (float)_max));
            }
            _chargeLast.put(cur, System.currentTimeMillis());

            // Effect
            cur.playSound(cur.getLocation(), Sound.CLICK, 1f, 1f + (0.1f * _charge.get(cur)));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void BarrageFireBow(EntityShootBowEvent event)
    {
        if (event.isCancelled())
            return;

        if (!Manager.GetGame().IsLive())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getProjectile() instanceof Arrow))
            return;

        Player player = (Player) event.getEntity();

        if (!_charge.containsKey(player))
            return;

        // Start Barrage
        _firing.add(player);
        _chargeLast.put(player, System.currentTimeMillis());
    }

    @EventHandler
    public void BarrageArrows(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        HashSet<Player> remove = new HashSet<Player>();

        for (Player cur : _firing)
        {
            if (!_charge.containsKey(cur) || !_chargeLast.containsKey(cur))
            {
                remove.add(cur);
                continue;
            }

            if (cur.getItemInHand() == null || cur.getItemInHand().getType() != Material.BOW)
            {
                remove.add(cur);
                continue;
            }

            int arrows = _charge.get(cur);
            if (arrows <= 0)
            {
                remove.add(cur);
                continue;
            }

            _charge.put(cur, arrows - 1);
            if (_useExp)
            {
            	cur.setExp(Math.min(0.9999f, (float)_charge.get(cur) / (float)_max));
            }

            // Fire Arrow
            Vector random = new Vector((Math.random() - 0.5) / 10, (Math.random() - 0.5) / 10, (Math.random() - 0.5) / 10);
            Projectile arrow = cur.launchProjectile(Arrow.class);
            arrow.setVelocity(cur.getLocation().getDirection().add(random).multiply(3));
            _arrows.add(arrow);
            cur.getWorld().playSound(cur.getLocation(), Sound.SHOOT_ARROW, 1f, 1f);
        }

        for (Player cur : remove)
        {
            if (_useExp)
            {
                cur.setExp(0f);
            }
            _charge.remove(cur);
            _chargeLast.remove(cur);
            _firing.remove(cur);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void BarrageDamageTime(CustomDamageEvent event)
    {
        if (!_noDelay)
            return;

        if (event.GetProjectile() == null)
            return;

        if (event.GetDamagerPlayer(true) == null)
            return;

        if (!(event.GetProjectile() instanceof Arrow))
            return;

        Player damager = event.GetDamagerPlayer(true);

        if (!Kit.HasKit(damager))
            return;

        event.SetCancelled("Barrage Cancel");

        event.GetProjectile().remove();

        // Damage Event
        Manager.GetDamage().NewDamageEvent(event.GetDamageeEntity(), damager, null, DamageCause.THORNS, event.GetDamage(), true,
                true, false, damager.getName(), GetName());
    }

    @EventHandler
    public void BarrageProjectileHit(ProjectileHitEvent event)
    {
        if (_remove)
            if (_arrows.remove(event.getEntity()))
                event.getEntity().remove();
    }

    @EventHandler
    public void BarrageClean(UpdateEvent event)
    {
        if (event.getType() != UpdateType.SEC)
            return;

        for (Iterator<Projectile> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();)
        {
            Projectile arrow = arrowIterator.next();

            if (arrow.isDead() || !arrow.isValid())
                arrowIterator.remove();
        }
    }

    @EventHandler
    public void Quit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        clean(player);
    }
    
    @Override
    public void addSuperCustom(Player player)
    {
    	clean(player);
    }
    
    public void clean(Player player)
    {
    	_charge.remove(player);
        _chargeLast.remove(player);
        _firing.remove(player);
    }
}
