package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillChargeSword;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class StaticLazer extends SkillChargeSword
{
	public StaticLazer(SkillFactory skills, String name,
					   IPvpClass.ClassType classType, SkillType skillType,
					   int cost, int maxLevel)
	{
		super(skills, name, classType, skillType, cost, maxLevel, 
				0.012f, 0.004f, 
				12000, -1000, true, true,
				false, true);

		SetDesc(new String[] 
				{
				"Hold Block to charge static electricity.",
				"Release Block to fire static lazer.",
				"",
				GetChargeString(),
				"Taking damage cancels charge.",
				"",
				"Deals #6#1 damage and travels up to",
				"#20#10 blocks.",
				});
		
		_fireOnFull = false;
		_energyPerCharge = 1.2f;
		setAchievementSkill(true);
	}
	
	@Override
	public String GetRechargeString() 
	{
		return "Recharge: " + "#12#-1 Seconds";
	}
	
	@Override
	public String GetEnergyString() 
	{
		return "Energy: " + "24 per Second";
	}

	@Override
	public void DoSkillCustom(Player player, float charge)
	{
		int level = getLevel(player);
		if (level <= 0)
			return;
		
		//Action
		double curRange = 0;
		while (curRange <= 20 + 10 * level)
		{
			Location newTarget = player.getEyeLocation().add(player.getLocation().getDirection().multiply(curRange));

			//Hit Player
			HashMap<LivingEntity, Double> hits = UtilEnt.getInRadius(newTarget, 2);
			hits.remove(player);
			if (!hits.isEmpty())
				break;
			
			//Hit Block
			if (!UtilBlock.airFoliage(newTarget.getBlock()))
				break;
				
			//Progress Forwards
			curRange += 0.2;

			//Smoke Trail
			UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, newTarget, 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
		}

		//Destination
		Location target = player.getLocation().add(player.getLocation().getDirection().multiply(curRange));
		
		UtilParticle.PlayParticle(ParticleType.EXPLODE, target, 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		//Firework 
		UtilFirework.playFirework(player.getLocation().add(player.getLocation().getDirection().multiply(Math.max(0, curRange - 0.6))), Type.BURST, Color.WHITE, false, false);
		
		HashMap<LivingEntity, Double> hit = UtilEnt.getInRadius(target.subtract(0, 1, 0), 4);
		for (LivingEntity other : hit.keySet())
		{
			if (other.equals(player))
				continue;

			//Damage Event
			Factory.Damage().NewDamageEvent(other, player, null, other.getLocation(),
					DamageCause.CUSTOM, 6 + level * charge, true, true, false,
					player.getName(), GetName());	
			
		}

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(getLevel(player))) + "."));

		//Effect
		player.getWorld().playSound(player.getEyeLocation(), Sound.ZOMBIE_REMEDY, 0.5f + player.getExp(), 1.75f - charge);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void damageCancelCharge(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		//Damagee
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (_charge.remove(damagee) == null)
			return;
		
		//Inform
		UtilPlayer.message(damagee, F.main(GetClassType().name(), F.skill(GetName()) + " was interrupted."));

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.ZOMBIE_REMEDY, 0.5f, 3f);

	}

	@Override
	public void Reset(Player player) 
	{
		_charge.remove(player);
	}
}
