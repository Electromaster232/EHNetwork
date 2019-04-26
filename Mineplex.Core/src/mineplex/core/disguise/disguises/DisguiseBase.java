package mineplex.core.disguise.disguises;

import mineplex.core.common.*;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;

import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;

public abstract class DisguiseBase
{
	protected Entity Entity;
	protected DataWatcher DataWatcher;
	
	private DisguiseBase _soundDisguise;
	
	public boolean Global = true;
	
	public DisguiseBase(org.bukkit.entity.Entity entity)
	{	
		if (entity != null)
		{
			setEntity(entity);
		}

		DataWatcher = new DataWatcher(new DummyEntity(null));
		
		DataWatcher.a(0, Byte.valueOf((byte)0));
		DataWatcher.a(1, Short.valueOf((short)300));
		
		_soundDisguise = this;
	}
	
	public void setEntity(org.bukkit.entity.Entity entity)
	{
		Entity = ((CraftEntity) entity).getHandle();
	}
	
	public void UpdateDataWatcher()
	{
		DataWatcher.watch(0, Entity.getDataWatcher().getByte(0));
		DataWatcher.watch(1, Entity.getDataWatcher().getShort(1));
	}
	
	public abstract Packet GetSpawnPacket();

	public Packet GetMetaDataPacket()
	{
		UpdateDataWatcher();
		return new PacketPlayOutEntityMetadata(Entity.getId(), DataWatcher, true);
	}

	public void setSoundDisguise(DisguiseBase soundDisguise)
	{
		_soundDisguise = soundDisguise;
		
		if (_soundDisguise == null)
			_soundDisguise = this;
	}
	
	public void playHurtSound()
	{
		Entity.world.makeSound(Entity, _soundDisguise.getHurtSound(), _soundDisguise.getVolume(), _soundDisguise.getPitch());
	}
	
	public void playHurtSound(Location location)
	{
		Entity.world.makeSound(location.getX(), location.getY(), location.getZ(), _soundDisguise.getHurtSound(), _soundDisguise.getVolume(), _soundDisguise.getPitch());
	}

	public Entity GetEntity()
	{
		return Entity;
	}

	public int GetEntityId()
	{
		return Entity.getId();
	}
	
	protected abstract String getHurtSound();
	
	protected abstract float getVolume();
	
	protected abstract float getPitch();
}
