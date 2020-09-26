package ehnetwork.game.microgames.game.games.smash.kits;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseSkeleton;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJumpHorse;
import ehnetwork.game.microgames.kit.perks.PerkFletcher;
import ehnetwork.game.microgames.kit.perks.PerkHorseKick;
import ehnetwork.game.microgames.kit.perks.PerkKnockbackArrow;
import ehnetwork.game.microgames.kit.perks.PerkNotFinished;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitKnight extends SmashKit
{
	private HashMap<Player, Horse> _mounts = new HashMap<Player, Horse>();
	private HashSet<Horse> _horses = new HashSet<Horse>();

	private HashSet<CustomDamageEvent> _calledEvents = new HashSet<CustomDamageEvent>();

	public KitKnight(MicroGamesManager manager)
	{
		super(manager, "Undead Knight", KitAvailability.Gem, 5000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.2, 0.25, 7.5),
				new PerkFletcher(1, 2, false),	
				new PerkKnockbackArrow(2),
				new PerkDoubleJumpHorse(),
				new PerkHorseKick(),
				new PerkNotFinished()
								}, 
								EntityType.HORSE,
								new ItemStack(Material.IRON_BARDING), 
								"", 0, null);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Horse Kick",
				new String[]
						{

						}));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Coming Soon...",
				new String[]
						{

						}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.IRON_BOOTS));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
	
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		//Disguise
		DisguiseSkeleton disguise = new DisguiseSkeleton(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);

		//Horse
		Manager.GetGame().CreatureAllowOverride = true;
		final Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
		Manager.GetGame().CreatureAllowOverride = false;

		//Owner
		horse.setTamed(true);
		horse.setOwner(player);
		horse.setMaxDomestication(1);

		//Visual
		horse.setColor(Color.DARK_BROWN);
		horse.setStyle(Style.WHITE_DOTS);
		horse.setVariant(Variant.UNDEAD_HORSE);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));

		//Stats
		horse.setAdult();
		horse.setJumpStrength(1);
		horse.setMaxHealth(100);
		horse.setHealth(horse.getMaxHealth());

		_horses.add(horse);
		_mounts.put(player, horse);
	
		/*
		final Player fPlayer = player;

		Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				horse.setPassenger(fPlayer);
			}
		}, 1);
		*/
	}

	@EventHandler
	public void HorseUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Horse> horseIterator = _horses.iterator();

		while (horseIterator.hasNext())
		{
			Horse horse = horseIterator.next();

			if (horse.isValid() && _mounts.containsValue(horse))
				continue;

			horseIterator.remove();
			horse.remove();
		}
	}

	@EventHandler
	public void HorseUpdate(PlayerInteractEntityEvent event)
	{
		if (!(event.getRightClicked() instanceof Horse))
			return;

		Player player = event.getPlayer();
		Horse horse = (Horse)event.getRightClicked();

		if (_mounts.containsKey(player) && _mounts.get(player).equals(horse))
			return;

		UtilPlayer.message(player, F.main("Game", "This is not your " + F.elem("Skeletal Horse") + "!"));
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDamage(CustomDamageEvent event)
	{
		if (_calledEvents.contains(event))
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (damagee.getVehicle() == null)
			return;

		if (!(damagee.getVehicle() instanceof Horse))
			return;

		if (event.GetCause() == DamageCause.SUFFOCATION)
		{
			event.SetCancelled("Horse Suffocation");
			return;
		}

		Horse horse = (Horse)damagee.getVehicle();

		//Damage Event
		CustomDamageEvent newEvent = new CustomDamageEvent(horse, event.GetDamagerEntity(true), event.GetProjectile(), 
				event.GetCause(), event.GetDamageInitial(), true, false, false,
				UtilEnt.getName(event.GetDamagerPlayer(true)), event.GetReason(), false);

		_calledEvents.add(newEvent);
		Manager.getPlugin().getServer().getPluginManager().callEvent(newEvent);
		_calledEvents.remove(newEvent);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void HorseDamage(CustomDamageEvent event)
	{
		if (_calledEvents.contains(event))
			return;

		if (event.GetCause() == DamageCause.THORNS)
			return;

		if (!(event.GetDamageeEntity() instanceof Horse))
			return;

		Horse horse = (Horse)event.GetDamageeEntity();

		if (horse.getPassenger() == null)
			return;

		if (!(horse.getPassenger() instanceof Player))
			return;

		Player player = (Player)horse.getPassenger();

		//Damage Event
		final CustomDamageEvent newEvent = new CustomDamageEvent(player, event.GetDamagerEntity(true), event.GetProjectile(), 
				event.GetCause(), event.GetDamageInitial(), true, false, false,
				UtilEnt.getName(event.GetDamagerPlayer(true)), event.GetReason(), false);

		_calledEvents.add(newEvent);
		Manager.getPlugin().getServer().getPluginManager().callEvent(newEvent);
		_calledEvents.remove(newEvent);
		//Add Knockback
		event.AddKnockback("Knockback Multiplier", 1.2);
	}
	
	@Override
	public Entity SpawnEntity(Location loc)
	{
		EntityType type = _entityType;
		if (type == EntityType.PLAYER)
			type = EntityType.ZOMBIE;

		LivingEntity entity = (LivingEntity) Manager.GetCreature().SpawnEntity(loc, type);

		entity.setRemoveWhenFarAway(false);
		entity.setCustomName(GetAvailability().GetColor() + GetName() + " Kit");
		entity.setCustomNameVisible(true);
		entity.getEquipment().setItemInHand(_itemInHand);

		if (type == EntityType.HORSE)
		{
			Horse horse = (Horse)entity;
			horse.setAdult();
			horse.setColor(Color.DARK_BROWN);
			horse.setStyle(Style.WHITE_DOTS);
			horse.setVariant(Variant.UNDEAD_HORSE);
			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
		}

		UtilEnt.Vegetate(entity);

		SpawnCustom(entity); 

		return entity;
	}
}
