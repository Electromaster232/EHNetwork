package ehnetwork.game.arcade.kit.perks;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkWitherForm extends SmashPerk
{
	public PerkWitherForm() 
	{
		super("Wither Form", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{
		Manager.GetDisguise().undisguise(player);

		//Disguise
		MobDisguise disguise = (MobDisguise) Manager.GetDisguise().createDisguise(EntityType.WITHER);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setCustomName(player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}

	@Override
	public void removeSuperCustom(Player player)
	{
		Manager.GetDisguise().undisguise(player);
		
		//Disguise
		MobDisguise disguise = (MobDisguise) Manager.GetDisguise().createDisguise(EntityType.SKELETON);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setCustomName(player.getName());

		disguise.setCustomNameVisible(true);
		//disguise.SetSkeletonType(SkeletonType.WITHER);
		//disguise.hideArmor();
		Manager.GetDisguise().applyDisguise(disguise, player);
		
		player.setFlying(false);
	}

	@EventHandler
	public void witherBump(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : ((SmashKit)Kit).getSuperActive())
		{
			ArrayList<Location> collisions = new ArrayList<Location>();	

			//Bump
			for (Block block : UtilBlock.getInRadius(player.getLocation().add(0, 0.5, 0), 1.5d).keySet())
			{
				if (!UtilBlock.airFoliage(block))
				{
					collisions.add(block.getLocation().add(0.5, 0.5, 0.5));
				}
			}

			Vector vec = UtilAlg.getAverageBump(player.getLocation(), collisions);

			if (vec == null)
				continue;

			UtilAction.velocity(player, vec, 0.6, false, 0, 0.4, 10, true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void witherMeleeCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;

		if (!isSuperActive(player))
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		event.SetCancelled("Wither Form Melee Cancel");
	}

	@EventHandler
	public void witherFlight(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : ((SmashKit)Kit).getSuperActive())
		{
			if (player.isFlying())
				continue;

			player.setAllowFlight(true);
			player.setFlying(true);
		}
	}
}
