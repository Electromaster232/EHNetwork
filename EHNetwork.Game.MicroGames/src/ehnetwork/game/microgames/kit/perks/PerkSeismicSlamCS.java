package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.microgames.kit.Perk;

public class PerkSeismicSlamCS extends Perk
{	
	public PerkSeismicSlamCS() 
	{
		super("Ground Pound", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Ground Pound"
				});
	}

	@EventHandler
	public void Leap(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return;

		//Action
		double range = 6;

		HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(player.getLocation(), range);
		for (LivingEntity cur : targets.keySet())
		{
			if (!(cur instanceof Player))
				return;

			if (cur.equals(player))
				continue;

			Player other = (Player)cur;

			if (!Manager.GetGame().IsAlive(other))
				continue;

			if (Manager.GetGame().GetTeam(other).equals(Manager.GetGame().GetTeam(player)))
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent(cur, player, null, 
					DamageCause.CUSTOM, 8 * targets.get(cur) + 1, false, true, false,
					player.getName(), GetName());	

			//Velocity
			UtilAction.velocity(cur, 
					UtilAlg.getTrajectory2d(player.getLocation().toVector(), cur.getLocation().toVector()), 
					1.8 * targets.get(cur), true, 0, 0.4 + 1.0 * targets.get(cur), 1.6, true);

			//Condition
			Manager.GetCondition().Factory().Falling(GetName(), cur, player, 10, false, true);

			//Inform
			if (cur instanceof Player)
				UtilPlayer.message((Player)cur, F.main("Game", F.name(player.getName()) +" hit you with " + F.skill(GetName()) + "."));	
		}

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_WOOD, 2f, 0.2f);
		for (Block cur : UtilBlock.getInRadius(player.getLocation(), 4d).keySet())
			if (UtilBlock.airFoliage(cur.getRelative(BlockFace.UP)) && !UtilBlock.airFoliage(cur))
				cur.getWorld().playEffect(cur.getLocation(), Effect.STEP_SOUND, cur.getTypeId());

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
	}
}
