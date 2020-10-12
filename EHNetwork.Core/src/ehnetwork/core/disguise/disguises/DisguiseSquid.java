package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguiseSquid extends DisguiseMonster
{
	public DisguiseSquid(org.bukkit.entity.Entity entity)
	{
		super(EntityType.SQUID, entity);
		
		DataWatcher.a(16, new Byte((byte)0));
	}
	
	public boolean bT() 
	{
		return (DataWatcher.getByte(16) & 0x01) != 0;
	}

	public void a(boolean flag)
	{
		byte b0 = DataWatcher.getByte(16);
		
		if (flag)
			b0 = (byte)(b0 | 0x1);
		else
			b0 = (byte)(b0 & 0xFFFFFFFE);
		
		DataWatcher.watch(16, Byte.valueOf(b0));
	}
	
    protected String getHurtSound()
    {
        return "damage.hit";
    }
    
    protected float getVolume()
    {
        return 0.4F;
    }
}
