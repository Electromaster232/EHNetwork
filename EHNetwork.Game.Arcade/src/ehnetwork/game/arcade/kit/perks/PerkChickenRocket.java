package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.game.arcade.kit.perks.data.ChickenMissileData;

public class PerkChickenRocket extends SmashPerk
{
	private HashSet<ChickenMissileData> _data = new HashSet<ChickenMissileData>();

	public PerkChickenRocket() 
	{
		super("Chicken Missile", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Chicken Missile",
				C.cGreen + "Chicken Missile" + C.cGray + " instantly recharges if you hit a player."
				});
	}

	@EventHandler
	public void Missile(PlayerInteractEvent event)
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
		
		if (isSuperActive(player))
			return;

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 7000, true, true))
			return;

		Manager.GetGame().CreatureAllowOverride = true;
		Chicken ent = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), Chicken.class);
		ent.getLocation().setPitch(0);
		ent.getLocation().setYaw(player.getLocation().getYaw());
		ent.setBaby();
		ent.setAgeLock(true);
		UtilEnt.Vegetate(ent);
		Manager.GetGame().CreatureAllowOverride = false;
		
		_data.add(new ChickenMissileData(player, ent));
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<ChickenMissileData> dataIterator = _data.iterator();

		while (dataIterator.hasNext())
		{
			ChickenMissileData data = dataIterator.next();

			data.Chicken.setVelocity(data.Direction);
			data.Chicken.getWorld().playSound(data.Chicken.getLocation(), Sound.CHICKEN_HURT, 0.3f, 1.5f);
			
			if (!UtilTime.elapsed(data.Time, 200))
			{
				continue;
			}
			
			boolean detonate = false;
			
			if (UtilTime.elapsed(data.Time, 4000))
			{
				detonate = true;
			}
			else
			{
				//Hit Entity
				for (Entity ent : data.Player.getWorld().getEntities())
				{
					if (ent instanceof Arrow)
						if (((Arrow)ent).isOnGround())
							continue;						
					
					if (ent.equals(data.Player))
						continue;
					
					if (ent.equals(data.Chicken))
						continue;

					if (UtilMath.offset(data.Chicken.getLocation(), ent.getLocation().add(0, 0.5, 0)) > 2)
						continue;
					
					if (ent instanceof Player)
						if (!Manager.GetGame().IsAlive((Player)ent))
							continue;
			
					//Recharge
					Recharge.Instance.useForce(data.Player, GetName(), -1);
					
					detonate = true;
					break;
				}

				//Hit Block
				if (!detonate && data.HasHitBlock())
				{
					detonate = true;
				}
			}
			
			if (detonate)
			{
				//Damage
				for (Entity ent : data.Player.getWorld().getEntities())
				{
					if (!(ent instanceof LivingEntity))
						continue;
					
					if (ent.equals(data.Player))
						continue;

					if (UtilMath.offset(data.Chicken.getLocation(), ent.getLocation().add(0, 0.5, 0)) > 3)
						continue;

					if (ent instanceof Player)
						if (!Manager.GetGame().IsAlive((Player)ent))
							continue;
	
					LivingEntity livingEnt = (LivingEntity)ent;
					
					//Damage Event
					Manager.GetDamage().NewDamageEvent(livingEnt, data.Player, null, 
							DamageCause.PROJECTILE, 8, false, true, false,
							data.Player.getName(), GetName());
					
					UtilAction.velocity(livingEnt, UtilAlg.getTrajectory2d(data.Chicken, livingEnt), 1.6, true, 0.8, 0, 10, true);
				}
				
				//Effect
				UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, data.Chicken.getLocation(), 0, 0, 0, 0, 1,
						ViewDist.MAX, UtilServer.getPlayers());
				data.Chicken.getWorld().playSound(data.Chicken.getLocation(), Sound.EXPLODE, 2f, 1.2f);
				
				//Firework
				UtilFirework.playFirework(data.Chicken.getLocation().add(0, 0.6, 0), Type.BALL, Color.WHITE, false, false);
				

				data.Chicken.remove();
				dataIterator.remove();
				continue;
			}
		}
	}
}
