package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;

public class DisguiseCat extends DisguiseTameableAnimal
{
	public DisguiseCat(org.bukkit.entity.Entity entity)
	{
		super(EntityType.OCELOT, entity);

		DataWatcher.a(18, Byte.valueOf((byte)0));
	}
	
	public int getCatType()
	{
		return DataWatcher.getByte(18);
	}

	public void setCatType(int i) 
	{
		DataWatcher.watch(18, Byte.valueOf((byte)i));
	}
	
    protected String getHurtSound()
    {
        return "mob.cat.hitt";
    }
}
