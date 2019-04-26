package mineplex.minecraft.game.classcombat.Skill.Assassin;

import java.util.HashSet;
import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.common.util.UtilTime;
import mineplex.minecraft.game.classcombat.Skill.Skill;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class ComboAttack extends Skill
{
	private WeakHashMap<Player, Float> _repeat = new WeakHashMap<Player, Float>();
	private WeakHashMap<Player, Long> _last = new WeakHashMap<Player, Long>();

	public ComboAttack(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Each time you attack, your damage",
				"increases by 1.",
				"Maximum of #0#1 bonus damage.",
				"",
				"Not attacking for 2 seconds clears",
				"your bonus damage."
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		//Damager
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return; 

		int level = getLevel(damager);
		if (level == 0)			return;

		if (!_repeat.containsKey(damager))
			_repeat.put(damager, 0.5f);

		//Damage
		event.AddMod(damager.getName(), GetName(), _repeat.get(damager), true);

		//Increase Repeat
		_repeat.put(damager, Math.min(level, _repeat.get(damager) + 1f));
		_last.put(damager, System.currentTimeMillis());
		
		//Sound
		damager.getWorld().playSound(damager.getLocation(), Sound.NOTE_STICKS, 1f, 0.7f + (0.3f * _repeat.get(damager)));
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		HashSet<Player>	remove = new HashSet<Player>();

		for (Player cur : _repeat.keySet())
			if (UtilTime.elapsed(_last.get(cur), 2000))
				remove.add(cur);

		for (Player cur : remove)
		{
			_repeat.remove(cur);
			_last.remove(cur);
		}
	}

	@Override
	public void Reset(Player player) 
	{
		_repeat.remove(player);
		_last.remove(player);
	}
}
