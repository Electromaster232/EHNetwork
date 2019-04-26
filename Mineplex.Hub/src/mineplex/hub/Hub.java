package mineplex.hub;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.CustomTagFix;
import mineplex.core.TablistFix;
import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.antihack.AntiHack;
import mineplex.core.aprilfools.AprilFoolsManager;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.chat.Chat;
import mineplex.core.command.CommandCenter;
import mineplex.core.creature.Creature;
import mineplex.core.disguise.DisguiseManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.elo.EloManager;
import mineplex.core.energy.Energy;
import mineplex.core.friend.FriendManager;
import mineplex.core.give.Give;
import mineplex.core.hologram.HologramManager;
import mineplex.core.ignore.IgnoreManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.memory.MemoryFix;
import mineplex.core.message.MessageManager;
import mineplex.core.monitor.LagMeter;
import mineplex.core.movement.Movement;
import mineplex.core.npc.NpcManager;
import mineplex.core.packethandler.PacketHandler;
import mineplex.core.party.PartyManager;
import mineplex.core.personalServer.PersonalServerManager;
import mineplex.core.pet.PetManager;
import mineplex.core.portal.Portal;
import mineplex.core.preferences.PreferencesManager;
import mineplex.core.projectile.ProjectileManager;
import mineplex.core.punish.Punish;
import mineplex.core.recharge.Recharge;
import mineplex.core.resourcepack.ResUnloadCheck;
import mineplex.core.resourcepack.ResPackManager;
import mineplex.core.serverConfig.ServerConfiguration;
import mineplex.core.spawn.Spawn;
import mineplex.core.stats.StatsManager;
import mineplex.core.status.ServerStatusManager;
import mineplex.core.task.TaskManager;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.FileUpdater;
import mineplex.core.updater.Updater;
import mineplex.core.visibility.VisibilityManager;
import mineplex.hub.modules.StackerManager;
import mineplex.hub.poll.PollManager;
import mineplex.hub.queue.QueueManager;
import mineplex.hub.server.ServerManager;
import mineplex.minecraft.game.classcombat.Class.ClassManager;
import mineplex.minecraft.game.classcombat.Condition.SkillConditionManager;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;
import mineplex.minecraft.game.classcombat.item.ItemFactory;
import mineplex.minecraft.game.classcombat.shop.ClassCombatShop;
import mineplex.minecraft.game.classcombat.shop.ClassShopManager;
import mineplex.minecraft.game.core.IRelation;
import mineplex.minecraft.game.core.combat.CombatManager;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.damage.DamageManager;
import mineplex.minecraft.game.core.fire.Fire;

public class Hub extends JavaPlugin implements IRelation
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
		DisguiseManager disguiseManager = new DisguiseManager(this, packetHandler);
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
		new CustomTagFix(this, packetHandler);
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
}
