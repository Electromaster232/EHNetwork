package ehnetwork.game.survival;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.potion.PotionEffect;

import com.google.common.base.Objects;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.account.event.BrandListener;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.blood.Blood;
import ehnetwork.core.chat.Chat;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.jsonchat.ClickEvent;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.elo.EloManager;
import ehnetwork.core.energy.Energy;
import ehnetwork.core.explosion.Explosion;
import ehnetwork.core.hologram.HologramManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.notifier.NotificationManager;
import ehnetwork.core.packethandler.IPacketHandler;
import ehnetwork.core.packethandler.PacketHandler;
import ehnetwork.core.packethandler.PacketInfo;
import ehnetwork.core.packethandler.PacketPlayResourcePackStatus;
import ehnetwork.core.packethandler.PacketPlayResourcePackStatus.EnumResourcePackStatus;
import ehnetwork.core.party.PartyManager;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.projectile.ProjectileManager;
import ehnetwork.core.resourcepack.ResPackManager;
import ehnetwork.core.resourcepack.ResUnloadCheck;
import ehnetwork.core.resourcepack.redis.RedisUnloadResPack;
import ehnetwork.core.reward.RewardRarity;
import ehnetwork.core.reward.rewards.PetReward;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.status.ServerStatusManager;
import ehnetwork.core.task.TaskManager;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.core.timing.TimingManager;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.survival.commands.BuildRemoveCommand;
import ehnetwork.game.survival.commands.CosmeticsCommand;
import ehnetwork.game.survival.commands.DoubleJumpCommand;
import ehnetwork.game.survival.commands.FlyCommand;
import ehnetwork.game.survival.commands.GodCommand;
import ehnetwork.game.survival.commands.OofCommand;
import ehnetwork.game.survival.managers.ProtectionManager;
import ehnetwork.minecraft.game.classcombat.Class.ClassManager;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;
import ehnetwork.minecraft.game.classcombat.shop.ClassCombatShop;
import ehnetwork.minecraft.game.classcombat.shop.ClassShopManager;
import ehnetwork.minecraft.game.core.IRelation;
import ehnetwork.minecraft.game.core.condition.ConditionManager;
import ehnetwork.minecraft.game.core.damage.DamageManager;
import ehnetwork.minecraft.game.core.fire.Fire;
import ehnetwork.game.survival.commands.BuildAllowCommand;
import ehnetwork.game.survival.commands.BuildRequestCommand;
import ehnetwork.game.survival.commands.VanishCommand;
import ehnetwork.game.survival.managers.JumpManager;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.getWorlds;

public class SurvivalManager extends MiniPlugin implements IRelation
{
	// Modules
	private BlockRestore _blockRestore;
	private Blood _blood;
	private Chat _chat;
	private CoreClientManager _clientManager;
	private DisguiseManager _disguiseManager;
	private DonationManager _donationManager;
	private ConditionManager _conditionManager;
	private JumpManager _jumpManager;
	private PetManager _petManager;
	private Creature _creature;
	private DamageManager _damageManager;
	private Explosion _explosionManager;

	private Fire _fire;
	private ProjectileManager _projectileManager;

	private Portal _portal;

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
	private ServerStatusManager _serverStatusManager;
	private InventoryManager _inventoryManager;
	private CosmeticManager _cosmeticManager;
    private HologramManager _hologramManager;
	private AchievementManager _achievementManager;
	private StatsManager _statsManager;
	private PartyManager _partyManager;
	private PreferencesManager _preferencesManager;

	private BrandListener _brandListener;

	private TaskManager _taskManager;
    private PacketHandler _packetHandler;
    private ProtectionManager _protectionManager;


	private IPacketHandler _resourcePacketHandler;
	private String _resourcePackUrl;
	private boolean _resourcePackRequired;
	private NautHashMap<String, Boolean> _resourcePackUsers = new NautHashMap<String, Boolean>();
	private NautHashMap<String, Long> _resourcePackNoResponse = new NautHashMap<String, Long>();

	// Observers
	private HashSet<Player> _specList = new HashSet<Player>();

	// Server Games


	// Games

	//Youtuber Kits
	private HashSet<Player> _youtube = new HashSet<Player>();

	public SurvivalManager(Survival plugin, ServerStatusManager serverStatusManager,
						   CoreClientManager clientManager, DonationManager donationManager, DamageManager damageManager,
						   StatsManager statsManager, AchievementManager achievementManager, DisguiseManager disguiseManager, Creature creature, Teleport teleport, Blood blood, Chat chat,
						   Portal portal, PreferencesManager preferences, InventoryManager inventoryManager, PacketHandler packetHandler,
						   CosmeticManager cosmeticManager, ProjectileManager projectileManager, PetManager petManager, HologramManager hologramManager, String webAddress, BrandListener brandListener)
	{
		super("Game Manager", plugin);


		// Modules
		_blockRestore = new BlockRestore(plugin);
		_conditionManager = new ConditionManager(this.getPlugin());
		
		_blood = blood;
		_preferencesManager = preferences;

		_explosionManager = new Explosion(plugin, _blockRestore);
		_explosionManager.SetDebris(false);

		_clientManager = clientManager;
		_serverStatusManager = serverStatusManager;
		_chat = chat;
		_creature = creature;

		_damageManager = damageManager;
		//_damageManager.UseSimpleWeaponDamage = true;
		_damageManager.setConditionManager(_conditionManager);
		
		_disguiseManager = disguiseManager;

		_brandListener = brandListener;

		_protectionManager = new ProtectionManager(this);

		//_jumpManager = new JumpManager(this);

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
		_hologramManager = hologramManager;

		
		new NotificationManager(getPlugin(), clientManager);
		
		//Champions Modules
		//_energy = new Energy(plugin);
		
		_itemFactory = new ItemFactory(_plugin, _blockRestore, _conditionManager, damageManager, _energy,
				_fire, _projectileManager, webAddress);
		
		//_skillFactory = new SkillFactory(plugin, damageManager, this, _damageManager.GetCombatManager(),
		//		_conditionManager, _projectileManager, _disguiseManager, _blockRestore, _fire, new Movement(plugin), teleport,
		//		_energy, webAddress);
		
		//_classManager = new ClassManager(plugin, clientManager, donationManager, _skillFactory, _itemFactory,
		//		webAddress);

		//_classShopManager = new ClassShopManager(_plugin, _classManager, _skillFactory, _itemFactory, _achievementManager, clientManager);

		//_classShop = new ClassCombatShop(_classShopManager, clientManager, donationManager, false, "Class Shop");

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
		addCommand(new VanishCommand(this));
		addCommand(new FlyCommand(this));
		addCommand(new OofCommand(this));
		addCommand(new GodCommand(this));
		addCommand(new CosmeticsCommand(this));
		addCommand(new DoubleJumpCommand(this));
		// Build Commands
		addCommand(new BuildRequestCommand(this));
		addCommand(new BuildAllowCommand(this));
		addCommand(new BuildRemoveCommand(this));
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


	public TaskManager GetTaskManager()
	{
		return _taskManager;
	}


	public BrandListener GetBrandListener(){
		return _brandListener;
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
	public JumpManager GetJumpManager() {
		return _jumpManager;
	}

	public WorldGuardPlugin GetWorldGuard() {
		return WGBukkit.getPlugin();
	}


	@Override
	public boolean canHurt(String a, String b)
	{
		return canHurt(UtilPlayer.searchExact(a), UtilPlayer.searchExact(b));
	}

	public boolean canHurt(Player pA, Player pB)
	{
		return true;
	}

	@Override
	public boolean isSafe(Player player)
	{

		return true;
	}

	@EventHandler
	public void MessageMOTD(ServerListPingEvent event)
	{

	}

	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;

		//Dont Let Chat Near Spawn!

		Player player = event.getPlayer();
		String playerName = player.getName();


		Rank rank = GetClients().Get(player).GetRank();

		boolean ownsUltra = _donationManager.Get(player.getName()).OwnsUltraPackage();

		//Level Prefix
		String levelStr = _achievementManager.getMineplexLevel(player, rank);

		//Rank Prefix
		String rankStr = "";
		if  (rank != Rank.ALL)
			rankStr = rank.GetTag(true, true) + " ";

		if (ownsUltra && !rank.Has(Rank.ULTRA))
			rankStr = Rank.ULTRA.GetTag(true, true) + " ";

		else
		{
			for (Player other : UtilServer.getPlayers())
			{

				event.setMessage(event.getMessage());
				event.setFormat(levelStr + rankStr + C.cYellow + playerName + " " + C.cWhite + "%2$s");
			}
		}
	}

	@EventHandler
	public void MessageJoin(PlayerJoinEvent event)
	{
		String name = event.getPlayer().getName();

		if(!UtilPlayer.is1_8(event.getPlayer())){
			UtilPlayer.kick(event.getPlayer(), "Version", "Please rejoin using version 1.8 or greater!");
		}
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		_cosmeticManager.disableItemsForPlayer(event.getPlayer());

		event.setJoinMessage(F.sys("Join", name));
	}

	@EventHandler
	public void MessageQuit(PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();

		event.setQuitMessage(F.sys("Quit", name));

		// Threw this in here because the scheduler was giving me problems otherwise.
		_brandListener.removePlayerOnQuit(event.getPlayer());
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
			else if (_clientManager.Get(event.getPlayer().getName()).GetRank().Has(event.getPlayer(), Rank.ULTRA, false))
			{
				
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
	public void ObserverQuit(PlayerQuitEvent event)
	{
		_specList.remove(event.getPlayer());
	}

	public boolean IsObserver(Player player)
	{
		return _specList.contains(player);
	}
	
	public String GetHost()
	{
		return "SURV-1";
	}


	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		Location loc = event.getEntity().getBedSpawnLocation();
		Location loc2 = getWorlds().get(0).getSpawnLocation();
		if (loc == null){
			event.getEntity().teleport(loc2);
		}
		else{
			event.getEntity().teleport(loc);
		}
		if(GetClients().hasRank(event.getEntity(), Rank.LEGEND)){
			UtilPlayer.message(event.getEntity(), F.main("Death", "Use /back to return to your death location!"));
		}
	}


	@EventHandler(priority = EventPriority.LOW)
	public void scoreboardUpdate(UpdateEvent event){

	}

	public InventoryManager getInventoryManager()
	{
		return _inventoryManager;
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



	
	@EventHandler
	public void quitYoutuber(PlayerQuitEvent event)
	{
		_youtube.remove(event.getPlayer());
	}


	public void rewardPet(Player player, String pet, EntityType type)
	{
		if (!player.isOnline())
			return;
		
		PetReward reward = new PetReward(_petManager, _inventoryManager, _donationManager, pet, pet, type, RewardRarity.OTHER, 0);
		
		if (reward.canGiveReward(player))
			reward.giveReward(null, player);
	}


	public PartyManager getPartyManager()
	{
		return _partyManager;
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
