package ehnetwork.game.survival;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import net.minecraft.server.v1_7_R4.MinecraftServer;

import ehnetwork.core.TablistFix;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.account.event.BrandListener;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.blood.Blood;
import ehnetwork.core.chat.Chat;
import ehnetwork.core.command.CommandCenter;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.give.Give;
import ehnetwork.core.hologram.HologramManager;
import ehnetwork.core.ignore.IgnoreManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.leaderboard.LeaderboardManager;
import ehnetwork.core.memory.MemoryFix;
import ehnetwork.core.message.MessageManager;
import ehnetwork.core.monitor.LagMeter;
import ehnetwork.core.mount.MountManager;
import ehnetwork.core.npc.NpcManager;
import ehnetwork.core.packethandler.PacketHandler;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.projectile.ProjectileManager;
import ehnetwork.core.punish.Punish;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.serverConfig.ServerConfiguration;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.status.ServerStatusManager;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.core.updater.FileUpdater;
import ehnetwork.core.updater.Updater;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.minecraft.game.core.combat.CombatManager;
import ehnetwork.minecraft.game.core.damage.DamageManager;

public class Survival extends JavaPlugin
{      
	private String WEB_CONFIG = "webServer";

	//Modules   
	private CoreClientManager _clientManager;
	private DonationManager _donationManager; 
	private DamageManager _damageManager;
	private SurvivalManager _gameManager;

	private BrandListener _brandListener;

	 
	private ServerConfiguration _serverConfiguration;

	@Override     
	public void onEnable() 
	{

		System.out.println("lmfao goteem");
		//Delete Old Games Folders
 
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
		 _gameManager = new SurvivalManager(this, serverStatusManager, _clientManager, _donationManager, _damageManager, statsManager, achievementManager, disguiseManager, creature, teleport, new Blood(this), chat, portal, preferenceManager, inventoryManager, packetHandler, cosmeticManager, projectileManager, petManager, hologramManager, webServerAddress, _brandListener);
		
		new MemoryFix(this);
		new CustomTagFix(this, packetHandler);
		new TablistFix(this);
		
		//Updates
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1, 1);
		
		MinecraftServer.getServer().getPropertyManager().setProperty("debug", true);

		// Remove nasty biomes from natural terrain generation, used for UHC
		//BiomeBase.getBiomes()[BiomeBase.OCEAN.id] = BiomeBase.PLAINS;
		//BiomeBase.getBiomes()[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
		//BiomeBase.getBiomes()[BiomeBase.SWAMPLAND.id] = BiomeBase.PLAINS;
		//BiomeBase.getBiomes()[BiomeBase.RIVER.id] = BiomeBase.PLAINS;
		Messenger messenger = Bukkit.getMessenger();
		messenger.registerIncomingPluginChannel(this, "MC|Brand", _brandListener);
	}

	@Override 
	public void onDisable() 
	{

	}


}
