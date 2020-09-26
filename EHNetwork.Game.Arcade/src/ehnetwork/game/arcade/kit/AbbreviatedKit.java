package ehnetwork.game.arcade.kit;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.ArcadeFormat;
import ehnetwork.game.arcade.ArcadeManager;

public abstract class AbbreviatedKit extends Kit
{		
	public AbbreviatedKit(ArcadeManager manager, String name,
			KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks,
			EntityType entityType, ItemStack itemInHand) 
	{
		super(manager, name, kitAvailability, 3000, kitDesc, kitPerks, entityType, itemInHand);
	}

	public AbbreviatedKit(ArcadeManager manager, String name,
					KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks,
					EntityType entityType, ItemStack itemInHand)
	{
		super(manager, name, kitAvailability, cost, kitDesc, kitPerks, entityType, itemInHand);
	}

	@Override
	public void DisplayDesc(Player player) 
	{
		for (int i=0 ; i<3 ; i++)
			UtilPlayer.message(player, "");
		
		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, "§aKit - §f§l" + GetName());

		//Desc
		for (String line : GetDesc())
		{
			UtilPlayer.message(player, C.cGray + "  " + line);
		}

		//Perk Descs
		for (Perk perk : GetPerks())
		{
			if (!perk.IsVisible())
				continue;

			for (String line : perk.GetDesc())
			{
				UtilPlayer.message(player, C.cGray + "  " + line);
			}
		}

		UtilPlayer.message(player, ArcadeFormat.Line);

	}
}
