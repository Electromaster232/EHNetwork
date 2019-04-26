package mineplex.mapparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.MapUtil;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.mapparser.command.*;

public class MapParser extends JavaPlugin implements Listener
{
	private WorldManager _worldManager;
	
	private Parse _curParse = null;
	private HashMap<String, MapData> _mapData = new HashMap<String, MapData>();
	private HashSet<String> _mapsBeingZipped = new HashSet<String>();
	private List<BaseCommand> _commands = new ArrayList<BaseCommand>();
	private Location _spawnLocation;
	
	private HashMap<Player, Boolean> _permissionMap = new HashMap<Player, Boolean>();
	
	@Override
	public void onEnable()
	{
		_worldManager = new WorldManager(this);
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getServer().getWorlds().get(0).setSpawnLocation(0, 106, 0);
		_spawnLocation = new Location(getServer().getWorlds().get(0), 0, 106, 0);
		
		//Updates
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Ticker(this), 1, 1);

		_commands.add(new AuthorCommand(this));
		_commands.add(new AdminCommand(this));
		_commands.add(new CopySchematicsCommand(this));
		_commands.add(new CreateCommand(this));
		_commands.add(new DeleteCommand(this));
		_commands.add(new GameTypeCommand(this));
		_commands.add(new HubCommand(this));
		_commands.add(new ListCommand(this));
		_commands.add(new MapCommand(this));
		_commands.add(new NameCommand(this));
		_commands.add(new ParseCommand200(this));
		_commands.add(new ParseCommand400(this));
		_commands.add(new ParseCommand600(this));
		_commands.add(new RenameCommand(this));
		_commands.add(new SaveCommand(this));
		_commands.add(new WorldsCommand(this));
		_commands.add(new CopyCommand(this));
		_commands.add(new SpawnCommand(this));
		_commands.add(new SetSpawnCommand(this));
	}

	@Override
	public void onDisable()
	{
        
	} 
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		player.teleport(getSpawnLocation());
		
		ResetInventory(event.getPlayer());
		
		DisplayHelp(player);
	}
	
	@EventHandler
	public void permissionUpdate(TickEvent event)
	{
		for (Player player : UtilServer.getPlayers())
		{
			permissionSet(player);
		}
	}
	
	public void permissionSet(Player player)
	{
		boolean hasPermission = GetData(player.getWorld().getName()).HasAccess(player);
		
		if (!_permissionMap.containsKey(player) || _permissionMap.get(player) != hasPermission)
		{
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
			{
				player.addAttachment(plugin, "worldedit.*", hasPermission);
				player.addAttachment(plugin, "voxelsniper.sniper", hasPermission);
				player.addAttachment(plugin, "voxelsniper.brush.*", hasPermission);
			}
			
			_permissionMap.put(player, hasPermission);
			
			UtilPlayer.message(player, "Plugin Permissions: " + F.tf(hasPermission));
		}
	}
	
	
	public void DisplayHelp(Player player)
	{
		UtilPlayer.message(player, F.main("Parser", "Listing Commands;"));
	 	UtilPlayer.message(player, F.value("Parameters", "Parameters: <?> = Required, [?] = Optional"));
		UtilPlayer.message(player, F.value("/hub", "Return to hub world"));
		UtilPlayer.message(player, " ");
		UtilPlayer.message(player, F.value("/name <name>", "Set name for current map"));
		UtilPlayer.message(player, F.value("/author <name>", "Set author for current map"));
		UtilPlayer.message(player, F.value("/gametype <type>", "Set gametype for current map"));
		UtilPlayer.message(player, " ");
		UtilPlayer.message(player, F.value("/admin <name>", "Toggle admin for player on map"));
		UtilPlayer.message(player, " ");
		UtilPlayer.message(player, F.value("/create <name> [gametype]", "Creates a new map"));
		UtilPlayer.message(player, F.value("/delete <name> [gametype]", "Deletes an existing map"));
		UtilPlayer.message(player, F.value("/copy <name> <copy name>", "Copies an existing map"));
		UtilPlayer.message(player, " ");
		UtilPlayer.message(player, F.value("/list", "List maps"));
		UtilPlayer.message(player, F.value("/map <name> [gametype]", "Teleport to a map"));		
		UtilPlayer.message(player, " ");
		UtilPlayer.message(player, C.cYellow + "Documentation: " + C.cGreen + "http://tinyurl.com/mpxmaps");
		
	}
	
	@EventHandler
	public void Command(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();

		String[] parts = event.getMessage().split(" ");
		String commandLabel = parts[0].substring(1);
		String[] args = new String[parts.length - 1];
		System.arraycopy(parts, 1, args, 0, parts.length - 1);

		if (_curParse != null)
		{
			UtilPlayer.message(player, F.main("Parser", "Cannot use commands during Map Parse!"));
			return;
		}
		if (event.getMessage().toLowerCase().startsWith("/help"))
		{
			event.setCancelled(true);
			
			DisplayHelp(player);
		}

		for (BaseCommand command : _commands)
		{
			for (String alias : command.getAliases())
			{
				if (alias.equalsIgnoreCase(commandLabel))
				{
					event.setCancelled(true);
					
					if (!command.execute(player, commandLabel, args))
					{
						UtilPlayer.message(player, F.main("Parser", "Invalid Input."));
						UtilPlayer.message(player, F.elem(command.getUsage()));
					}

					return;
				}
			}
		}
	}

	public void sendValidGameTypes(Player player)
	{
		UtilPlayer.message(player, F.main("Parser", "Valid Game Types;"));

		String gameTypes = "";

		for (GameType game : GameType.values())
		{
			gameTypes += game.toString() + " ";
		}

		player.sendMessage(gameTypes);
	}

	@EventHandler
	public void ParseUpdate(TickEvent event)
	{
		if (_curParse == null)
			return;
		
		if (_curParse.Update())
		{
			Announce("Parse Completed!");
			
			Announce("Cleaning and Creating ZIP...");
			
			try
			{
				_worldManager.finalizeParsedWorld(_curParse.getWorld());
			}
			catch (Exception e)
			{
				Announce("Creating ZIP Failed! Please Try Again!");
				e.printStackTrace();
			}
			
			_curParse = null;
		}
	}
	
	@EventHandler
	public void DisableCreatures(EntitySpawnEvent event)
	{
		if (event.getEntityType() == EntityType.DROPPED_ITEM || event.getEntity() instanceof LivingEntity)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void DisableBurn(BlockBurnEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void DisableFire(BlockSpreadEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void DisableFade(BlockFadeEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void DisableDecay(LeavesDecayEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void DisableIceForm(BlockFormEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void Updates(PlayerMoveEvent event)
	{
		for (World world : this.getServer().getWorlds())
		{
			if (world.getName().toLowerCase().contains("halloween"))
				world.setTime(16000);
			else
				world.setTime(8000);
			world.setStorm(false);
		}
			
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.getPlayer().setGameMode(GameMode.CREATIVE);
	}
	
	@EventHandler
	public void SaveUnloadWorlds(TickEvent event)
	{
		for (final World world : getServer().getWorlds())
		{
			if (world.getName().equalsIgnoreCase("world"))
				continue;

			if (world.getName().startsWith("parse_"))
				continue;

			if (!world.getName().startsWith("map"))
				continue;

			if (world.getPlayers().isEmpty()) 
			{
				Announce("Saving & Closing World: " + F.elem(world.getName()));
				MapUtil.UnloadWorld(this, world, true);

				_mapsBeingZipped.add(world.getName());
				System.out.println("Starting backup of " + world);
				BackupTask backupTask = new BackupTask(this, world.getName(), new Callback<Boolean>()
				{
					@Override
					public void run(Boolean data)
					{
						System.out.println("Finished backup of " + world);
						_mapsBeingZipped.remove(world.getName());
					}
				});
			}
		}
	}

	public void Announce(String msg)
	{
		for (Player player : UtilServer.getPlayers())
		{
			player.sendMessage(C.cGold + msg);
		}

		System.out.println("[Announce] " + msg);
	}

	public boolean DoesMapExist(String mapName, GameType gameType)
	{
		return DoesMapExist(getWorldString(mapName, gameType));
	}

	public boolean DoesMapExist(String worldName)
	{
		File file = new File(worldName);

		if (file.exists() && file.isDirectory())
			return true;

		return false;
	}

	public String getShortWorldName(String worldName)
	{
		int lastIndexOfSeperator = worldName.lastIndexOf('/');

		if (lastIndexOfSeperator != -1)
			return worldName.substring(lastIndexOfSeperator + 1);

		return worldName;
	}

	public World GetMapWorld(String mapName, GameType gameType)
	{
		return GetMapWorld(getWorldString(mapName, gameType));
	}

	public World GetMapWorld(String worldName)
	{
		for (World world : this.getServer().getWorlds())
		{
			if (world.getName().equals(worldName))
				return world;
		}

		return null;
	}

	public String getWorldString(String mapName, GameType type)
	{
		return "map" + "/" + type.GetName() + "/" + mapName;
	}

	public List<String> getMapsByName(String name)
	{
		name = name.toLowerCase();

		List<String> maps = new LinkedList<String>();
		boolean matchesExact = false;

		for (GameType type : GameType.values())
		{

			File mapsFolder = new File("map" + File.separator + type.GetName());
			if (!mapsFolder.exists())
				continue;

			for (File file : mapsFolder.listFiles())
			{
				if (!file.isDirectory())
					continue;

				if (!file.getName().toLowerCase().contains(name))
					continue;

				if (file.getName().equalsIgnoreCase(name))
					matchesExact = true;

				maps.add(getWorldString(file.getName(), type));
			}
		}

		if (matchesExact)
		{
			Iterator<String> it = maps.iterator();
			while (it.hasNext())
			{
				String mapString = it.next();

				if (!mapString.toLowerCase().endsWith(name))
				{
					it.remove();
				}
			}
		}

		return maps;
	}

	public MapData GetData(String mapName)
	{
		if (_mapData.containsKey(mapName))
			return _mapData.get(mapName);

		MapData data = new MapData(mapName);

		_mapData.put(mapName, data);

		return data;
	}

	public Location getSpawnLocation()
	{
		return _spawnLocation;
	}

	public void ResetInventory(Player player)
	{
//		UtilInv.Clear(player); 
//
//		player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
//		player.getInventory().addItem(new ItemStack(Material.STONE_SPADE));
//		player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
//		player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
//		player.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
	}

	public WorldManager getWorldManager()
	{
		return _worldManager;
	}

	public void setCurrentParse(Parse parse)
	{
		_curParse = parse;
	}

	@EventHandler
	public void Chat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);

		String world = C.cDGreen + C.Bold + getShortWorldName(event.getPlayer().getWorld().getName());



		String name = C.cYellow + event.getPlayer().getName();
		if (GetData(event.getPlayer().getWorld().getName()).HasAccess(event.getPlayer()))
			name = C.cGreen + event.getPlayer().getName();

		String grayName = C.cBlue + event.getPlayer().getName();
		String grayWorld = C.cBlue + C.Bold + event.getPlayer().getWorld().getName();

		for (Player player : UtilServer.getPlayers())
		{
			if (player.getWorld().equals(event.getPlayer().getWorld()))
			{
				player.sendMessage(world + ChatColor.RESET + " " + name + ChatColor.RESET + " " + event.getMessage());
			}
			else
			{
				player.sendMessage(grayWorld + ChatColor.RESET + " " + grayName + ChatColor.RESET + " " + C.cGray + event.getMessage());
			}

		}

		System.out.println(world + ChatColor.RESET + " " + name + ChatColor.RESET + " " + event.getMessage());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void InteractCancel(PlayerInteractEvent event)
	{
		//Permission
		if (!GetData(event.getPlayer().getWorld().getName()).HasAccess(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void commandCancel(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().startsWith("/tp") ||
			event.getMessage().startsWith("/hub") ||
			event.getMessage().startsWith("/list") ||
			event.getMessage().startsWith("/map") ||
			event.getMessage().startsWith("/create") ||
			event.getMessage().startsWith("/copy") ||
			event.getMessage().startsWith("/delete"))
			return;
		
		//Permission
		if (!GetData(event.getPlayer().getWorld().getName()).HasAccess(event.getPlayer()))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Parser", "You do not have Build-Access for this Map."));
			event.setCancelled(true);
		}
	}

	public HashSet<String> getMapsBeingZipped()
	{
		return _mapsBeingZipped;
	}

	@EventHandler
	public void Join(PlayerJoinEvent event)
	{
		event.setJoinMessage(F.sys("Player Join", event.getPlayer().getName()));
	}

	@EventHandler
	public void Join(PlayerQuitEvent event)
	{
		event.setQuitMessage(F.sys("Player Quit", event.getPlayer().getName()));
	}
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void TeleportCommand(PlayerCommandPreprocessEvent event)
	{
		if (!event.getMessage().toLowerCase().startsWith("/tp"))
			return;
		
		Player player = event.getPlayer();
					
		String[] tokens = event.getMessage().split(" ");
		
		if (tokens.length != 2)
		{
			return;
		}
		
		event.setCancelled(true);
		
		Player target = UtilPlayer.searchOnline(player, tokens[1], true);
		if (target != null)
		{
			UtilPlayer.message(player, F.main("Game", "You teleported to " + F.name(target.getName()) + "."));
			player.teleport(target);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void FlySpeed(PlayerCommandPreprocessEvent event)
	{
		if (!event.getMessage().toLowerCase().startsWith("/speed"))
			return;
		
		Player player = event.getPlayer();
		
		String[] tokens = event.getMessage().split(" ");
		
		if (tokens.length != 2)
		{
			return;
		}
		
		event.setCancelled(true);
		
		try
		{
			float speed = Float.parseFloat(tokens[1]);
			
			player.setFlySpeed(speed);
			
			UtilPlayer.message(player, F.main("Game", "Fly Speed set to " + F.elem("" + speed) + "."));
		}
		catch (Exception e)
		{
			UtilPlayer.message(player, F.main("Game", "Invalid Speed Input."));
		}
	}
	
	private HashMap<Player, ArrayList<HashSet<BlockData>>> treeHistory = new HashMap<Player, ArrayList<HashSet<BlockData>>>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void treeRemover(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		//Permission
		if (!GetData(event.getPlayer().getWorld().getName()).HasAccess(event.getPlayer()))
		{
			return;
		}
		
		Player player = event.getPlayer();
		
		if (!UtilGear.isMat(player.getItemInHand(), Material.NETHER_STAR))
			return;
		
		event.setCancelled(true);
		
		//Remove
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if (event.getClickedBlock().getType() != Material.LOG)
			{
				player.sendMessage(C.cRed + C.Bold + "TreeTool: " + ChatColor.RESET + "Left-Click on Log");
				return;
			}
			
			HashSet<Block> toRemove = searchLog(new HashSet<Block>(), event.getClickedBlock());
			
			if (toRemove.isEmpty())
			{
				player.sendMessage(C.cRed + C.Bold + "TreeTool: " + ChatColor.RESET + "Left-Click on Log");
				return;
			}
			
			HashSet<BlockData> history = new HashSet<BlockData>();
			
			for (Block block : toRemove)
			{
				history.add(new BlockData(block));
				
				block.setType(Material.AIR);
			}
				
			if (!treeHistory.containsKey(player))
				treeHistory.put(player, new ArrayList<HashSet<BlockData>>());
			
			treeHistory.get(player).add(0, history);
			
			player.sendMessage(C.cRed + C.Bold + "TreeTool: " + ChatColor.RESET + "Tree Removed");
			
			while (treeHistory.get(player).size() > 10)
				treeHistory.get(player).remove(10);
		}
		else if (UtilEvent.isAction(event, ActionType.R))
		{
			if (!treeHistory.containsKey(player) || treeHistory.get(player).isEmpty())
			{
				player.sendMessage(C.cGreen + C.Bold + "TreeTool: " + ChatColor.RESET + "No Tree History");
				return;
			}
			
			HashSet<BlockData> datas = treeHistory.get(player).remove(0);
			
			for (BlockData data : datas)
				data.restore();
			
			player.sendMessage(C.cGreen + C.Bold + "TreeTool: " + ChatColor.RESET + "Tree Restored");
		}
	}

	private HashSet<Block> searchLog(HashSet<Block> blocks, Block current)
	{
		//Not Tree
		if (current.getType() != Material.LOG && current.getType() != Material.LEAVES)
			return blocks;
		
		if (!blocks.add(current))
			return blocks;
		
		for (Block other : UtilBlock.getSurrounding(current, true))
		{
			if (current.getType() != Material.LOG && current.getType() != Material.LEAVES)
				continue;
			
			if (blocks.contains(other))
				continue;
			
			//Dont spread from leaves to log
			if (current.getType() == Material.LEAVES && other.getType() == Material.LOG)
				continue;
			
			searchLog(blocks, other);
		}
		
		return blocks;
	}
}
