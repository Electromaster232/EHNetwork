package ehnetwork.game.microgames.kit.perks;

import ehnetwork.core.common.util.C;
import ehnetwork.game.microgames.kit.Perk;

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
