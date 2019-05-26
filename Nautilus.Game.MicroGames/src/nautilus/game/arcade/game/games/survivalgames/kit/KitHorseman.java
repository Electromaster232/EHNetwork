package nautilus.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.achievement.Achievement;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitHorseman extends Kit
{
	public KitHorseman(ArcadeManager manager)
	{
		super(manager, "Horseman", KitAvailability.Achievement,

				new String[] 
						{
				"Proud owner of a (rapidly growing) horse!"
						}, 

						new Perk[] 
								{
				new PerkHorsePet()
								}, 
								EntityType.HORSE,
								new ItemStack(Material.DIAMOND_BARDING));

		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.SURVIVAL_GAMES_BLOODLUST,
				Achievement.SURVIVAL_GAMES_LIGHT_WEIGHT,
				Achievement.SURVIVAL_GAMES_LOOT,
				Achievement.SURVIVAL_GAMES_SKELETONS,
				Achievement.SURVIVAL_GAMES_WINS,
				});
	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
