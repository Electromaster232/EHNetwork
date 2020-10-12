package ehnetwork.game.microgames.game.games.draw.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitTools extends Kit
{
	public KitTools(MicroGamesManager manager)
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
