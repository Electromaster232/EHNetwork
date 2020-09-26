package ehnetwork.game.arcade.game.games.draw.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitTools extends Kit
{
	public KitTools(ArcadeManager manager)
	{
		super(manager, "Extra Tools", KitAvailability.Achievement, 

				new String[] 
						{
				"Can draw lines, circles and squares!"
						}, 

						new Perk[] 
								{ 
				
								}, 
								EntityType.SKELETON,
								null);

		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.DRAW_MY_THING_KEEN_EYE,
				Achievement.DRAW_MY_THING_MR_SQUIGGLE,
				Achievement.DRAW_MY_THING_PURE_LUCK,
				Achievement.DRAW_MY_THING_WINS,
				});
	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
