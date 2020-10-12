package ehnetwork.game.arcade.kit.perks;

import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.kit.Perk;

public class PerkLooter extends Perk
{
	public PerkLooter() 
	{
		super("Looter", new String[] 
				{ 
				C.cGray + "You find extra loot in chests.",
				});
	}
}
