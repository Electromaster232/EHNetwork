package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
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
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Agility extends SkillActive
{
	private HashSet<Player>	_active = new HashSet<Player>();

	public Agility(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Sprint with great agility, gaining",
				"Speed 2 for #3#1 seconds. You take",
				"#45#5 % less damage and take no knockback.",
				"",
				"Agility ends if you Left-Click."	
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
		Factory.Condition().Factory().Speed(GetName(), player, player, 3 + level, 1, false, true, true);
		_active.add(player);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 0.5f, 0.5f);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void End(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (UtilEvent.isAction(event, ActionType.R))
			return;
		
		if (event.getAction() == Action.PHYSICAL)
			return;

		if (!_active.contains(player))
			return;
		
		//Remove
		_active.remove(player);
		player.removePotionEffect(PotionEffectType.SPEED);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player damagee = event.GetDamageePlayer();

		if (damagee == null)
			return;

		if (!damagee.isSprinting())
			return;

		if (!_active.contains(damagee))
			return;

		//Cancel
		event.AddMult(GetName(), GetName(), (0.55 - 0.05 * getLevel(damagee)), false);
		
		event.SetKnockback(false);
		
		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.BLAZE_BREATH, 0.5f, 2f);
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		HashSet<Player>	expired = new HashSet<Player>();
		for (Player cur : _active)
			if (!cur.hasPotionEffect(PotionEffectType.SPEED))
				expired.add(cur);

		for (Player cur : expired)
			_active.remove(cur);
	}
	
	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : _active)
		{
			if (player.isSprinting())
				UtilParticle.PlayParticle(ParticleType.SPELL, player.getLocation(), 
					(float)(Math.random() - 0.5), 0.2f + (float)(Math.random() * 1), (float)(Math.random() - 0.5), 0, 4,
					ViewDist.LONG, UtilServer.getPlayers());
		}
	}

	@Override
	public void Reset(Player player) 
	{
		_active.remove(player);
	}
}
