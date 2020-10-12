package ehnetwork.hub;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.TablistFix;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.aprilfools.AprilFoolsManager;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.chat.Chat;
import ehnetwork.core.command.CommandCenter;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.elo.EloManager;
import ehnetwork.core.energy.Energy;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.give.Give;
import ehnetwork.core.hologram.HologramManager;
import ehnetwork.core.ignore.IgnoreManager;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.memory.MemoryFix;
import ehnetwork.core.message.MessageManager;
import ehnetwork.core.monitor.LagMeter;
import ehnetwork.core.movement.Movement;
import ehnetwork.core.npc.NpcManager;
import ehnetwork.core.packethandler.PacketHandler;
import ehnetwork.core.party.PartyManager;
import ehnetwork.core.personalServer.PersonalServerManager;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.projectile.ProjectileManager;
import ehnetwork.core.punish.Punish;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.resourcepack.ResPackManager;
import ehnetwork.core.resourcepack.ResUnloadCheck;
import ehnetwork.core.serverConfig.ServerConfiguration;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.status.ServerStatusManager;
import ehnetwork.core.task.TaskManager;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.core.updater.FileUpdater;
import ehnetwork.core.updater.Updater;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.hub.modules.StackerManager;
import ehnetwork.hub.poll.PollManager;
import ehnetwork.hub.queue.QueueManager;
import ehnetwork.hub.server.ServerManager;
import ehnetwork.minecraft.game.classcombat.Class.ClassManager;
import ehnetwork.minecraft.game.classcombat.Condition.SkillConditionManager;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;
import ehnetwork.minecraft.game.classcombat.shop.ClassCombatShop;
import ehnetwork.minecraft.game.classcombat.shop.ClassShopManager;
import ehnetwork.minecraft.game.core.IRelation;
import ehnetwork.minecraft.game.core.combat.CombatManager;
import ehnetwork.minecraft.game.core.condition.ConditionManager;
import ehnetwork.minecraft.game.core.damage.DamageManager;
import ehnetwork.minecraft.game.core.fire.Fire;

public class Hub extends JavaPlugin implements IRelation, Listener
{
	private String WEB_CONFIG = "webServer";
 
	@Override 
	public void onEnable()
	{
		getConfig().addDefault(WEB_CONFIG, "http://mplex.endlcdn.site/accounts/");
		getConfig().set(WEB_CONFIG, getConfig().getString(WEB_CONFIG));
		saveConfig();
		
		String webServerAddress = getConfig().getString(WEB_CONFIG);
		 
		//Logger.initialize(this);
		 
		//Static Modules
		CommandCenter.Initialize(this);
		CoreClientManager clientManager = new CoreClientManager(this, webServerAddress);
		getServer().getPluginManager().registerEvents(this, this);
		CommandCenter.Instance.setClientManager(clientManager);
		
		ItemStackFactory.Initialize(this, false);
		Recharge.Initialize(this);
		VisibilityManager.Initialize(this);		Give.Initialize(this);
		Punish punish = new Punish(this, webServerAddress, clientManager);
		BlockRestore blockRestore = new BlockRestore(this);
		DonationManager donationManager = new DonationManager(this, clientManager, webServerAddress);

		new ServerConfiguration(this, clientManager);
		
		//Other Modules
		PacketHandler packetHandler = new PacketHandler(this);
		DisguiseManager disguiseManager = new DisguiseManager(this);
		PreferencesManager preferenceManager = new PreferencesManager(this, clientManager, donationManager);
		preferenceManager.GiveItem = true;
		Creature creature = new Creature(this);
		NpcManager npcManager = new NpcManager(this, creature);
		PetManager petManager = new PetManager(this, clientManager, donationManager, disguiseManager, creature, blockRestore, webServerAddress);
		PollManager pollManager = new PollManager(this, clientManager, donationManager);
		
		//Main Modules
		ServerStatusManager serverStatusManager = new ServerStatusManager(this, clientManager, new LagMeter(this, clientManager));
		
		Portal portal = new Portal(this, clientManager, serverStatusManager.getCurrentServerName());

		AntiHack.Initialize(this, punish, portal, preferenceManager, clientManager);
		
        IgnoreManager ignoreManager = new IgnoreManager(this, clientManager, preferenceManager, portal);

        FriendManager friendManager = new FriendManager(this, clientManager, preferenceManager, portal);        
		
		StatsManager statsManager = new StatsManager(this, clientManager);
		AchievementManager achievementManager = new AchievementManager(statsManager, clientManager, donationManager);
 
		PartyManager partyManager = new PartyManager(this, portal, clientManager, preferenceManager);
		
		HubManager hubManager = new HubManager(this, blockRestore, clientManager, donationManager, new ConditionManager(this), disguiseManager, new TaskManager(this, clientManager, webServerAddress), portal, partyManager, preferenceManager, petManager, pollManager, statsManager, achievementManager, new HologramManager(this));

		QueueManager queueManager = new QueueManager(this, clientManager, donationManager, new EloManager(this, clientManager), partyManager);

		new ServerManager(this, clientManager, donationManager, portal, partyManager, serverStatusManager, hubManager, new StackerManager(hubManager), queueManager);
		Chat chat = new Chat(this, clientManager, preferenceManager, achievementManager, serverStatusManager.getCurrentServerName());
		new MessageManager(this, clientManager, preferenceManager, ignoreManager, punish, friendManager, chat);
		new MemoryFix(this);  
		new FileUpdater(this, portal, serverStatusManager.getCurrentServerName(), serverStatusManager.getRegion());
		new TablistFix(this);
		new ResPackManager(new ResUnloadCheck()
		{
			public boolean canSendUnload(Player player)
			{
				return true;
			}
		});
		//new Replay(this, packetHandler);
		new PersonalServerManager(this, clientManager);
		
		AprilFoolsManager.Initialize(this, clientManager, disguiseManager);
		
		CombatManager combatManager = new CombatManager(this);
		
		ProjectileManager throwManager = new ProjectileManager(this);
		SkillConditionManager conditionManager = new SkillConditionManager(this);
		
		DamageManager damage = new DamageManager(this, combatManager, npcManager, disguiseManager, conditionManager);
		Fire fire = new Fire(this, conditionManager, damage);
		Teleport teleport = new Teleport(this); 
		Energy energy = new Energy(this);
		energy.setEnabled(false);
		
		ItemFactory itemFactory = new ItemFactory(this, blockRestore, conditionManager, damage, energy, fire, throwManager, webServerAddress);
		SkillFactory skillManager = new SkillFactory(this, damage, this, combatManager, conditionManager, throwManager, disguiseManager, blockRestore, fire, new Movement(this), teleport, energy, webServerAddress);
		ClassManager classManager = new ClassManager(this, clientManager, donationManager, skillManager, itemFactory, webServerAddress);
		
        ClassShopManager shopManager = new ClassShopManager(this, classManager, skillManager, itemFactory, achievementManager, clientManager);
        
        new ClassCombatShop(shopManager, clientManager, donationManager, false, "Brute", classManager.GetClass("Brute"));
        new ClassCombatShop(shopManager, clientManager, donationManager, false, "Mage", classManager.GetClass("Mage"));
        new ClassCombatShop(shopManager, clientManager, donationManager, false, "Ranger", classManager.GetClass("Ranger"));
        new ClassCombatShop(shopManager, clientManager, donationManager, false, "Knight", classManager.GetClass("Knight"));
        new ClassCombatShop(shopManager, clientManager, donationManager, false, "Assassin", classManager.GetClass("Assassin"));
        
		//Updates
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1, 1);
	}

	@Override
	public void onDisable()
	{
 
	}
	
	@Override
	public boolean canHurt(Player a, Player b)
	{
		return false;
	}

	@Override
	public boolean canHurt(String a, String b)
	{
		return false;
	}

	@Override
	public boolean isSafe(Player a)
	{
		return true;
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		CommandCenter.Instance.OnPlayerCommandPreprocess(event);
	}
}
