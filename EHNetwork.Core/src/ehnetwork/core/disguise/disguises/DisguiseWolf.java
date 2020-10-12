package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;
import net.minecraft.server.v1_7_R4.BlockCloth;

public class DisguiseWolf extends DisguiseTameableAnimal
{
	public DisguiseWolf(org.bukkit.entity.Entity entity)
	{
		super(EntityType.WOLF, entity);

		DataWatcher.a(18, new Float(20F));
		DataWatcher.a(19, new Byte((byte)0));
		DataWatcher.a(20, new Byte((byte)BlockCloth.b(1)));
	}
	
	public boolean isAngry() 
	{
		return (DataWatcher.getByte(16) & 0x2) != 0;
	}
	
	public void setAngry(boolean angry)
	{
	    byte b0 = DataWatcher.getByte(16);

	    if (angry)
	    	DataWatcher.watch(16, Byte.valueOf((byte)(b0 | 0x2)));
	    else
	    	DataWatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
	}

	 public int getCollarColor()
	{
		 return DataWatcher.getByte(20) & 0xF;
	}

	public void setCollarColor(int i) 
	{
		DataWatcher.watch(20, Byte.valueOf((byte)(i & 0xF)));
	}
	
	public void m(boolean flag)
	{
	    if (flag)
	    	DataWatcher.watch(19, Byte.valueOf((byte)1));
	    else
	    	DataWatcher.watch(19, Byte.valueOf((byte)0));
	}
	
	public boolean ce()
	{
		return DataWatcher.getByte(19) == 1;
	}
	
    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }
}
