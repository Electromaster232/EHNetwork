package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguiseCreeper extends DisguiseMonster
{
	public DisguiseCreeper(org.bukkit.entity.Entity entity)
	{
		super(EntityType.CREEPER, entity);
		
		DataWatcher.a(16, Byte.valueOf((byte)-1));
		DataWatcher.a(17, Byte.valueOf((byte)0));
	}
	
	public boolean IsPowered()
	{
		return DataWatcher.getByte(17) == 1;
	}
	
	public void SetPowered(boolean powered)
	{
		DataWatcher.watch(17, Byte.valueOf((byte)(powered ? 1 : 0)));
	}
	
	public int bV()
	{
		return DataWatcher.getByte(16);
	}
	
	public void a(int i)
	{
		DataWatcher.watch(16, Byte.valueOf((byte)i));
	}
	
    protected String getHurtSound()
    {
        return "mob.creeper.say";
    }
}
