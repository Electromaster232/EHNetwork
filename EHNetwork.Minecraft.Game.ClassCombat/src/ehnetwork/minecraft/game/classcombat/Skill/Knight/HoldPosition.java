package ehnetwork.minecraft.game.classcombat.Skill.Knight;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.condition.Condition;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class HoldPosition extends SkillActive
{
	public HoldPosition(SkillFactory skills, String name, ClassType classType, SkillType skillType, 
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
				"Hold your position, gaining",
				"Protection 3, Slow 4 and no",
				"knockback for #3#1 seconds."
				});
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
		double duration = 3 + (1 * level);

		//Action
		Factory.Condition().Factory().Slow(GetName(), player, player, duration, 3, false, true, false, true);
		Factory.Condition().Factory().Protection(GetName(), player, player, duration, 2, false, true, true);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 1.5f, 0f);
		player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 49);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		int level = getLevel(damagee);
		if (level == 0)		return;

		Condition data = Factory.Condition().GetActiveCondition(damagee, ConditionType.DAMAGE_RESISTANCE);
		if (data == null)	return;

		if (!data.GetReason().equals(GetName()))
			return;

		//Damage
		event.AddMod(damagee.getName(), GetName(), 0, false);
		event.SetKnockback(false);
	}

	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetUsers())
		{
			Condition data = Factory.Condition().GetActiveCondition(player, ConditionType.DAMAGE_RESISTANCE);
			if (data == null)	continue;

			if (!data.GetReason().equals(GetName()))
				continue;

			UtilParticle.PlayParticle(ParticleType.MOB_SPELL, player.getLocation(), 
					(float)(Math.random() - 0.5), 0.2f + (float)(Math.random() * 1), (float)(Math.random() - 0.5), 0, 3 + getLevel(player),
					ViewDist.NORMAL, UtilServer.getPlayers());
		}
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
