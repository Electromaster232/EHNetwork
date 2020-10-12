package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguisePig extends DisguiseAnimal
{
	public DisguisePig(org.bukkit.entity.Entity entity)
	{
		super(EntityType.PIG, entity);
	}
	
	public String getHurtSound()
	{
		return "mob.pig.say";
	}
}
