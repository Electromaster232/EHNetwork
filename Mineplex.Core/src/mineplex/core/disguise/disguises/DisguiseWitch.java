package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;

public class DisguiseWitch extends DisguiseMonster
{
	public DisguiseWitch(org.bukkit.entity.Entity entity)
	{
		super(EntityType.WITCH, entity);

		DataWatcher.a(21, Byte.valueOf((byte)0));
	}
	
	public String getHurtSound()
	{
	    return "mob.witch.hurt";
	}

	public void a(boolean flag) 
	{
		DataWatcher.watch(21, Byte.valueOf((byte)(flag ? 1 : 0)));
	}
	
	public boolean bT() 
	{
	    return DataWatcher.getByte(21) == 1;
	}
}
