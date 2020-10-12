package ehnetwork.game.arcade.game.games.gravity.objects;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import ehnetwork.game.arcade.game.games.gravity.Gravity;
import ehnetwork.game.arcade.game.games.gravity.GravityObject;

public class GravityHook extends GravityObject
{
	public GravityHook(Gravity host, Entity ent, double mass, Vector vel) 
	{
		super(host, ent, mass, 1, vel);
	}

	@Override
	public void PlayCollideSound(double power) 
	{
		Ent.getWorld().playSound(Ent.getLocation(), Sound.IRONGOLEM_HIT, 1f, 2f);
	}
}
