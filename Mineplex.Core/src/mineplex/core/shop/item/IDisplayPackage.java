package mineplex.core.shop.item;

import org.bukkit.Material;

public interface IDisplayPackage
{
	String GetName();
	String[] GetDescription();
	Material GetDisplayMaterial();
	byte GetDisplayData();
}
