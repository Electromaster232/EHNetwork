package ehnetwork.game.arcade.game.games.wizards.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.wizards.Wizards;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

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
