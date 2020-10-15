package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkSlimeRocket extends SmashPerk implements IThrown
{
	private HashMap<Player, Long> _charge = new HashMap<Player, Long>();
	private HashMap<Slime, Player> _owner = new HashMap<Slime, Player>();
	private HashMap<Slime, Long> _lastAttack = new HashMap<Slime, Long>();
	
	public PerkSlimeRocket() 
	{
		super("Slime Rocket", new String[] 
				{ 
				C.cYellow + "Hold/Release Block" + C.cGray + " to use " + C.cGreen + "Slime Rocket"
				});
	}

	@EventHandler
	public void EnergyUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			if (isSuperActive(player))
				continue;
			
			int size = 1;
			if (player.getExp() > 0.8)				size = 3;
			else if (player.getExp() > 0.55)		size = 2;
			
			
			MobDisguise slime = (MobDisguise)Manager.GetDisguise().getDisguise(player);

			if (player.isBlocking())
				continue;

			player.setExp((float) Math.min(0.999, player.getExp()+0.004));
		}
	}

	@EventHandler
	public void Activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SWORD"))
			return;

		Player player = event.getPlayer();

		if (isSuperActive(player))
			return;
		
		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 6000, true, true))
			return;

		UtilPlayer.message(player, F.main("Skill", "You are charging " + F.skill(GetName()) + "."));
		
		_charge.put(player, System.currentTimeMillis());
	}

	@EventHandler
	public void ChargeRelease(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> chargeIterator = _charge.keySet().iterator();

		while (chargeIterator.hasNext())
		{
			Player player = chargeIterator.next();

			long time = _charge.get(player);
			
			//Charge
			if (player.isBlocking())
			{
				//Energy Depleted
				if (player.getExp() < 0.1)
				{
					FireRocket(player);
					chargeIterator.remove();
				}
				else
				{
					double elapsed = Math.min(3, (double)(System.currentTimeMillis() - time)/1000d);
					
					//Use Energy
					if (!UtilTime.elapsed(time, 3000))
					{
						player.setExp((float) Math.max(0, player.getExp()-0.01f));
					}
					
					//AutoFire
					if (UtilTime.elapsed(time, 5000))
					{
						FireRocket(player);
						chargeIterator.remove();
					}
					
					//Effect
					player.getWorld().playSound(player.getLocation(), Sound.SLIME_WALK, 0.5f, (float)(0.5 + 1.5*(elapsed/3d)));		
					UtilParticle.PlayParticle(ParticleType.SLIME, player.getLocation().add(0, 1, 0), 
							(float)(elapsed/6d), (float)(elapsed/6d), (float)(elapsed/6d), 0, (int)(elapsed * 5),
							ViewDist.LONGER, UtilServer.getPlayers());
				}
			}
			//Release
			else
			{
				FireRocket(player);
				chargeIterator.remove();
			}
		}
	}

	public void FireRocket(Player player)
	{
		double charge = Math.min(3, (double)(System.currentTimeMillis() - _charge.get(player))/1000d);

		//Spawn Slime
		Manager.GetGame().CreatureAllowOverride = true;
		Slime slime = player.getWorld().spawn(player.getEyeLocation(), Slime.class);
		slime.setSize(1);
		Manager.GetGame().CreatureAllowOverride = false;

		//Size
		slime.setSize(Math.max(1, (int)charge));
		
		slime.setMaxHealth(5 + charge * 7);
		slime.setHealth(slime.getMaxHealth());
		
		_owner.put(slime, player);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You released " + F.skill(GetName()) + "."));

		slime.leaveVehicle();
		player.eject();

		UtilAction.velocity(slime, player.getLocation().getDirection(), 1 + charge/2d, false, 0, 0.2, 10, true);
		
		Manager.GetProjectile().AddThrow(slime, player, this, -1, true, true, true, 
				null, 0, 0, null, 0, UpdateType.FASTEST, 1f);
	}

	@EventHandler
	public void SlimeTarget(EntityTargetEvent event)
	{
		if (event.isCancelled())
			return;

		if (!_owner.containsKey(event.getEntity()))
			return;

		if (_owner.get(event.getEntity()).equals(event.getTarget()))
		{
			event.setCancelled(true);
		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target == null)
			return;
		
		if (!(data.GetThrown() instanceof Slime))
			return;
		
		Slime slime = (Slime)data.GetThrown();

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.PROJECTILE, 3 + slime.getSize() * 3, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 3.0);
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		
	}
	
	@EventHandler
	public void SlimeDamage(CustomDamageEvent event)
	{
		if (!(event.GetDamagerEntity(false) instanceof Slime))
			return;
		
		Slime slime = (Slime)event.GetDamagerEntity(false);
		
		
		//Attack Rate
		if (_lastAttack.containsKey(slime) && !UtilTime.elapsed(_lastAttack.get(slime), 500))
		{
			event.SetCancelled("Slime Attack Rate");
			return;
		}
		
		_lastAttack.put(slime, System.currentTimeMillis());
		
		//Get Owner
		Player owner = _owner.get(slime);
		//if (owner != null)
		//	event.SetDamager(owner);  This gives knockback from wrong direction :(
			
			
		if (owner != null && owner.equals(event.GetDamageeEntity()))
		{
			event.SetCancelled("Owner Damage");
		}
		else
		{
			event.AddMod("Slime Damage", "Negate", -event.GetDamageInitial(), false);
			event.AddMod("Slime Damage", "Attack", 2 * slime.getSize(), true);
			event.AddKnockback("Slime Knockback", 2);
		}	 
	}
	
	@EventHandler
	public void SlimeClean(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.SEC)
			return;

		Iterator<Slime> slimeIterator = _owner.keySet().iterator();

		while (slimeIterator.hasNext())
		{
			Slime slime = slimeIterator.next();
			
			//Shrink
			if (slime.getVehicle() == null)
			{
				if (slime.getTicksLived() > 120)
				{
					slime.setTicksLived(1);
					
					Manager.GetBlood().Effects(null, slime.getLocation(), 6 + 6 * slime.getSize(), 0.2 + 0.1 * slime.getSize(), null, 1f, 1f, Material.SLIME_BALL, (byte)0, 15, false);
					
					if (slime.getSize() <= 1)
						slime.remove();
					else
						slime.setSize(slime.getSize()-1);			
				}
			}
			
			if (!slime.isValid())
				slimeIterator.remove();
		}
		
		slimeIterator = _lastAttack.keySet().iterator();

		while (slimeIterator.hasNext())
		{
			Slime slime = slimeIterator.next();

			if (!slime.isValid())
				slimeIterator.remove();
		}
	}
}
