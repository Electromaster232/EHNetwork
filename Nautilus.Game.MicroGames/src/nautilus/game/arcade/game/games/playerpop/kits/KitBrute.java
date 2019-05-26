package nautilus.game.arcade.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkSpeed;

public class KitBrute extends Kit
{
	public KitBrute(ArcadeManager manager)
	{
		super(manager, "Brute", KitAvailability.Gem, 1500,

				new String[]
						{
								"You stand tall but can fall!"
						},

				new Perk[]
						{
								new PerkDoubleJump("Double Jump", 0.9, 0.9, true)
						},
				EntityType.ENDERMAN,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		return;
	}
}
