package nautilus.game.arcade.game.games.draw.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import mineplex.core.achievement.Achievement;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

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
