package ehnetwork.game.arcade.game.games.bridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.explosion.ExplosionEvent;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.events.PlayerDeathOutEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.bridge.kits.KitApple;
import ehnetwork.game.arcade.game.games.bridge.kits.KitArcher;
import ehnetwork.game.arcade.game.games.bridge.kits.KitBeserker;
import ehnetwork.game.arcade.game.games.bridge.kits.KitBomber;
import ehnetwork.game.arcade.game.games.bridge.kits.KitDestructor;
import ehnetwork.game.arcade.game.games.bridge.kits.KitMammoth;
import ehnetwork.game.arcade.game.games.bridge.kits.KitMiner;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.ore.OreHider;
import ehnetwork.game.arcade.ore.OreObsfucation;
import ehnetwork.game.arcade.stats.BridgesSniperStatTracker;
import ehnetwork.game.arcade.stats.DeathBomberStatTracker;
import ehnetwork.game.arcade.stats.FoodForTheMassesStatTracker;
import ehnetwork.game.arcade.stats.KillFastStatTracker;
import ehnetwork.game.arcade.stats.TntMinerStatTracker;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Bridge extends TeamGame implements OreObsfucation
{
	/**
	 * When a block is broken of one of these materials, the item drop will be locked to the player that broke the block for 8 seconds. After that, anyone can pick up the item.
	 */
	private static final Material[] PLAYER_DROP_DELAY_MATERIALS = new Material[] { Material.LOG, Material.LOG_2, Material.IRON_ORE, Material.DIAMOND_ORE, Material.COAL_ORE, Material.GOLD_ORE, Material.WORKBENCH, Material.FURNACE };

	//Bridge Timer
	private int _bridgeTime = 600000;
	private boolean _bridgesDown = false;

	//Wood Bridge
	private ArrayList<Location> _woodBridge = new ArrayList<Location>();
	private HashMap<Location, Integer> _woodBridgeBlocks = null; 

	//Lava Bridge
	private ArrayList<Location> _lavaBridge = new ArrayList<Location>();
	private ArrayList<Location> _lavaSource = new ArrayList<Location>();

	//Lilly Pad Bridge
	private NautHashMap<Location, Long> _lillyPads = new NautHashMap<Location, Long>();
	
	//Mushrooms
	private NautHashMap<Location, Long> _mushroomStem = new NautHashMap<Location, Long>();
	private NautHashMap<Location, Long> _mushroomTop = new NautHashMap<Location, Long>();
	private boolean _stemsGrown = false;
	
	//Ice
	private ArrayList<Location> _iceBridge = new ArrayList<Location>();

	private HashSet<BridgePart> _bridgeParts = new HashSet<BridgePart>();

	//Animals
	private long _lastAnimal = System.currentTimeMillis();
	private HashMap<GameTeam, HashSet<Entity>> _animalSet = new HashMap<GameTeam, HashSet<Entity>>();

	//Mushroom
	private long _lastMushroom = System.currentTimeMillis();

	//Chest Loot
	private ArrayList<ItemStack> _chestLoot = new ArrayList<ItemStack>();

	//Ore
	private OreHider _ore;
	private double _oreDensity = 2.2;

	//Map Flags
	private int _buildHeight = -1;

	//Player Respawn
	private HashSet<String> _usedLife = new HashSet<String>();

	//Tourney Mode
	private boolean _tournament;
	private HashMap<GameTeam, Integer> _tournamentKills = new HashMap<GameTeam, Integer>();
	private long _tournamentKillMessageTimer = 0;

	//Item pickup delay for players that don't break the block
	private HashMap<Block, UUID> _blockToUUIDMap = new HashMap<Block, UUID>();


	public Bridge(ArcadeManager manager)
	{
		super(manager, GameType.Bridge,

				new Kit[] 
						{ 
				new KitApple(manager),
				new KitBeserker(manager),
				new KitMammoth(manager),
				new KitArcher(manager),
				new KitMiner(manager),
				new KitBomber(manager),
				new KitDestructor(manager),
						},

						new String[] { 
				"Gather resources and prepare for combat.",
				"After 10 minutes, The Bridges will emerge.",
				"Special loot is located in the center.",
				"The last team alive wins!" 
		});

		_ore = new OreHider();

		// Flags
		GameTimeout = Manager.IsTournamentServer() ? 5400000 : 3600000;

		Manager.GetExplosion().SetLiquidDamage(false);

		this.StrictAntiHack = true;

		DamageSelf = true;

		ItemDrop = true;
		ItemPickup = true;

		InventoryClick = true;

		PrivateBlocks = true;
		BlockBreak = true;
		BlockPlace = true;

		InventoryOpenBlock = true;
		InventoryOpenChest = true;

		WorldTimeSet = 2000;

		WorldWaterDamage = 0;
		WorldBoundaryKill = false; 

		CompassEnabled = true;

		DeathDropItems = true;

		GemMultiplier = 2.5;  

		PrepareFreeze = false;

		//Tournament
		if (Manager.IsTournamentServer())
		{
			QuitOut = false;

			_gameDesc = new String[] 
					{ 
					"Gather resources and prepare for combat.",
					"After 10 minutes, The Bridges will emerge.",
					"Special loot is located in the center.",
					"Killing yourself counts as -1 team kill.",
					"Team with the most kills wins!"
					};
		}


		_tournament = Manager.IsTournamentServer();

		registerStatTrackers(
				new FoodForTheMassesStatTracker(this),
				new BridgesSniperStatTracker(this),
				new TntMinerStatTracker(this),
				new KillFastStatTracker(this, 4, 10, "Rampage"),
				new DeathBomberStatTracker(this, 5)
				);
	}

	@EventHandler
	public void PlayerOut(final PlayerDeathOutEvent event)
	{
		if (_bridgesDown)
			return;

		Player player = event.GetPlayer();

		if (!_usedLife.contains(player.getName()))
		{
			_usedLife.add(player.getName());

			UtilPlayer.message(player, F.main("Game", "You used your " + F.elem(C.cAqua + "Early Game Revive") + "."));

			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;
		
		if (!WorldData.GetCustomLocs("WATER_DAMAGE").isEmpty())
		{
			WorldWaterDamage = 4;
		}

		if (WorldWaterDamage > 0)
		{
			if (WorldData.MapName.equals("Volcanic Islands"))
				UtilTextMiddle.display(C.cRed + "Warning", "Water is Boiling Hot", 10, 60, 20);
			else if (WorldData.MapName.equals("Icelands"))
				UtilTextMiddle.display(C.cRed + "Warning", "Water is Freezing Cold", 10, 60, 20);
			else
				UtilTextMiddle.display(C.cRed + "Warning", "Water is Deadly", 10, 60, 20);
		}
	}

	//parse
	@Override
	public void ParseData() 
	{
		ParseLavaBridge();
		ParseWoodBridge();
		ParseIceBridge();
		ParseLillyPad();
		ParseMushrooms();
		
		ParseChests();

		ParseOre(WorldData.GetCustomLocs("73")); // Red
		ParseOre(WorldData.GetCustomLocs("14")); // Yellow
		ParseOre(WorldData.GetCustomLocs("129")); // Green
		ParseOre(WorldData.GetCustomLocs("56")); // Blue

		//Mass Teams
		if (!WorldData.GetCustomLocs("152").isEmpty())		ParseOre(WorldData.GetCustomLocs("152"));
		if (!WorldData.GetCustomLocs("41").isEmpty())		ParseOre(WorldData.GetCustomLocs("41"));
		if (!WorldData.GetCustomLocs("133").isEmpty())		ParseOre(WorldData.GetCustomLocs("133"));
		if (!WorldData.GetCustomLocs("57").isEmpty())		ParseOre(WorldData.GetCustomLocs("57"));

		if (!WorldData.GetCustomLocs("100").isEmpty())		ParseOre(WorldData.GetCustomLocs("100"));
		if (!WorldData.GetCustomLocs("86").isEmpty())		ParseOre(WorldData.GetCustomLocs("86"));
		if (!WorldData.GetCustomLocs("103").isEmpty())		ParseOre(WorldData.GetCustomLocs("103"));
		if (!WorldData.GetCustomLocs("22").isEmpty())		ParseOre(WorldData.GetCustomLocs("22"));
	}

	private void ParseChests()
	{
		for (Location loc : WorldData.GetCustomLocs("54")) 
		{
			if (loc.getBlock().getType() != Material.CHEST)
				loc.getBlock().setType(Material.CHEST);

			Chest chest = (Chest) loc.getBlock().getState();

			chest.getBlockInventory().clear();

			int count = 2 + UtilMath.r(5);
			for (int i = 0; i < count; i++) 
			{
				chest.getBlockInventory().addItem(GetChestItem());
			}
		}
	}

	private ItemStack GetChestItem()
	{
		if (_chestLoot.isEmpty()) 
		{
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_HELMET));
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_LEGGINGS));
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_BOOTS));
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_SWORD));
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_AXE));
			for (int i = 0; i < 1; i++)
				_chestLoot.add(new ItemStack(Material.DIAMOND_PICKAXE));

			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_HELMET));
			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_CHESTPLATE));
			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_LEGGINGS));
			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_BOOTS));
			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_SWORD));
			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_AXE));
			for (int i = 0; i < 6; i++)
				_chestLoot.add(new ItemStack(Material.IRON_PICKAXE));

			for (int i = 0; i < 18; i++)
				_chestLoot.add(new ItemStack(Material.BOW));
			for (int i = 0; i < 24; i++)
				_chestLoot.add(new ItemStack(Material.ARROW, 8));

			for (int i = 0; i < 48; i++)
				_chestLoot.add(new ItemStack(Material.MUSHROOM_SOUP));
			for (int i = 0; i < 24; i++)
				_chestLoot.add(new ItemStack(Material.COOKED_CHICKEN, 2));
		}

		ItemStack stack = _chestLoot.get(UtilMath.r(_chestLoot.size()));

		int amount = 1;

		if (stack.getType().getMaxStackSize() > 1)
			amount = stack.getAmount() + UtilMath.r(stack.getAmount());

		return ItemStackFactory.Instance.CreateStack(stack.getTypeId(), amount);
	}

	@EventHandler
	public void ChestDeny(PlayerInteractEvent event)
	{
		if (_bridgesDown)
			return;

		if (event.getClickedBlock() == null)
			return;

		if (event.getClickedBlock().getType() != Material.CHEST)
			return;

		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;

		for (Location loc : WorldData.GetCustomLocs("54")) 
		{
			if (loc.getBlock().equals(event.getClickedBlock()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}



	private void ParseOre(ArrayList<Location> teamOre)
	{
		int coal = (int) ((teamOre.size() / 32d) * _oreDensity);
		int iron = (int) ((teamOre.size() / 24d) * _oreDensity);
		int gold = (int) ((teamOre.size() / 64d) * _oreDensity);
		int diamond = 1 + (int) ((teamOre.size() / 128d) * _oreDensity);

		int gravel = (int) ((teamOre.size() / 64d) * _oreDensity);

		int lowY = 256;
		int highY = 0;

		for (Location loc : teamOre)
		{
			if (loc.getBlockY() < lowY)
				lowY = loc.getBlockY();

			if (loc.getBlockY() > highY)
				highY = loc.getBlockY();

			loc.getBlock().setTypeId(1);
		}

		int varY = highY - lowY;

		//Gravel
		for (int i = 0; i < gravel && !teamOre.isEmpty(); i++) 
		{
			int attempts = 20;
			int id = 0;

			while (attempts > 0) 
			{
				id = UtilMath.r(teamOre.size());

				double height = (double) (teamOre.get(id).getBlockY() - lowY) / (double) varY;

				if (height > 0.8)
					break;

				else if (height > 0.6 && Math.random() > 0.4)
					break;

				else if (height > 0.4 && Math.random() > 0.6)
					break;

				else if (height > 0.2 && Math.random() > 0.8)
					break;
			}

			CreateOre(teamOre.remove(id), Material.GRAVEL, 6);
		}

		//Coal
		for (int i = 0; i < coal && !teamOre.isEmpty(); i++) 
		{
			int attempts = 20;
			int id = 0;

			while (attempts > 0) 
			{
				id = UtilMath.r(teamOre.size());

				double height = (double) (teamOre.get(id).getBlockY() - lowY) / (double) varY;

				if (height > 0.8)
					break;

				else if (height > 0.6 && Math.random() > 0.4)
					break;

				else if (height > 0.4 && Math.random() > 0.6)
					break;

				else if (height > 0.2 && Math.random() > 0.8)
					break;
			}

			CreateOre(teamOre.remove(id), Material.COAL_ORE, 6);
		}

		//Iron
		for (int i = 0; i < iron && !teamOre.isEmpty(); i++) 
		{
			int id = UtilMath.r(teamOre.size());

			CreateOre(teamOre.remove(id), Material.IRON_ORE, 3);
		}

		//Gold
		for (int i = 0; i < gold && !teamOre.isEmpty(); i++) 
		{
			int attempts = 20;
			int id = 0;

			while (attempts > 0) 
			{
				id = UtilMath.r(teamOre.size());

				double height = (double) (teamOre.get(id).getBlockY() - lowY)
						/ (double) varY;

				if (height > 0.8 && Math.random() > 0.8)
					break;

				else if (height > 0.6 && Math.random() > 0.7)
					break;

				else if (height > 0.4 && Math.random() > 0.6)
					break;

				else if (height > 0.2 && Math.random() > 0.4)
					break;

				else if (Math.random() > 0.2)
					break;
			}

			CreateOre(teamOre.remove(id), Material.GOLD_ORE, 3);
		}

		//Diamond
		for (int i = 0; i < diamond && !teamOre.isEmpty(); i++)
		{
			int attempts = 20;
			int id = 0;

			while (attempts > 0)
			{
				id = UtilMath.r(teamOre.size());

				double height = (double) (teamOre.get(id).getBlockY() - lowY)
						/ (double) varY;

				if (height > 0.8)
					continue;

				else if (height > 0.6 && Math.random() > 0.9)
					break;

				else if (height > 0.4 && Math.random() > 0.7)
					break;

				else if (height > 0.2 && Math.random() > 0.5)
					break;

				else
					break;
			}

			CreateOre(teamOre.remove(id), Material.DIAMOND_ORE, 2);
		}
	}

	private void CreateOre(Location loc, Material type, int amount) 
	{
		double bonus = Math.random() + 1;

		amount = (int) (amount * bonus);

		int attempts = 100;
		while (amount > 0 && attempts > 0) 
		{
			attempts--;

			BlockFace faceXZ = BlockFace.SELF;
			BlockFace faceY = BlockFace.SELF;

			if (Math.random() > 0.20)
			{
				int rFace = UtilMath.r(6);

				if (rFace == 0)				faceY = BlockFace.UP;
				else if (rFace == 1)		faceY = BlockFace.DOWN;
				else if (rFace == 2)		faceXZ = BlockFace.NORTH;
				else if (rFace == 3)		faceXZ = BlockFace.SOUTH;
				else if (rFace == 4)		faceXZ = BlockFace.EAST;
				else						faceXZ = BlockFace.WEST;
			}
			else
			{
				//Height
				int rFace = UtilMath.r(3);

				if (rFace == 0)				faceY = BlockFace.SELF;
				else if (rFace == 1)		faceY = BlockFace.UP;
				else						faceY = BlockFace.DOWN;

				//Flat
				if (faceY == BlockFace.SELF)
				{
					rFace = UtilMath.r(4);

					if (rFace == 0)				faceXZ = BlockFace.NORTH_EAST;
					else if (rFace == 1)		faceXZ = BlockFace.NORTH_WEST;
					else if (rFace == 2)		faceXZ = BlockFace.SOUTH_EAST;
					else 						faceXZ = BlockFace.SOUTH_WEST;
				}
				else
				{
					rFace = UtilMath.r(4);

					if (rFace == 0)				faceXZ = BlockFace.NORTH;
					else if (rFace == 1)		faceXZ = BlockFace.SOUTH;
					else if (rFace == 2)		faceXZ = BlockFace.EAST;
					else						faceXZ = BlockFace.WEST;
				}
			}

			if (loc.getBlock().getRelative(faceY).getRelative(faceXZ).getType() != Material.STONE)
				continue;

			loc = loc.getBlock().getRelative(faceY).getRelative(faceXZ).getLocation();

			_ore.AddOre(loc, type);

			amount--;
		}
	}

	private void ParseWoodBridge() {
		_woodBridge = new ArrayList<Location>();

		// Load Wood In
		for (Location loc : WorldData.GetDataLocs("BROWN")) {
			_woodBridge.add(loc.getBlock().getLocation());
		}

		for (Location loc : WorldData.GetDataLocs("GRAY")) {
			_woodBridge.add(loc.getBlock().getLocation());
			_woodBridge.add(loc.getBlock().getRelative(BlockFace.UP)
					.getLocation());
		}

		// Determine Wood Block
		_woodBridgeBlocks = new HashMap<Location, Integer>();

		for (Location loc : _woodBridge) {
			if (_woodBridge.contains(loc.getBlock().getRelative(BlockFace.DOWN)
					.getLocation())) {
				_woodBridgeBlocks.put(loc, 85);
			}

			if (_woodBridge.contains(loc.getBlock().getRelative(BlockFace.UP)
					.getLocation())) {
				_woodBridgeBlocks.put(loc, 17);
			}

			if (!_woodBridgeBlocks.containsKey(loc)) {
				_woodBridgeBlocks.put(loc, 126);
			}
		}
	}

	private void ParseLavaBridge() {
		for (Location loc : WorldData.GetDataLocs("RED")) {
			_lavaBridge.add(loc.getBlock().getLocation());
		}

		for (Location loc : WorldData.GetDataLocs("ORANGE")) {
			_lavaBridge.add(loc.getBlock().getLocation());
			_lavaBridge.add(loc.getBlock().getRelative(BlockFace.UP)
					.getLocation());
		}

		_lavaSource = WorldData.GetDataLocs("BLACK");
	}

	private void ParseIceBridge() 
	{
		_iceBridge = WorldData.GetDataLocs("LIGHT_BLUE");
	}

	private void ParseMushrooms() 
	{
		for (Location loc : WorldData.GetCustomLocs("21"))
		{
			_mushroomStem.put(loc, 0L);
			loc.getBlock().setType(Material.AIR);
		}
		
		for (Location loc : WorldData.GetDataLocs("PURPLE"))
		{
			_mushroomTop.put(loc, 0L);
		}
	}
	
	private void ParseLillyPad() 
	{
		for (Location loc : WorldData.GetDataLocs("LIME"))
		{
			_lillyPads.put(loc, 0L);
		}
	}

	@EventHandler
	public void BridgeBuild(UpdateEvent event) 
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FASTEST)
			return;

		if (!UtilTime.elapsed(this.GetStateTime(), _bridgeTime))
			return;

		if (!_bridgesDown)
		{
			Manager.GetExplosion().SetLiquidDamage(true);
			this.Announce(C.cRed + C.Bold + "ALERT: " + ChatColor.RESET + C.Bold + "THE BRIDGES ARE SPAWNING!");
		}

		_bridgesDown = true;

		for (Kit kit : this.GetKits())
		{
			if (kit instanceof KitDestructor)
			{
				((KitDestructor)kit).SetEnabled(true);
			}
		}

		BuildWood();
		BuildLava();
		BuildIce();
		BuildLillyPad();
		buildMushroom();
	}

	private void BuildLava() 
	{
		for (int i = 0; i < 3; i++)
			if (_lavaBridge != null && _lavaSource != null
			&& !_lavaBridge.isEmpty() && !_lavaSource.isEmpty()) {
				// Random Block
				Location bestLoc = _lavaBridge.get(UtilMath.r(_lavaBridge
						.size()));

				if (bestLoc.getBlock().getRelative(BlockFace.DOWN)
						.isLiquid())
					continue;

				_lavaBridge.remove(bestLoc);

				Location source = _lavaSource.get(UtilMath.r(_lavaSource
						.size()));

				// Create Part
				FallingBlock block = bestLoc.getWorld().spawnFallingBlock(
						source, 87, (byte) 0);
				BridgePart part = new BridgePart(block, bestLoc, true);
				_bridgeParts.add(part);

				// Sound
				source.getWorld().playSound(source, Sound.EXPLODE,
						5f * (float) Math.random(),
						0.5f + (float) Math.random());
			}
	}

	private void BuildLillyPad() 
	{
		for (int i = 0; i < 3; i++)
			if (_lillyPads != null && !_lillyPads.isEmpty()) 
			{
				// Random Block
				Location loc = UtilAlg.Random(_lillyPads.keySet());
				
				if (!UtilTime.elapsed(_lillyPads.get(loc), 8000))
					continue;

				if (!loc.getBlock().getRelative(BlockFace.DOWN).isLiquid())
					continue;

				_lillyPads.remove(loc);

				MapUtil.QuickChangeBlockAt(loc, Material.WATER_LILY);

				// Sound
				loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 111);
			}
	}

	@EventHandler
	public void breakLillyPad(BlockBreakEvent event)
	{
		if (event.getBlock().getType() != Material.WATER_LILY)
			return;
		
		_lillyPads.put(event.getBlock().getLocation(), System.currentTimeMillis() + (long)(Math.random() * 12000));
	}
	
	private void buildMushroom()
	{
		if (_mushroomStem != null && !_mushroomStem.isEmpty()) 
		{
			for (int i=0 ; i<4 && !_mushroomStem.isEmpty() ; i++)
			{
				double lowestY = 0;
				Location lowestLoc = null;
				
				for (Location loc : _mushroomStem.keySet())
				{
					if (!UtilTime.elapsed(_mushroomStem.get(loc), 6000))
						continue;
					
					if (lowestLoc == null || loc.getY() < lowestY)
					{
						lowestY = loc.getY();
						lowestLoc = loc;
					}
				}
				
				if (lowestLoc == null)
					continue;
				
				_mushroomStem.remove(lowestLoc);
				
				MapUtil.QuickChangeBlockAt(lowestLoc, 100, (byte)15);
			}
		}
		else
		{
			_stemsGrown = true;
		}
		
		if (_stemsGrown && _mushroomTop != null && !_mushroomTop.isEmpty()) 
		{
			int attempts = 0;
			int done = 0;
			while (done < 6 && attempts < 400)	
			{
				attempts++;

				// Random Block
				Location loc = UtilAlg.Random(_mushroomTop.keySet());

				if (!UtilTime.elapsed(_mushroomTop.get(loc), 6000))
					continue;
				
				Block block = loc.getBlock();

				if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR &&
					block.getRelative(BlockFace.NORTH).getType() == Material.AIR &&
					block.getRelative(BlockFace.EAST).getType() == Material.AIR &&
					block.getRelative(BlockFace.SOUTH).getType() == Material.AIR &&
					block.getRelative(BlockFace.WEST).getType() == Material.AIR)			
					continue;

				_mushroomTop.remove(loc);

				MapUtil.QuickChangeBlockAt(block.getLocation(), 99, (byte)14);
				
				done++;
			}
		}
	}
	
	@EventHandler
	public void breakMushroom(BlockBreakEvent event)
	{
		if (event.isCancelled())
			return;
		
		
		if (event.getBlock().getTypeId() == 100 && 
				WorldData.GetCustomLocs("21").contains(event.getBlock().getLocation().add(0.5, 0, 0.5)))
		{
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			
			_mushroomStem.put(event.getBlock().getLocation(), System.currentTimeMillis() + (long)(Math.random() * 12000));
		}
		
		if (event.getBlock().getTypeId() == 99 && 
				WorldData.GetDataLocs("PURPLE").contains(event.getBlock().getLocation().add(0.5, 0, 0.5)))
		{
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			
			_mushroomTop.put(event.getBlock().getLocation(), System.currentTimeMillis() + (long)(Math.random() * 12000));
		}
	}
	
	private void BuildIce() 
	{
		if (_iceBridge == null || _iceBridge.isEmpty() || UtilTime.elapsed(this.GetStateTime(), _bridgeTime + 120000))
		{
			WorldData.World.setStorm(false);
			return;
		}

		WorldData.World.setStorm(true);
		
		int attempts = 0;
		int done = 0;
		while (done < 5 && attempts < 400)	
		{
			attempts++;

			// Random Block
			Location loc = _iceBridge.get(UtilMath.r(_iceBridge.size()));

			Block block = loc.getBlock().getRelative(BlockFace.DOWN);

			if (!block.isLiquid())
				continue;

			if (block.getRelative(BlockFace.NORTH).isLiquid() &&
					block.getRelative(BlockFace.EAST).isLiquid() &&
					block.getRelative(BlockFace.SOUTH).isLiquid() &&
					block.getRelative(BlockFace.WEST).isLiquid())			
				continue;

			_iceBridge.remove(loc);

			if (Math.random() > 0.25)
				MapUtil.QuickChangeBlockAt(block.getLocation(), Material.PACKED_ICE);
			else
				MapUtil.QuickChangeBlockAt(block.getLocation(), Material.ICE);

			done++;
		}
	}

	private void BuildWood() 
	{
		if (_woodBridgeBlocks != null && !_woodBridgeBlocks.isEmpty()) 
		{
			ArrayList<Location> toDo = new ArrayList<Location>();

			BlockFace[] faces = new BlockFace[] { BlockFace.NORTH,
					BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

			for (Location loc : _woodBridgeBlocks.keySet()) 
			{
				if (_woodBridgeBlocks.get(loc) == 17) 
				{
					int adjacent = 0;

					for (BlockFace face : faces)
						if (loc.getBlock().getRelative(face).getTypeId() != 0)
							adjacent++;

					if (adjacent > 0)
						toDo.add(loc);

				} else if (_woodBridgeBlocks.get(loc) == 85) 
				{
					if (loc.getBlock().getRelative(BlockFace.DOWN).getTypeId() == 0)
						continue;

					toDo.add(loc);
				} else if (_woodBridgeBlocks.get(loc) == 126)
				{
					int adjacent = 0;

					for (BlockFace face : faces)
						if (loc.getBlock().getRelative(face).getTypeId() != 0)
							adjacent++;

					if (adjacent > 0)
						toDo.add(loc);
				}
			}

			if (toDo.size() == 0)
				return;

			for (Location loc : toDo)
			{
				int id = _woodBridgeBlocks.remove(loc);

				Location source = loc.clone().add(0, 30, 0);

				// Create Part
				FallingBlock block = loc.getWorld().spawnFallingBlock(source,
						id, (byte) 0);
				block.setVelocity(new Vector(0, -1, 0));
				BridgePart part = new BridgePart(block, loc, false);
				_bridgeParts.add(part);
			}
		}
	}

	@EventHandler
	public void BridgeUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<BridgePart> partIterator = _bridgeParts.iterator();

		while (partIterator.hasNext()) 
		{
			BridgePart part = partIterator.next();

			if (part.Update())
				partIterator.remove();
		}
	}

	@EventHandler
	public void BridgeForm(EntityChangeBlockEvent event)
	{
		for (BridgePart part : _bridgeParts)
			if (part.Entity.equals(event.getEntity())) 
				event.setCancelled(true);
	}

	@EventHandler
	public void BridgeItem(ItemSpawnEvent event) 
	{
		for (BridgePart part : _bridgeParts)
			if (part.ItemSpawn(event.getEntity()))
				event.setCancelled(true);
	}

	@EventHandler
	public void IceForm(BlockFormEvent event) 
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void AnimalSpawn(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		if (!UtilTime.elapsed(_lastAnimal, 30000))
			return;

		_lastAnimal = System.currentTimeMillis();

		for (GameTeam team : GetTeamList()) 
		{
			if (_animalSet.get(team) == null)
				_animalSet.put(team, new HashSet<Entity>());

			// Clean
			Iterator<Entity> entIterator = _animalSet.get(team).iterator();

			while (entIterator.hasNext())
			{
				Entity ent = entIterator.next();

				if (ent.isDead() || !ent.isValid())
					entIterator.remove();
			}

			// Too Many
			if (_animalSet.get(team).size() > 4)
				continue;

			// Spawn
			double rand = Math.random();

			Entity ent;

			CreatureAllowOverride = true;
			if (rand > 0.66)
				ent = team.GetSpawn().getWorld().spawn(team.GetSpawn(), Cow.class);
			else if (rand > 0.33)
				ent = team.GetSpawn().getWorld().spawn(team.GetSpawn(), Pig.class);
			else
				ent = team.GetSpawn().getWorld().spawn(team.GetSpawn(), Chicken.class);
			CreatureAllowOverride = false;

			_animalSet.get(team).add(ent);
		}
	}

	@EventHandler
	public void MushroomSpawn(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		if (!UtilTime.elapsed(_lastMushroom, 20000))
			return;

		_lastMushroom = System.currentTimeMillis();

		for (GameTeam team : GetTeamList()) 
		{
			Block block = team.GetSpawn().getBlock();

			while (!UtilBlock.airFoliage(block))
			{
				block = block.getRelative(BlockFace.UP);
			}

			while (UtilBlock.airFoliage(block))
			{
				block = block.getRelative(BlockFace.DOWN);
			}

			block = block.getRelative(BlockFace.UP);

			if (block.getTypeId() == 0)
			{
				if (Math.random() > 0.5)
					block.setTypeId(39);
				else
					block.setTypeId(40);
			}
		}
	}

	@EventHandler
	public void handleExplosion(ExplosionEvent event)
	{
		// Reveal ore that are inside the explosion
		_ore.Explosion(event);

		// Handle block ownership for explosion
		if (event.getOwner() != null)
		{
			for (Block cur : event.GetBlocks())
			{
				// These are the only blocks that will drop from the explosion so they are the only ones
				// we need to worry about for keeping owner data of
				if (cur.getType() == Material.IRON_ORE ||
						cur.getType() == Material.COAL_ORE ||
						cur.getType() == Material.GOLD_ORE ||
						cur.getType() == Material.DIAMOND_ORE)
				{
					_blockToUUIDMap.put(cur, event.getOwner().getUniqueId());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OreReveal(BlockBreakEvent event) 
	{
		if (event.isCancelled())
			return;

		_ore.BlockBreak(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void BlockPlace(BlockPlaceEvent event) 
	{
		if (event.isCancelled())
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		//Too High
		if (event.getBlock().getLocation().getBlockY() > GetHeightLimit())
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game",
					"Cannot place blocks this high up."));
			event.setCancelled(true);
			return;
		}	

		if (_bridgesDown)
			return;

		//In Liquid
		if (event.getBlock().getRelative(BlockFace.UP).isLiquid() ||
				event.getBlockReplacedState().getTypeId() == 8 ||
				event.getBlockReplacedState().getTypeId() == 9 ||
				event.getBlockReplacedState().getTypeId() == 10 ||
				event.getBlockReplacedState().getTypeId() == 11)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game",
					"Cannot place blocks in liquids until Bridge is down."));

			event.getPlayer().setVelocity(new Vector(0,-0.5,0));

			event.setCancelled(true);
			return;
		}

		//Above Water/Void
		for (int i = 1; i <= event.getBlock().getLocation().getY(); i++) 
		{
			Block below = event.getBlock().getRelative(BlockFace.DOWN, i);

			if (below.isLiquid())
			{
				UtilPlayer
				.message(
						event.getPlayer(),
						F.main("Game",
								"Cannot place blocks above water until Bridge is down."));
				event.setCancelled(true);
				return;
			}

			if (event.getBlock().getLocation().getY() - i <= 0) 
			{
				UtilPlayer
				.message(
						event.getPlayer(),
						F.main("Game",
								"Cannot place blocks above void until Bridge is down."));
				event.setCancelled(true);
				return;
			}

			if (below.getTypeId() != 0)
				break;
		}
	}

	private void addBlockPickupDelay(Player owner, Block block)
	{
		Material blockMaterial = block.getType();
		boolean shouldAddToMap = false;

		for (int i = 0; i < PLAYER_DROP_DELAY_MATERIALS.length && !shouldAddToMap; i++)
		{
			if (blockMaterial.equals(PLAYER_DROP_DELAY_MATERIALS[i]))
			{
				shouldAddToMap = true;
			}
		}

		if (shouldAddToMap)
			_blockToUUIDMap.put(block, owner.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void dropItem(BlockBreakEvent event)
	{
		addBlockPickupDelay(event.getPlayer(), event.getBlock());
	}

	@EventHandler
	public void itemSpawn(ItemSpawnEvent event)
	{
		Item item = event.getEntity();
		Block block = event.getLocation().getBlock();

		UUID uuid = _blockToUUIDMap.remove(block);

		if (uuid != null)
		{
			item.setMetadata("owner", new FixedMetadataValue(Manager.getPlugin(), uuid));
		}
	}

	@EventHandler
	public void itemPickup(PlayerPickupItemEvent event)
	{
		Item item = event.getItem();

		if (item.hasMetadata("owner"))
		{
			FixedMetadataValue ownerData = (FixedMetadataValue) item.getMetadata("owner").get(0);
			UUID ownerUUID = (UUID) ownerData.value();

			// 8 Seconds
			if (item.getTicksLived() <= 160 && !event.getPlayer().getUniqueId().equals(ownerUUID))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void ChestProtect(EntityExplodeEvent event) 
	{
		Iterator<Block> blockIterator = event.blockList().iterator();

		while (blockIterator.hasNext()) 
		{
			Block block = blockIterator.next();

			if (block.getType() == Material.CHEST
					|| block.getType() == Material.FURNACE
					|| block.getType() == Material.BURNING_FURNACE
					|| block.getType() == Material.WORKBENCH)
				blockIterator.remove();
		}
	}


	@EventHandler(priority = EventPriority.LOW)
	public void BucketEmpty(PlayerBucketEmptyEvent event) 
	{
		if (event.getBucket() != Material.WATER_BUCKET)
			return;

		if (WorldWaterDamage > 0)
		{
			UtilPlayer.message(
					event.getPlayer(),
					F.main("Game", "Cannot use " + F.elem("Water Bucket") + " on this map."));
			event.setCancelled(true);
		}

		else if (!_bridgesDown)
		{
			UtilPlayer.message(
					event.getPlayer(),
					F.main("Game", "Cannot use " + F.elem("Water Bucket") + " befores Bridges drop."));
			event.setCancelled(true);
		}
	}

	public int GetHeightLimit()
	{
		if (_buildHeight == -1)
		{
			_buildHeight = 0;
			int amount = 0;

			for (GameTeam team : GetTeamList())
				for (Location loc : team.GetSpawns())
				{
					_buildHeight += loc.getBlockY();
					amount++;
				}


			_buildHeight = _buildHeight / amount;
		}

		return _buildHeight + 24;
	}

	@Override
	public OreHider GetOreHider() 
	{
		return _ore;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void CraftingDeny(PrepareItemCraftEvent event)
	{
		if (event.getRecipe().getResult() == null)
			return;

		Material type = event.getRecipe().getResult().getType();

		if (type != Material.GOLDEN_APPLE &&
				type != Material.GOLDEN_CARROT && 
				type != Material.FLINT_AND_STEEL && type != Material.HOPPER)
			return;

		if (!(event.getInventory() instanceof CraftingInventory))
			return;

		CraftingInventory inv = (CraftingInventory)event.getInventory();
		inv.setResult(null);
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		//Wipe Last
		Scoreboard.Reset();

		//Display Players Alive
		if (!_tournament)
		{
			for (GameTeam team : this.GetTeamList())
			{		
				//Display Individual Players
				if (this.GetPlayers(true).size() < 8)
				{
					if (!team.IsTeamAlive())
						continue;

					Scoreboard.WriteBlank();

					for (Player player : team.GetPlayers(true))
					{
						Scoreboard.Write(team.GetColor() + player.getName());
					}
				}

				//Display Players Alive
				else 
				{
					Scoreboard.WriteBlank();
					Scoreboard.Write(team.GetColor() + team.GetName() + " Team");
					Scoreboard.Write(team.GetColor() + "" + team.GetPlayers(true).size() + " Alive");
				}
			}
		}
		//Display Kills + Players
		else
		{
			for (GameTeam team : this.GetTeamList())
			{			
				int kills = 0;
				if (_tournamentKills.containsKey(team))
					kills = _tournamentKills.get(team);

				Scoreboard.WriteBlank();

				Scoreboard.Write(team.GetColor() + " " + team.GetPlayers(true).size() + " Players");
				Scoreboard.Write(team.GetColor() + " " + kills + " Kills");
			}
		}

		Scoreboard.WriteBlank();


		long time = _bridgeTime
				- (System.currentTimeMillis() - this.GetStateTime());

		if (time > 0)
		{
			Scoreboard.Write(C.cYellow + C.Bold + "Bridges In");
			Scoreboard.Write(UtilTime.MakeStr(time, 0));
		}
		else
		{
			Scoreboard.Write(C.cYellow + C.Bold + "Time Left");
			Scoreboard.Write(UtilTime.MakeStr(5400000 - (System.currentTimeMillis() - this.GetStateTime()), 0));
		}	

		Scoreboard.Draw();
	}

	@EventHandler
	public void RecordKill(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player)event.GetEvent().getEntity();

		GameTeam killedTeam = GetTeam(killed);
		if (killedTeam == null)		
			return;

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer != null && !killer.equals(killed))
			{
				GameTeam killerTeam = GetTeam(killer);
				if (killerTeam == null)		
					return;

				if (killerTeam.equals(killedTeam))
					return;

				if (!_tournamentKills.containsKey(killerTeam))
					_tournamentKills.put(killerTeam, 1);
				else
					_tournamentKills.put(killerTeam, _tournamentKills.get(killerTeam) + 1);
			}
			//self kill
			else if (_bridgesDown)
			{
				if (!_tournamentKills.containsKey(killedTeam))
					_tournamentKills.put(killedTeam, -1);
				else
					_tournamentKills.put(killedTeam, _tournamentKills.get(killedTeam) - 1);
			}
		}
		//self kill
		else if (_bridgesDown)
		{
			if (!_tournamentKills.containsKey(killedTeam))
				_tournamentKills.put(killedTeam, -1);
			else
				_tournamentKills.put(killedTeam, _tournamentKills.get(killedTeam) - 1);
		}
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(true).size() > 0)
				teamsAlive.add(team);

		if (!QuitOut)
		{
			//Offline Player Team
			for (GameTeam team : RejoinTeam.values())
				teamsAlive.add(team);
		}

		if (teamsAlive.size() <= 1)
		{
			//Announce Winner
			if (!_tournament)
			{
				if (teamsAlive.size() > 0)
					AnnounceEnd(teamsAlive.get(0));
			}
			else
			{
				GameTeam bestTeam = null;
				int bestKills = 0;

				for (GameTeam team : GetTeamList())
				{
					if (_tournamentKills.containsKey(team))
					{
						int kills = _tournamentKills.get(team);

						if (bestTeam == null || bestKills < kills)
						{
							bestTeam = team;
							bestKills = kills;
						}
					}
				}

				if (bestTeam != null)
					AnnounceEnd(bestTeam);
			}


			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
						AddGems(player, 10, "Winning Team", false, false);
				}

				for (Player player : team.GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
			}

			//End
			SetState(GameState.End);	
		}
	}

	@Override
	public void HandleTimeout()
	{
		if (!_tournament)
		{
			SetState(GameState.End);
			return;
		}

		ArrayList<GameTeam> bestTeams = new ArrayList<GameTeam>();
		int bestKills = 0;

		for (GameTeam team : GetTeamList())
		{
			if (_tournamentKills.containsKey(team))
			{
				int kills = _tournamentKills.get(team);

				if (bestTeams == null || kills > bestKills)
				{
					bestTeams.clear();
					bestTeams.add(team);
					bestKills = kills;
				}

				else if (kills == bestKills)
				{
					bestTeams.add(team);
				}
			}
		}

		//Many Teams Alive
		if (bestTeams.size() != 1)
		{
			if (UtilTime.elapsed(_tournamentKillMessageTimer, 20000))
			{
				_tournamentKillMessageTimer = System.currentTimeMillis();

				this.Announce(C.cRed + C.Bold + "ALERT: " + ChatColor.RESET + C.Bold + "FIRST TEAM TO HAVE MOST KILLS WINS!");
			}
		}

		//Team Won
		else
		{	
			AnnounceEnd(bestTeams.get(0));

			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
						AddGems(player, 10, "Winning Team", false, false);
				}

				for (Player player : team.GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
			}

			//End
			SetState(GameState.End);	
		}	
	}

	public boolean isBridgesDown()
	{
		return _bridgesDown;
	}

	@EventHandler
	public void CheatChestBreak(BlockBreakEvent event)
	{
		if (_bridgesDown)
			return;

		if (event.getBlock().getType() != Material.CHEST)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		for (Location loc : WorldData.GetCustomLocs("54")) 
		{
			if (loc.getBlock().equals(event.getBlock()))
			{
				cheaterKill(event.getPlayer());
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void CheatChestBreak(BlockPlaceEvent event)
	{
		if (_bridgesDown)
			return;

		if (event.getBlock().getType() != Material.CHEST)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		for (Location loc : WorldData.GetCustomLocs("54")) 
		{
			if (UtilMath.offset(loc, event.getBlock().getLocation()) < 2)
			{
				cheaterKill(event.getPlayer());
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void CheatChestInteract(PlayerInteractEvent event)
	{
		if (_bridgesDown)
			return;

		if (event.getClickedBlock() == null)
			return;		

		if (event.getClickedBlock().getType() != Material.CHEST)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		for (Location loc : WorldData.GetCustomLocs("54")) 
		{
			if (loc.getBlock().equals(event.getClickedBlock()))
			{
				cheaterKill(event.getPlayer());
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PreBridgeDamage(CustomDamageEvent event) 
	{
		if (event.IsCancelled())
			return;

		if (_bridgesDown || event.GetProjectile() != null)
			return;

		GameTeam damageeTeam = GetTeam(event.GetDamageePlayer());
		GameTeam damagerTeam = GetTeam(event.GetDamagerPlayer(false));

		if (damageeTeam == null || damagerTeam == null)
			return;

		if (damageeTeam.equals(damagerTeam))
			return;

		Player damagee = event.GetDamageePlayer();
		Player damager = event.GetDamagerPlayer(false);

		//Damagee is closer to Damagers Island
		if (UtilMath.offset(damagee.getLocation(), UtilWorld.averageLocation(damageeTeam.GetSpawns())) > 
		UtilMath.offset(damagee.getLocation(), UtilWorld.averageLocation(damagerTeam.GetSpawns())))
		{
			cheaterKill(damagee);
		}

		//Damagee is closer to Damagees Island
		if (UtilMath.offset(damager.getLocation(), UtilWorld.averageLocation(damagerTeam.GetSpawns())) > 
		UtilMath.offset(damager.getLocation(), UtilWorld.averageLocation(damageeTeam.GetSpawns())))
		{
			cheaterKill(damager);
		}
	}

	public void cheaterKill(Player player)
	{
		Announce(C.Bold + player.getName() + " was killed for cheating!");
		_usedLife.add(player.getName());
		player.damage(9999);
	}

	@EventHandler
	public void liquidFlow(BlockFromToEvent event)
	{ 
		if (!_bridgesDown)
		{
			if (!event.getToBlock().getRelative(BlockFace.UP).equals(event.getBlock()))
			{
				event.setCancelled(true);
			}
		}
	}

	//	@EventHandler
	//	public void liquidBlockDeny(BlockBreakEvent event)
	//	{
	//		if (_bridgesDown)
	//			return;
	//
	//		if (!IsAlive(event.getPlayer()))
	//			return;
	//
	//		if (event.getBlock().getRelative(BlockFace.UP).isLiquid() || event.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).isLiquid())
	//		{
	//			UtilPlayer.message(event.getPlayer(), F.main("Game",
	//					"Cannot tunnel under liquids."));
	//			
	//			event.setCancelled(true);
	//		}
	//	}

	@EventHandler
	public void vehicleDeny(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R))
			return;

		if (UtilGear.isMat(event.getPlayer().getItemInHand(), Material.BOAT))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game",
					"You cannot place boats."));

			event.setCancelled(true);
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

	@EventHandler
	public void debug(PlayerCommandPreprocessEvent event)
	{
		if (Manager.GetClients().hasRank(event.getPlayer(), Rank.ADMIN) && event.getMessage().contains("/oretoggle"))
			_ore.ToggleVisibility();
		
		if (Manager.GetClients().hasRank(event.getPlayer(), Rank.ADMIN) && event.getMessage().contains("/bridge"))
			_bridgeTime = 30000;
	}
	
	@EventHandler
	public void disableIceForm(BlockFormEvent event)
	{
		event.setCancelled(true);
	}
}
