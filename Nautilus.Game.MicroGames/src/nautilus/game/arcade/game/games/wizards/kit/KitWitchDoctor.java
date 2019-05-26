package nautilus.game.arcade.game.games.wizards.kit;

import mineplex.core.achievement.Achievement;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.wizards.Wizards;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
