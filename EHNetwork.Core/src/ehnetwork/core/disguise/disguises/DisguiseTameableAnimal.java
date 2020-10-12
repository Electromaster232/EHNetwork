package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public abstract class DisguiseTameableAnimal extends DisguiseAnimal
{
	public DisguiseTameableAnimal(EntityType disguiseType, org.bukkit.entity.Entity entity)
	{
		super(disguiseType, entity);
		
	    DataWatcher.a(16, Byte.valueOf((byte)0));
	    DataWatcher.a(17, "");
	}
	
	public boolean isTamed()
	{
		return (DataWatcher.getByte(16) & 0x4) != 0;
	}
	
	public void setTamed(boolean tamed)
	{
		int i = DataWatcher.getByte(16);
		
		if (tamed)
			DataWatcher.watch(16,  Byte.valueOf((byte)(i | 0x4)));
		else
			DataWatcher.watch(16,  Byte.valueOf((byte)(i | 0xFFFFFFFB)));
	}
	
	public boolean isSitting()
	{
		return (DataWatcher.getByte(16) & 0x1) != 0;
	}
	
	public void setSitting(boolean sitting)
	{
		int i = DataWatcher.getByte(16);
		
		if (sitting)
			DataWatcher.watch(16,  Byte.valueOf((byte)(i | 0x1)));
		else
			DataWatcher.watch(16,  Byte.valueOf((byte)(i | 0xFFFFFFFE)));
	}
	
	public void setOwnerName(String name)
	{
		DataWatcher.watch(17,  name);
	}
	
	public String getOwnerName()
	{
		return DataWatcher.getString(17);
	}
}
