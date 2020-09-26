package ehnetwork.game.arcade.game.games.sneakyassassins.powerups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorPowerUp extends PowerUp
{
	private static final List<List<Material>> ARMOR_PROGRESSION = Arrays.asList(
			Arrays.asList(
					Material.LEATHER_BOOTS,
					Material.CHAINMAIL_BOOTS,
					Material.IRON_BOOTS
			),
			Arrays.asList(
					Material.LEATHER_LEGGINGS,
					Material.CHAINMAIL_LEGGINGS,
					Material.IRON_LEGGINGS
			),
			Arrays.asList(
					Material.LEATHER_CHESTPLATE,
					Material.CHAINMAIL_CHESTPLATE,
					Material.IRON_CHESTPLATE
			),
			Arrays.asList(
					Material.LEATHER_HELMET,
					Material.CHAINMAIL_HELMET,
					Material.IRON_HELMET
			)
	);

	public ArmorPowerUp()
	{
		super(PowerUpType.ARMOR);
	}

	@Override
	public boolean powerUpPlayer(Player player, Random random)
	{
		return powerUpArmor(player, random);
		/*
		boolean powerUp1 = powerUpArmor(player, random);
		boolean powerUp2 = powerUpArmor(player, random);

		return powerUp1 || powerUp2;
		*/
	}

	public static boolean powerUpArmor(Player player, Random random)
	{
		ItemStack[] armor = player.getInventory().getArmorContents();

		List<Integer> upgradeable = new ArrayList<>();

		for (int level = 0; level < ARMOR_PROGRESSION.get(0).size(); level++)
		{
			for (int kind = 0; kind < armor.length; kind++)
			{
				if (ARMOR_PROGRESSION.get(kind).get(level) == armor[kind].getType())
					upgradeable.add(kind);
			}

			if (!upgradeable.isEmpty())
			{
				int choice = upgradeable.get(random.nextInt(upgradeable.size()));
				int nextLevel = Math.min(ARMOR_PROGRESSION.get(choice).size() - 1, level + 1);

				armor[choice] = new ItemStack(ARMOR_PROGRESSION.get(choice).get(nextLevel));
				player.getInventory().setArmorContents(armor);

				return true;
			}
		}

		return false;
	}
}
