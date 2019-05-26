package nautilus.game.arcade.game.games.wizards.kit;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.wizards.Wizards;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitSorcerer extends Kit
{
	public KitSorcerer(ArcadeManager manager)
	{
		super(manager, "Sorcerer", KitAvailability.Gem, new String[]
			{
				"Start out with an extra wand"
			}, new Perk[0], EntityType.WITCH, new ItemStack(Material.STONE_HOE));
	}

	@Override
	public void GiveItems(Player player)
	{
		((Wizards) this.Manager.GetGame()).setupWizard(player);
	}
}
