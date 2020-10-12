package ehnetwork.game.arcade.game.games.draw.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitSelector extends Kit
{
	public KitSelector(ArcadeManager manager)
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
