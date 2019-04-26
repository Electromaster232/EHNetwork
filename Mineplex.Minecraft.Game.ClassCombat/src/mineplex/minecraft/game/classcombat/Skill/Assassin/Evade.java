package mineplex.minecraft.game.classcombat.Skill.Assassin;

import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.minecraft.game.classcombat.Skill.SkillActive;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class Evade extends SkillActive
{
	private HashSet<Player> _active = new HashSet<Player>();

	public Evade(SkillFactory skills, String name, ClassType classType, SkillType skillType, 
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
				"Block attacks to evade them and",
				"teleport behind the attacker.",
				"",
				"Crouch and Evade to teleport backwards."
				});
	}
	
	@Override
	public String GetEnergyString()
	{
		return "Energy: #26#-2 and #18#-2 per Second";
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return false;
		}
		
		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		_active.add(player);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You prepared to " + F.skill(GetName()) + "."));
	}

	@EventHandler
	public void Energy(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{
			if (!_active.contains(cur))
				continue;

			if (!cur.isBlocking())
			{
				_active.remove(cur);
				continue;
			}

			if (!Factory.Energy().Use(cur, GetName(), 0.9 - (getLevel(cur) * 0.1), true, true))
			{
				_active.remove(cur);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		//Damagee
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		//Blocking
		if (!damagee.isBlocking())
			return;

		//Active
		if (!_active.contains(damagee))
			return;

		//Damager
		LivingEntity damager = event.GetDamagerEntity(false);
		if (damager == null)	return;

		//Level
		int level = getLevel(damagee);
		if (level == 0)			return;

		if (!mineplex.core.recharge.Recharge.Instance.use(damagee, GetName(), 500, false, false))
			return;

		//Cancel
		event.SetCancelled(GetName());
		
		_active.remove(damagee);

		//Effect
		for (int i=0 ; i<3 ; i++)
			damagee.getWorld().playEffect(damagee.getLocation(), Effect.SMOKE, 5);

		//Location
		Location target = null;
		if (damagee.isSneaking())	target = FindLocationBack(damager, damagee);
		else						target = FindLocationBehind(damager, damagee);
		if (target == null)	
			return;
		
		//Effect
		UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, damagee.getLocation(), 
				(float)(Math.random() - 0.5), (float)(Math.random() * 1.4), (float)(Math.random() - 0.5), 0, 10,
				ViewDist.NORMAL, UtilServer.getPlayers());

		//Action
		damagee.teleport(target);

		//Cloak
		if (damagee.isSneaking())
			Factory.Condition().Factory().Cloak(GetName(), damagee, damagee, 0.1, false, false);

		//Invul/Cloak
		Factory.Condition().Factory().Invulnerable(GetName(), damagee, damagee, 0.5, false, false);

		//Inform
		UtilPlayer.message(damagee, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));
		UtilPlayer.message(damager, F.main(GetClassType().name(), F.name(damagee.getName()) +" used " + F.skill(GetName(level)) + "."));
	}

	private Location FindLocationBehind(LivingEntity damager, Player damagee) 
	{
		double curMult = 0;
		double maxMult = 1.5;

		double rate = 0.1;

		Location lastValid = damager.getLocation();

		while (curMult <= maxMult)
		{
			Vector vec = UtilAlg.getTrajectory(damager, damagee).multiply(curMult);
			Location loc = damager.getLocation().subtract(vec);

			if (!UtilBlock.airFoliage(loc.getBlock()) || !UtilBlock.airFoliage(loc.getBlock().getRelative(BlockFace.UP)))
				return lastValid;

			lastValid = loc;

			curMult += rate;
		}

		return lastValid;
	}

	private Location FindLocationBack(LivingEntity damager, Player damagee) 
	{
		double curMult = 0;
		double maxMult = 3;

		double rate = 0.1;

		Location lastValid = damagee.getLocation();

		while (curMult <= maxMult)
		{
			Vector vec = UtilAlg.getTrajectory(damager, damagee).multiply(curMult);
			Location loc = damagee.getLocation().add(vec);

			if (!UtilBlock.airFoliage(loc.getBlock()) || !UtilBlock.airFoliage(loc.getBlock().getRelative(BlockFace.UP)))
				return lastValid;

			lastValid = loc;

			curMult += rate;
		}

		return lastValid;
	}

	@Override
	public void Reset(Player player) 
	{
		_active.remove(player);
	}
}
