package ehnetwork.game.microgames.game.games.minestrike;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.TeamNameTagVisibility;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_7_R4.EntityArrow;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.disguise.disguises.DisguisePlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.recharge.RechargedEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.events.PlayerKitGiveEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.GameTeam.PlayerState;
import ehnetwork.game.microgames.game.TeamGame;
import ehnetwork.game.microgames.game.games.minestrike.data.Bomb;
import ehnetwork.game.microgames.game.games.minestrike.data.Bullet;
import ehnetwork.game.microgames.game.games.minestrike.items.StrikeItem;
import ehnetwork.game.microgames.game.games.minestrike.items.StrikeItemType;
import ehnetwork.game.microgames.game.games.minestrike.items.equipment.armor.Armor;
import ehnetwork.game.microgames.game.games.minestrike.items.grenades.Grenade;
import ehnetwork.game.microgames.game.games.minestrike.items.guns.Gun;
import ehnetwork.game.microgames.game.games.minestrike.items.guns.GunStats;
import ehnetwork.game.microgames.game.games.minestrike.items.guns.GunType;
import ehnetwork.game.microgames.game.games.minestrike.kits.KitPlayer;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.stats.KaboomStatTracker;
import ehnetwork.game.microgames.stats.KillAllOpposingMineStrikeRoundStatTracker;
import ehnetwork.game.microgames.stats.KillFastStatTracker;
import ehnetwork.game.microgames.stats.KillReasonStatTracker;
import ehnetwork.game.microgames.stats.KillsWithConditionStatTracker;
import ehnetwork.game.microgames.stats.MineStrikeLastAliveKillStatTracker;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MineStrike extends TeamGame
{
	public static class PlayerHeadshotEvent extends PlayerEvent
	{
		private static final HandlerList handlers = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return handlers;
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}

		private final Player _shooter;

		public PlayerHeadshotEvent(Player who, Player shooter)
		{
			super(who);

			_shooter = shooter;
		}

		public Player getShooter()
		{
			return _shooter;
		}
	}

	public static class RoundOverEvent extends Event
	{
		private static final HandlerList handlers = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return handlers;
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}

		private final Game _game;

		public RoundOverEvent(Game game)
		{
			_game = game;
		}

		public Game getGame()
		{
			return _game;
		}
	}

	//Managers
	private ShopManager _shopManager;

	//Data
	private int _roundsToWin = 8;
	private long _roundTime = 120000;

	private boolean _debug = false;
	private int _bulletInstant = 2;	//0 = Slow, 1 = Instant, 2 = Mix
	private boolean _customHitbox = true;
	private boolean _bulletAlternate = false;

	//Map Data
	private ArrayList<Location> _bombSites;

	//Ongoing Data
	private HashMap<Gun, Player> _gunsEquipped = new HashMap<Gun, Player>();
	private HashMap<Grenade, Player> _grenadesEquipped = new HashMap<Grenade, Player>();

	private HashMap<GameTeam, Integer> _score = new HashMap<GameTeam, Integer>();

	private HashMap<Player, DisguisePlayer> _disguise = new HashMap<Player, DisguisePlayer>();

	//Round Data (wiped at end of each round)
	private HashMap<Entity, Gun> _gunsDropped = new HashMap<Entity, Gun>();
	private HashMap<Entity, Grenade> _grenadesDropped = new HashMap<Entity, Grenade>();

	private HashMap<Entity, Bullet> _bullets = new HashMap<Entity, Bullet>();
	private HashMap<Entity, Grenade> _grenadesThrown = new HashMap<Entity, Grenade>();

	private HashSet<Entity> _defusalDropped = new HashSet<Entity>();

	private HashMap<Location, Long> _incendiary = new HashMap<Location, Long>();

	private Bomb _bomb = null;
	private Item _bombItem = null;
	private Player _bombHolder = null;

	private Player _bombPlanter;
	private Player _bombDefuser;
	private Player _bombPlantedBy;

	private boolean _bombScoreboardFlash = false;

	private HashMap<Player, ItemStack> _scoped = new HashMap<Player, ItemStack>();

	//Round Data
	private String _winText = null;
	private boolean _roundOver = false;
	private int _freezeTime = 0;

	//Money Data
	private boolean _bombPlanted = false;
	private boolean _ctWonLast = false;
	private int _winStreak = 0;

	//Scoreboard
	private Objective _scoreObj;

	public MineStrike(MicroGamesManager manager)
	{
		super(manager, GameType.MineStrike,

				new Kit[] 
						{ 
				new KitPlayer(manager),
						},

						new String[]
								{
				C.cAqua + "SWAT" + C.cWhite + "  Defend the Bomb Sites",
				C.cAqua + "SWAT" + C.cWhite + "  Kill the Bombers",
				" ",
				C.cRed + "Bombers" + C.cWhite + "  Plant the Bomb at Bomb Site",
				C.cRed + "Bombers" + C.cWhite + "  Kill the SWAT Team",
								});

		_shopManager = new ShopManager(this);

		this.StrictAntiHack = true;
		
		this.HungerSet = 20;

		this.ItemDrop = true;

		this.InventoryClick = true;

		this.JoinInProgress = true;
		
		this.VersionRequire1_8 = true;
		
		this.DontAllowOverfill = true;

		_scoreObj = Scoreboard.GetScoreboard().registerNewObjective("HP", "dummy");
		_scoreObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		this._help = new String[] 
				{
				//"Tap Crouch when close to an ally to Boost",
				"Open Inventory at spawn to buy guns",
				"Hold Right-Click to Plant Bomb",
				"Look at the Bomb to Defuse Bomb",
				"Moving decreases accuracy",
				"Sprinting heavily decreases accuracy",
				"Jumping massively decreases accuracy",
				"Crouching increases accuracy",
				"Left-Click to roll Grenades",
				"Right-Click to throw Grenades",
				"Burst Fire for greater accuracy",
				"Sniper Rifles are only accurate while scoped",
				"Rifles have 30% recoil reduction while scoped",
				"Pick up better weapons from dead players"
				};

		registerStatTrackers(
				new KillReasonStatTracker(this, "Headshot", "BoomHeadshot", true),
				new KillAllOpposingMineStrikeRoundStatTracker(this),
				new KaboomStatTracker(this),
				new KillReasonStatTracker(this, "Backstab", "Assassination", false),
				new MineStrikeLastAliveKillStatTracker(this),
				new KillFastStatTracker(this, 4, 5, "KillingSpree"),
				new KillsWithConditionStatTracker(this, "Blindfolded", ConditionType.BLINDNESS, "Flash Bang", 2)
		);
	}

	@Override
	public void ParseData() 
	{
		_bombSites = WorldData.GetDataLocs("RED");
	}

	@EventHandler
	public void CustomTeamGeneration(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Recruit)
			return;
		
		this.GetTeamList().get(0).SetColor(ChatColor.AQUA);
		this.GetTeamList().get(0).SetName("SWAT");

		this.GetTeamList().get(1).SetColor(ChatColor.RED);
		this.GetTeamList().get(1).SetName("Bombers");
	}
	
	@EventHandler
	public void SetScoreboardNameVisibility(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;
		
		System.out.println("Hiding Scoreboard Nametags for Other Teams");
		for (Team curTeam : Scoreboard.GetScoreboard().getTeams())
		{
			curTeam.setNameTagVisibility(TeamNameTagVisibility.HIDE_FOR_OTHER_TEAMS);
			//UtilServer.getServer().dispatchCommand(UtilServer.getServer().getConsoleSender(), 
			//		"scoreboard teams option " + curTeam.getName() + " nametagVisibility hideForOtherTeams");
		}
	}

	@EventHandler
	public void giveStartEquipment(PlayerKitGiveEvent event)
	{
		GameTeam team = GetTeam(event.GetPlayer());
		if (team == null)
			return;

		if (team.GetColor() == ChatColor.RED)
		{
			if (IsAlive(event.GetPlayer()))
			{
				//Pistol
				Gun gun = new Gun(GunStats.GLOCK_18);
				registerGun(gun, event.GetPlayer());
				gun.giveToPlayer(event.GetPlayer(), true);

				//Knife
				event.GetPlayer().getInventory().setItem(2, ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, "Knife"));	

				//Armor
				giveTeamArmor(event.GetPlayer(), Color.fromRGB(255, 0, 0));
			}
		}
		else if (team.GetColor() == ChatColor.AQUA)
		{
			if (IsAlive(event.GetPlayer()))
			{
				//Pistol
				Gun gun = new Gun(GunStats.P2000);
				registerGun(gun, event.GetPlayer());
				gun.giveToPlayer(event.GetPlayer(), true);

				//Knife
				event.GetPlayer().getInventory().setItem(2, ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, "Knife"));

				//Armor
				giveTeamArmor(event.GetPlayer(), Color.fromRGB(0, 0, 255));
			}
		}

		//Enter Shop
		_shopManager.enterShop(event.GetPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerDeath(PlayerDeathEvent event)
	{
		//Dont Get Hit By Bullets
		((CraftPlayer) event.getEntity()).getHandle().spectating = true;
	}

//	public void disguiseSneak(Player player, GameTeam team)
//	{
//		//Create Disguise
//		if (!_disguise.containsKey(player))
//		{
//			_disguise.put(player, new DisguisePlayer(player, ((CraftPlayer)player).getProfile()));
//		}
//
//		DisguisePlayer disguise = _disguise.get(player);
//		disguise.setSneaking(true);
//		Manager.GetDisguise().disguise(_disguise.get(player));
//
//		for (Player other : UtilServer.getPlayers())
//		{
//			if (team.HasPlayer(other))
//			{
//				Manager.GetDisguise().removeViewerToDisguise(disguise, other);
//			}
//			else
//			{
//				Manager.GetDisguise().addViewerToDisguise(disguise, other, true);
//			}
//		}
//	}

	public void giveTeamArmor(Player player, Color color)
	{
		ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
		meta.setColor(color);
		armor.setItemMeta(meta);
		player.getInventory().setChestplate(armor);

		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta metaLegs = (LeatherArmorMeta)legs.getItemMeta();
		metaLegs.setColor(color);
		legs.setItemMeta(metaLegs);
		player.getInventory().setLeggings(legs);

		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta metaBoots = (LeatherArmorMeta)boots.getItemMeta();
		metaBoots.setColor(color);
		boots.setItemMeta(metaBoots);
		player.getInventory().setBoots(boots);
	}

	@EventHandler
	public void shopInventoryClick(InventoryClickEvent event)
	{
		_shopManager.inventoryClick(event);
	}



	@EventHandler
	public void shopUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		_shopManager.update();
	}

	@EventHandler
	public void quitClean(PlayerQuitEvent event)
	{
		_shopManager.leaveShop(event.getPlayer(), false, true);
		_disguise.remove(event.getPlayer());
		_scoped.remove(event.getPlayer());
		dropInventory(event.getPlayer());
	}

	@EventHandler
	public void giveBombInitial(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				Player player = UtilAlg.Random(GetTeam(ChatColor.RED).GetPlayers(true));

				giveBombToPlayer(player);
			}
		}, 40);
	}

	public void giveBombToPlayer(Player player)
	{
		if (player == null)
			return;

		GameTeam team = GetTeam(player);
		if (team == null)
			return;

		if (team.GetColor() != ChatColor.RED)
			return;

		//Bomb
		player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.GOLD_SWORD, (byte)0, 1, C.cGold + C.Bold + "C4 Explosive"));

		//Inform
		if (!IsLive() || _freezeTime > 0)
		{
			for (Player other : team.GetPlayers(false))
				if (!other.equals(player))
				{
					UtilTextMiddle.display(null, player.getName() + " has the Bomb", 10, 80, 10, other);
					UtilPlayer.message(other, C.cGold + C.Bold + player.getName() + " has the Bomb!");
				}

			//Chat
			UtilPlayer.message(player, C.cGold + C.Bold + "You have the Bomb!");
			UtilPlayer.message(player, C.cGold + C.Bold + "Hold Right-Click to place at a Bomb Site!");

			//Title
			UtilTextMiddle.display(C.cRed + "You have the Bomb", "Hold Right-Click to place at a Bomb Site!", 10, 80, 10, player);
		}
		else
		{
			for (Player other : team.GetPlayers(false))
				if (!other.equals(player))
				{
					UtilPlayer.message(other, C.cGold + C.Bold + player.getName() + " picked up the Bomb!");

					//Title
					UtilTextMiddle.display(null, player.getName() + " picked up the Bomb", 10, 50, 10, other);
				}


			//Chat
			UtilPlayer.message(player, C.cGold + C.Bold + "You picked up the Bomb!");

			//Title
			UtilTextMiddle.display(null, "You picked up the Bomb", 10, 50, 10, player);
		}


		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
		_bombHolder = player;

		if (_bombItem != null)
		{
			_bombItem.remove();
			_bombItem = null;
		}
	} 

	public void registerGun(Gun gun, Player player)
	{
		_gunsEquipped.put(gun, player);
	}

	public void deregisterGun(Gun gun)
	{
		_gunsEquipped.remove(gun);
	}

	public void registerGrenade(Grenade grenade, Player player)
	{
		_grenadesEquipped.put(grenade, player);
	}

	public void deregisterGrenade(Grenade grenade)
	{
		_grenadesEquipped.remove(grenade);
	}

	public void registerDroppedGun(Entity ent, Gun gun)
	{
		_gunsDropped.put(ent, gun);
	}

	public void deregisterDroppedGun(Gun gun)
	{
		Iterator<Entity> entIterator = _gunsDropped.keySet().iterator();

		while (entIterator.hasNext())
			if (gun.equals(_gunsDropped.get(entIterator.next())))
				entIterator.remove();
	}

	public void registerDroppedGrenade(Entity ent, Grenade grenade)
	{
		_grenadesDropped.put(ent, grenade);
	}

	public void deregisterDroppedGrenade(Grenade grenade)
	{
		Iterator<Entity> entIterator = _grenadesDropped.keySet().iterator();

		while (entIterator.hasNext())
			if (grenade.equals(_grenadesDropped.get(entIterator.next())))
				entIterator.remove();
	}

	public void registerBullet(Bullet bullet)
	{
		_bullets.put(bullet.Bullet, bullet);

		UtilEnt.ghost(bullet.Bullet, false, true);
	}

	public void registerThrownGrenade(Entity ent, Grenade grenade)
	{
		_grenadesThrown.put(ent, grenade);
	}

	public void registerIncendiary(Location loc, long endTime)
	{
		_incendiary.put(loc, endTime);
	}

	public Gun getGunInHand(Player player, ItemStack overrideStack)
	{
		ItemStack stack = player.getItemInHand();
		if (overrideStack != null)
			stack = overrideStack;

		for (Gun gun : _gunsEquipped.keySet())
		{
			if (!_gunsEquipped.get(gun).equals(player))
				continue;

			if (!gun.isStack(stack))
				continue;

			return gun;
		}

		return null;
	}

	public Grenade getGrenadeInHand(Player player, ItemStack overrideStack)
	{
		ItemStack stack = player.getItemInHand();
		if (overrideStack != null)
			stack = overrideStack;

		for (Grenade grenade : _grenadesEquipped.keySet())
		{
			if (!_grenadesEquipped.get(grenade).equals(player))
				continue;

			if (!grenade.isStack(stack))
				continue;

			return grenade;
		}

		return null;
	}



	@EventHandler
	public void triggerShoot(PlayerInteractEvent event)
	{
		if (_freezeTime > 0)
			return;

		if (!IsLive())
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		if (!UtilEvent.isAction(event, ActionType.R))
			return;

		//Players get between 150 and 250. 200 is norm. For some reason it doesnt go outside 150-250 bounds.
		//Announce("Since Last: " + (System.currentTimeMillis() - last), false);
		//last = System.currentTimeMillis();

		Gun gun = getGunInHand(event.getPlayer(), null);
		if (gun == null)
			return;

		gun.shoot(event.getPlayer(), this);
		event.setCancelled(true);
	}

	@EventHandler
	public void triggerReload(PlayerInteractEvent event)
	{
		if (_freezeTime > 0)
			return;

		if (!IsLive())
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		Gun gun = getGunInHand(event.getPlayer(), null);
		if (gun == null)
			return;

		gun.reload(event.getPlayer());
		event.setCancelled(true);
	}

	@EventHandler
	public void triggerGrenade(PlayerInteractEvent event)
	{
		if (_freezeTime > 0)
		{
			event.setCancelled(true);
			return;
		}

		if (!UtilEvent.isAction(event, ActionType.L) && !UtilEvent.isAction(event, ActionType.R))
			return;

		if (!IsLive())
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		Grenade grenade = getGrenadeInHand(event.getPlayer(), null);
		if (grenade == null)
			return;

		grenade.throwGrenade(event.getPlayer(), UtilEvent.isAction(event, ActionType.L), this);
		event.setCancelled(true);
	}

	@EventHandler
	public void triggerDrop(PlayerDropItemEvent event)
	{
		if (!InProgress())
			return;

		//Without this, the event is cancelled in pre-game by managers
		//Which results in the item staying in your hand, even if i set to null here.
		event.setCancelled(false);

		//Guns
		Gun gun = getGunInHand(event.getPlayer(), event.getItemDrop().getItemStack());
		if (gun != null)
		{
			gun.drop(this, event.getPlayer(), false, false);
			event.getItemDrop().remove();
			event.getPlayer().setItemInHand(null);
			return;
		}

		//Grenades
		Grenade grenade = getGrenadeInHand(event.getPlayer(), event.getItemDrop().getItemStack());
		if (grenade != null)
		{
			grenade.drop(this, event.getPlayer(), false, false);
			event.getItemDrop().remove();
			event.getPlayer().setItemInHand(null);
			return;
		}

		//Bomb
		if (event.getItemDrop().getItemStack().getType() == Material.GOLD_SWORD)
		{
			_bombItem = event.getItemDrop();
			_bombHolder = null;

			//Radio
			playSound(Radio.T_BOMB_DROP, null, GetTeam(event.getPlayer()));

			return;
		}

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void deathDrop(PlayerDeathEvent event)
	{
		_scoped.remove(event.getEntity());
		dropInventory(event.getEntity());
	}

	public void dropInventory(Player player)
	{
		if (!InProgress())
			return;

		for (int i=0 ; i<9 ; i++)
		{
			ItemStack stack = player.getInventory().getItem(i);

			if (stack == null || stack.getType() == Material.AIR)
				continue;

			HashSet<StrikeItem> toDrop = new HashSet<StrikeItem>();

			//Guns
			for (StrikeItem item : _gunsEquipped.keySet())
			{
				if (!_gunsEquipped.get(item).equals(player))
					continue;

				toDrop.add(item);
			}

			//Grenades
			for (StrikeItem item : _grenadesEquipped.keySet())
			{
				if (!_grenadesEquipped.get(item).equals(player))
					continue;

				toDrop.add(item);
			}

			//Drop Primary
			boolean primaryDropped = false;
			for (StrikeItem item : toDrop)
			{
				if (item.getType() == StrikeItemType.PRIMARY_WEAPON)
				{
					item.drop(this, player, true, false);
					primaryDropped = true;
				}
			}

			//Drop Remaining
			boolean grenadeDropped = false;
			for (StrikeItem item : toDrop)
			{
				//Record Primary Dropped
				if (item.getType() == StrikeItemType.PRIMARY_WEAPON)
					continue;

				//Pistol
				if (item.getType() == StrikeItemType.SECONDARY_WEAPON)
				{
					item.drop(this, player, true, primaryDropped);
					continue;
				}

				//Grenade
				if (item.getType() == StrikeItemType.GRENADE)
				{
					item.drop(this, player, true, grenadeDropped);
					grenadeDropped = true;
					continue;
				}

				//Other
				item.drop(this, player, true, false);	
			}

			//Defuse Kit
			if (stack.getType() == Material.SHEARS)
			{
				_defusalDropped.add(player.getWorld().dropItemNaturally(player.getEyeLocation(), stack));
			}

			//Bomb
			if (stack.getType() == Material.GOLD_SWORD)
			{
				_bombItem = player.getWorld().dropItemNaturally(player.getEyeLocation(), stack);
				_bombItem.setPickupDelay(40);
				_bombHolder = null;

				//Radio
				playSound(Radio.T_BOMB_DROP, null, GetTeam(ChatColor.RED));
			}
		}

		UtilInv.Clear(player);
	}

	@EventHandler
	public void triggerPickup(PlayerPickupItemEvent event)
	{
		if (!InProgress())
			return;

		if (event.getItem().getTicksLived() < 10)
			return;

		if (UtilMath.offset(event.getItem(), event.getPlayer()) > 1)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		//Guns
		Gun gun = _gunsDropped.get(event.getItem());
		if (gun != null)
		{
			if (gun.pickup(this, event.getPlayer()))
				event.getItem().remove();
		}

		//Grenades
		Grenade grenade = _grenadesDropped.get(event.getItem());
		if (grenade != null)
		{
			if (grenade.pickup(this, event.getPlayer()))
				event.getItem().remove();
		}

		//Defusal
		if (UtilGear.isMat(event.getItem().getItemStack(), Material.SHEARS))
		{
			if (GetTeam(event.getPlayer()).GetColor() == ChatColor.RED)
				return;

			if (UtilInv.contains(event.getPlayer(), Material.SHEARS, (byte)0, 1))
				return;

			event.getPlayer().getInventory().setItem(8, event.getItem().getItemStack());

			UtilPlayer.message(event.getPlayer(), F.main("Game", "You equipped Defusal Kit."));

			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.HORSE_ARMOR, 1.5f, 1f);

			event.getItem().remove();
		}

		//Bomb
		if (_bombItem != null && _bombItem.equals(event.getItem()))
		{
			giveBombToPlayer(event.getPlayer());
		}
	}

	@EventHandler
	public void reloadCancel(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Gun gun : _gunsEquipped.keySet())
		{
			gun.cancelReloadCheck(_gunsEquipped.get(gun), this);
		}
	}

	@EventHandler
	public void rechargeWeapons(RechargedEvent event)
	{
		for (Gun gun : _gunsEquipped.keySet())
		{
			if (_gunsEquipped.get(gun).equals(event.GetPlayer()))
			{
				gun.reloadEvent(event);
			}
		}
	}

	@EventHandler
	public void coneOfFireIncrease(PlayerMoveEvent event)
	{
		Gun gun = getGunInHand(event.getPlayer(), null);
		if (gun != null)
		{
			gun.moveEvent(event);
		}
	}

	@EventHandler
	public void coneOfFireReduction(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Gun gun : _gunsEquipped.keySet())
		{
			gun.reduceCone();
		}
	}

	@EventHandler
	public void slowBulletHit(final ProjectileHitEvent event)
	{
		if (!(event.getEntity() instanceof Snowball))
			return;

		Bullet bullet = _bullets.get(event.getEntity());

		//Particle
		if (bullet != null && bullet.Shooter != null)
			UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, event.getEntity().getLocation(), 0, 0, 0, 0, 1,
					ViewDist.MAX, bullet.Shooter);

		//Hit Block Sound
		event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENDERMAN_HIT, 1f, 1f);

		//Block Particle
		Location loc = event.getEntity().getLocation().add(event.getEntity().getVelocity().multiply(0.8));
		Block block = loc.getBlock();
		if (block.getType() == Material.AIR)
		{
			Block closest = null;
			double closestDist = 0;

			for (Block other : UtilBlock.getSurrounding(block, true))
			{
				if (other.getType() == Material.AIR)
					continue;

				double dist = UtilMath.offset(loc, other.getLocation().add(0.5, 0.5, 0.5));

				if (closest == null || dist < closestDist)
				{
					closest = other;
					closestDist = dist;
				}
			}

			if (closest != null)
				block = closest;
		}

		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
		
		if (block.getType() == Material.STAINED_GLASS_PANE)
			block.setType(Material.AIR);
	}

	@EventHandler
	public void slowBulletWhizz(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Entity ent : _bullets.keySet())
		{
			if (ent instanceof Snowball)
				continue;

			if (ent.getTicksLived() < 10)
				continue;

			Bullet bullet = _bullets.get(ent);

			for (Player player : UtilServer.getPlayers())
			{
				if (UtilMath.offset(ent, player) < 4)
				{
					if (!bullet.WhizzSound.contains(player))
					{
						player.playSound(ent.getLocation(), Sound.BAT_IDLE, (float)(0.5 + Math.random() * 0.5), 1f);
						bullet.WhizzSound.add(player);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void instantBulletHit(final ProjectileHitEvent event)
	{
		if (!(event.getEntity() instanceof Arrow))
			return;

		UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				Arrow arrow = (Arrow)event.getEntity();

				Bullet bullet = _bullets.get(arrow);

				//Particle
				if (bullet != null && bullet.Shooter != null)
					UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, arrow.getLocation(), 0, 0, 0, 0, 1,
							ViewDist.MAX, bullet.Shooter);

				//Hit Block Sound
				arrow.getWorld().playSound(arrow.getLocation(), Sound.ENDERMAN_HIT, 1f, 1f);

				//Bullet Whiz Sound
				instantBulletWhizz(arrow.getLocation(), bullet);

				//Tracer Particle
				Location loc = bullet.Origin.clone();
				while (UtilMath.offset(loc, arrow.getLocation()) > 2)
				{
					loc.add(UtilAlg.getTrajectory(loc, arrow.getLocation()).multiply(1));

					UtilParticle.PlayParticle(ParticleType.CRIT, loc, 0, 0, 0, 0, 1,
							ViewDist.NORMAL, UtilServer.getPlayers());
				}

				//Block Particle
				try
				{
					EntityArrow entityArrow = ((CraftArrow) arrow).getHandle();

					Field fieldX = EntityArrow.class.getDeclaredField("d");
					Field fieldY = EntityArrow.class.getDeclaredField("e");
					Field fieldZ = EntityArrow.class.getDeclaredField("f");

					fieldX.setAccessible(true);
					fieldY.setAccessible(true);
					fieldZ.setAccessible(true);

					int x = fieldX.getInt(entityArrow);
					int y = fieldY.getInt(entityArrow);
					int z = fieldZ.getInt(entityArrow);

					Block block = arrow.getWorld().getBlockAt(x, y, z);
					block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
					
					if (block.getType() == Material.STAINED_GLASS_PANE)
						block.setType(Material.AIR);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				_bullets.remove(arrow);
				arrow.remove();
			}	
		}, 0);
	}

	private void instantBulletWhizz(Location bulletEndLocation, Bullet bullet)
	{
		if (bullet == null)
			return;

		Location loc = bullet.Origin.clone();

		if (bullet.Shooter != null)
			bullet.WhizzSound.add(bullet.Shooter);

		//Move between points, check players
		while (UtilMath.offset(loc, bulletEndLocation) > 1)
		{
			//This is used to calculate whether players are between current/end.
			//Add 5 so you still hear the whizz if it hits just infront of you.
			double offsetStartToEnd = UtilMath.offset(loc, bulletEndLocation) + 4;

			for (Player player : UtilServer.getPlayers())
			{
				if (bullet.WhizzSound.contains(player))
					continue;

				//Remove players who are not between current/end points
				if (offsetStartToEnd < UtilMath.offset(player.getEyeLocation(), loc) && 
						offsetStartToEnd < UtilMath.offset(player.getEyeLocation(), bulletEndLocation))
				{
					bullet.WhizzSound.add(player);
					continue;
				}

				//Check
				if (UtilMath.offset(player.getEyeLocation(), loc) < 4)
				{
					player.playSound(loc, Sound.BAT_IDLE, (float)(0.5 + Math.random() * 0.5), 1f);
					bullet.WhizzSound.add(player);
				}
			}

			//Move Closer
			loc.add(UtilAlg.getTrajectory(loc, bulletEndLocation));
		}
	}




	@EventHandler(priority=EventPriority.MONITOR)
	public void removeArrowsFromPlayer(CustomDamageEvent event)
	{
		if (event.GetDamageePlayer() != null)
			((CraftPlayer) event.GetDamageePlayer()).getHandle().p(0);
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void damage(CustomDamageEvent event)
	{
		if (event.GetDamageePlayer() == null)
			return;

		if (event.GetCause() == DamageCause.FALL)
		{
			event.AddMod(GetName(), "Fall Reduction", -2, false);
			return;
		}

		//Knife
		if (event.GetProjectile() == null && event.GetCause() == DamageCause.ENTITY_ATTACK)
		{
			if (event.GetDamagerPlayer(false) != null)
			{
				//Cancel Non-Knife Melee
				if (!UtilGear.isMat(event.GetDamagerPlayer(false).getItemInHand(), Material.IRON_AXE) && 
						!UtilGear.isMat(event.GetDamagerPlayer(false).getItemInHand(), Material.IRON_SWORD))
				{
					event.SetCancelled("Non-Knife");
				}
				//Knife Attack
				else if (!event.IsCancelled())
				{
					Player damager = event.GetDamagerPlayer(false);
					if (damager == null)	return;

					LivingEntity damagee = event.GetDamageeEntity();
					if (damagee == null)	return;

					Vector look = damagee.getLocation().getDirection();
					look.setY(0);
					look.normalize();

					Vector from = damager.getLocation().toVector().subtract(damagee.getLocation().toVector());
					from.setY(0);
					from.normalize();

					Vector check = new Vector(look.getX() * -1, 0, look.getZ() * -1);

					//Backstab
					if (check.subtract(from).length() < 0.8)
					{
						//Damage
						event.AddMod(damager.getName(), "Knife (Backstab)", 40 - event.GetDamage(), false);

						//Effect
						damagee.getWorld().playSound(damagee.getLocation(), Sound.IRONGOLEM_DEATH, 1f, 1f);	

						event.SetKnockback(false);
					}
					//Standard
					else
					{
						//Damage
						event.AddMod(damager.getName(), "Knife", 6 - event.GetDamage(), false);

						event.GetDamageeEntity().getWorld().playSound(event.GetDamageeEntity().getLocation(), Sound.BAT_HURT, 1f, 1f);

						event.AddKnockback("Knife", 1.2);
					}
				}
			}

			return;
		}

		//Gun
		Bullet bullet = _bullets.remove(event.GetProjectile());
		if (bullet == null)
			return;

		//Get Hit Area
		int hitArea = 0;

		if (_customHitbox)
		{
			if (event.GetProjectile() instanceof Arrow)
				hitArea = getArrowHitArea(event.GetDamageePlayer(), bullet.Origin.clone(), bullet.Direction.clone());
			else
				hitArea = getSnowballHitArea(event.GetDamageePlayer(), event.GetProjectile());
		}


		if (hitArea == -1)
		{
			event.SetCancelled("Miss");
			return;
		}

		//Bullet Whiz Sound
		if (event.GetProjectile() instanceof Arrow)
			instantBulletWhizz(event.GetDamageePlayer().getEyeLocation(), bullet);

		//Wipe previous data!
		event.GetCancellers().clear();
		event.GetDamageMod().clear();

		if (!Manager.canHurt(event.GetDamageePlayer(), event.GetDamagerPlayer(true)))
			event.SetCancelled("Team Damage");

		event.AddMod(GetName(), "Negate Default", -event.GetDamageInitial(), false);	

		//Damage + Dropoff
		double damage = bullet.getDamage();
		double damageDropoff = bullet.getDamageDropoff(event.GetDamageeEntity().getLocation());

		//Add Damages
		event.AddMod(bullet.Shooter.getName(), bullet.Gun.getName(), damage, true);
		event.AddMod(bullet.Shooter.getName(), "Distance Dropoff", damageDropoff, false);

		//Headshot
		if (hitArea == 1)
		{
			Bukkit.getPluginManager().callEvent(new PlayerHeadshotEvent(bullet.Shooter, event.GetDamageePlayer()));

			event.AddMod(bullet.Shooter.getName(), "Headshot", damage*2, true);

			//Wearing Helmet
			if (Armor.isArmor(event.GetDamageePlayer().getInventory().getHelmet()) ||
					(_scoped.containsKey(event.GetDamageePlayer()) && UtilGear.isMat(_scoped.get(event.GetDamageePlayer()), Material.LEATHER_HELMET)))
			{
				event.AddMod(event.GetDamageePlayer().getName(), "Helmet", -damage*1, false);
				event.GetDamageePlayer().getWorld().playSound(event.GetDamageePlayer().getEyeLocation(), Sound.SPIDER_DEATH, 1f, 1f);
			}
			else
			{

				event.GetDamageePlayer().getWorld().playSound(event.GetDamageePlayer().getEyeLocation(), Sound.SLIME_ATTACK, 1f, 1f);
			}
		}

		//Kevlar - Body Hit
		if (hitArea == 0 && Armor.isArmor(event.GetDamageePlayer().getInventory().getChestplate()))
		{
			double damageArmor = -(1 - bullet.Gun.getArmorPenetration()) * (damage + damageDropoff);

			event.AddMod(event.GetDamageePlayer().getName(), "Kevlar", damageArmor, false);
		}
		//Mini-Stun
		else
		{
			event.GetDamageePlayer().setVelocity(new Vector(0,0,0));
		}

		event.SetKnockback(false);
		event.SetIgnoreRate(true);
		event.SetIgnoreArmor(true);
	}

	public int getArrowHitArea(Player damagee, Location origin, Vector trajectory)
	{
		//Move to near-player
		Location start = origin.clone().add(trajectory.clone().multiply(UtilMath.offset(origin, damagee.getEyeLocation()) - 2));

		Location loc = start.clone();

		while (!hitHead(damagee, loc) && !hitBody(damagee, loc) && UtilMath.offset(damagee.getLocation(), loc) < 6)
		{
			loc.add(trajectory.clone().multiply(0.1));
		}

		if (hitHead(damagee, loc))
		{
			return 1;
		}


		if (hitBody(damagee, loc))
		{
			return 0;
		}

		return -1;
	}

	public int getSnowballHitArea(Player damagee, Projectile snowball)
	{
		//Move to near-player
		Location start = snowball.getLocation();

		Location loc = start.clone();

		while (!hitHead(damagee, loc) && !hitBody(damagee, loc) && UtilMath.offset(damagee.getLocation(), loc) < 6)
		{
			loc.add(snowball.getVelocity().clone().multiply(0.1));
		}

		if (hitBody(damagee, loc))
			return 0;

		if (hitHead(damagee, loc))
			return 1;

		return -1;
	}

	public boolean hitBody(Player player, Location loc)
	{
		return UtilMath.offset2d(loc, player.getLocation()) < 0.6 &&	//0.6 is ideal
				loc.getY() > player.getLocation().getY() &&
				loc.getY() < player.getEyeLocation().getY() - 0.1;
	}

	public boolean hitHead(Player player, Location loc)
	{
		return UtilMath.offset2d(loc, player.getLocation()) < 0.2 &&	//0.3 was old value, too large
				loc.getY() >= player.getEyeLocation().getY() - 0.1 &&
				loc.getY() < player.getEyeLocation().getY() + 0.1;		//0.2 was old value, too large
	}


	@EventHandler
	public void killReward(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player)event.GetEvent().getEntity();

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer == null || killer.equals(killed))
				return;

			if (GetTeam(killed).equals(GetTeam(killer)))
				return;

			int amount = 300;

			if (event.GetLog().GetLastDamager().GetReason().contains("AWP"))
				amount = 100;

			else if (event.GetLog().GetLastDamager().GetReason().contains("Nova"))
				amount = 900;

			else if (event.GetLog().GetLastDamager().GetReason().contains("Knife"))
				amount = 1500;

			_shopManager.addMoney(killer, amount, "kill with " + event.GetLog().GetLastDamager().GetReason());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void killMessage(CombatDeathEvent event)
	{
		Player killed = (Player)event.GetEvent().getEntity();

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			String reason = "You died.";
			if (killer != null)
			{
				if (killer.equals(killed))
				{
					reason = "You killed yourself";
				}
				else
				{
					reason = "";
					
					GameTeam team = GetTeam(killer);
					if (team != null)
						reason += team.GetColor();
						
					reason += killer.getName() + ChatColor.RESET + " killed you with " + event.GetLog().GetLastDamager().GetReason();
				}
			}
			
			UtilTextMiddle.display("DEAD", reason, 0, 100, 20, killed);
		}
	}

	@EventHandler
	public void updateBulletsGrenades(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		//Bullets
		Iterator<Entity> bulletIterator = _bullets.keySet().iterator();

		while (bulletIterator.hasNext())
		{
			Entity bullet = bulletIterator.next();

			if (!bullet.isValid() || bullet.getTicksLived() > 40)
			{
				bulletIterator.remove();
				bullet.remove();
			}
		}

		//Grenades
		Iterator<Entity> grenadeIterator = _grenadesThrown.keySet().iterator();

		while (grenadeIterator.hasNext())
		{
			Entity grenadeItem = grenadeIterator.next();

			UtilParticle.PlayParticle(ParticleType.CRIT, grenadeItem.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());

			//Expired
			if (!grenadeItem.isValid() || grenadeItem.getTicksLived() > 400)
			{
				grenadeItem.remove();
				grenadeIterator.remove();
				continue;
			}

			//Completed
			Grenade grenade = _grenadesThrown.get(grenadeItem);
			if (grenade.update(this, grenadeItem))
			{
				grenadeItem.remove();
				grenadeIterator.remove();
			}
		}
	}

	public void dropSlotItem(Player player, int slot)
	{
		for (Gun gun : _gunsEquipped.keySet())
		{
			if (!_gunsEquipped.get(gun).equals(player))
				continue;

			if (gun.isStack(player.getInventory().getItem(slot)))
			{
				gun.drop(this, player, false, false);
				player.getInventory().setItem(slot, null);
				return;
			}
		}
	}

	@EventHandler
	public void plantBomb(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!UtilGear.isMat(player.getItemInHand(), Material.GOLD_SWORD))
			return;

		if (!UtilEnt.isGrounded(player))
		{
			UtilPlayer.message(player, F.main("Game", "You can only plant Bomb on the ground!"));
			event.setCancelled(true);
			return;
		}

		//Should never occur with 1 Bomb
		if (_bombPlanter != null)
		{
			UtilPlayer.message(player, F.main("Game", "Someone else is planting Bomb..."));
			event.setCancelled(true);
			return;
		}

		//Check Bomb Sites
		boolean near = false;
		for (Location loc : _bombSites)
		{
			if (UtilMath.offset(player.getLocation(), loc) < 5)
			{
				near = true;
				break;
			}
		}

		//Too Far
		if (!near)
		{
			UtilPlayer.message(player, F.main("Game", "You can only plant Bomb at a bomb site!"));
			event.setCancelled(true);
			return;
		}

		_bombPlanter = player;
		_bombPlanter.setExp(0f);

		UtilPlayer.message(player, F.main("Game", "You are placing the Bomb."));

		//Radio
		playSound(Radio.T_BOMB_PLANT, null, GetTeam(_bombPlanter));
	}

	@EventHandler
	public void plantBombUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (_bombPlanter == null)
			return;

		if (!_bombPlanter.isBlocking() || !_bombPlanter.isOnline())
		{
			_bombPlanter.setExp(0f);
			UtilTextMiddle.clear(_bombPlanter);
			_bombPlanter = null;
			return;
		}

		_bombPlanter.setExp(Math.min(_bombPlanter.getExp() + 0.017f, 0.99999f));

		if (Math.random() > 0.90)
			_bombPlanter.getWorld().playSound(_bombPlanter.getLocation(), Sound.NOTE_PLING, 1f, 3f);

		UtilTextMiddle.display(C.cRed + C.Bold + "Planting Bomb", UtilTextMiddle.progress(_bombPlanter.getExp()), 0, 10, 0, _bombPlanter);

		if (_bombPlanter.getExp() >= 0.98f)
		{
			_bomb = new Bomb(_bombPlanter);

			_shopManager.addMoney(_bombPlanter, 300, "planting the bomb");

			Announce(C.cRed + C.Bold + _bombPlanter.getName() + " has planted the bomb!");

			_bombPlantedBy = _bombPlanter;

			_bombPlanter.setExp(0f);
			_bombPlanter.setItemInHand(null);
			_bombPlanter = null;
			_bombHolder = null; 

			//Sound
			playSound(Radio.BOMB_PLANT, null, null);

			//Title
			UtilTextMiddle.display(null, C.cRed + C.Bold + "Bomb has been planted!", 10, 50, 10);
		}
	}

	@EventHandler
	public void plantDefuseBombRestrictMovement(PlayerMoveEvent event)
	{
		if (_bombPlanter != null && _bombPlanter.equals(event.getPlayer()))
			if (UtilMath.offset(event.getFrom(), event.getTo()) > 0)
				event.setTo(event.getFrom());

		//		if (_bombDefuser != null && _bombDefuser.equals(event.getPlayer()))
		//			if (UtilMath.offset(event.getFrom(), event.getTo()) > 0)
		//				event.setTo(event.getFrom());
	}

	@EventHandler
	public void defuseKitMessage(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!UtilGear.isMat(player.getItemInHand(), Material.SHEARS))
			return;

		UtilPlayer.message(player, F.main("Game", "Look at the Bomb to defuse it."));
	}

	public void startDefuse()
	{
		for (Player player : GetTeam(ChatColor.AQUA).GetPlayers(true))
		{
			Block block = player.getTargetBlock(null, 5);

			if (block == null || !_bomb.isBlock(block))
				continue;

			if (UtilMath.offset(player.getLocation(), block.getLocation().add(0.5, 0, 0.5)) > 3)
				continue;

			if (_bombDefuser != null)
			{
				if (Recharge.Instance.use(player, "Defuse Message", 2000, false, false))
					UtilPlayer.message(player, F.main("Game", _bombDefuser.getName() + " is already defusing the Bomb."));

				continue;
			}

			_bombDefuser = player;
			_bombDefuser.setExp(0f);

			UtilPlayer.message(player, F.main("Game", "You are defusing the Bomb."));

			_bombDefuser.getWorld().playSound(_bombDefuser.getLocation(), Sound.PISTON_RETRACT, 2f, 1f);
		}
	}

	@EventHandler
	public void defuseBombUpdate(UpdateEvent event)
	{
		if (_bomb == null)
			return;

		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		if (_bombDefuser == null)
		{
			startDefuse();
		}

		if (_bombDefuser == null)
			return;

		Block block = _bombDefuser.getTargetBlock(null, 5);

		if (!IsAlive(_bombDefuser) || block == null || !_bomb.isBlock(block)  || !_bombDefuser.isOnline() || UtilMath.offset(_bombDefuser.getLocation(), block.getLocation().add(0.5, 0, 0.5)) > 3)
		{
			_bombDefuser.setExp(0f);
			_bombDefuser = null;
			return;
		}

		//Kit or Not?
		float defuseRate = 0.005f;
		if (UtilGear.isMat(_bombDefuser.getInventory().getItem(8), Material.SHEARS))
			defuseRate = 0.01f;

		_bombDefuser.setExp(Math.min(_bombDefuser.getExp() + defuseRate, 0.99999f));

		UtilTextMiddle.display(C.cAqua + C.Bold + "Defusing Bomb", UtilTextMiddle.progress(_bombDefuser.getExp()), 0, 10, 0, _bombDefuser);

		if (_bombDefuser.getExp() >= 0.98f)
		{
			_bomb.defuse();

			_winText = _bombDefuser.getName() + " defused the bomb!";

			_bomb = null;
			_bombDefuser.setExp(0f);
			_bombDefuser = null;

			//Sound
			playSound(Radio.BOMB_DEFUSE, null, null);

			setWinner(GetTeam(ChatColor.AQUA), true);
		}
	}

	@EventHandler
	public void bombUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		if (_bomb == null)
			return;

		if (!_bomb.update())
			return;

		/*
		Set<Block> blocks = UtilBlock.getInRadius(_bomb.Block.getLocation(), 10d).keySet();

		Iterator<Block> blockIterator = blocks.iterator();
		while (blockIterator.hasNext())
		{
			Block block = blockIterator.next();

			if (block.getY() < 2)
				blockIterator.remove();
		}

		Manager.GetExplosion().BlockExplosion(blocks, _bomb.Block.getLocation(), false);
		 */


		HashMap<Player, Double> players = UtilPlayer.getInRadius(_bomb.Block.getLocation(), 48);
		for (Player player : players.keySet())
		{
			if (!IsAlive(player))
				continue;

			// Damage Event
			Manager.GetDamage().NewDamageEvent(player, null, null,
					DamageCause.CUSTOM, 1 + (players.get(player) * 40),
					true, true, false, "Bomb", "C4 Explosion");
		}


		_bomb = null;

		_winText = _bombPlantedBy.getName() + " destroyed the bomb site!";

		setWinner(GetTeam(ChatColor.RED), false);
	}

	@EventHandler
	public void bombItemUpdate(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		if (_bombItem == null)
			return;

		UtilParticle.PlayParticle(ParticleType.HAPPY_VILLAGER, _bombItem.getLocation().add(0.0, 0.2, 0.0), 0, 0, 0, 0, 1,
				ViewDist.LONG, UtilServer.getPlayers());
	}

	public int getScore(GameTeam team)
	{
		if (!_score.containsKey(team))
			_score.put(team, 0);

		return _score.get(team);
	}

	public void addScore(GameTeam team)
	{
		_score.put(team, getScore(team) + 1);
	}

	@EventHandler
	public void roundTimerUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		if (_bomb != null)
			return;

		if (UtilTime.elapsed(GetStateTime(), _roundTime))
		{
			_winText = "Bomb sites were successfully defended!";
			drawScoreboard();
			setWinner(GetTeam(ChatColor.AQUA), false);
		}
	}

	@EventHandler
	public void roundPlayerCheck(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(true).size() > 0)
				teamsAlive.add(team);

		if (teamsAlive.size() == 1)
		{
			//Bomb Planted - CT cannot win without defusing
			if (_bomb != null)
			{
				if (teamsAlive.size() > 0)
				{
					if (teamsAlive.get(0).GetColor() == ChatColor.AQUA)
					{
						return;
					}
				}
			}

			setWinner(teamsAlive.get(0), false);
		}
		else if (teamsAlive.size() == 0)
		{
			if (_bomb == null)
			{
				_winText = "Bomb sites were successfully defended!";
				setWinner(GetTeam(ChatColor.AQUA), false);
			}
			else
			{
				_winText = "Bomb site will be destroyed!";
				setWinner(GetTeam(ChatColor.RED), false);
			}
		}
	}

	public void setWinner(final GameTeam winner, boolean defuse)
	{
		if (_roundOver)
			return;

		Bukkit.getPluginManager().callEvent(new RoundOverEvent(this));

		_roundOver = true;

		String winnerLine = C.Bold + "The round was a draw!";
		ChatColor color = ChatColor.GRAY;
		if (winner != null)
		{
			winnerLine= winner.GetColor() + C.Bold + winner.GetName() + " has won the round!";
			addScore(winner);
			drawScoreboard();
			color = winner.GetColor();


		}

		//Sound
		if (winner != null)
		{
			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					if (winner.GetColor() == ChatColor.RED)
						playSound(Radio.T_WIN, null, null);
					else
						playSound(Radio.CT_WIN, null, null);
				}
			}, defuse ? 60 : 0);
		}

		//Record Streak for Money
		if (winner.GetColor() == ChatColor.RED)
		{
			_winStreak++;

			if (_ctWonLast)
			{
				_ctWonLast = false;
				_winStreak = 0;
			}
		}
		else
		{
			_winStreak++;

			if (!_ctWonLast)
			{
				_ctWonLast = true;
				_winStreak = 0;
			}
		}

		//Announce
		Announce("", false);
		Announce(color + "===================================", false);
		Announce("", false);
		Announce(winnerLine, false);
		if (_winText != null)
			Announce(_winText, false);
		Announce("", false);
		Announce(color + "===================================", false);

		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
			
			Recharge.Instance.Reset(player, "reload");
		}

		UtilTextMiddle.display(null, winnerLine, 20, 120, 20);

		//Check for total game win
		EndCheck();

		//Next Round (if not over)
		if (IsLive())
		{
			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					restartRound();
				}
			}, 100);
		}
	}

	public void restartRound()
	{
		giveMoney();

		//Clean
		resetGame();

		//Teleport to Spawn
		for (GameTeam team : GetTeamList())
			team.SpawnTeleport(false);

		//Revive Dead Players
		for (Player player : GetPlayers(false))
			if (!IsAlive(player))
			{
				SetPlayerState(player, PlayerState.IN);

				GameTeam team = GetTeam(player);

				//Teleport
				team.SpawnTeleport(player);

				Manager.Clear(player);
				UtilInv.Clear(player);

				ValidateKit(player, GetTeam(player));

				if (GetKit(player) != null)
					GetKit(player).ApplyKit(player);		
			}

		//Remove Scope
		for (Player player : GetPlayers(false))
			removeScope(player);
		
		//Get Hit By Bullets
		for (Player player : GetPlayers(false))
			((CraftPlayer) player).getHandle().spectating = false;

		//Prepare Sound
		for (Player player : GetPlayers(false))
		{
			player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 2f);
			Manager.GetCondition().Factory().Blind("Respawn", player, null, 2, 0, false, false, false);
			UtilPlayer.message(player, F.main("Game", "You have " + F.elem(C.cGreen + "$" + _shopManager.getMoney(player)) + ". Open your Inventory to spend it."));

			UtilTextMiddle.display(C.cGreen + "$" + _shopManager.getMoney(player), "Open your Inventory to buy new equipment", 10, 120, 10, player);
		}

		//Update Scoreboard Teams
		for (GameTeam team : GetTeamList())
			for (Player teamMember : team.GetPlayers(true))
				GetScoreboard().SetPlayerTeam(teamMember, team.GetName().toUpperCase());

		//Alternate Bullets
		if (_bulletAlternate)
			_bulletInstant = (_bulletInstant + 1)%3;

		//Debug Details
		if (_debug)
		{
			Announce(C.cDPurple + C.Bold + "ROUND SETTINGS:");

			if (_customHitbox)
				Announce(C.cPurple + C.Bold + "Hitbox: " + ChatColor.RESET + "Accurate with Headshots");
			else
				Announce(C.cPurple + C.Bold + "Hitbox: " + ChatColor.RESET + "Default with No Headshot");

			if (_bulletInstant == 0)
				Announce(C.cPurple + C.Bold + "Bullets: " + ChatColor.RESET + "Slow and Visible");
			else if (_bulletInstant == 1)
				Announce(C.cPurple + C.Bold + "Bullets: " + ChatColor.RESET + "Instant and Invisible");
			else
				Announce(C.cPurple + C.Bold + "Bullets: " + ChatColor.RESET + "Slow and Visible with Instant Sniper");
		}
	}

	public void giveMoney()
	{
		if (_ctWonLast)
		{
			int ctMoney = 3250;
			int tMoney = 1400 + (Math.min(4,_winStreak) * 500);

			if (_bombPlanted)
			{
				ctMoney += 250;
				tMoney += 800;
			}

			//Award
			for (Player player : GetTeam(ChatColor.RED).GetPlayers(false))
				_shopManager.addMoney(player, tMoney, "losing the round");

			for (Player player : GetTeam(ChatColor.AQUA).GetPlayers(false))
				_shopManager.addMoney(player, ctMoney, "winning the round");
		}
		else
		{
			int tMoney = 3250;
			int ctMoney = 1400 + (Math.min(4,_winStreak) * 500);

			//Award
			for (Player player : GetTeam(ChatColor.RED).GetPlayers(false))
				_shopManager.addMoney(player, tMoney, "winning the round");

			for (Player player : GetTeam(ChatColor.AQUA).GetPlayers(false))
				_shopManager.addMoney(player, ctMoney, "losing the round");
		}
	}

	public void resetGame()
	{
		//General
		_roundOver = false;
		SetStateTime(System.currentTimeMillis());
		_freezeTime = 10;
		_winText = null;

		//Bomb
		if (_bomb != null)
			_bomb.clean();

		if (_bombItem != null)
			_bombItem.remove();

		if (_bombHolder != null)
		{
			_bombHolder.getInventory().remove(Material.GOLD_SWORD);
			_bombHolder = null;
		}

		_bomb = null;
		_bombItem = null;

		_bombPlanter = null;
		_bombDefuser = null;
		_bombPlantedBy = null;
		_bombPlanted = false;

		//Dropped Guns
		for (Entity ent : _gunsDropped.keySet())
			ent.remove();
		_gunsDropped.clear();

		//Dropped Grenades
		for (Entity ent : _grenadesDropped.keySet())
			ent.remove();
		_grenadesDropped.clear();

		//Thrown Grenades
		for (Entity ent : _grenadesThrown.keySet())
			ent.remove();
		_grenadesThrown.clear();

		//Bullets
		for (Entity ent : _bullets.keySet())
			ent.remove();
		_bullets.clear();

		//Bullets
		for (Entity ent : _defusalDropped)
			ent.remove();
		_defusalDropped.clear();

		//Incendiary
		_incendiary.clear();
		Manager.GetBlockRestore().RestoreAll();

		//Restock Ammo
		for (Gun gun : _gunsEquipped.keySet())
			gun.restockAmmo(_gunsEquipped.get(gun));

		//Health
		for (Player player : UtilServer.getPlayers())
			player.setHealth(20);

		//Reset Shop
		for (Player player : UtilServer.getPlayers())
			_shopManager.leaveShop(player, false, false);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void specToTeam(PlayerJoinEvent event)
	{
		if (GetState() == GameState.Recruit || GetState() == GameState.Loading)
			return;

		//Target Team
		GameTeam targetTeam = null;
		if (GetTeamList().get(0).GetPlayers(false).size() < GetTeamList().get(1).GetPlayers(false).size())
			targetTeam = GetTeamList().get(0);
		else if (GetTeamList().get(0).GetPlayers(false).size() > GetTeamList().get(1).GetPlayers(false).size())
			targetTeam = GetTeamList().get(1);
		else if (Math.random() > 0.5)
			targetTeam = GetTeamList().get(1);
		else
			targetTeam = GetTeamList().get(0);

		SetPlayerTeam(event.getPlayer(), targetTeam, false);
		
		((CraftPlayer) event.getPlayer()).getHandle().spectating = true;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void quitLeaveTeam(PlayerQuitEvent event)
	{
		GameTeam team = GetTeam(event.getPlayer());

		if (team != null)
		{
			team.RemovePlayer(event.getPlayer());
		}	
	}

	@EventHandler
	public void restartFreezeCountdown(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		if (_freezeTime <= 0)
			return;

		_freezeTime--;

		for (Player player : UtilServer.getPlayers())
		{
			if (_freezeTime > 0)
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
			else
				player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1f, 1f);
		}

		if (_freezeTime == 0)
		{
			//Give Bomb
			Player bombPlayer = UtilAlg.Random(GetTeam(ChatColor.RED).GetPlayers(true));
			giveBombToPlayer(bombPlayer);

			//Sound
			playSound(Radio.CT_START, null, GetTeam(ChatColor.AQUA));
			playSound(Radio.T_START, null, GetTeam(ChatColor.RED));
		}
	}

	@EventHandler
	public void restartPlayerFreeze(PlayerMoveEvent event)
	{
		if (_freezeTime <= 0)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		if (UtilMath.offset2d(event.getFrom(), event.getTo()) <= 0)
			return;

		event.getFrom().setPitch(event.getTo().getPitch());
		event.getFrom().setYaw(event.getTo().getYaw());

		event.setTo(event.getFrom());
	}

	//@EventHandler
	public void boostClimb(PlayerToggleSneakEvent event)
	{
		if (!IsLive())
			return;

		Player player = event.getPlayer();

		boolean nearOther = false;
		for (Player other : GetPlayers(true))
		{
			if (player.equals(other))
				continue;

			if (UtilMath.offset(player, other) < 1 && other.getLocation().getY() <= player.getLocation().getY())
			{
				nearOther = true;
				break;
			}
		}

		if (!nearOther)
			return;

		if (!Recharge.Instance.use(player, "Boost", 1500, false, false))
			return;

		UtilAction.velocity(event.getPlayer(), new Vector(0,1,0), 0.6, false, 0, 0, 1, true);

		Recharge.Instance.useForce(player, "Boost", 1500);

		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
	}

	@EventHandler
	public void healthCancel(EntityRegainHealthEvent event)
	{
		if (event.getRegainReason() == RegainReason.SATIATED)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void scopeUpdate(PlayerToggleSneakEvent event)
	{
		if (!IsLive())
			return;

		Gun gun = getGunInHand(event.getPlayer(), null);
		if (gun == null)
			return;

		if (!gun.hasScope())
			return;

		//Enable
		if (!event.getPlayer().isSneaking())
		{
			_scoped.put(event.getPlayer(), event.getPlayer().getInventory().getHelmet());
			Manager.GetCondition().Factory().Slow("Scope", event.getPlayer(), null, 9999, 2, false, false, false, false);
			event.getPlayer().getInventory().setHelmet(new ItemStack(Material.PUMPKIN));

			if (gun.getGunType() == GunType.SNIPER)
			{
				event.getPlayer().getWorld().playSound(event.getPlayer().getEyeLocation(), Sound.GHAST_DEATH, 0.8f, 1f);

				//if (Manager.GetCondition().GetActiveCondition(event.getPlayer(), ConditionType.BLINDNESS) == null)
				Manager.GetCondition().Factory().Blind("Scope Blind", event.getPlayer(), null, 1, 0, false, false, false);
				Manager.GetCondition().Factory().NightVision("Scope Accuracy", event.getPlayer(), null, 0.5, 0, false, false, false);
			}
		}
		else
		{
			removeScope(event.getPlayer());
		}
	}

	@EventHandler
	public void scopeUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetPlayers(true))
		{
			if (!_scoped.containsKey(player))
				continue;

			Gun gun = getGunInHand(player, null);
			if (gun == null || !gun.hasScope() || !player.isSneaking())
			{
				removeScope(player);
			}
		}
	}

	public void removeScope(Player player)
	{
		if (!_scoped.containsKey(player))
			return;

		ItemStack stack = _scoped.remove(player);


		player.getInventory().setHelmet(stack);
		UtilInv.Update(player);

		Manager.GetCondition().EndCondition(player, null, "Scope");

		player.getWorld().playSound(player.getEyeLocation(), Sound.GHAST_DEATH, 0.8f, 1f);
	}

	//@EventHandler
	public void speedUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetPlayers(true))
		{
			if (UtilGear.isMat(player.getItemInHand(), Material.IRON_AXE) || UtilGear.isMat(player.getItemInHand(), Material.IRON_SWORD))
				Manager.GetCondition().Factory().Speed("Knife", player, player, 1.9, 0, false, false, false);
			else
				Manager.GetCondition().EndCondition(player, ConditionType.SPEED, null);
		}
	}

	@EventHandler
	public void incendiaryUpdate(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK)
			for (Player player : UtilServer.getPlayers())
				player.setFireTicks(0);

		if (event.getType() != UpdateType.SLOW)
			return;

		Iterator<Location> fireIterator = _incendiary.keySet().iterator();

		while (fireIterator.hasNext())
		{
			Location loc = fireIterator.next();

			if (_incendiary.get(loc) < System.currentTimeMillis())
				fireIterator.remove();

			else 
				loc.getWorld().playSound(loc, Sound.PIG_DEATH, 1f, 1f);
		}
	}

	@EventHandler
	public void bombBurnUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (_bombItem == null)
			return;

		if (!_bombItem.isValid())
		{
			Location loc = _bombItem.getLocation();

			_bombItem.remove();

			_bombItem = loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_SWORD));
		}
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event != null && event.getType() != UpdateType.FAST)
			return;

		drawScoreboard();
	}

	public void drawScoreboard()
	{
		Scoreboard.Reset();

		for (GameTeam team : this.GetTeamList())
		{

			Scoreboard.WriteBlank();

			Scoreboard.Write(getScore(team) + " " + team.GetColor() + C.Bold + team.GetName());
			//Scoreboard.Write(team.GetColor() + "" + getScore(team) + "" + " Wins" + team.GetColor());
			Scoreboard.Write(team.GetPlayers(true).size() + "" + " Alive" + team.GetColor());

		}

		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cGold + C.Bold + "Playing To");
		Scoreboard.Write(_roundsToWin + " Wins");

		if (InProgress())
		{
			Scoreboard.WriteBlank();

			if (_bomb == null)
			{
				Scoreboard.Write(C.cGold + C.Bold + "Time Left");
				Scoreboard.Write(UtilTime.MakeStr(_roundTime - (System.currentTimeMillis() - this.GetStateTime()), 1));
			}
			else
			{
				if (_bombScoreboardFlash)
					Scoreboard.Write(C.cRed + C.Bold + "Bomb Active");
				else
					Scoreboard.Write(C.cWhite + C.Bold + "Bomb Active");

				_bombScoreboardFlash = !_bombScoreboardFlash;
			}
		}


		Scoreboard.Draw();
	}

	@Override
	public void EndCheck()
	{
		endCheckScore();
		endCheckPlayer();
	}

	public void endCheckScore()
	{
		if (!IsLive())
			return;

		for (GameTeam team : GetTeamList())
		{
			if (getScore(team) >= _roundsToWin)
			{
				//Announce
				AnnounceEnd(team);

				for (GameTeam other : GetTeamList())
				{
					if (WinnerTeam != null && other.equals(WinnerTeam))
					{
						for (Player player : other.GetPlayers(false))
							AddGems(player, 10, "Winning Team", false, false);
					}

					for (Player player : other.GetPlayers(false))
						if (player.isOnline())
							AddGems(player, 10, "Participation", false, false);
				}

				//End
				SetState(GameState.End);
			}
		}
	}

	public void endCheckPlayer()
	{
		if (!IsLive())
			return;

		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(false).size() > 0)
				teamsAlive.add(team);

		if (teamsAlive.size() <= 1)
		{
			//Announce
			if (teamsAlive.size() > 0)
				AnnounceEnd(teamsAlive.get(0));

			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
						AddGems(player, 10, "Winning Team", false, false);
				}

				for (Player player : team.GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
			}

			//End
			SetState(GameState.End);
		}
	}

	//Cleans entities that may not have been removed due to unloaded chunks
	@EventHandler
	public void clean(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		for (Entity ent : WorldData.World.getEntities())
		{
			if (ent instanceof Player)
				continue;

			if (ent instanceof Painting)
				continue;

			if (_gunsDropped.containsKey(ent))
				continue;

			if (_grenadesDropped.containsKey(ent))
				continue;

			if (_grenadesThrown.containsKey(ent))
				continue;

			if (_bullets.containsKey(ent))
				continue;

			if (_defusalDropped.contains(ent))
				continue;

			if (_bombItem != null && _bombItem.equals(ent))
				continue;
			
			if (ent instanceof Item && ((Item)ent).getItemStack().getTypeId() == 175)
				continue;

			ent.remove();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST)
	public void damagePainting(PaintingBreakEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void entityExpire(ItemDespawnEvent event)
	{
		if (_gunsDropped.containsKey(event.getEntity()))
			event.setCancelled(true);

		else if (_grenadesDropped.containsKey(event.getEntity()))
			event.setCancelled(true);

		else if (_grenadesThrown.containsKey(event.getEntity()))
			event.setCancelled(true);

		else if (_defusalDropped.contains(event.getEntity()))
			event.setCancelled(true);

		else if (_bombItem != null && _bombItem.equals(event.getEntity()))
			event.setCancelled(true);
	}

	@EventHandler
	public void terroristCompass(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		Location target = null;
		if (_bombItem != null)
			target = _bombItem.getLocation();
		else if (_bombHolder != null)
			target = _bombHolder.getLocation();
		else if (_bomb != null)
			target = _bomb.Block.getLocation();

		for (Player player : GetTeam(ChatColor.RED).GetPlayers(true))
		{
			//Has Bomb
			if (player.getInventory().contains(Material.GOLD_SWORD))
				continue;

			//Error - Random Loc
			if (target == null)
				target = player.getLocation().add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);

			//Set Target
			player.setCompassTarget(target);

			ItemStack stack = new ItemStack(Material.COMPASS);

			//Text
			ItemMeta itemMeta = stack.getItemMeta();

			if (_bombItem != null)
			{
				itemMeta.setDisplayName(
						"    " + C.cGreen + C.Bold + "Bomb Dropped" + 
								"    " + C.cGreen + C.Bold + "Distance: " + C.cWhite + UtilMath.trim(1, UtilMath.offset2d(target, player.getLocation())));
			}
			else if (_bombHolder != null)
			{
				itemMeta.setDisplayName(
						"    " + C.cGreen + C.Bold + "Bomb Holder: " + C.cWhite + _bombHolder.getName() +
						"    " + C.cGreen + C.Bold + "Distance: " + C.cWhite + UtilMath.trim(1, UtilMath.offset2d(target, player.getLocation())));
			}
			else if (_bomb != null)
			{
				itemMeta.setDisplayName(
						"    " + C.cGreen + C.Bold + "Bomb Planted" + 
								"    " + C.cGreen + C.Bold + "Distance: " + C.cWhite + UtilMath.trim(1, UtilMath.offset2d(target, player.getLocation())));
			}
			else
			{
				itemMeta.setDisplayName(
						"    " + C.cGreen + C.Bold + "Bomb Not Found");;
			}

			stack.setItemMeta(itemMeta);

			//Set
			player.getInventory().setItem(8, stack);
		}	
	}

	@EventHandler
	public void healthUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		for (Player player : UtilServer.getPlayers())
			_scoreObj.getScore(player.getName()).setScore((int)(player.getHealth() * 5));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void damageHealth(CustomDamageEvent event)
	{
		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;

		_scoreObj.getScore(player.getName()).setScore((int)(player.getHealth() * 5));
	}
	
	@EventHandler
	public void fireDamage(CustomDamageEvent event)
	{
		if (event.GetCause() == DamageCause.FIRE)
			event.AddMod(this.GetName(), "Fire", 3, false);
	}

	@EventHandler
	public void teleportCancel(PlayerTeleportEvent event)
	{
		if (!IsLive())
			return;

		if (event.getCause() == TeleportCause.ENDER_PEARL)
			event.setCancelled(true);
	}

	public int getBulletType()
	{
		return _bulletInstant;
	}

	public void playSound(Radio radio, Player player, GameTeam team)
	{
		if (player == null && team == null)
		{
			for (Player other : UtilServer.getPlayers())
				other.playSound(other.getLocation(), radio.getSound(), 1.5f, 1f);
		}
		else if (team != null)
		{
			for (Player other : team.GetPlayers(false))
				other.playSound(other.getLocation(), radio.getSound(), 1.5f, 1f);
		}
		else if (player != null)
		{
			GameTeam playerTeam = GetTeam(player);
			if (playerTeam == null)
				return;

			for (Player other : playerTeam.GetPlayers(false))
				other.playSound(player.getLocation(), radio.getSound(), 1.5f, 1f);
		}
	}
	
	@EventHandler
	public void debug(PlayerCommandPreprocessEvent event)
	{
		if (!event.getPlayer().isOp())
			return;

		if (event.getMessage().contains("money"))
		{
			_shopManager.addMoney(event.getPlayer(), 16000, "Debug");
			event.setCancelled(true);
		}

		if (event.getMessage().contains("instant"))
		{
			_bulletInstant = (_bulletInstant + 1)%3;

			if (_bulletInstant == 0)
				Announce(C.cPurple + C.Bold + "Bullets: " + ChatColor.RESET + "Slow and Visible");
			else if (_bulletInstant == 1)
				Announce(C.cPurple + C.Bold + "Bullets: " + ChatColor.RESET + "Instant and Invisible");
			else
				Announce(C.cPurple + C.Bold + "Bullets: " + ChatColor.RESET + "Slow and Visible with Instant Sniper");


			event.setCancelled(true);
		}

		if (event.getMessage().contains("hitbox"))
		{
			_customHitbox = !_customHitbox;

			if (_customHitbox)
				Announce(C.cPurple + C.Bold + "Hitbox: " + ChatColor.RESET + "Accurate with Headshots");
			else
				Announce(C.cPurple + C.Bold + "Hitbox: " + ChatColor.RESET + "Default with No Headshot");

			event.setCancelled(true);
		}

		if (event.getMessage().contains("alternate"))
		{
			_bulletAlternate = !_bulletAlternate;

			Announce(C.cPurple + C.Bold + "Alternate Bullet Type: " + ChatColor.RESET + _bulletAlternate);

			event.setCancelled(true);
		}

		if (event.getMessage().contains("god"))
		{
			if (HealthSet == 20)
				HealthSet = -1;
			else
				HealthSet = 20;

			Announce(C.cPurple + C.Bold + "God Mode: " + ChatColor.RESET + (HealthSet == 20));

			event.setCancelled(true);
		}

		if (event.getMessage().contains("debugplayer"))
		{
			Announce(C.Bold + "PLAYER DEBUG:");

			for (Player player : UtilServer.getPlayers())
			{
				GameTeam team = GetTeam(player);

				Announce(player.getName() + "   " + 
						(team != null ? team.GetColor() + team.GetName() : C.cGray + "No Team") + "   " + 
						(IsAlive(player) ? C.cGreen + "ALIVE" : C.cRed + "DEAD") + "   " + 
						C.cGray + UtilWorld.locToStrClean(player.getLocation())
						);
			}

			event.setCancelled(true);
		}

		if (event.getMessage().contains("debugteam"))
		{
			Announce(C.Bold + "TEAM DEBUG:");

			for (GameTeam team : GetTeamList())
				for (Player player : team.GetPlayers(false))
				{
					Announce(player.getName() + "   " + 
							(team != null ? team.GetColor() + team.GetName() : C.cGray + "No Team") + "   " + 
							(IsAlive(player) ? C.cGreen + "ALIVE" : C.cRed + "DEAD") + "   " + 
							C.cGray + UtilWorld.locToStrClean(player.getLocation())
							);
				}

			event.setCancelled(true);
		}
	}

	//Used for fire grenade spread
	public int getRound()
	{
		int rounds = 0;
		
		for (int i : _score.values())
			rounds += i;
		
		return rounds;
	}

	public boolean isFreezeTime()
	{
		return _freezeTime > 0;
	}
}
