package ehnetwork.game.arcade.game.games.sneakyassassins.powerups;

import java.util.Random;

import org.bukkit.entity.Player;

public enum PowerUpType
{
	WEAPON(new WeaponPowerUp()),
	ARMOR(new ArmorPowerUp()),
	SMOKE_BOMB(new SmokeBombPowerUp()),
	COMPASS(new CompassPowerUp());

	private final PowerUp _powerUp;

	PowerUpType(PowerUp powerUp)
	{
		_powerUp = powerUp;
	}

	public boolean powerUpPlayer(Player player, Random random)
	{
		return _powerUp.powerUpPlayer(player, random);
	}
}
