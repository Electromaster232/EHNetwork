package mineplex.minecraft.game.classcombat.Skill.Brute;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.core.common.util.F;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.visibility.VisibilityManager;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.minecraft.game.classcombat.Skill.SkillActive;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;
import mineplex.minecraft.game.classcombat.Skill.event.SkillEvent;
import mineplex.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;

public class DwarfToss extends SkillActive
{
	private HashSet<Player>	_used = new HashSet<Player>();
	private NautHashMap<Player, LivingEntity> _holding = new NautHashMap<Player, LivingEntity>();
	private NautHashMap<Player, Long> _time = new NautHashMap<Player, Long>();
	
	private long _chargeTime = 2500;

	public DwarfToss(SkillFactory skills, String name, ClassType classType, SkillType skillType, 
			int cost, int levels, 
			int energy, int energyMod, 
			long recharge, long rechargeMod, boolean rechargeInform, 
			Material[] itemArray, 
			Action[] actionArray) 
	{
		super(skills, name, classType, skillType,
				cost, levels,
				energy, energyMod, 
				recharge, rechargeMod, rechargeInform, 
				itemArray,
				actionArray);

		SetDesc(new String[] 
				{
				"Hold Block to pick up target player.",
				"Release Block to throw with #1.2#0.2 velocity.",
				"",
				"Players you are holding cannot harm",
				"you, or be harmed by others.",
				"",
				"Takes 2.5 seconds to fully charge."
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (_used.contains(player))
			return false;

		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		UtilPlayer.message(player, F.main(GetClassType().name(), "You failed " + F.skill(GetName()) + "."));
	}

	@EventHandler
	public void Miss(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		_used.clear();
	}

	public boolean CanUse(Player player)
	{
		int level = getLevel(player);
		if (level == 0)		return false;

		//Check Material
		if (player.getItemInHand() != null)
			if (!_itemSet.contains(player.getItemInHand().getType()))
				return false;

		//Check Allowed
		SkillTriggerEvent trigger = new SkillTriggerEvent(player, GetName(), GetClassType());
		UtilServer.getServer().getPluginManager().callEvent(trigger);
		if (trigger.IsCancelled())
			return false;

		//Check Energy/Recharge
		if (!EnergyRechargeCheck(player, level))
			return false;

		//Allow
		return true;
	}
	
	@EventHandler
	public void PreventDismount(VehicleExitEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (!(event.getExited() instanceof Player))
			return;
		
		if (!(event.getVehicle() instanceof Player))
			return;
		
		if (_holding.containsKey((Player)event.getVehicle()) && _holding.get((Player)event.getVehicle()) == event.getExited())
			event.setCancelled(true);
	}

	@EventHandler
	public void Grab(PlayerInteractEntityEvent event)
	{
		if (event.isCancelled())
			return;
		
		Player player = event.getPlayer();

		//Level
		int level = getLevel(player);
		if (level == 0)			return;

		//Set Used
		_used.add(player);

		if (!CanUse(player))
			return;

		if (!(event.getRightClicked() instanceof LivingEntity))
			return;

		LivingEntity target = (LivingEntity)event.getRightClicked();

		if (target instanceof Player)
		{
			if (UtilPlayer.isSpectator(target))
			{
				UtilPlayer.message(player, F.main(GetClassType().name(), F.name(((Player)target).getName()) + " is not attackable."));
				return;
			}
		}

		//Distance
		if (UtilMath.offset(player.getLocation(), target.getLocation()) > 4)
		{
			UtilPlayer.message(player, F.main(GetClassType().name(), F.name(UtilEnt.getName(target)) + " is too far away."));
			return;
		}	

		//Hold Loop
		if (target instanceof Player && _holding.containsKey((Player)target))
			if (_holding.get((Player)target).equals(player))
				if (target instanceof Player)
				{
					UtilPlayer.message(player, F.main(GetClassType().name(), F.name(((Player)target).getName()) + " is already holding you."));
					return;
				}

		if (_holding.containsValue(target))
		{
			UtilPlayer.message(player, F.main(GetClassType().name(), F.name(UtilEnt.getName(target)) + " is already being held."));
			return;
		}

		//Obstruct Check
		/*
		if (target instanceof Player)
			for (int i = 0 ; i < 10 ; i++)			
			{
				Block block = player.getWorld().getBlockAt(player.getEyeLocation()
						.add((target.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector())).multiply(i/10d)));

				if (!UtilBlock.airFoliage(block))
				{
					UtilPlayer.message(player, F.main(GetClassType().name(), F.name(((Player)target).getName()) + " is obstructed by blocks."));
					return;
				}
			}
		*/

		//Action
		target.leaveVehicle();
		player.eject();
		player.setPassenger(target);
		_holding.put(player, target);
		_time.put(player, System.currentTimeMillis());

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You picked up " + F.name(UtilEnt.getName(target)) + " with " + F.skill(GetName(level)) + "."));
		UtilPlayer.message(target, F.main(GetClassType().name(), F.name(player.getName()) + " grabbed you with " + F.skill(GetName(level)) + "."));

		//Hide!
		if (target instanceof Player)
			VisibilityManager.Instance.setVisibility((Player)target, false, player);
		
		//Event
		UtilServer.getServer().getPluginManager().callEvent(new SkillEvent(player, GetName(), ClassType.Brute, target));

		//Effect
		target.playEffect(EntityEffect.HURT);
	}


	@EventHandler(priority = EventPriority.LOWEST)
	public void DamageePassenger(CustomDamageEvent event) 
	{
		if (event.IsCancelled())
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		if (!_holding.containsValue(damagee))
			return;

		event.SetCancelled(GetName());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void DamagerPassenger(CustomDamageEvent event) 
	{
		if (event.IsCancelled())
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		LivingEntity damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (!_holding.containsKey(damagee))
			return;

		if (!_holding.get(damagee).equals(damager))
			return;

		//Inform
		UtilPlayer.message(damager, F.main(GetClassType().name(), "You cannot attack " + F.name(damagee.getName()) + "."));

		event.SetCancelled(GetName());
	}

	@EventHandler
	public void ThrowExpire(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		HashSet<Player> voidSet = new HashSet<Player>();
		HashSet<Player> throwSet = new HashSet<Player>();

		for (Player cur : _holding.keySet())
		{
			if (cur.getPassenger() == null)
			{
				voidSet.add(cur);
				continue;	
			}

			if (_holding.get(cur).getVehicle() == null)
			{
				voidSet.add(cur);
				continue;	
			}

			if (!_holding.get(cur).getVehicle().equals(cur))
			{
				voidSet.add(cur);
				continue;	
			}

			//Expire 
			if (!cur.isBlocking() || System.currentTimeMillis() - _time.get(cur) > 5000)
			{
				throwSet.add(cur);
			}			
		}

		for (Player cur : voidSet)
		{
			LivingEntity target = _holding.remove(cur);
			_time.remove(cur);
			int level = getLevel(cur);

			UtilPlayer.message(cur, F.main(GetClassType().name(), F.name(UtilEnt.getName(target)) + " escaped your " + F.skill(GetName(level)) + "."));
			
			//Show!
			if (target instanceof Player)
				VisibilityManager.Instance.setVisibility((Player)target, true, cur);
		}

		for (final Player cur : throwSet)
		{
			final LivingEntity target = _holding.remove(cur);			
			long time = _time.remove(cur);
			int level = getLevel(cur);
			
			//Time Reduce
			double timeScale = 1;
			if (time < _chargeTime)
			{
				timeScale = Math.max(0.25, ((double)time/(double)_chargeTime));
			}
			
			//Show!
			if (target instanceof Player)
				VisibilityManager.Instance.setVisibility((Player)target, true, cur);
			
			//Throw
			cur.eject();
			target.leaveVehicle();
			final double mult = (1.2 + (0.2 * level)) * timeScale;
			
			//Delay
			Bukkit.getScheduler().scheduleSyncDelayedTask(Factory.getPlugin(), new Runnable() 
			{
				@Override
				public void run() 
				{
					UtilAction.velocity(target, cur.getLocation().getDirection(), mult, false, 0, 0.2, 1.2, true);
					
					//Condition
					Factory.Condition().Factory().Falling(GetName(), target, cur, 10, false, true);
					
					//Effect
					target.playEffect(EntityEffect.HURT);
				} 
			}, 5);

			//Inform
			UtilPlayer.message(cur, F.main(GetClassType().name(), "You threw " + F.name(UtilEnt.getName(target)) + " with " + F.skill(GetName(level)) + "."));
			UtilPlayer.message(target, F.main(GetClassType().name(), F.name(cur.getName()) + " threw you with " + F.skill(GetName(level)) + "."));
		}
	}

	@Override
	public void Reset(Player player) 
	{
		player.eject();
		player.leaveVehicle();
		
		
		for (Player cur : _holding.keySet())
		{
			if (cur.equals(player) || _holding.get(cur).equals(player))
			{
				cur.eject();
				
				LivingEntity target = _holding.remove(cur);
				_time.remove(cur);
				
				//Show!
				if (target instanceof Player)
					VisibilityManager.Instance.setVisibility((Player)target, true, cur);
			}
		}
		
		_holding.remove(player);
		_time.remove(player);
	}
}
