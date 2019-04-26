package mineplex.hub.modules;

import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.donation.DonationManager;
import mineplex.core.gadget.event.GadgetActivateEvent;
import mineplex.core.gadget.event.GadgetBlockEvent;
import mineplex.core.gadget.types.MusicGadget;
import mineplex.core.mount.event.MountActivateEvent;
import mineplex.core.recharge.Recharge;
import mineplex.core.task.TaskManager;
import mineplex.core.treasure.event.TreasureStartEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.HubManager;
import mineplex.hub.modules.parkour.ParkourData;

public class ParkourManager extends MiniPlugin
{
	public HubManager Manager;

	private HashSet<Player> _active = new HashSet<Player>();

	private HashSet<ParkourData> _parkour = new HashSet<ParkourData>();

	//private Location _snakeParkourReturn;
	private Location _lavaParkourReturn;
	private WeakHashMap<Player, Location> _lavaLocation = new WeakHashMap<Player, Location>();
	private WeakHashMap<Player, Long> _lavaTimer = new WeakHashMap<Player, Long>();

	//Modules
	protected DonationManager _donationManager;
	protected TaskManager _taskManager;
 
	public ParkourManager(HubManager manager, DonationManager donation, TaskManager task)
	{
		super("Parkour", manager.getPlugin());

		Manager = manager;

		_taskManager = task;
		_donationManager = donation;

		_parkour.add(new ParkourData("Ruins Parkour", new String[]
				{
				"This is an extremely difficult parkour.",
				"You will need to find the correct way through",
				"the ruins, overcoming many challenging jumps.",
				},
				6000, new Location(Manager.GetSpawn().getWorld(), 110,66,-44), 
				new Location(Manager.GetSpawn().getWorld(), 103,100,-60),  new Location(Manager.GetSpawn().getWorld(), 150,50,26)));


		_parkour.add(new ParkourData("Lava Parkour", new String[]
				{
				"This parkour is HOT! It's so hot that you",
				"must keep sprinting for the entire course,",
				"or you will die in flames!"
				}, 4000, new Location(Manager.GetSpawn().getWorld(), -93,67,38),  
				new Location(Manager.GetSpawn().getWorld(), -86,100,42),  new Location(Manager.GetSpawn().getWorld(), -120,50,-17)));
		
//		_parkour.add(new ParkourSnake("Snake Parkour", new String[]
//				{
//				"This parkour requires incredible timing",
//				"and great agility! Some say it was created",
//				"by the devil as a cruel joke!"
//				}, 8000, new Location(Manager.GetSpawn().getWorld(), 22,70,-54),  
//				new Location(Manager.GetSpawn().getWorld(), 35,-200,-90),  new Location(Manager.GetSpawn().getWorld(), -30,250,-46)));

		_lavaParkourReturn = new Location(Manager.GetSpawn().getWorld(), -89.5,68,36.5);
		_lavaParkourReturn.setYaw(90);
		
		//_snakeParkourReturn = new Location(Manager.GetSpawn().getWorld(), 16.5,72,-52.5);
		//_snakeParkourReturn.setYaw(180);
	}

	public boolean isParkourMode(Player player)
	{
		return _active.contains(player);
	}
	
	public void setParkourMode(Player player, boolean enabled)
	{
		if (enabled)
		{
			_active.add(player);
			UtilPlayer.message(player, F.main("Parkour", "You have entered " + F.elem("Parkour Mode") + "."));
			
			Manager.GetGadget().DisableAll(player);
			
			player.setVelocity(new Vector(0,-1,0));
		}
		else
		{
			_active.remove(player);
			UtilPlayer.message(player, F.main("Parkour", "You have exited " + F.elem("Parkour Mode") + "."));
		}
	}
	
	@EventHandler
	public void playerVelocity(PlayerVelocityEvent event)
	{
		if (isParkourMode(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void disableGadgets(GadgetActivateEvent event)
	{
		if (isParkourMode(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void disableMounts(MountActivateEvent event)
	{
		if (isParkourMode(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void playerEnterParkour(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		for (Player player : UtilServer.getPlayers())
		{
			if (isParkourMode(player))
				continue;
			
			for (ParkourData data : _parkour)
				if (UtilMath.offset(player.getLocation(), data.NPC) < 6)
					if (Recharge.Instance.use(player, data.Name+" Info", 300000, false, false))
						data.Inform(player);
		}
	}
	
	@EventHandler
	public void parkourUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Player> playerIterator = _active.iterator();
		
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			
			player.leaveVehicle();
			player.eject();
			
			if (!InsideParkour(player.getLocation()))
			{
				playerIterator.remove();
				UtilPlayer.message(player, F.main("Parkour", "You have exited " + F.elem("Parkour Mode") + "."));
			}
			else
			{
				
				for (Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator(); iterator.hasNext();)
				{
					player.removePotionEffect(iterator.next().getType());
				}
			}
		}
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_active.remove(event.getPlayer());
	}

	@EventHandler
	public void disallowBlockBreak(BlockBreakEvent event)
	{
		if (isParkourMode(event.getPlayer()))
		{
			event.getPlayer().teleport(Manager.GetSpawn());
			UtilPlayer.message(event.getPlayer(), F.main("Parkour", "You cannot break blocks in Parkour Mode!"));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void disallowBlockPlace(PlayerInteractEvent event)
	{
		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (!event.getPlayer().getItemInHand().getType().isBlock())
			return;
	
		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;
		
		if (isParkourMode(event.getPlayer()))
		{
			event.getPlayer().teleport(Manager.GetSpawn());
			UtilPlayer.message(event.getPlayer(), F.main("Parkour", "You cannot place blocks in Parkour Mode!"));
		}
	}

	@EventHandler
	public void lavaReturn(EntityDamageEvent event)
	{
		if (event.getCause() != DamageCause.LAVA)
			return;

		if (!(event.getEntity() instanceof Player))
		{
			event.getEntity().remove();
			return;
		}

		Player player = (Player)event.getEntity();

		if (!isParkourMode(player))
			return;

		event.getEntity().eject();
		event.getEntity().leaveVehicle();
		event.getEntity().teleport(_lavaParkourReturn);
	}
	
//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void snakeReturn(EntityDamageEvent event)
//	{
//		if (event.getCause() != DamageCause.VOID)
//			return;
//		
//		if (!(event.getEntity() instanceof Player))
//			return;
//		
//		Player player = (Player)event.getEntity();
//		
//		if (!isParkourMode(player))
//			return;
//					
//		event.getEntity().eject();
//		event.getEntity().leaveVehicle();
//		event.getEntity().teleport(_snakeParkourReturn);
//	}

	@EventHandler
	public void lavaBlockReturn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!UtilEnt.isGrounded(player))
				continue;
			
			if (!isParkourMode(player))
				continue;

			int id = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getTypeId();
			int data = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getData();
			if (id != 0 && id != 112 && id != 114 && !(id == 43 && data == 6)  && !(id == 44 && data == 6))
				continue;

			if (!_lavaLocation.containsKey(player) || UtilMath.offset(player.getLocation(), _lavaLocation.get(player)) > 1.5)
			{
				_lavaLocation.put(player, player.getLocation());
				_lavaTimer.put(player, System.currentTimeMillis());
				continue;
			}

			if (UtilTime.elapsed(_lavaTimer.get(player), 500))
			{
				boolean inCourse = false;
				for (Block block : UtilBlock.getInRadius(player.getLocation(), 1.5).keySet())
				{
					if (block.getType() == Material.NETHER_BRICK || block.getType() == Material.NETHER_BRICK_STAIRS)
					{
						inCourse = true;
						break;
					}
				}

				if (!inCourse)
					continue;

				_lavaLocation.remove(player);
				_lavaTimer.remove(player);

				player.eject();
				player.leaveVehicle();
				player.teleport(_lavaParkourReturn);
				player.setFireTicks(0);

				UtilPlayer.message(player, F.main("Parkour", "You cannot stop running during Lava Parkour!"));
			}
		}
	}

	@EventHandler
	public void finishParkour(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() == null)
			return;

		if (!(event.getRightClicked() instanceof LivingEntity))
			return;

		LivingEntity ent = (LivingEntity)event.getRightClicked();

		if (ent.getCustomName() == null)
			return;
		
		//Start Message
		if (ent.getCustomName().contains("Start"))
		{
			Player player = event.getPlayer();

			for (ParkourData data : _parkour)
			{
				if (!ent.getCustomName().contains(data.Name))
					continue;
				
				if (isParkourMode(player))
					setParkourMode(player, false);
				else
					setParkourMode(player, true);
			}
		}

		//Finish Message
		if (ent.getCustomName().contains("Finish"))
		{
			final Player player = event.getPlayer();
			
			if (!isParkourMode(player))
			{
				//Inform
				UtilPlayer.message(player, F.main("Parkour", "You must be in " + F.elem("Parkour Mode") + " to cleanup."));
				UtilPlayer.message(player, F.main("Parkour", "Talk to the " + F.elem("Start NPC") + " to enter Parkour Mode."));
				return;
			}

			if (!Recharge.Instance.use(player, "Finish Parkour", 30000, false, false))
				return;

			for (ParkourData data : _parkour)
			{
				if (!ent.getCustomName().contains(data.Name))
					continue;

				//Inform
				UtilPlayer.message(player, F.main("Parkour", "You completed " + F.elem(data.Name) + "."));

				//Gems
				if (!_taskManager.hasCompletedTask(player, data.Name))		
				{
					final ParkourData fData = data;

					_taskManager.completedTask(new Callback<Boolean>() 
					{
						public void run(Boolean completed)
						{
							_donationManager.RewardGems(new Callback<Boolean>() 
							{
								public void run(Boolean completed)
								{
									if (completed)
									{
										UtilPlayer.message(player, F.main("Parkour", "You received " + F.elem(C.cGreen + fData.Gems + " Gems") + "."));
	
										//Sound
										player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1.5f);
									}
									else
									{
										_taskManager.Get(player).TasksCompleted.remove(_taskManager.getTaskId(fData.Name));
										UtilPlayer.message(player, F.main("Parkour", "There as an error giving " + F.elem(C.cGreen + fData.Gems + " gems to you.  Please click the NPC again.") + "."));
									}
								}
							}, "Parkour " + fData.Name, player.getName(), player.getUniqueId(), fData.Gems);							

							//Sound
							player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1.5f);
						}
					}, player, fData.Name);
				}
			}
		}
	}
	
	@EventHandler
	public void gadgetBlockChange(GadgetBlockEvent event)
	{
		for (Iterator<Block> iterator = event.getBlocks().iterator(); iterator.hasNext();)
		{
			Block block = iterator.next();
			
			for (ParkourData data : _parkour)
			{
				if (data.InBoundary(block.getLocation()))
				{
					iterator.remove();
					break;
				}
			}	
		}
	}

	public boolean InsideParkour(Location loc)
	{
		for (ParkourData data : _parkour)
			if (data.InBoundary(loc))
				return true;
		
		return false;
	}

	@EventHandler
	public void preventTreasureNearParkour(TreasureStartEvent event)
	{
		if (InsideParkour(event.getPlayer().getLocation()))
		{
			event.setCancelled(true);
			UtilPlayer.message(event.getPlayer(), F.main("Parkour", "You can't open chests near Parkour."));
		}
	}
	
	@EventHandler
	public void musicDisable(GadgetActivateEvent event)
	{
		if (event.getGadget() instanceof MusicGadget)
		{
			if (InsideParkour(event.getPlayer().getLocation()))
			{
				event.setCancelled(true);
			}
		}
	}
	
//	@EventHandler
//	public void snakeUpdate(UpdateEvent event)
//	{
//		if (event.getType() != UpdateType.FASTER)
//			return;
//		
//		for (ParkourData parkour : _parkour)
//			if (parkour instanceof ParkourSnake)
//				((ParkourSnake)parkour).Update();
//	}
}
