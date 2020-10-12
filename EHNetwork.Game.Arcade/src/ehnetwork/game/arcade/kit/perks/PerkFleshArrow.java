package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkFleshArrow extends SmashPerk
{
	private HashSet<Entity> _arrows = new HashSet<Entity>();

	public PerkFleshArrow() 
	{
		super("Flesh Arrow", new String[] 
				{
				C.cYellow + "Left-Click" + C.cGray + " with Bow to " + C.cGreen + "Flesh Arrow"
				});
	}

	@EventHandler
	public void fire(PlayerInteractEvent event)
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

		if (!Recharge.Instance.use(player, GetName(), 8000, true, true))
			return;

		//Arrow
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setVelocity(player.getLocation().getDirection().multiply(3));
		_arrows.add(arrow);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You fired " + F.skill(GetName()) + "."));	
	}

	@EventHandler
	public void hit(ProjectileHitEvent event)
	{
		if (!_arrows.remove(event.getEntity()))
			return;

		System.out.println("Flesh Arrow A");
		
		event.getEntity().remove();
	}
	
	@EventHandler
	public void damage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;
		
		if (!_arrows.contains(event.GetProjectile()))
			return;
		
		LivingEntity ent = event.GetDamageeEntity();
		
		Manager.GetCondition().Factory().Slow(GetName(), ent, event.GetDamagerEntity(true), 4, 3, false, false, false, false);
		
		ent.setVelocity(new Vector(0,-0.5,0));
	}

	@EventHandler
	public void clean(UpdateEvent event)
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
