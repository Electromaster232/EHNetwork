package mineplex.game.clans;

import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_7_R4.MinecraftServer;

import mineplex.core.account.CoreClientManager;
import mineplex.core.antihack.AntiHack;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.stats.StatsManager;
import mineplex.core.chat.Chat;
import mineplex.core.command.CommandCenter;
import mineplex.core.donation.DonationManager;
import mineplex.core.explosion.Explosion;
import mineplex.core.friend.FriendManager;
import mineplex.core.ignore.IgnoreManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.memory.MemoryFix;
import mineplex.core.message.MessageManager;
import mineplex.core.monitor.LagMeter;
import mineplex.core.portal.Portal;
import mineplex.core.preferences.PreferencesManager;
import mineplex.core.punish.Punish;
import mineplex.core.recharge.Recharge;
import mineplex.core.serverConfig.ServerConfiguration;
import mineplex.core.spawn.Spawn;
import mineplex.core.status.ServerStatusManager;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.FileUpdater;
import mineplex.core.updater.Updater;
import mineplex.game.clans.clans.ClansManager;
import mineplex.game.clans.shop.building.BuildingShop;
import mineplex.game.clans.shop.pvp.PvpShop;

public class Clans extends JavaPlugin
{      
	private String WEB_CONFIG = "webServer";
 
	//Modules   
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private StatsManager _statsManager;
	private AchievementManager _achievementManager;

	@Override     
	public void onEnable() 
	{
		//Configs
		getConfig().addDefault(WEB_CONFIG, "http://mplex.endlcdn.site/accounts/");
		getConfig().addDefault("ram", "1024M");
		getConfig().set("ram", getConfig().getString("ram"));
		getConfig().set(WEB_CONFIG, getConfig().getString(WEB_CONFIG));
		saveConfig();
		
		String webServerAddress = getConfig().getString(WEB_CONFIG);

		//Logger.initialize(this);

		//Static Modules
		CommandCenter.Initialize(this);		
		_clientManager = new CoreClientManager(this, webServerAddress);
		CommandCenter.Instance.setClientManager(_clientManager);

		_statsManager = new StatsManager(this, _clientManager);

		ItemStackFactory.Initialize(this, false);  
		Recharge.Initialize(this);   

		_donationManager = new DonationManager(this, _clientManager, webServerAddress);

		_achievementManager = new AchievementManager(_statsManager, _clientManager, _donationManager);

		new ServerConfiguration(this, _clientManager);
		
		PreferencesManager preferenceManager = new PreferencesManager(this, _clientManager, _donationManager);

		ServerStatusManager serverStatusManager = new ServerStatusManager(this, _clientManager, new LagMeter(this, _clientManager));
		new Spawn(this, serverStatusManager.getCurrentServerName());
		Teleport teleport = new Teleport(this);
		Portal portal = new Portal(this, _clientManager, serverStatusManager.getCurrentServerName());
		new FileUpdater(this, portal, serverStatusManager.getCurrentServerName(), serverStatusManager.getRegion());


		Punish punish = new Punish(this, webServerAddress, _clientManager);
		AntiHack.Initialize(this, punish, portal, preferenceManager, _clientManager);
		AntiHack.Instance.setKick(false);

		BlockRestore blockRestore = new BlockRestore(this);
 
		IgnoreManager ignoreManager = new IgnoreManager(this, _clientManager, preferenceManager, portal);
		Chat chat = new Chat(this,_clientManager,preferenceManager,_achievementManager,"Clans");
		new MessageManager(this, _clientManager, preferenceManager, ignoreManager, punish, new FriendManager(this, _clientManager, preferenceManager, portal), chat);

		new MemoryFix(this);
		new Explosion(this, blockRestore);
		new FriendManager(this, _clientManager, preferenceManager, portal);
		new InventoryManager(this, _clientManager);
		
		ClansManager clans = new ClansManager(this, serverStatusManager.getCurrentServerName(), _clientManager, _donationManager, blockRestore, teleport, webServerAddress);	
		new Recipes(this);
		new Farming(this);
		new BuildingShop(clans, _clientManager, _donationManager);
		new PvpShop(clans, _clientManager, _donationManager);
		
		//Updates
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1, 1);
		
		MinecraftServer.getServer().getPropertyManager().setProperty("debug", true);
	}

	@Override
	public void onDisable()
	{

	}
}
