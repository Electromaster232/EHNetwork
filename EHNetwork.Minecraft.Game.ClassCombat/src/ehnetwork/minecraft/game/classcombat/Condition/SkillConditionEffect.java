package ehnetwork.minecraft.game.classcombat.Condition;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;
import ehnetwork.minecraft.game.core.condition.ConditionEffect;
import ehnetwork.minecraft.game.core.condition.ConditionManager;

public class SkillConditionEffect extends ConditionEffect
{
	public SkillConditionEffect(ConditionManager manager)
	{
		super(manager);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void Silence(SkillTriggerEvent event)
	{
		if (event.IsCancelled())
			return;

		if (!Manager.IsSilenced(event.GetPlayer(), event.GetSkillName()))
			return;

		//Set Damage
		event.SetCancelled(true);
	}
}
