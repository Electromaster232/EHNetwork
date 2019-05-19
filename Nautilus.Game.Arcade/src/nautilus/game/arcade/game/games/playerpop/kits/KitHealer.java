package nautilus.game.arcade.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkHealthBoost;


public class KitHealer extends Kit
{
	public KitHealer(ArcadeManager manager)
	{
		super(manager, "Healer", KitAvailability.Gem, 1000,

				new String[]
						{
								"You do not have speed or jump but take twice as many hits to kill"
						},

				new Perk[]
						{
								//new PerkHealthBoost(9)
						},
				EntityType.MUSHROOM_COW,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		return;
	}
}
