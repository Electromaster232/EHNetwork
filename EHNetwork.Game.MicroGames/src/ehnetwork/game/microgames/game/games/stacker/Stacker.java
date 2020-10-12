package ehnetwork.game.microgames.game.games.stacker;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.SoloGame;
import ehnetwork.game.microgames.game.games.stacker.kits.KitDefault;
import ehnetwork.game.microgames.kit.Kit;

public class Stacker extends SoloGame implements IThrown
{
	private HashSet<Entity> _tempStackShift = new HashSet<Entity>();

	public Stacker(MicroGamesManager manager)
	{
		super(manager, GameType.Stacker,

				new Kit[] 
						{ 
				new KitDefault(manager)
						},

						new String[]
								{
						"Right-Click animals to stack them.",
						"Left-Click to throw an animal from your stack.",
						"Players lose 5 stacked animals if they get hit.",
						"First to stack 16 high wins!"
								});
	}
	
	@EventHandler
	public void SpawnFood(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		Location loc = GetTeamList().get(0).GetSpawns().get(UtilMath.r(GetTeamList().get(0).GetSpawns().size()));

		//Spawn
		this.CreatureAllowOverride = true;
		Pig pig = loc.getWorld().spawn(loc, Pig.class);
		this.CreatureAllowOverride = false;
	}
	
	@EventHandler
	public void GrabEntity(PlayerInteractEntityEvent event)
	{
		if (event.isCancelled())
			return;

		Player stacker = event.getPlayer();

		Entity stackee = event.getRightClicked();
		if (stackee == null)
			return;

		if (!(stackee instanceof LivingEntity))
			return;

		if (stackee instanceof Horse)
			return;

		if (stackee instanceof Player)
			return;

		while (stackee.getVehicle() != null)
			stackee = stackee.getVehicle();

		if (stackee.equals(stacker))
			return;

		Entity top = stacker;
		while (top.getPassenger() != null)
			top = top.getPassenger();

		if (!Recharge.Instance.use(stacker, "Stacker", 250, false, false))
			return;

		top.setPassenger(stackee);

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

		thrower.eject();

		Entity throweeStack = throwee.getPassenger();
		if (throweeStack != null)
		{
			throwee.eject();
			throweeStack.leaveVehicle();

			final Entity fThrower = thrower;
			final Entity fThroweeStack = throweeStack;

			_tempStackShift.add(throweeStack);

			Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					fThrower.setPassenger(fThroweeStack);
					_tempStackShift.remove(fThroweeStack);
				}
			}, 2);
		}

		UtilAction.velocity(throwee, thrower.getLocation().getDirection(), 1.8, false, 0, 0.3, 2, false);

		Manager.GetProjectile().AddThrow(throwee, thrower, this, -1, true, false, true, false, 1f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target == null)
			return;
		
		Entity hit = target;
		
		// Shuffle Down
		while (hit.getVehicle() != null)
			hit = hit.getVehicle();

		//Hit Own Stack > Rethrow
		if (hit.equals(data.GetThrower()))
		{
			Manager.GetProjectile().AddThrow(data.GetThrown(), data.GetThrower(), this, -1, true, false, true, false, 1f);
			return;
		}
		
		//Velocity on base component
		UtilAction.velocity(hit, UtilAlg.getTrajectory2d(data.GetThrown(), target), 1, true, 0.8, 0, 10, true);

		//Shuffle Up
		Entity top = target;
		while (top.getPassenger() != null)
			top = top.getPassenger();

		Entity rider = target.getPassenger();
		while (rider != null)
		{
			rider.leaveVehicle();
			rider.setVelocity(new Vector(0.5 - Math.random(), Math.random()/2, 5 - Math.random()));
			rider = rider.getPassenger();
		}

		UtilPlayer.message(target, F.main("Game", F.name(UtilEnt.getName(data.GetThrower())) + " hit you with " + F.name(UtilEnt.getName(data.GetThrown()))));

		//Effect
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.HURT_FLESH, 1f, 1f);
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
