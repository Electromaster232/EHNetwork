package nautilus.game.arcade;

import java.awt.Event;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.google.common.base.Objects;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.blood.Blood;
import mineplex.core.chat.Chat;
import mineplex.core.common.Rank;
import mineplex.core.common.jsonchat.ClickEvent;
import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.cosmetic.CosmeticManager;
import mineplex.core.creature.Creature;
import mineplex.core.disguise.DisguiseManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.elo.EloManager;
import mineplex.core.energy.Energy;
import mineplex.core.explosion.Explosion;
import mineplex.core.hologram.HologramManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.movement.Movement;
import mineplex.core.notifier.NotificationManager;
import mineplex.core.packethandler.IPacketHandler;
import mineplex.core.packethandler.PacketHandler;
import mineplex.core.packethandler.PacketInfo;
import mineplex.core.packethandler.PacketPlayResourcePackStatus;
import mineplex.core.packethandler.PacketPlayResourcePackStatus.EnumResourcePackStatus;
import mineplex.core.party.PartyManager;
import mineplex.core.pet.PetManager;
import mineplex.core.portal.Portal;
import mineplex.core.preferences.PreferencesManager;
import mineplex.core.projectile.ProjectileManager;
import mineplex.core.resourcepack.ResUnloadCheck;
import mineplex.core.resourcepack.ResPackManager;
import mineplex.core.resourcepack.redis.RedisUnloadResPack;
import mineplex.core.reward.RewardRarity;
import mineplex.core.reward.rewards.PetReward;
import mineplex.core.stats.StatsManager;
import mineplex.core.status.ServerStatusManager;
import mineplex.core.task.TaskManager;
import mineplex.core.teleport.Teleport;
import mineplex.core.timing.TimingManager;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.classcombat.Class.ClassManager;
import mineplex.minecraft.game.classcombat.Condition.SkillConditionManager;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;
import mineplex.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;
import mineplex.minecraft.game.classcombat.item.ItemFactory;
import mineplex.minecraft.game.classcombat.item.event.ItemTriggerEvent;
import mineplex.minecraft.game.classcombat.shop.ClassCombatShop;
import mineplex.minecraft.game.classcombat.shop.ClassShopManager;
import mineplex.minecraft.game.core.IRelation;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.damage.DamageManager;
import mineplex.minecraft.game.core.fire.Fire;
import nautilus.game.arcade.addons.CompassAddon;
import nautilus.game.arcade.addons.SoupAddon;
import nautilus.game.arcade.addons.TeamArmorAddon;
import nautilus.game.arcade.command.DisguiseCommand;
import nautilus.game.arcade.command.GameCommand;
import nautilus.game.arcade.command.VanishCommand;
import nautilus.game.arcade.command.WriteCommand;
import nautilus.game.arcade.command.KitUnlockCommand;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.GameServerConfig;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.games.event.EventModule;
import nautilus.game.arcade.game.games.uhc.UHC;
import nautilus.game.arcade.managers.GameAchievementManager;
import nautilus.game.arcade.managers.GameChatManager;
import nautilus.game.arcade.managers.GameCreationManager;
import nautilus.game.arcade.managers.GameFlagManager;
import nautilus.game.arcade.managers.GameGemManager;
import nautilus.game.arcade.managers.GameHostManager;
import nautilus.game.arcade.managers.GameLobbyManager;
import nautilus.game.arcade.managers.GameLootManager;
import nautilus.game.arcade.managers.GameManager;
import nautilus.game.arcade.managers.GamePlayerManager;
import nautilus.game.arcade.managers.GameSpectatorManager;
import nautilus.game.arcade.managers.GameStatManager;
import nautilus.game.arcade.managers.GameTournamentManager;
import nautilus.game.arcade.managers.GameWorldManager;
import nautilus.game.arcade.managers.IdleManager;
import nautilus.game.arcade.managers.MiscManager;
import nautilus.game.arcade.shop.ArcadeShop;
import static org.bukkit.Bukkit.getServer;

public class ArcadeManager extends MiniPlugin implements IRelation
{ 
	// Modules       
	private BlockRestore _blockRestore;
	private Blood _blood;
	private Chat _chat;
	private CoreClientManager _clientManager;
	private DisguiseManager _disguiseManager;
	private DonationManager _donationManager;
	private ConditionManager _conditionManager;
	private PetManager _petManager;
	private Creature _creature;
	private DamageManager _damageManager;
	private Explosion _explosionManager;
	private EventModule _eventManager;
	
	private Fire _fire;
	private ProjectileManager _projectileManager;
	
	private Portal _portal;  
	private ArcadeShop _arcadeShop;
	
	//Champions Modules
	private boolean _enabled = true;
	private ClassManager _classManager;
	private SkillFactory _skillFactory;
	private ItemFactory _itemFactory;
	private Energy _energy;
	private ClassShopManager _classShopManager;
	private ClassCombatShop _classShop;
	private EloManager _eloManager;
	
	// Managers 
	private GameCreationManager _gameCreationManager;
	private GameGemManager _gameGemManager;
	private GameManager _gameManager;
	private GameLobbyManager _gameLobbyManager;
	private GamePlayerManager _gamePlayerManager;
	private GameTournamentManager _gameTournamentManager;
	private GameWorldManager _gameWorldManager;
	private GameHostManager _gameHostManager;
	private ServerStatusManager _serverStatusManager;
	private InventoryManager _inventoryManager;
	private CosmeticManager _cosmeticManager;
	private final IdleManager _idleManager;
    private HologramManager _hologramManager;
	private AchievementManager _achievementManager;
	private StatsManager _statsManager;
	private PartyManager _partyManager;
	private PreferencesManager _preferencesManager;

	private TaskManager _taskManager;
    private PacketHandler _packetHandler;
	
	
	private IPacketHandler _resourcePacketHandler;
	private String _resourcePackUrl;
	private boolean _resourcePackRequired;
	private NautHashMap<String, Boolean> _resourcePackUsers = new NautHashMap<String, Boolean>();
	private NautHashMap<String, Long> _resourcePackNoResponse = new NautHashMap<String, Long>();

	// Observers
	private HashSet<Player> _specList = new HashSet<Player>();

	// Server Games
	private GameServerConfig _serverConfig;

	// Games
	private Game _game;
	
	//Youtuber Kits
	private HashSet<Player> _youtube = new HashSet<Player>();

	public ArcadeManager(Arcade plugin, ServerStatusManager serverStatusManager, GameServerConfig serverConfig,
						 CoreClientManager clientManager, DonationManager donationManager, DamageManager damageManager,
						 StatsManager statsManager, AchievementManager achievementManager, DisguiseManager disguiseManager, Creature creature, Teleport teleport, Blood blood, Chat chat,
						 Portal portal, PreferencesManager preferences, InventoryManager inventoryManager, PacketHandler packetHandler,
						 CosmeticManager cosmeticManager, ProjectileManager projectileManager, PetManager petManager, HologramManager hologramManager, String webAddress)
	{
		super("Game Manager", plugin);

		_serverConfig = serverConfig;

		// Modules
		_blockRestore = new BlockRestore(plugin);
		
		_blood = blood;
		_preferencesManager = preferences;

		_explosionManager = new Explosion(plugin, _blockRestore);
		_explosionManager.SetDebris(false);

		if (serverConfig.GameList.contains(GameType.ChampionsDominate)
			|| serverConfig.GameList.contains(GameType.ChampionsTDM))
		{
			_conditionManager = new SkillConditionManager(plugin);
		}
		else
		{
			_conditionManager = new ConditionManager(plugin);
		}
		
		_clientManager = clientManager;
		_serverStatusManager = serverStatusManager;
		_chat = chat;
		_creature = creature;

		_damageManager = damageManager;
		_damageManager.UseSimpleWeaponDamage = true;
		_damageManager.setConditionManager(_conditionManager);
		
		_disguiseManager = disguiseManager;

		_donationManager = donationManager;

		_fire = new Fire(plugin, _conditionManager, damageManager);

		_projectileManager = projectileManager;
		
		_packetHandler = packetHandler;
		
		_partyManager = new PartyManager(plugin, portal, _clientManager, preferences);
		_statsManager = statsManager;
		_taskManager = new TaskManager(plugin, clientManager, webAddress);
		_achievementManager = achievementManager;
		_inventoryManager = inventoryManager;
		_cosmeticManager = cosmeticManager;
		_portal = portal;
		_petManager = petManager;
		_eventManager = new EventModule(this, getPlugin());

		// Shop
		_arcadeShop = new ArcadeShop(this, clientManager, donationManager);

		// Managers
		new GameChatManager(this);
		_gameCreationManager = new GameCreationManager(this);
		_gameGemManager = new GameGemManager(this);
		_gameManager = new GameManager(this);
		_gameLobbyManager = new GameLobbyManager(this, packetHandler);
		_gameHostManager = new GameHostManager(this);
		new GameFlagManager(this);
		_gamePlayerManager = new GamePlayerManager(this);
		new GameAchievementManager(this);
		_gameTournamentManager = new GameTournamentManager(this);
		new GameStatManager(this);
		new GameLootManager(this, petManager);
		new GameSpectatorManager(this);
		_gameWorldManager = new GameWorldManager(this);
		new MiscManager(this);
		_hologramManager = hologramManager;
		_idleManager = new IdleManager(this);
		//new HolidayManager(this);
		
		// Game Addons
		new CompassAddon(plugin, this);
		new SoupAddon(plugin, this);
		new TeamArmorAddon(plugin, this);
		
		new NotificationManager(getPlugin(), clientManager);
		
		//Champions Modules
		_energy = new Energy(plugin);
		
		_itemFactory = new ItemFactory(_plugin, _blockRestore, _conditionManager, damageManager, _energy,
				_fire, _projectileManager, webAddress);
		
		_skillFactory = new SkillFactory(plugin, damageManager, this, _damageManager.GetCombatManager(),
				_conditionManager, _projectileManager, _disguiseManager, _blockRestore, _fire, new Movement(plugin), teleport,
				_energy, webAddress);
		
		_classManager = new ClassManager(plugin, clientManager, donationManager, _skillFactory, _itemFactory,
				webAddress);

		_classShopManager = new ClassShopManager(_plugin, _classManager, _skillFactory, _itemFactory, _achievementManager, clientManager);
		
		_classShop = new ClassCombatShop(_classShopManager, clientManager, donationManager, false, "Class Shop");

		_eloManager = new EloManager(_plugin, clientManager);
		
		if (GetHost() != null && !GetHost().isEmpty())
		{
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
			{
				public void run()
				{
					Portal.transferPlayer(GetHost(), _serverStatusManager.getCurrentServerName());
				}
			}, 80L);
		}

		_resourcePacketHandler = new IPacketHandler()
		{

			@Override
			public void handle(PacketInfo packetInfo)
			{
				if (_resourcePackUrl != null && packetInfo.getPacket() instanceof PacketPlayResourcePackStatus)
				{

					final Player player = packetInfo.getPlayer();
					final EnumResourcePackStatus response = ((PacketPlayResourcePackStatus) packetInfo.getPacket())
							.getResourcePackStatus();

					Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
					{

						@Override
						public void run()
						{
							if (_resourcePackRequired)
							{
								if (response == EnumResourcePackStatus.ACCEPTED)
								{
									_resourcePackNoResponse.remove(player.getName());
								}
								else if (response == EnumResourcePackStatus.DECLINED)
								{
									_resourcePackNoResponse.remove(player.getName());

									UtilPlayer.message(player, "  ");
									JsonMessage message = new JsonMessage("")
											.color("gold")
											.bold()
											.extra("You need to accept the resource pack!\n"
													+ "Click me for instructions on how to fix this!")

											.click(ClickEvent.OPEN_URL,

											"http://mineplex.com/forums/m/11929946/viewthread/21554536-wizards-resource-pack-help");

									message.sendToPlayer(player);
									UtilPlayer.message(player, "  ");

									returnHubNoResPack(player);
								}
								else if (response == EnumResourcePackStatus.FAILED_DOWNLOAD)
								{
									_resourcePackNoResponse.remove(player.getName());

									returnHubNoResPack(player, "Failed to download resource pack!");

									return;
								}
							}

							if (response == EnumResourcePackStatus.ACCEPTED || response == EnumResourcePackStatus.LOADED)
							{
								_resourcePackUsers.put(player.getName(), true);
							}
							else
							{
								_resourcePackUsers.remove(player.getName());
							}
						}
					});
				}
			}
		};

		new ResPackManager(new ResUnloadCheck()
		{
			public boolean canSendUnload(Player player)
			{
				if (_resourcePackUsers.containsKey(player.getName()))
				{
					return false;
				}

				return true;
			}
		});

		getPacketHandler().addPacketHandler(_resourcePacketHandler);
	}

	@Override
	public void addCommands()
	{
		addCommand(new GameCommand(this));
		addCommand(new WriteCommand(this));
		addCommand(new KitUnlockCommand(this));
		addCommand(new DisguiseCommand(this));
		addCommand(new VanishCommand(this));
	}

	public GameServerConfig GetServerConfig()
	{
		return _serverConfig;
	}

	public ArrayList<GameType> GetGameList()
	{
		return GetServerConfig().GameList;
	}

	public AchievementManager GetAchievement()
	{
		return _achievementManager;
	}

	public Blood GetBlood()
	{
		return _blood;
	}

	public Chat GetChat()
	{
		return _chat;
	}

	public BlockRestore GetBlockRestore()
	{
		return _blockRestore;
	}

	public CoreClientManager GetClients()
	{
		return _clientManager;
	}

	public ConditionManager GetCondition()
	{
		return _conditionManager;
	}

	public Creature GetCreature()
	{
		return _creature;
	}
	
	public PacketHandler getPacketHandler()
	{
	    return _packetHandler;
	}

	public CosmeticManager getCosmeticManager()
	{
		return _cosmeticManager;
	}

	public DisguiseManager GetDisguise()
	{
		return _disguiseManager;
	}
	
	public HologramManager getHologramManager()
	{
	    return _hologramManager;
	}

	public DamageManager GetDamage()
	{
		return _damageManager;
	}

	public DonationManager GetDonation()
	{
		return _donationManager;
	}

	public EloManager getEloManager()
	{
		return _eloManager;
	}

	public Explosion GetExplosion()
	{
		return _explosionManager;
	}

	public Fire GetFire()
	{
		return _fire;
	}
	
	public ProjectileManager GetProjectile()
	{
		return _projectileManager;
	}

	public Portal GetPortal()
	{
		return _portal;
	}

	public GameLobbyManager GetLobby()
	{
		return _gameLobbyManager;
	}

	public TaskManager GetTaskManager()
	{
		return _taskManager;
	}

	public ArcadeShop GetShop()
	{
		return _arcadeShop;
	}

	public GameCreationManager GetGameCreationManager()
	{
		return _gameCreationManager;
	}

	public GameHostManager GetGameHostManager()
	{
		return _gameHostManager;
	}

	public GameManager GetGameManager()
	{
		return _gameManager;
	}

	public GameGemManager GetGameGemManager()
	{
		return _gameGemManager;
	}
	
	public GamePlayerManager GetGamePlayerManager()
	{
		return _gamePlayerManager;
	}
	
	public GameTournamentManager GetGameTournamentManager()
	{
		return _gameTournamentManager;
	}

	public GameWorldManager GetGameWorldManager()
	{
		return _gameWorldManager;
	}
	
	public EventModule GetEventModule()
	{
		return _eventManager;
	}
	
	public PreferencesManager getPreferences()
	{
		return _preferencesManager;
	}

	public StatsManager GetStatsManager()
	{
		return _statsManager;
	}

	public ServerStatusManager GetServerStatusManager()
	{
		return _serverStatusManager;
	}

	public ChatColor GetColor(Player player)
	{
		if (_game == null)
			return ChatColor.GRAY;

		GameTeam team = _game.GetTeam(player);
		if (team == null)
			return ChatColor.GRAY;

		return team.GetColor();
	}

	@Override
	public boolean canHurt(String a, String b)
	{
		return canHurt(UtilPlayer.searchExact(a), UtilPlayer.searchExact(b));
	}

	public boolean canHurt(Player pA, Player pB)
	{
		if (pA == null || pB == null)
			return false;

		if (!_game.Damage)
			return false;

		if (!_game.DamagePvP)
			return false;

		// Self Damage
		if (pA.equals(pB))
			return _game.DamageSelf;

		GameTeam tA = _game.GetTeam(pA);
		if (tA == null)
			return false;

		GameTeam tB = _game.GetTeam(pB);
		if (tB == null)
			return false;

		if (tA.equals(tB) && !_game.DamageTeamSelf)
			return false;

		if (!tA.equals(tB) && !_game.DamageTeamOther)
			return false;

		return true;
	}

	@Override
	public boolean isSafe(Player player)
	{
		if (_game == null)
			return true;

		if (_game.IsPlaying(player))
			return false;

		return true;
	}

	@EventHandler
	public void MessageMOTD(ServerListPingEvent event)
	{
		event.setMaxPlayers(_serverConfig.MaxPlayers);

		//MPS
		if (_gameHostManager != null && _gameHostManager.isPrivateServer())
		{
			if (_gameHostManager.isHostExpired())
			{
				event.setMotd(ChatColor.RED + "Finished");
				return;
			}
			
			if (!GetServerConfig().PublicServer || GetServerConfig().PlayerServerWhitelist)
			{
				event.setMotd(ChatColor.GRAY + "Private");
				return;
			}
		}

		String extrainformation = "|" + _serverConfig.ServerType + "|" + (_game == null ? "Unknown" : _game.GetName())
				+ "|" + ((_game == null || _game.WorldData == null) ? "Unknown" : _game.WorldData.MapName);

		if (_gameHostManager.isPrivateServer() && _gameHostManager.hasRank(Rank.MODERATOR))
			extrainformation += "|StaffHosted";

		//Always Joinable
//		if (_game != null && _game.JoinInProgress)
//		{
//			event.setMotd(ChatColor.GREEN + "Recruiting" + extrainformation);
//		}
		//UHC Timed
		if (_game != null && _game.GetType() == GameType.UHC)
		{
			event.setMotd(((UHC) _game).getMotdStatus() + extrainformation);
		}
		//Recruiting
		else if (_game == null || _game.GetState() == GameState.Recruit)
		{
			if (_game != null && _game.GetCountdown() != -1)
			{
				event.setMotd(ChatColor.GREEN + "Starting in " + _game.GetCountdown() + " Seconds" + extrainformation);
			}
			else
			{
				event.setMotd(ChatColor.GREEN + "Recruiting" + extrainformation);
			}
		}
		//In Progress
		else
		{
			event.setMotd(ChatColor.YELLOW + "In Progress" + extrainformation);
		}
	}

	@EventHandler
	public void MessageJoin(PlayerJoinEvent event)
	{
		String name = event.getPlayer().getName();
		
		if (_game != null && _game.AnnounceJoinQuit)
			event.setJoinMessage(F.sys("Join", GetColor(event.getPlayer()) + name));

		else
			event.setJoinMessage(null);
	}

	@EventHandler
	public void MessageQuit(PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		
		if (_game == null || _game.AnnounceJoinQuit)
			event.setQuitMessage(F.sys("Quit", GetColor(event.getPlayer()) + name));
		else
			event.setQuitMessage(null);
	}

	public Game GetGame()
	{
		return _game;
	}

	public void SetGame(Game game)
	{
		_game = game;
	}

	public int GetPlayerMin()
	{
		return GetServerConfig().MinPlayers;
	}

	public int GetPlayerFull()
	{
		return GetServerConfig().MaxPlayers;
	}

	public void HubClock(Player player)
	{
		if (!IsHotbarHubClock())
			return;

		if (_game != null && !_game.GiveClock)
			return;

		if (player.getOpenInventory().getType() != InventoryType.CRAFTING &&
				player.getOpenInventory().getType() != InventoryType.CREATIVE)
			return;

		if (!UtilGear.isMat(player.getInventory().getItem(8), Material.WATCH) && !UtilGear.isMat(player.getInventory().getItem(8), Material.SPECKLED_MELON))
		{
			player.getInventory().setItem(
					8,
					ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte) 0, 1, (short) 0, C.cGreen
							+ "Return to Hub", new String[]{"", ChatColor.RESET + "Click while holding this",
							ChatColor.RESET + "to return to the Hub."}));

			UtilInv.Update(player);
		}
	}

	@EventHandler
	public void Login(PlayerLoginEvent event)
	{
		if (getServer().hasWhitelist())
		{
			if (_clientManager.Get(event.getPlayer().getName()).GetRank().Has(event.getPlayer(), Rank.MODERATOR, false))
			{
				event.allow();
				event.setResult(PlayerLoginEvent.Result.ALLOWED);

				if (_serverConfig.Tournament)
				{
					event.getPlayer().setOp(true);
				}
			}
			else
			{
				for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
				{
					if (player.getName().equalsIgnoreCase(event.getPlayer().getName()))
					{
						event.allow();
						event.setResult(PlayerLoginEvent.Result.ALLOWED);
						return;
					}
				}

				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server Whitelisted!");
			}

			return;
		}

		// Reserved Slot Check
		if (Bukkit.getOnlinePlayers().size() >= getServer().getMaxPlayers())
		{
			if (_clientManager.Get(event.getPlayer().getName()).GetRank().Has(event.getPlayer(), Rank.HELPER, false))
			{
				event.allow();
				event.setResult(PlayerLoginEvent.Result.ALLOWED);
				return;
			}
			else if (_clientManager.Get(event.getPlayer().getName()).GetRank().Has(event.getPlayer(), Rank.ULTRA, false)
					|| _donationManager.Get(event.getPlayer().getName()).OwnsUnknownPackage(_serverConfig.ServerType + " ULTRA"))
			{
				
				if (GetGame() != null && GetGame().DontAllowOverfill)
				{
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, C.Bold + "Server has reached max capacity for gameplay purposes.");
					return;
				}
				else if (getServer().getOnlinePlayers().size() / Bukkit.getMaxPlayers() > 1.5)
				{
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, C.Bold + "Server has reached max capacity for gameplay purposes.");
					return;
				}
				else if (_gameHostManager.isEventServer() && getServer().getOnlinePlayers().size() >= 128)
				{
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, C.Bold + "Server has reached max capacity for gameplay purposes.");
					return;
				}
				
				event.allow();
				event.setResult(PlayerLoginEvent.Result.ALLOWED);

				return;
			}

			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server Full > Request Ultra at https://discord.gg/FttmSEQ");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void AdminOP(PlayerJoinEvent event)
	{
		// Give developers operator on their servers
		boolean testServer = _plugin.getConfig().getString("serverstatus.group").equalsIgnoreCase("Testing");

		if (_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.OWNER) || (testServer && (_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.DEVELOPER) || _clientManager.Get(event.getPlayer()).GetRank() == Rank.JNR_DEV)))
			event.getPlayer().setOp(true);
		else
			event.getPlayer().setOp(false);
	}

	public boolean IsAlive(Player player)
	{
		if (_game == null)
			return false;

		return _game.IsAlive(player);
	}

	public void Clear(Player player)
	{
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlySpeed(0.1F);
		
		UtilInv.Clear(player);

		((CraftEntity) player).getHandle().getDataWatcher().watch(0, Byte.valueOf((byte) 0));

		player.setSprinting(false);
		player.setSneaking(false);

		player.setFoodLevel(20);
		player.setSaturation(3f);
		player.setExhaustion(0f);

		player.setMaxHealth(20);
		player.setHealth(player.getMaxHealth());

		player.setFireTicks(0);
		player.setFallDistance(0);
		
		player.eject();
		player.leaveVehicle();

		player.setLevel(0);
		player.setExp(0f);

		((CraftPlayer) player).getHandle().spectating = false;
		((CraftPlayer) player).getHandle().k = true;

		// Arrows go bye bye.
		((CraftPlayer) player).getHandle().p(0);

		//Remove all conditions
		GetCondition().EndCondition(player, null, null);
		for (PotionEffect potion : player.getActivePotionEffects())
			player.removePotionEffect(potion.getType());

		HubClock(player);

		GetDisguise().undisguise(player);
	}

	public ArrayList<String> LoadFiles(String gameName)
	{
		TimingManager.start("ArcadeManager LoadFiles");
		
		File folder = new File(".." + File.separatorChar + ".." + File.separatorChar + "update" + File.separatorChar
				+ "maps" + File.separatorChar + gameName);
		System.out.println(folder.getAbsolutePath()+" -=-=-=-=-=" );
		if (!folder.exists())
			folder.mkdirs();

		ArrayList<String> maps = new ArrayList<String>();

		System.out.println("Searching Maps in: " + folder);

		for (File file : folder.listFiles())
		{
			if (!file.isFile())
				continue;

			String name = file.getName();

			if (name.length() < 5)
				continue;

			name = name.substring(name.length() - 4, name.length());

			if (file.getName().equals(".zip"))
				continue;

			maps.add(file.getName().substring(0, file.getName().length() - 4));
		}

		for (String map : maps)
			System.out.println("Found Map: " + map);

		TimingManager.stop("ArcadeManager LoadFiles");

		return maps;
	}

	public ClassManager getClassManager()
	{
		return _classManager;
	}

	public ClassCombatShop getClassShop()
	{
		return _classShop;
	}

	public void openClassShop(Player player)
	{
		_classShop.attemptShopOpen(player);
	}

	@EventHandler
	public void BlockBurn(BlockBurnEvent event)
	{
		if (_game == null)
			event.setCancelled(true);
	}

	@EventHandler
	public void BlockSpread(BlockSpreadEvent event)
	{
		if (_game == null)
			event.setCancelled(true);
	}

	@EventHandler
	public void BlockFade(BlockFadeEvent event)
	{
		if (_game == null)
			event.setCancelled(true);
	}

	@EventHandler
	public void BlockDecay(LeavesDecayEvent event)
	{
		if (_game == null)
			event.setCancelled(true);
	}

	@EventHandler
	public void MobSpawn(CreatureSpawnEvent event)
	{
		if (_game == null)
			event.setCancelled(true);
	}

	@EventHandler
	public void SkillTrigger(SkillTriggerEvent event)
	{
		if (_game == null || !_game.IsLive())
		{
			event.SetCancelled(true);
		}
	}

	@EventHandler
	public void ItemTrigger(ItemTriggerEvent event)
	{
		if (_game == null || !_game.IsLive())
		{
			event.SetCancelled(true);
		}
	}

	@EventHandler
	public void Observer(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().equalsIgnoreCase("/spec"))
		{
			event.setCancelled(true);

			if (_game != null && _game.InProgress())
			{
				UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot toggle Spectator during games."));
				return;
			}

			if (!_specList.remove(event.getPlayer()))
			{
				_specList.add(event.getPlayer());

				UtilPlayer.message(event.getPlayer(), F.main("Game", "You are now a Spectator!"));
			}
			else
			{
				UtilPlayer.message(event.getPlayer(), F.main("Game", "You are no longer a Spectator!"));
			}

			// Clean
			if (_game != null)
			{
				// Remove Data
				_game.RemoveTeamPreference(event.getPlayer());
				_game.GetPlayerKits().remove(event.getPlayer());
				_game.GetPlayerGems().remove(event.getPlayer());

				// Leave Team
				GameTeam team = _game.GetTeam(event.getPlayer());

				if (team != null)
				{
					team.RemovePlayer(event.getPlayer());
				}
			}
		}
	}

	@EventHandler
	public void ObserverQuit(PlayerQuitEvent event)
	{
		_specList.remove(event.getPlayer());
	}

	public boolean IsObserver(Player player)
	{
		return _specList.contains(player);
	}

	public boolean IsTournamentServer()
	{
		return _serverConfig.Tournament;
	}
	
	public boolean IsTournamentPoints()
	{
		return _serverConfig.TournamentPoints;
	}

	public boolean IsTeamRejoin()
	{
		return _serverConfig.TeamRejoin;
	}

	public boolean IsTeamAutoJoin()
	{
		return _serverConfig.TeamAutoJoin;
	}

	public boolean IsGameAutoStart()
	{
		return _serverConfig.GameAutoStart;
	}

	public boolean IsGameTimeout()
	{
		return _serverConfig.GameTimeout;
	}

	public boolean IsTeamBalance()
	{
		return _serverConfig.TeamForceBalance;
	}

	public boolean IsRewardGems()
	{
		return _serverConfig.RewardGems;
	}

	public boolean IsRewardItems()
	{
		return _serverConfig.RewardItems;
	}

	public boolean IsRewardStats()
	{
		return _serverConfig.RewardStats;
	}

	public boolean IsRewardAchievements()
	{
		return _serverConfig.RewardAchievements;
	}

	public boolean IsHotbarInventory()
	{
		return _serverConfig.HotbarInventory;
	}

	public boolean IsHotbarHubClock()
	{
		return _serverConfig.HotbarHubClock;
	}

	public boolean IsPlayerKickIdle()
	{
		return _serverConfig.PlayerKickIdle;
	}

	public int GetDesiredPlayerAmount()
	{
		return _serverConfig.MaxPlayers;
	}
	
	public String GetHost()
	{
		return _serverConfig.HostName;
	}

	@EventHandler
	public void ObserverQuit(GameStateChangeEvent event)
	{
		if (_skillFactory != null)
		{
			_skillFactory.ResetAll();
		}
	}

	public InventoryManager getInventoryManager()
	{
		return _inventoryManager;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void cosmeticState(GameStateChangeEvent event)
	{
		//Disable
		if (event.GetState() == GameState.Recruit)
		{
			getCosmeticManager().setActive(true);
		}
		else if (event.GetState() == GameState.Prepare || event.GetState() == GameState.Loading || event.GetState() == GameState.Dead)
		{
			if (event.GetGame().GadgetsDisabled)
			{
				if (getCosmeticManager().isShowingInterface())
				{
					getCosmeticManager().setActive(false);
					getCosmeticManager().disableItemsForGame();
				}
			}
		}
	}

	/*public void saveBasicStats(final Game game)
	{
		if (!IsTournamentServer())
			return;
		
		final Map<UUID, Boolean> data = new HashMap<>();

		for (Player loser : game.getLosers())
			data.put(loser.getUniqueId(), false);

		for (Player winner : game.getWinners())
			data.put(winner.getUniqueId(), true);

		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				getArcadeRepository().saveBasicStats(game.GetType(), IsTournamentServer(), (int) (System.currentTimeMillis() - game.getGameLiveTime()), data);
			}
		});
	}*/

	/*public void saveLeaderboardStats(Game game)
	{
		final TournamentType type = TournamentType.getTournamentType(game.GetType());

		if (type != null)
		{
			final Map<UUID, Boolean> data = new HashMap<>();

			for (Player loser : game.getLosers())
				data.put(loser.getUniqueId(), false);

			for (Player winner : game.getWinners())
				data.put(winner.getUniqueId(), true);

			Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					getArcadeRepository().saveLeaderboardStats(0, type.ordinal(), data);
				}
			});
		}
	}*/

	public boolean isGameInProgress()
	{
		return _game != null && _game.InProgress();
	}

	public boolean hasKitsUnlocked(Player player)
	{
		return _youtube.contains(player);
	}

	public void toggleUnlockKits(Player caller)
	{
		if (_youtube.remove(caller))
		{
			UtilPlayer.message(caller, C.cRed + C.Bold + "Celebrity Mode Disabled: " + ChatColor.RESET + "Kits Locked");
		}
		else
		{
			_youtube.add(caller);
			UtilPlayer.message(caller, C.cGreen + C.Bold + "Celebrity Mode Enabled: " + ChatColor.RESET + "All Kits Unlocked");
		}
	}
	
	@EventHandler
	public void quitYoutuber(PlayerQuitEvent event)
	{
		_youtube.remove(event.getPlayer());
	}

	public IdleManager getIdleManager()
	{
		return _idleManager;
	}

	public void rewardPet(Player player, String pet, EntityType type)
	{
		if (!player.isOnline())
			return;
		
		PetReward reward = new PetReward(_petManager, _inventoryManager, _donationManager, pet, pet, type, RewardRarity.OTHER, 0);
		
		if (reward.canGiveReward(player))
			reward.giveReward(null, player);
	}
	
	public void toggleChampionsModules(GameType gameType)
	{
		boolean isChamps = gameType == GameType.ChampionsDominate || gameType == GameType.ChampionsTDM;

		if (_enabled == isChamps)
		{
			System.out.println("----------Champions Modules are still " + isChamps);
			return;
		}
			
		System.out.println("----------Champions Modules set to " + isChamps);
		_enabled = isChamps;
		
		if (_enabled)
		{
			_classManager.setEnabled(true);
			_classShopManager.registerSelf();
			_skillFactory.registerSelf();
			_itemFactory.registerSelf();
			_energy.registerSelf();
			_eloManager.registerSelf();
			
			//Class Shop
			_plugin.getServer().getPluginManager().registerEvents(_classShop, _plugin);
		}
		else
		{
			_classManager.setEnabled(false);
			_classShopManager.deregisterSelf();
			_skillFactory.deregisterSelf();
			_itemFactory.deregisterSelf();
			_energy.deregisterSelf();
			_eloManager.deregisterSelf();
			
			//Class Shop
			HandlerList.unregisterAll(_classShop);
		}
	}

	public PartyManager getPartyManager()
	{
		return _partyManager;
	}

	public void addSpectator(Player player, boolean teleport) 
	{
		if (GetGame() == null)
			return;
		
		Clear(player);

		if (teleport)
			player.teleport(GetGame().GetSpectatorLocation());
		
		//Set Spec State
		player.setVelocity(new Vector(0,1,0));
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setFlySpeed(0.1f);
		((CraftPlayer) player).getHandle().spectating = true;
		((CraftPlayer) player).getHandle().k = false;

		GetCondition().Factory().Cloak("Spectator", player, player, 7777, true, true);
		
		//Game Team  
		GetGame().GetScoreboard().SetPlayerTeam(player, "SPEC");
	}

	public boolean isSpectator(Entity player) 
	{
		if (player instanceof Player)
			return UtilPlayer.isSpectator((Player)player);
		return false;
	}

	@EventHandler
	public void onSecond(UpdateEvent event)
	{
		Iterator<Entry<String, Long>> itel = _resourcePackNoResponse.entrySet().iterator();

		while (itel.hasNext())
		{
			Entry<String, Long> entry = itel.next();

			if (UtilTime.elapsed(entry.getValue(), 20000))
			{
				Player player = Bukkit.getPlayerExact(entry.getKey());

				if (player != null)
				{
					// Send it again, enforce it!
					_resourcePackNoResponse.put(player.getName(), System.currentTimeMillis());
					player.setResourcePack(_resourcePackUrl);
				}
				else
				{
					itel.remove();
				}
			}
		}
	}

	@EventHandler
	public void ResourcePackQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		if (_resourcePackUsers.containsKey(player.getName()) && _resourcePackUsers.get(player.getName()))
		{
			new RedisUnloadResPack(player.getName()).publish();

			_resourcePackUsers.remove(player.getName());
		}
	}

	@EventHandler
	public void outdatedVersion(GameStateChangeEvent event)
	{
		if (!_resourcePackRequired)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!UtilPlayer.is1_8(player))
				returnHubNoResPack(player, "You need to be using 1.8 to play " + GetGame().GetName() + "!");
		}
	}

	private void returnHubNoResPack(Player player)
	{
		player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 1f);
		GetPortal().sendPlayerToServer(player, "Lobby");
	}

	private void returnHubNoResPack(Player player, String message)
	{
		UtilPlayer.message(player, "  ");
		UtilPlayer.message(player, C.cGold + C.Bold + message);
		UtilPlayer.message(player, "  ");
		
		returnHubNoResPack(player);
	}

	@EventHandler
	public void ResourcePackJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		if (!UtilPlayer.is1_8(player) && _resourcePackRequired)
		{
			returnHubNoResPack(player, "You need to be using 1.8 to play " + GetGame().GetName() + "!");

			return;
		}

		if (_resourcePackUrl != null)
		{
			if (_resourcePackRequired)
			{
				_resourcePackNoResponse.put(player.getName(), System.currentTimeMillis());
			}

			_resourcePackUsers.put(player.getName(), false);
			player.setResourcePack(_resourcePackUrl);
		}
	}

	public void setResourcePack(String resourcePack, boolean forceResourcePack)
	{
		if (!Objects.equal(resourcePack, _resourcePackUrl) || forceResourcePack != _resourcePackRequired)
		{
			_resourcePackNoResponse.clear();
			_resourcePackUsers.clear();
			_resourcePackUrl = resourcePack == null || resourcePack.isEmpty() ? null : resourcePack;
			_resourcePackRequired = forceResourcePack;

			if (_resourcePackUrl == null || _resourcePackUrl.isEmpty())
			{
				_resourcePackRequired = false;

				for (Player player : Bukkit.getOnlinePlayers())
				{
					player.setResourcePack("http://www.chivebox.com/file/c/empty.zip");
				}
			}
			else
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (_resourcePackRequired)
					{
						_resourcePackNoResponse.put(player.getName(), System.currentTimeMillis());
					}
					
					_resourcePackUsers.put(player.getName(), false);
					player.setResourcePack(_resourcePackUrl);
				}
			}
		}
	}
}
