package nautilus.game.arcade.game.games.uhc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.packethandler.PacketPlayOutWorldBorder;
import mineplex.core.recharge.Recharge;
import mineplex.core.timing.TimingManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.CombatLog;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.serverdata.Utility;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.TeamGame;
import nautilus.game.arcade.kit.Kit;

public class UHC extends TeamGame
{
	private NautHashMap<Player, Player> _teamReqs = new NautHashMap<Player, Player>();
	
	private NautHashMap<String, Long> _deathTime = new NautHashMap<String, Long>();
	// private NautHashMap<String, Long> _combatTime = new NautHashMap<String, Long>();

	private boolean _mapLoaded = false;
	private double _mapLoadPercent = 0;
	private int _chunksPerTick = 1;
	private int _chunkTotal;
	private Chunk _chunk = null;
	private int _chunkX = 0;
	private int _chunkZ = 0;
	private int _chunksLoaded = 0;

	private int _gameMinutes = 0;
	private int _safeTime = 11;
	private long _lastMinute = System.currentTimeMillis();

	private Objective _scoreObj;

	private long _createTime;
	private long _serverTime;
	

	private boolean xrayDebug = false;

	// Border
	private int _secondsSinceStart;
	private HashMap<Integer, Double> _borderPositions = new HashMap<Integer, Double>();
	private double _currentBorder = 1000;
	private double _previousBorder = 1000;
	private long _borderStartedMoving;

	public UHC(ArcadeManager manager)
	{
		super(manager, GameType.UHC,

		new Kit[]
			{
				new KitUHC(manager)
			},

		new String[]
			{
					"10 minutes of no PvP", "Only Golden Apples restore health", "Ores can only be found in caves",
					"Borders shrink over time", "Last player/team alive wins!"
			});

		this.StrictAntiHack = true;

		this.GameTimeout = 10800000;

		this.DamagePvP = false;

		this.DeathDropItems = true;

		this.ItemDrop = true;
		this.ItemPickup = true;

		this.BlockBreak = true;
		this.BlockPlace = true;

		this.InventoryOpenBlock = true;
		this.InventoryOpenChest = true;
		this.InventoryClick = true;

		this.DeathOut = true;
		this.QuitOut = false;

		this.CreatureAllow = true;

		this.AnnounceStay = false;

		this.DisplayLobbySide = true;

		this.DeathMessages = false;

		this.SoupEnabled = false;

		this.CompassEnabled = true;
		this.CompassGiveItem = false;

		this.WorldBoundaryKill = false;

		this.TickPerTeleport = 3;

		this.GemBoosterEnabled = false;
		this.GemDoubleEnabled = false;
		this.GemHunterEnabled = false;

		this.WorldBoneMeal = true;

		this.DontAllowOverfill = true;

		this.VersionRequire1_8 = true;

		this.GadgetsDisabled = true;

		WorldTimeSet = -1;
		
		CraftRecipes();

		// Disable Custom Mob Drops (and EXP Disable)
		Manager.GetCreature().SetDisableCustomDrops(true);

		// Disable Anti-Stack
		setItemMerge(true);

		// Damage Settings
		Manager.GetDamage().SetEnabled(false);

		_scoreObj = Scoreboard.GetScoreboard().registerNewObjective("Health", "health");
		_scoreObj.setDisplaySlot(DisplaySlot.PLAYER_LIST);

		_createTime = System.currentTimeMillis();
		_serverTime = Utility.currentTimeMillis();
	}

	@Override
	public void ParseData()
	{
		WorldData.World.setDifficulty(Difficulty.HARD);	
		
		_chunkX = (int) -(_currentBorder / 16);
		_chunkZ = (int) -(_currentBorder / 16);
		_chunkTotal = (int) ((_currentBorder * 2 / 16) * (_currentBorder * 2 / 16));

		WorldData.MinX = -1000;
		WorldData.MinZ = -1000;
		WorldData.MaxX = 1000;
		WorldData.MaxZ = 1000;

		int i = 0;

		for (double border : buildBorders(1000, 1000, 32))
		{
			_borderPositions.put(i++ * 4, border);
		}
	}

	@EventHandler
	public void onDamage(CustomDamageEvent event)
	{
		if (!IsLive())
			return;

		if (UtilTime.elapsed(getGameLiveTime(), 20000))
			return;

		if (!(event.GetDamageeEntity() instanceof Player))
			return;

		event.SetCancelled("Spawn Invincibility");
		event.GetDamageeEntity().setFireTicks(0);

	}

	@EventHandler
	public void onSecond(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
		{
			return;
		}

		if (!IsLive())
		{
			return;
		}

		_previousBorder = _currentBorder;

		// We half the number so this only activates every 2nd second.
		if (_borderPositions.containsKey(_secondsSinceStart))
		{
			_currentBorder = _borderPositions.get(_secondsSinceStart);

			setBorder();
		}

		_secondsSinceStart++;
	}

	private void setBorder()
	{
		_borderStartedMoving = System.currentTimeMillis();

		PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder();
		packet.worldBorderType = 1;

		packet.newRadius = _currentBorder;
		packet.oldRadius = _previousBorder;

		packet.speed = _currentBorder != _previousBorder ? 1000 : 0;

		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (UtilPlayer.is1_8(player))
			{
				UtilPlayer.sendPacket(player, packet);
			}
		}
	}

	@EventHandler
	public void outsideBorder(UpdateEvent event)
	{
		if (!IsLive())
		{
			return;
		}

		if (event.getType() != UpdateType.FAST)
		{
			return;
		}

		// The distance between the old border and the new
		double distanceMovedSince = _currentBorder - _previousBorder;

		// Multiply that distance depending on how long its been since it moved.
		long timeSinceMoved = System.currentTimeMillis() - _borderStartedMoving;
		double percentageBorderMoved = Math.min(timeSinceMoved, 1000D) / 1000D;

		distanceMovedSince *= percentageBorderMoved;

		double border = (_previousBorder - 0.3D) + distanceMovedSince;

		for (Player player : UtilServer.getPlayers())
		{
			Location loc = player.getLocation();

			// Bump Players Back In
			if (loc.getX() > border || loc.getX() < -border || loc.getZ() > border || loc.getZ() < -border)
			{
				if (Recharge.Instance.use(player, "Hit by Border", 1000, false, false))
				{
					Entity bottom = player;
					while (bottom.getVehicle() != null)
						bottom = bottom.getVehicle();

					UtilAction
							.velocity(bottom, UtilAlg.getTrajectory2d(loc, GetSpectatorLocation()), 1.2, true, 0.4, 0, 10, true);

					if (Manager.IsAlive(player))
					{
						Manager.GetDamage().NewDamageEvent(player, null, null, DamageCause.CUSTOM, 10, false, false, false,
								"Nether Field", "Vaporize");

						player.getWorld().playSound(loc, Sound.NOTE_BASS, 2f, 1f);
						player.getWorld().playSound(loc, Sound.NOTE_BASS, 2f, 1f);
					}
				}
			}
		}
	}

	@EventHandler
	public void onGameState(PlayerChangedWorldEvent event)
	{
		if (GetState() == GameState.Prepare || IsLive())
		{
			setupBorder(event.getPlayer());
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		if (GetState() == GameState.Prepare || IsLive())
		{
			setupBorder(event.getPlayer());
		}
	}

	private void setupBorder(Player player)
	{
		PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder();
		packet.worldBorderType = 3;

		packet.newRadius = _currentBorder;
		packet.oldRadius = _previousBorder;

		packet.warningBlocks = -10;
		packet.warningTime = -10;

		if (_currentBorder != _previousBorder)
		{
			packet.speed = 1000 - Math.min(1000, (System.currentTimeMillis() - _borderStartedMoving));
		}

		// We don't set warnings speed or blocks as its not particularly useful for this game.
		// Also if we don't use it here, its more effective in other places to reinforce the idea what its for.

		if (UtilPlayer.is1_8(player))
		{
			UtilPlayer.sendPacket(player, packet);
		}
	}

	private ArrayList<Double> buildBorders(int seconds, double border, double leaveRemaining)
	{
		double totalNumber = Math.pow(seconds, 1.9D) + (seconds * 50);

		ArrayList<Double> borders = new ArrayList<Double>();

		for (int i = 0; i <= seconds; i++)
		{
			borders.add(border - ((border - leaveRemaining) * (((Math.pow(i, 1.9D) + (i * 50))) / totalNumber)));
		}

		return borders;
	}

	@EventHandler
	public void loadMap(UpdateEvent event)
	{
		if (_mapLoaded)
			return;

		if (WorldData.World == null)
			return;
		
		if (GetState() != GameState.Recruit)
			return;

		// Print Debug
		if (event.getType() == UpdateType.SLOWER)
		{
			Announce(C.cGreen + C.Bold + "Generating Map: " + C.cWhite + getMapLoadETA() + " Remaining...", false);
			
			TimingManager.endTotal("UHC Generation", true);
			return;
		}

		if (event.getType() != UpdateType.TICK)
			return;

		// Timings
		TimingManager.startTotal("UHC Generation");

		for (int i = 0; i < _chunksPerTick ; i++)
		{
			// Unload Previous
//			if (_chunk != null)
//				_chunk.unload(true); 

			// Load Chunks
			_chunk = WorldData.World.getChunkAt(_chunkX, _chunkZ);
			_chunk.load(true);

			// Scan Map
			if (_chunkX < _currentBorder / 16)
			{
				_chunkX++;
			}
			else if (_chunkZ < _currentBorder / 16)
			{
				_chunkX = (int) -(_currentBorder / 16);
				_chunkZ++;
			}
			else
			{
				_mapLoaded = true;
				System.out.println("Map Loading Finished!");
				generateSpawns();
				break;
			}

			_chunksLoaded++;
		}
			
		_mapLoadPercent = (double)_chunksLoaded / (double)_chunkTotal;

		// Timings
		TimingManager.stopTotal("UHC Generation");
	}
		
	@EventHandler
	public void chunkUnload(ChunkUnloadEvent event)
	{
		//Allow unloading after players in
		if (IsLive())
			return;
		
		if (WorldData.World != null && event.getWorld().equals(WorldData.World))
		{
			System.out.println("Disallowing Unload of World");
			event.setCancelled(true);
		}
	}

	public void generateSpawns()
	{
		// Wipe Spawns
		for (GameTeam team : this.GetTeamList())
		{
			team.GetSpawns().clear();
		}

		TimingManager.start("UHC Spawn Generation");

		// Solo Game
		if (this.GetTeamList().size() == 1)
		{
			while (GetTeamList().get(0).GetSpawns().size() < this.GetPlayers(true).size())
			{
				Location loc = GetRandomSpawn(null);

				// Dynamically scale distance requirement based on how many teams need to fit
				double dist = (2 * _currentBorder) / (Math.sqrt(this.GetPlayers(true).size()) + 3);

				// Ensure distance between Teams - 500 Attempts
				for (int i=0 ; i<500 ; i++)
				{
					boolean clash = false;

					for (Location otherSpawn : GetTeamList().get(0).GetSpawns())
					{
						if (UtilMath.offset(loc, otherSpawn) < dist)
						{
							clash = true;
							break;
						}
					}

					if (!clash)
						break;

					loc = GetRandomSpawn(null);
				}

				GetTeamList().get(0).GetSpawns().add(loc);
			}
		}
		// Team Game
		else
		{
			for (GameTeam team : GetTeamList())
			{
				Location loc = GetRandomSpawn(null);

				// Dynamically scale distance requirement based on how many teams need to fit
				double dist = (2 * _currentBorder) / (Math.sqrt(GetTeamList().size()) + 3);

				// Ensure distance between Teams - 500 Attempts
				for (int i=0 ; i<500 ; i++)
				{
					boolean clash = false;

					for (GameTeam otherTeam : GetTeamList())
					{
						if (otherTeam.GetSpawns().isEmpty())
							continue;

						if (UtilMath.offset(loc, otherTeam.GetSpawn()) < dist)
						{
							clash = true;
							break;
						}
					}

					if (!clash)
						break;

					loc = GetRandomSpawn(null);
				}

				team.GetSpawns().add(loc);

				while (team.GetSpawns().size() < 20)
				{
					Location other = GetRandomSpawn(loc);

					team.GetSpawns().add(other);
				}
			}
		}

		TimingManager.stop("UHC Spawn Generation");
	}

	@EventHandler
	public void endPortalTransfer(final PlayerPortalEvent event)
	{
		if (event.getCause() == TeleportCause.END_PORTAL)
			event.setCancelled(true);
	}

	@EventHandler
	public void TimeUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		if (!UtilTime.elapsed(_lastMinute, 60000))
			return;

		_gameMinutes++;
		_lastMinute = System.currentTimeMillis();

		if (_gameMinutes < _safeTime)
		{
			UtilTextMiddle.display(null, "PvP enabled in " + (_safeTime - _gameMinutes) + " minutes.", 5, 80, 5);
		}
		else if (_gameMinutes == _safeTime)
		{
			UtilTextMiddle.display(null, "PvP has been enabled!", 5, 80, 5);

			for (Player player : UtilServer.getPlayers())
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);

			this.DamagePvP = true;
			this.CompassGiveItem = true;
			this.QuitOut = true;
		}
	}

	@EventHandler
	public void EarlyGameUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		if (DamagePvP)
			return;

		WorldData.World.setTime(2000);

		for (Player player : GetPlayers(true))
		{
			player.setSaturation(3f);
			player.setExhaustion(0f);
			player.setFoodLevel(20);
		}
	}

	@EventHandler
	public void GameStart(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		WorldData.World.setTime(2000);

		// Kill Evil Mobs
		for (Entity ent : WorldData.World.getEntities())
		{
			if (!(ent instanceof Monster))
				continue;

			ent.remove();
		}

		// Hunger
		for (Player player : GetPlayers(true))
		{
			player.setSaturation(4f);
			player.setExhaustion(0f);
		}
	}

	/*@EventHandler
	public void WorldBoundaryCheck(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : GetPlayers(true))
		{
			//Damage
			if (Math.abs(player.getLocation().getX()) > _borderSize || Math.abs(player.getLocation().getZ()) > _borderSize)
			{
				player.damage(0.75);
			}
		}
	}

	@EventHandler
	public void WorldBoundaryShrink(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() == UpdateType.SLOW)
			if (_borderSize > 16)
				_borderSize--;
	}

	//@EventHandler
	public void WorldBoundarySet(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Recruit)
			return;

		long time = System.currentTimeMillis();

		this.WorldData.MinX = -_borderSize;
		this.WorldData.MaxX = _borderSize;
		this.WorldData.MinZ = -_borderSize;
		this.WorldData.MaxZ = _borderSize;

		this.WorldData.MinY = -1000;
		this.WorldData.MaxY = 1000;

		//Find Y Max
		for (int x=-16 ; x<16 ; x++)
			for (int z=-16 ; z<16 ; z++)
			{
				int y = UtilBlock.getHighest(WorldData.World, x, z).getY();

				if (y > _yMax)
					_yMax = y;
			}
		
		System.out.println("Y Max: " + _yMax);

		System.out.println("Time: " + UtilTime.MakeStr(System.currentTimeMillis() - time));
	}*/

	@EventHandler
	public void WorldBoundaryYLimit(BlockPlaceEvent event)
	{
		if (event.getBlock().getX() >= -36 && event.getBlock().getX() <= 36 && event.getBlock().getZ() >= -36
				&& event.getBlock().getZ() <= 36)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot build this high near center of map."));
			event.setCancelled(true);
		}
	}

	public Location GetRandomSpawn(Location around)
	{
		Location loc = null;

		while (loc == null)
		{
			Block block = null;

			// Get Team Location
			if (around == null)
			{
				// Return a int from 0 - 1800, then remove 900 so its a int from -900 to 900
				int x = (int) (UtilMath.r((int) (1.8 * _currentBorder)) - (0.9 * _currentBorder));
				int z = (int) (UtilMath.r((int) (1.8 * _currentBorder)) - (0.9 * _currentBorder));

				block = UtilBlock.getHighest(WorldData.World, x, z, null);
			}
			// Get Radius Location
			else
			{
				block = UtilBlock.getHighest(WorldData.World, around.getBlockX() - 4 + UtilMath.r(8), around.getBlockZ() - 4
						+ UtilMath.r(8), null);
			}

			// Check Validity

			// Liquid
			if (block.getRelative(BlockFace.DOWN).isLiquid())
				continue;

			// Suffocated
			if (block.getType() != Material.AIR || block.getRelative(BlockFace.UP).getType() != Material.AIR)
				continue;

			loc = block.getLocation().add(0.5, 0.5, 0.5);
		}

		return loc;
	}

	@EventHandler
	public void GhastDrops(EntityDeathEvent event)
	{
		if (event.getEntity() instanceof Ghast)
		{
			event.getDrops().clear();
			event.getDrops().add(ItemStackFactory.Instance.CreateStack(Material.GOLD_INGOT, 1));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerQuitDropItems(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		GameTeam team = GetTeam(player);
		if (team == null)
			return;

		if (!IsAlive(player))
			return;

		if (!QuitOut)
			return;

		// Drop Items
		UtilInv.drop(player, true);

		// Skull Drop
		ItemStack stack = ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM, (byte) 3, 1,
				team.GetColor() + player.getName() + "'s Head");

		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(player.getName());
		stack.setItemMeta(meta);

		event.getPlayer().getWorld().dropItemNaturally(player.getEyeLocation(), stack);
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();

		GameTeam team = GetTeam(player);
		if (team == null)
			return;

		// Skull Drop
		ItemStack stack = ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM, (byte) 3, 1,
				team.GetColor() + player.getName() + "'s Head");

		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(player.getName());
		stack.setItemMeta(meta);

		event.getDrops().add(stack);

		// Lightning
		Location loc = player.getLocation();
		loc.setY(-150);
		player.getWorld().strikeLightningEffect(loc);

		// Gems
		if (IsLive())
		{
			long timeAlive = System.currentTimeMillis() - GetStateTime();
			AddGems(player, timeAlive / 60000d, "Survived " + UtilTime.MakeStr(timeAlive), false, false);
		}

		// Time
		_deathTime.put(player.getName(), System.currentTimeMillis());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerDeathMessage(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player dead = (Player) event.GetEvent().getEntity();

		CombatLog log = event.GetLog();

		Player killer = null;
		if (log.GetKiller() != null)
			killer = UtilPlayer.searchExact(log.GetKiller().GetName());

		// Simple
		if (killer != null)
		{
			Announce(Manager.GetColor(dead) + C.Bold + dead.getName() + C.cGray + C.Bold + " was killed by "
					+ Manager.GetColor(killer) + C.Bold + killer.getName() + C.cGray + C.Bold + ".");
		}
		else
		{
			if (log.GetAttackers().isEmpty())
			{
				Announce(Manager.GetColor(dead) + C.Bold + dead.getName() + C.cGray + C.Bold + " has died by unknown causes.");
			}

			else
			{
				Announce(Manager.GetColor(dead) + C.Bold + dead.getName() + C.cGray + C.Bold + " was killed by "
						+ log.GetAttackers().getFirst().GetName() + ".");
			}
		}
	}

	@EventHandler
	public void PlayerDeathTimeKick(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!_deathTime.containsKey(player.getName()))
				continue;

			if (!UtilTime.elapsed(_deathTime.get(player.getName()), 20000))
				continue;

			_deathTime.remove(player.getName());
			// XXX Need this ? Manager.GetPortal().SendPlayerToServer(player, "Lobby");
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerKick(PlayerKickEvent event)
	{
		event.setLeaveMessage(null);
	}

	/*
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		GameTeam team = GetTeam(player);
		if (team == null)		return;

		if (!team.IsAlive(player))
			return;

		team.RemovePlayer(player);

		if (player.isDead())
			return;


		if (true)
		{
			//Announcement
			Announce(team.GetColor() + C.Bold + player.getName() + " was killed for disconnecting.");

			player.damage(5000);
			return;
		}


		if (_combatTime.containsKey(player.getName()) && !UtilTime.elapsed(_combatTime.get(player.getName()), 15000))
		{
			//Announcement
			Announce(team.GetColor() + C.Bold + player.getName() + " was killed for disconnecting during combat.");

			player.damage(5000);
			return;
		}
	}
	 */

	@EventHandler
	public void CreatureCull(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.SLOW)
			return;

		HashMap<EntityType, ArrayList<Entity>> ents = new HashMap<EntityType, ArrayList<Entity>>();

		for (Entity ent : WorldData.World.getEntities())
		{
			if (!ents.containsKey(ent.getType()))
				ents.put(ent.getType(), new ArrayList<Entity>());

			ents.get(ent.getType()).add(ent);
		}

		for (EntityType type : ents.keySet())
		{
			ArrayList<Entity> entList = ents.get(type);
			int count = 0;

			if (type == EntityType.DROPPED_ITEM)
				continue;

			while (entList.size() > 500)
			{
				Entity ent = entList.remove(UtilMath.r(entList.size()));
				ent.remove();
				count++;
			}

			if (count > 0)
				System.out.println("Removed " + count + " " + type);
		}
	}

	private void CraftRecipes()
	{
		ShapelessRecipe goldMelon = new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON, 1));
		goldMelon.addIngredient(1, Material.MELON);
		goldMelon.addIngredient(1, Material.GOLD_BLOCK);
		UtilServer.getServer().addRecipe(goldMelon);

		ShapedRecipe headApple2 = new ShapedRecipe(new ItemStack(Material.GOLDEN_APPLE, 1));
		headApple2.shape("GGG", "GHG", "GGG");
		headApple2.setIngredient('G', Material.GOLD_INGOT);
		headApple2.setIngredient('H', new MaterialData(Material.SKULL_ITEM, (byte) 3));
		UtilServer.getServer().addRecipe(headApple2);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void CraftGoldenAppleDeny(PrepareItemCraftEvent event)
	{
		if (event.getRecipe().getResult() == null)
			return;

		Material type = event.getRecipe().getResult().getType();

		if (type != Material.GOLDEN_APPLE)
			return;

		if (!(event.getInventory() instanceof CraftingInventory))
			return;

		CraftingInventory inv = (CraftingInventory) event.getInventory();

		for (ItemStack item : inv.getMatrix())
			if (item != null && item.getType() != Material.AIR)
				if (item.getType() == Material.GOLD_INGOT)
					return;

		inv.setResult(null);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void CraftGoldenAppleHead(PrepareItemCraftEvent event)
	{
		if (event.getRecipe().getResult() == null)
			return;

		Material type = event.getRecipe().getResult().getType();

		if (type != Material.GOLDEN_APPLE)
			return;

		if (!(event.getInventory() instanceof CraftingInventory))
			return;

		CraftingInventory inv = (CraftingInventory) event.getInventory();

		for (ItemStack item : inv.getMatrix())
			if (item != null && item.getType() != Material.AIR)
				if (item.getType() == Material.SKULL_ITEM || item.getType() == Material.SKULL)
				{
					if (item.getItemMeta() == null)
						continue;

					if (item.getItemMeta().getDisplayName() == null)
						continue;

					ItemStack apple = ItemStackFactory.Instance.CreateStack(Material.GOLDEN_APPLE, (byte) 0, 1, item
							.getItemMeta().getDisplayName() + ChatColor.AQUA + " Golden Apple");
					apple.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);

					inv.setResult(apple);
					return;
				}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void CraftGlisteringMelon(PrepareItemCraftEvent event)
	{
		if (event.getRecipe().getResult() == null)
			return;

		Material type = event.getRecipe().getResult().getType();

		if (type != Material.SPECKLED_MELON)
			return;

		if (!(event.getInventory() instanceof CraftingInventory))
			return;

		CraftingInventory inv = (CraftingInventory) event.getInventory();

		// Allow FULL BLOCK Gold Melon
		for (ItemStack item : inv.getMatrix())
			if (item != null && item.getType() != Material.AIR)
				if (item.getType() == Material.GOLD_BLOCK)
					return;

		inv.setResult(null);
	}

	@EventHandler
	public void HealthChange(EntityRegainHealthEvent event)
	{
		if (event.getRegainReason() == RegainReason.SATIATED)
			event.setCancelled(true);
	}

	@EventHandler
	public void HeadPlaceCancel(BlockPlaceEvent event)
	{
		if (event.getItemInHand().getType() == Material.SKULL || event.getItemInHand().getType() == Material.SKULL_ITEM)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void HeadPickup(PlayerPickupItemEvent event)
	{
		if (!IsLive())
			return;

		if (event.isCancelled())
			return;

		if (event.getItem().getItemStack().getType() == Material.SKULL_ITEM)
		{
			UtilPlayer.message(event.getPlayer(), " ");
			UtilPlayer.message(event.getPlayer(), C.cGreen + C.Bold + "You picked up a Player Head!");
			UtilPlayer.message(event.getPlayer(), C.cWhite + "Craft a Golden Head Apple with it for ultimate healing.");
			UtilPlayer.message(event.getPlayer(), C.cWhite + "Use the recipe for Golden Apple, but Head replaces Apple.");
			UtilPlayer.message(event.getPlayer(), " ");
		}
	}

	/*
	@EventHandler
	public void HeadEat(PlayerInteractEvent event)
	{
		if (UtilGear.isMat(event.getPlayer().getItemInHand(), Material.SKULL_ITEM))
		{
			UtilPlayer.message(event.getPlayer(), "You ate " + event.getPlayer().getItemInHand().getItemMeta().getDisplayName() + ChatColor.RESET + ".");

			(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0)).apply(event.getPlayer());
			(new PotionEffect(PotionEffectType.REGENERATION, 200, 1)).apply(event.getPlayer());

			event.getPlayer().setItemInHand(null);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void DamageHealCancel(EntityDamageEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			player.removePotionEffect(PotionEffectType.REGENERATION);

			UtilPlayer.message(player, "You took damage and lost " + F.elem(C.cGreen + "Regeneration") + ChatColor.RESET + ".");
		}
	}*/

	@EventHandler
	public void ConsumeHeadApple(PlayerItemConsumeEvent event)
	{
		if (event.getItem().getItemMeta().getDisplayName() == null)
			return;

		if (!event.getItem().getItemMeta().getDisplayName().contains("Head"))
			return;

		UtilPlayer.message(event.getPlayer(), "You ate " + event.getItem().getItemMeta().getDisplayName());

		(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0)).apply(event.getPlayer());
		(new PotionEffect(PotionEffectType.REGENERATION, 200, 1)).apply(event.getPlayer());
	}

	@EventHandler
	public void NetherObsidianCancel(BlockPlaceEvent event)
	{
		if (event.getBlock().getWorld().getEnvironment() == Environment.NETHER)
		{
			if (event.getBlock().getType() == Material.OBSIDIAN)
			{
				UtilPlayer.message(event.getPlayer(),
						F.main("Game", "You cannot place " + F.elem("Obsidian") + " in the " + F.elem("Nether") + "."));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void Commands(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().startsWith("/kill"))
			event.setCancelled(true);

		// if (event.getMessage().startsWith("/uhc time day"))
		// {
		// this.WorldTimeSet = 4000;
		// event.setCancelled(true);
		//
		// Announce(event.getPlayer().getName() + " set time to Always Day!");
		// }
		//
		// if (event.getMessage().startsWith("/uhc time night"))
		// {
		// this.WorldTimeSet = 16000;
		// event.setCancelled(true);
		//
		// Announce(event.getPlayer().getName() + " set time to Always Night!");
		// }
		//
		// if (event.getMessage().startsWith("/uhc time cycle"))
		// {
		// this.WorldTimeSet = -1;
		// event.setCancelled(true);
		//
		// Announce(event.getPlayer().getName() + " set time to Day and Night!");
		// }
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void clearCreeperExplode(EntityExplodeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void clearCreeperExplodeReenable(EntityExplodeEvent event)
	{
		event.setCancelled(false);
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		// Solo
		if (GetTeamList().size() == 1)
		{
			if (GetPlayers(true).size() <= 1)
			{
				ArrayList<Player> places = GetTeamList().get(0).GetPlacements(true);

				// Announce
				AnnounceEnd(places);

				// Gems
				if (places.size() >= 1)
					AddGems(places.get(0), 20, "1st Place", false, false);

				if (places.size() >= 2)
					AddGems(places.get(1), 15, "2nd Place", false, false);

				if (places.size() >= 3)
					AddGems(places.get(2), 10, "3rd Place", false, false);

				for (Player player : GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);

				// End
				SetState(GameState.End);
			}
		}
		else
		{
			ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

			// Online Teams
			for (GameTeam team : this.GetTeamList())
				if (team.GetPlayers(true).size() > 0)
					teamsAlive.add(team);

			// Offline Player Team
			if (!QuitOut)
				for (GameTeam team : RejoinTeam.values())
					teamsAlive.add(team);

			if (teamsAlive.size() <= 1)
			{
				if (teamsAlive.size() > 0)
					for (Player player : teamsAlive.get(0).GetPlayers(false))
						AddGems(player, 8000, "Winning Team", false, false);

				AnnounceEnd(teamsAlive.get(0));
				SetState(GameState.End);
			}
		}
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event != null && event.getType() != UpdateType.FAST)
			return;

		ScoreboardWrite();
	}

	public void ScoreboardWrite()
	{
		Scoreboard.Reset();

		Scoreboard.WriteBlank();

		// Solo
		if (GetTeamList().size() == 1)
		{
			if (GetPlayers(true).size() < 8)
			{
				for (Player player : GetPlayers(true))
				{
					Scoreboard.WriteOrdered("Health", player.getName(), GetHealth(player), true);
				}
			}
			else
			{
				Scoreboard.Write(C.cYellow + C.Bold + "Players");
				Scoreboard.Write(GetPlayers(true).size() + " Alive");
			}
		}
		// Team
		else
		{
			ArrayList<GameTeam> aliveList = new ArrayList<GameTeam>();

			for (GameTeam team : GetTeamList())
				if (team.IsTeamAlive())
					aliveList.add(team);

			if (GetPlayers(true).size() < 8)
			{
				for (Player player : GetPlayers(true))
				{
					Scoreboard.WriteOrdered("Health", GetTeam(player).GetColor() + player.getName(), GetHealth(player), true);
				}
			}
			else
			{
				Scoreboard.Write(C.cYellow + C.Bold + "Teams");
				Scoreboard.Write(aliveList.size() + " Alive");
			}
		}

		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cYellow + C.Bold + "Time");
		Scoreboard.Write(UtilTime.MakeStr(System.currentTimeMillis() - GetStateTime()));

		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cYellow + C.Bold + "Borders");
		Scoreboard.Write("-" + (int) _currentBorder + " to " + "+" + (int) _currentBorder);

		Scoreboard.Draw();
	}

	public int GetHealth(Player player)
	{
		int health = (int) player.getHealth();

		if (player.getHealth() % 1d != 0)
		{
			health += 1;
		}

		return health;
	}

	public long getServerTime()
	{
		return _serverTime + (System.currentTimeMillis() - _createTime);
	}

	@Override
	public boolean CanJoinTeam(GameTeam team)
	{
		return team.GetSize() < 2;
	}

	// Ensure 2 players per team
	@Override
	public GameTeam ChooseTeam(Player player)
	{
		GameTeam team = null;

		// Random Team
		for (int i = 0; i < _teamList.size(); i++)
		{
			if (_teamList.get(i).GetSize() == 1)
				return _teamList.get(i);

			if (team == null || _teamList.get(i).GetSize() < team.GetSize())
			{
				team = _teamList.get(i);
			}
		}

		return team;
	}

	@EventHandler
	public void TeamRename(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		for (GameTeam team : GetTeamList())
		{
			// Big Team
			if (team.GetSize() > 2)
			{
				team.SetName("Team " + team.GetName());
				continue;
			}

			String name = "";

			for (int i = 0; i < team.GetPlayers(false).size(); i++)
			{
				Player player = team.GetPlayers(false).get(i);

				name += player.getName();

				if (i < team.GetPlayers(false).size() - 1)
					name += " & ";
			}

			team.SetName(name);
		}
	}

	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		if (killer.equals(killed))
			return 0;

		if (GetTeam(killer) != null && GetTeam(killed) != null && GetTeam(killer).equals(GetTeam(killed)))
			return 0;

		if (assist)
			return 40;

		return 200;
	}

	@EventHandler
	public void damageCancel(EntityDamageEvent event)
	{
		if (!IsLive())
			event.setCancelled(true);

		// Damagee
		Player damagee = null;
		if (event.getEntity() instanceof Player)
		{
			damagee = (Player) event.getEntity();

			// Dead
			if (!IsAlive(damagee))
				event.setCancelled(true);
		}

		// Damager
		LivingEntity damagerEnt = UtilEvent.GetDamagerEntity(event, true);

		if (damagerEnt instanceof Player)
		{
			// PvP
			if (!DamagePvP && damagee != null)
				event.setCancelled(true);

			Player damager = (Player) damagerEnt;

			// Dead
			if (!IsAlive(damager))
				event.setCancelled(true);

			// Same Team
			if (damagee != null)
				if (GetTeam(damager) != null && GetTeam(damagee) != null && GetTeam(damager).equals(GetTeam(damagee)))
				{
					event.setCancelled(true);
				}
		}
	}

	public String getMotdStatus()
	{
		// In Progress
		if (InProgress())
		{
			return ChatColor.YELLOW + "In Progress";
		}

		// Ended
		if (GetState() == GameState.End || GetState() == GameState.Dead)
		{
			return ChatColor.YELLOW + "In Progress";
		}

		// Not Loaded (but still joinable)
		if (!_mapLoaded)
		{
			return ChatColor.GREEN + "Generating Map (" + C.cWhite + getMapLoadETA() + C.cGreen + ")";
		}

		return ChatColor.GREEN + "Recruiting";
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void xrayBlockBreak(BlockBreakEvent event)
	{
		if (event.isCancelled())
			return;

		if (xrayDebug)
			TimingManager.start("Block Break");

		int range = 3;

		// Find Nearby Ores
		ArrayList<Block> ores = findOres(event.getBlock(), range);

		// Anti-Xray
		removeNonAirVeins(generateVeins(ores));

		if (xrayDebug)
			TimingManager.stop("Block Break");
	}

	private ArrayList<Block> findOres(Block source, int range)
	{
		ArrayList<Block> ores = new ArrayList<Block>();

		for (int x = -range; x <= range; x++)
			for (int z = -range; z <= range; z++)
				for (int y = -range; y <= range; y++)
				{
					Block block = source.getRelative(x, y, z);

					findOreFromBlock(ores, block);
				}

		if (xrayDebug)
			for (Block debug : ores)
				System.out.println("Found " + debug.getType() + " at " + UtilWorld.locToStrClean(debug.getLocation()));

		return ores;
	}

	public void findOreFromBlock(ArrayList<Block> ores, Block block)
	{
		if (ores.contains(block))
			return;

		if (isOre(block))
		{
			ores.add(block);

			for (Block neighbour : UtilBlock.getSurrounding(block, true))
			{
				findOreFromBlock(ores, neighbour);
			}
		}
	}

	public boolean isOre(Block block)
	{
		return (block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE || block.getType() == Material.DIAMOND_ORE);
	}

	private ArrayList<ArrayList<Block>> generateVeins(ArrayList<Block> ores)
	{
		ArrayList<ArrayList<Block>> veins = new ArrayList<ArrayList<Block>>();

		while (!ores.isEmpty())
		{
			Block block = ores.remove(0);

			if (xrayDebug)
				System.out.println("NEW VEIN - " + block.getType());

			// Start New Vein
			ArrayList<Block> vein = new ArrayList<Block>();
			veins.add(vein);
			vein.add(block);

			// Find Vein Ores
			boolean addedToVein = true;
			while (addedToVein)
			{
				addedToVein = false;

				Iterator<Block> oreIterator = ores.iterator();

				while (oreIterator.hasNext())
				{
					Block ore = oreIterator.next();

					boolean inVein = false;

					// Check if in Vein
					for (Block veinOre : vein)
					{
						// if (veinOre.getType() != ore.getType())
						// continue;

						if (UtilMath.offset(ore.getLocation(), veinOre.getLocation()) <= 2)
						{
							inVein = true;
							break;
						}
					}

					// Add to Vein
					if (inVein)
					{
						vein.add(ore);
						oreIterator.remove();
						addedToVein = true;
					}
				}
			}

			if (xrayDebug)
				for (Block veinOre : vein)
					System.out.println(UtilWorld.locToStrClean(veinOre.getLocation()));
		}

		return veins;
	}

	private void removeNonAirVeins(ArrayList<ArrayList<Block>> oreVeins)
	{
		// Remove Non-Aired Veins
		for (ArrayList<Block> vein : oreVeins)
		{
			boolean visible = false;

			// Check if Air is near Vein
			for (Block ore : vein)
			{
				for (Block visibleCheckBlock : UtilBlock.getSurrounding(ore, true))
				{
					if (visibleCheckBlock.getType() == Material.AIR || UtilBlock.isVisible(visibleCheckBlock))
					{
						visible = true;
					}

					if (visible)
						break;
				}

				if (visible)
					break;
			}

			// Remove Vein
			if (!visible)
			{
				if (xrayDebug)
					System.out.println("DELETING VEIN;");

				for (Block ore : vein)
				{
					if (xrayDebug)
						System.out.println(ore.getType() + "  " + UtilWorld.locToStrClean(ore.getLocation()));

					ore.setType(Material.STONE);
				}
			}
			else
			{
				if (xrayDebug)
					System.out.println("VALID VEIN!");
			}
		}
	}

	public boolean isMapLoaded()
	{
		return _mapLoaded;
	}

	public String getMapLoadPercent() 
	{
		return (int)(_mapLoadPercent * 100) + "%";
	}
	
	public String getMapLoadETA() 
	{
		int chunksToGo = _chunkTotal - _chunksLoaded;
		
		return UtilTime.MakeStr((long) ((double)chunksToGo / (double)(_chunksPerTick * 20) * 1000d), 1);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void teamSelectInteract(PlayerInteractEntityEvent event)
	{
		if (GetState() != GameState.Recruit)
			return;
		
		if (event.getRightClicked() == null)
			return;
		
		if (!(event.getRightClicked() instanceof Player))
			return;

		Player player = event.getPlayer();

		//Observer
		if (Manager.IsObserver(player))
		{
			UtilPlayer.message(player, F.main("Game", "Spectators cannot partake in games."));
			return;
		}

		selectTeamMate(player, (Player)event.getRightClicked());
	}
	
	@EventHandler
	public void teamSelectCommand(PlayerCommandPreprocessEvent event)
	{
		if (GetState() != GameState.Recruit)
			return;
				
		if (!event.getMessage().toLowerCase().startsWith("/team "))
			return;
		
		event.setCancelled(true);
		
		Player target = UtilPlayer.searchOnline(event.getPlayer(), event.getMessage().split(" ")[1], true);
		if (target == null)
			return;
		
		//Observer
		if (Manager.IsObserver(event.getPlayer()))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Spectators cannot partake in games."));
			return;
		}
		
		if (event.getPlayer().equals(target))
			return;
		
		selectTeamMate(event.getPlayer(), target);
	}

	public void selectTeamMate(Player player, Player ally)
	{
		//Accept Invite
		if (_teamReqs.containsKey(ally) && _teamReqs.get(ally).equals(player))
		{
			//Remove Prefs
			_teamReqs.remove(player);
			_teamReqs.remove(ally);
			
			//Inform
			UtilPlayer.message(player, F.main("Game", "You accepted " + ally.getName() + "'s Team Request!"));
			UtilPlayer.message(ally, F.main("Game", player.getName() + " accepted your Team Request!"));
			
			//Leave Old Teams
			if (GetTeam(player) != null)
				GetTeam(player).DisbandTeam();
			
			if (GetTeam(ally) != null)
				GetTeam(ally).DisbandTeam();
				
			//Get Team
			GameTeam team = getEmptyTeam();
			if (team == null)
				return;
				
			team.setDisplayName(player.getName() + " & " +  ally.getName());
			
			//Join Team
			SetPlayerTeam(player, team, true);
			SetPlayerTeam(ally, team, true);			
		}
		//Send Invite
		else
		{
			//Already on Team with Target
			if (GetTeam(player) != null)
				if (GetTeam(player).HasPlayer(ally))
					return;
				
			//Inform Player
			UtilPlayer.message(player, F.main("Game", "You sent a Team Request to " + ally.getName() + "!"));
			
			//Inform Target
			if (Recharge.Instance.use(player, "Team Req " + ally.getName(), 2000, false, false))
			{
				UtilPlayer.message(ally, F.main("Game", player.getName() + " sent you a Team Request!"));
				UtilPlayer.message(ally, F.main("Game", "Type " + F.elem("/team " + player.getName()) + " to accept!"));
			}
			
			//Add Pref
			_teamReqs.put(player, ally);
		}
	}
	
	@EventHandler
	public void teamQuit(PlayerQuitEvent event)
	{
		if (GetState() != GameState.Recruit)
			return;
		
		Player player = event.getPlayer();
		
		if (GetTeam(player) != null)
			GetTeam(player).DisbandTeam();
		
		Iterator<Player> teamIter = _teamReqs.keySet().iterator();
		while (teamIter.hasNext())
		{
			Player sender = teamIter.next();
			if (sender.equals(player) || _teamReqs.get(sender).equals(player))
				teamIter.remove();
		}
	}
	
	public GameTeam getEmptyTeam()
	{
		for (GameTeam team : GetTeamList())
		{
			if (team.GetPlayers(false).isEmpty())
				return team;
		}
		
		return null;
	}
}