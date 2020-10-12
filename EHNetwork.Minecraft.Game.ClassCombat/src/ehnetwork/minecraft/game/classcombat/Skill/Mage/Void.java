package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import java.util.HashSet;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Void extends Skill
{
	private HashSet<Player> _active = new HashSet<Player>();

	public Void(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Drop Axe/Sword to Toggle.",
				"",
				"While in void form, you receive",
				"Slow 3, take no knockback and",
				"use no energy to swing weapons.",
				"",
				"Reduces incoming damage by #1#1 , but",
				"burns #11#-1 Energy per 1 damage reduced."
				});
	}

	@Override
	public String GetEnergyString()
	{
		return "Energy: 6 per Second";
	}

	@EventHandler
	public void Toggle(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();

		if (!UtilGear.isWeapon(event.getItemDrop().getItemStack()))
			return;

		if (getLevel(player) == 0)				
			return;

		event.setCancelled(true);  

		//Check Allowed
		SkillTriggerEvent trigger = new SkillTriggerEvent(player, GetName(), GetClassType());
		UtilServer.getServer().getPluginManager().callEvent(trigger);
		if (trigger.IsCancelled())
			return;

		if (_active.contains(player))
		{
			_active.remove(player);
			UtilPlayer.message(player, F.main(GetClassType().name(), GetName() + ": " + F.oo("Disabled", false)));

			//Remove Condition
			Factory.Condition().EndCondition(event.getPlayer(), null, GetName());
		}
		else
		{
			if (!Factory.Energy().Use(player, "Enable " + GetName(), 10, true, true))
				return;

			_active.add(player);
			UtilPlayer.message(player, F.main(GetClassType().name(), GetName() + ": " + F.oo("Enabled", true)));
			
			//Remove Condition
			Factory.Condition().EndCondition(event.getPlayer(), null, GetName());
		} 
	}
	
	@EventHandler
	public void Silence(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{
			if (!_active.contains(cur))
				continue;
			
			//Check Allowed
			SkillTriggerEvent trigger = new SkillTriggerEvent(cur, GetName(), GetClassType());
			UtilServer.getServer().getPluginManager().callEvent(trigger);
			if (trigger.IsCancelled())
				_active.remove(cur);
		}
	}

	@EventHandler
	public void Audio(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (Player cur : _active)
			cur.getWorld().playSound(cur.getLocation(), Sound.BLAZE_BREATH, 0.5f, 0.5f);
	}

	@EventHandler
	public void Aura(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : _active)
		{	
			if (cur.isDead())
				continue;
			
			//Energy
			Factory.Energy().ModifyEnergy(cur, -0.3);
		}
	}
	
	@EventHandler
	public void Conditions(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (Player cur : _active)
		{	
			if (cur.isDead())
				continue;
			
			//Condition
			Factory.Condition().Factory().Invisible(GetName(), cur, cur, 1.9, 1, false, true, true);
			Factory.Condition().Factory().Slow(GetName(), cur, cur, 1.9, 2, false, true, false, true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Shield(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (!_active.contains(damagee))
			return;

		int level = getLevel(damagee);
		if (level == 0)			return;

		double dmgToEnergy = 11 - level;
		int dmgLower = 1 + level;
		
		int currentEnergy = (int)Factory.Energy().GetCurrent(damagee);
		double requiredEnergy = Math.min(dmgLower, event.GetDamage()) * dmgToEnergy;

		//Lower
		if (currentEnergy < requiredEnergy)
			dmgLower = (int)(currentEnergy/dmgToEnergy);

		//Use Energy
		Factory.Energy().ModifyEnergy(damagee, -requiredEnergy);

		//Damage
		event.AddMod(damagee.getName(), GetName(), -dmgLower, false);
		event.SetKnockback(false);

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.BLAZE_BREATH, 2f, 1f);
	} 


	@Override
	public void Reset(Player player) 
	{ 
		_active.remove(player);
		
		//Remove Condition
		Factory.Condition().EndCondition(player, null, GetName());
	}
}
