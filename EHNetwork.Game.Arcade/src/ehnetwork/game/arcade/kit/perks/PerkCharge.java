package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkCharge extends Perk
{
	public PerkCharge() 
	{
		super("Cow Charge", new String[] 
				{ 
				C.cYellow + "Sprint" + C.cGray + " to use " + C.cGreen + "Cow Charge"
				});
	}

	@EventHandler
	public void Damage(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!player.isSprinting())
				continue;

			if (!Kit.HasKit(player))
				continue;
			
			player.getWorld().playSound(player.getLocation(), Sound.COW_WALK, 0.8f, 1f);
			
			Manager.GetCondition().Factory().Speed(GetName(), player, player, 0.9, 2, false, false, false);

			HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(player.getLocation().add(player.getLocation().getDirection().setY(0).normalize()), 2);
			for (LivingEntity cur : targets.keySet())
			{
				if (cur.equals(player))
					continue;
				
				if (cur instanceof Cow)
					continue;

				Vector dir = UtilAlg.getTrajectory2d(player.getLocation().toVector(), cur.getLocation().toVector());
				dir.add(player.getLocation().getDirection().setY(0).normalize());
				dir.setY(0);
				dir.normalize();

				//Damage Event
				Manager.GetDamage().NewDamageEvent(cur, player, null, 
						DamageCause.CUSTOM, 3, false, false, false,
						player.getName(), GetName());	

				//Velocity
				UtilAction.velocity(cur, dir, 1 + 0.8 * targets.get(cur), true, 0, 0.8 + 0.4 * targets.get(cur), 1.6, true);

				//Condition
				Manager.GetCondition().Factory().Falling(GetName(), cur, player, 10, false, true);

				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_WOOD, 0.75f, 1f);
			}
		}
	}

	@EventHandler
	public void Charge(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;

			if (player.isSprinting())
			{
				UtilPlayer.hunger(player, -1);
				
				if (player.getFoodLevel() <= 0)
				{
					player.setSprinting(false);
				}
			}
			else
			{
				UtilPlayer.hunger(player, 1);
			}
		}
	}
}
