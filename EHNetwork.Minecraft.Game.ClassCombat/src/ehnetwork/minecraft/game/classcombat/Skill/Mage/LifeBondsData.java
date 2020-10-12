package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;

public class LifeBondsData
{
	private Location _loc;
	private Player _target;
	private double _health;
	
	public LifeBondsData(org.bukkit.Location loc, Player target, double amount)
	{
		_loc = loc;
		_target = target;
		_health = amount;
	}
	
	public boolean Update()
	{
		if (!_target.isValid() || !_target.isOnline())
			return true;

		if (UtilMath.offset(_loc, _target.getLocation()) < 1)
		{
			UtilPlayer.health(_target, _health);
			return true;
		}
		
		_loc.add(UtilAlg.getTrajectory(_loc, _target.getLocation().add(0, 0.8, 0)).multiply(0.9));
		UtilParticle.PlayParticle(ParticleType.HEART, _loc, 0, 0, 0, 0, 1,
				ViewDist.LONG, UtilServer.getPlayers());
		
		return false;
	}
}
