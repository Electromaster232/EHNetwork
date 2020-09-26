package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkHorsePet extends Perk
{
	private HashMap<Player, Horse> _horseMap = new HashMap<Player, Horse>();
	private HashMap<Player, Long> _deathTime = new HashMap<Player, Long>();
	
	public PerkHorsePet() 
	{
		super("Horse Master", new String[] 
				{
				C.cGray + "You have a loyal horse companion.",
				});
	}

	@Override
	public void Apply(Player player) 
	{
		spawnHorse(player, false);
	}
	
	public void spawnHorse(Player player, boolean baby)
	{
		if (!Manager.GetGame().IsAlive(player))
			return;
		
		Manager.GetGame().CreatureAllowOverride = true;
		Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
		Manager.GetGame().CreatureAllowOverride = false;

		horse.setAdult();
		horse.setAgeLock(true);
		horse.setColor(Color.BROWN);
		horse.setStyle(Style.NONE);
		horse.setVariant(Variant.HORSE);
		horse.setOwner(player);
		horse.setMaxDomestication(1);
		horse.setJumpStrength(1);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.setMaxHealth(40);
		horse.setHealth(40);
		
		UtilEnt.Vegetate(horse);
		
		_horseMap.put(player, horse);
		
		horse.getWorld().playSound(horse.getLocation(), Sound.HORSE_ANGRY, 2f, 1f);
	}

	@EventHandler
	public void horseUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		//Respawn
		Iterator<Player> respawnIterator = _deathTime.keySet().iterator();
		while (respawnIterator.hasNext())
		{
			Player player = respawnIterator.next();
			
			if (UtilTime.elapsed(_deathTime.get(player), 15000))
			{
				respawnIterator.remove();
				spawnHorse(player, true);
			}
		}
			
		//Update
		Iterator<Player> playerIterator = _horseMap.keySet().iterator();
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			Horse horse = _horseMap.get(player);

			//Dead
			if (!horse.isValid() || horse.isDead())
			{
				horse.getWorld().playSound(horse.getLocation(), Sound.HORSE_DEATH, 1f, 1f);
				_deathTime.put(player, System.currentTimeMillis());
				playerIterator.remove();
				continue;
			}	
			
			//Return to Owner
			if (UtilMath.offset(horse, player) > 3)
			{
				if (UtilMath.offset(horse, player) > 24)
				{
					horse.teleport(player);
					continue;
				}
				
				float speed = Math.min(1f, (float)(UtilMath.offset(horse, player) - 5) / 8f);
				
				UtilEnt.CreatureMove(horse, player.getLocation().add(UtilAlg.getTrajectory(player, horse).multiply(2.5)), 1f + speed);
			}
			
			//Age
			if (horse.getTicksLived() > 900 && !horse.isAdult())
			{
				horse.setAdult();
				horse.getWorld().playSound(horse.getLocation(), Sound.HORSE_ANGRY, 2f, 1f);
				
				UtilPlayer.message(player, F.main("Game", "Your horse is now an adult!"));
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 2f);
				
				horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
			}
		}
	}


	@EventHandler
	public void heal(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;

		for (Horse horse : _horseMap.values())
		{
			if (horse.getHealth() > 0)
				horse.setHealth(Math.min(horse.getMaxHealth(), horse.getHealth()+1));
		}
	}

	@EventHandler
	public void death(PlayerDeathEvent event)
	{
		Horse horse = _horseMap.remove(event.getEntity());

		if (horse == null)
			return;

		horse.remove();
	}

	@EventHandler
	public void damageRider(CustomDamageEvent event)
	{
		if (!(event.GetDamageeEntity() instanceof Horse))
			return;
		
		Horse horse = (Horse)event.GetDamageeEntity();
		
		if (!_horseMap.values().contains(horse))
			return;
		
		if (!horse.isAdult())
			event.SetCancelled("Baby Cancel");
		
		Entity ent = event.GetDamageeEntity().getPassenger();

		if (!(ent instanceof Player))
			return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent((Player)ent, event.GetDamagerEntity(true), event.GetProjectile(), 
				event.GetCause(), event.GetDamage() * 0.5, true, false, false,
				UtilEnt.getName(event.GetDamagerEntity(true)), event.GetReason());	
	}
	
	@EventHandler
	public void mountCancel(PlayerInteractEntityEvent event)
	{
		if (!(event.getRightClicked() instanceof Horse))
			return;
				
		if (!_horseMap.containsValue(event.getRightClicked()))
			return;

		Player player = event.getPlayer();
		Horse horse = (Horse)event.getRightClicked();
		
		if (horse.getOwner() != null && !horse.getOwner().equals(player))
		{
			UtilPlayer.message(player, F.main("Mount", "This is not your Horse!"));
			event.setCancelled(true);
		}
	}
}
