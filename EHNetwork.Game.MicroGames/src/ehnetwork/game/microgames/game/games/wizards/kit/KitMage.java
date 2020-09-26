package ehnetwork.game.microgames.game.games.wizards.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.wizards.Wizards;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitMage extends Kit
{
	public KitMage(MicroGamesManager manager)
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
