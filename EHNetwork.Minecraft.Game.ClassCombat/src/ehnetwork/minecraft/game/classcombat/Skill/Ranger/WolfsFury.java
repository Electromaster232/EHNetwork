package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;
import java.util.WeakHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class WolfsFury extends SkillActive
{
	private WeakHashMap<Player, Long> _active = new WeakHashMap<Player, Long>();
	private HashSet<Player> _swing = new HashSet<Player>();
	private HashSet<Player> _miss = new HashSet<Player>();

	public WolfsFury(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Summon the power of the wolf, gaining",
				"Strength 3 for #2#2 seconds, and giving",
				"no knockback on your attacks.",
				"",
				"If you miss two consecutive attacks,",
				"Wolfs Fury ends."
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
		_active.put(player, System.currentTimeMillis() + 8000);

		//Condition
		Factory.Condition().Factory().Strength(GetName(), player, player, 2 + 2*level, 2, false, true, true);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.WOLF_GROWL, 1.4f, 1.2f);
	}

	@EventHandler
	public void Expire(UpdateEvent event)
	{
		//End Skill
		if (event.getType() == UpdateType.FAST)
		{
			HashSet<Player>	expired = new HashSet<Player>();

			for (Player cur : _active.keySet())
				if (System.currentTimeMillis() > _active.get(cur))
					expired.add(cur);

			for (Player cur : expired)
				End(cur);
		}
	}

	@EventHandler
	public void Swing(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		if(!_active.containsKey(event.getPlayer()))
			return;

		if (!UtilGear.isAxe(event.getPlayer().getItemInHand()) && !UtilGear.isSword(event.getPlayer().getItemInHand()))
			return;

		_swing.add(event.getPlayer());						
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Hit(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;
		
		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		int level = getLevel(damager);
		if (level == 0)			return;
		
		if (!_active.containsKey(damager))
			return;

		//Remove Swing
		_swing.remove(damager);

		//Remove Miss
		_miss.remove(damager);

		//Damage
		event.SetKnockback(false);

		//Effect
		if (!event.IsCancelled())
			damager.getWorld().playSound(damager.getLocation(), Sound.WOLF_BARK, 0.5f, 1.2f);
	}

	@EventHandler
	public void Miss(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		HashSet<Player>	expired = new HashSet<Player>();

		for (Player cur : _swing)
			expired.add(cur);

		for (Player cur : expired)
		{
			_swing.remove(cur);
			
			if (_miss.remove(cur))
			{
				End(cur);
			}
			else
			{
				_miss.add(cur);
			}
		}	
	}

	public void End(Player player)
	{
		Reset(player);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), F.skill(GetName()) + " has ended."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.WOLF_WHINE, 0.6f, 0.8f);
	}
	
	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : _active.keySet())
		{
			UtilParticle.PlayParticle(ParticleType.RED_DUST, player.getLocation(), 
					(float)(Math.random() - 0.5), 0.2f + (float)(Math.random() * 1), (float)(Math.random() - 0.5), 0, 4,
					ViewDist.LONG, UtilServer.getPlayers());
		}
	}

	@Override
	public void Reset(Player player)
	{
		_active.remove(player);
		_swing.remove(player);
		_miss.remove(player);
		Factory.Condition().EndCondition(player, ConditionType.INCREASE_DAMAGE, GetName());
	}
}
