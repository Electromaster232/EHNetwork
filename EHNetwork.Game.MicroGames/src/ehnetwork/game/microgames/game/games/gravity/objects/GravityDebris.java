package ehnetwork.game.microgames.game.games.gravity.objects;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.microgames.game.games.gravity.Gravity;
import ehnetwork.game.microgames.game.games.gravity.GravityObject;

public class GravityDebris extends GravityObject
{
	public GravityDebris(Gravity host, Entity ent, double mass, Vector vel) 
	{
		super(host, ent, mass, 2, vel);
		
		CollideDelay = System.currentTimeMillis() + 500;
		SetMovingBat(true);
	}
	
	@Override
	public void CustomCollide(GravityObject other) 
	{
		Ent.remove();
		UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, Ent.getLocation(), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
	}
	
	public boolean CanCollide(GravityObject other)
	{
		return !(other instanceof GravityDebris);
	}
}
