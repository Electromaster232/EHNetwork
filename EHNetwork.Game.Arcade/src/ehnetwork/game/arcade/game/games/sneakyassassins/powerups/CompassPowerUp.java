package ehnetwork.game.arcade.game.games.sneakyassassins.powerups;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CompassPowerUp extends PowerUp
{
	protected CompassPowerUp()
	{
		super(PowerUpType.COMPASS);
	}

	@Override
	public boolean powerUpPlayer(Player player, Random random)
	{
		if (player.getInventory().contains(Material.COMPASS))
			return false;

		player.getInventory().addItem(new ItemStack(Material.COMPASS));

		return true;
	}
}
