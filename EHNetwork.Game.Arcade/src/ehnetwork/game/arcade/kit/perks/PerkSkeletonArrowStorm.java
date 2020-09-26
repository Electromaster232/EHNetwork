package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.SmashPerk;

public class PerkSkeletonArrowStorm extends SmashPerk
{
    private HashSet<Projectile> _arrows = new HashSet<Projectile>();

    public PerkSkeletonArrowStorm()
    {
        super("Arrow Storm", new String[]
            {
                
            }, false);
    }

    @EventHandler
    public void fireArrows(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        for (Player cur : ((SmashKit)Kit).getSuperActive())
        {
            Vector random = new Vector((Math.random() - 0.5) / 5, (Math.random() - 0.5) / 5, (Math.random() - 0.5) / 5);
            Projectile arrow = cur.launchProjectile(Arrow.class);
            arrow.setVelocity(cur.getLocation().getDirection().add(random).multiply(3));
            _arrows.add(arrow);
            cur.getWorld().playSound(cur.getLocation(), Sound.SHOOT_ARROW, 1f, 1f);
        }
    }
    
    @EventHandler
    public void projectileHit(ProjectileHitEvent event)
    {
         if (_arrows.remove(event.getEntity()))
        	 event.getEntity().remove();
    }

    @EventHandler
    public void clean(UpdateEvent event)
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
}
