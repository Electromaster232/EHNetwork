package mineplex.core.disguise.disguises;

import org.spigotmc.ProtocolData;

public abstract class DisguiseHuman extends DisguiseLiving
{
	public DisguiseHuman(org.bukkit.entity.Entity entity)
	{
		super(entity);
		
		DataWatcher.a(10, new ProtocolData.HiddenByte( (byte) 0 ) ); // Spigot - protocol patch, handle new metadata value
	    DataWatcher.a(16, new ProtocolData.DualByte( (byte) 0, (byte) 0 ) );
	    DataWatcher.a(17, Float.valueOf(0.0F));
	    DataWatcher.a(18, Integer.valueOf(0));
	}
}
