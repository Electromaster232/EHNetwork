package mineplex.hub.modules;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.event.StackerEvent;
import mineplex.core.gadget.gadgets.MorphBlock;
import mineplex.core.gadget.types.GadgetType;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileManager;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import mineplex.hub.HubManager;

public class StackerManager extends MiniPlugin implements IThrown
{
	public HubManager Manager;

	private ProjectileManager _projectileManager;

	private HashSet<Entity> _tempStackShift = new HashSet<Entity>();
	
	public StackerManager(HubManager manager)
	{
		super("Stacker", manager.getPlugin());

		Manager = manager;

		_projectileManager = new ProjectileManager(manager.getPlugin());
	} 
	
	@EventHandler
	public void GrabEntity(PlayerInteractEntityEvent event)
	{
		if (event.isCancelled())
			return;

		Entity stackee = event.getRightClicked();
		if (stackee == null)
			return;

		if (!(stackee instanceof LivingEntity))
			return;
		
		if (stackee instanceof Horse)
			return;
		
		if (stackee instanceof EnderDragon)
			return;

		if (stackee instanceof Player && ((Player)stackee).getGameMode() != GameMode.SURVIVAL)
			return;
		
		Player stacker = event.getPlayer();

		if (stacker.getGameMode() != GameMode.SURVIVAL)
			return;
		
		if (UtilGear.isMat(stacker.getItemInHand(), Material.SNOW_BALL))
			return;
		
		StackerEvent stackerEvent = new StackerEvent(stacker);		
		Bukkit.getServer().getPluginManager().callEvent(stackerEvent);
		if (stackerEvent.isCancelled())
			return;

		//Parkour Disable
		if (Manager.GetParkour().InsideParkour(stacker.getLocation()))
		{
			UtilPlayer.message(stacker, F.main("Parkour", "You cannot Stack/Throw near Parkour Challenges."));
			return;
		}

		if (!Manager.CanBump(stacker))
		{
			UtilPlayer.message(stacker, F.main("Stacker", "You are not playing stacker."));
			return;
		}

		if (stacker.getVehicle() != null || _tempStackShift.contains(stacker))
		{
			UtilPlayer.message(stacker, F.main("Stacker", "You cannot stack while stacked..."));
			return;
		}

		if (Manager.GetGadget().getActive(stacker, GadgetType.Morph) instanceof MorphBlock)
		{
			UtilPlayer.message(stacker, F.main("Stacker", "You cannot stack while using the Block Morph."));
			return;
		}

		if (Manager.GetTreasure().isOpening(stacker))
			return;

		stackerEvent = new StackerEvent(stackee);		
		Bukkit.getServer().getPluginManager().callEvent(stackerEvent);
		if (stackerEvent.isCancelled())
			return;

		if (stackee instanceof Player)
		{
			if (!Manager.CanBump(((Player)stackee)))
			{
				UtilPlayer.message(stacker, F.main("Stacker", F.name(UtilEnt.getName(stackee)) + " is not playing stacker."));
				return;
			}

			if (Manager.GetTreasure().isOpening((Player) stackee))
			{
				UtilPlayer.message(stacker, F.main("Stacker", F.main("Stacker", F.name(UtilEnt.getName(stackee)) + " is opening a chest!")));
				return;
			}
		}		
		
		if (stackee instanceof LivingEntity)
		{
			if (Manager.getPetManager().getPets().contains(stackee) || stackee instanceof Wither || stackee instanceof EnderDragon || ((LivingEntity)stackee).isCustomNameVisible())
			{
				UtilPlayer.message(stacker, F.main("Stacker", "You cannot stack this entity."));
				return;
			}
		}

		while (stackee.getVehicle() != null)
			stackee = stackee.getVehicle();

		if (stackee.equals(stacker))
			return;

		Entity top = stacker;
		while (top.getPassenger() != null)
			top = top.getPassenger();

		if (!Recharge.Instance.use(stacker, "Stacker", 500, true, false))
			return;

		top.setPassenger(stackee);

		UtilPlayer.message(stacker, F.main("Stacker", "You stacked " + F.name(UtilEnt.getName(stackee) + ".")));
		UtilPlayer.message(stackee, F.main("Stacker", "You were stacked by " + F.name(stacker.getName() + ".")));
		UtilPlayer.message(stackee, F.main("Stacker", "Push " + F.skill("Crouch") + " to escape!"));

		//Portal Delay
		Manager.SetPortalDelay(stacker);
		Manager.SetPortalDelay(stackee);

		event.setCancelled(true);
	}

	@EventHandler
	public void ThrowEntity(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		Player thrower = event.getPlayer();

		if (thrower.getVehicle() != null)
			return;

		Entity throwee = thrower.getPassenger();
		if (throwee == null)
			return;
		
		StackerEvent stackerEvent = new StackerEvent(thrower);		
		Bukkit.getServer().getPluginManager().callEvent(stackerEvent);
		if (stackerEvent.isCancelled())
			return;

		thrower.eject();

		Entity throweeStack = throwee.getPassenger();
		if (throweeStack != null)
		{
			throwee.eject();
			throweeStack.leaveVehicle();

			final Entity fThrower = thrower;
			final Entity fThroweeStack = throweeStack;

			_tempStackShift.add(throweeStack);

			getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
			{
				public void run()
				{
					fThrower.setPassenger(fThroweeStack);
					_tempStackShift.remove(fThroweeStack);
				}
			}, 2);
		}

		//Parkour Disable
		if (Manager.GetParkour().InsideParkour(thrower.getLocation()))
		{
			UtilPlayer.message(thrower, F.main("Parkour", "You cannot Stack/Throw near Parkour Challenges."));
			return;
		}

		UtilPlayer.message(thrower, F.main("Stacker", "You threw " + F.name(UtilEnt.getName(throwee))));
		UtilPlayer.message(throwee, F.main("Stacker", "You were thrown by " + F.name(thrower.getName())));

		UtilAction.velocity(throwee, thrower.getLocation().getDirection(), 1.8, false, 0, 0.3, 2, false);

		_projectileManager.AddThrow(throwee, thrower, this, -1, true, false, true, false, 0.5f);

		//Portal Delay
		Manager.SetPortalDelay(thrower);
		Manager.SetPortalDelay(throwee);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target == null)
			return;
		
		if (target.getCustomName() != null || (target.getPassenger() != null && target.getPassenger() instanceof LivingEntity && ((LivingEntity)target.getPassenger()).getCustomName() != null))
			return;
		
		if (!Manager.CanBump(target))
			return;

		//Velocity
		UtilAction.velocity(target, UtilAlg.getTrajectory2d(data.GetThrown(), target), 1, true, 0.8, 0, 10, true);

		Entity rider = target.getPassenger();
		while (rider != null)
		{
			//Portal Delay
			Manager.SetPortalDelay(rider);
			
			rider.leaveVehicle();
			rider.setVelocity(new Vector(0.25 - Math.random()/2, Math.random()/2, 0.25 - Math.random()/2));
			rider = rider.getPassenger();
		}

		UtilPlayer.message(target, F.main("Stacker", F.name(UtilEnt.getName(data.GetThrower())) + " hit you with " + F.name(UtilEnt.getName(data.GetThrown()))));

		//Effect
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.HURT_FLESH, 1f, 1f);
		
		//Portal Delay
		Manager.SetPortalDelay(target);
	}

	@Override
	public void Idle(ProjectileUser data) 
	{

	}

	@Override
	public void Expire(ProjectileUser data) 
	{

	}
}
