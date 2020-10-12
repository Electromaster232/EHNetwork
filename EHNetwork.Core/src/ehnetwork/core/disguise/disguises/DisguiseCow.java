package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguiseCow extends DisguiseAnimal
{
	public DisguiseCow(org.bukkit.entity.Entity entity)
	{
		super(EntityType.COW, entity);
	}
	
	public String getHurtSound()
	{
		return "mob.cow.hurt";
	}
}
