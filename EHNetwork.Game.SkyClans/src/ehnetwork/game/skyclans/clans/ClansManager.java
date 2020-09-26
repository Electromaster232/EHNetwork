package ehnetwork.game.skyclans.clans;

import java.util.HashSet;
import java.util.TimeZone;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.CustomTagFix;
import ehnetwork.core.MiniClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.creature.event.CreatureSpawnCustomEvent;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.energy.Energy;
import ehnetwork.core.movement.Movement;
import ehnetwork.core.npc.NpcManager;
import ehnetwork.core.packethandler.PacketHandler;
import ehnetwork.core.projectile.ProjectileManager;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.game.skyclans.clans.commands.ClansAllyChatCommand;
import ehnetwork.game.skyclans.clans.commands.ClansCommand;
import ehnetwork.game.skyclans.clans.repository.ClanTerritory;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanMemberToken;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanTerritoryToken;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanToken;
import ehnetwork.game.skyclans.clans.war.WarManager;
import ehnetwork.game.skyclans.fields.Field;
import ehnetwork.game.skyclans.gameplay.Gameplay;
import ehnetwork.game.skyclans.clans.commands.ClansChatCommand;
import ehnetwork.game.skyclans.clans.commands.ServerTimeCommand;
import ehnetwork.minecraft.game.classcombat.Class.ClassManager;
import ehnetwork.minecraft.game.classcombat.Class.repository.token.CustomBuildToken;
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
import ehnetwork.minecraft.game.core.mechanics.Weapon;

public class ClansManager extends MiniClientPlugin<ClientClan> implements IRelation
{
	private static final TimeZone TIME_ZONE = TimeZone.getDefault();

	private String _serverName;
	
	private CoreClientManager _clientManager;
	private CombatManager _combatManager;
	private ClansUtility _clanUtility;
	private ClansDataAccessLayer _clanDataAccess;
	private ClansDisplay _clanDisplay;
	private ClansAdmin _clanAdmin;
	private ClansGame _clanGame;
	private ClansBlocks _clanBlocks;
	private BlockRestore _blockRestore;
	private Teleport _teleport;
	private ConditionManager _condition;
	private ClassCombatShop _classShop;
	private ClassManager _classManager;
	private WarManager _warManager;
	
	private int _inviteExpire = 2;
	private int _nameMin = 3;
	private int _nameMax = 10;
	private long _reclaimTime = 1800000;
	private long _onlineTime = 1200000;

	//Clans
	private NautHashMap<String, ClanInfo> _clanMap = new NautHashMap<String, ClanInfo>();
	private NautHashMap<String, ClanInfo> _clanMemberMap = new NautHashMap<String, ClanInfo>();
	private NautHashMap<String, ClanTerritory> _claimMap = new NautHashMap<String, ClanTerritory>();
	private NautHashMap<String, Long> _unclaimMap = new NautHashMap<String, Long>();
	
	public String[] denyClan = new String[] {
			"neut", "neutral", "sethome", "promote", "demote", "admin", "help", "create", "disband", "delete", "invite", "join", "kick", "ally", "trust", "claim", "unclaim", "territory", "home"};
	
	public ClansManager(JavaPlugin plugin, String serverName, CoreClientManager clientManager, DonationManager donationManager, BlockRestore blockRestore, Teleport teleport, String webServerAddress)
	{
		super("Clans Manager", plugin);
		
		_serverName = serverName;
		_clientManager = clientManager;
		_combatManager = new CombatManager(plugin);
		
		_blockRestore = blockRestore;
		_teleport = teleport;
				
		_clanAdmin = new ClansAdmin(this);
		_clanBlocks = new ClansBlocks();
		_clanDataAccess = new ClansDataAccessLayer(this);
		_clanDisplay = new ClansDisplay(plugin, this);
		_clanGame = new ClansGame(plugin, this);
		_clanUtility = new ClansUtility(this);
		
		Energy energy = new Energy(plugin);
		PacketHandler packetHandler = new PacketHandler(plugin);
		new CustomTagFix(plugin, packetHandler);
		DisguiseManager disguiseManager = new DisguiseManager(plugin, packetHandler);
		_condition = new SkillConditionManager(plugin);
		Creature creature = new Creature(plugin);	
		
		new Field(plugin, creature, _condition, energy, serverName);
		
		DamageManager damageManager = new DamageManager(plugin, _combatManager, new NpcManager(plugin, creature), disguiseManager, _condition);
		
		new Weapon(plugin, energy);
		new Gameplay(plugin, this, blockRestore, damageManager);
		ProjectileManager throwManager = new ProjectileManager(plugin);
		Fire fire = new Fire(plugin, _condition, damageManager);
		
		HashSet<String> itemIgnore = new HashSet<String>();
		itemIgnore.add("Proximity Explosive");
		itemIgnore.add("Proximity Zapper");
		
		ItemFactory itemFactory = new ItemFactory(plugin, blockRestore, _condition, damageManager, energy, fire, throwManager, webServerAddress, itemIgnore);
		SkillFactory skillManager = new SkillFactory(plugin, damageManager, this, _combatManager, _condition, throwManager, disguiseManager, blockRestore, fire, new Movement(plugin), teleport, energy, webServerAddress);
		skillManager.RemoveSkill("Dwarf Toss", "Block Toss");
		_classManager = new ClassManager(plugin, _clientManager, donationManager, skillManager, itemFactory, webServerAddress);
		
		StatsManager statsManager = new StatsManager(plugin, _clientManager);
		AchievementManager achievementManager = new AchievementManager(statsManager, _clientManager, donationManager);
        ClassShopManager shopManager = new ClassShopManager(plugin, _classManager, skillManager, itemFactory, achievementManager, _clientManager);
        _classShop = new ClassCombatShop(shopManager, _clientManager, donationManager, true, "Class Shop");

		_warManager = new WarManager(plugin, this);

		ClanEnergyManager clanEnergyManager = new ClanEnergyManager(plugin, this, clientManager, donationManager);
		
		for (ClanToken token : _clanDataAccess.getRepository().retrieveClans())
		{
			ClanInfo clan = new ClanInfo(this, token);
			_clanMap.put(token.Name, clan);
			
			for (ClanMemberToken memberToken : token.Members)
				_clanMemberMap.put(memberToken.Name, clan);
			
			for (ClanTerritoryToken territoryToken : token.Territories)
				_claimMap.put(territoryToken.Chunk, new ClanTerritory(territoryToken));
		}
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new ClansCommand(this));
		addCommand(new ClansChatCommand(this));
		addCommand(new ClansAllyChatCommand(this));
		addCommand(new ServerTimeCommand(this));
	}

	public int getInviteExpire()
	{
		return _inviteExpire;
	}

	public NautHashMap<String, ClanInfo> getClanMap()
	{
		return _clanMap;
	}

	public NautHashMap<String, ClanInfo> getClanMemberMap()
	{
		return _clanMemberMap;
	}

	public ClanInfo getClan(Player player)
	{
		return _clanMemberMap.get(player.getName());
	}

	public boolean isInClan(Player player)
	{
		return _clanMemberMap.containsKey(player.getName());
	}

	public ClanInfo getClan(String clan)
	{
		return _clanMap.get(clan);
	}

	public NautHashMap<String, ClanTerritory> getClaimMap()
	{
		return _claimMap;
	}

	public long lastPower = System.currentTimeMillis();

	
	@EventHandler
	public void savePlayerActiveBuild(PlayerQuitEvent event)
	{
		if (_classManager.Get(event.getPlayer()) != null && _classManager.Get(event.getPlayer()).GetGameClass() != null)
		{
			CustomBuildToken activeBuild = _classManager.Get(event.getPlayer()).GetActiveCustomBuild(_classManager.Get(event.getPlayer()).GetGameClass());
			
			if (activeBuild == null)
				return;
			
			activeBuild.PlayerName = event.getPlayer().getName();
			
			// 0 is set aside for active build so we just dupe build to this row whenever we update it.
			activeBuild.CustomBuildNumber = 0;
			_classManager.GetRepository().SaveCustomBuild(activeBuild);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void BlockCreatureSpawn(CreatureSpawnCustomEvent event)
	{
		ClanInfo clan = _clanUtility.getOwner(event.GetLocation());
		
		if (clan != null)
			if (!clan.isAdmin() && !clan.getName().equals("Spawn"))
				event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Interact(PlayerInteractEvent event)
	{
		getClanGame().Interact(event);
		getClanDisplay().handleInteract(event);
	}

	@EventHandler
	public void join(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		ClanInfo clanInfo = getClanMemberMap().get(player.getName());
		if (clanInfo != null)
		{
			clanInfo.playerOnline(player);
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		ClanInfo clanInfo = getClanMemberMap().get(player.getName());
		if (clanInfo != null)
		{
			clanInfo.playerOffline(player);
		}
	}
	
	@EventHandler
	public void handlePlayerChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;
		
		ClientClan client =	Get(event.getPlayer());
		
		if (client == null)
			return;
		
		ClanInfo clan = _clanUtility.getClanByPlayer(event.getPlayer());
		
		if (client.isClanChat())
		{
			if (clan == null)	
			{
				Get(event.getPlayer()).setClanChat(false);
				return;
			}
	
			event.setFormat(C.cAqua + "%1$s " + C.cDAqua + "%2$s");
			event.getRecipients().clear();
			
			for (String cur : clan.getMembers().keySet())
			{
				Player player = UtilPlayer.searchOnline(null, cur, false);
	
				if (player == null)
					continue;
	
				event.getRecipients().add(player);
			}
		}
		else if (client.isAllyChat())
		{
			if (clan == null)	
			{
				Get(event.getPlayer()).setAllyChat(false);
				return;
			}
			
			event.setFormat(C.cDGreen + clan.getName() + " " + C.cDGreen + "%1$s " + C.cGreen + "%2$s");
			event.getRecipients().clear();
			
			for (String cur : clan.getMembers().keySet())
			{
				Player player = UtilPlayer.searchOnline(null, cur, false);

				if (player == null)
					continue;

				event.getRecipients().add(player);
			}
			
			for (String allyName : clan.getAllyMap().keySet())
			{
				ClanInfo ally = _clanUtility.getClanByClanName(allyName);
				if (ally == null)	continue;
				
				for (String playerName : ally.getMembers().keySet())
				{
					Player player = UtilPlayer.searchOnline(null, playerName, false);

					if (player == null)
						continue;

					event.getRecipients().add(player);
				}
			}
		}
		else
		{
			if (clan == null)	
			{
				event.setFormat(C.cYellow + "%1$s " + C.cWhite + "%2$s");
				return;
			}

			event.setFormat(C.cGold + clan.getName() + " " + C.cYellow + "%1$s " + C.cWhite + "%2$s");
		}
	}

	public void messageClan(ClanInfo clan, String message)
	{
		for (Player player : clan.getOnlinePlayers())
			UtilPlayer.message(player, message);
	}

	public void chatClan(ClanInfo clan, Player caller, String message)
	{
		messageClan(clan, C.cAqua + caller.getName() + " " + C.cDAqua + message);
	}
	
	public void chatAlly(ClanInfo clan, Player caller, String message)
	{
		String sendMessage = C.cDGreen + clan.getName() + " " + C.cDGreen + caller.getName() + " " + C.cGreen + message;

		messageClan(clan, sendMessage);

		for (String allyName : clan.getAllyMap().keySet())
		{
			ClanInfo ally = _clanUtility.getClanByClanName(allyName);
			if (ally == null)	continue;

			messageClan(ally, sendMessage);
		}
	}

	public int getNameMin() 
	{
		return _nameMin;
	}

	public int getNameMax() 
	{
		return _nameMax;
	}

	public long getReclaimTime() 
	{
		return _reclaimTime;
	}

	public boolean canHurt(Player a, Player b) 
	{
		if (a.equals(b))
			return false;

		return _clanUtility.canHurt(a, b);
	}

	public boolean canHurt(String a, String b) 
	{
		if (a.equals(b))
			return false;

		return _clanUtility.canHurt(UtilPlayer.searchExact(a), UtilPlayer.searchExact(b));
	}
	
	public boolean isSafe(Player a) 
	{
		return _clanUtility.isSafe(a);
	}

	public ClansUtility.ClanRelation getRelation(String playerA, String playerB)
	{
		return getClanUtility().rel(_clanMemberMap.get(playerA), _clanMemberMap.get(playerB));
	}
	
	public long getOnlineTime()
	{
		return _onlineTime;
	}
	
	public CombatManager getCombatManager()
	{
		return _combatManager;
	}
	
	public ClansUtility getClanUtility()
	{
		return _clanUtility;
	}

	@Override
	protected ClientClan AddPlayer(String player)
	{
		return new ClientClan();
	}

	public BlockRestore getBlockRestore()
	{
		return _blockRestore;
	}

	public ClansDataAccessLayer getClanDataAccess()
	{
		return _clanDataAccess;
	}

	public Teleport getTeleport()
	{
		return _teleport;
	}

	public ClansDisplay getClanDisplay()
	{
		return _clanDisplay;
	}

	public NautHashMap<String, Long> getUnclaimMap()
	{
		return _unclaimMap;
	}

	public ClansAdmin getClanAdmin()
	{
		return _clanAdmin;
	}
	
	public ClansGame getClanGame()
	{
		return _clanGame;
	}
	
	public ClansBlocks getClanBlocks()
	{
		return _clanBlocks;
	}

	public String getServerName()
	{
		return _serverName;
	}

	public CoreClientManager getClientManager()
	{
		return _clientManager;
	}

	public ConditionManager getCondition()
	{
		return _condition;
	}

	public ClassCombatShop getClanShop()
	{
		return _classShop;
	}

	public WarManager getWarManager()
	{
		return _warManager;
	}

	public int convertGoldToEnergy(int gold)
	{
		return gold * 4;
	}

	public int convertEnergyToGold(int energy)
	{
		return (energy / 4) + (energy % 4 == 0 ? 0 : 1);
	}

	/**
	 * Get the timezone for this server.
	 * This may be used in the future if we have
	 * clans servers with varying timezones.
	 * @return {@link java.util.TimeZone} that this server should run at
	 */
	public TimeZone getServerTimeZone()
	{
		return TIME_ZONE;
	}
}
