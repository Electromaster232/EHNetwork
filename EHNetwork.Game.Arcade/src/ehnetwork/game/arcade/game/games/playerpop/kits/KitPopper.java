package ehnetwork.game.arcade.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkSpeed;

public class KitPopper extends Kit
{
	public KitPopper(ArcadeManager manager)
	{
		super(manager, "Popper", KitAvailability.Free, 0,

				new String[]
						{
								"Hippity Hoppity your life is now my property!"
						},

				new Perk[]
						{
								new PerkDoubleJump("Double Jump", 0.9, 0.9, true),
								new PerkSpeed(2)
						},
				EntityType.ENDERMAN,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.setHealth(10);
		player.setFoodLevel(14);
		return;
	}
}
