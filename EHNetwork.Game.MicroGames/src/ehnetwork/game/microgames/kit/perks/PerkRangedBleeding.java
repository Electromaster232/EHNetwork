package ehnetwork.game.microgames.kit.perks;

import java.util.Iterator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkRangedBleeding extends Perk
{

    public PerkRangedBleeding()
    {
        super("Bleeding", new String[]
            {
                    "After being hit by a arrow", "You will bleed for a few seconds"
            });
    }

    @EventHandler
    public void onSecond(UpdateEvent event)
    {
        if (event.getType() != UpdateType.FAST)
        {
            return;
        }

        Iterator<LivingEntity> itel = _timeBleeding.keySet().iterator();
        while (itel.hasNext())
        {
            LivingEntity entity = itel.next();
            if (entity.isDead() || (entity instanceof Player && !Manager.IsAlive((Player) entity)))
            {
                itel.remove();
                continue;
            }
            Manager.GetDamage().NewDamageEvent(entity, null, null, DamageCause.CUSTOM, 1, false, true, true, "Bleed",
                    "Stitcher Bleeding");
            // TODO Bleed particles?
            if (_timeBleeding.get(entity) <= 1)
            {
                itel.remove();
            }
            else
            {
                _timeBleeding.put(entity, _timeBleeding.get(entity) - 1);
            }
        }
    }

    private NautHashMap<LivingEntity, Integer> _timeBleeding = new NautHashMap<LivingEntity, Integer>();

    @EventHandler(ignoreCancelled = true)
    public void onDamage(CustomDamageEvent event)
    {
        if (event.GetCause() == DamageCause.PROJECTILE)
        {
            Player player = event.GetDamagerPlayer(true);
            if (player != null && Kit.HasKit(player))
            {
                LivingEntity entity = event.GetDamageeEntity();
                if (!_timeBleeding.containsKey(entity))
                {
                    _timeBleeding.put(entity, 4);
                }
                else
                {
                    _timeBleeding.put(entity, _timeBleeding.get(entity) + 4);
                }
            }
        }
    }

}
