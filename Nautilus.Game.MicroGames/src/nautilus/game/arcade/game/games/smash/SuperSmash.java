package nautilus.game.arcade.game.games.smash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.data.BlockData;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeFormat;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.GameTeam.PlayerState;
import nautilus.game.arcade.game.games.smash.kits.KitBlaze;
import nautilus.game.arcade.game.games.smash.kits.KitChicken;
import nautilus.game.arcade.game.games.smash.kits.KitCreeper;
import nautilus.game.arcade.game.games.smash.kits.KitEnderman;
import nautilus.game.arcade.game.games.smash.kits.KitGolem;
import nautilus.game.arcade.game.games.smash.kits.KitMagmaCube;
import nautilus.game.arcade.game.games.smash.kits.KitPig;
import nautilus.game.arcade.game.games.smash.kits.KitSheep;
import nautilus.game.arcade.game.games.smash.kits.KitSkeletalHorse;
import nautilus.game.arcade.game.games.smash.kits.KitSkeleton;
import nautilus.game.arcade.game.games.smash.kits.KitSkySquid;
import nautilus.game.arcade.game.games.smash.kits.KitSlime;
import nautilus.game.arcade.game.games.smash.kits.KitSnowman;
import nautilus.game.arcade.game.games.smash.kits.KitSpider;
import nautilus.game.arcade.game.games.smash.kits.KitWitch;
import nautilus.game.arcade.game.games.smash.kits.KitWitherSkeleton;
import nautilus.game.arcade.game.games.smash.kits.KitWolf;
import nautilus.game.arcade.game.games.smash.kits.KitZombie;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.stats.FreeKitWinStatTracker;
import nautilus.game.arcade.stats.KillFastStatTracker;
import nautilus.game.arcade.stats.OneVThreeStatTracker;
import nautilus.game.arcade.stats.RecoveryMasterStatTracker;
import nautilus.game.arcade.stats.WinWithoutDyingStatTracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class SuperSmash extends Game
{
	private HashMap<Player, Integer> _lives = new HashMap<Player, Integer>();

	private Location _powerupCurrent = null;
	private Location _powerupTarget = null;
	private EnderCrystal _powerup = null;
	private long _nextPowerup = 0;
	
	private HashSet<BlockData> _restoreBlock = new HashSet<BlockData>();

	public SuperSmash(ArcadeManager manager, GameType type, String[] description) 
	{
		super(manager, type,

				new Kit[]
						{

				new KitSkeleton(manager),
				new KitGolem(manager),
				new KitSpider(manager),
				new KitSlime(manager),

				new KitCreeper(manager),
				new KitEnderman(manager), 
				new KitSnowman(manager),
				new KitWolf(manager),


				new KitBlaze(manager),
				new KitWitch(manager),
				new KitChicken(manager),
				new KitSkeletalHorse(manager),
				new KitPig(manager),
				new KitSkySquid(manager),
				new KitWitherSkeleton(manager),
				new KitMagmaCube(manager),
				new KitZombie(manager),

				new KitSheep(manager)

						},description);

		this.DeathOut = false;

		this.CompassEnabled = true;

		this.DeathSpectateSecs = 4;

		this.WorldWaterDamage = 1000;
		
		this.HideTeamSheep = true;
		
		this.ReplaceTeamsWithKits = true;
		
		// Add stat table here
		// Example
		// Manager.GetStatsManager().addTable("SuperSmashMobStats", "Kills", "Deaths", "Wins", "Losses");

		registerStatTrackers(
				new WinWithoutDyingStatTracker(this, "MLGPro"),
				new FreeKitWinStatTracker(this),
				new OneVThreeStatTracker(this),
				new KillFastStatTracker(this, 3, 10, "TripleKill"),
				new RecoveryMasterStatTracker(this)
				);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		for (Player player : GetPlayers(true))
			_lives.put(player, 4);
		
		_nextPowerup = (long) (System.currentTimeMillis() + 240000 + 360000 * Math.random());
	}

	@EventHandler
	public void PlayerOut(PlayerDeathEvent event)
	{
		if (!LoseLife(event.getEntity()))
		{
			this.SetPlayerState(event.getEntity(), PlayerState.OUT);
		}
	}

	public int GetLives(Player player)
	{
		if (!_lives.containsKey(player))
			return 0;

		if (!IsAlive(player))
			return 0;

		return _lives.get(player);
	}

	public boolean LoseLife(Player player) 
	{
		int lives = GetLives(player) - 1;

		if (lives > 0)
		{
			UtilPlayer.message(player, C.cRed + C.Bold + "You have died!");
			UtilPlayer.message(player, C.cRed + C.Bold + "You have " + lives + " " + (lives == 1 ? "life" : "lives") + " left!");
			player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 2f, 0.5f);

			_lives.put(player, lives);

			return true;
		}
		else
		{
			UtilPlayer.message(player, C.cRed + C.Bold + "You are out of the game!");
			player.playSound(player.getLocation(), Sound.EXPLODE, 2f, 1f);

			return false;
		}
	}

	@EventHandler
	public void powerupSpawn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		if (!IsLive())
			return;

		if (_powerup == null)
		{
			if (System.currentTimeMillis() < _nextPowerup)
				return;

			if (WorldData.GetDataLocs("RED").isEmpty())
				return;

			if (_powerupTarget == null)
			{
				Location newTarget = UtilAlg.Random(WorldData.GetDataLocs("RED"));
				
				_powerupTarget = newTarget;
				_powerupCurrent = _powerupTarget.clone().add(0, 120, 0);
				
				//Blocks
				for (int x=-1 ; x<=1 ; x++)
					for (int z=-1 ; z<=1 ; z++)
					{
						_restoreBlock.add(new BlockData(_powerupTarget.getBlock().getRelative(x, -2, z)));
						_restoreBlock.add(new BlockData(_powerupTarget.getBlock().getRelative(x, -1, z)));
						
						_powerupTarget.getBlock().getRelative(x, -2, z).setType(Material.IRON_BLOCK);
						_powerupTarget.getBlock().getRelative(x, -1, z).setType(Material.QUARTZ_BLOCK);
					}
				
				_powerupTarget.getBlock().getRelative(BlockFace.DOWN).setType(Material.BEACON);	
			}

			if (_powerupTarget.getY() < _powerupCurrent.getY())
			{
				_powerupCurrent.add(0, -2, 0);
				UtilFirework.playFirework(_powerupCurrent, Type.BURST, Color.RED, false, true);
			}
			else
			{
				CreatureAllowOverride = true;
				_powerup = _powerupTarget.getWorld().spawn(_powerupTarget, EnderCrystal.class);
				CreatureAllowOverride = false;

				_powerupTarget.getBlock().getRelative(BlockFace.DOWN).setType(Material.GLASS);
				
				_powerupTarget = null;
				_powerupCurrent = null;
				

				//Restore Blocks
				for (BlockData block : _restoreBlock)
					block.restore(true);
				_restoreBlock.clear();
			}
		}
	}

	@EventHandler
	public void powerupPickup(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (!IsLive())
			return;

		if (_powerup == null)
			return;

		Player best = null;
		double bestDist = 0;

		for (Player player : GetPlayers(true))
		{
			double dist = UtilMath.offset(player, _powerup);

			if (dist > 2)
				continue;

			if (best == null || dist < bestDist)
			{
				best = player;
				bestDist = dist;
			}
		}

		if (best != null)
		{
			_powerup.remove();
			_powerup = null;

			best.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, ((SmashKit)GetKit(best)).getSuperCharges(), 
					C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + ((SmashKit)GetKit(best)).getSuperName()));

			_nextPowerup = (long) (System.currentTimeMillis() + 240000 + 360000 * Math.random());

			Manager.GetGame().Announce(C.Bold + best.getName() + " collected " + C.cGreen + C.Bold + "Smash Crystal" + ChatColor.RESET + C.Bold + "!");
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void powerupDamage(EntityDamageEvent event)
	{
		if (_powerup != null && _powerup.equals(event.getEntity()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void FallDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() == DamageCause.FALL)
			event.SetCancelled("No Fall Damage");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetDamageePlayer() != null)
			event.AddKnockback("Smash Knockback", 1 + 0.1 * (20 - event.GetDamageePlayer().getHealth()));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void ArenaWalls(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() == DamageCause.VOID || event.GetCause() == DamageCause.LAVA)
		{
			event.GetDamageeEntity().eject();
			event.GetDamageeEntity().leaveVehicle();

			if (event.GetDamageePlayer() != null)
				event.GetDamageeEntity().getWorld().strikeLightningEffect(event.GetDamageeEntity().getLocation());

			event.AddMod("Smash", "Super Smash Mobs", 5000, false);
		}	
	}

	@EventHandler
	public void HealthChange(EntityRegainHealthEvent event)
	{
		if (event.getRegainReason() == RegainReason.SATIATED)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void EntityDeath(EntityDeathEvent event)
	{
		event.getDrops().clear();
	}

	@Override
	public void SetKit(Player player, Kit kit, boolean announce) 
	{
		GameTeam team = GetTeam(player);
		if (team != null)
		{
			if (!team.KitAllowed(kit))
			{
				player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 0.5f);
				UtilPlayer.message(player, F.main("Kit", F.elem(team.GetFormattedName()) + " cannot use " + F.elem(kit.GetFormattedName() + " Kit") + "."));
				return;
			}
		}

		_playerKit.put(player, kit);

		if (announce)
		{
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2f, 1f);
			UtilPlayer.message(player, F.main("Kit", "You equipped " + F.elem(kit.GetFormattedName() + " Kit") + "."));
			kit.ApplyKit(player);
			UtilInv.Update(player);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void AbilityDescription(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		if (player.getItemInHand() == null)
			return;

		if (player.getItemInHand().getItemMeta() == null)
			return;

		if (player.getItemInHand().getItemMeta().getDisplayName() == null)
			return;

		if (player.getItemInHand().getItemMeta().getLore() == null)
			return;

		if (Manager.GetGame() == null || Manager.GetGame().GetState() != GameState.Recruit)
			return;

		for (int i=player.getItemInHand().getItemMeta().getLore().size() ; i<=7 ; i++)
			UtilPlayer.message(player, " ");

		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, "§aAbility - §f§l" + player.getItemInHand().getItemMeta().getDisplayName());

		//Perk Descs
		for (String line : player.getItemInHand().getItemMeta().getLore())
		{
			UtilPlayer.message(player, line);
		}

		UtilPlayer.message(player, ArcadeFormat.Line);

		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);

		event.setCancelled(true);
	}

	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		return 4;
	}

	@EventHandler
	public void BlockFade(BlockFadeEvent event)
	{
		event.setCancelled(true);
	}

	private int hungerTick = 0;
	@EventHandler
	public void Hunger(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		if (!IsLive())
			return;

		hungerTick = (hungerTick + 1)%10;

		for (Player player : GetPlayers(true))
		{
			player.setSaturation(3f); 
			player.setExhaustion(0f);

			if (player.getFoodLevel() <= 0)
			{
				Manager.GetDamage().NewDamageEvent(player, null, null, 
						DamageCause.STARVATION, 1, false, true, false,
						"Starvation", GetName());

				UtilPlayer.message(player, F.main("Game", "Attack other players to restore hunger!"));
			}

			if (hungerTick == 0)
				UtilPlayer.hunger(player, -1);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void HungerRestore(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)
			return;

		if (damager.equals(event.GetDamageeEntity()))
			return;
		
		if (!(event.GetDamageeEntity() instanceof Player))
		    return;

		if (!Recharge.Instance.use(damager, "Hunger Restore", 250, false, false))
			return;

		int amount = Math.max(1, (int)(event.GetDamage()/2));
		UtilPlayer.hunger(damager, amount);
	}
}
