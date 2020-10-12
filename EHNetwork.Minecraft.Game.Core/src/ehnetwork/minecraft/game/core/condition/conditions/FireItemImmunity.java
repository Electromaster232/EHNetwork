package ehnetwork.minecraft.game.core.condition.conditions;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import ehnetwork.minecraft.game.core.condition.Condition;
import ehnetwork.minecraft.game.core.condition.ConditionManager;

public class FireItemImmunity extends Condition
{
	public FireItemImmunity(ConditionManager manager, String reason, LivingEntity ent,
			LivingEntity source, ConditionType type, int mult, int ticks,
			boolean add, Material visualType, byte visualData,
			boolean showIndicator) 
	{
		super(manager, reason, ent, source, type, mult, ticks, add, visualType,
				visualData, showIndicator, false);
	}
}
