package mineplex.hub;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import mineplex.core.MiniClientPlugin;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.aprilfools.AprilFoolsManager;
import mineplex.core.benefit.BenefitManager;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.cosmetic.CosmeticManager;
import mineplex.core.disguise.DisguiseManager;
import mineplex.core.disguise.disguises.DisguiseSlime;
import mineplex.core.donation.DonationManager;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.event.GadgetActivateEvent;
import mineplex.core.gadget.event.GadgetCollideEntityEvent;
import mineplex.core.hologram.HologramManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.message.PrivateMessageEvent;
import mineplex.core.mount.MountManager;
import mineplex.core.mount.event.MountActivateEvent;
import mineplex.core.notifier.NotificationManager;
import mineplex.core.party.Party;
import mineplex.core.party.PartyManager;
import mineplex.core.pet.PetManager;
import mineplex.core.portal.Portal;
import mineplex.core.preferences.PreferencesManager;
import mineplex.core.projectile.ProjectileManager;
import mineplex.core.stats.StatsManager;
import mineplex.core.task.TaskManager;
import mineplex.core.treasure.TreasureManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.commands.ForcefieldRadius;
import mineplex.hub.commands.GadgetToggle;
import mineplex.hub.commands.GameModeCommand;
import mineplex.hub.commands.NewsCommand;
import mineplex.hub.modules.ForcefieldManager;
import mineplex.hub.modules.HubVisibilityManager;
import mineplex.hub.modules.JumpManager;
import mineplex.hub.modules.NewsManager;
import mineplex.hub.modules.ParkourManager;
import mineplex.hub.modules.TextManager;
import mineplex.hub.modules.WorldManager;
import mineplex.hub.poll.PollManager;
import mineplex.hub.tutorial.TutorialManager;
import mineplex.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;
import mineplex.minecraft.game.classcombat.item.event.ItemTriggerEvent;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;

public class HubManager extends MiniClientPlugin<HubClient>
{
	// ☃❅ Snowman!
	public HubType Type = HubType.Normal;
  
	private BlockRestore _blockRestore;
	private CoreClientManager _clientManager;
	private ConditionManager _conditionManager;
	private DonationManager _donationManager;
	private DisguiseManager _disguiseManager;
	private PartyManager _partyManager;   
	private ForcefieldManager _forcefieldManager; 
	private Portal _portal;  
	private StatsManager _statsManager; 
	private GadgetManager _gadgetManager;
	private MountManager _mountManager;
	private HubVisibilityManager _visibilityManager; 
	private TutorialManager _tutorialManager;  
	private TextManager _textCreator;
	private ParkourManager _parkour;   
	private PreferencesManager _preferences; 
	private InventoryManager _inventoryManager;
	private NewsManager _news;
	private AchievementManager _achievementManager;
	private TreasureManager _treasureManager;
	private PetManager _petManager;

	private Location _spawn;
	private int _scoreboardTick = 0;

	private HashMap<Player, Scoreboard> _scoreboards = new HashMap<Player, Scoreboard>();

	private String _pigStacker = "0 - Nobody"; 
	private String _serverName = "";
	 
	private ItemStack _ruleBook = null;

	private boolean _shuttingDown;

	private HashMap<String, Long> _portalTime = new HashMap<String, Long>();
	
	private HashMap<String, ArrayList<String>> _creativeAdmin = new HashMap<String, ArrayList<String>>();

	//Admin
	private boolean _gadgetsEnabled = true;

	public HubManager(JavaPlugin plugin, BlockRestore blockRestore, CoreClientManager clientManager, DonationManager donationManager, ConditionManager conditionManager, DisguiseManager disguiseManager, TaskManager taskManager, Portal portal, PartyManager partyManager, PreferencesManager preferences, PetManager petManager, PollManager pollManager, StatsManager statsManager, AchievementManager achievementManager, HologramManager hologramManager)
	{
		super("Hub Manager", plugin);

		_blockRestore = blockRestore;
		_clientManager = clientManager;
		_conditionManager = conditionManager;
		_donationManager = donationManager;
		_disguiseManager = disguiseManager;
		
		_portal = portal;

		_spawn = new Location(UtilWorld.getWorld("world"), 0.5, 74, 0.5);
		// Disable item merging
		((CraftWorld) _spawn.getWorld()).getHandle().spigotConfig.itemMerge = 0;

		_textCreator = new TextManager(this);
		_parkour = new ParkourManager(this, donationManager, taskManager);

		new WorldManager(this);
		new JumpManager(this);
		//new TournamentInviter(this);
		    
		_news = new NewsManager(this);

		_mountManager = new MountManager(_plugin, clientManager, donationManager, blockRestore, _disguiseManager);
		_inventoryManager = new InventoryManager(plugin, clientManager);
		new BenefitManager(plugin, clientManager, _inventoryManager);
		_gadgetManager = new GadgetManager(_plugin, clientManager, donationManager, _inventoryManager, _mountManager, petManager, preferences, disguiseManager, blockRestore, new ProjectileManager(plugin));
		_treasureManager = new TreasureManager(_plugin, clientManager, donationManager, _inventoryManager, petManager, _blockRestore, hologramManager);
		new CosmeticManager(_plugin, clientManager, donationManager, _inventoryManager, _gadgetManager, _mountManager, petManager, _treasureManager);

		_petManager = petManager;
		_partyManager = partyManager;
		_preferences = preferences;
		_tutorialManager = new TutorialManager(this, donationManager, taskManager, _textCreator);
		_visibilityManager = new HubVisibilityManager(this);
		
		_forcefieldManager = new ForcefieldManager(this);
		addCommand(new ForcefieldRadius(_forcefieldManager));
		
		_statsManager = statsManager;
		_achievementManager = achievementManager;
		_achievementManager.setGiveInterfaceItem(true);
		
		new NotificationManager(getPlugin(), clientManager);

		((CraftWorld)Bukkit.getWorlds().get(0)).getHandle().pvpMode = true;

//		NotificationManager notificationManager = new NotificationManager(plugin, clientManager, donationManager);
//		new MailManager(_plugin, notificationManager);
		
		_ruleBook = ItemStackFactory.Instance.CreateStack(Material.WRITTEN_BOOK, (byte)0, 1, ChatColor.GREEN + "Rule Book", new String[] { });
		BookMeta meta = (BookMeta)_ruleBook.getItemMeta();
		_serverName = getPlugin().getConfig().getString("serverstatus.name");
		_serverName = _serverName.substring(0, Math.min(16,  _serverName.length()));

		meta.addPage("§m-------------------§r\n"
				+ "Welcome to §6§lEHPlex§r\n"
				+ "§r§0§l§r§m§0§m-------------------§r§0\n"
				+ "\n"
				+ "§2Please §0take a moment to read through this book!\n"
				+ "\n"
				+ "\n"
				+ "Part 1 - Rules\n"
				+ "\n"
				+ "Part 2 - FAQ\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§l1.§§§r §4No§r spamming.\n"
				+ "\n"
				+ "§0This is sending too many messages and/or repeating the same message in a short period of time.\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§l2.§m§r §4No§0 use of excessive caps.\n"
				+ "\n"
				+ "This is sending messages with an excessive amount of capital letters.\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§l3.§r §4No§0 hacking or use of any unapproved mods.\n"
				+ "\n"
				+ "This means we do not tolerate any sort of hacked client or any unapproved mods, such as fly hacks.\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§l4.§r §4No§0 advertising non-Mineplex related links.\n"
				+ "\n"
				+ "This is when a link is sent in chat which directs others to non-Mineplex related content.\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§l5.§r §4No§0 trolling or use of any exploits.\n"
				+ "\n"
				+ "This means that abuse of bugs/glitches is not tolerated. You also may not do things such as teamkilling and/or blocking spawns.\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§l6.§r §2Be§0 respectful to others, yourself, and the environment around you.\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lRules§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§rPlease report any bugs, exploits, and/or rule breakers on our forums with evidence.\n"
				+ "§omineplex.com/forums\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lFAQ§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§lWhat is stacker and how do you play it?\n"
				+ "\n"
				+ "§rStacker is a hub game where you can stack & throw players/mobs.\n"
				+ "\n"
				+ "§9Right-Click: pick up\n"
				+ "Left-Click: throw\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lFAQ§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§lHow do I get §bUltra§l, §5Hero§l, or §aLegend§l?\n"
				+ "\n"
				+ "§r§0You are able to request these ranks through on our Discord at:\n"
				+ "\n"
				+ "§odiscord.gg/FttmSEQ\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lFAQ§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§lWhy hasn't my rank been applied yet?\n"
				+ "\n"
				+ "§m§rYour rank may take a while to be applied. If it has been over 24 hours please contact:\n"
				+ "\n"
				+ "§osupport.theendlessweb.com\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lFAQ§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§lWhat do I do if I was wrongfully punished?\n"
				+ "\n"
				+ "§0If you believe you were wrongfully punished, please submit an appeal at:\n"
				+ "\n"
				+ "§osupport.theendlessweb.com\n");

		meta.addPage("§m-------------------\n"
				+ "§r          §2§lFAQ§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "§lHow do I apply for Trainee?\n"
				+ "\n"
				+ "§rYou may only apply for Trainee if you have §bUltra§0, §5Hero§0, or §aLegend§0.\n"
				+ "Apply at:\n"
				+ "§osupport.theendlessweb.com\n");

		meta.addPage("§m-------------------\n"
				+ "§r   §6§lThank you for \n"
				+ "      reading!§r§0\n"
				+ "§m-------------------\n"
				+ "§r\n"
				+ "Remember to visit our website §2theendlessweb.com§0 for important news & updates!\n"
				+ "\n"
				+ "\n"
				+ "§c§lH§6§lA§a§lV§9§lE §c§lF§6§lU§a§lN§9§l!\n");

		// These are needed or 1.8 clients will not show book correctly
		meta.setTitle("Rule Book");
		meta.setAuthor("Endless Hosting");

		_ruleBook.setItemMeta(meta);
	}

	@Override
	public void addCommands()
	{
		addCommand(new GadgetToggle(this));
		addCommand(new NewsCommand(this));
		addCommand(new GameModeCommand(this));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void reflectMotd(ServerListPingEvent event)
	{
		if (_shuttingDown)
		{
			event.setMotd("Restarting soon");
		}
	}
	
	public boolean BumpDisabled(Entity ent)
	{
		if (ent == null)
			return false;

		if (ent instanceof Player)
		{
			return !_preferences.Get((Player)ent).HubGames;
		}
		
		return true;
	}

	
	@EventHandler
	public void SnowballPickup(BlockDamageEvent event)
	{
		if (Type != HubType.Christmas)
			return;
		
		if (event.getBlock().getType() != Material.SNOW)
			return;
		
		Player player = event.getPlayer();
		
		_gadgetManager.RemoveItem(player);
		
		player.getInventory().setItem(3, new ItemStack(Material.SNOW_BALL, 16));
	}
	
	@EventHandler
	public void SnowballHit(CustomDamageEvent event)
	{
		if (Type != HubType.Christmas)
			return;
		
		Projectile proj = event.GetProjectile();
		if (proj == null)	return;

		if (!(proj instanceof Snowball))
			return;
		
		event.SetCancelled("Snowball Cancel");
		
		if (BumpDisabled(event.GetDamageeEntity()))
			return;
		
		if (BumpDisabled(event.GetDamagerEntity(true)))
			return;
		
		UtilAction.velocity(event.GetDamageeEntity(), UtilAlg.getTrajectory2d(event.GetDamagerEntity(true), event.GetDamageeEntity()), 
				0.4, false, 0, 0.2, 1, false);
		
		//No Portal
		SetPortalDelay(event.GetDamageeEntity());
	}
	
	@EventHandler
	public void redirectStopCommand(PlayerCommandPreprocessEvent event)
	{
		if (event.getPlayer().isOp() && event.getMessage().equalsIgnoreCase("/stop"))
		{
			_shuttingDown = true;

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
			{
				public void run()
				{
					_portal.sendAllPlayers("Lobby");

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
					{
						public void run()
						{
							Bukkit.shutdown();
						}
					}, 40L);
				}
			}, 60L);

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void preventEggSpawn(ItemSpawnEvent event)
	{
		if (event.getEntity() instanceof Egg)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnChunkLoad(ChunkLoadEvent event)
	{
		for (Entity entity : event.getChunk().getEntities())
		{
			if (entity instanceof LivingEntity)
			{
				if (((LivingEntity)entity).isCustomNameVisible() && ((LivingEntity)entity).getCustomName() != null)
				{
					if (ChatColor.stripColor(((LivingEntity)entity).getCustomName()).equalsIgnoreCase("Block Hunt"))
					{
						DisguiseSlime disguise = new DisguiseSlime(entity);
						disguise.setCustomNameVisible(true);
						disguise.setName(((LivingEntity)entity).getCustomName(), null);
						disguise.SetSize(2);
						_disguiseManager.disguise(disguise);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void login(final PlayerLoginEvent event)
	{
        CoreClient client = _clientManager.Get(event.getPlayer().getName());

        // Reserved Slot Check
		if (Bukkit.getOnlinePlayers().size() - Bukkit.getServer().getMaxPlayers() >= 20)
		{
			if (!client.GetRank().Has(Rank.ULTRA))
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						_portal.sendPlayerToServer(event.getPlayer(), "Lobby");
					}
				});

				event.allow();
			}
		}
		else
			event.allow();
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
	
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(GetSpawn().add(0, 10, 0));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		String playerName = player.getName();

		// April Fools
		if (AprilFoolsManager.Instance.isActive())
			playerName = AprilFoolsManager.Instance.getName(player);

		//Public Message
		event.setJoinMessage(null);
		
//		if (_clientManager.Get(player).GetRank() != Rank.LEGEND)
//			event.setJoinMessage(null);
//		else
//		{
//			event.setJoinMessage(C.cGreen + C.Bold + "Legend " + playerName + " has joined!");
//		}
				
		//Teleport
		player.teleport(GetSpawn().add(0, 10, 0));
		
		//Survival
		player.setGameMode(GameMode.SURVIVAL);
		
		//Clear Inv
		UtilInv.Clear(player);
		
		//Allow Double Jump
		player.setAllowFlight(true);
		
		//Health
		player.setHealth(20);

		//Rules
		player.getInventory().setItem(2, _ruleBook);
		
		//Scoreboard
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(board);
		_scoreboards.put(player, board);

		//Objective
		Objective obj = board.registerNewObjective(C.Bold + "Player Data", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		for (Rank rank : Rank.values())
		{
			if (rank != Rank.ALL)
				board.registerNewTeam(rank.Name).setPrefix(rank.GetTag(true, true) + ChatColor.RESET + " ");
			else
				board.registerNewTeam(rank.Name).setPrefix("");
		}

		for (Player otherPlayer : Bukkit.getOnlinePlayers())
		{
			if (_clientManager.Get(otherPlayer) == null)
				continue;

			String rankName = _clientManager.Get(player).GetRank().Name;
			String otherRankName = _clientManager.Get(otherPlayer).GetRank().Name;

			if (!_clientManager.Get(player).GetRank().Has(Rank.ULTRA) && _donationManager.Get(player.getName()).OwnsUltraPackage())
			{
				rankName = Rank.ULTRA.Name;
			}

			if (!_clientManager.Get(otherPlayer).GetRank().Has(Rank.ULTRA) && _donationManager.Get(otherPlayer.getName()).OwnsUltraPackage())
			{
				otherRankName = Rank.ULTRA.Name;
			}

			//Add Other to Self
			board.getTeam(otherRankName).addPlayer(otherPlayer);

			//Add Self to Other
			otherPlayer.getScoreboard().getTeam(rankName).addPlayer(player);
		}
	}

	
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event)
	{
		//XXX make this work > event.setKeepInventory(true);
		event.getDrops().clear();
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(null);

		event.getPlayer().leaveVehicle();
		event.getPlayer().eject();

		for (Player player : UtilServer.getPlayers())
			player.getScoreboard().resetScores(event.getPlayer().getName());

		_scoreboards.remove(event.getPlayer());

		_portalTime.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void playerPrivateMessage(PrivateMessageEvent event)
	{
		//Dont Let PM Near Spawn!
		if (UtilMath.offset2d(GetSpawn(), event.getSender().getLocation()) == 0 && !_clientManager.Get(event.getSender()).GetRank().Has(Rank.HELPER))
		{
			UtilPlayer.message(event.getSender(), F.main("Chat", "You must leave spawn before you can Private Message!"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;
		
		//Dont Let Chat Near Spawn!
		if (UtilMath.offset2d(GetSpawn(), event.getPlayer().getLocation()) == 0 && !_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.HELPER))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Chat", "You must leave spawn before you can chat!"));
			event.setCancelled(true);
			return;
		}
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		// April Fools
		if (AprilFoolsManager.Instance.isActive())
			playerName = AprilFoolsManager.Instance.getName(player);

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

		//Party Chat
		if (event.getMessage().charAt(0) == '@')
		{
			Party party = _partyManager.GetParty(player);
			if (party != null)
			{
				event.getRecipients().clear();

				event.setMessage(event.getMessage().substring(1, event.getMessage().length()));
				event.setFormat(levelStr + C.cDPurple + C.Bold + "Party " + C.cWhite + C.Bold + playerName + " " + C.cPurple + "%2$s");

				for (String name : party.GetPlayers())
				{
					Player other = UtilPlayer.searchExact(name);

					if (other != null)
						event.getRecipients().add(other);
				}
			}
			else
			{
				UtilPlayer.message(player, F.main("Party", "You are not in a Party."));
				event.setCancelled(true);
			}

			return;
		} 
		else
		{
			for (Player other : UtilServer.getPlayers())
			{
				if (_tutorialManager.InTutorial(other))
				{
					event.getRecipients().remove(other);
					continue;
				}

				event.setMessage(event.getMessage());
				event.setFormat(levelStr + rankStr + C.cYellow + playerName + " " + C.cWhite + "%2$s");
			}
		}
	}

	@EventHandler
	public void Damage(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.VOID)
			if (event.getEntity() instanceof Player)
			{
				event.getEntity().eject();
				event.getEntity().leaveVehicle();
				event.getEntity().teleport(GetSpawn());
			}

			else
				event.getEntity().remove();

		event.setCancelled(true);
	}


	@EventHandler
	public void FoodHealthUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			player.setFoodLevel(20);
			player.setExhaustion(0f);
			player.setSaturation(3f);
		}
	}

	@EventHandler
	public void InventoryCancel(InventoryClickEvent event)
	{
		if (event.getWhoClicked() instanceof Player && ((Player)event.getWhoClicked()).getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler
	public void UpdateScoreboard(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		_scoreboardTick = (_scoreboardTick + 1)%3;

		if (_scoreboardTick != 0)
			return;

//		int bestPig = 0;
//		for (Player player : UtilServer.getPlayers())
//		{
//			if (player.getVehicle() != null)
//				continue;
//
//			int count = 0;
//
//			Entity ent = player;
//			while (ent.getPassenger() != null)
//			{
//				ent = ent.getPassenger();
//				count++;
//			}
//
//			if (count > bestPig)
//			{
//				_pigStacker = player.getName();
//				bestPig = count;
//			}
//		}
//		if (bestPig == 0)
//		{
//			_pigStacker = "0 - Nobody";
//		}
//		else
//		{
//			_pigStacker = bestPig + " - " + _pigStacker;
//
//			if (_pigStacker.length() > 16)
//				_pigStacker = _pigStacker.substring(0, 16);
//		}

		for (Player player : UtilServer.getPlayers())
		{
			//Dont Waste Time
			if (_partyManager.GetParty(player) != null)
				continue;

			//Return to Main Scoreboard
			if (!player.getScoreboard().equals(_scoreboards.get(player)))
				player.setScoreboard(_scoreboards.get(player));

			//Objective
			Objective obj = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

			//Title
			obj.setDisplayName(C.cWhite + C.Bold + Get(player).GetScoreboardText());

			int line = 15;
			
			//Stacker
			obj.getScore(C.cAqua + C.Bold + "Server").setScore(line--);
			obj.getScore(_serverName).setScore(line--);
			
			//Space
			obj.getScore("  ").setScore(line--);
			
			//Gems
			obj.getScore(C.cGreen + C.Bold +  "Gems").setScore(line--);
			// Remove Old/Add New
			player.getScoreboard().resetScores(Get(player.getName()).GetLastGemCount() + "");
			obj.getScore(GetDonation().Get(player.getName()).GetGems() + "").setScore(line--);
			Get(player.getName()).SetLastGemCount(GetDonation().Get(player.getName()).GetGems());

			//Space
			obj.getScore("   ").setScore(line--);

			//Coins
			obj.getScore(C.cYellow + C.Bold +  "Coins").setScore(line--);

			// Remove Old
			player.getScoreboard().resetScores(Get(player.getName()).GetLastCoinCount() + "");
			// Add New			
			obj.getScore(GetDonation().Get(player.getName()).getCoins() + "").setScore(line--);

			Get(player.getName()).SetLastCoinCount(GetDonation().Get(player.getName()).getCoins());
			

			//Space
			obj.getScore("    ").setScore(line--);
 
			
			//Display Rank
			obj.getScore(C.cGold + C.Bold + "Rank").setScore(line--);
			if (GetClients().Get(player).GetRank().Has(Rank.ULTRA))
				obj.getScore(GetClients().Get(player).GetRank().Name).setScore(line--);
			else if (GetDonation().Get(player.getName()).OwnsUnknownPackage("SuperSmashMobs ULTRA") ||
					GetDonation().Get(player.getName()).OwnsUnknownPackage("Survival Games ULTRA") ||
					GetDonation().Get(player.getName()).OwnsUnknownPackage("Minigames ULTRA") ||
					GetDonation().Get(player.getName()).OwnsUnknownPackage("CastleSiege ULTRA") ||
					GetDonation().Get(player.getName()).OwnsUnknownPackage("Champions ULTRA"))
				obj.getScore("Single Ultra").setScore(line--);
			else
				obj.getScore("No Rank").setScore(line--);
				

			//Space
			obj.getScore("     ").setScore(line--);

			//Website
			obj.getScore(C.cRed + C.Bold + "Website").setScore(line--);
			obj.getScore("www.endl.site").setScore(line--);
			obj.getScore("----------------").setScore(line--);
		}
	}

	@Override
	protected HubClient AddPlayer(String player)
	{
		return new HubClient(player);
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

	public DonationManager GetDonation()
	{
		return _donationManager;
	}

	public DisguiseManager GetDisguise()
	{
		return _disguiseManager;
	}

	public GadgetManager GetGadget()
	{
		return _gadgetManager;
	}

	public TreasureManager GetTreasure()
	{
		return _treasureManager;
	}

	public MountManager GetMount()
	{
		return _mountManager;
	}

	public ParkourManager GetParkour()
	{
		return _parkour;
	}

	public PreferencesManager getPreferences()
	{
		return _preferences;
	}
	
	public Location GetSpawn()
	{
		return _spawn.clone();
	}
	
	public PetManager getPetManager()
	{
	    return _petManager;
	}

	public TutorialManager GetTutorial()
	{
		return _tutorialManager;
	}

	public StatsManager GetStats()
	{
		return _statsManager;
	}

	public HubVisibilityManager GetVisibility()
	{
		return _visibilityManager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void gadgetCollide(GadgetCollideEntityEvent event)
	{
		if (!event.isCancelled())
			SetPortalDelay(event.getOther());
	}

	public void SetPortalDelay(Entity ent)
	{
		if (ent instanceof Player)
			_portalTime.put(((Player)ent).getName(), System.currentTimeMillis());
	}

	public boolean CanPortal(Player player)
	{
		//Riding
		if (player.getVehicle() != null || player.getPassenger() != null)
			return false;

		//Portal Delay
		if (!_portalTime.containsKey(player.getName()))
			return true;

		return UtilTime.elapsed(_portalTime.get(player.getName()), 5000);
	}

	public boolean CanBump(LivingEntity ent)
	{
		if (!(ent instanceof Player))
			return true;

		if (BumpDisabled(ent))
			return false;

		if (!getPreferences().Get((Player)ent).ShowPlayers)
			return false;

		return true;
	}

	@EventHandler
	public void SkillTrigger(SkillTriggerEvent event)
	{
		event.SetCancelled(true);
	}

	@EventHandler
	public void ItemTrigger(ItemTriggerEvent event)
	{
		event.SetCancelled(true);
	}

	public boolean IsGadgetEnabled()
	{
		return _gadgetsEnabled;
	}

	
	public NewsManager GetNewsManager()
	{
		return _news;
	}

	@EventHandler
	public void ignoreVelocity(PlayerVelocityEvent event)
	{
		if (_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.MODERATOR) && _preferences.Get(event.getPlayer()).IgnoreVelocity)
		{
			event.setCancelled(true);
		}
	}

	public void ToggleGadget(Player caller)
	{
		_gadgetsEnabled = !_gadgetsEnabled;
		
		if (!_gadgetsEnabled)
		{
			GetMount().DisableAll();
			GetGadget().DisableAll();
		}
		
		for (Player player : UtilServer.getPlayers())
			player.sendMessage(C.cWhite + C.Bold + "Gadgets/Mounts are now " + F.elem(_gadgetsEnabled ? C.cGreen + C.Bold + "Enabled" : C.cRed + C.Bold + "Disabled"));
	}
	
	@EventHandler
	public void GadgetActivate(GadgetActivateEvent event)
	{
		if (!_gadgetsEnabled)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void MountActivate(MountActivateEvent event)
	{
		if (!_gadgetsEnabled)
			event.setCancelled(true);
	}

	public void addGameMode(Player caller, Player target)
	{
		if (!_creativeAdmin.containsKey(caller.getName()))
			_creativeAdmin.put(caller.getName(), new ArrayList<String>());
		
		if (target.getGameMode() == GameMode.CREATIVE)
		{
			_creativeAdmin.get(caller.getName()).add(target.getName());
		}
		else
		{
			_creativeAdmin.get(caller.getName()).remove(target.getName());
		}
	}
	
	@EventHandler
	public void clearGameMode(PlayerQuitEvent event)
	{
		ArrayList<String> creative = _creativeAdmin.remove(event.getPlayer().getName());
		
		if (creative == null)
			return;
		
		for (String name : creative)
		{
			Player player = UtilPlayer.searchExact(name);
			if (player == null)
				continue;
			
			player.setGameMode(GameMode.SURVIVAL);
			
			UtilPlayer.message(player, F.main("Game Mode", event.getPlayer().getName() + " left the game. Creative Mode: " + F.tf(false)));
		}
	}
}
