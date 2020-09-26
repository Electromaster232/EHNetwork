package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLargeFireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_7_R4.EntityLargeFireball;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkMagmaBlast extends Perk
{
	public HashMap<LargeFireball, Location> _proj = new HashMap<LargeFireball, Location>();
	
	public PerkMagmaBlast() 
	{
		super("Magma Blast", new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Magma Blast"
				});
	}


	@EventHandler
	public void Shoot(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 6000, true, true))
			return;

		event.setCancelled(true);

		//Action
		LargeFireball ball = player.launchProjectile(LargeFireball.class);
		ball.setShooter(player);
		ball.setIsIncendiary(false);		
		ball.setYield(0);
		ball.setBounce(false);
		ball.teleport(player.getEyeLocation().add(player.getLocation().getDirection().multiply(1)));
		
		//ball.setVelocity(new Vector(0,0,0));
		
		Vector dir = player.getLocation().getDirection().multiply(0.2);
		
		EntityLargeFireball eFireball = ((CraftLargeFireball) ball).getHandle();
		eFireball.dirX = dir.getX();
		eFireball.dirY = dir.getY();
		eFireball.dirZ = dir.getZ();
		
		//Knockback
		UtilAction.velocity(player, player.getLocation().getDirection().multiply(-1), 1.2, false, 0, 0.2, 1.2, true);
		
		//Add
		_proj.put(ball, player.getLocation());

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.CREEPER_DEATH, 2f, 1.5f);
	}
	
	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<LargeFireball> projIterator = _proj.keySet().iterator();
		
		while (projIterator.hasNext())
		{
			LargeFireball proj = projIterator.next();
			
			if (!proj.isValid())
			{
				projIterator.remove();
				proj.remove();
				continue;
			}
			
//			proj.setDirection(_proj.get(proj).clone().getDirection());
//			proj.setVelocity(_proj.get(proj).clone().getDirection().multiply(0.6));
		}
	}

	@EventHandler
	public void Collide(ProjectileHitEvent event)
	{
		Projectile proj = event.getEntity();
		
		if (!_proj.containsKey(proj))
			return;

		if (proj.getShooter() == null)
			return;

		if (!(proj.getShooter() instanceof Player))
			return;

		//Velocity Players
		HashMap<Player,Double> hitMap = UtilPlayer.getInRadius(proj.getLocation(), 8);
		for (Player cur : hitMap.keySet())
		{	
			double range = hitMap.get(cur);

			//Velocity
			UtilAction.velocity(cur, UtilAlg.getTrajectory(proj.getLocation().add(0, -0.5, 0), cur.getEyeLocation()), 
					1 + 2 * range, false, 0, 0.2 + 0.4 * range, 1.2, true);
		}
		
		//Particles
		UtilParticle.PlayParticle(ParticleType.LAVA, proj.getLocation(), 0.1f, 0.1f, 0.1f, 0.1f, 50,
				ViewDist.LONG, UtilServer.getPlayers());
	}
}
