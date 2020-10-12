package ehnetwork.game.microgames.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;

public class PerkRopedArrow extends SmashPerk
{
	private HashSet<Entity> _arrows = new HashSet<Entity>();

	private String _name;
	private double _power;
	private long _recharge;

	public PerkRopedArrow(String name, double power, long recharge) 
	{
		super(name, new String[] 
				{
				C.cYellow + "Left-Click" + C.cGray + " with Bow to " + C.cGreen + name
				});

		_name = name;
		_power = power;
		_recharge = recharge;
	}

	@EventHandler
	public void Fire(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (event.getPlayer().getItemInHand().getType() != Material.BOW)
			return;

		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, _name, _recharge, true, true))
			return;

		//Arrow
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setVelocity(player.getLocation().getDirection().multiply(2.4 * _power));
		_arrows.add(arrow);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You fired " + F.skill(_name) + "."));	
	}

	@EventHandler
	public void Hit(ProjectileHitEvent event)
	{
		if (!_arrows.remove(event.getEntity()))
			return;

		Projectile proj = event.getEntity();

		if (proj.getShooter() == null)
			return;

		if (!(proj.getShooter() instanceof Player))
			return;

		Vector vec = UtilAlg.getTrajectory((LivingEntity)proj.getShooter(), proj);
		double mult = (proj.getVelocity().length() / 3d);

		//Action
		UtilAction.velocity((LivingEntity)proj.getShooter(), vec, 
				0.4 + mult * _power, false, 0, 0.6 * mult * _power, 1.2 * mult * _power, true);

		//Effect
		proj.getWorld().playSound(proj.getLocation(), Sound.ARROW_HIT, 2.5f, 0.5f);
	}

	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Iterator<Entity> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();) 
		{
			Entity arrow = arrowIterator.next();

			if (!arrow.isValid())
				arrowIterator.remove();
		}
	}
}
