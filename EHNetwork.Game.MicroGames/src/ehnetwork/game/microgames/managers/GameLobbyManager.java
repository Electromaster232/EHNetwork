package ehnetwork.game.microgames.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.WatchableObject;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilBlockText;
import ehnetwork.core.common.util.UtilBlockText.TextAlign;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.cosmetic.event.ActivateGemBoosterEvent;
import ehnetwork.core.donation.Donor;
import ehnetwork.core.event.CustomTagEvent;
import ehnetwork.core.packethandler.IPacketHandler;
import ehnetwork.core.packethandler.PacketHandler;
import ehnetwork.core.packethandler.PacketInfo;
import ehnetwork.core.packethandler.PacketVerifier;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.games.uhc.UHC;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.KitSorter;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class GameLobbyManager implements Listener, IPacketHandler
{
	public MicroGamesManager Manager;

	private Location _gameText;
	private Location _advText;
	private Location _kitText;
	private Location _teamText;

	private Location _kitDisplay;
	private Location _teamDisplay;  

	private Location spawn;

	private NautHashMap<Entity, LobbyEnt> _kits = new NautHashMap<Entity, LobbyEnt>();
	private NautHashMap<Block, Material> _kitBlocks = new NautHashMap<Block, Material>();

	private NautHashMap<Entity, LobbyEnt> _teams = new NautHashMap<Entity, LobbyEnt>();
	private NautHashMap<Block, Material> _teamBlocks = new NautHashMap<Block, Material>();

	private long _fireworkStart;
	private Color _fireworkColor;

	private int _advertiseStage = 0;
 
	//Scoreboard
	private NautHashMap<Player, Scoreboard> _scoreboardMap = new NautHashMap<Player, Scoreboard>();
	private NautHashMap<Player, Integer> _gemMap = new NautHashMap<Player, Integer>();
	private NautHashMap<Player, Integer> _eloMap = new NautHashMap<Player, Integer>();
	private NautHashMap<Player, String> _kitMap = new NautHashMap<Player, String>();

	private int _oldPlayerCount = 0;
	private int _oldMaxPlayerCount = 0; // Used for scoreboard when max player count changes
	
	private boolean _handlingPacket = false;
	private String _serverName;
	
	private boolean _colorTick = false;

	public GameLobbyManager(MicroGamesManager manager, PacketHandler packetHandler)
	{
		Manager = manager;

		packetHandler.addPacketHandler(this);
		
		World world = UtilWorld.getWorld("world");
		
		world.setTime(6000);
		world.setStorm(false);
		world.setThundering(false);
		world.setGameRuleValue("doDaylightCycle", "false");
		
		spawn = new Location(world, 0, 104, 0);

		_gameText = new Location(world, 0, 130, 50);
		_kitText = new Location(world, -40, 120, 0);
		_teamText = new Location(world, 40, 120, 0);
		_advText = new Location(world, 0, 140, -60);

		_kitDisplay = new Location(world, -17, 101, 0);
		_teamDisplay = new Location(world, 18, 101, 0);

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
				
		_serverName = Manager.getPlugin().getConfig().getString("serverstatus.name");
		_serverName = _serverName.substring(0, Math.min(16,  _serverName.length()));
	}

	private boolean HasScoreboard(Player player)
	{
		return _scoreboardMap.containsKey(player);
	}

	public void CreateScoreboards()
	{
		for (Player player : UtilServer.getPlayers())
		{
			CreateScoreboard(player, false);
		}
		
		for (Player otherPlayer : UtilServer.getPlayers())
		{
			AddPlayerToScoreboards(otherPlayer, null);
		}
	}

	private void CreateScoreboard(Player player, boolean resendToAll) 
	{
		_scoreboardMap.put(player, Bukkit.getScoreboardManager().getNewScoreboard());

		Scoreboard scoreboard = _scoreboardMap.get(player);
		Objective objective = scoreboard.registerNewObjective("§l" + "Lobby", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		for (Rank rank : Rank.values())
		{
			if (rank == Rank.ALL)
			{
				scoreboard.registerNewTeam(rank.Name).setPrefix("");
			}
			else
			{
				scoreboard.registerNewTeam(rank.Name).setPrefix(rank.GetTag(true, true) + ChatColor.RESET + " " + ChatColor.WHITE);
			}

			if (Manager.GetGame() != null && !Manager.GetGame().GetTeamList().isEmpty())
			{
				for (GameTeam team : Manager.GetGame().GetTeamList())
				{
					if(team.GetDisplaytag())
					{
						scoreboard.registerNewTeam(rank.Name + team.GetName().toUpperCase()).setPrefix(team.GetColor() + C.Bold + team.GetName() + team.GetColor() + " ");	
					}
					else 
					{
						if (rank == Rank.ALL)
						{
							scoreboard.registerNewTeam(rank.Name + team.GetName().toUpperCase()).setPrefix(team.GetColor() + "");
						}
						else
						{
							scoreboard.registerNewTeam(rank.Name + team.GetName().toUpperCase()).setPrefix(rank.GetTag(true, true) + ChatColor.RESET + " " + team.GetColor());
						}
					}
				}
			}
		}

		if (resendToAll)
		{
			for (Player otherPlayer : UtilServer.getPlayers())
			{
				String teamName = null;
				if (Manager.GetGame() != null && Manager.GetGame().GetTeam(otherPlayer) != null)
					teamName = Manager.GetGame().GetTeam(otherPlayer).GetName().toUpperCase();

				AddPlayerToScoreboards(otherPlayer, teamName);
			}
		}
	}

	public Collection<Scoreboard> GetScoreboards()
	{
		return _scoreboardMap.values();
	}

	public void WriteLine(Player player, int x, int y, int z, BlockFace face, int line, String text)
	{
		Location loc = player.getLocation();
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);

		int id = 159;
		byte data = 15;

		if (player.getItemInHand() != null && player.getItemInHand().getType().isBlock() && player.getItemInHand().getType() != Material.AIR)
		{
			id = player.getItemInHand().getTypeId();
			data = UtilInv.GetData(player.getItemInHand());
		}

		if (line > 0)
			loc.add(0, line*-6, 0);

		UtilBlockText.MakeText(text, loc, face, id, data, TextAlign.CENTER);

		player.sendMessage("Writing: " + text + " @ " + UtilWorld.locToStrClean(loc));
	}

	public void WriteGameLine(String text, int line, int id, byte data)
	{
		Location loc = new Location(_gameText.getWorld(), _gameText.getX(), _gameText.getY(), _gameText.getZ());

		if (line > 0)
			loc.add(0, line*-6, 0);

		BlockFace face = BlockFace.WEST;

		UtilBlockText.MakeText(text, loc, face, id, data, TextAlign.CENTER);
	}

	public void WriteAdvertiseLine(String text, int line, int id, byte data)
	{
		Location loc = new Location(_advText.getWorld(), _advText.getX(), _advText.getY(), _advText.getZ());

		if (line > 0)
			loc.add(0, line*-6, 0);

		BlockFace face = BlockFace.EAST;

		UtilBlockText.MakeText(text, loc, face, id, data, TextAlign.CENTER);
	}

	public void WriteKitLine(String text, int line, int id, byte data)
	{
		Location loc = new Location(_kitText.getWorld(), _kitText.getX(), _kitText.getY(), _kitText.getZ());

		if (line > 0)
			loc.add(0, line*-6, 0);

		BlockFace face = BlockFace.NORTH;

		UtilBlockText.MakeText(text, loc, face, id, data, TextAlign.CENTER);
	}

	public void WriteTeamLine(String text, int line, int id, byte data)
	{
		Location loc = new Location(_teamText.getWorld(), _teamText.getX(), _teamText.getY(), _teamText.getZ());

		if (line > 0)
			loc.add(0, line*-6, 0);

		BlockFace face = BlockFace.SOUTH;

		UtilBlockText.MakeText(text, loc, face, id, data, TextAlign.CENTER);
	}

	public Location GetSpawn() 
	{	
		return spawn.clone().add(4 - Math.random()*8, 0, 4 - Math.random()*8);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void TeamGeneration(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Recruit)
			return;

		if (event.GetGame().GetMode() == null)
			WriteGameLine(event.GetGame().WorldData.MapName, 1, 159, (byte)4);
		else
			WriteGameLine(event.GetGame().WorldData.MapName, 2, 159, (byte)4);
		
		CreateTeams(event.GetGame());
	}

	public void CreateTeams(Game game)
	{
		//Text
		WriteTeamLine("Select", 0, 159, (byte)15);
		WriteTeamLine("Team", 1, 159, (byte)4);

		//Remove Old Ents
		for (Entity ent : _teams.keySet())
			ent.remove();
		_teams.clear();

		//Remove Blocks
		for (Block block : _teamBlocks.keySet())
			block.setType(_teamBlocks.get(block));
		_teamBlocks.clear();
		
		//Smash
		if (game.HideTeamSheep)
		{
			//Text
			WriteTeamLine("Select", 0, 159, (byte)15);
			WriteTeamLine("Kit", 1, 159, (byte)4);
			
			CreateScoreboards();
			return;
		}
				
		//Standard
		if ((game.GetKits().length > 1 || game.GetTeamList().size() < 6) && game.GetType() != GameType.SurvivalGamesTeams)
		{
			//Display
			ArrayList<GameTeam> teams = new ArrayList<GameTeam>();
			
			for (GameTeam team : game.GetTeamList())
				if (team.GetVisible())
					teams.add(team);
			
			//Positions
			double space = 6;		
			double offset = (teams.size()-1)*space/2d;

			for (int i=0 ; i<teams.size() ; i++)
			{
				Location entLoc = _teamDisplay.clone().subtract(0, 0, i*space - offset);

				SetKitTeamBlocks(entLoc.clone(), 35, teams.get(i).GetColorData(), _teamBlocks);

				entLoc.add(0, 1.5, 0);

				entLoc.getChunk().load();

				Sheep ent = (Sheep)Manager.GetCreature().SpawnEntity(entLoc, EntityType.SHEEP);
				ent.setRemoveWhenFarAway(false);
				ent.setCustomNameVisible(true);

				ent.setColor(DyeColor.getByWoolData(teams.get(i).GetColorData()));

				UtilEnt.Vegetate(ent);

				teams.get(i).SetTeamEntity(ent);

				_teams.put(ent, new LobbyEnt(ent, entLoc, teams.get(i)));
			}
		}
		//Double
		else
		{
			//Text
			WriteKitLine("Select", 0, 159, (byte)15);
			WriteKitLine("Team", 1, 159, (byte)4);
			
			//Display
			ArrayList<GameTeam> teamsA = new ArrayList<GameTeam>();
			ArrayList<GameTeam> teamsB = new ArrayList<GameTeam>();
			
			for (int i=0 ; i<game.GetTeamList().size() ; i++)
			{
				if (i < game.GetTeamList().size()/2)
					teamsA.add(game.GetTeamList().get(i));
				else
					teamsB.add(game.GetTeamList().get(i));
			}
			
			//A
			{
				//Positions
				double space = 6;		
				double offset = (teamsA.size()-1)*space/2d;

				for (int i=0 ; i<teamsA.size() ; i++)
				{
					Location entLoc = _teamDisplay.clone().subtract(0, 0, i*space - offset);

					SetKitTeamBlocks(entLoc.clone(), 35, teamsA.get(i).GetColorData(), _teamBlocks);

					entLoc.add(0, 1.5, 0);

					entLoc.getChunk().load();

					Sheep ent = (Sheep)Manager.GetCreature().SpawnEntity(entLoc, EntityType.SHEEP);
					ent.setRemoveWhenFarAway(false);
					ent.setCustomNameVisible(true);

					ent.setColor(DyeColor.getByWoolData(teamsA.get(i).GetColorData()));

					UtilEnt.Vegetate(ent);

					teamsA.get(i).SetTeamEntity(ent);

					_teams.put(ent, new LobbyEnt(ent, entLoc, teamsA.get(i)));
				}
			}
			//B
			{
				//Positions
				double space = 6;		
				double offset = (teamsB.size()-1)*space/2d;

				for (int i=0 ; i<teamsB.size() ; i++)
				{
					Location entLoc = _kitDisplay.clone().subtract(0, 0, i*space - offset);

					SetKitTeamBlocks(entLoc.clone(), 35, teamsB.get(i).GetColorData(), _teamBlocks);

					entLoc.add(0, 1.5, 0);

					entLoc.getChunk().load();

					Sheep ent = (Sheep)Manager.GetCreature().SpawnEntity(entLoc, EntityType.SHEEP);
					ent.setRemoveWhenFarAway(false);
					ent.setCustomNameVisible(true);

					ent.setColor(DyeColor.getByWoolData(teamsB.get(i).GetColorData()));

					UtilEnt.Vegetate(ent);

					teamsB.get(i).SetTeamEntity(ent);

					_teams.put(ent, new LobbyEnt(ent, entLoc, teamsB.get(i)));
				}
			}
		}

		CreateScoreboards();
	}

	public void CreateKits(Game game)
	{
		//Text
		WriteKitLine("Select", 0, 159, (byte)15);
		WriteKitLine("Kit", 1, 159, (byte)4);

		//Remove Old Ents
		for (Entity ent : _kits.keySet())
			ent.remove();
		_kits.clear();

		//Remove Blocks
		for (Block block : _kitBlocks.keySet())
			block.setType(_kitBlocks.get(block));
		_kitBlocks.clear();

		if (game.GetKits().length <= 1 && game.GetType() == GameType.UHC)
		{
			WriteKitLine("      ", 0, 159, (byte)15);
			WriteKitLine("      ", 1, 159, (byte)4);
			return;
		}

		//Display
		ArrayList<Kit> kits = new ArrayList<Kit>();
		for (Kit kit : game.GetKits())
		{
			if (kit.GetAvailability() != KitAvailability.Hide)
				kits.add(kit);
		}

		// Break up the kits into chunks with respect to Null Kits
		ArrayList<List<Kit>> kitChunks = new ArrayList<List<Kit>>();
		int lastBreak = 0;
		for (int i = 0; i < kits.size(); i++)
		{
			if (i == kits.size() - 1 || kits.get(i).GetAvailability() == KitAvailability.Null)
			{
				kitChunks.add(kits.subList(lastBreak, i + 1));
				lastBreak = i + 1;
			}
		}

		// Sort each kit chunk
		for (List<Kit> kitList : kitChunks)
			Collections.sort(kitList, new KitSorter());

		// Create the new sorted list
		kits = new ArrayList<Kit>();
		for (List<Kit> kitList : kitChunks)
		{
			kits.addAll(kitList);
		}

		//Smash
		if (game.ReplaceTeamsWithKits)
		{
//			WriteKitLine("Free", 0, 159, (byte)15);
//			WriteKitLine("Kits", 1, 159, (byte)4);
			
			ArrayList<Kit> kitsA = new ArrayList<Kit>();
			ArrayList<Kit> kitsB = new ArrayList<Kit>();
			
			for (int i=0 ; i<kits.size() ; i++)
			{
				if (kits.get(i).GetCost() < 5000)								kitsA.add(kits.get(i));
				else															kitsB.add(kits.get(i));
			}
			
			{
				//Positions
				double space = 4;		
				double offset = (kitsA.size()-1)*space/2d;

				for (int i=0 ; i<kitsA.size() ; i++)
				{
					Kit kit = kitsA.get(i);

					if (kit.GetAvailability() == KitAvailability.Null)
						continue;

					Location entLoc = _kitDisplay.clone().subtract(0, 0, i*space - offset);

					byte data = 4;
					if (kit.GetAvailability() == KitAvailability.Gem) 				data = 5;
					else if (kit.GetAvailability() == KitAvailability.Achievement) 	data = 2;
					SetKitTeamBlocks(entLoc.clone(), 35, data, _kitBlocks);

					entLoc.add(0, 1.5, 0);

					entLoc.getChunk().load();

					Entity ent = kit.SpawnEntity(entLoc);

					if (ent == null)
						continue;

					_kits.put(ent, new LobbyEnt(ent, entLoc, kit));
				}
			}
			{
				//Positions
				double space = 4;		
				double offset = (kitsB.size()-1)*space/2d;

				for (int i=0 ; i<kitsB.size() ; i++)
				{
					Kit kit = kitsB.get(i);

					if (kit.GetAvailability() == KitAvailability.Null)
						continue;

					Location entLoc = _teamDisplay.clone().subtract(0, 0, i*space - offset);

					byte data = 4;
					if (kit.GetAvailability() == KitAvailability.Gem) 				data = 5;
					else if (kit.GetAvailability() == KitAvailability.Achievement) 	data = 2;
					SetKitTeamBlocks(entLoc.clone(), 35, data, _kitBlocks);

					entLoc.add(0, 1.5, 0);

					entLoc.getChunk().load();

					Entity ent = kit.SpawnEntity(entLoc);

					if (ent == null)
						continue;

					_kits.put(ent, new LobbyEnt(ent, entLoc, kit));
				}
			}
			
			return;
		}

		//Positions
		double space = 4;		
		double offset = (kits.size()-1)*space/2d;

		for (int i=0 ; i<kits.size() ; i++)
		{
			Kit kit = kits.get(i);

			if (kit.GetAvailability() == KitAvailability.Null)
				continue;

			Location entLoc = _kitDisplay.clone().subtract(0, 0, i*space - offset);

			byte data = 4;
			if (kit.GetAvailability() == KitAvailability.Gem) 				data = 5;
			else if (kit.GetAvailability() == KitAvailability.Achievement) 	data = 2;
			SetKitTeamBlocks(entLoc.clone(), 35, data, _kitBlocks);

			entLoc.add(0, 1.5, 0);

			entLoc.getChunk().load();

			Entity ent = kit.SpawnEntity(entLoc);

			if (ent == null)
				continue;

			_kits.put(ent, new LobbyEnt(ent, entLoc, kit));
		}
	}

	public void SetKitTeamBlocks(Location loc, int id, byte data, NautHashMap<Block, Material> blockMap) 
	{
		//Coloring
		Block block = loc.clone().add( 0.5, 0,  0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

		block = loc.clone().add(-0.5, 0,  0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

		block = loc.clone().add( 0.5, 0, -0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

		block = loc.clone().add(-0.5, 0, -0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

		//Top
		block = loc.clone().add( 0.5, 1,  0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

		block = loc.clone().add(-0.5, 1,  0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

		block = loc.clone().add( 0.5, 1, -0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

		block = loc.clone().add(-0.5, 1, -0.5).getBlock();
		blockMap.put(block, block.getType());
		MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

		//Floor
		for (int x=-2 ; x<2 ; x++)
		{
			for (int z=-2 ; z<2 ; z++)
			{
				block = loc.clone().add(x + 0.5, -1,  z + 0.5).getBlock();

				blockMap.put(block, block.getType());
				MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);
			}
		}

		//Outline
		for (int x=-3 ; x<3 ; x++)
		{
			for (int z=-3 ; z<3 ; z++)
			{
				block = loc.clone().add(x + 0.5, -1,  z + 0.5).getBlock();

				if (blockMap.containsKey(block))
					continue;

				blockMap.put(block, block.getType());
				MapUtil.QuickChangeBlockAt(block.getLocation(), 35, (byte)15);
			}
		}
	}
	
	public void AddKitLocation(Entity ent, Kit kit, Location loc)
	{
		_kits.put(ent, new LobbyEnt(ent, loc, kit));
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		_scoreboardMap.remove(event.getPlayer());
		_gemMap.remove(event.getPlayer());
		_kitMap.remove(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void DamageCancel(CustomDamageEvent event)
	{
		if (_kits.containsKey(event.GetDamageeEntity()))
			event.SetCancelled("Kit Cancel");
	}

	@EventHandler
	public void Update(UpdateEvent event) 
	{
		if (event.getType() == UpdateType.FAST)
		{
			//spawn.getWorld().setTime(6000);
			//spawn.getWorld().setStorm(false);
			//spawn.getWorld().setThundering(false);
		}


		if (event.getType() == UpdateType.TICK)
			UpdateEnts();

		if (event.getType() == UpdateType.FASTEST)
			UpdateFirework();

		if (event.getType() == UpdateType.SLOW)
			UpdateAdvertise();

		ScoreboardDisplay(event);
		ScoreboardSet(event);
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		
		if (!event.getWorld().equals(spawn.getWorld()))
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	private void RemoveInvalidEnts(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Recruit)
			return;
		
		for (Entity ent : UtilWorld.getWorld("world").getEntities())
		{
			if (ent instanceof Creature || ent instanceof Slime)
			{
				if (_kits.containsKey(ent))
					continue;

				if (_teams.containsKey(ent))
					continue;

				if (ent.getPassenger() != null)
					continue;
				
				ent.remove();
			}
		}
	}

	private void UpdateAdvertise() 
	{
		if (Manager.GetGame() == null || Manager.GetGame().GetState() != GameState.Recruit)
			return;
		
		_advertiseStage = (_advertiseStage+1)%2;
		
		if (Manager.GetGame().AdvertiseText(this, _advertiseStage))
		{
			return;
		}

		if (_advertiseStage == 0)
		{
			WriteAdvertiseLine("GET EH ULTRA", 0, 159, (byte)4);
			WriteAdvertiseLine("FOR AMAZING", 1, 159, (byte)15);
			WriteAdvertiseLine("FUN TIMES", 2, 159, (byte)15);

			WriteAdvertiseLine("www.endl.site", 4, 159, (byte)15);
		}
		else if (_advertiseStage == 1)
		{
			WriteAdvertiseLine("KEEP CALM", 0, 159, (byte)4);
			WriteAdvertiseLine("AND", 1, 159, (byte)15);
			WriteAdvertiseLine("PLAY ENDLESS", 2, 159, (byte)4);

			WriteAdvertiseLine("www.endl.site", 4, 159, (byte)15);
		}
	}

	public void UpdateEnts()
	{
		for (Entity ent : _kits.keySet())
			ent.teleport(_kits.get(ent).GetLocation());

		for (Entity ent : _teams.keySet())
			ent.teleport(_teams.get(ent).GetLocation());
	}

	public Kit GetClickedKit(Entity clicked)
	{
		for (LobbyEnt ent : _kits.values())
			if (clicked.equals(ent.GetEnt()))
				return ent.GetKit();

		return null;
	}

	public GameTeam GetClickedTeam(Entity clicked)
	{
		for (LobbyEnt ent : _teams.values())
			if (clicked.equals(ent.GetEnt()))
				return ent.GetTeam();

		return null;
	}

	public void RegisterFireworks(GameTeam winnerTeam)
	{
		if (winnerTeam != null)
		{
			_fireworkColor = Color.GREEN;
			if (winnerTeam.GetColor() == ChatColor.RED)			_fireworkColor = Color.RED;
			if (winnerTeam.GetColor() == ChatColor.AQUA)		_fireworkColor = Color.BLUE;
			if (winnerTeam.GetColor() == ChatColor.YELLOW)		_fireworkColor = Color.YELLOW;

			_fireworkStart = System.currentTimeMillis();
		}
	}

	public void UpdateFirework()
	{
		if (UtilTime.elapsed(_fireworkStart, 10000))
			return;

		UtilFirework.playFirework(spawn.clone().add(Math.random()*160-80, 30 + Math.random()*10, Math.random()*160-80), 
				Type.BALL_LARGE, _fireworkColor, false, false);
	}

	@EventHandler
	public void Combust(EntityCombustEvent event) 
	{
		for (LobbyEnt ent : _kits.values())
			if (event.getEntity().equals(ent.GetEnt()))
			{
				event.setCancelled(true);
				return;
			}
	}

	public void DisplayLast(Game game) 
	{
		//Start Fireworks
		RegisterFireworks(game.WinnerTeam);
	}

	public void DisplayNext(Game game, HashMap<String, ChatColor> pastTeams) 
	{
		WriteGameLine(game.GetType().GetLobbyName(), 0, 159, (byte)14);
		
		if (game.GetMode() == null)
			WriteGameLine("      ", 1, 159, (byte)1);
		else
			WriteGameLine(game.GetMode(), 1, 159, (byte)1);	
		
		DisplayWaiting();
		CreateKits(game);
		CreateTeams(game);
	}

	public void DisplayWaiting()
	{
		WriteGameLine("waiting for players", 3, 159, (byte)13);
	}

	@EventHandler
	public void ScoreboardDisplay(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (Manager.GetGame() != null && 
			(Manager.GetGame().GetState() != GameState.Loading && 
			Manager.GetGame().GetState() != GameState.Recruit))
		{
			for (Player player : UtilServer.getPlayers())
				player.setScoreboard(Manager.GetGame().GetScoreboard().GetScoreboard()); //XXX
		}

		else
		{
			for (Player player : UtilServer.getPlayers())
			{
				if (!HasScoreboard(player))
				{
					CreateScoreboard(player, true);
				}
				else
				{
					player.setScoreboard(_scoreboardMap.get(player));	
				}
			}
		}
	}
		
	//this is called from above
	public void ScoreboardSet(UpdateEvent event) 
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (Manager.GetGame() != null && !Manager.GetGame().DisplayLobbySide)
		{
			return;
		}
		
		_colorTick = !_colorTick;
		
		for (Entry<Player, Scoreboard> entry : _scoreboardMap.entrySet())		
		{	
			Objective objective = entry.getValue().getObjective("§l" + "Lobby");

			if (Manager.GetGame() != null && Manager.GetGame().GetCountdown() >= 0)
			{
				if (Manager.GetGame().GetCountdown() > 0)
					objective.setDisplayName(C.Bold + "§lStarting in " + C.cGreen + "§l" + Manager.GetGame().GetCountdown() + (Manager.GetGame().GetCountdown() == 1 ? " Second" : " Seconds"));
				else if (Manager.GetGame().GetCountdown() == 0)
					objective.setDisplayName(ChatColor.WHITE + "§lIn Progress...");
			}
			else
			{
				if (Manager.GetGame() instanceof UHC && !((UHC)Manager.GetGame()).isMapLoaded())
				{
					objective.setDisplayName(((UHC)Manager.GetGame()).getMapLoadPercent() + " " + (_colorTick ? ChatColor.GREEN : ChatColor.YELLOW) + "§l" + "Generating Map");
				}
				else
				{
					objective.setDisplayName(ChatColor.GREEN + "§l" + "Waiting for Players");
				}
			}

			int line = 15;

			objective.getScore(" ").setScore(line--);
			
			objective.getScore(C.cYellow + C.Bold + "Players").setScore(line--);

			// Remove Old
			entry.getValue().resetScores(_oldPlayerCount + "/" + _oldMaxPlayerCount);
			// Set new
			objective.getScore(UtilServer.getPlayers().length + "/" + Manager.GetPlayerFull()).setScore(line--);

			if (Manager.GetGame() != null)
			{
				ChatColor teamColor = ChatColor.GRAY;
				String kitName = "None";

				if (Manager.GetGame().GetTeam(entry.getKey()) != null)
				{
					teamColor = Manager.GetGame().GetTeam(entry.getKey()).GetColor();
				}

				if (Manager.GetGame().GetKit(entry.getKey()) != null)
				{
					kitName = Manager.GetGame().GetKit(entry.getKey()).GetName() + "";
				}

				if (teamColor == null)

					//Shorten Kit Name
					if (kitName.length() > 16)
						kitName = kitName.substring(0, 16);

				// Remove old
				//entry.getValue().resetScores(teamColor + C.Bold +  "Kit");
				for(String string : entry.getValue().getEntries())
				{
					if(string.endsWith("Kit"))
					{
						entry.getValue().resetScores(string);
					}
				}
				entry.getValue().resetScores(_kitMap.get(entry.getKey()) + "");

				// Set new
				objective.getScore("    ").setScore(line--);
				objective.getScore(teamColor + C.Bold +  "Kit").setScore(line--);
				objective.getScore(kitName + "").setScore(line--);

				_kitMap.put(entry.getKey(), kitName + "");
			}

			objective.getScore("     ").setScore(line--);
			objective.getScore(C.cGreen + C.Bold +  "Gems").setScore(line--);

			// Remove old
			entry.getValue().resetScores(_gemMap.get(entry.getKey()) + "     ");
			// Set new
			objective.getScore(Manager.GetDonation().Get(entry.getKey().getName()).GetGems() + "     ").setScore(line--);

			_gemMap.put(entry.getKey(), Manager.GetDonation().Get(entry.getKey().getName()).GetGems());
			
			//Server
			objective.getScore("      ").setScore(line--);
			objective.getScore(C.cAqua + C.Bold + "Server").setScore(line--);
			objective.getScore(_serverName).setScore(line--);
			
			//ELO
			if (Manager.GetGame() != null && Manager.GetGame().EloRanking)
			{
				objective.getScore("      ").setScore(line--);
				objective.getScore(C.cPurple + C.Bold +  "Elo").setScore(line--);
	
				// Remove old
				entry.getValue().resetScores(_eloMap.get(entry.getKey()) + "      ");
				// Set new
				objective.getScore(Manager.getEloManager().getElo(entry.getKey().getUniqueId(), Manager.GetGame().GetName()) + "      ").setScore(line--);
	
			}
		}

		_oldPlayerCount = UtilServer.getPlayers().length;
		_oldMaxPlayerCount = Manager.GetPlayerFull();
	}

	private String GetKitCustomName(Player player, Game game, LobbyEnt ent)
	{
		CoreClient client = Manager.GetClients().Get(player);
		Donor donor = Manager.GetDonation().Get(player.getName());

		String entityName = ent.GetKit().GetName();

		if (!player.isOnline() || client == null || donor == null)
			return entityName;
		
		if (client.GetRank() == null)
		{
			System.out.println("client rank is null");
		}
		
		if (game == null)
		{
			System.out.println("game is null");
		}
		
		if (Manager == null)
		{
			System.out.println("Manager is null");
		}
		
		if (Manager.GetServerConfig() == null)
		{
			System.out.println("Manager.GetServerConfig() is null");
		}
		
		if (ent.GetKit().GetAvailability() == KitAvailability.Free || 										//Free
			Manager.hasKitsUnlocked(player) || 																	//YouTube
			(ent.GetKit().GetAvailability() == KitAvailability.Achievement && 
			Manager.GetAchievement().hasCategory(player, ent.GetKit().getAchievementRequirement())) ||		//Achievement
			donor.OwnsUnknownPackage(Manager.GetGame().GetName() + " " + ent.GetKit().GetName()) ||			//Green
			Manager.GetClients().Get(player).GetRank().Has(Rank.MAPDEV) ||									//STAFF
			donor.OwnsUnknownPackage(Manager.GetServerConfig().ServerType + " ULTRA") ||					//Single Ultra (Old)
			Manager.GetServerConfig().Tournament)															//Tournament
		{
			entityName = ent.GetKit().GetAvailability().GetColor() + entityName;
		}
		else if (ent.GetKit().GetAvailability() == KitAvailability.Achievement)
		{
			entityName = ChatColor.RED + C.Bold + entityName;
			entityName += ChatColor.RESET + " (" + C.cPurple + "Achievement Kit" + ChatColor.RESET + ")";
		}
		else
		{
			entityName = ChatColor.RED + C.Bold + entityName;
			entityName += ChatColor.RESET + " (" + C.cGreen + ent.GetKit().GetCost() + " Gems" + ChatColor.RESET + ")";
		}

		return entityName;
	}

	@EventHandler
	public void customEntityName(CustomTagEvent event)
	{
		// TODO: This needs to be changed when removing CustomTagFix

		if (Manager.GetGame() != null)
		{
			String customName = null;

			for (LobbyEnt ent : _kits.values())
			{
				if (ent.GetEnt().getEntityId() == event.getEntityId())
				{
					customName = GetKitCustomName(event.getPlayer(), Manager.GetGame(), ent);
					break;
				}
			}

			if (customName != null)
			{
				event.setCustomName(customName);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void handle(PacketInfo packetInfo)
	{
		// This only applies to 1.7 clients currently, 1.8 custom names are handled by customEntityName(CustomTagEvent event)
		if (_handlingPacket || UtilPlayer.is1_8(packetInfo.getPlayer()))
			return;

		Packet packet = packetInfo.getPacket();
		Player owner = packetInfo.getPlayer();
		PacketVerifier packetVerifier = packetInfo.getVerifier();

		int entityId = -1;

		if (packet instanceof PacketPlayOutEntityMetadata)
		{
			entityId = ((PacketPlayOutEntityMetadata)packet).a;
		}

		if (entityId != -1)
		{
			String customName = null;

			// Order important (_next and _prev overlap if games are same and will throw NPE on _game.GetName())
			for (LobbyEnt ent : _kits.values())
			{
				if (ent.GetEnt().getEntityId() == entityId && Manager.GetGame() != null)
				{
					customName = GetKitCustomName(owner, Manager.GetGame(), ent);
					break;
				}
			}

			if (customName != null)
			{
				try
				{
					if (packet instanceof PacketPlayOutEntityMetadata)
					{
						List<WatchableObject> watchables = new ArrayList<WatchableObject>();

						for (WatchableObject watchableObject : (List<WatchableObject>)((PacketPlayOutEntityMetadata) packet).b)
						{
							WatchableObject newWatch = new WatchableObject(watchableObject.c(), watchableObject.a(), watchableObject.b());

							if (newWatch.a() == 10)
							{
								newWatch.a(customName);
							}

							watchables.add(newWatch);
						}

						PacketPlayOutEntityMetadata newPacket = new PacketPlayOutEntityMetadata();
						newPacket.a = entityId;
						newPacket.b = watchables;

						_handlingPacket = true;
						packetVerifier.process(newPacket);
						_handlingPacket = false;

						packetInfo.setCancelled(true);
					}
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void AddPlayerToScoreboards(Player player, String teamName) 
	{		
		if (teamName == null)
			teamName = "";

		String rankName = Manager.GetClients().Get(player).GetRank().Name;
		boolean rankIsUltra = !Manager.GetClients().Get(player).GetRank().Has(Rank.ULTRA) && Manager.GetDonation().Get(player.getName()).OwnsUnknownPackage(Manager.GetServerConfig().ServerType + " ULTRA");
		
		if (rankIsUltra)
		{
			rankName = Rank.ULTRA.Name;
		}
		
		String rankTeamName = rankName + teamName;

		for (Scoreboard scoreboard : GetScoreboards())
		{
			try
			{
				scoreboard.getTeam(rankTeamName).addPlayer(player);
			}
			catch (Exception e)
			{
				//UHC adds people to teams earlier than usual, which can case this
				if (Manager.GetGame() instanceof UHC)
				{
					try
					{
						Manager.GetGame().GetScoreboard().GetScoreboard().getTeam(teamName).addPlayer(player);
						System.out.println("GameLobbyManager UHC Team Assignment Success");
						break;
					}
					catch(Exception f)
					{
						System.out.println("GameLobbyManager AddPlayerToScoreboard UHC Error");
						System.out.println("[" + teamName + "] adding [" + player.getName() + "]");
						System.out.println("Team is Null [" + (Manager.GetGame().GetScoreboard().GetScoreboard().getTeam(teamName) == null) + "]");
					}
				}
				else
				{
					System.out.println("GameLobbyManager AddPlayerToScoreboard Error");
					System.out.println("[" + rankTeamName + "] adding [" + player.getName() + "]");
					System.out.println("Team is Null [" + (scoreboard.getTeam(rankTeamName) == null) + "]");
				}
			}
		}
		
		
	}
	
	@EventHandler
	public void disallowInventoryClick(InventoryClickEvent event)
	{
		if (Manager.GetGame() == null)
			return;
		
		if (Manager.GetGame().GetState() != GameState.Recruit)
			return;
		
		if (event.getInventory().getType() == InventoryType.CRAFTING)
		{
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
		}
	}
	
	@EventHandler
	public void InventoryUpdate(UpdateEvent event)
	{
		if (!Manager.IsHotbarInventory())
			return;
		
		if (event.getType() != UpdateType.FAST)
			return; 
		
		if (Manager.GetGame() == null)
			return;
		 
		if (Manager.GetGame().GetState() != GameState.Recruit && Manager.GetGame().GadgetsDisabled)
			return;
				
		for (Player player : UtilServer.getPlayers())
		{
			if (player.getOpenInventory().getType() != InventoryType.CRAFTING)
				continue;
			
			//Cosmetic Menu
			Manager.getCosmeticManager().giveInterfaceItem(player);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void GemBoosterInteract(ActivateGemBoosterEvent event)
	{
		if (!Manager.IsHotbarInventory() || Manager.GetGame() == null || Manager.GetGame().GetState() != GameState.Recruit)
		{
			event.setCancelled(true);
			
			event.getPlayer().sendMessage(F.main("Arcade", "You can't use Gem Boosters right now."));
			
			return;
		}
		
		Manager.GetGame().AddGemBooster(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void explodeBlockBreakFix(EntityExplodeEvent event)
	{
		if (Manager.GetGame() == null)
			return;
		
		if (Manager.GetGame().GetState() == GameState.Live)
			return;
		
		event.blockList().clear();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void velocityEventCancel(PlayerVelocityEvent event)
	{
		if (Manager.GetGame() == null)
			return;
		
		if (Manager.GetGame().GetState() == GameState.Live)
			return;
		
		event.setCancelled(true);
	}

}
