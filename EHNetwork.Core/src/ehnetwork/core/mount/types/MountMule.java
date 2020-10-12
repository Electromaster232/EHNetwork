package ehnetwork.core.mount.types;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;

import ehnetwork.core.mount.HorseMount;
import ehnetwork.core.mount.MountManager;

public class MountMule extends HorseMount
{
	public MountMule(MountManager manager)
	{
		super(manager, "Mount Mule", new String[]
				{
					ChatColor.RESET + "Muley muley!"
				},
				Material.HAY_BLOCK,
				(byte)0,
				3000,
				Color.BLACK, Style.BLACK_DOTS, Variant.MULE, 1.0, null);
	}
}
