package ehnetwork.game.arcade.game.games.survivalgames;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLargeFireball;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.TeamNameTagVisibility;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_7_R4.EntityArrow;
import net.minecraft.server.v1_7_R4.EntityLargeFireball;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_7_R4.ScoreboardTeam;
import net.minecraft.server.v1_7_R4.TileEntity;
import net.minecraft.server.v1_7_R4.TileEntityChest;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.disguise.disguises.DisguisePlayer;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.loot.ChestLoot;
import ehnetwork.core.loot.RandomItem;
import ehnetwork.core.packethandler.IPacketHandler;
import ehnetwork.core.packethandler.PacketInfo;
import ehnetwork.core.packethandler.PacketPlayOutWorldBorder;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitArcher;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitAssassin;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitAxeman;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBarbarian;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBeastmaster;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBomber;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBrawler;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitHorseman;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitKnight;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitLooter;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitNecromancer;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.stats.FirstSupplyDropOpenStatTracker;
import ehnetwork.game.arcade.stats.KillsWithinTimeLimitStatTracker;
import ehnetwork.game.arcade.stats.SimultaneousSkeletonStatTracker;
import ehnetwork.game.arcade.stats.WinWithoutWearingArmorStatTracker;
import ehnetwork.minecraft.game.core.combat.CombatComponent;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public abstract class SurvivalGames extends Game
{
	// Chest loot
	private ChestLoot _baseLoot = new ChestLoot(true);
	private ChestLoot _spawnLoot = new ChestLoot(true);
	private ChestLoot _crateLoot = new ChestLoot(true);
	private ChestLoot _deathMatchLoot = new ChestLoot(true);

	// Furnace loot
	private ChestLoot _rawFurnace = new ChestLoot(true);
	private ChestLoot _cookedFurnace = new ChestLoot(true);

	private HashMap<Player, HashSet<String>> _hiddenNames = new HashMap<Player, HashSet<String>>();
	private HashSet<Location> _lootedBlocks = new HashSet<Location>();

	// Misc
	private HashMap<Entity, Player> _tntMap = new HashMap<Entity, Player>();
	private HashSet<Location> _placedBlocks = new HashSet<Location>();
	private Location _spawn;

	// Supply Drop
	private ArrayList<Location> _supplyLocations = new ArrayList<Location>();
	private Location _supplyCurrent = null;
	private Location _supplyEffect = null;
	private ArrayList<Block> _supplyCrates = new ArrayList<Block>();
	private HashSet<Location> _landedCrates = new HashSet<Location>();

	// Border
	private int _secondsSinceStart;
	private HashMap<Integer, Double> _borderPositions = new HashMap<Integer, Double>();
	private double _currentBorder;
	private double _previousBorder;
	private long _borderStartedMoving;

	// Deathmatch
	private boolean _deathMatchTeleported = false;
	private int _deathMatchTime = 10 * 60;
	private boolean _informedDeathmatchCommand;
	private int _gameEndTime = 3 * 60;

	private Field _nameTagVisibility;
	private Field _packetTeam;
	private IPacketHandler _useEntityPacketHandler;
	private int _deadBodyCount;

	private int _chestRefillTime = 60 * 7;
	private NautHashMap<TileEntityChest, Integer> _openedChests = new NautHashMap<TileEntityChest, Integer>();
	/**
	 * @The field is originally set to 1, if the next tick finds it at 1, then its set to 10. If the next tick finds it at 10 then
	 *      it removes.
	 * @Else the ticks set it to 50
	 */
	private Field _ticksField;

	public SurvivalGames(ArcadeManager manager, GameType type, String[] description)
	{
		super(manager, type,

				new Kit[]
						{
				new KitAxeman(manager),

				// new KitLooter(manager),

				new KitKnight(manager),

				new KitArcher(manager),

				new KitBrawler(manager),

				new KitAssassin(manager),

				new KitBeastmaster(manager),

				new KitBomber(manager),

				new KitNecromancer(manager),

				new KitBarbarian(manager),

				new KitHorseman(manager),
						}, description);

		_help = new String[]
				{
				C.cGreen + "Use a Compass to find and kill enemies!",

				C.cGreen + "You lose Speed 2 at start of game if you attack.",

				C.cAqua + "Avoid enemies who have better gear than you!"
				};

		// Manager.GetAntiStack().SetEnabled(false);

		StrictAntiHack = true;
		
		HideTeamSheep = true;
		
		this.ReplaceTeamsWithKits = true;

		GameTimeout = 1500000;

		QuitDropItems = true;

		WorldTimeSet = 0;
		WorldBoundaryKill = false;
	
		DamageSelf = true;
		DamageTeamSelf = true;

		DeathDropItems = true;

		ItemDrop = true;
		ItemPickup = true;

		InventoryClick = true;
		InventoryOpenBlock = true;
		InventoryOpenChest = true;

		PlaySoundGameStart = false;
		PrepareTime = 15000;
		
		VersionRequire1_8 = true;

		BlockBreakAllow.add(Material.WEB.getId()); // Web
		BlockPlaceAllow.add(Material.WEB.getId());


		BlockBreakAllow.add(Material.LEAVES.getId()); // Leaves
		BlockBreakAllow.add(Material.LEAVES_2.getId()); // Leaves

		BlockPlaceAllow.add(Material.CAKE_BLOCK.getId());
		BlockBreakAllow.add(Material.CAKE_BLOCK.getId());

		BlockBreakAllow.add(Material.LONG_GRASS.getId());
		BlockBreakAllow.add(Material.RED_ROSE.getId());
		BlockBreakAllow.add(Material.YELLOW_FLOWER.getId());
		BlockBreakAllow.add(Material.BROWN_MUSHROOM.getId());
		BlockBreakAllow.add(Material.RED_MUSHROOM.getId());
		BlockBreakAllow.add(Material.DEAD_BUSH.getId());
		BlockBreakAllow.add(Material.CARROT.getId());
		BlockBreakAllow.add(Material.POTATO.getId());
		BlockBreakAllow.add(Material.DOUBLE_PLANT.getId());
		BlockBreakAllow.add(Material.CROPS.getId());
		BlockBreakAllow.add(Material.SAPLING.getId());
		BlockBreakAllow.add(Material.VINE.getId());
		BlockBreakAllow.add(Material.WATER_LILY.getId());

		// Manager.GetStatsManager().addTable(GetName(), "kills", "deaths", "chestsOpened");

		setupLoot();

		try
		{

			_packetTeam = Class.forName("org.bukkit.craftbukkit.v1_7_R4.scoreboard.CraftTeam").getDeclaredField("team");
			_packetTeam.setAccessible(true);

			_nameTagVisibility = PacketPlayOutScoreboardTeam.class.getDeclaredField("_nameTagVisibility");
			_nameTagVisibility.setAccessible(true);

			_ticksField = TileEntityChest.class.getDeclaredField("ticks");
			_ticksField.setAccessible(true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		_useEntityPacketHandler = new IPacketHandler()
		{
			@Override
			public void handle(PacketInfo packetInfo)
			{
				if (packetInfo.getPacket() instanceof PacketPlayInUseEntity)
				{
					net.minecraft.server.v1_7_R4.Entity entity = ((PacketPlayInUseEntity) packetInfo.getPacket())
							.a(((CraftWorld) packetInfo.getPlayer().getWorld()).getHandle());

					if (entity instanceof EntityArrow)
					{
						packetInfo.setCancelled(true);
					}
				}
			}
		};

		registerStatTrackers(new WinWithoutWearingArmorStatTracker(this), new KillsWithinTimeLimitStatTracker(this, 3, 60,
				"Bloodlust"), new FirstSupplyDropOpenStatTracker(this), new SimultaneousSkeletonStatTracker(this, 5));
	}

	@EventHandler
	public void BlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();

		if (_placedBlocks.remove(block.getLocation()))
		{
			block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
			block.setType(Material.AIR);
		}

		if (block.getType().name().contains("CHEST"))
		{
			for (TileEntityChest tileEntityChest : _openedChests.keySet())
			{
				if (tileEntityChest.x == block.getX() && tileEntityChest.y == block.getY() && tileEntityChest.z == block.getZ())
				{
					WorldServer world = ((CraftWorld) block.getWorld()).getHandle();

					world.playBlockAction(block.getX(), block.getY(), block.getZ(),
							net.minecraft.server.v1_7_R4.Block.getById(block.getTypeId()), 1, 10);

					break;
				}
			}
		}
	}

	@EventHandler
	public void BlockBurn(BlockBurnEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockDecay(LeavesDecayEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockFade(BlockFadeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockSpread(BlockSpreadEvent event)
	{
		event.setCancelled(true);
	}

	private ItemStack buildCompass(int uses)
	{
		ItemBuilder item = new ItemBuilder(Material.COMPASS);
		item.setTitle(C.cWhite + "Player Tracker" + buildTime());

		item.addLore(C.cBlue + "Uses remaining: " + C.cWhite + uses);
		item.addLore(C.cGray + "Use this to find the location and");
		item.addLore(C.cGray + "distance of the nearest player!");

		return item.build();
	}

	private String buildTime()
	{
		String s = "";

		for (char c : ("" + System.nanoTime()).toCharArray())
		{
			s += "§" + c;
		}

		return s;
	}

	@EventHandler
	public void CancelItemFrameBreak(HangingBreakEvent event)
	{
		if (event.getEntity() instanceof ItemFrame)
		{
			event.setCancelled(true);
		}
	}

	// @EventHandler TODO Wait for bulk chunk packet
	public void chestCloseEvent(InventoryCloseEvent event)
	{
		InventoryHolder holder = event.getInventory().getHolder();

		if (holder instanceof DoubleChest)
		{
			holder = (Chest) ((DoubleChest) holder).getLeftSide();
		}

		if (holder instanceof Chest)
		{
			Block block = ((Chest) holder).getBlock();

			TileEntity tileEntity = ((CraftWorld) block.getWorld()).getTileEntityAt(block.getX(), block.getY(), block.getZ());

			if (tileEntity instanceof TileEntityChest)
			{
				TileEntityChest chest = (TileEntityChest) tileEntity;

				try
				{
					chest.o = 10;

					int key = (chest.x + chest.y + chest.z) % 200;

					_ticksField.setInt(chest, (200 - key) + 10);

					_openedChests.put(chest, 10);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	public void refillSecond()
	{
		if (_deathMatchTime <= 60)
			return;

		if (_chestRefillTime <= 0)
			return;

		_chestRefillTime--;

		switch (_chestRefillTime)
		{
		case 0:

			Announce(C.cGold + C.Bold + "The chests have been refilled!", false);

			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.playSound(player.getEyeLocation(), Sound.IRONGOLEM_DEATH, 1000, 0);
			}

			refillChests();

			_chestRefillTime--;
			break;
		case 300:
		case 180:
		case 120:
		case 60:
		case 30:
		case 15:
		case 10:
		case 5:
		case 4:
		case 3:
		case 2:
		case 1:

			String time;

			if (_chestRefillTime >= 60)
			{
				time = (_chestRefillTime / 60) + " minute" + (_chestRefillTime > 60 ? "s" : "");
			}
			else
			{
				time = _chestRefillTime + " second" + (_chestRefillTime != 1 ? "s" : "");
			}

			Announce(C.cGold + C.Bold + "The chests will be refilled in " + time, false);

			break;
		default:
			break;
		}
	}

	// @EventHandler
	public void chestTickEvent(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;

		Iterator<Entry<TileEntityChest, Integer>> itel = _openedChests.entrySet().iterator();

		while (itel.hasNext())
		{
			Entry<TileEntityChest, Integer> entry = itel.next();
			// TODO Test this is removed properly when unloaded chunks
			// TODO Load chests status when chunk loads packets
			try
			{
				int key = (entry.getKey().x + entry.getKey().y + entry.getKey().z) % 200;

				int ticks = (_ticksField.getInt(entry.getKey()) + key) % 200;

				if (ticks == entry.getValue())
				{
					if (ticks == 1)
					{
						System.out.print("Removed");
						itel.remove();
					}
					else
					{
						_ticksField.setInt(entry.getKey(), (200 - key) + ticks - 1);
						_openedChests.put(entry.getKey(), ticks - 1);
					}
				}
				else
				{
					_ticksField.setInt(entry.getKey(), (200 - key) + 10);
					_openedChests.put(entry.getKey(), 10);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@EventHandler
	public void CreateRandomChests(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Recruit)
			return;

		HashSet<Material> ignore = new HashSet<Material>();

		ignore.add(Material.LEAVES);

		int xDiff = WorldData.MaxX - WorldData.MinX;
		int zDiff = WorldData.MaxZ - WorldData.MinZ;

		int done = 0;

		while (done < 40)
		{

			Block block = UtilBlock.getHighest(WorldData.World, WorldData.MinX + UtilMath.r(xDiff),
					WorldData.MinZ + UtilMath.r(zDiff), ignore);

			if (!UtilBlock.airFoliage(block) || !UtilBlock.solid(block.getRelative(BlockFace.DOWN)))
				continue;

			block.setTypeIdAndData(Material.CHEST.getId(), (byte) UtilMath.r(4), true);
			done++;
		}
	}

	@EventHandler
	public void DayNightCycle(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		if (_deathMatchTeleported)
			return;

		long time = WorldData.World.getTime();

		if (time > 22000 || time < 14000)
		{
			WorldTimeSet = (WorldTimeSet + 4) % 24000;
		}
		else
		{
			WorldTimeSet = (WorldTimeSet + 16) % 24000;
		}

		WorldData.World.setTime(WorldTimeSet);
	}

	@EventHandler
	public void onVechilePlace(VehicleCreateEvent event)
	{
		if (event.getVehicle() instanceof Boat)
		{
			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					for (int z = -1; z <= 1; z++)
					{
						Block b = event.getVehicle().getLocation().add(x, y, z).getBlock();

						if (b.isLiquid())
						{
							return;
						}
					}
				}
			}

			event.getVehicle().remove();
		}
	}

	@EventHandler
	public void deathmatchBowShoot(EntityShootBowEvent event)
	{
		if (!_deathMatchTeleported)
			return;

		if (_deathMatchTime <= 0)
			return;

		event.getProjectile().remove();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void DeathmatchDamage(CustomDamageEvent event)
	{
		if (!_deathMatchTeleported)
			return;

		if (_deathMatchTime <= 0)
			return;

		event.SetCancelled("Deathmatch");
	}

	@EventHandler
	public void DeathmatchMoveCancel(PlayerMoveEvent event)
	{
		if (_deathMatchTime <= 0)
			return;

		if (!_deathMatchTeleported)
			return;

		if (UtilMath.offset2d(event.getFrom(), event.getTo()) == 0)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		event.setTo(event.getFrom());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void DeathmatchStart(PlayerCommandPreprocessEvent event)
	{
		if (!event.getMessage().equalsIgnoreCase("/dm"))
			return;

		event.setCancelled(true);

		if (!IsAlive(event.getPlayer()))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You are not in the game."));
			return;
		}

		if (!IsLive() || _deathMatchTime <= 60)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Deathmatch cannot be started now."));
			return;
		}

		if (_secondsSinceStart < 5 * 60 || GetPlayers(true).size() > 4)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Deathmatch cannot be started now."));
			return;
		}

		Announce(C.cGreen + C.Bold + event.getPlayer().getName() + " has initiated Deathmatch!");
		Announce(C.cGreen + C.Bold + "Deathmatch starting in 60 seconds...");

		_deathMatchTime = 60;

		for (Player player : UtilServer.getPlayers())
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
	}

	public void deathmatchSecond()
	{
		if (_deathMatchTime <= 0)
		{
			_gameEndTime--;

			if (_gameEndTime <= 0)
			{
				for (Player player : GetPlayers(true))
				{
					Manager.GetDamage().NewDamageEvent(player, null, null, DamageCause.CUSTOM, 1, false, true, true, "Game End",
							"Game End Damage");
				}
			}

			return;
		}

		_deathMatchTime--;

		if (_deathMatchTime > 0 && _deathMatchTime <= 60)
		{
			if (_deathMatchTime % 30 == 0 || _deathMatchTime == 15 || _deathMatchTime == 10
					|| (_deathMatchTeleported ? _deathMatchTime <= 10 : _deathMatchTime <= 5))
			{
				if (_deathMatchTeleported && _deathMatchTime <= 10)
				{
					Announce(C.cRed + C.Bold + "Deathmatch in " + _deathMatchTime + "...");
				}
				else
				{
					Announce(C.cGreen + C.Bold + "Deathmatch starting in " + _deathMatchTime + " seconds...");
				}
			}
		}

		if (_deathMatchTeleported)
		{
			if (_deathMatchTime == 5)
			{
				for (Player player : GetPlayers(true))
				{
					VisibilityManager.Instance.refreshPlayerToAll(player);
				}
			}
			else if (_deathMatchTime == 0)
			{
				Announce(C.cRed + C.Bold + "Deathmatch has begun!", false);

				_spawn.getWorld().playSound(_spawn, Sound.WITHER_DEATH, 1000, 0);

				refillChests();
			}
		}
		else if (_deathMatchTime == 0)
		{
			_deathMatchTeleported = true;

			WorldTimeSet = 0;
			WorldData.World.setTime(15000);

			for (GameTeam team : GetTeamList())
				team.SpawnTeleport(false);

			_borderPositions.clear();

			_currentBorder = 30.5;
			_previousBorder = 30.5;
			int i = 0;

			for (double border : buildBorders((2 * 30) - 15, 30.5, 7.5))
			{
				_borderPositions.put(_secondsSinceStart + 60 + (i++ * 2), border);
			}

			setBorder();

			_deathMatchTime = 11;
		}
		else
		{
			if (_deathMatchTime <= 60)
				return;

			if (_secondsSinceStart < 5 * 60)
				return;

			if (GetPlayers(true).size() > 4)
				return;

			if (_informedDeathmatchCommand)
				return;

			Announce(C.cGreen + C.Bold + "Type " + ChatColor.RESET + C.Bold + "/dm" + C.cGreen + C.Bold + " to start Deathmatch!");

			_informedDeathmatchCommand = true;
		}
	}

	private BlockFace getFace(Location loc)
	{
		Block block = loc.getBlock();

		while (block.getY() > 0 && !UtilBlock.fullSolid(block.getRelative(BlockFace.DOWN))
				&& !UtilBlock.solid(block.getRelative(BlockFace.DOWN)))
		{
			block = block.getRelative(BlockFace.DOWN);
		}

		BlockFace proper = BlockFace.values()[Math.round(loc.getYaw() / 90F) & 0x3].getOppositeFace();

		// A complicated way to get the face the dead body should be towards.
		for (HashSet<Byte> validBlocks : new HashSet[]
				{
				UtilBlock.blockAirFoliageSet, UtilBlock.blockPassSet
				})
		{

			if (validBlocks.contains((byte) block.getRelative(proper).getTypeId()))
			{
				return proper;
			}

			for (BlockFace face : new BlockFace[]
					{
					BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.WEST
					})
			{
				if (validBlocks.contains((byte) block.getRelative(face).getTypeId()))
				{
					return face;
				}
			}
		}

		return proper;
	}

	private void deathOrQuit(Player player)
	{
		String name = "";

		for (char c : ("" + _deadBodyCount++).toCharArray())
		{
			name += "§" + c;
		}

		try
		{

			Team team = Scoreboard.GetScoreboard().registerNewTeam(name);

			if (!_hiddenNames.get(player).isEmpty())
			{
				ScoreboardTeam nmsTeam = (ScoreboardTeam) _packetTeam.get(team);

				PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(nmsTeam, 2);

				Field teamName = PacketPlayOutScoreboardTeam.class.getDeclaredField("a");
				teamName.setAccessible(true);
				Field displayName = PacketPlayOutScoreboardTeam.class.getDeclaredField("b");
				displayName.setAccessible(true);

				for (Player alive : GetPlayers(true))
				{
					if (_hiddenNames.get(player).contains(alive.getName()))
					{
						teamName.set(packet, alive.getName());
						displayName.set(packet, alive.getName());

						UtilPlayer.sendPacket(player, packet);
					}
				}
			}

			_hiddenNames.remove(player);

			team.setNameTagVisibility(TeamNameTagVisibility.NEVER);
			team.addEntry(name);

			PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam((ScoreboardTeam) _packetTeam.get(team), 2);

			for (Player alive : GetPlayers(false))
			{
				UtilPlayer.sendPacket(alive, packet);
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		GameProfile newProfile = new GameProfile(UUID.randomUUID(), name);

		newProfile.getProperties().putAll(((CraftPlayer) player).getHandle().getProfile().getProperties());

		DisguisePlayer disguise = new DisguisePlayer(null, newProfile);

		disguise.setSleeping(getFace(player.getLocation()));

		getArcadeManager().GetDisguise().addFutureDisguise(disguise);

		Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARROW);

		try
		{
			EntityArrow entityArrow = ((CraftArrow) entity).getHandle();

			Field at = EntityArrow.class.getDeclaredField("at");
			at.setAccessible(true);
			at.set(entityArrow, Integer.MIN_VALUE); // Despawn time
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void DisableDamageLevel(CustomDamageEvent event)
	{
		event.SetDamageToLevel(false);
	}

	@EventHandler
	public void SnowballEggsDamage(CustomDamageEvent event)
	{
		if (event.GetProjectile() instanceof Snowball || event.GetProjectile() instanceof Egg)
		{
			event.AddMod("Projectile", "Projectile", 0.01, false);
		}
	}

	@EventHandler
	public void DisallowBrewingStand(PlayerInteractEvent event)
	{
		if (event.getClickedBlock() == null)
			return;

		if (event.getClickedBlock().getType() == Material.BREWING_STAND)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void ExplosionDamageRemove(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}

	private void fillChest(Player looter, Block block)
	{
		_lootedBlocks.add(block.getLocation());

		Chest chest = (Chest) block.getState();

		chest.getBlockInventory().clear();

		int items = 2;
		if (Math.random() > 0.50)
			items++;
		if (Math.random() > 0.65)
			items++;
		if (Math.random() > 0.80)
			items++;
		if (Math.random() > 0.95)
			items++;

		boolean spawnChest = _chestRefillTime > 0 && UtilMath.offset(chest.getLocation(), _spawn) < 8;

		if (spawnChest)
			items += 3;

		if (GetKit(looter) instanceof KitLooter)
		{
			items += UtilMath.r(3);
		}

		if (_supplyCrates.contains(block))
		{
			items = 2;
			if (Math.random() > 0.75)
				items++;
			if (Math.random() > 0.95)
				items++;
		}

		for (int i = 0; i < items; i++)
		{
			ItemStack item;

			if (spawnChest)
			{
				item = _spawnLoot.getLoot();
			}
			else if (_deathMatchTeleported)
			{
				item = _deathMatchLoot.getLoot();
			}
			else
			{
				item = GetChestItem(_supplyCrates.contains(block));
			}

			if (item.getType() == Material.COMPASS)
			{
				item = buildCompass(5);
			}

			chest.getBlockInventory().setItem(UtilMath.r(27), item);
		}

		if (_supplyCrates.contains(block))
		{
			Bukkit.getPluginManager().callEvent(new SupplyChestOpenEvent(looter, block));
		}

		_supplyCrates.remove(block);
	}

	private ItemStack GetChestItem(boolean superChest)
	{
		if (superChest)
			return _crateLoot.getLoot();

		return _baseLoot.getLoot();
	}

	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		if (assist)
			return 3;
		else
			return 12;
	}

	@EventHandler
	public void handleEntityPacket(GameStateChangeEvent event)
	{
		if (event.GetState() == GameState.Live)
		{
			getArcadeManager().getPacketHandler().addPacketHandler(_useEntityPacketHandler);
		}
		else if (event.GetState() == GameState.Dead)
		{
			getArcadeManager().getPacketHandler().removePacketHandler(_useEntityPacketHandler);
		}
	}

	// If an item spawns and no one is there to see it, does it really spawn? No.
	@EventHandler
	public void ItemSpawn(ItemSpawnEvent event)
	{
		Material mat = event.getEntity().getItemStack().getType();

		switch (mat)
		{
		case SEEDS:
		case SAPLING:
		case VINE:
		case LEAVES:
		case LONG_GRASS:
		case RED_ROSE:
		case YELLOW_FLOWER:
		case DEAD_BUSH:
		case WATER_LILY:
			event.setCancelled(true);
			return;
		case CARROT_ITEM:
			if (UtilMath.r(10) != 0)
			{
				event.setCancelled(true);
			}
			return;
		case POTATO_ITEM:
			if (UtilMath.r(10) != 0)
			{
				event.setCancelled(true);
			}
			return;
		case WHEAT:
			if (UtilMath.r(6) != 0)
			{
				event.setCancelled(true);
			}
			return;
		case WOOD:
			event.setCancelled(true);
			return;

		default:
			break;
		}

		for (Player player : GetPlayers(true))
			if (UtilMath.offset(player, event.getEntity()) < 6)
				return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onGameEnd(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.End)
			return;

		new BukkitRunnable()
		{
			public void run()
			{
				if (GetState() != GameState.End)
				{
					cancel();
					return;
				}

				for (Location loc : GetTeamList().get(0).GetSpawns())
				{
					Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);

					FireworkMeta meta = firework.getFireworkMeta();

					meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).with(Type.BALL).withTrail().build());

					firework.setFireworkMeta(meta);
				}
			}
		}.runTaskTimer(getArcadeManager().getPlugin(), 0, 60);
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

		refillSecond();
		deathmatchSecond();
	}

	@EventHandler
	public void onUse(PlayerInteractEvent event)
	{
		if (!IsLive())
			return;

		Player player = event.getPlayer();

		if (!IsAlive(player))
			return;

		if (!event.getAction().name().contains("RIGHT"))
			return;

		ItemStack item = event.getItem();

		if (item == null || item.getType() != Material.COMPASS)
			return;

		int uses = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(0)).replaceAll("\\D+", ""));

		if (uses > 0)
		{
			uses--;

			Player closestPlayer = null;
			double closestDistance = 0;

			for (Player alive : GetPlayers(true))
			{
				if (alive != player)
				{
					double distance = alive.getLocation().distance(player.getLocation());

					if (distance > 10 && (closestPlayer == null || distance < closestDistance))
					{
						closestDistance = distance;
						closestPlayer = alive;
					}
				}
			}

			if (closestPlayer != null)
			{
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 0);

				player.setCompassTarget(closestPlayer.getLocation());
				player.setItemInHand(buildCompass(uses));

				player.sendMessage(F.main("Compass", "Located " + closestPlayer.getName() + " " + (int) closestDistance
						+ " blocks away"));

				if (uses >= 1)
				{
					player.sendMessage(F.main("Compass", uses + " use" + (uses > 1 ? "s" : "") + " of the compass remaining."));
				}
				else
				{
					player.sendMessage(F.main("Compass", "No remaining uses! Next use will break it!"));
				}
			}
			else
			{
				player.sendMessage(F.main("Compass", "Can't find anyone! Pointing to spawn!"));
				player.setCompassTarget(_spawn);
			}
		}
		else
		{
			player.sendMessage(F.main("Compass", "The compass breaks! No remaining uses!"));

			player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 5);

			player.setItemInHand(new ItemStack(Material.AIR));
		}
	}

	@EventHandler
	public void OpenChest(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		Block block = event.getClickedBlock();

		if (block == null)
			return;

		if (!IsLive())
			return;

		if (_lootedBlocks.contains(block.getLocation()))
			return;

		BlockState state = block.getState();

		if (state instanceof DoubleChest)
		{
			DoubleChest doubleChest = (DoubleChest) state;

			fillChest(event.getPlayer(), ((Chest) doubleChest.getLeftSide()).getBlock());
			fillChest(event.getPlayer(), ((Chest) doubleChest.getRightSide()).getBlock());
		}
		else if (state instanceof Chest)
		{
			fillChest(event.getPlayer(), block);
		}
		else if (state instanceof Furnace)
		{
			Furnace furnace = (Furnace) state;

			if (furnace.getCookTime() == 0)
			{
				FurnaceInventory inv = furnace.getInventory();

				if (UtilMath.r(3) == 0)
				{
					int random = UtilMath.r(9);

					if (random == 0)
					{
						inv.setFuel(new ItemStack(Material.STICK, new Random().nextInt(2) + 1));
					}
					else if (random <= 3)
					{
						inv.setSmelting(_rawFurnace.getLoot());
					}
					else
					{
						inv.setResult(_cookedFurnace.getLoot());
					}
				}

				_lootedBlocks.add(block.getLocation());
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

		//24 @ 100+    reduced to    0 at 32-
		double borderAttackDist = Math.max(8, (Math.min(100, border) - 28d) / 3d);
		double borderCheckDist = borderAttackDist + 6;
		
		for (Player player : UtilServer.getPlayers())
		{
			Location loc = player.getLocation();

			//Bump Players Back In
			if (loc.getX() > _spawn.getX() + border || 
					loc.getX() < _spawn.getX() - border || 
					loc.getZ() > _spawn.getZ() + border	|| 
					loc.getZ() < _spawn.getZ() - border)
			{
				if (Recharge.Instance.use(player, "Hit by Border", 1000, false, false))
				{
					Entity bottom = player;
					while (bottom.getVehicle() != null)
						bottom = bottom.getVehicle();
					
					UtilAction.velocity(bottom, UtilAlg.getTrajectory2d(loc, GetSpectatorLocation()), 1.2, true, 0.4, 0, 10, true);

					if (Manager.IsAlive(player))
					{
						Manager.GetDamage().NewDamageEvent(player, null, null, DamageCause.CUSTOM, 10, false, false, false, "Nether Field",
								"Vaporize");

						player.getWorld().playSound(loc, Sound.NOTE_BASS, 2f, 1f);
						player.getWorld().playSound(loc, Sound.NOTE_BASS, 2f, 1f);
					}
				}
			}

			if (border < 32)
				continue;
			
			//Attack Players who are nearby
			boolean isX = true;
			Location attackSource = null;
			if (loc.getX() > _spawn.getX() + (border-borderCheckDist))
			{
				attackSource = player.getLocation();
				attackSource.setX(_spawn.getX() + border);
			}
			else if (loc.getX() < _spawn.getX() - (border-borderCheckDist))
			{
				attackSource = player.getLocation();
				attackSource.setX(_spawn.getX() - border);
			}
			else if (loc.getZ() > _spawn.getZ() + (border-borderCheckDist))
			{
				attackSource = player.getLocation();
				attackSource.setZ(_spawn.getZ() + border);
				isX = false;
			}
			else if (loc.getZ() < _spawn.getZ() - (border-borderCheckDist))
			{
				attackSource = player.getLocation();
				attackSource.setZ(_spawn.getZ() - border);
				isX = false;
			}

			if (attackSource != null)
			{
				double dist = UtilMath.offset(player.getLocation(), attackSource);
				
				double scale = 1 - (dist / borderAttackDist);
				
				player.playSound(player.getLocation().add(UtilAlg.getTrajectory(player.getLocation(), attackSource).multiply(8)), 
						Sound.PORTAL, (float)(1 - (dist / borderCheckDist)) * 2, 2f);

				if (!Manager.IsAlive(player))
					continue;
				
				//Shoot more frequently when they get closer
				if (dist < borderAttackDist && Math.random() < scale)
				{			 
					//Spawn Fireball
					Location spawn = attackSource.clone();
					spawn.add(isX ? 0 : (Math.random()-0.5)*12, 4 + Math.random() * 2 + (Math.random() * 12 * scale), isX ? (Math.random()-0.5)*12 : 0);
					
					//Raytrace back
					double maxBack = 8; 
					double back = 0;
					while (spawn.getBlock().getType() == Material.AIR && back < maxBack)
					{
						spawn.subtract(UtilAlg.getTrajectory(spawn, player.getLocation()).multiply(0.2));
						back += 0.1;
					}
					
					//Move out of block
					spawn.add(UtilAlg.getTrajectory(spawn, player.getLocation()).multiply(Math.min(back, 1)));
					
					
					Fireball ball = player.getWorld().spawn(spawn, Fireball.class);	

					//Trajectory
					Vector traj = UtilAlg.getTrajectory(spawn, player.getLocation());
					traj.add(new Vector((Math.random()-0.5)*0.2,(Math.random()-0.5)*0.2,(Math.random()-0.5)*0.2));

					EntityLargeFireball eFireball = ((CraftLargeFireball) ball).getHandle();
					eFireball.dirX = traj.getX() * 0.1;
					eFireball.dirY = traj.getY() * 0.1;
					eFireball.dirZ = traj.getZ() * 0.1;

					UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, spawn, 0, 0, 0, 0, 1,
							ViewDist.MAX, UtilServer.getPlayers());
					player.getWorld().playSound(attackSource, Sound.GHAST_FIREBALL, 2f, 2f);
				}	
			}
		}
	}

	@EventHandler
	public void borderBlockDamage(ProjectileHitEvent event)
	{
		if (!IsLive())
			return;
		
		if (!(event.getEntity() instanceof Fireball))
			return; 

		Collection<Block> blocks = UtilBlock.getInRadius(event.getEntity().getLocation(), 2.4).keySet();

		Manager.GetExplosion().BlockExplosion(blocks, event.getEntity().getLocation(), false);	
	}

	public boolean isStableBlock(Block block)
	{
		int sides = 0;
		if (UtilBlock.solid(block.getRelative(BlockFace.NORTH)))	sides++;
		if (UtilBlock.solid(block.getRelative(BlockFace.EAST)))		sides++;
		if (UtilBlock.solid(block.getRelative(BlockFace.SOUTH)))	sides++;
		if (UtilBlock.solid(block.getRelative(BlockFace.WEST)))		sides++;
		
		return sides >= 3;
	}
	
	@EventHandler
	public void borderDamage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;

		if (!(event.GetProjectile() instanceof Fireball))
			return;

		event.AddMult("Fireball", "Nether Field", 1, true);

		event.AddKnockback("Fireball", 2);
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

	@Override
	public void ParseData()
	{
		_spawn = UtilWorld.averageLocation(GetTeamList().get(0).GetSpawns());

		ArrayList<Double> borders = new ArrayList<Double>();

		borders.add(WorldData.MaxX - _spawn.getX());
		borders.add(_spawn.getX() - WorldData.MinX);
		borders.add(WorldData.MaxZ - _spawn.getZ());
		borders.add(_spawn.getZ() - WorldData.MinZ);

		Collections.sort(borders);

		double largestBorder = borders.get(3);
		int i = 0;

		for (double border : buildBorders(10 * 30, largestBorder, 30.5))
		{
			_borderPositions.put(i++ * 2, border);
		}

		_currentBorder = _borderPositions.get(0);
		_previousBorder = _currentBorder;

		for (Location loc : GetTeamList().get(0).GetSpawns())
			loc.setYaw(UtilAlg.GetYaw(UtilAlg.getTrajectory(loc, _spawn)));

		setupChestsEnchantingCrafting();

		_supplyLocations = WorldData.GetDataLocs("WHITE");
		for (Location loc : _supplyLocations)
			loc.getBlock().setType(Material.GLASS);
	}

	@EventHandler
	public void PlayerKill(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player player = (Player) event.GetEvent().getEntity();

		deathOrQuit(player);

		FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BALL_LARGE).trail(false)
				.build();
		for (int i = 0; i < 3; i++)
			UtilFirework.launchFirework(player.getLocation(), effect, null, 3);
	}
	
	@EventHandler
	public void killLevelReward(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player)event.GetEvent().getEntity();

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer != null && !killer.equals(killed))
			{
				//Kill
				killer.giveExpLevels(2);
				
				killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1f, 1f);
			}
		}

		for (CombatComponent log : event.GetLog().GetAttackers())
		{
			if (event.GetLog().GetKiller() != null && log.equals(event.GetLog().GetKiller()))
				continue;

			Player assist = UtilPlayer.searchExact(log.GetName());

			//Assist
			if (assist != null)
			{
				assist.giveExpLevels(1);
				assist.playSound(assist.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
			}
			
		}
	}

	@EventHandler
	public void preventCrafting(PrepareItemCraftEvent event)
	{
		ItemStack result = event.getInventory().getResult();

		if (result != null)
		{
			Material type = result.getType();

			if (type == Material.BUCKET || type == Material.GOLDEN_APPLE || type == Material.FLINT_AND_STEEL || type.isBlock())
			{
				event.getInventory().setResult(new ItemStack(Material.AIR));
			}
		}
	}

	public void refillChests()
	{
		ArrayList<Location> list = new ArrayList<Location>(_lootedBlocks);

		_lootedBlocks.clear();

		WorldServer world = list.isEmpty() ? null : ((CraftWorld) list.get(0).getWorld()).getHandle();

		for (Location loc : list)
		{
			boolean open = false;

			if (loc.getChunk().isLoaded())
			{
				Block block = loc.getBlock();

				if (block.getState() instanceof InventoryHolder)
				{
					InventoryHolder holder = (InventoryHolder) block.getState();

					if (!holder.getInventory().getViewers().isEmpty())
					{
						open = true;

						if (_landedCrates.contains(loc))
							continue;

						fillChest((Player) holder.getInventory().getViewers().get(0), block);
					}
				}
			}

			if (!open)
			{
				world.playBlockAction(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
						net.minecraft.server.v1_7_R4.Block.getById(loc.getBlock().getTypeId()), 1, 0);

				Iterator<TileEntityChest> itel = _openedChests.keySet().iterator();

				while (itel.hasNext())
				{
					TileEntityChest tile = itel.next();

					if (tile.x == loc.getBlockX() && tile.y == loc.getBlockY() && tile.z == loc.getBlockZ())
					{
						itel.remove();
					}
				}
			}
		}
	}

	@EventHandler
	public void RemoveNametagInfo(PlayerQuitEvent event)
	{
		if (!IsLive())
		{
			return;
		}

		if (!_hiddenNames.containsKey(event.getPlayer()))
		{
			return;
		}

		deathOrQuit(event.getPlayer());

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

	private void setupBorder(Player player)
	{
		PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder();
		packet.worldBorderType = 3;

		packet.centerX = _spawn.getX();
		packet.centerZ = _spawn.getZ();

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

	private void setupChestsEnchantingCrafting()
	{
		ArrayList<Location> chests = WorldData.GetCustomLocs("54");

		System.out.println("Map Chest Locations: " + chests.size());

		// Enchants
		System.out.println("Enchanting Tables: " + Math.min(5, chests.size()));
		for (int i = 0; i < 5 && !chests.isEmpty(); i++)
		{
			Location loc = chests.remove(UtilMath.r(chests.size()));
			loc.getBlock().setType(Material.ENCHANTMENT_TABLE);
		}

		// Crafting
		System.out.println("Crafting Benches: " + Math.min(10, chests.size()));
		for (int i = 0; i < 10 && !chests.isEmpty(); i++)
		{
			Location loc = chests.remove(UtilMath.r(chests.size()));
			loc.getBlock().setType(Material.WORKBENCH);
		}

		int spawn = 0;

		// Chests
		System.out.println("Chests: " + Math.min(250, chests.size()));
		for (int i = 0; i < 250 && !chests.isEmpty(); i++)
		{
			Location loc = chests.remove(UtilMath.r(chests.size()));

			if (UtilMath.offset2d(loc, _spawn) < 8)
				spawn++;
		}

		for (Location loc : chests)
		{
			if (spawn < 10 && UtilMath.offset(loc, _spawn) < 8)
			{
				spawn++;
				continue;
			}

			loc.getBlock().setType(Material.AIR);
		}
	}

	private void setupLoot()
	{
		// Food
		_baseLoot.addLoot(new RandomItem(Material.BAKED_POTATO, 30, 1, 3));
		_baseLoot.addLoot(new RandomItem(Material.COOKED_BEEF, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.COOKED_CHICKEN, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.CARROT_ITEM, 30, 1, 3));
		_baseLoot.addLoot(new RandomItem(Material.MUSHROOM_SOUP, 15, 1, 1));
		_baseLoot.addLoot(new RandomItem(Material.WHEAT, 30, 1, 6));
		_baseLoot.addLoot(new RandomItem(Material.APPLE, 30, 1, 4));
		_baseLoot.addLoot(new RandomItem(Material.PORK, 30, 1, 4));
		_baseLoot.addLoot(new RandomItem(Material.ROTTEN_FLESH, 40, 1, 6));

		// Weapons
		_baseLoot.addLoot(new RandomItem(Material.WOOD_AXE, 80));
		_baseLoot.addLoot(new RandomItem(Material.WOOD_SWORD, 70));
		_baseLoot.addLoot(new RandomItem(Material.STONE_AXE, 60));
		_baseLoot.addLoot(new RandomItem(Material.STONE_SWORD, 30));

		// Leather armor
		_baseLoot.addLoot(new RandomItem(Material.LEATHER_BOOTS, 30));
		_baseLoot.addLoot(new RandomItem(Material.LEATHER_CHESTPLATE, 30));
		_baseLoot.addLoot(new RandomItem(Material.LEATHER_HELMET, 30));
		_baseLoot.addLoot(new RandomItem(Material.LEATHER_LEGGINGS, 30));

		// Gold armor
		_baseLoot.addLoot(new RandomItem(Material.GOLD_BOOTS, 25));
		_baseLoot.addLoot(new RandomItem(Material.GOLD_CHESTPLATE, 25));
		_baseLoot.addLoot(new RandomItem(Material.GOLD_HELMET, 25));
		_baseLoot.addLoot(new RandomItem(Material.GOLD_LEGGINGS, 25));

		// Chain armor
		_baseLoot.addLoot(new RandomItem(Material.CHAINMAIL_BOOTS, 20));
		_baseLoot.addLoot(new RandomItem(Material.CHAINMAIL_CHESTPLATE, 20));
		_baseLoot.addLoot(new RandomItem(Material.CHAINMAIL_HELMET, 20));
		_baseLoot.addLoot(new RandomItem(Material.CHAINMAIL_LEGGINGS, 20));

		// Throwable
		_baseLoot.addLoot(new RandomItem(Material.FISHING_ROD, 30));
		_baseLoot.addLoot(new RandomItem(Material.BOW, 20));
		_baseLoot.addLoot(new RandomItem(Material.ARROW, 20, 1, 3));
		_baseLoot.addLoot(new RandomItem(Material.SNOW_BALL, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.EGG, 30, 1, 2));

		// Misc
		_baseLoot.addLoot(new RandomItem(Material.EXP_BOTTLE, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.COMPASS, 20));
		_baseLoot.addLoot(new RandomItem(Material.STICK, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.BOAT, 15));
		_baseLoot.addLoot(new RandomItem(Material.FLINT, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.FEATHER, 30, 1, 2));
		_baseLoot.addLoot(new RandomItem(Material.GOLD_INGOT, 20));
		_baseLoot.addLoot(new RandomItem(ItemStackFactory.Instance.CreateStack(Material.TNT, (byte)0, 1, F.item("Throwing TNT")), 15));
		_spawnLoot.addLoot(new RandomItem(Material.MUSHROOM_SOUP, 15));

		_spawnLoot.cloneLoot(_baseLoot);

		// Food
		_spawnLoot.addLoot(new RandomItem(Material.BAKED_POTATO, 30, 1, 5));
		_spawnLoot.addLoot(new RandomItem(Material.CAKE, 30));
		_spawnLoot.addLoot(new RandomItem(Material.MUSHROOM_SOUP, 30, 1, 1));
		_spawnLoot.addLoot(new RandomItem(Material.COOKED_BEEF, 30, 1, 3));
		_spawnLoot.addLoot(new RandomItem(Material.COOKED_CHICKEN, 30, 1, 3));
		_spawnLoot.addLoot(new RandomItem(Material.COOKED_FISH, 30, 1, 6));
		_spawnLoot.addLoot(new RandomItem(Material.GRILLED_PORK, 30, 1, 3));
		_spawnLoot.addLoot(new RandomItem(Material.COOKIE, 30));
		_spawnLoot.addLoot(new RandomItem(Material.PUMPKIN_PIE, 30, 1, 3));
		_spawnLoot.addLoot(new RandomItem(Material.APPLE, 30, 2, 6));

		// Loot for chests in spawn
		// Weaponry and ores
		_spawnLoot.addLoot(new RandomItem(Material.STONE_SWORD, 30));
		_spawnLoot.addLoot(new RandomItem(Material.IRON_AXE, 30));
		_spawnLoot.addLoot(new RandomItem(Material.IRON_INGOT, 30, 1, 2));
		_spawnLoot.addLoot(new RandomItem(Material.DIAMOND, 30));

		// Iron gear
		_spawnLoot.addLoot(new RandomItem(Material.IRON_BOOTS, 30));
		_spawnLoot.addLoot(new RandomItem(Material.IRON_CHESTPLATE, 30));
		_spawnLoot.addLoot(new RandomItem(Material.IRON_HELMET, 30));
		_spawnLoot.addLoot(new RandomItem(Material.IRON_LEGGINGS, 30));

		// Supply crate loot
		// Diamond gear
		_crateLoot.addLoot(new RandomItem(Material.DIAMOND_HELMET, 10));
		_crateLoot.addLoot(new RandomItem(Material.DIAMOND_CHESTPLATE, 6));
		_crateLoot.addLoot(new RandomItem(Material.DIAMOND_LEGGINGS, 8));
		_crateLoot.addLoot(new RandomItem(Material.DIAMOND_BOOTS, 10));

		// Iron gear
		_crateLoot.addLoot(new RandomItem(Material.IRON_HELMET, 30));
		_crateLoot.addLoot(new RandomItem(Material.IRON_CHESTPLATE, 24));
		_crateLoot.addLoot(new RandomItem(Material.IRON_LEGGINGS, 27));
		_crateLoot.addLoot(new RandomItem(Material.IRON_BOOTS, 30));

		// Weapons
		_crateLoot.addLoot(new RandomItem(Material.IRON_SWORD, 24));
		_crateLoot.addLoot(new RandomItem(Material.DIAMOND_SWORD, 8));
		_crateLoot.addLoot(new RandomItem(Material.DIAMOND_AXE, 16));

		// Cooked furnace
		_cookedFurnace.addLoot(new RandomItem(Material.COOKED_BEEF, 3, 1, 2));
		_cookedFurnace.addLoot(new RandomItem(Material.COOKED_CHICKEN, 3, 1, 2));
		_cookedFurnace.addLoot(new RandomItem(Material.COOKED_FISH, 3, 1, 2));
		_cookedFurnace.addLoot(new RandomItem(Material.GRILLED_PORK, 3, 1, 2));
		_cookedFurnace.addLoot(new RandomItem(Material.BAKED_POTATO, 3, 1, 1));
		_cookedFurnace.addLoot(new RandomItem(Material.PUMPKIN_PIE, 3, 1, 1));
		_cookedFurnace.addLoot(new RandomItem(Material.IRON_INGOT, 1, 1, 1));

		// Raw furnace
		_rawFurnace.addLoot(new RandomItem(Material.RAW_BEEF, 1, 1, 3));
		_rawFurnace.addLoot(new RandomItem(Material.RAW_CHICKEN, 1, 1, 3));
		_rawFurnace.addLoot(new RandomItem(Material.RAW_FISH, 1, 1, 3));
		_rawFurnace.addLoot(new RandomItem(Material.PORK, 1, 1, 3));
		_rawFurnace.addLoot(new RandomItem(Material.POTATO_ITEM, 1, 1, 3));

		// Deathmatch Loot
		_deathMatchLoot.addLoot(new RandomItem(Material.PUMPKIN_PIE, 4));
		_deathMatchLoot.addLoot(new RandomItem(Material.BAKED_POTATO, 4));
		_deathMatchLoot.addLoot(new RandomItem(Material.CAKE, 4));
		_deathMatchLoot.addLoot(new RandomItem(Material.APPLE, 4));
		_deathMatchLoot.addLoot(new RandomItem(Material.CARROT_ITEM, 4));
		_deathMatchLoot.addLoot(new RandomItem(Material.WOOD_SWORD, 3));
		_deathMatchLoot.addLoot(new RandomItem(Material.WOOD_AXE, 3));
		_deathMatchLoot.addLoot(new RandomItem(Material.STONE_AXE, 3));
		_deathMatchLoot.addLoot(new RandomItem(Material.STONE_SWORD, 1));
	}

	@EventHandler
	public void SpeedRemove(CustomDamageEvent event)
	{
		Player damager = event.GetDamagerPlayer(true);
		if (damager != null)
			Manager.GetCondition().EndCondition(damager, null, "Start Speed");
	}

	@EventHandler
	public void StartEffectApply(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		Scoreboard board = GetScoreboard().GetScoreboard();

		for (Player player : GetPlayers(true))
		{
			player.playSound(player.getLocation(), Sound.DONKEY_DEATH, 0.8F, 0);

			Manager.GetCondition().Factory().Speed("Start Speed", player, player, 30, 1, false, false, false);
			Manager.GetCondition().Factory().HealthBoost("Start Health", player, player, 30, 1, false, false, false);

			player.setHealth(player.getMaxHealth());

			Team team = board.registerNewTeam(player.getName());

			team.setPrefix(board.getPlayerTeam(player).getPrefix());

			team.addPlayer(player);

			_hiddenNames.put(player, new HashSet<String>());
		}
	}

	@EventHandler
	public void SupplyDrop(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FASTEST)
			return;

		long time = WorldData.World.getTime();

		if (time > 14000 && time < 23000)
		{
			if (_supplyCurrent == null && !_deathMatchTeleported)
			{
				if (_supplyLocations.isEmpty())
					return;

				_supplyCurrent = _supplyLocations.remove(UtilMath.r(_supplyLocations.size()));

				// Remove Prior
				_supplyCrates.remove(_supplyCurrent.getBlock().getRelative(BlockFace.UP));
				_supplyCurrent.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);

				// Create New
				_supplyCurrent.getBlock().setType(Material.BEACON);
				for (int x = -1; x <= 1; x++)
					for (int z = -1; z <= 1; z++)
						_supplyCurrent.getBlock().getRelative(x, -1, z).setType(Material.IRON_BLOCK);

				// Announce
				Announce(C.cYellow + C.Bold + "Supply Drop Incoming (" + ChatColor.RESET
						+ UtilWorld.locToStrClean(_supplyCurrent) + C.cYellow + C.Bold + ")");
			}
		}
		else
		{
			if (_supplyCurrent != null)
			{
				if (_supplyEffect == null)
				{
					_supplyEffect = _supplyCurrent.clone();
					_supplyEffect.setY(250);
				}

				FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).with(Type.BURST)
						.trail(false).build();
				UtilFirework.playFirework(_supplyEffect, effect);

				_supplyEffect.setY(_supplyEffect.getY() - 2);

				if (UtilMath.offset(_supplyEffect, _supplyCurrent) < 2)
				{
					effect = FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).with(Type.BALL_LARGE).trail(true)
							.build();
					UtilFirework.playFirework(_supplyEffect, effect);

					// Create Chest
					_supplyCurrent.getBlock().setType(Material.GLASS);

					Block block = _supplyCurrent.getBlock().getRelative(BlockFace.UP);
					block.setType(Material.CHEST);
					_landedCrates.add(block.getLocation());
					_supplyCrates.add(block);
					_lootedBlocks.remove(_supplyCurrent);

					// Reset
					_supplyEffect = null;
					_supplyCurrent = null;
				}
			}
		}
	}

	@EventHandler
	public void SupplyGlow(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		if (_supplyCrates.isEmpty())
			return;

		Iterator<Block> chestIterator = _supplyCrates.iterator();

		while (chestIterator.hasNext())
		{
			Block block = chestIterator.next();

			if (block.getType() != Material.CHEST)
			{
				chestIterator.remove();
				continue;
			}

			UtilParticle.PlayParticle(ParticleType.SPELL, block.getLocation().add(0.5, 0.5, 0.5), 0.3f, 0.3f, 0.3f, 0, 1,
					ViewDist.LONG, UtilServer.getPlayers());
		}
	}

	@EventHandler
	public void TNTDelay(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		for (Player player : UtilServer.getPlayers())
			Recharge.Instance.useForce(player, "Throw TNT", 30000);
	}

	@EventHandler
	public void TNTExplosion(ExplosionPrimeEvent event)
	{
		if (!_tntMap.containsKey(event.getEntity()))
			return;

		Player player = _tntMap.remove(event.getEntity());

		/*CustomExplosion explosion = new CustomExplosion(getArcadeManager().GetDamage(), event.getEntity().getLocation(),
				((TNTPrimed) event.getEntity()).getYield(), "Throwing TNT");

		explosion.setPlayer(player, true);*/

		for (Player other : UtilPlayer.getNearby(event.getEntity().getLocation(), 14))
			Manager.GetCondition().Factory().Explosion("Throwing TNT", other, player, 50, 0.1, false, false);
	}

	@EventHandler
	public void TNTThrow(PlayerInteractEvent event)
	{
		if (!IsLive())
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		Player player = event.getPlayer();

		if (!UtilInv.IsItem(player.getItemInHand(), Material.TNT, (byte) 0))
			return;

		event.setCancelled(true);

		if (!Recharge.Instance.use(player, "Throw TNT", 0, true, false))
		{
			UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot use " + F.item("Throw TNT") + " yet."));
			return;
		}

		if (!Manager.GetGame().CanThrowTNT(player.getLocation()))
		{
			// Inform
			UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot use " + F.item("Throw TNT") + " here."));
			return;
		}

		UtilInv.remove(player, Material.TNT, (byte) 0, 1);
		UtilInv.Update(player);

		TNTPrimed tnt = player.getWorld()
				.spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);

		tnt.setFuseTicks(60);

		UtilAction.velocity(tnt, player.getLocation().getDirection(), 0.5, false, 0, 0.1, 10, false);

		_tntMap.put(tnt, player);
	}

	@EventHandler
	public void TourneyKills(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player) event.GetEvent().getEntity();

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer != null && !killer.equals(killed))
			{
				// Manager.GetStatsManager().addStat(killer, GetName(), "kills", 1);
			}
		}

		if (event.GetLog().GetPlayer() != null)
		{
			if (killed != null)
			{
				// Manager.GetStatsManager().addStat(killed, GetName(), "deaths", 1);
			}
		}
	}

	@EventHandler
	public void UpdateNametagVisibility(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (!IsLive())
			return;

		ArrayList<Player> alivePlayers = new ArrayList<Player>(_hiddenNames.keySet());
		HashMap<Player, HashMap<Player, Boolean>> checkedPlayers = new HashMap<Player, HashMap<Player, Boolean>>();

		for (Player target : alivePlayers)
		{

			PacketPlayOutScoreboardTeam packet = null;

			try
			{
				ScoreboardTeam nmsTeam = (ScoreboardTeam) _packetTeam.get(target.getScoreboard().getTeam(target.getName()));

				packet = new PacketPlayOutScoreboardTeam(nmsTeam, 2);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			for (Player player : alivePlayers)
			{
				if (target != player)
				{
					boolean hideName = false;

					if (!checkedPlayers.containsKey(target) || !checkedPlayers.get(target).containsKey(player))
					{
						if (player.getLocation().distance(target.getLocation()) > (GetKit(target) instanceof KitAssassin ? 8 : 24))
						{
							hideName = true;
						}
						else if (!player.hasLineOfSight(target))
						{
							// no los
							hideName = true;
						}

						Player[] players = new Player[]
								{
								target, player
								};

						if (!(GetKit(player) instanceof KitAssassin || GetKit(target) instanceof KitAssassin))
						{
							for (int i = 0; i <= 1; i++)
							{
								Player p1 = players[i];
								Player p2 = players[1 - i];

								if (!checkedPlayers.containsKey(p1))
								{
									checkedPlayers.put(p1, new HashMap<Player, Boolean>());
								}

								checkedPlayers.get(p1).put(p2, hideName);
							}
						}
					}
					else
					{
						hideName = checkedPlayers.get(target).get(player);
					}

					// If hiddenNames conta
					if (hideName != _hiddenNames.get(player).contains(target.getName()))
					{
						if (!hideName)
						{
							_hiddenNames.get(player).remove(target.getName());
						}
						else
						{
							_hiddenNames.get(player).add(target.getName());
						}

						try
						{
							_nameTagVisibility.set(packet, hideName ? "never" : "always");
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}

						UtilPlayer.sendPacket(player, packet);
					}
				}
			}
		}
	}
	
	public int getSecondsSinceStart() 
	{
		return this._secondsSinceStart;
	}
	
	public int getChestRefillTime() 
	{
		return this._chestRefillTime;
	}
	
	public int getDeathMatchTime() 
	{
		return this._deathMatchTime;
	}
	
	public boolean isDeathMatchTeleported() 
	{
		return this._deathMatchTeleported;
	}
	
	public int getGameEndTime() 
	{
		return this._gameEndTime;
	}
	
}
