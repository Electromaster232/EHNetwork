package ehnetwork.game.microgames.game.games.draw.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitSelector extends Kit
{
	public KitSelector(MicroGamesManager manager)
	{
		super(manager, "Selector", KitAvailability.Gem, 4000, 

				new String[] 
						{
				"Choose from one of three words to draw!"
						}, 

						new Perk[] 
								{ 
				
								}, 
								EntityType.SKELETON,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
