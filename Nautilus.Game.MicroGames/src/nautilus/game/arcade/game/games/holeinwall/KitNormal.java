package nautilus.game.arcade.game.games.holeinwall;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import mineplex.core.achievement.Achievement;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

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
