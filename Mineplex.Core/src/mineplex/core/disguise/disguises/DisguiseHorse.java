package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;
import org.spigotmc.ProtocolData;

public class DisguiseHorse extends DisguiseAnimal
{
	public DisguiseHorse(org.bukkit.entity.Entity entity)
	{
		super(EntityType.HORSE, entity);

		DataWatcher.a(16, Integer.valueOf(0));
		DataWatcher.a(19, Byte.valueOf((byte) 0));
		DataWatcher.a(20, Integer.valueOf(0));
		DataWatcher.a(21, String.valueOf(""));
		DataWatcher.a(22, Integer.valueOf(0));
	}

	public void setType(Horse.Variant horseType)
	{
		DataWatcher.watch(19, Byte.valueOf((byte) horseType.ordinal()));
	}

	public Horse.Variant getType()
	{
		return Horse.Variant.values()[DataWatcher.getByte(19)];
	}

	public void setVariant(Horse.Color color)
	{
		DataWatcher.watch(20, Integer.valueOf(color.ordinal()));
	}

	public Horse.Color getVariant()
	{
		return Horse.Color.values()[DataWatcher.getInt(20)];
	}

	private boolean w(int i)
	{
		return (DataWatcher.getInt(16) & i) != 0;
	}

	public void kick()
	{
	    b(32, false);
	    b(64, true);
	}
	
	public void stopKick()
	{
	    b(64, false);
	}
	
	private void b(int i, boolean flag)
	{
		int j = DataWatcher.getInt(16);

		if (flag)
			DataWatcher.watch(16, Integer.valueOf(j | i));
		else
			DataWatcher.watch(16, Integer.valueOf(j & (i ^ 0xFFFFFFFF)));
	}

	public String getOwnerName()
	{
		return DataWatcher.getString(21);
	}

	public void setOwnerName(String s)
	{
		DataWatcher.watch(21, s);
	}

	public int cf()
	{
		return DataWatcher.getInt(22);
	}

	public void r(int i)
	{
		DataWatcher.watch(22, Integer.valueOf(i));
	}
}
