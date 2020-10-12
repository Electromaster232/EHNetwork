package ehnetwork.core.gadget.gadgets;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFallingSand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ItemFootball extends ItemGadget
{
	private HashSet<Bat> _active = new HashSet<Bat>();

	public ItemFootball(GadgetManager manager)
	{
		super(manager, "Football", new String[] 
				{
				C.cWhite + "An amazing souvenier from the",
				C.cWhite + "Mineplex World Cup in 2053!",
				},
				-1,
				Material.CLAY_BALL, (byte)3,
				1000, new Ammo("Melon Launcher", "10 Footballs", Material.CLAY_BALL, (byte)0, new String[] { C.cWhite + "10 Footballs to play with" }, 1000, 10));
	}

	@Override
	public void ActivateCustom(Player player)
	{
		//Action
		FallingBlock ball = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 1, 0), Material.SKULL, (byte) 3);

		Bat bat = player.getWorld().spawn(player.getLocation(), Bat.class);
		UtilEnt.Vegetate(bat);
		UtilEnt.ghost(bat, true, true);
		UtilEnt.silence(bat, true);
		
		bat.setPassenger(ball);
		
		_active.add(bat);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}

	@EventHandler
	public void Collide(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Bat ball : _active)
		{
			if (ball.getPassenger() != null)
			{
				((CraftFallingSand)ball.getPassenger()).getHandle().ticksLived = 1;
				ball.getPassenger().setTicksLived(1);
			}
			
			for (Player other : UtilServer.getPlayers())
			{				
				if (UtilMath.offset(ball, other) > 1.5)
					continue;
				
				if (!Recharge.Instance.use(other, GetName() + " Bump", 200, false, false))
					continue;
				
				
				double power = 0.4;
				if (other.isSprinting())
					power = 0.7;

				//Velocity
				UtilAction.velocity(ball, UtilAlg.getTrajectory2d(other, ball), power, false, 0, 0, 0, false);
				
				other.getWorld().playSound(other.getLocation(), Sound.ITEM_PICKUP, 0.2f, 0.2f);	
			}
		}
	}
	
	@EventHandler
	public void Snort(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;
		
		for (Bat ball : _active)
		{
			if (UtilMath.offset(ball, player) > 2)
				continue;
			
			if (!Recharge.Instance.use(player, GetName() + " Kick", 1000, false, false))
				return;
			
			Recharge.Instance.useForce(player, GetName() + " Bump", 1000);
			
			//Velocity
			UtilAction.velocity(ball, UtilAlg.getTrajectory2d(player, ball), 2, false, 0, 0, 0, false);
			
			player.getWorld().playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 0.1f);	
		}
	}
}
