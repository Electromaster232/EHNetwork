package nautilus.game.arcade;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import net.minecraft.server.v1_7_R4.BiomeBase;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import mineplex.core.CustomTagFix;
import mineplex.core.TablistFix;
import mineplex.core.account.CoreClientManager;
import mineplex.core.account.event.BrandListener;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.antihack.AntiHack;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.blood.Blood;
import mineplex.core.chat.Chat;
import mineplex.core.command.CommandCenter;
import mineplex.core.common.util.FileUtil;
import mineplex.core.common.util.UtilServer;
import mineplex.core.cosmetic.CosmeticManager;
import mineplex.core.creature.Creature;
import mineplex.core.disguise.DisguiseManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.friend.FriendManager;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.give.Give;
import mineplex.core.hologram.HologramManager;
import mineplex.core.ignore.IgnoreManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.leaderboard.LeaderboardManager;
import mineplex.core.memory.MemoryFix;
import mineplex.core.message.MessageManager;
import mineplex.core.monitor.LagMeter;
import mineplex.core.mount.MountManager;
import mineplex.core.npc.NpcManager;
import mineplex.core.packethandler.PacketHandler;
import mineplex.core.pet.PetManager;
import mineplex.core.portal.Portal;
import mineplex.core.preferences.PreferencesManager;
import mineplex.core.projectile.ProjectileManager;
import mineplex.core.punish.Punish;
import mineplex.core.recharge.Recharge;
import mineplex.core.serverConfig.ServerConfiguration;
import mineplex.core.stats.StatsManager;
import mineplex.core.status.ServerStatusManager;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.FileUpdater;
import mineplex.core.updater.Updater;
import mineplex.core.visibility.VisibilityManager;
import mineplex.minecraft.game.core.combat.CombatManager;
import mineplex.minecraft.game.core.damage.DamageManager;
import nautilus.game.arcade.command.VanishCommand;
import nautilus.game.arcade.game.GameServerConfig;

public class Arcade extends JavaPlugin
{      
	private String WEB_CONFIG = "webServer";

	//Modules   
	private CoreClientManager _clientManager;
	private DonationManager _donationManager; 
	private DamageManager _damageManager;

	private BrandListener _brandListener;

	private ArcadeManager _gameManager;
	 
	private ServerConfiguration _serverConfiguration;

	@Override     
	public void onEnable() 
	{
		//Delete Old Games Folders
		DeleteFolders();
 
		//Configs
		getConfig().addDefault(WEB_CONFIG, "http://mplex.endlcdn.site/accounts/");
		getConfig().set(WEB_CONFIG, getConfig().getString(WEB_CONFIG));
		saveConfig();
		
		String webServerAddress = getConfig().getString(WEB_CONFIG);

		//Logger.initialize(this);

		//Static Modules
		CommandCenter.Initialize(this);		
		_clientManager = new CoreClientManager(this, webServerAddress);
		CommandCenter.Instance.setClientManager(_clientManager);

		ItemStackFactory.Initialize(this, false);  
		Recharge.Initialize(this);   
		VisibilityManager.Initialize(this);
		Give.Initialize(this);

		_donationManager = new DonationManager(this, _clientManager, webServerAddress);

		_serverConfiguration = new ServerConfiguration(this, _clientManager);

		_brandListener = new BrandListener();
		
		PreferencesManager preferenceManager = new PreferencesManager(this, _clientManager, _donationManager);

		Creature creature = new Creature(this);
		ServerStatusManager serverStatusManager = new ServerStatusManager(this, _clientManager, new LagMeter(this, _clientManager));
		LeaderboardManager leaderboardManager = new LeaderboardManager(this, _clientManager);
		Teleport teleport = new Teleport(this);		
		Portal portal = new Portal(this, _clientManager, serverStatusManager.getCurrentServerName());
		new FileUpdater(this, portal, serverStatusManager.getCurrentServerName(), serverStatusManager.getRegion());
		PacketHandler packetHandler = new PacketHandler(this);
		
		DisguiseManager disguiseManager = new DisguiseManager(this, packetHandler);

		_damageManager = new DamageManager(this, new CombatManager(this), new NpcManager(this, creature), disguiseManager, null);

		Punish punish = new Punish(this, webServerAddress, _clientManager);
		AntiHack.Initialize(this, punish, portal, preferenceManager, _clientManager);
		AntiHack.Instance.setKick(false);
		
        IgnoreManager ignoreManager = new IgnoreManager(this, _clientManager, preferenceManager, portal);
		StatsManager statsManager = new StatsManager(this, _clientManager);
		AchievementManager achievementManager = new AchievementManager(statsManager, _clientManager, _donationManager);
        FriendManager friendManager = new FriendManager(this, _clientManager, preferenceManager, portal);
        Chat chat = new Chat(this, _clientManager, preferenceManager, achievementManager, serverStatusManager.getCurrentServerName());
        new MessageManager(this, _clientManager, preferenceManager, ignoreManager, punish, friendManager, chat);
		
		BlockRestore blockRestore = new BlockRestore(this);
		
		ProjectileManager projectileManager = new ProjectileManager(this);
		HologramManager hologramManager = new HologramManager(this);
		
		//Inventory
		InventoryManager inventoryManager = new InventoryManager(this, _clientManager);
		PetManager petManager = new PetManager(this, _clientManager, _donationManager, disguiseManager, creature, blockRestore, webServerAddress);
		MountManager mountManager = new MountManager(this, _clientManager, _donationManager, blockRestore, disguiseManager);
		GadgetManager gadgetManager = new GadgetManager(this, _clientManager, _donationManager, inventoryManager, mountManager, petManager, preferenceManager, disguiseManager, blockRestore, projectileManager);
		CosmeticManager cosmeticManager = new CosmeticManager(this, _clientManager, _donationManager, inventoryManager, gadgetManager, mountManager, petManager, null);
		cosmeticManager.setInterfaceSlot(7);
		
		//Arcade Manager  
		_gameManager = new ArcadeManager(this, serverStatusManager, ReadServerConfig(), _clientManager, _donationManager, _damageManager, statsManager, achievementManager, disguiseManager, creature, teleport, new Blood(this), chat, portal, preferenceManager, inventoryManager, packetHandler, cosmeticManager, projectileManager, petManager, hologramManager, webServerAddress, _brandListener);
		
		new MemoryFix(this);
		new CustomTagFix(this, packetHandler);
		new TablistFix(this);
		
		//Updates
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1, 1);
		
		MinecraftServer.getServer().getPropertyManager().setProperty("debug", true);

		// Remove nasty biomes from natural terrain generation, used for UHC
		BiomeBase.getBiomes()[BiomeBase.OCEAN.id] = BiomeBase.PLAINS;
		BiomeBase.getBiomes()[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
		BiomeBase.getBiomes()[BiomeBase.SWAMPLAND.id] = BiomeBase.PLAINS;
		BiomeBase.getBiomes()[BiomeBase.RIVER.id] = BiomeBase.PLAINS;
		Messenger messenger = Bukkit.getMessenger();
		messenger.registerIncomingPluginChannel(this, "MC|Brand", _brandListener);
	}

	@Override 
	public void onDisable() 
	{
		for (Player player : UtilServer.getPlayers())
			player.kickPlayer("Server Shutdown");

		if (_gameManager.GetGame() != null)
			if (_gameManager.GetGame().WorldData != null)
				_gameManager.GetGame().WorldData.Uninitialize();
	}

	public GameServerConfig ReadServerConfig() 
	{
		GameServerConfig config = new GameServerConfig();

		try
		{
			config.HostName = _serverConfiguration.getServerGroup().getHost();
			config.ServerType = _serverConfiguration.getServerGroup().getServerType();
			config.MinPlayers = _serverConfiguration.getServerGroup().getMinPlayers();
			config.MaxPlayers = _serverConfiguration.getServerGroup().getMaxPlayers();
			config.Tournament = _serverConfiguration.getServerGroup().getTournament();
			config.TournamentPoints = _serverConfiguration.getServerGroup().getTournamentPoints();
			config.TeamRejoin = _serverConfiguration.getServerGroup().getTeamRejoin();
			config.TeamAutoJoin = _serverConfiguration.getServerGroup().getTeamAutoJoin();
			config.TeamForceBalance = _serverConfiguration.getServerGroup().getTeamForceBalance();
			config.GameAutoStart = _serverConfiguration.getServerGroup().getGameAutoStart();
			config.GameTimeout = _serverConfiguration.getServerGroup().getGameTimeout();
			config.RewardGems = _serverConfiguration.getServerGroup().getRewardGems();
			config.RewardItems = _serverConfiguration.getServerGroup().getRewardItems();
			config.RewardStats = _serverConfiguration.getServerGroup().getRewardStats();
			config.RewardAchievements = _serverConfiguration.getServerGroup().getRewardAchievements();
			config.HotbarInventory = _serverConfiguration.getServerGroup().getHotbarInventory();
			config.HotbarHubClock = _serverConfiguration.getServerGroup().getHotbarHubClock();
			config.PlayerKickIdle = _serverConfiguration.getServerGroup().getPlayerKickIdle();

			GameType[] _gameList = new GameType[] {GameType.BaconBrawl, GameType.Barbarians,
					GameType.DeathTag, GameType.DragonEscape, GameType.DragonEscapeTeams, GameType.DragonRiders, GameType.Dragons,
					GameType.HoleInTheWall, GameType.Horse, GameType.Micro, GameType.MilkCow, GameType.Paintball, GameType.Runner, GameType.SearchAndDestroy,
					GameType.Smash, GameType.SmashTeams,
					GameType.Snake, GameType.SneakyAssassins, GameType.SnowFight, GameType.Spleef, GameType.SpleefTeams, GameType.SquidShooter,
					GameType.Stacker, GameType.WitherAssault, GameType.ZombieSurvival, GameType.PlayerPop};

			config.GameList.addAll(Arrays.asList(_gameList));
		}
		catch (Exception ex)
		{
			System.out.println("Error reading ServerConfiguration values : " + ex.getMessage());
		}

		if (!config.IsValid())
			config = GetDefaultConfig();

		return config;
	}

	public GameServerConfig GetDefaultConfig()
	{
		GameServerConfig config = new GameServerConfig();

		config.ServerType = "Micro Games";
		config.MinPlayers = 8;
		config.MaxPlayers = 16;
		config.Tournament = false;

		return config;
	}

	private void DeleteFolders() 
	{
		File curDir = new File(".");

		File[] filesList = curDir.listFiles();
		for(File file : filesList)
		{
			if (!file.isDirectory())
				continue;

			if (file.getName().length() < 4)
				continue;

			if (!file.getName().substring(0, 4).equalsIgnoreCase("Game"))
				continue;

			FileUtil.DeleteFolder(file);

			System.out.println("Deleted Old Game: " + file.getName());
		}
	}
}
