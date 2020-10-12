package ehnetwork.game.arcade.game.games.uhc;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitUHC extends Kit
{
	public KitUHC(ArcadeManager manager)
	{
		super(manager, "UHC Player", KitAvailability.Free, 

				new String[] 
						{
				"A really unfortunate guy, who has been",
				"forced to fight to the death against",
				"a bunch of other guys."
						}, 

						new Perk[] 
								{
			
								}, 
								EntityType.ZOMBIE,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
	
	}
}
