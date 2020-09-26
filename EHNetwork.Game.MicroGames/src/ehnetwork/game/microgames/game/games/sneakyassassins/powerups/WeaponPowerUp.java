package ehnetwork.game.microgames.game.games.sneakyassassins.powerups;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponPowerUp extends PowerUp
{
	private static final List<Material> SWORD_PROGRESSION = Arrays.asList(
			Material.WOOD_SWORD,
			Material.STONE_SWORD,
			Material.GOLD_SWORD,
			Material.IRON_SWORD,
			Material.DIAMOND_SWORD
	);

	public WeaponPowerUp()
	{
		super(PowerUpType.WEAPON);
	}

	@Override
	public boolean powerUpPlayer(Player player, Random random)
	{
		for (int swordType = 0; swordType < SWORD_PROGRESSION.size(); swordType++)
		{
			int swordSlot = player.getInventory().first(SWORD_PROGRESSION.get(swordType));

			if (swordSlot != -1)
			{
				int newSwordType = swordType + 1;

				if (newSwordType < SWORD_PROGRESSION.size())
				{
					player.getInventory().setItem(swordSlot, new ItemStack(SWORD_PROGRESSION.get(newSwordType)));

					return true;
				}
			}
		}

		return false;
	}
}
