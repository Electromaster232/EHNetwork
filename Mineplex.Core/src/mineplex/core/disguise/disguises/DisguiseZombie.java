package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;

public class DisguiseZombie extends DisguiseMonster
{
	public DisguiseZombie(Entity entity)
	{
		this(EntityType.ZOMBIE, entity);
	}

	public DisguiseZombie(EntityType disguiseType, Entity entity)
	{
		super(disguiseType, entity);
		
		DataWatcher.a(12, Byte.valueOf((byte)0));
	    DataWatcher.a(13, Byte.valueOf((byte)0));
	    DataWatcher.a(14, Byte.valueOf((byte)0));
	}
	
	public boolean IsBaby()
	{
		return DataWatcher.getByte(12) == 1;
	}
	
	public void SetBaby(boolean baby)
	{
		DataWatcher.watch(12, Byte.valueOf((byte)(baby ? 1 : 0)));
	}
	
	public boolean IsVillager()
	{
		return DataWatcher.getByte(13) == 1;
	}
	
	public void SetVillager(boolean villager)
	{
		DataWatcher.watch(13, Byte.valueOf((byte)(villager ? 1 : 0)));
	}
	
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }
}
