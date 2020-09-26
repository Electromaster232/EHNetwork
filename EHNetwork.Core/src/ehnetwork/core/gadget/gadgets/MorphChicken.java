package ehnetwork.core.gadget.gadgets;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.disguise.disguises.DisguiseChicken;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MorphChicken extends MorphGadget
{
	public MorphChicken(GadgetManager manager)
	{
		super(manager, "Chicken Morph", new String[] 
				{
				C.cWhite + "Soar through the air like a fat Chicken!",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Egg Shot",
				C.cYellow + "Double Jump" + C.cGray + " to use " + C.cGreen + "Flap",
				},
				20000,
				Material.FEATHER, (byte)0);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguiseChicken disguise = new DisguiseChicken(player);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		disguise.setCustomNameVisible(true);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
		
		player.setAllowFlight(false);
		player.setFlying(false);
	}

	@EventHandler
	public void Egg(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 100, false, false))
			return;
		
		Vector offset = player.getLocation().getDirection();
		if (offset.getY() < 0)
			offset.setY(0);

		Egg egg = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0).add(offset), Egg.class);
		egg.setVelocity(player.getLocation().getDirection().add(new Vector(0,0.2,0)));
		egg.setShooter(player);
		 
		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 0.5f, 1f);
	}
	
	@EventHandler
	public void Flap(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();
		
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		
		if (!IsActive(player))
			return;
		
		event.setCancelled(true);
		player.setFlying(false);
		
		//Disable Flight
		player.setAllowFlight(false);
		
		double power = 0.4 + (0.5 * player.getExp());
		
		//Velocity
		UtilAction.velocity(player, player.getLocation().getDirection(), power, true, power, 0, 10, true);
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, (float)(0.3 + player.getExp()), (float)(Math.random()/2+1));
		
		//Set Recharge
		Recharge.Instance.use(player, GetName(), 80, false, false);
		
		//Energy
		player.setExp(Math.max(0f, player.getExp() - (1f/9f)));
	}
	
	@EventHandler
	public void FlapUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetActive())
		{
			if (player.getGameMode() == GameMode.CREATIVE)
				continue;

			if (UtilEnt.isGrounded(player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN)))
			{
				player.setExp(0.999f);
				player.setAllowFlight(true);
			}
			else if (Recharge.Instance.usable(player, GetName()) && player.getExp() > 0)
			{
				player.setAllowFlight(true);
			}
		}
	}
	
	@EventHandler
	public void EggHit(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Egg)
		{
			event.getEntity().setVelocity(new Vector(0,0,0));
		}
	}
}
