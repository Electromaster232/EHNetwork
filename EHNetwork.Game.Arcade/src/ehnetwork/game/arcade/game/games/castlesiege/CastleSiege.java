package ehnetwork.game.arcade.game.games.castlesiege;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.events.PlayerGameRespawnEvent;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitHumanKnight;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitHumanMarksman;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitHumanPeasant;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitUndeadArcher;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitUndeadGhoul;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitUndeadZombie;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.NullKit;
import ehnetwork.game.arcade.stats.BloodThirstyStatTracker;
import ehnetwork.game.arcade.stats.KingDamageStatTracker;
import ehnetwork.game.arcade.stats.KingSlayerStatTracker;
import ehnetwork.game.arcade.stats.WinAsTeamStatTracker;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class CastleSiege extends TeamGame
{
	public static class KingDamageEvent extends PlayerEvent
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

		private final double _damage;

		public KingDamageEvent(Player who, double damage)
		{
			super(who);

			_damage = damage;
		}

		public double getDamage()
		{
			return _damage;
		}
	}

	public static class KingSlaughterEvent extends PlayerEvent
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

		public KingSlaughterEvent(Player who)
		{
			super(who);
		}
	}

	private ArrayList<String> _lastScoreboard = new ArrayList<String>();

	private long _tntSpawn = 0;
	private ArrayList<Location> _tntSpawns = new ArrayList<Location>();
	private ArrayList<Location> _tntWeakness = new ArrayList<Location>();

	private HashMap<Player, FallingBlock> _tntCarry = new HashMap<Player, FallingBlock>();
	private HashSet<Player> _tntCarryEnd = new HashSet<Player>();

	private ArrayList<Location> _kingLocs;
	private Creature _king;
	private Location _kingLoc;
	private String _kingName;
	private Player _kingDamager = null;
	private int _kingHealth = 40;

	private ArrayList<Location> _peasantSpawns;
	private ArrayList<Location> _horseSpawns;

	public CastleSiege(ArcadeManager manager)
	{
		super(manager, GameType.CastleSiege,

				new Kit[]
						{
								new KitHumanMarksman(manager),
								new KitHumanKnight(manager),
								//new KitHumanBrawler(manager),
								//new KitHumanAssassin(manager),
								new NullKit(manager),
								new KitHumanPeasant(manager),
								new NullKit(manager),
								new KitUndeadGhoul(manager),
								new KitUndeadArcher(manager),
								new KitUndeadZombie(manager),
						},

				new String[]
						{
								F.elem(C.cAqua + "Defenders") + C.cWhite + " must defend the King.",
								F.elem(C.cAqua + "Defenders") + C.cWhite + " win when the sun rises.",
								F.elem(C.cAqua + "Defenders") + C.cWhite + " respawn as wolves.",
								"",
								F.elem(C.cRed + "Undead") + C.cWhite + " must kill the King.",
								F.elem(C.cRed + "Undead") + C.cWhite + " lose when the sun rises.",

						});

		_help = new String[]
				{
						"Marksmen are extremely important to defence!",
						"It's recommended 50%+ of defence are Marksmen.",
						"Use Barricades to block the Undeads path.",
						"Use TNT to destroy weak points in walls.",
						"Weak points are marked by cracked stone brick.",
						"Undead can break fences with their axes.",
						"Undead Archers must pick up arrows from the ground.",

				};

		this.StrictAntiHack = true;
		
		this.HungerSet = 20;
		this.DeathOut = false;
		this.WorldTimeSet = 14000; //14000
		this.BlockPlaceAllow.add(85);

		_kingName = C.cYellow + C.Bold + "King Sparklez";

		GameTeam notRedTeam = null;
		for (GameTeam team : GetTeamList())
		{
			if (team.GetColor() != ChatColor.RED)
			{
				notRedTeam = team;
				break;
			}
		}

		registerStatTrackers(
				new WinAsTeamStatTracker(this, notRedTeam, "ForTheKing"),
				new KingSlayerStatTracker(this),
				new BloodThirstyStatTracker(this),
				new KingDamageStatTracker(this)
		);
	}

	@Override
	public void ParseData()
	{
		_tntSpawns = WorldData.GetDataLocs("RED");
		_tntWeakness = WorldData.GetDataLocs("BLACK");

		_kingLocs = WorldData.GetDataLocs("YELLOW");

		_peasantSpawns = WorldData.GetDataLocs("GREEN");
		_horseSpawns = WorldData.GetDataLocs("BROWN");
	}

	@Override
	public void RestrictKits()
	{
		for (Kit kit : GetKits())
		{
			for (GameTeam team : GetTeamList())
			{
				if (team.GetColor() == ChatColor.RED)
				{
					if (kit.GetName().contains("Castle"))
						team.GetRestrictedKits().add(kit);

					team.SetName("Undead");
				}
				else
				{
					if (kit.GetName().contains("Undead"))
						team.GetRestrictedKits().add(kit);

					team.SetRespawnTime(8);

					team.SetName("Defenders");
				}
			}
		}
	}

	@EventHandler
	public void MoveKits(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		for (int i = 0; i < WorldData.GetDataLocs("PINK").size() && i < 3; i++)
		{
			if (GetKits().length <= 5 + i)
				continue;

			this.CreatureAllowOverride = true;
			Entity ent = GetKits()[5 + i].SpawnEntity(WorldData.GetDataLocs("PINK").get(i));
			this.CreatureAllowOverride = false;

			Manager.GetLobby().AddKitLocation(ent, GetKits()[5 + i], WorldData.GetDataLocs("PINK").get(i));
		}
	}

	@EventHandler
	public void HorseSpawn(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		for (Location loc : _horseSpawns)
		{
			this.CreatureAllowOverride = true;
			Horse horse = loc.getWorld().spawn(loc, Horse.class);
			this.CreatureAllowOverride = false;

			horse.setAdult();
			horse.setAgeLock(true);
			horse.setColor(org.bukkit.entity.Horse.Color.BLACK);
			horse.setStyle(Style.BLACK_DOTS);
			horse.setVariant(Variant.HORSE);
			horse.setMaxDomestication(1);
			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));

			horse.setMaxHealth(60);
			horse.setHealth(horse.getMaxHealth());

			horse.setCustomName("War Horse");
		}
	}

	@EventHandler
	public void HorseInteract(PlayerInteractEntityEvent event)
	{
		if (!(event.getRightClicked() instanceof Horse))
			return;

		Player player = event.getPlayer();
		GameTeam team = GetTeam(player);

		if (team == null || team.GetColor() == ChatColor.RED || !IsAlive(player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void HorseDamageCancel(CustomDamageEvent event)
	{
		if (!(event.GetDamageeEntity() instanceof Horse))
			return;

		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;

		if (!IsAlive(player))
			return;

		if (GetTeam(player) == null)
			return;

		if (GetTeam(player).GetColor() == ChatColor.RED)
			return;

		event.SetCancelled("Horse Team Damage");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		//Spawn King
		this.CreatureAllowOverride = true;

		_kingLoc = _kingLocs.get(UtilMath.r(_kingLocs.size()));

		_king = (Creature) _kingLoc.getWorld().spawnEntity(_kingLoc, EntityType.ZOMBIE);

		_king.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		_king.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		_king.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		_king.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		_king.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));

		_king.setCustomName(_kingName);
		_king.setCustomNameVisible(true);

		_king.setRemoveWhenFarAway(false);

		this.CreatureAllowOverride = false;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void SetDefenderRespawn(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		//Change to Peasant Spawns
		this.GetTeam(ChatColor.AQUA).SetSpawns(_peasantSpawns);
	}


	@EventHandler
	public void KingTarget(EntityTargetEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void KingDamage(CustomDamageEvent event)
	{
		if (_king == null || !_king.isValid())
			return;

		if (!event.GetDamageeEntity().equals(_king))
			return;

		event.SetCancelled("King Damage");

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null) return;

		GameTeam team = GetTeam(damager);

		if (team != null && team.GetColor() == ChatColor.RED)
		{
			if (!Recharge.Instance.use(damager, "Damage King", 400, false, false))
				return;

			_king.playEffect(EntityEffect.HURT);

			_kingDamager = damager;
			_kingHealth--;

			Bukkit.getPluginManager().callEvent(new KingDamageEvent(damager, 1));

			if (_kingHealth < 0)
				_kingHealth = 0;

			WriteScoreboard();

			if (_kingHealth <= 0)
				_king.damage(500);

			EndCheck();
		}
	}

	@EventHandler
	public void KingUpdate(UpdateEvent event)
	{
		if (GetState() != GameState.Live)
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		if (_king == null)
			return;

		if (UtilMath.offset(_king.getLocation(), _kingLoc) > 6)
		{
			_king.teleport(_kingLoc);
		}
		else
		{
			UtilEnt.CreatureMove(_king, _kingLoc, 1f);
		}
	}

	@EventHandler
	public void PlayerDeath(PlayerGameRespawnEvent event)
	{
		if (GetTeam(ChatColor.AQUA).HasPlayer(event.GetPlayer()))
			SetKit(event.GetPlayer(), GetKits()[3], true);
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		WriteScoreboard();
	}

	public void WriteScoreboard()
	{
		//Get Values
		HashMap<String, Integer> _scoreGroup = new HashMap<String, Integer>();
		_scoreGroup.put(C.cAqua + "Defenders", 0);
		_scoreGroup.put(C.cDAqua + "Wolves", 0);
		_scoreGroup.put(C.cRed + "Undead", 0);

		for (Player player : UtilServer.getPlayers())
		{
			if (!IsAlive(player))
				continue;

			Kit kit = GetKit(player);
			if (kit == null) continue;

			if (kit.GetName().contains("Castle"))
			{
				if (kit.GetName().contains("Wolf"))
				{
					_scoreGroup.put(C.cDAqua + "Wolves", 1 + _scoreGroup.get(C.cDAqua + "Wolves"));
				}
				else
				{
					_scoreGroup.put(C.cAqua + "Defenders", 1 + _scoreGroup.get(C.cAqua + "Defenders"));
				}
			}
			else if (kit.GetName().contains("Undead"))
			{
				_scoreGroup.put(C.cRed + "Undead", 1 + _scoreGroup.get(C.cRed + "Undead"));
			}
		}

		//Wipe Last
		Scoreboard.Reset();

		//Teams
		for (String group : _scoreGroup.keySet())
		{
			Scoreboard.WriteBlank();
			Scoreboard.Write(group);
			Scoreboard.Write(ChatColor.getLastColors(group) + _scoreGroup.get(group) + " Players");
		}

		//King
		if (_king != null && _king.isValid())
		{
			Scoreboard.WriteBlank();
			Scoreboard.Write(C.cYellow + C.Bold + "King");
			Scoreboard.Write(_kingHealth + " Health");
		}

		long timeLeft = 24000 - WorldTimeSet;
		timeLeft = timeLeft / 20 * 1000;


		if (timeLeft > 0)
		{
			Scoreboard.WriteBlank();
			Scoreboard.Write(C.cYellow + C.Bold + "Sun Rise");
			Scoreboard.Write(UtilTime.MakeStr(timeLeft, 0));
		}
		else
		{
			Scoreboard.WriteBlank();
			Scoreboard.Write(C.cYellow + C.Bold + "Sun Rise");
			Scoreboard.Write("Undead Burning!");

			for (Player player : GetTeam(ChatColor.RED).GetPlayers(true))
				Manager.GetCondition().Factory().Ignite("Sun Damage", player, player, 5, false, false);
		}

		Scoreboard.Draw();
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		if (this.WorldTimeSet > 24100)
		{
			SetCustomWinLine(_kingName + ChatColor.RESET + " has survived the siege!");

			AnnounceEnd(GetTeam(ChatColor.AQUA));

			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
					{
						AddGems(player, 10, "Winning Team", false, false);
					}
				}

				for (Player player : team.GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
			}

			SetState(GameState.End);
		}

		if (!_king.isValid())
		{
			if (_kingDamager != null)
			{
				SetCustomWinLine(C.cRed + _kingDamager.getName() + C.cWhite + " slaughtered " + _kingName + ChatColor.RESET + "!");
				AddGems(_kingDamager, 20, "King Slayer", false, false);

				Bukkit.getPluginManager().callEvent(new KingSlaughterEvent(_kingDamager));
			}
			else
				SetCustomWinLine(_kingName + ChatColor.RESET + " has died!");

			AnnounceEnd(GetTeam(ChatColor.RED));

			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
					{
						AddGems(player, 10, "Winning Team", false, false);
					}
				}

				for (Player player : team.GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
			}

			SetState(GameState.End);
		}
	}

	@EventHandler
	public void TNTSpawn(UpdateEvent event)
	{
		if (GetState() != GameState.Live)
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		if (!UtilTime.elapsed(this.GetStateTime(), 20000))
			return;

		if (!UtilTime.elapsed(_tntSpawn, 25000))
			return;

		if (_tntSpawns.isEmpty())
			return;

		Location loc = _tntSpawns.get(UtilMath.r(_tntSpawns.size()));

		if (loc.getBlock().getTypeId() == 46)
			return;

		loc.getBlock().setTypeId(46);
		_tntSpawn = System.currentTimeMillis();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void TNTPickup(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		if (event.getClickedBlock().getTypeId() != 46)
			return;

		event.setCancelled(true);

		Player player = event.getPlayer();

		if (!IsAlive(player))
			return;

		if (!GetTeam(ChatColor.RED).HasPlayer(player))
			return;

		if (_tntCarry.containsKey(player))
			return;

		event.getClickedBlock().setTypeId(0);

		FallingBlock tnt = player.getWorld().spawnFallingBlock(player.getEyeLocation(), 46, (byte) 0);

		player.eject();
		player.setPassenger(tnt);

		_tntCarry.put(player, tnt);

		UtilPlayer.message(player, F.main("Game", "You picked up " + F.skill("TNT") + "."));
		UtilPlayer.message(player, F.main("Game", F.elem("Right-Click") + " to detonate yourself."));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void TNTUse(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		Player player = event.getPlayer();

		if (!_tntCarry.containsKey(player))
			return;

		event.setCancelled(true);

		for (Location loc : _tntSpawns)
		{
			if (UtilMath.offset(player.getLocation(), loc) < 16)
			{
				UtilPlayer.message(player, F.main("Game", "You cannot " + F.skill("Detonate") + " so far from the Castle."));
				return;
			}
		}

		_tntCarry.remove(player).remove();

		TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
		tnt.setFuseTicks(0);
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Detonate") + "."));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void TNTDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();

		if (!_tntCarry.containsKey(player))
			return;

		//Remove the TNT
		_tntCarry.get(player).remove();

		//Flag for Removal
		_tntCarryEnd.add(player);


		TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
		tnt.setFuseTicks(0);
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Detonate") + "."));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void TNTDamageDivert(ProjectileHitEvent event)
	{
		Iterator<Player> playerIterator = _tntCarry.keySet().iterator();

		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();

			if (player.getPassenger() == null)
				continue;

			double dist = UtilMath.offset(player.getPassenger().getLocation(), event.getEntity().getLocation().add(event.getEntity().getVelocity()));

			if (dist < 2)
			{
				int damage = (int) (5 * (event.getEntity().getVelocity().length() / 3d));

				//Damage Event
				Manager.GetDamage().NewDamageEvent(player, (LivingEntity)event.getEntity().getShooter(), event.getEntity(),
						DamageCause.CUSTOM, damage, true, false, false,
						null, GetName());

				event.getEntity().remove();
			}

			if (_tntCarryEnd.contains(player))
			{
				playerIterator.remove();
			}
		}

		_tntCarryEnd.clear();
	}


	@EventHandler
	public void TNTExpire(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		Iterator<Player> tntIterator = _tntCarry.keySet().iterator();

		while (tntIterator.hasNext())
		{
			Player player = tntIterator.next();
			FallingBlock block = _tntCarry.get(player);

			if (player.isDead() || !block.isValid() || block.getTicksLived() > 1500)
			{
				player.eject();
				block.remove();

				TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
				tnt.setFuseTicks(0);

				tntIterator.remove();
				continue;
			}

			//Firework
			UtilFirework.playFirework(player.getEyeLocation(), Type.BURST, Color.RED, false, false);
		}
	}

	@EventHandler
	public void TNTWeakness(ExplosionPrimeEvent event)
	{
		Location weakness = null;
		for (Location loc : _tntWeakness)
		{
			if (UtilMath.offset(loc, event.getEntity().getLocation()) < 4)
			{
				weakness = loc;
				break;
			}
		}

		if (weakness == null)
			return;

		_tntWeakness.remove(weakness);

		final Location extra = weakness;

		for (int i = 0; i < 10; i++)
		{
			Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					TNTPrimed tnt = extra.getWorld().spawn(extra.clone().add(3 - UtilMath.r(6), 5 + UtilMath.r(2), 3 - UtilMath.r(6)), TNTPrimed.class);
					tnt.setFuseTicks(0);
					tnt.setIsIncendiary(true);
				}
			}, i * 3);
		}

		weakness.getWorld().playSound(weakness, Sound.EXPLODE, 16f, 0.8f);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void AttackerBlockBreak(org.bukkit.event.block.BlockBreakEvent event)
	{
		GameTeam team = GetTeam(event.getPlayer());
		if (team == null)
			return;

		if (team.GetColor() != ChatColor.RED)
			return;

		if (event.getBlock().getTypeId() == 85)
			event.setCancelled(false);
	}

	@EventHandler
	public void DefenderBlockPlace(org.bukkit.event.block.BlockPlaceEvent event)
	{
		GameTeam team = GetTeam(event.getPlayer());
		if (team == null)
			return;

		if (team.GetColor() != ChatColor.AQUA)
			return;

		if (event.getBlock().getTypeId() != 85)
			return;

		for (Block block : UtilBlock.getSurrounding(event.getBlock(), false))
		{
			if (block.isLiquid())
			{
				event.setCancelled(true);
				UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot place " + F.elem("Barricade") + " in water."));
			}
		}

		if (event.getBlockAgainst().getTypeId() == 85)
		{
			event.setCancelled(true);
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot place " + F.elem("Barricade") + " on each other."));
		}

		if (_king != null && UtilMath.offset(_king.getLocation(), event.getBlock().getLocation().add(0.5, 0.5, 0.5)) < 4)
		{
			event.setCancelled(true);
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot place " + F.elem("Barricade") + " near " + F.elem(C.cAqua + _kingName) + "."));
		}
	}
	
	@EventHandler
	public void DefenderBlockInteract(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;
		
		GameTeam team = GetTeam(event.getPlayer());
		if (team == null)
			return;
	
		if (team.GetColor() != ChatColor.AQUA)
			return;

		if (event.getClickedBlock() == null || event.getClickedBlock().getTypeId() != 85)
			return;

		UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5), 0, 0, 0, 0, 1,
				ViewDist.LONG, UtilServer.getPlayers());
		event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.NOTE_STICKS, 2f, 1f);
		Manager.GetBlockRestore().Add(event.getClickedBlock(), 0, (byte)0, 1000);
	}

	@EventHandler
	public void DayTimer(UpdateEvent event)
	{
		if (GetState() != GameState.Live)
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		WorldTimeSet = (WorldTimeSet + 1);
	}

	@EventHandler
	public void SnowDamage(UpdateEvent event)
	{
		if (GetState() != GameState.Live)
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		for (Player player : GetPlayers(true))
			if (player.getLocation().getBlock().getTypeId() == 78)
			{
				//Damage Event
				Manager.GetDamage().NewDamageEvent(player, null, null,
						DamageCause.DROWNING, 2, false, true, false,
						"Snow", "Snow Damage");

				player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 80);
			}
	}
}
