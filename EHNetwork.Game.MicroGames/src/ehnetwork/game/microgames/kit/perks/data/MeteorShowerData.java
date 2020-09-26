package ehnetwork.game.microgames.kit.perks.data;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLargeFireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_7_R4.EntityLargeFireball;

import ehnetwork.core.common.util.UtilTime;

public class MeteorShowerData
{
	public Player Shooter;
	public Location Target;
	public long Time;

	public MeteorShowerData(Player shooter, Location target)
	{
		Shooter = shooter;
		Target = target;
		Time = System.currentTimeMillis();
	}	

	public boolean update()
	{
		if (UtilTime.elapsed(Time, 12000))
			return true;

		LargeFireball ball = Target.getWorld().spawn(Target.clone().add(Math.random() * 24 - 12, 32 + Math.random() * 16, Math.random() * 24 - 12), LargeFireball.class);

		EntityLargeFireball eFireball = ((CraftLargeFireball) ball).getHandle();
		eFireball.dirX = (Math.random()-0.5)*0.02;
		eFireball.dirY = -0.2 - 0.05 * Math.random();
		eFireball.dirZ = (Math.random()-0.5)*0.02;
		
		ball.setShooter(Shooter);
		ball.setYield(2.2f);
		ball.setBounce(false);

		return false;
	}
}
