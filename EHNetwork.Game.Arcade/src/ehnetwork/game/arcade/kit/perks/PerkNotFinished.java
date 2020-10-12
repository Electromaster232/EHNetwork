package ehnetwork.game.arcade.kit.perks;

import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.kit.Perk;

public class PerkNotFinished extends Perk
{
	public PerkNotFinished() 
	{
		super("Not Completed", new String[] 
				{ 
				C.cRed + C.Bold + "KIT IS NOT FINISHED",
				});
	}
}
