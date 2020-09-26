package ehnetwork.game.microgames.game.games.paintball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.events.PlayerPrepareTeleportEvent;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.GameTeam.PlayerState;
import ehnetwork.game.microgames.game.TeamGame;
import ehnetwork.game.microgames.game.games.paintball.kits.KitMachineGun;
import ehnetwork.game.microgames.game.games.paintball.kits.KitRifle;
import ehnetwork.game.microgames.game.games.paintball.kits.KitShotgun;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.stats.KillFastStatTracker;
import ehnetwork.game.microgames.stats.LastStandStatTracker;
import ehnetwork.game.microgames.stats.MedicStatTracker;
import ehnetwork.game.microgames.stats.WinFastStatTracker;
import ehnetwork.game.microgames.stats.WinWithoutLosingTeammateStatTracker;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Paintball extends TeamGame
{
	public static class ReviveEvent extends PlayerEvent
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

		private final Player _revivedPlayer;

		public ReviveEvent(Player who, Player revivedPlayer)
		{
			super(who);

			_revivedPlayer = revivedPlayer;
		}

		public Player getRevivedPlayer()
		{
			return _revivedPlayer;
		}
	}

	private HashMap<Player, PlayerCopy> _doubles = new HashMap<Player, PlayerCopy>();

	public Paintball(MicroGamesManager manager)
	{
		super(manager, GameType.Paintball,

				new Kit[] 
						{ 
				new KitRifle(manager),
				new KitShotgun(manager),
				new KitMachineGun(manager),
						},

						new String[]
								{
				"Shoot enemies to paint them",
				"Revive/heal with Water Bombs",
				"Last team alive wins!"
								});

		this.StrictAntiHack = true;
		
		this.HungerSet = 20;

		registerStatTrackers(
				new KillFastStatTracker(this, 4, 5, "KillingSpree"),
				new WinWithoutLosingTeammateStatTracker(this, "FlawlessVictory"),
				new MedicStatTracker(this),
				new WinFastStatTracker(this, 30, "Speedrunner"),
				new LastStandStatTracker(this)
		);
	}

	@EventHandler
	public void CustomTeamGeneration(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Recruit)
			return;

		this.GetTeamList().get(0).SetColor(ChatColor.AQUA);
		this.GetTeamList().get(0).SetName("Frost");

		this.GetTeamList().get(1).SetColor(ChatColor.RED);
		this.GetTeamList().get(1).SetName("Nether");
	}

	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void ColorArmor(PlayerPrepareTeleportEvent event)
	{
		CleanColorArmor(event.GetPlayer());
	}

	@EventHandler
	public void HealthRegen(EntityRegainHealthEvent event)
	{
		if (event.getRegainReason() == RegainReason.SATIATED)
			event.setCancelled(true);
	}

	@EventHandler
	public void Teleport(PlayerTeleportEvent event)
	{
		if (event.getCause() == TeleportCause.ENDER_PEARL)
			event.setCancelled(true);
	}

	@EventHandler
	public void Paint(ProjectileHitEvent event)
	{
		if (event.getEntity() instanceof ThrownPotion)	
			return;

		byte color = 3;
		if (event.getEntity() instanceof EnderPearl)
			color = 14;

		Location loc = event.getEntity().getLocation().add(event.getEntity().getVelocity());

		for (Block block : UtilBlock.getInRadius(loc, 1.5d).keySet())
		{
			if (block.getType() != Material.WOOL && block.getType() != Material.STAINED_CLAY)
				continue;

			block.setData(color);
		}

		if (color == 3)		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 8);
		else				loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 10);
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		_doubles.remove(event.getPlayer());
	}

	@EventHandler
	public void DamageCancel(CustomDamageEvent event)
	{
		if (event.GetDamageePlayer() == null)
			event.SetCancelled("Not Player");

		if (event.GetProjectile() == null)
			event.SetCancelled("No Projectile");
	}

	@EventHandler
	public void PaintballDamage(CustomDamageEvent event)
	{
		if (!IsLive())
			return;
			
		if (event.GetProjectile() == null)
			return;

		if (!(event.GetProjectile() instanceof Snowball) && !(event.GetProjectile() instanceof EnderPearl))	
			return;

		//Negate
		event.AddMod("Negate", "Negate", -event.GetDamageInitial(), false);

		event.AddMod("Paintball", "Paintball", 2, true);
		event.AddKnockback("Paintball", 2);

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)		return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)		return;
		
		GameTeam damageeTeam = GetTeam(damagee);
		if (damageeTeam == null)	return;

		GameTeam damagerTeam = GetTeam(damager);
		if (damagerTeam == null)	return;
		
		if (damagerTeam.equals(damageeTeam))
			return;

		//Count
		int count = 1;
		if (GetKit(damager) != null)
		{
			if (GetKit(damager) instanceof KitRifle)
			{
				count = 3;
			}
		}

		//Out
		if (Color(damagee, count))
		{
			for (Player player : UtilServer.getPlayers())
				UtilPlayer.message(player, damageeTeam.GetColor() + damagee.getName() + ChatColor.RESET + " was painted by " + 
					damagerTeam.GetColor() + damager.getName() + ChatColor.RESET + "!");
			
			PlayerOut(damagee);

			AddGems(damager, 2, "Kills", true, true);
		}

		//Hit Sound
		Player player = event.GetDamagerPlayer(true);
		if (player != null)
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 3f);
	}	

	public boolean Color(Player player, int amount)
	{
		//Get Non-Coloured
		ArrayList<ItemStack> nonColored = new ArrayList<ItemStack>();
		for (ItemStack stack : player.getInventory().getArmorContents())
		{
			if (!(stack.getItemMeta() instanceof LeatherArmorMeta))
				continue;

			LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();

			if (meta.getColor().equals(Color.RED) || meta.getColor().equals(Color.AQUA))
				nonColored.add(stack);
		}

		//Color Piece
		for (int i=0 ; i<amount ; i++)
		{
			if (nonColored.isEmpty())
				break;

			ItemStack armor = nonColored.remove(UtilMath.r(nonColored.size()));

			LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
			meta.setColor(Color.PURPLE);
			armor.setItemMeta(meta);
		}

		player.setHealth(Math.min(20, Math.max(2, nonColored.size() * 5 + 1)));

		return nonColored.isEmpty();
	}

	public void PlayerOut(Player player)
	{
		//State
		SetPlayerState(player, PlayerState.OUT);
		player.setHealth(20);

		//Conditions
		Manager.GetCondition().Factory().Blind("Hit", player, player, 1.5, 0, false, false, false);
		Manager.GetCondition().Factory().Cloak("Hit", player, player, 9999, false, false);

		//Settings
		player.setAllowFlight(true);
		player.setFlying(true);
		((CraftPlayer)player).getHandle().spectating = true;
		((CraftPlayer)player).getHandle().k = false;

		player.setVelocity(new Vector(0,1.2,0));

		_doubles.put(player, new PlayerCopy(this, player, GetTeam(player).GetColor()));
	}

//	@EventHandler
//	public void CleanThrow(PlayerInteractEvent event)
//	{
//		if (!IsLive())
//			return;
//		
//		Player player = event.getPlayer();
//
//		if (!UtilGear.isMat(player.getItemInHand(), Material.POTION))
//			return;
//
//		if (!IsAlive(player))
//			return;
//
//		if (!Recharge.Instance.use(player, "Water Bomb", 4000, false, false))
//			return;
//
//		//Use Stock
//		UtilInv.remove(player, Material.POTION, (byte)0, 1);
//
//		//Start
//		ThrownPotion potion = player.launchProjectile(ThrownPotion.class);
//
//		_water.add(potion);
//
//		//Inform
//		UtilPlayer.message(player, F.main("Skill", "You threw " + F.skill("Water Bomb") + "."));
//	}

	@EventHandler
	public void CleanHit(ProjectileHitEvent event)
	{
		if (!IsLive())
			return;
		
		if (!(event.getEntity() instanceof ThrownPotion))
			return;

		if (event.getEntity().getShooter() == null)
			return;

		if (!(event.getEntity().getShooter() instanceof Player))
			return;

		Player thrower = (Player)event.getEntity().getShooter();

		GameTeam throwerTeam = GetTeam(thrower);
		if (throwerTeam == null)	return;

		//Revive
		Iterator<PlayerCopy> copyIterator = _doubles.values().iterator();
		while (copyIterator.hasNext())
		{
			PlayerCopy copy = copyIterator.next();

			GameTeam otherTeam = GetTeam(copy.GetPlayer());
			if (otherTeam == null || !otherTeam.equals(throwerTeam))
				continue;

			if (UtilMath.offset(copy.GetEntity().getLocation().add(0,1,0), event.getEntity().getLocation()) > 3)
				continue;

			PlayerIn(copy.GetPlayer(), copy.GetEntity());
			copyIterator.remove();

			AddGems(thrower, 3, "Revived Ally", true, true);

			Bukkit.getPluginManager().callEvent(new ReviveEvent(thrower, copy.GetPlayer()));
		}

		//Clean
		for (Player player : GetPlayers(true))
		{
			GameTeam otherTeam = GetTeam(player);
			if (otherTeam == null || !otherTeam.equals(throwerTeam))
				continue;

			if (UtilMath.offset(player.getLocation().add(0,1,0), event.getEntity().getLocation()) > 3)
				continue;

			PlayerIn(player, null);
		}
	}

	public void PlayerIn(final Player player, final LivingEntity copy)
	{
		//State
		SetPlayerState(player, PlayerState.IN);
		player.setHealth(20);

		//Teleport
		if (copy != null)
		{
			Location loc = player.getLocation();
			loc.setX(copy.getLocation().getX());
			loc.setY(copy.getLocation().getY());
			loc.setZ(copy.getLocation().getZ());
			player.teleport(loc);
		}

		//Settings
		player.setAllowFlight(false);
		player.setFlying(false);
		((CraftPlayer)player).getHandle().spectating = false;
		((CraftPlayer)player).getHandle().k = true;
		
		//Items
		player.getInventory().remove(Material.WATCH);
		player.getInventory().remove(Material.COMPASS);

		//Clean Armor
		CleanColorArmor(player);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You have been cleaned!"));

		//Delayed Visibility
		if (copy != null)
		{
			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					//Remove Invis
					if (IsAlive(player))
						Manager.GetCondition().EndCondition(player, ConditionType.CLOAK, null);

					//Remove Copy
					copy.remove();
				}
			}, 4);
		}
	}
	
	public void CleanColorArmor(Player player)
	{
		Color color = Color.RED;
		if (Manager.GetColor(player) != ChatColor.RED)
			color = Color.AQUA;
		
		for (ItemStack stack : player.getEquipment().getArmorContents())
		{
			if (!(stack.getItemMeta() instanceof LeatherArmorMeta))
				continue;

			LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
			meta.setColor(color);
			stack.setItemMeta(meta);
		}
	}
	
	@EventHandler
	public void removePotionEffect(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : GetPlayers(true))
			player.removePotionEffect(PotionEffectType.WATER_BREATHING);
	}
}
