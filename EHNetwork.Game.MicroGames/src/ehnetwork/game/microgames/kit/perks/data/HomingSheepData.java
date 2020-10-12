package ehnetwork.game.microgames.kit.perks.data;

import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;

public class HomingSheepData
{
	public Player Shooter;
	public Player Target;
	public Sheep Sheep;
	
	private int _colorTick = 0;
	
	public HomingSheepData(Player shooter, Player target, Sheep sheep)
	{
		Shooter = shooter;
		Target = target;
		Sheep = sheep;
	}

	public boolean update()
	{
		if (!Sheep.isValid() || !Target.isValid() || UtilPlayer.isSpectator(Target))
			return true;
		
		if (Sheep.getTicksLived() > 300)
			return true;
		
		if (UtilMath.offset(Sheep.getLocation(), Target.getEyeLocation()) < 2)
			return true;
		
		Sheep.setVelocity(UtilAlg.getTrajectory(Sheep.getLocation(), Target.getEyeLocation()).multiply(0.36));
		
		Sheep.getWorld().playSound(Sheep.getLocation(), Sound.SHEEP_IDLE, 1.5f, 1.5f);
		
		if (_colorTick == 0) 	Sheep.setColor(DyeColor.RED);
		if (_colorTick == 1) 	Sheep.setColor(DyeColor.YELLOW);
		if (_colorTick == 2) 	Sheep.setColor(DyeColor.LIME);
		if (_colorTick == 3) 	Sheep.setColor(DyeColor.LIGHT_BLUE);
		if (_colorTick == 4) 	Sheep.setColor(DyeColor.PURPLE);
		
		_colorTick = (_colorTick + 1) % 5;
		
		return false;
	}
}
