package ehnetwork.game.arcade.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTabTitle;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeFormat;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.events.PlayerGameRespawnEvent;
import ehnetwork.game.arcade.events.PlayerStateChangeEvent;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.managers.GameLobbyManager;
import ehnetwork.game.arcade.scoreboard.GameScoreboard;
import ehnetwork.game.arcade.stats.AssistsStatTracker;
import ehnetwork.game.arcade.stats.DamageDealtStatTracker;
import ehnetwork.game.arcade.stats.DamageTakenStatTracker;
import ehnetwork.game.arcade.stats.DeathsStatTracker;
import ehnetwork.game.arcade.stats.ExperienceStatTracker;
import ehnetwork.game.arcade.stats.GamesPlayedStatTracker;
import ehnetwork.game.arcade.stats.KillsStatTracker;
import ehnetwork.game.arcade.stats.LoseStatTracker;
import ehnetwork.game.arcade.stats.StatTracker;
import ehnetwork.game.arcade.stats.TeamDeathsStatTracker;
import ehnetwork.game.arcade.stats.TeamKillsStatTracker;
import ehnetwork.game.arcade.stats.WinStatTracker;
import ehnetwork.game.arcade.world.WorldData;
import ehnetwork.minecraft.game.classcombat.event.ClassCombatCreatureAllowSpawnEvent;
import ehnetwork.minecraft.game.core.combat.DeathMessageType;


public abstract class Game implements Listener
{

	public long getGameLiveTime()
	{
		return _gameLiveTime;
	}

	public void setGameLiveTime(long gameLiveTime)
	{
		_gameLiveTime = gameLiveTime;
	}

	public enum GameState
	{
		Loading,
		Recruit,
		Prepare,
		Live,
		End,
		Dead
	}

	public ArcadeManager Manager;

	//Game
	private GameType _gameType;
	protected String[] _gameDesc;

	//Map
	private HashMap<GameType, ArrayList<String>> _files;

	//State 
	private GameState _gameState = GameState.Loading;
	private long _gameLiveTime;
	private long _gameStateTime = System.currentTimeMillis();

	private boolean _prepareCountdown = false;

	private int _countdown = -1;
	private boolean _countdownForce = false;

	private String _customWinLine = "";

	//Kits
	private Kit[] _kits;

	//Teams	
	protected ArrayList<GameTeam> _teamList = new ArrayList<GameTeam>();

	//Player Preferences
	protected NautHashMap<Player, Kit> _playerKit = new NautHashMap<Player, Kit>();
	private NautHashMap<GameTeam, ArrayList<Player>> _teamPreference = new NautHashMap<GameTeam, ArrayList<Player>>();

	//Player Data
	private NautHashMap<Player, HashMap<String, GemData>> _gemCount = new NautHashMap<Player, HashMap<String, GemData>>();
	private NautHashMap<Player, HashMap<String, Integer>> _stats = new NautHashMap<Player, HashMap<String, Integer>>();

	//Player Location Store
	private NautHashMap<String, Location> _playerLocationStore = new NautHashMap<String, Location>();

	//Scoreboard
	protected GameScoreboard Scoreboard;

	//Loaded from Map Config
	public ehnetwork.game.arcade.world.WorldData WorldData = null;

	//Game Help
	private long _helpTimer = 0;
	private int _helpIndex = 0;
	private ChatColor _helpColor = ChatColor.YELLOW;
	protected String[] _help;

	//Gameplay Flags
	public long GameTimeout = 1200000;

	public boolean Damage = true;
	public boolean DamagePvP = true;
	public boolean DamagePvE = true;
	public boolean DamageEvP = true;
	public boolean DamageSelf = true;
	public boolean DamageFall = true;
	public boolean DamageTeamSelf = false;
	public boolean DamageTeamOther = true;

	public boolean BlockBreak = false;
	public boolean BlockBreakCreative = false;
	public HashSet<Integer> BlockBreakAllow = new HashSet<Integer>();
	public HashSet<Integer> BlockBreakDeny = new HashSet<Integer>();

	public boolean BlockPlace = false;
	public boolean BlockPlaceCreative = false;
	public HashSet<Integer> BlockPlaceAllow = new HashSet<Integer>();
	public HashSet<Integer> BlockPlaceDeny = new HashSet<Integer>();

	public boolean ItemPickup = false;
	public HashSet<Integer> ItemPickupAllow = new HashSet<Integer>();
	public HashSet<Integer> ItemPickupDeny = new HashSet<Integer>();

	public boolean ItemDrop = false;
	public HashSet<Integer> ItemDropAllow = new HashSet<Integer>();
	public HashSet<Integer> ItemDropDeny = new HashSet<Integer>();

	public boolean InventoryOpenBlock = false;
	public boolean InventoryOpenChest = false;
	public boolean InventoryClick = false;

	public boolean PrivateBlocks = false;

	public boolean DeathOut = true;
	public boolean DeathDropItems = false;
	public boolean DeathMessages = true;
	public boolean AutomaticRespawn = true;

	public double DeathSpectateSecs = 0;

	public boolean QuitOut = true;
	public boolean QuitDropItems = false;
	
	public boolean IdleKickz = true;

	public boolean CreatureAllow = false;
	public boolean CreatureAllowOverride = false;

	public int WorldTimeSet = 12000;
	public boolean WorldWeatherEnabled = false;
	public int WorldWaterDamage = 0;
	public boolean WorldBoundaryKill = true;
	public boolean WorldBlockBurn = false;
	public boolean WorldFireSpread = false;
	public boolean WorldLeavesDecay = false;
	public boolean WorldSoilTrample = false;
	public boolean WorldBoneMeal = false;

	public int HungerSet = -1;
	public int HealthSet = -1;

	public boolean PrepareFreeze = true;

	private double _itemMergeRadius = 0;

	public boolean AnnounceStay = true;
	public boolean AnnounceJoinQuit = true;
	public boolean AnnounceSilence = true;

	public boolean DisplayLobbySide = true;

	public GameState KitRegisterState = GameState.Live;

	public boolean JoinInProgress = false;
	
	public int TickPerTeleport = 1;
	
	public int FillTeamsInOrderToCount = -1;
	
	public boolean SpawnNearAllies = false;
	public boolean SpawnNearEnemies = false;
	
	public boolean StrictAntiHack = false;
	
	public boolean DisableKillCommand = true;
	
	public boolean GadgetsDisabled = true;
	
	public boolean TeleportsDisqualify = true;
	
	public boolean DontAllowOverfill = false;

	//Addons
	public boolean CompassEnabled = false;
	public boolean CompassGiveItem = true;

	public boolean SoupEnabled = true;
	public boolean TeamArmor = false;
	public boolean TeamArmorHotbar = false;					

	public boolean GiveClock = true;						
	
	public boolean AllowParticles = true;					

	public double GemMultiplier = 1;
	public boolean GemHunterEnabled = true;
	public boolean GemBoosterEnabled = true;
	public boolean GemDoubleEnabled = true;
	
	public long PrepareTime = 9000;
	public boolean PlaySoundGameStart = true;
	
	//Gameplay Data
	public HashMap<Location, Player> PrivateBlockMap = new HashMap<Location, Player>();
	public HashMap<String, Integer> PrivateBlockCount = new HashMap<String, Integer>();

	public Location SpectatorSpawn = null;

	public boolean FirstKill = true;

	public String Winner = "Nobody";
	public GameTeam WinnerTeam = null;

	public boolean EloRanking = false;
	public int EloStart = 1000;

	public boolean CanAddStats = true;
	public boolean CanGiveLoot = true;
	
	public boolean HideTeamSheep = false;
	public boolean ReplaceTeamsWithKits = false;
	
	public boolean VersionRequire1_8 = false;
	
	public ArrayList<String> GemBoosters = new ArrayList<String>();
	private final Set<StatTracker<? extends Game>> _statTrackers = new HashSet<>();

	public Game(ArcadeManager manager, GameType gameType, Kit[] kits, String[] gameDesc)
	{
		Manager = manager;

		//Player List
		UtilTabTitle.broadcastHeaderAndFooter(C.cGold + C.Bold + gameType.GetName(), "Visit " + C.cGreen + "theendlessweb.com" + ChatColor.RESET + " for News, Forums and Shop");
		
		//Game
		_gameType = gameType;
		_gameDesc = gameDesc;

		//Kits
		_kits = kits;

		//Scoreboard
		Scoreboard = new GameScoreboard(this);

		//Map Select
		_files = new HashMap<GameType, ArrayList<String>>();
		for(GameType type : GetWorldHostNames())
		{
			_files.put(type, Manager.LoadFiles(type.GetName()));
		}
		if (Manager.GetGameCreationManager().MapPref != null)
		{
			HashMap<GameType, ArrayList<String>> matches = new HashMap<GameType, ArrayList<String>>();
			for (GameType game : _files.keySet())
			{
				ArrayList<String> list = new ArrayList<String>();
				for(String cur : _files.get(game))
				{
					if (cur.toLowerCase().contains(Manager.GetGameCreationManager().MapPref.toLowerCase()))
					{
						if(game.toString().toLowerCase().contains(Manager.GetGameCreationManager().MapSource.toLowerCase()))
						{
							list.add(cur);
							System.out.print("Map Preference: " + cur);
							matches.put(game, list);
						}
					}
				}
			}

			if (matches.size() > 0)
				_files = matches;

			Manager.GetGameCreationManager().MapPref = null;
			Manager.GetGameCreationManager().MapSource = null;
		}
		WorldData = new WorldData(this);

		//Stat Trackers
		registerStatTrackers(
				new KillsStatTracker(this),
				new DeathsStatTracker(this),
				new AssistsStatTracker(this),
				new ExperienceStatTracker(this),
				new WinStatTracker(this),
				new LoseStatTracker(this),
				new DamageDealtStatTracker(this),
				new DamageTakenStatTracker(this),
				new GamesPlayedStatTracker(this)
		);
		
		if (gameType != GameType.UHC)
		{
			registerStatTrackers(
					new TeamDeathsStatTracker(this),
					new TeamKillsStatTracker(this)
					);
		}
		
		Manager.setResourcePack(gameType.getResourcePackUrl(), gameType.isEnforceResourcePack());

		System.out.println("Loading " + GetName() + "...");
	}

	public void setKits(Kit[] kits)
	{
		_kits = kits;
	}

	public HashMap<GameType, ArrayList<String>> GetFiles()
	{
		return _files;
	}

	public String GetName()
	{
		return _gameType.GetName();
	}
	
	public GameType[] GetWorldHostNames()
	{
		GameType[] mapSource = new GameType[]{GetType()};
		if(GetType().getMapSource() != null)
		{
			if(GetType().ownMaps())
			{
				int i = 1;
				mapSource = new GameType[GetType().getMapSource().length + 1];
				for(GameType type : GetType().getMapSource())
				{
					mapSource[i] = type;
					i++;
				}
				mapSource[0] = GetType();
			} 
			else
			{
				mapSource = GetType().getMapSource();
			}
		}
		return mapSource;
	}
	
	public String GetGameNamebyMap(String game, String map)
	{
		for(GameType type : _files.keySet())
		{
			if(type.GetName().toLowerCase().contains(game.toLowerCase()))
			{
				for(String string : _files.get(type))
				{
					if(string.toLowerCase().contains(map.toLowerCase()))
					{
						return type.GetName();
					}
				}
			}
		}
		return null;
	}
	
	public GameType GetGameByMapList(ArrayList<String> maps) 
	{
		for(GameType game : _files.keySet())
		{
			if(maps.equals(_files.get(game)))
			{
				return game;
			}
		}
		return null;
	}

	public String GetMode()
	{
		return null;
	}

	public GameType GetType()
	{
		return _gameType;
	}

	public String[] GetDesc()
	{
		return _gameDesc;
	}

	public void SetCustomWinLine(String line)
	{
		_customWinLine = line;
	}

	public GameScoreboard GetScoreboard()
	{
		return Scoreboard;
	}

	private Objective GetObjectiveSide()
	{
		return Scoreboard.GetObjectiveSide();
	}

	public ArrayList<GameTeam> GetTeamList()
	{
		return _teamList;
	}

	public int GetCountdown()
	{
		return _countdown;
	}

	public void SetCountdown(int time)
	{
		_countdown = time;
	}

	public boolean GetCountdownForce()
	{
		return _countdownForce;
	}

	public void SetCountdownForce(boolean value)
	{
		_countdownForce = value;
	}

	public NautHashMap<GameTeam, ArrayList<Player>> GetTeamPreferences()
	{
		return _teamPreference;
	}

	public NautHashMap<Player, Kit> GetPlayerKits()
	{
		return _playerKit;
	}

	public NautHashMap<Player, HashMap<String, GemData>> GetPlayerGems()
	{
		return _gemCount;
	}

	public NautHashMap<String, Location> GetLocationStore()
	{
		return _playerLocationStore;
	}

	public GameState GetState()
	{
		return _gameState;
	}

	public void SetState(GameState state)
	{
		_gameState = state;
		_gameStateTime = System.currentTimeMillis();

		if (_gameState == GameState.Live)
			setGameLiveTime(_gameStateTime);

		for (Player player : UtilServer.getPlayers())
			player.leaveVehicle();

		//Event
		GameStateChangeEvent stateEvent = new GameStateChangeEvent(this, state);
		UtilServer.getServer().getPluginManager().callEvent(stateEvent);

		System.out.println(GetName() + " state set to " + state.toString());
	}

	public void SetStateTime(long time)
	{
		_gameStateTime = time;
	}

	public long GetStateTime()
	{
		return _gameStateTime;
	}

	public boolean InProgress()
	{
		return GetState() == GameState.Prepare || GetState() == GameState.Live;
	}

	public boolean IsLive()
	{
		return _gameState == GameState.Live;
	}

	public void AddTeam(GameTeam team)
	{
		//Add
		GetTeamList().add(team);

		System.out.println("Created Team: " + team.GetName());
	}

	public void RemoveTeam(GameTeam team)
	{
		if (GetTeamList().remove(team))
			System.out.println("Deleted Team: " + team.GetName());
	}

	public boolean HasTeam(GameTeam team)
	{
		for (GameTeam cur : GetTeamList())
			if (cur.equals(team))
				return true;

		return false;
	}

	public void RestrictKits()
	{
		//Null Default
	}

	public void RegisterKits()
	{
		for (Kit kit : _kits)
		{
			UtilServer.getServer().getPluginManager().registerEvents(kit, Manager.getPlugin());

			for (Perk perk : kit.GetPerks())
			{
				UtilServer.getServer().getPluginManager()
						.registerEvents(perk, Manager.getPlugin());
				perk.registeredEvents();
			}
		}
	}

	public void DeregisterKits()
	{
		for (Kit kit : _kits)
		{
			HandlerList.unregisterAll(kit);

			for (Perk perk : kit.GetPerks())
				HandlerList.unregisterAll(perk);
		}
	}

	public void ParseData()
	{
		//Nothing by default,
		//Use this to parse in extra location data from maps
	}

	public void SetPlayerTeam(Player player, GameTeam team, boolean in)
	{
		//Clean Old Team
		GameTeam pastTeam = this.GetTeam(player);
		if (pastTeam != null)
		{
			pastTeam.RemovePlayer(player);
		}

		team.AddPlayer(player, in);

		//Ensure Valid Kit
		ValidateKit(player, team);

		//Game Scoreboard
		Scoreboard.SetPlayerTeam(player, team.GetName().toUpperCase());

		//Lobby Scoreboard
		Manager.GetLobby().AddPlayerToScoreboards(player, team.GetName().toUpperCase());

		//Save Tournament Team
		Manager.GetGameTournamentManager().setTournamentTeam(player, team);
	}

	public GameTeam ChooseTeam(Player player)
	{
		if (FillTeamsInOrderToCount != -1)
		{
			for (int i = 0; i < _teamList.size(); i++)
			{
				if (_teamList.get(i).GetSize() < FillTeamsInOrderToCount)
				{
					return _teamList.get(i);
				}
			}
		}
		
		GameTeam team = null;

		//Random Team
		for (int i = 0; i < _teamList.size(); i++)
		{
			if (team == null || _teamList.get(i).GetSize() < team.GetSize())
			{
				team = _teamList.get(i);
			}
		}

		return team;
	}

	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		if (!DeathOut)
		{
			return 0.5;
		}

		if (!assist)
		{
			return 4;
		}
		else
		{
			return 1;
		}
	}

	public HashMap<String, GemData> GetGems(Player player)
	{
		if (!_gemCount.containsKey(player))
			_gemCount.put(player, new HashMap<String, GemData>());

		return _gemCount.get(player);
	}

	public void AddGems(Player player, double gems, String reason, boolean countAmount, boolean multipleAllowed)
	{
		if (!countAmount && gems < 1)
			gems = 1;

		if (GetGems(player).containsKey(reason) && multipleAllowed)
		{
			GetGems(player).get(reason).AddGems(gems);
		}
		else
		{
			GetGems(player).put(reason, new GemData(gems, countAmount));
		}
		if (this.getArcadeManager().GetBrandListener().playerUsingClient(player)){
			GetGems(player).put("Using EHNetwork Client", new GemData(75, false));
		}
	}

	public void ValidateKit(Player player, GameTeam team)
	{
		//Kit
		if (GetKit(player) == null || !team.KitAllowed(GetKit(player)))
		{
			for (Kit kit : _kits)
			{
				if (kit.GetAvailability() == KitAvailability.Hide ||
						kit.GetAvailability() == KitAvailability.Null)
					continue;

				if (team.KitAllowed(kit))
				{
					SetKit(player, kit, false);
					break;
				}
			}
		}
	}

	public void SetKit(Player player, Kit kit, boolean announce)
	{
		GameTeam team = GetTeam(player);
		if (team != null)
		{
			if (!team.KitAllowed(kit))
			{
				player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 0.5f);
				UtilPlayer.message(player, F.main("Kit", F.elem(team.GetFormattedName()) + " cannot use " + F.elem(kit.GetFormattedName() + " Kit") + "."));
				return;
			}
		}

		if (_playerKit.get(player) != null)
		{
			_playerKit.get(player).Deselected(player);
		}

		_playerKit.put(player, kit);

		kit.Selected(player);

		if (announce)
		{
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2f, 1f);
			UtilPlayer.message(player, F.main("Kit", "You equipped " + F.elem(kit.GetFormattedName() + " Kit") + "."));
		}

		if (InProgress())
			kit.ApplyKit(player);
	}

	public Kit GetKit(Player player)
	{
		return _playerKit.get(player);
	}

	public Kit[] GetKits()
	{
		return _kits;
	}

	public boolean HasKit(Kit kit)
	{
		for (Kit cur : GetKits())
			if (cur.equals(kit))
				return true;

		return false;
	}

	public boolean HasKit(Player player, Kit kit)
	{
		if (!IsAlive(player))
			return false;

		if (GetKit(player) == null)
			return false;

		return GetKit(player).equals(kit);
	}

	public boolean SetPlayerState(Player player, GameTeam.PlayerState state)
	{
		GetScoreboard().ResetScore(player.getName());

		GameTeam team = GetTeam(player);

		if (team == null)
			return false;

		team.SetPlayerState(player, state);

		//Event
		PlayerStateChangeEvent playerStateEvent = new PlayerStateChangeEvent(this, player, GameTeam.PlayerState.OUT);
		UtilServer.getServer().getPluginManager().callEvent(playerStateEvent);

		return true;
	}

	public abstract void EndCheck();

	public void RespawnPlayer(final Player player)
	{
		player.eject();
		player.teleport(GetTeam(player).GetSpawn());

		Manager.Clear(player);

		//Event
		PlayerGameRespawnEvent event = new PlayerGameRespawnEvent(this, player);
		UtilServer.getServer().getPluginManager().callEvent(event);

		//Re-Give Kit
		Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				GetKit(player).ApplyKit(player);
			}
		}, 0);
	}

	public boolean IsPlaying(Player player)
	{
		return GetTeam(player) != null;
	}

	public boolean IsAlive(Entity entity)
	{
		if (entity instanceof Player)
		{
			Player player = (Player)entity;
			
			GameTeam team = GetTeam(player);

			if (team == null)
				return false;

			return team.IsAlive(player);
		}
		
		return false;
	}

	public ArrayList<Player> GetPlayers(boolean aliveOnly)
	{
		ArrayList<Player> players = new ArrayList<Player>();

		for (GameTeam team : _teamList)
			players.addAll(team.GetPlayers(aliveOnly));

		return players;
	}

	public GameTeam GetTeam(String player, boolean aliveOnly)
	{
		for (GameTeam team : _teamList)
			if (team.HasPlayer(player, aliveOnly))
				return team;

		return null;
	}

	public GameTeam GetTeam(Player player)
	{
		if (player == null)
			return null;

		for (GameTeam team : _teamList)
			if (team.HasPlayer(player))
				return team;

		return null;
	}

	public GameTeam GetTeam(ChatColor color)
	{
		for (GameTeam team : _teamList)
			if (team.GetColor() == color)
				return team;

		return null;
	}

	public Location GetSpectatorLocation()
	{
		if (SpectatorSpawn != null)
			return SpectatorSpawn;

		Vector vec = new Vector(0, 0, 0);
		double count = 0;

		for (GameTeam team : this.GetTeamList())
		{
			for (Location spawn : team.GetSpawns())
			{
				count++;
				vec.add(spawn.toVector());
			}
		}

		SpectatorSpawn = new Location(this.WorldData.World, 0, 0, 0);

		vec.multiply(1d / count);

		SpectatorSpawn.setX(vec.getX());
		SpectatorSpawn.setY(vec.getY());
		SpectatorSpawn.setZ(vec.getZ());

		//Move Up - Out Of Blocks
		while (!UtilBlock.airFoliage(SpectatorSpawn.getBlock()) || !UtilBlock.airFoliage(SpectatorSpawn.getBlock().getRelative(BlockFace.UP)))
		{
			SpectatorSpawn.add(0, 1, 0);
		}

		int Up = 0;

		//Move Up - Through Air
		for (int i = 0; i < 15; i++)
		{
			if (UtilBlock.airFoliage(SpectatorSpawn.getBlock().getRelative(BlockFace.UP)))
			{
				SpectatorSpawn.add(0, 1, 0);
				Up++;
			}
			else
			{
				break;
			}
		}

		//Move Down - Out Of Blocks
		while (Up > 0 && !UtilBlock.airFoliage(SpectatorSpawn.getBlock()) || !UtilBlock.airFoliage(SpectatorSpawn.getBlock().getRelative(BlockFace.UP)))
		{
			SpectatorSpawn.subtract(0, 1, 0);
			Up--;
		}

		SpectatorSpawn = SpectatorSpawn.getBlock().getLocation().add(0.5, 0.1, 0.5);

		while (SpectatorSpawn.getBlock().getTypeId() != 0 || SpectatorSpawn.getBlock().getRelative(BlockFace.UP).getTypeId() != 0)
			SpectatorSpawn.add(0, 1, 0);

		return SpectatorSpawn;
	}

	@EventHandler
	public void eloStart(PlayerLoginEvent event)
	{
		if (EloRanking)
		{
			if (Manager.getEloManager().getElo(event.getPlayer().getUniqueId(), GetName()) == -1)
			{
				Manager.getEloManager().saveElo(event.getPlayer().getUniqueId(), GetName(), EloStart);
			}
		}
	}

	@EventHandler
	public abstract void ScoreboardUpdate(UpdateEvent event);

	public DeathMessageType GetDeathMessageType()
	{
		if (!DeathMessages)
			return DeathMessageType.None;

		if (this.DeathOut)
			return DeathMessageType.Detailed;

		return DeathMessageType.Simple;
	}

	public boolean CanJoinTeam(GameTeam team)
	{
		return Manager.IsTeamBalance() ? team.GetSize() < Math.max(1, UtilServer.getPlayers().length / GetTeamList().size()) : true;
	}
	
	@EventHandler
	public final void onFoodLevelChangeEvent(FoodLevelChangeEvent event)
	{
	    ((Player) event.getEntity()).setSaturation(3.8F); // While not entirely accurate, this is a pretty good guess at original food level changes
	}

	public GameTeam GetTeamPreference(Player player)
	{
		for (GameTeam team : _teamPreference.keySet())
		{
			if (_teamPreference.get(team).contains(player))
				return team;
		}

		return null;
	}

	public void RemoveTeamPreference(Player player)
	{
		for (ArrayList<Player> queue : _teamPreference.values())
			queue.remove(player);
	}

	public String GetTeamQueuePosition(Player player)
	{
		for (ArrayList<Player> queue : _teamPreference.values())
		{
			for (int i = 0; i < queue.size(); i++)
			{
				if (queue.get(i).equals(player))
					return (i + 1) + "/" + queue.size();
			}
		}

		return "Unknown";
	}

	public void InformQueuePositions()
	{
		for (GameTeam team : _teamPreference.keySet())
		{
			for (Player player : _teamPreference.get(team))
			{
				UtilPlayer.message(player, F.main("Team", "You are " + F.elem(GetTeamQueuePosition(player)) + " in queue for " + F.elem(team.GetFormattedName() + " Team") + "."));
			}
		}
	}

	public void AnnounceGame()
	{
		for (Player player : UtilServer.getPlayers())
			AnnounceGame(player);

		if (AnnounceSilence)
			Manager.GetChat().Silence(PrepareTime, false);
	}

	public void AnnounceGame(Player player)
	{
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

		for (int i = 0; i < 6 - GetDesc().length; i++)
			UtilPlayer.message(player, "");

		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, C.cGreen + "Game - " + C.cYellow + C.Bold + this.GetName());
		UtilPlayer.message(player, "");

		for (String line : this.GetDesc())
		{
			UtilPlayer.message(player, C.cWhite + "  " + line);
		}

		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cGreen + "Map - " + C.cYellow + C.Bold + WorldData.MapName + ChatColor.RESET + C.cGray + " created by " + C.cYellow + C.Bold + WorldData.MapAuthor);

		UtilPlayer.message(player, ArcadeFormat.Line);
	}

	public void AnnounceEnd(GameTeam team)
	{
		if (!IsLive())
			return;

		String winnerText = ChatColor.WHITE + "Nobody";
		ChatColor subColor = ChatColor.WHITE;
		
		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

			UtilPlayer.message(player, "");
			UtilPlayer.message(player, ArcadeFormat.Line);

			UtilPlayer.message(player, "§aGame - §f§l" + this.GetName());
			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "");

			if (team != null)
			{
				WinnerTeam = team;
				Winner = team.GetName() + " Team";

				winnerText = team.GetColor() +  team.GetName();
				subColor = team.GetColor();
				
				UtilPlayer.message(player, team.GetColor() + C.Bold + team.GetName() + " won the game!");
			}
			else
			{
				UtilPlayer.message(player, "Nobody won the game!");
			}


			UtilPlayer.message(player, _customWinLine);
			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "§aMap - §f§l" + WorldData.MapName + C.cGray + " created by " + "§f§l" + WorldData.MapAuthor);

			UtilPlayer.message(player, ArcadeFormat.Line);
		}

		UtilTextMiddle.display(winnerText, subColor + "won the game", 20, 120, 20);

		if (AnnounceSilence)
			Manager.GetChat().Silence(5000, false);
	}

	public void AnnounceEnd(List<Player> places)
	{
		String winnerText = ChatColor.WHITE + "§lNobody won the game...";
		ChatColor subColor = ChatColor.WHITE;

		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

			UtilPlayer.message(player, "");
			UtilPlayer.message(player, ArcadeFormat.Line);

			UtilPlayer.message(player, "§aGame - §f§l" + this.GetName());
			UtilPlayer.message(player, "");

			if (places == null || places.isEmpty())
			{
				UtilPlayer.message(player, "");
				UtilPlayer.message(player, ChatColor.WHITE + "§lNobody won the game...");
				UtilPlayer.message(player, "");
			}
			else
			{
				if (places.size() >= 1)
				{
					Winner = places.get(0).getName();

					winnerText = C.cYellow + places.get(0).getName();
					subColor = ChatColor.YELLOW;

					UtilPlayer.message(player, C.cRed + C.Bold + "1st Place" + C.cWhite + " - " + places.get(0).getName());
				}


				if (places.size() >= 2)
				{
					UtilPlayer.message(player, C.cGold + C.Bold + "2nd Place" + C.cWhite + " - " + places.get(1).getName());
				}


				if (places.size() >= 3)
				{
					UtilPlayer.message(player, C.cYellow + C.Bold + "3rd Place" + C.cWhite + " - " + places.get(2).getName());
				}
			}

			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "§aMap - §f§l" + WorldData.MapName + C.cGray + " created by " + "§f§l" + WorldData.MapAuthor);

			UtilPlayer.message(player, ArcadeFormat.Line);
		}

		UtilTextMiddle.display(winnerText, subColor + "won the game", 20, 120, 20);

		if (AnnounceSilence)
			Manager.GetChat().Silence(5000, false);
	}


	public void Announce(String message)
	{
		if (message == null)
			return;
		
		Announce(message, true);
	}

	public void Announce(String message, boolean playSound)
	{
		for (Player player : UtilServer.getPlayers())
		{
			if (playSound)
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);

			UtilPlayer.message(player, message);
		}

		System.out.println("[Announcement] " + message);
	}

	public boolean AdvertiseText(GameLobbyManager gameLobbyManager, int _advertiseStage)
	{
		return false;
	}

	public boolean CanThrowTNT(Location location)
	{
		return true;
	}

	@EventHandler
	public void HelpUpdate(UpdateEvent event)
	{
		if (_help == null || _help.length == 0)
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		if (this.GetState() != GameState.Recruit)
			return;

		if (!UtilTime.elapsed(_helpTimer, 8000))
			return;

		if (_helpColor == ChatColor.YELLOW)
			_helpColor = ChatColor.GOLD;
		else
			_helpColor = ChatColor.YELLOW;

		_helpTimer = System.currentTimeMillis();

		String msg = C.cWhite + C.Bold + "TIP> " + ChatColor.RESET + _helpColor + _help[_helpIndex];

		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);

			UtilPlayer.message(player, msg);
		}

		_helpIndex = (_helpIndex + 1) % _help.length;
	}

	public void StartPrepareCountdown()
	{
		_prepareCountdown = true;
	}

	public boolean CanStartPrepareCountdown()
	{
		return _prepareCountdown;
	}

	@EventHandler
	public void TeamPlayerPlacement(PlayerStateChangeEvent event)
	{
		GameTeam team = GetTeam(event.GetPlayer());

		if (team != null)
			team.SetPlacement(event.GetPlayer(), event.GetState());
	}

	public void HandleTimeout()
	{
		SetState(GameState.End);
	}

	public void AddGemBooster(Player player)
	{
		if (!GemBoosterEnabled)
		{
			UtilPlayer.message(player, F.main("Game", "You cannot use " + F.elem("Gem Boosters")) + " for this game.");
			return;
		}
		
		if (GemBoosters.size() >= 4)
		{
			UtilPlayer.message(player, F.main("Game", "Games cannot have more than " + F.elem("4 Gem Boosters")) + ".");
			return;
		}

		if (GemBoosters.contains(player.getName()))
		{
			UtilPlayer.message(player, F.main("Game", "You can only use " + F.elem("1 Gem Booster")) + " per game.");
			return;
		}

		Announce(F.elem(player.getName()) + " used a " + F.elem(C.cGreen + "Gem Booster") + " for " + F.elem("+" + (100 - (GemBoosters.size() * 25)) + "% Gems") + "!");

		GemBoosters.add(player.getName());
	}

	public double GetGemBoostAmount()
	{
		if (GemBoosters.size() == 1) return 1;
		if (GemBoosters.size() == 2) return 1.75;
		if (GemBoosters.size() == 3) return 2.25;
		if (GemBoosters.size() == 4) return 2.5;

		return 0;
	}

	public void AddStat(Player player, String stat, int amount, boolean limitTo1, boolean global)
	{
		if (!Manager.IsRewardStats())
			return;

		if (!_stats.containsKey(player))
			_stats.put(player, new HashMap<String, Integer>());

		if (global)
			stat = "Global." + stat;
		else
			stat = GetName() + "." + stat;

		if (Manager.IsTournamentServer())
			stat += ".Tournament";

		int past = 0;
		if (_stats.get(player).containsKey(stat))
			past = _stats.get(player).get(stat);

		_stats.get(player).put(stat, limitTo1 ? Math.min(1, past + amount) : past + amount);
	}

	public abstract List<Player> getWinners();

	public abstract List<Player> getLosers();

	public NautHashMap<Player, HashMap<String, Integer>> GetStats()
	{
		return _stats;
	}

	public void registerStatTrackers(StatTracker<? extends Game>... statTrackers)
	{
		for (StatTracker<? extends Game> tracker : statTrackers)
		{
			if (_statTrackers.add(tracker))
				Bukkit.getPluginManager().registerEvents(tracker, Manager.getPlugin());
		}
	}

	

	public Collection<StatTracker<? extends Game>> getStatTrackers()
	{
		return _statTrackers;
	}

	@EventHandler
	public void onHangingBreak(HangingBreakEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void onHangingPlace(HangingPlaceEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamageHanging(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Hanging)
		{
			event.setCancelled(true);
		}
	}

	public void deRegisterStats()
	{
		for (StatTracker<? extends Game> tracker : _statTrackers)
			HandlerList.unregisterAll(tracker);

		_statTrackers.clear();
	}

	public ArcadeManager getArcadeManager()
	{
		return Manager;
	}
	
	@EventHandler
	public void classCombatCreatureAllow(ClassCombatCreatureAllowSpawnEvent event)
	{
		CreatureAllowOverride = event.getAllowed();
	}

	public boolean isInsideMap(Player player)
	{
		return isInsideMap(player.getLocation());
	}

	public boolean isInsideMap(Location loc)
	{
		return !(
			loc.getX() >= WorldData.MaxX + 1 ||
			loc.getX() <= WorldData.MinX ||
			loc.getZ() >= WorldData.MaxZ + 1 ||
			loc.getZ() <= WorldData.MinZ ||
			loc.getY() >= WorldData.MaxY + 1 ||
			loc.getY() <= WorldData.MinY);
	}

	public void setItemMerge(boolean itemMerge)
	{
		setItemMergeRadius(itemMerge ? 3.5 : 0);
	}

	public void setItemMergeRadius(double mergeRadius)
	{
		_itemMergeRadius = mergeRadius;

		if (WorldData.World != null)
		{
			((CraftWorld) WorldData.World).getHandle().spigotConfig.itemMerge = _itemMergeRadius;
		}
	}

	public double getItemMergeRadius()
	{
		return _itemMergeRadius;
	}

	@EventHandler
	public void applyItemMerge(WorldLoadEvent event)
	{
		if (event.getWorld().getName().equals(WorldData.GetFolder()))
		{
			System.out.println("Setting item merge radius for game to " + _itemMergeRadius);
			((CraftWorld) event.getWorld()).getHandle().spigotConfig.itemMerge = _itemMergeRadius;
		}
	}

	public void setGame(GameType gameType, Player caller, boolean inform)
	{
		Manager.GetGameCreationManager().SetNextGameType(gameType);

		//End Current
		if (GetState() == GameState.Recruit)
		{
			SetState(GameState.Dead);

			if (inform)
				Announce(C.cAqua + C.Bold + caller.getName() + " has changed game to " + gameType.GetName() + ".");
		}
		else
		{
			if (inform)
				Announce(C.cAqua + C.Bold + caller.getName() + " set next game to " + gameType.GetName() + ".");
		}
	}
	
	public void endGame(GameTeam winningTeam) 
	{
	    AnnounceEnd(winningTeam);

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
