package ehnetwork.game.arcade.game.games.wizards.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.wizards.Wizards;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitWitchDoctor extends Kit
{
	public KitWitchDoctor(ArcadeManager manager)
	{
		super(manager, "Witch Doctor", KitAvailability.Achievement, new String[]
			{
				"Max mana increased to 150"
			}, new Perk[0], EntityType.WITCH, new ItemStack(Material.IRON_HOE));

		this.setAchievementRequirements(new Achievement[]
			{
				Achievement.WIZARDS_WINS
			});
	}

	@Override
	public void GiveItems(Player player)
	{
		((Wizards) this.Manager.GetGame()).setupWizard(player);
	}
}
