package ehnetwork.game.microgames.game.games.holeinwall;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitNormal extends Kit
{
	public KitNormal(MicroGamesManager manager)
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
