package ehnetwork.game.arcade.game.games.skywars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.explosion.ExplosionEvent;
import ehnetwork.core.loot.ChestLoot;
import ehnetwork.core.loot.RandomItem;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.skywars.data.TNTGenerator;
import ehnetwork.game.arcade.game.games.skywars.events.PlayerKillZombieEvent;
import ehnetwork.game.arcade.game.games.skywars.kits.KitChicken;
import ehnetwork.game.arcade.game.games.skywars.kits.KitDestructor;
import ehnetwork.game.arcade.game.games.skywars.kits.KitMadScientist;
import ehnetwork.game.arcade.game.games.skywars.kits.KitMiner;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.ore.OreHider;
import ehnetwork.game.arcade.stats.DeathBomberStatTracker;
import ehnetwork.game.arcade.stats.SkywarsKillZombieStatTracker;
import ehnetwork.game.arcade.stats.SkywarsTNTStatTracker;
import ehnetwork.game.arcade.stats.WinWithoutOpeningChestStatTracker;
import ehnetwork.game.arcade.stats.WinWithoutWearingArmorStatTracker;
import ehnetwork.minecraft.game.core.combat.CombatComponent;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

@SuppressWarnings("deprecation")
public abstract class Skywars extends Game
{
	//./parse 19 30 56
	
	private long _crumbleTime = 210000;

	private ArrayList<Block> _worldBlocks = new ArrayList<Block>();
	private HashSet<Location> _lootedBlocks = new HashSet<Location>();
	private TNTGenerator _tntGen;
	private boolean _alreadyAnnounced;
	
	private NautHashMap<Zombie, Location> _zombies = new NautHashMap<Zombie, Location>();

	private ArrayList<Block> _spawnChests = new ArrayList<Block>();
	private ArrayList<Block> _middleChests = new ArrayList<Block>();
	
	private HashMap<Entity, Player> _tntMap = new HashMap<Entity, Player>();
	private HashSet<Projectile> _pearls = new HashSet<Projectile>();
	
	private long _lastChicken = 0;

	private OreHider _oreHider;

	private ChestLoot _playerArmor = new ChestLoot();
	private ChestLoot _playerFood = new ChestLoot();
	private ChestLoot _playerTool = new ChestLoot();
	private ChestLoot _playerProjectile = new ChestLoot();
	private ChestLoot _playerBlock = new ChestLoot();

	private ChestLoot _middleArmor = new ChestLoot();
	private ChestLoot _middleFood = new ChestLoot();
	private ChestLoot _middleTool = new ChestLoot();
	private ChestLoot _middleProjectile = new ChestLoot();
	private ChestLoot _middleBlock = new ChestLoot();

	@SuppressWarnings("unchecked")
	public Skywars(ArcadeManager manager, GameType type, String[] description)
	{
		super(manager, type, new Kit[]
				{
				new KitChicken(manager),
				new KitMiner(manager),
				new KitMadScientist(manager),
				new KitDestructor(manager),
				
				}, description);

		PrepareFreeze = true;
		
		HideTeamSheep = true;

		CompassEnabled = true;

		StrictAntiHack = true;

		GameTimeout = 1500000L;

		DeathDropItems = true;
		
		QuitDropItems = true;

		WorldTimeSet = 0;	
		WorldBoundaryKill = false;

		DamageSelf = true;
		DamageTeamSelf = true;
		DamageEvP = true;
		Damage = true;

		DeathDropItems = true;

		ItemDrop = true;
		ItemPickup = true;

		BlockBreak = true;
		BlockPlace = true;

		InventoryClick = true;
		InventoryOpenBlock = true;
		InventoryOpenChest = true;

		PlaySoundGameStart = true;
		PrepareTime = 10000L;
		
		DontAllowOverfill = true;

		_oreHider = new OreHider();

		_help = new String[]
				{

				};

		setupPlayerLoot();
		setupMiddleLoot();

		setAlreadyAnnounced(false);

		registerStatTrackers(
				new SkywarsTNTStatTracker(this),
				new DeathBomberStatTracker(this, 3), //TNT Kills
				new SkywarsKillZombieStatTracker(this),
				new WinWithoutOpeningChestStatTracker(this),
				new WinWithoutWearingArmorStatTracker(this));

	}

	public void ParseData()
	{
		parseCreateZombieSpawns();
		parseCreateMiddleChests();
		parseCreatePlayerChests();
		parseCreatePlayerWebs();

		for (Location oreLoc : WorldData.GetCustomLocs("56"))
		{
			oreLoc.getBlock().setType(Material.STONE);
		}
		
		// Remove Sponge (Holds up Sand)
		for (Location loc : WorldData.GetCustomLocs("19"))
		{
			MapUtil.QuickChangeBlockAt(loc, Material.AIR);
		}

		// TNT
		for (Location loc : WorldData.GetDataLocs("LIME"))
		{
			_tntGen = new TNTGenerator(this, loc);
		}

		// Register Blocks
		for (int y = WorldData.MinY; y < WorldData.MaxY; y++)
		{
			for (int x = WorldData.MinX; x < WorldData.MaxX; x++)
			{
				for (int z = WorldData.MinZ; z < WorldData.MaxZ; z++)
				{
					Block block = WorldData.World.getBlockAt(x, y, z);
					if ((block.getType() != Material.AIR)
							&& (!block.isLiquid()))
					{
						_worldBlocks.add(block);
					}
				}
			}
		}
	}

	private void parseCreateZombieSpawns()
	{
		// Zombies
		for (Location loc : WorldData.GetDataLocs("Ropen ED"))
		{
			//Spawn
			CreatureAllowOverride = true;
			Zombie zombie = (Zombie) loc.getWorld().spawn(loc, Zombie.class);
			zombie.setRemoveWhenFarAway(false);
			zombie.setCustomName(C.cDRed + "Zombie Guardian");
			zombie.setCustomNameVisible(true);
			zombie.setMaxHealth(15);
			zombie.setHealth(15);
			CreatureAllowOverride = false;
			
			// Armor - Make sure the player can't get it!
			zombie.getEquipment().setHelmet(
					new ItemStack(Material.GOLD_HELMET));
			zombie.getEquipment().setHelmetDropChance(0F);
			zombie.getEquipment().setChestplate(
					new ItemStack(Material.GOLD_CHESTPLATE));
			zombie.getEquipment().setChestplateDropChance(0F);
			zombie.getEquipment().setLeggings(
					new ItemStack(Material.GOLD_LEGGINGS));
			zombie.getEquipment().setLeggingsDropChance(0F);
			zombie.getEquipment().setBoots(
					new ItemStack(Material.GOLD_BOOTS));
			zombie.getEquipment().setBootsDropChance(0F);


			_zombies.put(zombie, loc);
		}
	}
	
	private void parseCreateMiddleChests()
	{
		for (int i=0 ; i<4 && !WorldData.GetDataLocs("YELLOW").isEmpty() ; i++)
		{
			Location loc = UtilAlg.Random(WorldData.GetDataLocs("YELLOW"));
			
			WorldData.GetDataLocs("YELLOW").remove(loc);
			
			loc.getBlock().setTypeIdAndData(Material.CHEST.getId(), (byte) UtilMath.r(4), true);
			
			_middleChests.add(loc.getBlock());
			_worldBlocks.add(loc.getBlock());
		}
	}

	private void parseCreatePlayerChests()
	{
		NautHashMap<Location, ArrayList<Location>> islandChests = new NautHashMap<Location, ArrayList<Location>>();

		for (Location chestLoc : WorldData.GetDataLocs("BROWN"))
		{

			Location closestSpawn = UtilAlg.findClosest(chestLoc,
					GetTeamList().get(0).GetSpawns());

			if (UtilMath.offset2d(chestLoc, closestSpawn) > 8)
				continue;

			if (!islandChests.containsKey(closestSpawn))
				islandChests.put(closestSpawn, new ArrayList<Location>());

			islandChests.get(closestSpawn).add(chestLoc);
		}

		// Create 2 Chests In Random Locations
		for (ArrayList<Location> chests : islandChests.values())
		{
			for (int i = 0; i < 2; i++)
			{
				if (!chests.isEmpty())
				{
					Location chest = UtilAlg.Random(chests);
					chests.remove(chest);

					_spawnChests.add(chest.getBlock());
					chest.getBlock().setTypeIdAndData(Material.CHEST.getId(), (byte) UtilMath.r(4), true);
					_worldBlocks.add(chest.getBlock());
				}
			}
		}
	}

	private void parseCreatePlayerWebs()
	{
		// Store which chests are closest to which spawn
		NautHashMap<Location, ArrayList<Location>> islandWebs = new NautHashMap<Location, ArrayList<Location>>();

		// Allocate chests to their nearest spawn point
		for (Location webLoc : WorldData.GetCustomLocs("30"))
		{
			// Gets the spawn point closest to the current chest
			Location closestSpawn = UtilAlg.findClosest(webLoc,
					GetTeamList().get(0).GetSpawns());

			if (UtilMath.offset2d(webLoc, closestSpawn) > 8)
				continue;

			// Ensure the list exists
			if (!islandWebs.containsKey(closestSpawn))
				islandWebs.put(closestSpawn, new ArrayList<Location>());

			// Add this chest location to the spawn
			islandWebs.get(closestSpawn).add(webLoc);
		}

		// Create 2 Webs
		for (ArrayList<Location> webs : islandWebs.values())
		{
			for (int i = 0; i < 2; i++)
			{
				if (!webs.isEmpty())
				{
					webs.remove(UtilAlg.Random(webs));
				}
			}

			for (Location loc : webs)
			{
				loc.getBlock().setType(Material.AIR);
			}
		}
	}


	@EventHandler
	public void blockUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		if (!IsLive())
		{
			return;
		}

		if (_worldBlocks.isEmpty())
		{
			return;
		}

		if (!UtilTime.elapsed(GetStateTime(), _crumbleTime))
		{
			return;
		}

		if (!alreadyAnnounced())
		{
			Announce(C.cGreen + C.Bold
					+ "As time passes, the world begins to crumble...");
			Player[] arrayOfPlayer;
			int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
			for (int i = 0; i < j; i++)
			{
				Player player = arrayOfPlayer[i];

				player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL,
						3.0F, 1.0F);
			}
			setAlreadyAnnounced(true);
		}

		for (int i = 0; i < 4; i++)
		{
			Block bestBlock = null;
			double bestDist = 0.0D;
			for (Block block : _worldBlocks)
			{
				double dist = UtilMath.offset2d(GetSpectatorLocation(), block
						.getLocation().add(0.5D, 0.5D, 0.5D));
				if ((bestBlock == null) || (dist > bestDist))
				{
					bestBlock = block;
					bestDist = dist;
				}
			}
			while (bestBlock.getRelative(BlockFace.DOWN).getType() != Material.AIR)
			{
				bestBlock = bestBlock.getRelative(BlockFace.DOWN);
			}
			_worldBlocks.remove(bestBlock);
			if (bestBlock.getType() != Material.AIR)
			{
				if (Math.random() > 0.75D)
				{
					bestBlock.getWorld().spawnFallingBlock(
							bestBlock.getLocation().add(0.5D, 0.5D, 0.5D),
							bestBlock.getType(), bestBlock.getData());
				}
				MapUtil.QuickChangeBlockAt(bestBlock.getLocation(),
						Material.AIR);
			}
		}
	}

	@EventHandler
	public void createIslandChickens(UpdateEvent event)
	{
		if (!IsLive())
			return;
		
		if (event.getType() != UpdateType.SEC)
			return;
		
		if (!UtilTime.elapsed(_lastChicken, 45000))
			return;
		
		if (!UtilTime.elapsed(this.GetStateTime(), 5000))
			return;
		
		CreatureAllowOverride = true;
		for (Location loc : GetTeamList().get(0).GetSpawns())
		{
			Chicken chicken = loc.getWorld().spawn(loc.clone().add(0, 1, 0), Chicken.class);
			
			if (Math.random() > 0.75)
				chicken.setBaby();
			
			chicken.setMaxHealth(4);
			chicken.setHealth(4);
		}
		CreatureAllowOverride = false;
		
		_lastChicken = System.currentTimeMillis();
	}
	
	@EventHandler
	public void sandMapWarning(GameStateChangeEvent event)
	{
		if (event.GetState() != Game.GameState.Live)	
			return;
		
		if (WorldData.MapName.equals("Sahara"))
		{
			UtilTextMiddle.display(C.cRed + "Warning", "Red Sand is Unstable", 10, 60, 20);
		}
	}
	
	@EventHandler
	public void createIslandOres(GameStateChangeEvent event)
	{
		if (event.GetState() != Game.GameState.Prepare)
		{
			return;
		}

		// Store which chests are closest to which spawn
		NautHashMap<Location, ArrayList<Location>> islandOres = new NautHashMap<Location, ArrayList<Location>>();

		// Allocate chests to their nearest spawn point
		for (Location oreLoc : WorldData.GetCustomLocs("56"))
		{
			// Gets the spawn point closest to the current
			Location closestOre = UtilAlg.findClosest(oreLoc,
					GetTeamList().get(0).GetSpawns());

			if (UtilMath.offset2d(oreLoc, closestOre) > 8)
				continue;

			// Ensure the list exists
			if (!islandOres.containsKey(closestOre))
				islandOres.put(closestOre, new ArrayList<Location>());

			// Add this chest location to the spawn
			islandOres.get(closestOre).add(oreLoc);
		}

		// Vein Counts
		int diamondVeins = 2 + UtilMath.r(2);
		int ironVeins = 4 + UtilMath.r(4);
		int gravelVeins = 4 + UtilMath.r(3);

		// Create Ores
		for (ArrayList<Location> ores : islandOres.values())
		{
			for (int i = 0; i < diamondVeins; i++)
				createVein(ores, Material.DIAMOND_ORE, 2, true);

			for (int i = 0; i < ironVeins; i++)
				createVein(ores, Material.IRON_ORE, 3 + UtilMath.r(3), true);

			for (int i = 0; i < gravelVeins; i++)
				createVein(ores, Material.GRAVEL, 3 + UtilMath.r(3), false);
		}
	}

	private void createVein(ArrayList<Location> ores, Material type,
			int veinSize, boolean allowAboveAir)
	{
		if (ores.isEmpty())
			return;

		// First
		Location ore = UtilAlg.Random(ores);

		// Create Vein
		for (int i = 0; i < veinSize && !ores.isEmpty(); i++)
		{
			if (allowAboveAir || ore.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
				_oreHider.AddOre(ore, type);

			ores.remove(ore);

			ore = UtilAlg.findClosest(ore, ores);
		}
	}

	@EventHandler
	public void createRandomChests(GameStateChangeEvent event)
	{
		if (event.GetState() != Game.GameState.Live)
		{
			return;
		}
		HashSet<Material> ignore = new HashSet<Material>();

		ignore.add(Material.LEAVES);

		int xDiff = WorldData.MaxX - WorldData.MinX;
		int zDiff = WorldData.MaxZ - WorldData.MinZ;

		int done = 0;
		int attempts = 0;
		while (done < GetPlayers(true).size() && attempts <= 1000)
		{
			attempts++;

			Block block = UtilBlock.getHighest(WorldData.World, WorldData.MinX
					+ UtilMath.r(xDiff), WorldData.MinZ + UtilMath.r(zDiff),
					ignore);

			// Dont spawn near player islands
			boolean nearPlayer = false;

			for (Location loc : GetTeamList().get(0).GetSpawns())
			{
				if (UtilMath.offset2d(loc, block.getLocation()) < 12)
				{
					nearPlayer = true;
					break;
				}
			}
			if (nearPlayer)
			{
				continue;
			}

			//Find LOWEST valid point - prevents spawning on rooftops/etc
			Block validChest = null;
			while (block.getY() > WorldData.MinY)
			{
				// Valid Spot for Chest
				if ((UtilBlock.airFoliage(block))
						&& (UtilBlock.solid(block.getRelative(BlockFace.DOWN))))
				{
					validChest = block;
				}

				block = block.getRelative(BlockFace.DOWN);
			}

			if (validChest != null)
			{
				validChest.setTypeIdAndData(Material.CHEST.getId(),
						(byte) UtilMath.r(4), false);

				_worldBlocks.add(validChest);

				done++;
			}
		}

		if (attempts >= 1000)
		{
			System.out.println("Placed: " + done);
			System.out.println("ERROR PLACING RANDOM CHESTS");
			System.out.println("X Min:" + WorldData.MinX);
			System.out.println("X Diff:" + xDiff);
			System.out.println("Z Min:" + WorldData.MinZ);
			System.out.println("Z Diff:" + zDiff);
		}
	}

	@EventHandler
	public void openChest(PlayerInteractEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}

		Block block = event.getClickedBlock();
		if (block == null)
		{
			return;
		}

		if (!IsLive())
		{
			return;
		}

		if (_lootedBlocks.contains(block.getLocation()))
		{
			return;
		}

		BlockState state = block.getState();
		if ((state instanceof DoubleChest))
		{
			DoubleChest doubleChest = (DoubleChest) state;

			fillChest(event.getPlayer(), ((Chest) doubleChest.getLeftSide()).getBlock());
			fillChest(event.getPlayer(), ((Chest) doubleChest.getRightSide()).getBlock());
		}
		else if ((state instanceof Chest))
		{
			fillChest(event.getPlayer(), block);
		}
	}
	
	@EventHandler
	public void pickupTNT(PlayerPickupItemEvent e)
	{
		ItemStack is = e.getItem().getItemStack();
		Player player = e.getPlayer();
		if (is.getType() == Material.TNT)
		{
			e.setCancelled(true);
			_tntGen.pickup(player, e.getItem());
		}
	}

	@EventHandler
	public void onTNTThrow(PlayerInteractEvent e)
	{
		if (!IsLive())
		{
			return;
		}

		Player player = e.getPlayer();

		if (!IsAlive(player))
		{
			return;
		}

		if (!UtilInv.IsItem(player.getItemInHand(), Material.TNT, (byte) 0))
		{
			return;
		}
		e.setCancelled(true);

		UtilInv.remove(player, Material.TNT, (byte) 0, 1);
		UtilInv.Update(player);

		TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(
				player.getEyeLocation()
				.add(player.getLocation().getDirection()),
				TNTPrimed.class);

		tnt.setFuseTicks(60);
		if (UtilEvent.isAction(e, UtilEvent.ActionType.L))
		{
			UtilAction.velocity(tnt, player.getLocation().getDirection(),
					1.75D, false, 0.0D, 0.1D, 10.0D, false);
			player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 3.0F,
					1.0F);
		}
		
		_tntMap.put(tnt, player);
	}

	@EventHandler
	public void ItemDespawn(ItemDespawnEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void handleTNTCannon(UpdateEvent e)
	{
		if (!IsLive())
			return;

		if (e.getType() == UpdateType.FAST)
			_tntGen.update();
	}

	@EventHandler
	public void zombieUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Zombie> zombieIter = _zombies.keySet().iterator();
		
		while (zombieIter.hasNext())
		{
			Zombie zombie = zombieIter.next();
			
			if (!zombie.isValid())
			{
				zombieIter.remove();
				continue;
			}
			
			Location loc = _zombies.get(zombie);
			
			if (zombie.getTarget() == null || UtilMath.offset(zombie.getLocation(), loc) > 8)
			{
				zombie.setTarget(null);
				UtilEnt.CreatureMove(zombie, loc, 1f);
			}
		}
	}
	
	@EventHandler
	public void zombieTarget(EntityTargetLivingEntityEvent event)
	{
		if (event.getEntity() instanceof Zombie && _zombies.containsKey((Zombie)event.getEntity()))
		{
			Zombie zombie = (Zombie)event.getEntity();
			Location loc = _zombies.get(zombie);
			
			if (UtilMath.offset(event.getTarget().getLocation(), loc) > 8)
			{
				event.setCancelled(true);
				zombie.setTarget(null);
			}
		}
	}
	
	@EventHandler
	public void noZombieBurn(EntityCombustEvent e)
	{
		if ((e.getEntity() instanceof Zombie))
		{
			e.setDuration(0);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void blockBurn(BlockBurnEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void blockDecay(LeavesDecayEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void blockFade(BlockFadeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void blockSpread(BlockSpreadEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlaceAdd(BlockPlaceEvent e)
	{
		_worldBlocks.add(e.getBlock());
		
		if (e.getBlock().getType() == Material.CHEST ||
			e.getBlock().getType() == Material.PISTON_BASE ||
			e.getBlock().getType() == Material.PISTON_STICKY_BASE ||
			e.getBlock().getType() == Material.HOPPER)
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void blockBonusDrops(BlockBreakEvent e)
	{
		e.setExpToDrop(0);
		
		final Block block = e.getBlock();
		
		if (e.getBlock().getType() == Material.WEB)
		{
			Bukkit.getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
						block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), new ItemStack(Material.STRING));
					
				}}, 1);
			
		}

		if (e.getBlock().getType() == Material.GRAVEL)
		{
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			Bukkit.getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					for (int i=0 ; i<1 + UtilMath.r(3) ; i++)
						block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), new ItemStack(Material.FLINT));
					
				}}, 1);
		}

		if (e.getBlock().getType() == Material.IRON_ORE)
		{
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			Bukkit.getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), new ItemStack(Material.IRON_INGOT));
					
				}}, 1);
		}
	}

	@EventHandler
	public void onKillZombie(EntityDeathEvent e)
	{
		if (e.getEntity() instanceof Zombie)
		{
			Zombie ent = (Zombie) e.getEntity();

			if (_zombies.containsKey(ent))
			{
				if (ent.getKiller() instanceof Player)
				{
					Player p = ent.getKiller();

					Bukkit.getPluginManager().callEvent(
							new PlayerKillZombieEvent(p, ent));
				}
			}
		}
		else
		{
			return;
		}
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent e)
	{
		if (e.getBlock().getType() == Material.IRON_BLOCK)
		{
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.REDSTONE_BLOCK)
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void disableDamageToLevel(CustomDamageEvent event)
	{
		event.SetDamageToLevel(false);
	}
	
	@EventHandler
	public void mobLoot(EntityDeathEvent event)
	{
		//Zombie Loot
		if (event.getEntity() instanceof Zombie && _zombies.containsKey((Zombie)event.getEntity()))
		{
			event.getDrops().clear();
			
			double r = Math.random();
			
			if (r > 0.80)				event.getDrops().add(_middleArmor.getLoot());
			else if (r > 0.60)			event.getDrops().add(_middleTool.getLoot());
			else if (r > 0.40)			event.getDrops().add(_middleProjectile.getLoot());
			else 						event.getDrops().add(_middleFood.getLoot());
		}
		//Chicken Loot
		else if (event.getEntity() instanceof Chicken)
		{
			event.getDrops().clear();
			
			event.getDrops().add(new ItemStack(Material.FEATHER, 1 + UtilMath.r(4)));
		}
	}

	@EventHandler
	public void eggHit(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;

		if (event.GetDamage() >= 1)
			return;

		if(GetTeam(event.GetDamagerPlayer(true)) == GetTeam(event.GetDamageePlayer()))
			return;
		
		if (event.GetProjectile() instanceof Egg || event.GetProjectile() instanceof Snowball)
		{
			event.SetCancelled("Egg/Snowball");

			// Damage Event
			Manager.GetDamage().NewDamageEvent(event.GetDamageeEntity(),
					(LivingEntity) event.GetProjectile().getShooter(), event.GetProjectile(),
					DamageCause.PROJECTILE, 1, false, true, false,
					UtilEnt.getName((LivingEntity) event.GetProjectile().getShooter()),
					(event.GetProjectile() instanceof Egg ? "Egg" : "Snowball"));

			
			Vector vel = event.GetProjectile().getVelocity().multiply(0.2);
			
			if (vel.getY() < 0.1)
				vel.setY(0.1);
			
			event.GetDamageeEntity().setVelocity(vel);
		}
	}
	
	@EventHandler
	public void projectile(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Snowball || event.getDamager() instanceof Egg || event.getDamager() instanceof EnderPearl)
		{
			Projectile prj = (Projectile) event.getDamager();
			if(prj.getShooter() instanceof Player)
			{
				if(event.getEntity() instanceof Player)
				{
					if(GetTeam((Player) prj.getShooter()) == GetTeam((Player) event.getEntity()))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}

	private void fillChest(Player looter, Block block)
	{
		_lootedBlocks.add(block.getLocation());
		Chest chest = (Chest) block.getState();

		chest.getBlockInventory().clear();
		
		//Prevents same inventory spot being used twice
		HashSet<Integer> used = new HashSet<Integer>();
		
		//Player Island
		if (_spawnChests.contains(block))
		{
			//Armor
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _playerArmor.getLoot());
			
			//Food
			for (int i=0 ; i<1 + UtilMath.r(3) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _playerFood.getLoot());
			
			//Tool
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _playerTool.getLoot());
			
			//Projectile
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _playerProjectile.getLoot());
			
			//Block
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _playerBlock.getLoot());
		}
		//Other
		else if (_middleChests.contains(block))
		{
			//Armor
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleArmor.getLoot());
			
			//Food
			for (int i=0 ; i<1 + UtilMath.r(3) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleFood.getLoot());
			
			//Tool
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleTool.getLoot());
			
			//Projectile
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleProjectile.getLoot());
			
			//Block
			for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleBlock.getLoot());
		}
		else 
		{
			//Armor
			for (int i=0 ; i<UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleArmor.getLoot());
			
			//Food
			for (int i=0 ; i<UtilMath.r(3) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleFood.getLoot());
			
			//Tool
			for (int i=0 ; i<UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleTool.getLoot());
			
			//Projectile
			for (int i=0 ; i<UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleProjectile.getLoot());
			
			//Block
			for (int i=0 ; i<UtilMath.r(2) ; i++)
				chest.getBlockInventory().setItem(getIndex(used), _middleBlock.getLoot());
		}
	}

	private int getIndex(HashSet<Integer> used)
	{
		int i = UtilMath.r(27);

		while (used.contains(i))
		{
			i = UtilMath.r(27);
		}

		used.add(i);

		return i;
	}


	private void setupPlayerLoot()
	{
		//Armor
		_playerArmor.addLoot(new RandomItem(Material.LEATHER_HELMET, 20));
		_playerArmor.addLoot(new RandomItem(Material.LEATHER_CHESTPLATE, 32));
		_playerArmor.addLoot(new RandomItem(Material.LEATHER_LEGGINGS, 28));
		_playerArmor.addLoot(new RandomItem(Material.LEATHER_BOOTS, 16));
		
		_playerArmor.addLoot(new RandomItem(Material.GOLD_HELMET, 5));
		_playerArmor.addLoot(new RandomItem(Material.GOLD_CHESTPLATE, 8));
		_playerArmor.addLoot(new RandomItem(Material.GOLD_LEGGINGS, 7));
		_playerArmor.addLoot(new RandomItem(Material.GOLD_BOOTS, 4));
		
		//Food
		_playerFood.addLoot(new RandomItem(Material.BAKED_POTATO, 1, 1, 4));
		_playerFood.addLoot(new RandomItem(Material.COOKED_BEEF, 1, 1, 2));
		_playerFood.addLoot(new RandomItem(Material.COOKED_CHICKEN, 1, 1, 2));
		_playerFood.addLoot(new RandomItem(Material.CARROT_ITEM, 1, 1, 3));
		_playerFood.addLoot(new RandomItem(Material.BREAD, 1, 1, 3));
		_playerFood.addLoot(new RandomItem(Material.APPLE, 1, 1, 4));
		_playerFood.addLoot(new RandomItem(Material.PORK, 1, 1, 4));
		_playerFood.addLoot(new RandomItem(Material.ROTTEN_FLESH, 1, 1, 6));
		
		//Tools
		_playerTool.addLoot(new RandomItem(Material.WOOD_SWORD, 2));
		_playerTool.addLoot(new RandomItem(Material.STONE_SWORD, 1));
		_playerTool.addLoot(new RandomItem(Material.FISHING_ROD, 2));
		
		_playerTool.addLoot(new RandomItem(Material.STONE_AXE, 2));
		_playerTool.addLoot(new RandomItem(Material.STONE_PICKAXE, 3));
		
		_playerTool.addLoot(new RandomItem(Material.IRON_AXE, 1));
		_playerTool.addLoot(new RandomItem(Material.IRON_PICKAXE, 2));

		
		//Projectile
		_playerProjectile.addLoot(new RandomItem(Material.ARROW, 18, 2, 8));
		_playerProjectile.addLoot(new RandomItem(Material.SNOW_BALL, 60, 2, 5));
		_playerProjectile.addLoot(new RandomItem(Material.EGG, 60, 2, 5));
		
		//Block
		_playerBlock.addLoot(new RandomItem(Material.COBBLESTONE, 30, 8, 16));
		_playerBlock.addLoot(new RandomItem(Material.DIRT, 30, 8, 16));
		_playerBlock.addLoot(new RandomItem(Material.WOOD, 30, 8, 16));
	}
	
	private void setupMiddleLoot()
	{
		//Armor
		_middleArmor.addLoot(new RandomItem(Material.GOLD_HELMET, 20));
		_middleArmor.addLoot(new RandomItem(Material.GOLD_CHESTPLATE, 32));
		_middleArmor.addLoot(new RandomItem(Material.GOLD_LEGGINGS, 28));
		_middleArmor.addLoot(new RandomItem(Material.GOLD_BOOTS, 16));
		
		_middleArmor.addLoot(new RandomItem(Material.IRON_HELMET, 20));
		_middleArmor.addLoot(new RandomItem(Material.IRON_CHESTPLATE, 32));
		_middleArmor.addLoot(new RandomItem(Material.IRON_LEGGINGS, 28));
		_middleArmor.addLoot(new RandomItem(Material.IRON_BOOTS, 16));
		
		_middleArmor.addLoot(new RandomItem(Material.DIAMOND_HELMET, 5));
		_middleArmor.addLoot(new RandomItem(Material.DIAMOND_CHESTPLATE, 8));
		_middleArmor.addLoot(new RandomItem(Material.DIAMOND_LEGGINGS, 7));
		_middleArmor.addLoot(new RandomItem(Material.DIAMOND_BOOTS, 4));
		
		//Food
		_middleFood.addLoot(new RandomItem(Material.COOKED_BEEF, 1, 1, 3));
		_middleFood.addLoot(new RandomItem(Material.COOKED_CHICKEN, 1, 1, 3));
		_middleFood.addLoot(new RandomItem(Material.MUSHROOM_SOUP, 1));
		_middleFood.addLoot(new RandomItem(Material.GRILLED_PORK, 1, 1, 3));
		
		//Tools
		_middleTool.addLoot(new RandomItem(Material.IRON_SWORD, 1));
		_middleTool.addLoot(new RandomItem(Material.DIAMOND_SWORD, 1));
		_middleTool.addLoot(new RandomItem(Material.FISHING_ROD, 1));
		
		_middleTool.addLoot(new RandomItem(Material.DIAMOND_AXE, 1));
		_middleTool.addLoot(new RandomItem(Material.DIAMOND_PICKAXE, 1));

		//Projectile
		_middleTool.addLoot(new RandomItem(Material.BOW, 1));
		_middleProjectile.addLoot(new RandomItem(Material.ARROW, 2, 4, 12));
		_middleProjectile.addLoot(new RandomItem(Material.ENDER_PEARL, 1, 1, 2));
		
		//Block
		_middleBlock.addLoot(new RandomItem(Material.BRICK, 30, 12, 24));
		_middleBlock.addLoot(new RandomItem(Material.GLASS, 30, 12, 24));
		_middleBlock.addLoot(new RandomItem(Material.SOUL_SAND, 30, 12, 24));
	}

	public boolean alreadyAnnounced()
	{
		return _alreadyAnnounced;
	}

	public boolean setAlreadyAnnounced(boolean _already)
	{
		_alreadyAnnounced = _already;
		return _already;
	}
	
	@EventHandler
	public void handleExplosion(ExplosionEvent event)
	{
		_oreHider.Explosion(event);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OreReveal(BlockBreakEvent event) 
	{
		if (event.isCancelled())
			return;

		_oreHider.BlockBreak(event);
	}
	
	@EventHandler
	public void tntDamageAttribute(ExplosionPrimeEvent event)
	{
		Player player = _tntMap.get(event.getEntity());
		if (player != null)
		{
			for (Player other : UtilPlayer.getNearby(event.getEntity().getLocation(), 14))
			{
				Manager.GetCondition().Factory().Explosion("Throwing TNT", other, player, 50, 0.1, false, false);
			}
		}
	}
	
	@EventHandler
	public void pearlRide(ProjectileLaunchEvent event)
	{
		if (!IsLive())
			return;
		
		if (!(event.getEntity() instanceof EnderPearl))
			return;
		
		if (event.getEntity().getShooter() == null)
			return;
		
		if (!(event.getEntity().getShooter() instanceof Player))
			return;
		
		Player shooter = (Player)event.getEntity().getShooter();
		
		if (GetKit(shooter) instanceof KitDestructor)
			return;
		
		event.getEntity().setPassenger(shooter);
		
		_pearls.add(event.getEntity());
		
		((CraftPlayer)shooter).getHandle().spectating = true;
	}
	
	@EventHandler
	public void pearlUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		//Disable Spec
		for (Player player : GetPlayers(true))
			if (((CraftPlayer)player).getHandle().spectating && player.getVehicle() ==  null)
				((CraftPlayer)player).getHandle().spectating = false;
		
		Iterator<Projectile> pearlIter = _pearls.iterator();
		
		while (pearlIter.hasNext())
		{
			Projectile proj = pearlIter.next();
			
			if (!proj.isValid())
			{
				proj.remove();
				pearlIter.remove();
				continue;
			}
			
			UtilParticle.PlayParticle(ParticleType.WITCH_MAGIC, proj.getLocation(), 0f, 0f, 0f, 0f, 1, ViewDist.MAX, UtilServer.getPlayers());
		}
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
	
	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		if (assist)
			return 3;
		else
			return 12;
	}
	
	public TNTGenerator getTnTGen() 
	{
		return this._tntGen;
	}
	
	public long getCrumbleTime()
	{
		return this._crumbleTime;
	}
	
}
