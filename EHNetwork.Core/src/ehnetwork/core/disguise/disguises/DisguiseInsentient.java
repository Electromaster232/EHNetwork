package ehnetwork.core.disguise.disguises;

import org.bukkit.ChatColor;

import ehnetwork.core.common.Rank;

public abstract class DisguiseInsentient extends DisguiseLiving
{
	private boolean _showArmor; 
	
	public DisguiseInsentient(org.bukkit.entity.Entity entity)
	{
		super(entity);

		DataWatcher.a(3, Byte.valueOf((byte) 0));
		DataWatcher.a(2, "");

		if (!(this instanceof DisguiseArmorStand))
		{
			DataWatcher.a(11, Byte.valueOf((byte) 0));
			DataWatcher.a(10, "");
		}
	}
	
	public void setName(String name)
	{
		setName(name, null);
	}
	
	public void setName(String name, Rank rank)
	{
		if (rank != null)
		{
			if (rank.Has(Rank.ULTRA))
			{
				name = rank.GetTag(true, true) + " " + ChatColor.RESET + name;
			}
		}

		DataWatcher.watch(10, name);
		DataWatcher.watch(2, name);
	}


	public boolean hasCustomName()
	{
		return DataWatcher.getString(10).length() > 0;
	}
	
	public void setCustomNameVisible(boolean visible)
	{
		DataWatcher.watch(11, Byte.valueOf((byte)(visible ? 1 : 0)));
		DataWatcher.watch(3, Byte.valueOf((byte)(visible ? 1 : 0)));
	}
	
	public boolean getCustomNameVisible()
	{
		return DataWatcher.getByte(11) == 1;
	}
	
	public boolean armorVisible()
	{
		return _showArmor;
	}
	
	public void showArmor()
	{
		_showArmor = true;
	}
	
	public void hideArmor()
	{
		_showArmor = false;
	}
}
