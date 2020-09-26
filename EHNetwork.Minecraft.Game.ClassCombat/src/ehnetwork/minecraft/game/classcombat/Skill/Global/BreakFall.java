package ehnetwork.minecraft.game.classcombat.Skill.Global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class BreakFall extends Skill
{
	public BreakFall(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"You roll when you hit the ground;",
				"Fall damage is reduced by #0#2.",
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.FALL)
			return;

		Player player = event.GetDamageePlayer();
		if (player == null)		return;

		int level = getLevel(player);
		if (level == 0)			return;

		event.AddMod(null, GetName(), -(2 * level), false);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
