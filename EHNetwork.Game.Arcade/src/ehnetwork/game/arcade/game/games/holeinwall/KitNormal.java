package ehnetwork.game.arcade.game.games.holeinwall;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitNormal extends Kit
{
	public KitNormal(ArcadeManager manager)
	{
		super(manager, "Default Kit", KitAvailability.Free,

		new String[]
			{
				"Default kit"
			},

		new Perk[]
			{

			}, EntityType.SKELETON, null);
	}

	@Override
	public void GiveItems(Player player)
	{

	}
}
