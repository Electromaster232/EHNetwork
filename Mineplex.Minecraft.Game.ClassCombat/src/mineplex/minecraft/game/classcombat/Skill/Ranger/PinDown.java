package mineplex.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.core.common.util.F;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.minecraft.game.classcombat.Skill.SkillActive;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class PinDown extends SkillActive
{
	private HashSet<Projectile> _arrows = new HashSet<Projectile>();

	public PinDown(SkillFactory skills, String name, ClassType classType, SkillType skillType,
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
				"Instantly fire an arrow, giving",
				"target Slow 4 for #3#1 seconds."
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

		//Use Arrow
		if (!UtilInv.contains(player, Material.ARROW, (byte)0, 1))
		{
			UtilPlayer.message(player, F.main("Skill", "You need " + F.item("1 Arrow") + " to use " + F.skill(GetName()) + "."));
			return false;
		}

		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Arrow
		UtilInv.remove(player, Material.ARROW, (byte)0, 1);

		//Action
		Projectile proj = player.launchProjectile(Arrow.class);
		_arrows.add(proj);

		//Speed
		proj.setVelocity(player.getLocation().getDirection().multiply(1.6));

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));

		//Effect
		player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
		player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Hit(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile projectile = event.GetProjectile();
		if (projectile == null)	return;

		//Not Pin Down Arrow
		if (!_arrows.contains(projectile))
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		//Level
		int level = getLevel(damager);
		if (level == 0)			return;

		//Cripple
		double dur = 3 + level;
		Factory.Condition().Factory().Slow(GetName(), damagee, damager, dur, 3, false, true, true, true);

		//Damage
		event.AddMod(damager.getName(), GetName(), -2, true);
		event.SetKnockback(false);

		//Effect
		for (int i=0 ; i<3 ; i++)
			damagee.playEffect(EntityEffect.HURT);
		
		//Clean
		_arrows.remove(projectile);
		projectile.remove();
		
		//Inform
		UtilPlayer.message(event.GetDamageePlayer(), F.main(GetClassType().name(), F.name(damager.getName()) +" hit you with " + F.skill(GetName(level)) + "."));
		UtilPlayer.message(damager, F.main(GetClassType().name(), "You hit " + F.name(UtilEnt.getName(event.GetDamageeEntity())) +" with " + F.skill(GetName(level)) + "."));
	}

	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		HashSet<Projectile> remove = new HashSet<Projectile>();

		for (Projectile cur : _arrows)
			if (cur.isDead() || !cur.isValid())
				remove.add(cur);

		for (Projectile cur : remove)
			_arrows.remove(cur);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
