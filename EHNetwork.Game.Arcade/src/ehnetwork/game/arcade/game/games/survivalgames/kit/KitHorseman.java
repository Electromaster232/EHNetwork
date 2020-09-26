package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkHorsePet;

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
