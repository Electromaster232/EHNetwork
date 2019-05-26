package nautilus.game.arcade.game.games.draw.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

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
