package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguiseBat extends DisguiseAnimal
{
	public DisguiseBat(org.bukkit.entity.Entity entity)
	{
		super(EntityType.BAT, entity);
		
		DataWatcher.a(16, new Byte((byte)0));
	}

	public boolean isSitting() 
	{
		return (DataWatcher.getByte(16) & 0x1) != 0;
	}

	public void setSitting(boolean paramBoolean) 
	{
		int i = DataWatcher.getByte(16);
		if (paramBoolean)
			DataWatcher.watch(16, Byte.valueOf((byte)(i | 0x1)));
		else
			DataWatcher.watch(16, Byte.valueOf((byte)(i & 0xFFFFFFFE)));
	}
	
	public String getHurtSound()
	{
		return "mob.bat.hurt";
	}
}