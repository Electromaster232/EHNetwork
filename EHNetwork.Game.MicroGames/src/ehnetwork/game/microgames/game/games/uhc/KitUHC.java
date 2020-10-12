package ehnetwork.game.microgames.game.games.uhc;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitUHC extends Kit
{
	public KitUHC(MicroGamesManager manager)
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
