package ehnetwork.game.microgames.game.games.sneakyassassins.powerups;

import java.util.Random;

import org.bukkit.entity.Player;

public abstract class PowerUp
{
	private final PowerUpType _powerUpType;

	protected PowerUp(PowerUpType powerUpType)
	{
		_powerUpType = powerUpType;
	}

	public PowerUpType getPowerUpType()
	{
		return _powerUpType;
	}

	public abstract boolean powerUpPlayer(Player player, Random random);
}
