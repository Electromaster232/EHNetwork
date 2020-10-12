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

public class KitMystic extends Kit
{
	public KitMystic(MicroGamesManager manager)
	{
		super(manager, "Mystic", KitAvailability.Gem, new String[]
			{
				"Mana regeneration increased by 10%"
			}, new Perk[0], EntityType.WITCH, new ItemStack(Material.WOOD_HOE));
	}

	@Override
	public void GiveItems(Player player)
	{
		((Wizards) this.Manager.GetGame()).setupWizard(player);
	}
}
