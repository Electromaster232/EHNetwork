package ehnetwork.game.skyclans;

import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_7_R4.MinecraftServer;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.chat.Chat;
import ehnetwork.core.command.CommandCenter;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.explosion.Explosion;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.ignore.IgnoreManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.memory.MemoryFix;
import ehnetwork.core.message.MessageManager;
import ehnetwork.core.monitor.LagMeter;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.punish.Punish;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.serverConfig.ServerConfiguration;
import ehnetwork.core.spawn.Spawn;
import ehnetwork.core.status.ServerStatusManager;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.core.updater.FileUpdater;
import ehnetwork.core.updater.Updater;
import ehnetwork.game.skyclans.shop.building.BuildingShop;
import ehnetwork.game.skyclans.shop.pvp.PvpShop;
import ehnetwork.game.skyclans.clans.ClansManager;

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
