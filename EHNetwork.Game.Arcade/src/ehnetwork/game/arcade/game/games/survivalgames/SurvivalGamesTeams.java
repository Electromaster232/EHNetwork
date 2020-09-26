package ehnetwork.game.arcade.game.games.survivalgames;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

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
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
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
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilTime.TimeUnit;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitArcher;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitAssassin;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitAxeman;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBeastmaster;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBomber;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitBrawler;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitHorseman;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitKnight;
import ehnetwork.game.arcade.game.games.survivalgames.kit.KitLooter;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class SurvivalGamesTeams extends TeamGame
{
	private HashSet<Location> _openedChest = new HashSet<Location>();
	private ArrayList<ItemStack> _baseChestLoot = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> _superChestLoot = new ArrayList<ItemStack>();

	//Misc
	private HashMap<Entity, Player> _tntMap = new HashMap<Entity, Player>();
	private HashSet<Location> _placedBlocks = new HashSet<Location>();
	private Location _spawn;

	//Creep
	private int _maxSpreadRate = 120;
	private ArrayList<Location> _redLocations = new ArrayList<Location>();
	private int _spreadType = 0;
	private String _spreadName = "";
	private boolean _ignoreLiquids = true;
	private ArrayList<Entry<Integer, Integer>> _spreadTypeBlocks;
	private HashMap<Player, Long> _redOutTime = new HashMap<Player, Long>();

	private HashMap<Integer, HashMap<Integer, HashSet<Integer>>> _redMap = new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();
	private HashMap<Player, ArrayList<ChunkChange>> _redChunks = new HashMap<Player, ArrayList<ChunkChange>>();

	//Supply Drop
	private ArrayList<Location> _supplyLocations = new ArrayList<Location>();
	private Location _supplyCurrent = null;
	private Location _supplyEffect = null;
	private ArrayList<Block> _supplyChests = new ArrayList<Block>();

	//Deathmatch
	private boolean _deathmatchCountdown = false;
	private boolean _deathmatchLive = false;
	private long _deathmatchTime = 0;

	//Debug
	private long totalTime = 0;

	public SurvivalGamesTeams(ArcadeManager manager) 
	{
		super(manager, GameType.SurvivalGames,

				new Kit[]
						{
				new KitAxeman(manager),
				//new KitLooter(manager),
				new KitKnight(manager),

				new KitArcher(manager),
				new KitBrawler(manager),

				new KitAssassin(manager),
				new KitBeastmaster(manager),
				new KitBomber(manager),
				//new KitNecromancer(manager),
				
				new KitHorseman(manager)
						},

						new String[]
								{
				"Search for chests to find loot",
				"Slaughter your opponents",
				"Stay away from the Deep Freeze!",
				"Last team alive wins!"
								});
		
		_help = new String[] 
				{
				C.cGreen + "Use a Compass to find and kill enemies!",
				C.cAqua + "Crouch to become invisible to Compass tracking!",
				C.cGreen + "Avoid the Deep Freeze at all costs!",
				C.cAqua + "Use TNT & Tripwires to make traps!",
				C.cGreen + "You lose Speed 2 at start of game if you attack.",
				C.cAqua + "Avoid enemies who have better gear than you!",
				C.cGreen + "Compass finds Supply Drops during night time.",
				C.cAqua + "Compass finds Players during day time.",
				};

		setItemMerge(true);
//		Manager.GetAntiStack().SetEnabled(false);

		this.GameTimeout = 9600000;
		
		this.WorldTimeSet = 0;
		this.WorldBoundaryKill = false; 
		
		SpawnNearAllies = true;
		SpawnNearEnemies = true; //This is to ensure theres no 'gaps' of 1 between teams, hence forcing a team to get split.
		
		this.DamageSelf = true;
		this.DamageTeamSelf = false;

		this.DeathDropItems = true;

		this.ItemDrop = true;
		this.ItemPickup = true;

		this.CompassEnabled = false; //XXX
		
		this.InventoryClick = true;
		this.InventoryOpenBlock = true;
		this.InventoryOpenChest = true;

		//Blocks
		this.BlockBreakAllow.add(46);	//TNT
		this.BlockPlaceAllow.add(46);

		this.BlockBreakAllow.add(30);	//Web
		this.BlockPlaceAllow.add(30);

		this.BlockBreakAllow.add(132); 	//Tripwire
		this.BlockPlaceAllow.add(132);

		this.BlockBreakAllow.add(131);	//Wire Hook
		this.BlockPlaceAllow.add(131);

		this.BlockBreakAllow.add(55);	//Redstone Dust
		this.BlockPlaceAllow.add(55);

		this.BlockBreakAllow.add(72);	//Wood Pressure Plate
		this.BlockPlaceAllow.add(72);

		this.BlockBreakAllow.add(69);	//Lever
		this.BlockPlaceAllow.add(69);

		this.BlockBreakAllow.add(18);	//Leaves

		//SPREAD
		_spreadType = 1;//UtilMath.r(3);

		_spreadTypeBlocks = new ArrayList<Entry<Integer, Integer>>();

		if (_spreadType == 0)
		{
			_spreadName = "Red";
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(159, 14));
		}
		else if (_spreadType == 1)
		{
			_spreadName = "Deep Freeze";
			_ignoreLiquids = false;
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(78, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(79, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(80, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(49, 0));
		}
		else if (_spreadType == 2)
		{
			_spreadName = "Nether Corruption";
			_ignoreLiquids = false;
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(49, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(87, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(88, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(89, 0));
			_spreadTypeBlocks.add(new AbstractMap.SimpleEntry<Integer, Integer>(153, 0));
		}

		System.out.println("===================");
		System.out.println("CREEP TYPE: " + _spreadName);
		System.out.println("===================");
		//Manager.GetStatsManager().addTable(GetName(), "kills", "deaths", "chestsOpened");
		
		//Tournament
		if (Manager.IsTournamentServer())
			QuitOut = false;
	}

	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		return 4;
	}

	@Override
	public void ParseData() 
	{
		ArrayList<Location> allSpawns = new ArrayList<Location>();
		for (GameTeam team : GetTeamList())
			allSpawns.addAll(team.GetSpawns());
		
		_spawn = UtilWorld.averageLocation(allSpawns);

		for (Location loc : allSpawns)
			loc.setYaw(UtilAlg.GetYaw(UtilAlg.getTrajectory(loc, _spawn)));

		CreateChestCraftEnchant();

		_supplyLocations = WorldData.GetDataLocs("WHITE");
		for (Location loc : _supplyLocations)
			loc.getBlock().setType(Material.GLASS);

		if (!WorldData.GetCustomLocs("VARS").isEmpty())
		{
			_maxSpreadRate = WorldData.GetCustomLocs("VARS").get(0).getBlockX();
			System.out.println("Spread Rate: " + _maxSpreadRate);
		}
	}

	private void CreateChestCraftEnchant() 
	{
		ArrayList<Location> chests = WorldData.GetCustomLocs("54");

		System.out.println("Map Chest Locations: " + chests.size());

		//Enchants
		System.out.println("Enchanting Tables: " + Math.min(5, chests.size()));
		for (int i=0 ; i<5 && !chests.isEmpty() ; i++)
		{
			Location loc = chests.remove(UtilMath.r(chests.size()));		
			loc.getBlock().setType(Material.ENCHANTMENT_TABLE);
		}

		//Crafting
		System.out.println("Crafting Benches: " + Math.min(10, chests.size()));
		for (int i=0 ; i<10 && !chests.isEmpty() ; i++)
		{
			Location loc = chests.remove(UtilMath.r(chests.size()));		
			loc.getBlock().setType(Material.WORKBENCH);
		}

		int spawn = 0;

		//Chests
		System.out.println("Chests: " + Math.min(250, chests.size()));
		for (int i=0 ; i<250 && !chests.isEmpty() ; i++)
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

			Block block = UtilBlock.getHighest(WorldData.World, WorldData.MinX + UtilMath.r(xDiff), WorldData.MinZ + UtilMath.r(zDiff), ignore);

			if (!UtilBlock.airFoliage(block) || !UtilBlock.solid(block.getRelative(BlockFace.DOWN)))
				continue;

			block.setTypeIdAndData(54, (byte)UtilMath.r(4), true);
			done++;
		}
	}

	@EventHandler
	private void OpenChest(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getClickedBlock() == null)
			return;

		if (!(event.getClickedBlock().getState() instanceof Chest))
			return;

		if (GetState() != GameState.Live)
			return;

		if (_openedChest.contains(event.getClickedBlock().getLocation()))
			return;

		_openedChest.add(event.getClickedBlock().getLocation());

		Chest chest = (Chest)event.getClickedBlock().getState();

		chest.getBlockInventory().clear();

		int count = 2;
		if (Math.random() > 0.50)			count++;	
		if (Math.random() > 0.65)			count++;	
		if (Math.random() > 0.80)			count++;	
		if (Math.random() > 0.95)			count++;

		if (UtilMath.offset(chest.getLocation(), _spawn) < 8)
			count += 3;

		if (GetKit(event.getPlayer()) instanceof KitLooter)
		{
			count += UtilMath.r(3);
		}

		if (_supplyChests.contains(event.getClickedBlock()))
		{
			count = 2;	
			if (Math.random() > 0.75)			count++;
			if (Math.random() > 0.95)			count++;
		}

		for (int i = 0; i < count; i++) 
			chest.getBlockInventory().setItem(UtilMath.r(27), GetChestItem(_supplyChests.contains(event.getClickedBlock())));

		_supplyChests.remove(event.getClickedBlock());

		Bukkit.getPluginManager().callEvent(new SupplyChestOpenEvent(event.getPlayer(), event.getClickedBlock()));
	}

	private ItemStack GetChestItem(boolean superChest)
	{
		if (_baseChestLoot.isEmpty()) 
		{
			//Armor
			for (int i = 0; i < 10; i++)			_baseChestLoot.add(new ItemStack(Material.IRON_HELMET));
			for (int i = 0; i < 3; i++)				_baseChestLoot.add(new ItemStack(Material.IRON_CHESTPLATE));
			for (int i = 0; i < 5; i++)				_baseChestLoot.add(new ItemStack(Material.IRON_LEGGINGS));
			for (int i = 0; i < 10; i++)			_baseChestLoot.add(new ItemStack(Material.IRON_BOOTS));

			for (int i = 0; i < 30; i++)			_baseChestLoot.add(new ItemStack(Material.CHAINMAIL_HELMET));
			for (int i = 0; i < 20; i++)			_baseChestLoot.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			for (int i = 0; i < 25; i++)			_baseChestLoot.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
			for (int i = 0; i < 30; i++)			_baseChestLoot.add(new ItemStack(Material.CHAINMAIL_BOOTS));

			for (int i = 0; i < 40; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_HELMET));
			for (int i = 0; i < 30; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_CHESTPLATE));
			for (int i = 0; i < 35; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_LEGGINGS));
			for (int i = 0; i < 40; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_BOOTS));

			for (int i = 0; i < 100; i++)			_baseChestLoot.add(new ItemStack(Material.LEATHER_HELMET));
			for (int i = 0; i < 90; i++)			_baseChestLoot.add(new ItemStack(Material.LEATHER_CHESTPLATE));
			for (int i = 0; i < 85; i++)			_baseChestLoot.add(new ItemStack(Material.LEATHER_LEGGINGS));
			for (int i = 0; i < 100; i++)			_baseChestLoot.add(new ItemStack(Material.LEATHER_BOOTS));

			//Weapons
			for (int i = 0; i < 70; i++)			_baseChestLoot.add(new ItemStack(Material.WOOD_AXE));
			for (int i = 0; i < 45; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_AXE));
			for (int i = 0; i < 35; i++)			_baseChestLoot.add(new ItemStack(Material.STONE_AXE));
			for (int i = 0; i < 15; i++)			_baseChestLoot.add(new ItemStack(Material.IRON_AXE));

			for (int i = 0; i < 60; i++)			_baseChestLoot.add(new ItemStack(Material.WOOD_SWORD));
			for (int i = 0; i < 35; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_SWORD));			
			for (int i = 0; i < 25; i++)			_baseChestLoot.add(new ItemStack(Material.STONE_SWORD));
			for (int i = 0; i < 8; i++)			_baseChestLoot.add(new ItemStack(Material.IRON_SWORD));

			for (int i = 0; i < 45; i++)			_baseChestLoot.add(new ItemStack(Material.BOW));
			for (int i = 0; i < 55; i++)			_baseChestLoot.add(new ItemStack(Material.ARROW, 4));

			for (int i = 0; i < 15; i++)			_baseChestLoot.add(new ItemStack(Material.TNT, 1));
			for (int i = 0; i < 30; i++)			_baseChestLoot.add(new ItemStack(Material.WEB, 2));

			//Food
			for (int i = 0; i < 40; i++)			_baseChestLoot.add(new ItemStack(Material.MUSHROOM_SOUP));
			for (int i = 0; i < 50; i++)			_baseChestLoot.add(new ItemStack(Material.COOKED_CHICKEN, 5));
			for (int i = 0; i < 80; i++)			_baseChestLoot.add(new ItemStack(Material.RAW_BEEF, 6));
			for (int i = 0; i < 50; i++)			_baseChestLoot.add(new ItemStack(Material.GRILLED_PORK, 5));
			for (int i = 0; i < 160; i++)			_baseChestLoot.add(new ItemStack(Material.BREAD, 5));
			for (int i = 0; i < 40; i++)			_baseChestLoot.add(new ItemStack(Material.PUMPKIN_PIE, 4));
			for (int i = 0; i < 80; i++)			_baseChestLoot.add(new ItemStack(Material.COOKIE, 7));
			for (int i = 0; i < 90; i++)			_baseChestLoot.add(new ItemStack(Material.ROTTEN_FLESH, 4));

			for (int i = 0; i < 80; i++)			_baseChestLoot.add(new ItemStack(Material.WHEAT, 6));

			//Misc
			for (int i = 0; i < 50; i++)			_baseChestLoot.add(new ItemStack(Material.COMPASS, 1));
			for (int i = 0; i < 25; i++)			_baseChestLoot.add(new ItemStack(Material.EXP_BOTTLE, 1));
			for (int i = 0; i < 50; i++)			_baseChestLoot.add(new ItemStack(Material.GOLD_INGOT, 2));
			for (int i = 0; i < 30; i++)			_baseChestLoot.add(new ItemStack(Material.IRON_INGOT));
			for (int i = 0; i < 5; i++)				_baseChestLoot.add(new ItemStack(Material.DIAMOND));
			for (int i = 0; i < 60; i++)			_baseChestLoot.add(new ItemStack(Material.STICK, 4));
			for (int i = 0; i < 80; i++)			_baseChestLoot.add(new ItemStack(Material.FLINT, 6));
			for (int i = 0; i < 80; i++)			_baseChestLoot.add(new ItemStack(Material.FEATHER, 6));
			for (int i = 0; i < 40; i++)			_baseChestLoot.add(new ItemStack(Material.BOAT));
			for (int i = 0; i < 70; i++)			_baseChestLoot.add(new ItemStack(Material.FISHING_ROD));

			//Building Supplies
			for (int i = 0; i < 45; i++)			_baseChestLoot.add(new ItemStack(Material.PISTON_BASE, 4));

			/*
			String material = _baseChestLoot.get(0).getType().toString();
			double count = 0;

			for (int i = 0; i < _baseChestLoot.size(); i++)
			{
				if (!_baseChestLoot.get(i).getType().toString().equalsIgnoreCase(material))
				{
					System.out.println(material + " - " + count + " - " + (count / _baseChestLoot.size() * 100) + "%");
					material = _baseChestLoot.get(i).getType().toString();
					count = 1;
				}
				else
				{
					count++;
				}
			}

			System.out.println(material + " " + (count / _baseChestLoot.size() * 100) + "%");
			 */
		}

		if (_superChestLoot.isEmpty()) 
		{
			for (int i = 0; i < 3; i++)				_superChestLoot.add(new ItemStack(Material.DIAMOND_HELMET));
			for (int i = 0; i < 1; i++)				_superChestLoot.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
			for (int i = 0; i < 2; i++)				_superChestLoot.add(new ItemStack(Material.DIAMOND_LEGGINGS));
			for (int i = 0; i < 3; i++)				_superChestLoot.add(new ItemStack(Material.DIAMOND_BOOTS));

			for (int i = 0; i < 30; i++)			_superChestLoot.add(new ItemStack(Material.IRON_HELMET));
			for (int i = 0; i < 24; i++)			_superChestLoot.add(new ItemStack(Material.IRON_CHESTPLATE));
			for (int i = 0; i < 27; i++)			_superChestLoot.add(new ItemStack(Material.IRON_LEGGINGS));
			for (int i = 0; i < 30; i++)			_superChestLoot.add(new ItemStack(Material.IRON_BOOTS));

			for (int i = 0; i < 24; i++)			_superChestLoot.add(new ItemStack(Material.IRON_SWORD));
			for (int i = 0; i < 8; i++)			_superChestLoot.add(new ItemStack(Material.DIAMOND_SWORD));
			for (int i = 0; i < 16; i++)			_superChestLoot.add(new ItemStack(Material.DIAMOND_AXE));
		}


		ItemStack stack = _baseChestLoot.get(UtilMath.r(_baseChestLoot.size()));
		if (superChest)		
			stack = _superChestLoot.get(UtilMath.r(_superChestLoot.size()));

		int amount = 1;

		if (stack.getType().getMaxStackSize() > 1)
			amount = Math.max(1, UtilMath.r(stack.getAmount()));

		if (stack.getTypeId() == 33)
			return ItemStackFactory.Instance.CreateStack(stack.getTypeId(), (byte)0, amount, "Barricade");

		return ItemStackFactory.Instance.CreateStack(stack.getTypeId(), amount);
	}

	@EventHandler
	public void StartEffectApply(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		for (Player player : GetPlayers(true))
		{
			Manager.GetCondition().Factory().Speed("Start Speed", player, player, 30, 1, false, false, false);
			Manager.GetCondition().Factory().HealthBoost("Start Health", player, player, 30, 1, false, false, false);

			player.setHealth(player.getMaxHealth());
		}
	}

	@EventHandler
	public void SpeedRemove(CustomDamageEvent event)
	{
		Player damager = event.GetDamagerPlayer(true);
		if (damager != null)
			Manager.GetCondition().EndCondition(damager, null, "Start Speed");
	}

	//If an item spawns and no one is there to see it, does it really spawn? No.
	@EventHandler
	public void ItemSpawn(ItemSpawnEvent event)
	{
		for (Player player : GetPlayers(true))
			if (UtilMath.offset(player, event.getEntity()) < 6)
				return;

		event.setCancelled(true);
	}

	@EventHandler
	public void RedBorderStart(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		//Start Red
		Block block;

		for (int x=WorldData.MinX ; x<=WorldData.MaxX ; x++)
		{
			block = WorldData.World.getHighestBlockAt(x, WorldData.MinZ);
			while (!UtilBlock.solid(block) && !block.isLiquid() && block.getY() > 0)
				block = block.getRelative(BlockFace.DOWN);
			if (block.getY() > 0)
				_redLocations.add(block.getLocation());

			block = WorldData.World.getHighestBlockAt(x, WorldData.MaxZ);
			while (!UtilBlock.solid(block) && !block.isLiquid() && block.getY() > 0)
				block = block.getRelative(BlockFace.DOWN);
			if (block.getY() > 0)
				_redLocations.add(block.getLocation());
		}

		for (int z=WorldData.MinZ ; z<=WorldData.MaxZ ; z++)
		{
			block = WorldData.World.getHighestBlockAt(WorldData.MinX, z);
			while (!UtilBlock.solid(block) && !block.isLiquid() && block.getY() > 0)
				block = block.getRelative(BlockFace.DOWN);
			if (block.getY() > 0)
				_redLocations.add(block.getLocation());

			block = WorldData.World.getHighestBlockAt(WorldData.MaxX, z);
			while (!UtilBlock.solid(block) && !block.isLiquid() && block.getY() > 0)
				block = block.getRelative(BlockFace.DOWN);
			if (block.getY() > 0)
				_redLocations.add(block.getLocation());
		}
	}

	public int RedMax()
	{
		return _maxSpreadRate;// + (24 - GetPlayers(true).size())*2;
	}

	@EventHandler
	public void RedUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (!IsLive())
			return;

		long time = System.currentTimeMillis();

		if (_redLocations.isEmpty())
			return;

		int max = Math.max(5, Math.min(RedMax(), _redLocations.size()/100));

		for (int i=0 ; i<max ; i++)
		{
			if (_redLocations.isEmpty())
				break;

			Location loc = _redLocations.remove(UtilMath.r(_redLocations.size()));

			//Already Red
			if (IsRed(loc.getBlock()))
			{
				i = Math.max(0, i-1);
				continue;
			}

			//Set to Red
			SetRed(loc);

			//Spread to Neighbours
			Block block = loc.getBlock();

			RedSpread(block.getRelative(BlockFace.UP));
			RedSpread(block.getRelative(BlockFace.DOWN));

			if (!RedSpread(block.getRelative(BlockFace.NORTH)))
			{
				RedSpread(block.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP));
				RedSpread(block.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN));
			}

			if (!RedSpread(block.getRelative(BlockFace.EAST)))
			{
				RedSpread(block.getRelative(BlockFace.EAST).getRelative(BlockFace.UP));
				RedSpread(block.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
			}

			if (!RedSpread(block.getRelative(BlockFace.SOUTH)))
			{
				RedSpread(block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP));
				RedSpread(block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN));
			}

			if (!RedSpread(block.getRelative(BlockFace.WEST)))
			{
				RedSpread(block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP));
				RedSpread(block.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
			}
		}

		totalTime += System.currentTimeMillis() - time;
	}

	@EventHandler
	public void RedTimer(UpdateEvent event)
	{
		if (event.getType() != UpdateType.MIN_01)
			return;

		if (!IsLive())
			return;

		System.out.println(" ");
		System.out.println("Game Time: " + UtilTime.MakeStr(System.currentTimeMillis() - this.GetStateTime()));
		System.out.println("Creep Size: " + _redLocations.size());
		System.out.println("Creep Time: " + UtilTime.convertString(totalTime, 4, TimeUnit.SECONDS));
		System.out.println(" ");

		totalTime = 0;
	}

	public boolean RedSpread(Block block)
	{
		if (block == null || block.getType() == Material.AIR)
			return false;

		//Inside Boundary
		if (UtilMath.offset(_spawn, block.getLocation()) < 48)
			return false;

		//Liquid
		if (block.isLiquid())
		{
			if (_ignoreLiquids)
				return false;

			//Only freeze surface water
			boolean surroundedByWater = true;
			for (Block other : UtilBlock.getSurrounding(block, false))
			{
				if (other.getY() < block.getY())
					continue;

				if (!other.isLiquid() && !IsRed(other))
				{
					surroundedByWater = false;
					break;
				}
			}
			if (surroundedByWater)
				return false;

			//Dont Spread to Moving
			if (block.getTypeId() == 9)
				if (block.getData() != 0)
					return false;

			//Dont spread near moving
			for (Block other : UtilBlock.getSurrounding(block, false))
			{
				if (other.getTypeId() == 9 && other.getData() != 0)
				{
					return false;
				}
			}
		}

		//Ignore Signs
		if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
			return false;

		//Pre-Red
		if (IsRed(block))
		{
			return false;
		}

		if ((!UtilBlock.solid(block) || UtilBlock.airFoliage(block) || block.getType() == Material.CHEST))
		{
			if (!block.isLiquid())
			{
				while (block.getType() == Material.VINE)
				{
					RedChangeBlock(block.getLocation(), 0, (byte)0);
					block = block.getRelative(BlockFace.DOWN);
				}

				//Remove Foliage
				if (block.getType() != Material.AIR)
					RedChangeBlock(block.getLocation(), 0, (byte)0);

				return false;
			}
		}

		//Outside Boundaries
		if (block.getX() < WorldData.MinX || block.getX() > WorldData.MaxX || block.getZ() < WorldData.MinZ || block.getZ() > WorldData.MaxZ)	
			return false;

		//Not Visible
		if (!UtilBlock.isVisible(block))
			return false;

		//Apply
		_redLocations.add(block.getLocation());
		return true;
	}

	public void RedChangeBlock(Location loc, int id, byte data)
	{
		if (!IsLive())
			return;

		MapUtil.ChunkBlockChange(loc, id, data, false);

		//Old Style
		/*
		if (true)
		{
			for (Player player : UtilServer.getPlayers())
			{
				if (Math.abs(player.getLocation().getChunk().getX() - loc.getChunk().getX()) > UtilServer.getServer().getViewDistance())
					continue;

				if (Math.abs(player.getLocation().getChunk().getZ() - loc.getChunk().getZ()) > UtilServer.getServer().getViewDistance())
					continue;

				player.sendBlockChange(loc, id, data);
			}

			return;
		}
		 */

		//Non-Queue
		for (Player player : UtilServer.getPlayers())
		{
			if (Math.abs(player.getLocation().getChunk().getX() - loc.getChunk().getX()) > UtilServer.getServer().getViewDistance())
				continue;

			if (Math.abs(player.getLocation().getChunk().getZ() - loc.getChunk().getZ()) > UtilServer.getServer().getViewDistance())
				continue;

			if (!_redChunks.containsKey(player))
				_redChunks.put(player, new ArrayList<ChunkChange>());

			boolean added = false;
			for (ChunkChange change : _redChunks.get(player))
			{
				if (change.Chunk.equals(loc.getChunk()))// && change.DirtyCount < 63)
				{
					change.AddChange(loc, id, data);
					added = true;
					break;
				}
			}
			if (!added)
				_redChunks.get(player).add(new ChunkChange(loc, id, data));
		}
	}

	@EventHandler
	public void RedChunkUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (!IsLive())
			return;

		for (Player player : _redChunks.keySet())
		{
			//Remove Far Away
			Iterator<ChunkChange> chunkIterator = _redChunks.get(player).iterator();
			while (chunkIterator.hasNext())
			{
				ChunkChange change = chunkIterator.next();

				if (Math.abs(player.getLocation().getChunk().getX() - change.Chunk.getX()) > UtilServer.getServer().getViewDistance() ||
						Math.abs(player.getLocation().getChunk().getZ() - change.Chunk.getZ()) > UtilServer.getServer().getViewDistance())
				{
					chunkIterator.remove();
				}
			}

			if (_redChunks.get(player).isEmpty())
				continue;

			//Get Fittest Chunk to Update
			int bestId = -1;
			double bestScore = 0;

			for (int i=0 ; i<_redChunks.get(player).size() ; i++)
			{
				ChunkChange change = _redChunks.get(player).get(i);

				//Base Score, 1 per block
				double score = change.Changes.size();

				//Time Score, multiply block score by 1 + 0.5 per second
				score = score * (1 + (System.currentTimeMillis() - change.Time)/200d);

				//Distance Divisor
				double dist = 0.5;
				if (!player.getLocation().getChunk().equals(change.Chunk))
				{
					int x = Math.abs(player.getLocation().getChunk().getX() - change.Chunk.getX());
					int z = Math.abs(player.getLocation().getChunk().getZ() - change.Chunk.getZ());
					dist = Math.sqrt(x*x + z*z);
				}
				score = score/(dist*dist);

				if (bestId == -1 || score > bestScore)
				{
					bestId = i;
					bestScore = score;
				}
			}

			if (bestId == -1)
				continue;

			//Send MultiBlock or Chunk Update for Fittest Chunk
			ChunkChange change = _redChunks.get(player).remove(bestId);

			if (change.DirtyCount > 63)
				MapUtil.SendChunkForPlayer(change.Chunk.getX(), change.Chunk.getZ(), player);
			else
				MapUtil.SendMultiBlockForPlayer(change.Chunk.getX(), change.Chunk.getZ(), change.DirtyBlocks, change.DirtyCount, change.Chunk.getWorld(), player);
		}
	}

	public boolean IsRed(Block block)
	{
		if (!_redMap.containsKey(block.getX()))
			return false;

		if (!_redMap.get(block.getX()).containsKey(block.getY()))
			return false;

		return _redMap.get(block.getX()).get(block.getY()).contains(block.getZ());
	}

	public void SetRed(Location loc)
	{
		//Save Red
		if (!_redMap.containsKey(loc.getBlockX()))
			_redMap.put(loc.getBlockX(), new HashMap<Integer, HashSet<Integer>>());

		if (!_redMap.get(loc.getBlockX()).containsKey(loc.getBlockY()))
			_redMap.get(loc.getBlockX()).put(loc.getBlockY(), new HashSet<Integer>());

		_redMap.get(loc.getBlockX()).get(loc.getBlockY()).add(loc.getBlockZ());

		//Red
		if (_spreadType == 0)
		{
			RedChangeBlock(loc, 159, (byte)14);
		}
		//Snow
		else if (_spreadType == 1)
		{
			if (loc.getBlock().getType() == Material.LEAVES)									//RedChangeBlock(loc, 79, (byte)0);							
			{

			}
			else if (loc.getBlock().getTypeId() == 8 || loc.getBlock().getTypeId() == 9)		RedChangeBlock(loc, 79, (byte)0);
			else if (loc.getBlock().getTypeId() == 10 || loc.getBlock().getTypeId() == 11)		RedChangeBlock(loc, 49, (byte)0);
			else																				RedChangeBlock(loc, 80, (byte)0);
		}
		//Nether
		else 
		{
			if (loc.getBlock().getType() == Material.LEAVES)									RedChangeBlock(loc, 88, (byte)0);
			else if (loc.getBlock().getTypeId() == 8 || loc.getBlock().getTypeId() == 9)		RedChangeBlock(loc, 49, (byte)0);
			else
			{
				double r = Math.random();
				if (r > 0.1)			RedChangeBlock(loc, 87, (byte)0);
				else					RedChangeBlock(loc, 153, (byte)0);
			}	
		}
	}

	@EventHandler
	public void RedAttack(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : GetPlayers(true))
		{
			boolean near = false;

			for (Block block : UtilBlock.getInRadius(player.getLocation(), 5d).keySet())
			{
				if (!IsRed(block))
					continue;

				near = true;

				//Red
				if (_spreadType == 0)
				{
					if (block.getRelative(BlockFace.UP).getType() == Material.AIR)
					{
						block.getRelative(BlockFace.UP).setType(Material.FIRE);
						break;
					}
				}

				//Snow
				else if (_spreadType == 1)
				{
					if (Math.random() > 0.95)
						player.playEffect(block.getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.SNOW_BLOCK);

					if (Math.random() > 0.8)
					{			
						Vector traj = UtilAlg.getTrajectory(block.getLocation().add(0.5, 1.5, 0.5), player.getLocation());

						Snowball ball = player.getWorld().spawn(block.getLocation().add(0.5, 1.5, 0.5).subtract(traj.clone().multiply(8 + UtilMath.r(8))), Snowball.class);	

						ball.setVelocity(UtilAlg.getTrajectory(ball.getLocation(), player.getEyeLocation().add(0, 3, 0)).add(new Vector(Math.random()-0.5, Math.random()-0.5, Math.random()-0.5).multiply(0.1)));
					}	
				}

				//Nether
				if (_spreadType == 2)
				{
					if (block.getRelative(BlockFace.UP).getType() == Material.AIR)
					{
						block.getRelative(BlockFace.UP).setType(Material.FIRE);
						break;
					}
				}
			}

			if (!near)
			{
				if (!UtilEnt.isGrounded(player))
				{
					Block block = player.getLocation().getBlock();

					while (!UtilBlock.solid(block) && block.getY() > 0)
						block = block.getRelative(BlockFace.DOWN);

					if (IsRed(block) || block.getY() == 0)
						near = true;
				}
			}

			if (!near)
			{
				if (_deathmatchLive && UtilMath.offset(player.getLocation(), _spawn) > 48)
					near = true;
			}

			if (near)
			{
				player.getWorld().playSound(player.getLocation(), Sound.AMBIENCE_RAIN, 0.5f, 0f);

				if (!_redOutTime.containsKey(player))
				{
					_redOutTime.put(player, System.currentTimeMillis());
				} 
				else
				{
					if (UtilTime.elapsed(_redOutTime.get(player), 5000))
					{ 
						Manager.GetDamage().NewDamageEvent(player, null, null, 
								DamageCause.VOID, 1, false, true, false,
								"Hunger Games", _spreadName);	
					}
				}
			}
			else
			{
				_redOutTime.remove(player);
			}
		}
	}

	@EventHandler
	public void RedDamage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;

		if (!(event.GetProjectile() instanceof Snowball))
			return;

		event.AddMod("Snowball", _spreadName, 2, true);

		event.AddKnockback("Snowball", 4);
	}

	@EventHandler
	public void DayNightCycle(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		long time = WorldData.World.getTime();

		if (time > 22000 || time < 14000)
		{
			WorldTimeSet = (WorldTimeSet + 4)%24000;
		}
		else
		{
			WorldTimeSet = (WorldTimeSet + 16)%24000;
		}

		WorldData.World.setTime(WorldTimeSet);
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
			if (_supplyCurrent == null && !_deathmatchCountdown && !_deathmatchLive)
			{
				if (_supplyLocations.isEmpty())
					return;

				_supplyCurrent = _supplyLocations.remove(UtilMath.r(_supplyLocations.size()));

				//Remove Prior
				_supplyChests.remove(_supplyCurrent.getBlock().getRelative(BlockFace.UP));
				_supplyCurrent.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);

				//Create New
				_supplyCurrent.getBlock().setType(Material.BEACON);
				for (int x=-1 ; x<=1 ; x++)
					for (int z=-1 ; z<=1 ; z++)
						_supplyCurrent.getBlock().getRelative(x, -1, z).setType(Material.IRON_BLOCK);

				//Announce
				this.Announce(C.cYellow + C.Bold + "Supply Drop Incoming (" + ChatColor.RESET + UtilWorld.locToStrClean(_supplyCurrent) + C.cYellow + C.Bold + ")");
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

				FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).with(Type.BURST).trail(false).build();
				UtilFirework.playFirework(_supplyEffect, effect);

				_supplyEffect.setY(_supplyEffect.getY()-2);

				if (UtilMath.offset(_supplyEffect, _supplyCurrent) < 2)
				{
					effect = FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).with(Type.BALL_LARGE).trail(true).build();
					UtilFirework.playFirework(_supplyEffect, effect);

					//Create Chest
					_supplyCurrent.getBlock().setType(Material.GLASS);
					_supplyCurrent.getBlock().getRelative(BlockFace.UP).setType(Material.CHEST);
					_supplyChests.add(_supplyCurrent.getBlock().getRelative(BlockFace.UP));
					_openedChest.remove(_supplyCurrent);

					//Reset
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

		if (_supplyChests.isEmpty())
			return;

		Iterator<Block> chestIterator = _supplyChests.iterator();

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
	public void DeathmatchUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (_deathmatchLive)
		{
			if (event.getType() != UpdateType.SEC)
				return;

			if (_deathmatchTime <= 0)
				return;

			for (Player player : GetPlayers(true))
				VisibilityManager.Instance.refreshPlayerToAll(player);

			Announce(C.cRed + C.Bold + "Deathmatch in " + _deathmatchTime + "...");
			_deathmatchTime--;
		}

		else if (_deathmatchCountdown)
		{
			if (event.getType() != UpdateType.TICK)
				return;

			long timeLeft = 60000 - (System.currentTimeMillis() - _deathmatchTime);

			if (timeLeft <= 0)
			{ 
				_deathmatchLive = true;

				for (GameTeam team : GetTeamList())
					team.SpawnTeleport();

				_redLocations.clear();

				for (Block block : UtilBlock.getInRadius(_spawn, 52d).keySet())
					RedSpread(block);

				_deathmatchTime = 10;
			}	 
		}
		else
		{
			if (event.getType() != UpdateType.SEC)
				return;

			if (!UtilTime.elapsed(GetStateTime(), 360000))
				return;

			if (GetPlayers(true).size() > 4)
				return;

			if (!UtilTime.elapsed(_deathmatchTime, 60000))
				return;

			Announce(C.cGreen + C.Bold + "Type " + ChatColor.RESET + C.Bold + "/dm" + C.cGreen + C.Bold + " to start Deathmatch!");

			_deathmatchTime = System.currentTimeMillis();
		}
	}

	@EventHandler
	public void DeathmatchMoveCancel(PlayerMoveEvent event)
	{
		if (!_deathmatchLive)
			return;

		if (_deathmatchTime <= 0)
			return;

		if (UtilMath.offset2d(event.getFrom(), event.getTo()) == 0)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		event.setTo(event.getFrom());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void DeathmatchDamage(CustomDamageEvent event)
	{
		if (!_deathmatchLive)
			return;

		if (_deathmatchTime <= 0)
			return;

		event.SetCancelled("Deathmatch");
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

		if (!IsLive())
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Deathmatch cannot be started now."));
			return;
		}

		if (!UtilTime.elapsed(GetStateTime(), 360000))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Deathmatch cannot be started now."));
			return;
		}

		if (GetPlayers(true).size() > 4)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Deathmatch cannot be started now."));
			return;
		}

		if (_deathmatchCountdown)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Deathmatch cannot be started now."));
			return;
		}

		_deathmatchCountdown = true;

		Announce(C.cGreen + C.Bold + event.getPlayer().getName() + " has initiated Deathmatch!");
		Announce(C.cGreen + C.Bold + "Deathmatch starting in 60 seconds...");
		_deathmatchTime = System.currentTimeMillis();

		for (Player player : UtilServer.getPlayers())
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void CropTrample(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.PHYSICAL)
			return;

		if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.SOIL)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void CompassUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		if (!IsLive())
			return;

		//Night Time > Drop Target
		if (WorldData.World.getTime() > 14000 && WorldData.World.getTime() < 23000 && _supplyCurrent != null)
		{
			for (Player player : GetPlayers(true))
			{
				player.setCompassTarget(_supplyCurrent);

				for (int i : player.getInventory().all(Material.COMPASS).keySet()) 
				{
					ItemStack stack = player.getInventory().getItem(i);

					ItemMeta itemMeta = stack.getItemMeta();
					itemMeta.setDisplayName(
							C.cWhite + C.Bold + "Supply Drop Location: " +  C.cYellow + UtilMath.trim(1, UtilMath.offset(player.getLocation(), _supplyCurrent)));
					stack.setItemMeta(itemMeta);

					player.getInventory().setItem(i, stack);
				}
			}
		}
		//Player Target
		else
		{
			for (Player player : GetPlayers(true))
			{	
				Player target = null;
				double bestDist = 0;

				for (Player other : Manager.GetGame().GetPlayers(true))
				{
					if (other.equals(player))
						continue;

					if (other.isSneaking())
						continue;
					
					if (GetTeam(player).equals(GetTeam(other)))
						continue;

					double dist = UtilMath.offset(player, other);

					if (target == null || dist < bestDist)
					{
						target = other;
						bestDist = dist;
					}
				}

				if (target != null)
				{
					player.setCompassTarget(target.getLocation());

					for (int i : player.getInventory().all(Material.COMPASS).keySet()) 
					{
						ItemStack stack = player.getInventory().getItem(i);

						ItemMeta itemMeta = stack.getItemMeta();
						itemMeta.setDisplayName(
								"    " + C.cWhite + C.Bold + "Nearest Player: " + GetTeam(target).GetColor() + target.getName() + 
								"    " + C.cWhite + C.Bold + "Distance: " + GetTeam(target).GetColor() + UtilMath.trim(1, bestDist));
						stack.setItemMeta(itemMeta);

						player.getInventory().setItem(i, stack);
					}
				}
			}
		}	
	}		

	@EventHandler
	public void DisallowBrewFurnace(PlayerInteractEvent event)
	{
		if (event.getClickedBlock() == null)
			return;

		if (event.getClickedBlock().getType() == Material.BREWING_STAND ||
			event.getClickedBlock().getType() == Material.FURNACE || 
			event.getClickedBlock().getType() == Material.BURNING_FURNACE)
			event.setCancelled(true);
	}

	@EventHandler
	public void CancelItemFrameBreak(HangingBreakEvent event)
	{
		if (event.getEntity() instanceof ItemFrame)
		{
			event.setCancelled(true);
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
	public void TNTThrow(PlayerInteractEvent event)
	{
		if (!IsLive())
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		Player player = event.getPlayer();

		if (!UtilInv.IsItem(player.getItemInHand(), Material.TNT, (byte)0))
			return;

		event.setCancelled(true);

		if (!Recharge.Instance.use(player, "Throw TNT", 0, true, false))
		{
			UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot use " + F.item("Throw TNT") + " yet."));
			return;
		}

		if (!Manager.GetGame().CanThrowTNT(player.getLocation()))
		{
			//Inform
			UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot use " + F.item("Throw TNT") + " here."));
			return;
		}

		UtilInv.remove(player, Material.TNT, (byte)0, 1);
		UtilInv.Update(player);

		TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);

		UtilAction.velocity(tnt, player.getLocation().getDirection(), 0.5, false, 0, 0.1, 10, false);

		_tntMap.put(tnt, player);
	}

	@EventHandler
	public void TNTExplosion(ExplosionPrimeEvent event)
	{
		if (!_tntMap.containsKey(event.getEntity()))
			return;

		Player player = _tntMap.remove(event.getEntity());

		for (Player other : UtilPlayer.getNearby(event.getEntity().getLocation(), 14))
			Manager.GetCondition().Factory().Explosion("Throwing TNT", other, player, 50, 0.1, false, false);
	}

	@EventHandler
	public void BlockPlace(BlockPlaceEvent event)
	{	
		if (IsRed(event.getBlockAgainst()))
		{
			event.setCancelled(true);
			return;
		}

		if (event.getItemInHand().getType() == Material.PISTON_BASE)
		{
			_placedBlocks.add(event.getBlock().getLocation());
			event.setCancelled(false);

			final Block block = event.getBlock();

			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					block.setType(Material.PISTON_BASE);
					block.setData((byte) 6);
				}
			}, 0);
		}
	}

	//@EventHandler
    public void TourneyKills(CombatDeathEvent event)
    {
	    if (!(event.GetEvent().getEntity() instanceof Player))
            return;
	   
	    Player killed = (Player)event.GetEvent().getEntity();
	
	    if (event.GetLog().GetKiller() != null)
	    {
            Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

            if (killer != null && !killer.equals(killed))
            {
            	//Manager.GetStatsManager().addStat(killer, GetName(), "kills", 1);
	        }
	    }
	    
	    if (event.GetLog().GetPlayer() != null)
	    {
            if (killed != null)
            {
            	//Manager.GetStatsManager().addStat(killed, GetName(), "deaths", 1);
	        }
	    }
    }
	
	@EventHandler
	public void BlockBreak(BlockBreakEvent event)
	{
		if (_placedBlocks.remove(event.getBlock().getLocation()))
		{
			event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, event.getBlock().getType());
			event.getBlock().setType(Material.AIR);
		}
	}

	@EventHandler
	public void BlockBurn(BlockBurnEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockSpread(BlockSpreadEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockFade(BlockFadeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockDecay(LeavesDecayEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void PlayerKillAward(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BALL_LARGE).trail(false).build();
		for (int i=0 ; i<3 ; i++)
			UtilFirework.launchFirework(event.GetEvent().getEntity().getLocation(), effect, null, 3);

		if (event.GetLog().GetKiller() == null)
			return;

		Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (killer == null)
			return;

		if (killer.equals(event.GetEvent().getEntity()))
			return;

		killer.giveExpLevels(1);
	}

	@EventHandler
	public void DisableDamageLevel(CustomDamageEvent event)
	{
		event.SetDamageToLevel(false);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void ExplosionDamageRemove(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}

	@EventHandler
	public void FixClean(PlayerQuitEvent event)
	{
		_redChunks.remove(event.getPlayer());
	}
}
