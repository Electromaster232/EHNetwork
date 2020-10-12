package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class GlacialBlade extends SkillActive implements IThrown
{
	public GlacialBlade(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Swinging your sword releases a",
				"shard of ice, dealing 3.5 damage",
				"to anything it hits.",
				"",
				"Will not work if enemies are close."
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		for (Player cur : UtilPlayer.getNearby(player.getLocation(), 4))
			if (cur.equals(player))
				continue;
			else if (Factory.Relation().canHurt(cur, player))
				return false;
		
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
			return false;
		
		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Action
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()).subtract(0, 0.2, 0), ItemStackFactory.Instance.CreateStack(370));
		UtilAction.velocity(item, player.getLocation().getDirection(), 1.6, false, 0, 0.2, 10, false);
		Factory.Projectile().AddThrow(item, player, this, -1, true, true, true, 
				null, 0, 0, ParticleType.SNOW_SHOVEL, UpdateType.TICK, 0.3f);

		//Effect
		item.getWorld().playSound(item.getLocation(), Sound.ORB_PICKUP, 1f, 2f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{		
		//Effect
		data.GetThrown().getWorld().playEffect(data.GetThrown().getLocation(), Effect.STEP_SOUND, 20);

		//Remove
		data.GetThrown().remove();

		if (target == null)
			return;

		//Damage
		Factory.Damage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, 3.5, false, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		//Effect
		data.GetThrown().getWorld().playEffect(data.GetThrown().getLocation(), Effect.STEP_SOUND, 20);

		//Remove
		data.GetThrown().remove();
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		//Effect
		data.GetThrown().getWorld().playEffect(data.GetThrown().getLocation(), Effect.STEP_SOUND, 20);

		//Remove
		data.GetThrown().remove();
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
