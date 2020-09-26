package ehnetwork.staffServer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.chat.Chat;
import ehnetwork.core.command.CommandCenter;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.memory.MemoryFix;
import ehnetwork.core.monitor.LagMeter;
import ehnetwork.core.npc.NpcManager;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.punish.Punish;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.status.ServerStatusManager;
import ehnetwork.core.updater.FileUpdater;
import ehnetwork.core.updater.Updater;
import ehnetwork.staffServer.customerSupport.CustomerSupport;
import ehnetwork.staffServer.password.Password;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class StaffServer extends JavaPlugin
{
	private String WEB_CONFIG = "webServer";
	
	@Override
	public void onEnable()
	{
		getConfig().addDefault(WEB_CONFIG, "http://mplex.endlcdn.site/accounts/");
		getConfig().set(WEB_CONFIG, getConfig().getString(WEB_CONFIG));
		saveConfig();
		
		String webServerAddress = getConfig().getString(WEB_CONFIG);
		
		//Static Modules
		CommandCenter.Initialize(this); 
		CoreClientManager clientManager = new CoreClientManager(this, webServerAddress);
		CommandCenter.Instance.setClientManager(clientManager);
		Recharge.Initialize(this);
		
		DonationManager donationManager = new DonationManager(this, clientManager, webServerAddress);
		
		Punish punish = new Punish(this, webServerAddress, clientManager);
		new NpcManager(this, new Creature(this));
		ServerStatusManager serverStatusManager = new ServerStatusManager(this, clientManager, new LagMeter(this, clientManager));
		PreferencesManager preferenceManager = new PreferencesManager(this, clientManager, donationManager);
		preferenceManager.GiveItem = false;
		
		Portal portal = new Portal(this, clientManager, serverStatusManager.getCurrentServerName());
		new Chat(this, clientManager, preferenceManager, new AchievementManager(new StatsManager(this, clientManager), clientManager, donationManager), serverStatusManager.getCurrentServerName());
		new MemoryFix(this);  
		new FileUpdater(this, portal, serverStatusManager.getCurrentServerName(), serverStatusManager.getRegion());
		AntiHack.Initialize(this, punish, portal, preferenceManager, clientManager);
		
		new CustomerSupport(this, clientManager, donationManager, new SalesPackageManager(this, clientManager, donationManager, new InventoryManager(this, clientManager), new StatsManager(this, clientManager)));
		new Password(this, serverStatusManager.getCurrentServerName());
		
		//Updates
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1, 1);
		
		MinecraftServer.getServer().getPropertyManager().setProperty("debug", true);
		
		Bukkit.getWorlds().get(0).setSpawnLocation(0, 102, 0);
		
		((CraftServer)getServer()).setWhitelist(true);
		
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("377bdea3-badc-448d-81c1-65db43b17ea4"), "Strutt20"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("cf1b629c-cc55-4eb4-be9e-3ca86dfc7b9d"), "mannalou"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("04a484d0-93e0-4777-a70c-808046917e3a"), "EvilEsther"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("adaa7613-6683-400f-baf8-7272c04b2cb4"), "Timmy48081_"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("2d5fd31b-0aa5-41db-a62d-a4611a24349a"), "ishh"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("efaf9a17-2304-4f42-8433-421523c308dc"), "B2_mp"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("492ff708-fe76-4c5a-b9ed-a747b5fa20a0"), "Cherdy8s"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("80f40f29-4d66-4355-a32f-01a65af2a14c"), "rl6"));
		((CraftServer)getServer()).getHandle().addWhitelist(new GameProfile(UUID.fromString("cf85f470-5248-4978-8208-435736fa136e"), "RustyRoo"));
		
		
		((CraftServer)getServer()).getHandle().addOp(new GameProfile(UUID.fromString("377bdea3-badc-448d-81c1-65db43b17ea4"), "Strutt20"));
		((CraftServer)getServer()).getHandle().addOp(new GameProfile(UUID.fromString("efaf9a17-2304-4f42-8433-421523c308dc"), "B2_mp"));
		((CraftServer)getServer()).getHandle().addOp(new GameProfile(UUID.fromString("2d5fd31b-0aa5-41db-a62d-a4611a24349a"), "ishh"));
		((CraftServer)getServer()).getHandle().addOp(new GameProfile(UUID.fromString("cf85f470-5248-4978-8208-435736fa136e"), "RustyRoo"));		
	}
}
