package ehnetwork.hub.modules;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.hub.HubManager;
import ehnetwork.hub.HubType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class WorldManager extends MiniPlugin
{
	public HubManager Manager;

	private HashSet<LivingEntity> _mobs = new HashSet<LivingEntity>();
	
	private boolean _christmasSnow = false;
	private long _christSnowTime = 0;

	public WorldManager(HubManager manager)
	{
		super("World Manager", manager.getPlugin());

		Manager = manager;
		
		//Added by TheMineBench, to stop day cycles instead of setting the time on update event.
		World world = UtilWorld.getWorld("world");
		
		world.setGameRuleValue("doDaylightCycle", "false");
		
		if (Manager.Type == HubType.Halloween)
			world.setTime(16000);
		else									
			world.setTime(6000);
		world.setStorm(false);
		world.setThundering(false);
	}

	@EventHandler
	public void SpawnAnimals(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;

		Iterator<LivingEntity> entIterator = _mobs.iterator();

		while (entIterator.hasNext())
		{
			LivingEntity ent = entIterator.next();

			if (!ent.isValid())
			{
				ent.remove();
				entIterator.remove();
			}
		}

		if (_mobs.size() > 16)
			return;

		//Loc
		double r = Math.random();

		Location loc = Manager.GetSpawn();

		if (r > 0.75)		loc = new Location(Manager.GetSpawn().getWorld(), -9, 72, 42);
		else if (r > 0.5)	loc = new Location(Manager.GetSpawn().getWorld(), -36, 72, -28);
		else if (r > 0.25)	loc = new Location(Manager.GetSpawn().getWorld(), 31, 72, -34);
		else 				loc = new Location(Manager.GetSpawn().getWorld(), 43, 72, 5);

		//Spawn
		if (Manager.Type == HubType.Halloween)
		{
			Skeleton ent = loc.getWorld().spawn(loc, Skeleton.class);

			if (Math.random() > 0.5)	
				ent.setSkeletonType(SkeletonType.WITHER);
			
			ent.getEquipment().setHelmet(ItemStackFactory.Instance.CreateStack(Material.PUMPKIN));
			
			ent.setCustomName(C.cYellow + "Pumpkin Minion");
			ent.setCustomNameVisible(false);
			
			_mobs.add(ent);

			Manager.GetCondition().Factory().Invisible("Perm", ent, ent, 999999999, 0, false, false, true);
			Manager.GetCondition().Factory().Slow("Perm", ent, ent, 999999999, 1, false, false, false, true);
		}
		else 	if (Manager.Type == HubType.Christmas)
		{
			_mobs.add(loc.getWorld().spawn(loc, Snowman.class));
		}
		else
		{
			r = Math.random();

			if (r > 0.66)		_mobs.add(loc.getWorld().spawn(loc, Cow.class));
			else if (r > 0.33)	_mobs.add(loc.getWorld().spawn(loc, Pig.class));
			else				_mobs.add(loc.getWorld().spawn(loc, Chicken.class));
		}
	}

	@EventHandler
	public void BlockBreak(BlockBreakEvent event)
	{
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return; 

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Explosion(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}

	@EventHandler
	public void VineGrow(BlockSpreadEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void LeaveDecay(LeavesDecayEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockPlace(BlockPlaceEvent event)
	{
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void BorderUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (UtilMath.offset(player.getLocation(), Manager.GetSpawn()) > 200)
			{
				player.eject();
				player.leaveVehicle();
				player.teleport(Manager.GetSpawn());
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void ItemPickup(PlayerPickupItemEvent event)
	{
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void ItemDrop(PlayerDropItemEvent event)
	{
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;

		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void ItemDespawn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Entity ent : UtilWorld.getWorld("world").getEntities())
		{
			if (!(ent instanceof Item))
				continue;
			
			if (((Item)ent).getItemStack().getType() == Material.MONSTER_EGG)
				continue;
			
			if (ent.getTicksLived() > 1200)
				ent.remove();
		}
	}
//Removed by TheMineBench, time is now stopped when the WorldManager is created
/*
	@EventHandler
	public void UpdateWeather(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		World world = UtilWorld.getWorld("world");

		if (Manager.Type == HubType.Halloween)	
			world.setTime(16000);
		else										
			world.setTime(6000);
										
		world.setStorm(false);
	}
*/
	//Added by TheMineBench.  Stops weather from changing.
	@EventHandler
	public void OnWeather(WeatherChangeEvent event) {
		
		if (!event.getWorld().getName().equals("world"))
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void HalloweenUpdates(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (Manager.Type != HubType.Halloween)
			return;

		//Block Lightup
		for (Player player : UtilServer.getPlayers())
		{
			for (Block block : UtilBlock.getInRadius(player.getLocation(), 3d).keySet())
			{
				if (block.getType() == Material.PUMPKIN)
					Manager.GetBlockRestore().Add(block, 91, block.getData(), 2000);
			}
		}

		//Mob Helmets
		for (LivingEntity ent : _mobs)
		{
			if (!(ent instanceof Creature))
				continue;

			Creature skel = (Creature)ent;

			if (skel.getTarget() != null && skel.getTarget() instanceof Player && UtilMath.offset(skel, skel.getTarget()) < 6)
			{
				skel.getEquipment().setHelmet(ItemStackFactory.Instance.CreateStack(Material.JACK_O_LANTERN));
			}
			else
			{
				skel.getEquipment().setHelmet(ItemStackFactory.Instance.CreateStack(Material.PUMPKIN));
			}
		}
	}
	
	@EventHandler
	public void SoundUpdate(UpdateEvent event)
	{
		if (Manager.Type != HubType.Halloween)
			return;

		if (event.getType() != UpdateType.SLOW)
			return;

		if (Math.random() > 0.1)
			return;

		for (Player player : UtilServer.getPlayers())
			player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 3f, 1f);
	}
	
	@EventHandler
	public void BlockForm(BlockFormEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void CreatureTarget(EntityTargetEvent event)
	{
		if (Manager.Type == HubType.Christmas)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void BoatBreak(CustomDamageEvent event)
	{
		if (event.GetDamageeEntity() instanceof Boat)
			event.SetCancelled("Boat Cancel");
	}
	
	@EventHandler
	public void combustPrevent(EntityCombustEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			event.setCancelled(true);
		}
	}
}
