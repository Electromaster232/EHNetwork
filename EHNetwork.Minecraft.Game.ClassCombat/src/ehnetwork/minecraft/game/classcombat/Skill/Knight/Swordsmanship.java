package ehnetwork.minecraft.game.classcombat.Skill.Knight;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Swordsmanship extends Skill
{
	private HashMap<Player, Integer> _charges = new HashMap<Player, Integer>();
	
	public Swordsmanship(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Prepare a powerful sword attack;",
				"You gain 1 Charge every #5#-1 seconds.",
				"You can store a maximum of #0#1 Charges.",
				"",
				"When you next attack, your damage is",
				"increased by the number of your Charges,",
				"and your Charges are reset to 0.",
				"",
				"This only applies for Swords."
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void IncreaseDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		//Damagee
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		if (!_charges.containsKey(damager))
			return;
		
		if (!UtilGear.isSword(damager.getItemInHand()))
			return;

		event.AddMod(damager.getName(), GetName(), _charges.remove(damager), false);
		
		//Cooldown
		int level = getLevel(damager);
		Recharge.Instance.useForce(damager, GetName(), 5000 - (1000 * level));
	}
	
	@EventHandler
	public void Charge(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player cur : GetUsers())
		{
			int level = getLevel(cur);
			
			if (!Recharge.Instance.use(cur, GetName(), 5000 - (1000 * level), false, false))
				continue;
						
			int max = level;
			
			int charge = 1;
			if (_charges.containsKey(cur))
				charge += _charges.get(cur);
			
			if (charge <= max)
			{
				_charges.put(cur, charge);
				
				//Inform
				UtilPlayer.message(cur, F.main(GetClassType().name(), "Swordsmanship Charges: " + F.elem(charge+"")));
			}
		}
	}

	@Override
	public void Reset(Player player) 
	{
		_charges.remove(player);
	}
}
