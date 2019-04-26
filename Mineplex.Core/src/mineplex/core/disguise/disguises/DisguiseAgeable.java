package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;
import org.spigotmc.ProtocolData;

public abstract class DisguiseAgeable extends DisguiseCreature
{
	public DisguiseAgeable(EntityType disguiseType, org.bukkit.entity.Entity entity)
	{
		super(disguiseType, entity);
		
		DataWatcher.a(12, new ProtocolData.IntByte(0, (byte)0));
	}
	
	public void UpdateDataWatcher()
	{
		super.UpdateDataWatcher();

		DataWatcher.watch(12, DataWatcher.getIntByte(12));
	}
	
	public boolean isBaby()
	{
		return DataWatcher.getIntByte(12).value < 0;
	}
	
	public void setBaby()
	{
		DataWatcher.watch(12, new ProtocolData.IntByte(-24000, (byte) ( -1 )));
	}
}
