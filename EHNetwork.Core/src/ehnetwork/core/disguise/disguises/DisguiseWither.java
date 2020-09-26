package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguiseWither extends DisguiseMonster
{
	public DisguiseWither(org.bukkit.entity.Entity entity)
	{
		super(EntityType.WITHER, entity);
		
        DataWatcher.a(17, new Integer(0));
        DataWatcher.a(18, new Integer(0));
        DataWatcher.a(19, new Integer(0));
        DataWatcher.a(20, new Integer(0));
	}

    public int ca() 
    {
        return DataWatcher.getInt(20);
    }
    
    public void s(int i) 
    {
    	DataWatcher.watch(20, Integer.valueOf(i));
    }
    
    public int t(int i) 
    {
        return DataWatcher.getInt(17 + i);
    }

    public void b(int i, int j) 
    {
    	DataWatcher.watch(17 + i, Integer.valueOf(j));
    }
}
