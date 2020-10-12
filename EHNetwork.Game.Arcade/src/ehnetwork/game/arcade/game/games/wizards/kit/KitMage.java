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

public class KitMage extends Kit
{
	public KitMage(ArcadeManager manager)
	{
		super(manager, "Mage", KitAvailability.Free, new String[]
			{
				"Start with two extra spells"
			}, new Perk[0], EntityType.WITCH, new ItemStack(Material.BLAZE_ROD));
	}

	@Override
	public void GiveItems(Player player)
	{
		((Wizards) this.Manager.GetGame()).setupWizard(player);
	}
}
