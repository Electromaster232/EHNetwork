package ehnetwork.game.microgames.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
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
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.data.FireflyData;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkFlameDash extends Perk
{
	private HashSet<FireflyData> _data = new HashSet<FireflyData>();

	public PerkFlameDash() 
	{
		super("Flame Dash", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Flame Dash"
				});
	}

	@EventHandler
	public void Skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.usable(player, GetName()))
		{
			boolean done = false;
			for (FireflyData data : _data)
			{
				if (data.Player.equals(player))
				{
					data.Time = 0;
					done = true;
				}
			}
			
			if (done)
			{
				UtilPlayer.message(player, F.main("Skill", "You ended " + F.skill(GetName()) + "."));
				UpdateMovement();
			}
			else
			{
				Recharge.Instance.use(player, GetName(), 8000, true, true);
			}
			
			return;
		}

		Recharge.Instance.useForce(player, GetName(), 8000);

		_data.add(new FireflyData(player));

		Manager.GetCondition().Factory().Invisible(GetName(), player, player, 2.5, 0, false, false, true);

		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		UpdateMovement();
	}

	public void UpdateMovement()
	{
		Iterator<FireflyData> dataIterator = _data.iterator();

		while (dataIterator.hasNext())
		{
			FireflyData data = dataIterator.next();


			//Move
			if (!UtilTime.elapsed(data.Time, 800))
			{
				Vector vel = data.Location.getDirection();
				vel.setY(0);
				vel.normalize();
				vel.setY(0.05);

				data.Player.setVelocity(vel);
				
				//Sound
				data.Player.getWorld().playSound(data.Player.getLocation(), Sound.FIZZ, 0.6f, 1.2f);
			
				//Particles
				UtilParticle.PlayParticle(ParticleType.FLAME, data.Player.getLocation().add(0, 0.4, 0), 0.2f, 0.2f, 0.2f, 0f, 3,
						ViewDist.LONGER, UtilServer.getPlayers());
			}
			//End
			else
			{
				for (Player other : UtilPlayer.getNearby(data.Player.getLocation(), 3))
				{
					if (other.equals(data.Player))
						continue;

					if (!Manager.GetGame().IsAlive(other))
						continue;
					
					double dist = UtilMath.offset(data.Player.getLocation(), data.Location)/2d;

					//Damage Event
					Manager.GetDamage().NewDamageEvent(other, data.Player, null, 
							DamageCause.CUSTOM, 2 + dist, true, true, false,
							data.Player.getName(), GetName());

					UtilPlayer.message(other, F.main("Game", F.elem(Manager.GetColor(data.Player) + data.Player.getName()) + " hit you with " + F.elem(GetName()) + "."));
				}

				//End Invisible
				Manager.GetCondition().EndCondition(data.Player, null, GetName());

				//Sound
				data.Player.getWorld().playSound(data.Player.getLocation(), Sound.EXPLODE, 1f, 1.2f);
				
				//Particles
				UtilParticle.PlayParticle(ParticleType.FLAME, data.Player.getLocation(), 0.1f, 0.1f, 0.1f, 0.3f, 100,
						ViewDist.MAX, UtilServer.getPlayers());
				UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, data.Player.getLocation().add(0, 0.4, 0), 0.2f, 0.2f, 0.2f, 0f, 1,
						ViewDist.MAX, UtilServer.getPlayers());

				dataIterator.remove();
			}
		}	
	}

	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 2);
	}
}
