package ehnetwork.game.arcade.game.games.minestrike.items.guns;

public class GunFactory
{
	public Gun createGun(GunStats gun)
	{
		if (gun.getGunType() == GunType.SHOTGUN)
			return new Shotgun(gun);
		
		return new Gun(gun);
	}
}
