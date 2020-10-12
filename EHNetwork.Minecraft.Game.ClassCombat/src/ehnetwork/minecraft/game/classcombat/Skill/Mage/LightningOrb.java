package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.player.PlayerEvent;

import ehnetwork.core.common.util.F;
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

public class LightningOrb extends SkillActive implements IThrown
{
	public static class LightningOrbEvent extends PlayerEvent
	{
		private static final HandlerList handlers = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return handlers;
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}

		private final List<LivingEntity> _struck;

		public LightningOrbEvent(Player who, List<LivingEntity> struck)
		{
			super(who);

			_struck = struck;
		}

		public List<LivingEntity> getStruck()
		{
			return _struck;
		}
	}

	public LightningOrb(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Launch a lightning orb. Upon a direct",
				"hit with player, or #5#-0.4 seconds, it will",
				"strike all enemies within #3#0.5 Blocks ",
				"with lightning, giving them Slow 2",
				"for 4 seconds."
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
		//Action
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(57));
		item.setVelocity(player.getLocation().getDirection());
		Factory.Projectile().AddThrow(item, player, this, System.currentTimeMillis() + 5000 - (400 * level), true, false, false, 
				Sound.FIZZ, 0.6f, 1.6f, ParticleType.FIREWORKS_SPARK, UpdateType.TICK, 0.4f);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));

		//Effect 
		item.getWorld().playSound(item.getLocation(), Sound.SILVERFISH_HIT, 2f, 1f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		Strike(target, data);
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Strike(null, data);
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Strike(null, data);
	}

	public void Strike(LivingEntity target, ProjectileUser data)
	{
		//Remove
		data.GetThrown().remove();

		//Thrower
		if (!(data.GetThrower() instanceof Player))
			return;

		Player player = (Player)data.GetThrower();

		//Level
		int level = getLevel(player);
		if (level == 0)				return;

		
		HashMap<LivingEntity, Double> hit = UtilEnt.getInRadius(data.GetThrown().getLocation(), 3 + 0.5 * level);
		
		//Lightning Condition 
		for (LivingEntity cur : hit.keySet())
		{	
			Factory.Condition().Factory().Lightning(GetName(), cur, player, 0, 0.5, false, true);
		}

		List<LivingEntity> struck = new ArrayList<>();

		//Lightning
		for (LivingEntity cur : hit.keySet())
		{
			if (cur.equals(player))
				continue;
			
			if (cur instanceof Player)
			{
				//Inform
				UtilPlayer.message(cur, F.main(GetClassType().name(), F.name(player.getName()) + " hit you with " + F.skill(GetName(level)) + "."));
			}

			//Lightning
			cur.getWorld().strikeLightning(cur.getLocation());
			
			struck.add(cur);
		}

		//Apply Conditions
		for (LivingEntity cur : hit.keySet())
		{	
			if (cur.equals(player))
				continue;
			
			Factory.Condition().Factory().Slow(GetName(), cur, player, 4, 1, false, true, true, true);
		}

		Bukkit.getPluginManager().callEvent(new LightningOrbEvent(player, struck));
	}

	@EventHandler
	public void CancelFire(BlockIgniteEvent event)
	{
		if (event.getCause() == IgniteCause.LIGHTNING)
			event.setCancelled(true);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
