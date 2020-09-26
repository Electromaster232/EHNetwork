package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkEndermanDragon extends SmashPerk
{
	private HashMap<Player, EnderDragon> _dragons = new HashMap<Player, EnderDragon>();

	public PerkEndermanDragon() 
	{
		super("Ender Dragon", new String[] 
				{ 
				}, false);
	}
	
	@Override
	public void addSuperCustom(Player player)
	{
		Manager.GetGame().CreatureAllowOverride = true;
		EnderDragon dragon = player.getWorld().spawn(player.getLocation().add(0, 5, 0), EnderDragon.class);
		UtilEnt.Vegetate(dragon);
		Manager.GetGame().CreatureAllowOverride = false;
		
		dragon.setCustomName(C.cYellow + player.getName() + "'s Dragon");
		
		UtilFirework.playFirework(dragon.getLocation(), Type.BALL_LARGE, Color.BLACK, true, true);
		
		_dragons.put(player, dragon);
	}
	
	@Override
	public void removeSuperCustom(Player player)
	{
		EnderDragon dragon = _dragons.remove(player);
		if (dragon == null)
			return;
		
		player.leaveVehicle();
		dragon.remove();
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : _dragons.keySet())
		{
			EnderDragon dragon = _dragons.get(player);
			
			//Mount
			if (dragon.getPassenger() == null || !dragon.getPassenger().equals(player))
			{
				player.leaveVehicle();
				dragon.setPassenger(player);
			}
						
			//Move
			Location target = player.getLocation().add(player.getLocation().getDirection().multiply(40));
			((CraftEnderDragon)dragon).getHandle().setTargetBlock(target.getBlockX(), target.getBlockY(), target.getBlockZ());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void explosionBlocks(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void suffocationCancel(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.SUFFOCATION)
			return;
		
		if (event.GetDamageePlayer() == null)
			return;
		
		if (isSuperActive(event.GetDamageePlayer()))
			event.SetCancelled("Enderman Dragon Suffocate");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void dragonDamageeCancel(CustomDamageEvent event)
	{
		if (event.GetDamagerEntity(false) == null)
			return;
		
		if (_dragons.values().contains(event.GetDamagerEntity(false)))
			event.SetCancelled("Enderman Dragon Damagee");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void dragonDamagerCancel(CustomDamageEvent event)
	{
		if (event.GetDamagerEntity(false) == null)
			return;
		
		if (!_dragons.values().contains(event.GetDamagerEntity(false)))
			return;
		
		event.SetCancelled("Dragon Damage Cancel");
	}
	
	@EventHandler
	public void updateDamageAoe(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : _dragons.keySet())
		{
			EnderDragon dragon = _dragons.get(player);
			
			UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, dragon.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.LONGER, UtilServer.getPlayers());
			
			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (other.equals(player))
					continue;
				
				if (UtilMath.offset(dragon.getLocation().add(0, 4, 0), other.getLocation()) < 6 && Recharge.Instance.use(other, "Hit By Dragon", 1000, false, false))
				{
					//Damage Event
					Manager.GetDamage().NewDamageEvent(other, player, null, 
							DamageCause.CUSTOM, 20, true, true, false,
							player.getName(), GetName());	
				}
			}
		}
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 4);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void voidCancel(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.VOID)
			return;
		
		Player player = event.GetDamageePlayer();
		if (player == null)
			return;
		
		if (!isSuperActive(player))
			return;
		
		event.SetCancelled("Dragon Void Immunity");	
	}
}
