package ehnetwork.game.microgames.game.games.sneakyassassins.powerups;

import java.util.Random;

import org.bukkit.entity.Player;

import ehnetwork.game.microgames.game.games.sneakyassassins.kits.SneakyAssassinKit;

public class SmokeBombPowerUp extends PowerUp
{
	protected SmokeBombPowerUp()
	{
		super(PowerUpType.SMOKE_BOMB);
	}

	@Override
	public boolean powerUpPlayer(Player player, Random random)
	{
		return player.getInventory().addItem(SneakyAssassinKit.SMOKE_BOMB.clone()).isEmpty();
	}
}
