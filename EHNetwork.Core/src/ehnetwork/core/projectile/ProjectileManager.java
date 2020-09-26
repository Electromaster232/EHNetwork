package ehnetwork.core.projectile;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ProjectileManager extends MiniPlugin
{
	private WeakHashMap<Entity, ProjectileUser> _thrown = new WeakHashMap<Entity, ProjectileUser>();
	
	public ProjectileManager(JavaPlugin plugin) 
	{
		super("Throw", plugin);
	}
	
	public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, float hitboxGrow)
	{
		_thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
				expireTime, hitPlayer, hitBlock, idle, false,
				null, 1f, 1f, null, 0, null, null, 0F, 0F, 0F, 0F, 1, hitboxGrow));  
	}
	
	public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, boolean pickup, float hitboxGrow)
	{
		_thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
				expireTime, hitPlayer, hitBlock, idle, pickup, 
				null, 1f, 1f, null, 0, null, null, 0F, 0F, 0F, 0F, 1, hitboxGrow)); 
	}
		
	public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
			Sound sound, float soundVolume, float soundPitch, Effect effect, int effectData, UpdateType effectRate , float hitboxGrow)
	{
		_thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
				expireTime, hitPlayer, hitBlock, idle, false,
				sound, soundVolume, soundPitch, effect, effectData, effectRate, null, 0F, 0F, 0F, 0F, 1, hitboxGrow)); 
	}
	
	public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
			Sound sound, float soundVolume, float soundPitch, ParticleType particle, Effect effect, int effectData, UpdateType effectRate, float hitboxGrow)
	{
		_thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
				expireTime, hitPlayer, hitBlock, idle, false,
				sound, soundVolume, soundPitch, effect, effectData, effectRate, particle, 0F, 0F, 0F, 0F, 1, hitboxGrow)); 
	}
	
	public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
			Sound sound, float soundVolume, float soundPitch, ParticleType particle, UpdateType effectRate, float hitboxMult)
	{
		_thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
				expireTime, hitPlayer, hitBlock, idle, false,
				sound, soundVolume, soundPitch, null, 0, effectRate, particle, 0F, 0F, 0F, 0F, 1, hitboxMult)); 
	}
	
	public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
			Sound sound, float soundVolume, float soundPitch, ParticleType particle, float pX, float pY, float pZ, float pS, int pC, UpdateType effectRate, float hitboxMult)
	{
		_thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
				expireTime, hitPlayer, hitBlock, idle, false,
				sound, soundVolume, soundPitch, null, 0, effectRate, particle, pX, pY, pZ, pS, pC, hitboxMult)); 
	}
		
	@EventHandler
	public void Update(UpdateEvent event)
	{
		//Collisions
		if (event.getType() == UpdateType.TICK)
		{
			for (Iterator<Entry<Entity, ProjectileUser>> iterator = _thrown.entrySet().iterator(); iterator.hasNext();)
			{
				Entry<Entity, ProjectileUser> entry = iterator.next();
				Entity cur = entry.getKey();
				
				if (cur.isDead() || !cur.isValid())
				{
					iterator.remove();
					continue;
				}
				else if (_thrown.get(cur).Collision())
					iterator.remove();
			}
		}
		
		//Effects
		for (ProjectileUser cur : _thrown.values())
			cur.Effect(event);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (event.isCancelled())
			return;

		if (_thrown.containsKey(event.getItem()))
			if (!_thrown.get(event.getItem()).CanPickup(event.getPlayer()))
				event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void HopperPickup(InventoryPickupItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (_thrown.containsKey(event.getItem()))
			event.setCancelled(true);
	}
}
