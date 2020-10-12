package ehnetwork.core.projectile;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;
import net.minecraft.server.v1_8_R3.Vec3D;

import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ProjectileUser 
{
	public ProjectileManager Throw;
	
	private Entity _thrown;
	private LivingEntity _thrower;
	private IThrown _callback;
	
	private long _expireTime;
	private boolean _hitPlayer = false;
	private boolean _hitBlock = false;
	private boolean _idle = false;
	private boolean _pickup = false;
	
	private Sound _sound = null;
	private float _soundVolume = 1f;
	private float _soundPitch = 1f;
	
	private ParticleType _particle = null;
	private float _particleX = 0F;
	private float _particleY = 0F;
	private float _particleZ = 0F;
	private float _particleS = 0F;
	private int _particleC = 1;
	
	private Effect _effect = null;
	private int _effectData = 0;
	private UpdateType _effectRate = UpdateType.TICK;
	
	private double _hitboxGrow;
	
	public ProjectileUser(ProjectileManager throwInput, Entity thrown, LivingEntity thrower, IThrown callback, 
			long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, boolean pickup,
			Sound sound, float soundVolume, float soundPitch, 
			Effect effect, int effectData, UpdateType effectRate,
			ParticleType particle, float particleX, float particleY, 
			float particleZ, float particleS, int particleC, double hitboxMult) 
	{
		Throw = throwInput;

		_thrown = thrown;
		_thrower = thrower;
		_callback = callback;
		
		_expireTime = expireTime;
		_hitPlayer = hitPlayer;
		_hitBlock = hitBlock;
		_idle = idle;
		_pickup = pickup;
		
		_sound = sound;
		_soundVolume = soundVolume;
		_soundPitch = soundPitch;
		_particle = particle;
		_particleX = particleX;
		_particleY = particleY;
		_particleZ = particleZ;
		_particleS = particleS;
		_particleC = particleC;
		_effect = effect;
		_effectData = effectData;
		_effectRate = effectRate;
		
		_hitboxGrow = hitboxMult;
	}

	public void Effect(UpdateEvent event)
	{
		if (event.getType() != _effectRate)
			return;
		
		if (_sound != null)
			_thrown.getWorld().playSound(_thrown.getLocation(), _sound, _soundVolume, _soundPitch);
		
		if (_effect != null)
			_thrown.getWorld().playEffect(_thrown.getLocation(), _effect, _effectData);
		
		if (_particle != null)
			UtilParticle.PlayParticle(_particle, _thrown.getLocation(), _particleX, _particleY, _particleZ, _particleS, _particleC,
					ViewDist.LONG, UtilServer.getPlayers());

	}

	public boolean Collision() 
	{
		if (_expireTime != -1 && System.currentTimeMillis() > _expireTime)
		{
			_callback.Expire(this);
			return true;
		}
		
		if (_hitPlayer)
		{
			double distanceToEntity = 0.0D;
			LivingEntity victim = null;
			
			net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity)_thrown).getHandle();
			Vec3D vec3d = new Vec3D(nmsEntity.locX, nmsEntity.locY, nmsEntity.locZ).a();
			Vec3D vec3d1 = new Vec3D(nmsEntity.locX + nmsEntity.motX, nmsEntity.locY + nmsEntity.motY, nmsEntity.locZ + nmsEntity.motZ).a();
			
			MovingObjectPosition finalObjectPosition = nmsEntity.world.rayTrace(vec3d, vec3d1, false, true, false);
			vec3d = new Vec3D(nmsEntity.locX, nmsEntity.locY, nmsEntity.locZ).a();
			vec3d1 = new Vec3D(nmsEntity.locX + nmsEntity.motX, nmsEntity.locY + nmsEntity.motY, nmsEntity.locZ + nmsEntity.motZ).a();

			if (finalObjectPosition != null)
			{
				vec3d1 = new Vec3D(finalObjectPosition.pos.a, finalObjectPosition.pos.b, finalObjectPosition.pos.c).a();
			}
			
			for (Object entity : ((CraftWorld)_thrown.getWorld()).getHandle().getEntities(((CraftEntity)_thrown).getHandle(), 
					((CraftEntity)_thrown).getHandle().getBoundingBox().a(
							((CraftEntity)_thrown).getHandle().motX, 
							((CraftEntity)_thrown).getHandle().motY, 
							((CraftEntity)_thrown).getHandle().motZ).grow(_hitboxGrow, _hitboxGrow, _hitboxGrow)))
			{
	            if (entity instanceof net.minecraft.server.v1_8_R3.Entity)
	            {
	                Entity bukkitEntity = ((net.minecraft.server.v1_8_R3.Entity) entity).getBukkitEntity();
	                
					if (bukkitEntity instanceof LivingEntity)
					{
						LivingEntity ent = (LivingEntity)bukkitEntity;
						
						//Avoid Self
						if (ent.equals(_thrower))
							continue;
						
						//Creative or Spec
						if (ent instanceof Player)
							if (((Player)ent).getGameMode() == GameMode.CREATIVE || UtilPlayer.isSpectator(ent))
								continue;

						//float f1 = (float)(nmsEntity.boundingBox.a() * 0.6f);
						AxisAlignedBB axisalignedbb1 = ((CraftEntity)ent).getHandle().getBoundingBox().grow(1F, 1F, 1F);
						MovingObjectPosition entityCollisionPosition = axisalignedbb1.a(vec3d, vec3d1);
						
						if (entityCollisionPosition != null)
						{
							if (_thrower instanceof Player)
								((Player)_thrower).playSound(_thrower.getLocation(), Sound.ORB_PICKUP, 1f, 1.25f);
							
							double d1 = vec3d.distanceSquared(entityCollisionPosition.pos);
							if ((d1 < distanceToEntity) || (distanceToEntity == 0.0D))
							{
								victim = ent;
								distanceToEntity = d1;
							}
						}
					}
	            }
			}
			
			if (victim != null)
			{
				finalObjectPosition = new MovingObjectPosition(((CraftLivingEntity)victim).getHandle());
				
				_callback.Collide(victim, null, this);
				return true;
			}
			
			if (finalObjectPosition != null)
			{
				if (_hitBlock)
				{
					Block block = _thrown.getWorld().getBlockAt(new Location(_thrown.getWorld(), finalObjectPosition.a().getX(), finalObjectPosition.a().getY(), finalObjectPosition.a().getZ()));
					if (!UtilBlock.airFoliage(block) && !block.isLiquid())
					{
						nmsEntity.motX = ((float) (finalObjectPosition.pos.a - nmsEntity.locX));
						nmsEntity.motY = ((float) (finalObjectPosition.pos.b - nmsEntity.locY));
						nmsEntity.motZ = ((float) (finalObjectPosition.pos.c - nmsEntity.locZ));
						float f2 = MathHelper.sqrt(nmsEntity.motX * nmsEntity.motX + nmsEntity.motY * nmsEntity.motY + nmsEntity.motZ * nmsEntity.motZ);
						nmsEntity.locX -= nmsEntity.motX / f2 * 0.0500000007450581D;
						nmsEntity.locY -= nmsEntity.motY / f2 * 0.0500000007450581D;
						nmsEntity.locZ -= nmsEntity.motZ / f2 * 0.0500000007450581D;
						
						_callback.Collide(null, block, this);
						return true;
					}
				}
			}
		}
		
		try
		{
			//Idle
			if (_idle)
			{
				if (_thrown.getVelocity().length() < 0.2 &&
						!UtilBlock.airFoliage(_thrown.getLocation().getBlock().getRelative(BlockFace.DOWN)))
				{
					_callback.Idle(this);
					return true;
				}
			}
		}
		catch (Exception ex)
		{
			if (_hitBlock)
			{
				return true;
			}
			
			if (_idle)
			{
				return true;
			}
		}
		
		return false;
	}

	public LivingEntity GetThrower() 
	{
		return _thrower;
	}

	public Entity GetThrown() 
	{
		return _thrown;
	}

	public boolean CanPickup(LivingEntity thrower)
	{
		if (!thrower.equals(_thrower))
			return false;
		
		return _pickup;
	}
}
