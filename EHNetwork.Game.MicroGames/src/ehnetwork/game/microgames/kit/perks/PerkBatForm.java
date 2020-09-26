package ehnetwork.game.microgames.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.disguise.disguises.DisguiseBat;
import ehnetwork.core.disguise.disguises.DisguiseWitch;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.game.microgames.kit.perks.data.SonicBoomData;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkBatForm extends SmashPerk
{
	private ArrayList<SonicBoomData> _sonic = new ArrayList<SonicBoomData>();
	
	public PerkBatForm() 
	{
		super("Bat Form", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{
		Manager.GetDisguise().undisguise(player);

		//Disguise
		DisguiseBat disguise = new DisguiseBat(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@Override
	public void removeSuperCustom(Player player)
	{
		Manager.GetDisguise().undisguise(player);

		//Disguise
		DisguiseWitch disguise = new DisguiseWitch(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void attackCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;

		if (!isSuperActive(player))
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		event.SetCancelled("Bat Form Melee Cancel");
	}
	
	@EventHandler(priority = EventPriority.LOW)	//Happen before activation of Super
	public void sonicBoom(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!isSuperActive(player))
			return;

		if (event.getAction() == Action.PHYSICAL)
			return;
		
		if (!Recharge.Instance.use(player, GetName() + " Screech", 1200, false, false))
			return;

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.BAT_HURT, 1f, 0.75f);
		
		_sonic.add(new SonicBoomData(player));
	}
	
	@EventHandler
	public void sonicBoomUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<SonicBoomData> sonicIter = _sonic.iterator();
		
		while (sonicIter.hasNext())
		{
			SonicBoomData data = sonicIter.next();
			
			//Time Boom
			if (UtilTime.elapsed(data.Time, 12000))
			{
				sonicIter.remove();
				explode(data);
				continue;
			}
			
			//Block Boom
			if (!UtilBlock.airFoliage(data.Location.getBlock()))
			{
				sonicIter.remove();
				explode(data);
				continue;
			}
			
			//Proxy Boom
			for (Player player : Manager.GetGame().GetPlayers(true))
			{
				if (Manager.isSpectator(player))
					continue;
				
				if (player.equals(data.Shooter))
					continue;
				
				if (UtilMath.offset(player.getLocation().add(0, 1, 0), data.Location) < 4)
				{
					sonicIter.remove();
					explode(data);
					continue;
				}
			}
			
			//Move
			data.Location.add(data.Direction.clone().multiply(0.75));
			
			//Effect
			UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, data.Location, 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
			data.Location.getWorld().playSound(data.Location, Sound.FIZZ, 1f, 2f);
		}
	}

	private void explode(SonicBoomData data)
	{
		//Effect
		UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, data.Location, 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		data.Location.getWorld().playSound(data.Location, Sound.EXPLODE, 1f, 1.5f);
		
		//Damage
		HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(data.Location, 10);
		for (LivingEntity cur : targets.keySet())
		{
			Manager.GetDamage().NewDamageEvent(cur, data.Shooter, null, 
					DamageCause.CUSTOM, 12 * targets.get(cur) + 0.5, true, false, false,
					data.Shooter.getName(), GetName());	
		}
	}

	@EventHandler
	public void flap(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();

		if (Manager.isSpectator(player))
			return;

		if (!isSuperActive(player))
			return;
		
		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		event.setCancelled(true);
		player.setFlying(false);

		//Disable Flight
		player.setAllowFlight(false);

		//Velocity
		UtilAction.velocity(player, player.getLocation().getDirection(), 0.8, false, 0, 0.8, 1, true);

		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, (float)(0.3 + player.getExp()), (float)(Math.random()/2+0.5));

		//Set Recharge
		Recharge.Instance.use(player, GetName() + " Flap", 40, false, false);
	}
	
	@EventHandler
	public void flapRecharge(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : ((SmashKit)Kit).getSuperActive())
		{
			if (Manager.isSpectator(player))
				continue;

			if (UtilEnt.isGrounded(player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) 
			{
				player.setAllowFlight(true);
			}
			else if (Recharge.Instance.usable(player, GetName() + " Flap"))
			{
				player.setAllowFlight(true);
			}
		}
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2);
	}
}
