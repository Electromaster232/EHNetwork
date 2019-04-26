package nautilus.game.arcade.game.games.uhc;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

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
