package ehnetwork.game.microgames.kit.perks;

import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkOvercharge extends SmashPerk
{
    private WeakHashMap<Player, Integer> _charge = new WeakHashMap<Player, Integer>();
    private WeakHashMap<Player, Long> _chargeLast = new WeakHashMap<Player, Long>();
    
    private WeakHashMap<Arrow, Integer> _arrows = new WeakHashMap<Arrow, Integer>();

    private int _max;
    private long _tick;
    private boolean _useExp;

    public PerkOvercharge(int max, long tick, boolean useExpBar)
    {
        super("Corrupted Arrow", new String[]
            {
                C.cYellow + "Charge" + C.cGray + " your Bow to use " + C.cGreen + "Corrupted Arrow"
            });
      
        _useExp = useExpBar;
        _max = max;
        _tick = tick;
    }

    @EventHandler
    public void drawBow(PlayerInteractEvent event)
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
    }

    @EventHandler
    public void charge(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        for (Player cur : UtilServer.getPlayers())
        {
            // Not Charging
            if (!_charge.containsKey(cur))
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
                	cur.setExp(0f);
                
                _charge.remove(cur);
                _chargeLast.remove(cur);
                continue;
            }

            // Increase Charge
            _charge.put(cur, _charge.get(cur) + 1);

            if (_useExp)
            	cur.setExp(Math.min(0.9999f, (float)_charge.get(cur) / (float)_max));
            
            _chargeLast.put(cur, System.currentTimeMillis());

            // Effect
            cur.playSound(cur.getLocation(), Sound.CLICK, 1f, 1f + (0.1f * _charge.get(cur)));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void fireBow(EntityShootBowEvent event)
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
        
        int charge = _charge.remove(player);
        if (charge <= 0)
        	return;

        // Start Barrage
        _arrows.put((Arrow)event.getProjectile(), charge);
        
        player.setExp(0f);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void damageBonus(CustomDamageEvent event)
    {
        if (event.GetProjectile() == null)
            return;

        if (!_arrows.containsKey(event.GetProjectile()))
        	return;
        
        int charge = _arrows.remove(event.GetProjectile());

        event.AddMod(GetName(), GetName(), charge, true);
    }

    @EventHandler
    public void clean(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        for (Iterator<Arrow> arrowIterator = _arrows.keySet().iterator(); arrowIterator.hasNext();)
        {
        	Arrow arrow = arrowIterator.next();

            if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround() || arrow.getTicksLived() > 120)
                arrowIterator.remove();
            else
            	UtilParticle.PlayParticle(ParticleType.RED_DUST, arrow.getLocation(), 0, 0, 0, 0, 1,
						ViewDist.MAX, UtilServer.getPlayers());
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        _charge.remove(player);
        _chargeLast.remove(player);
    }
}
