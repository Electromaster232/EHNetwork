package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguiseChicken extends DisguiseAnimal
{
	public DisguiseChicken(org.bukkit.entity.Entity entity)
	{
		super(EntityType.CHICKEN, entity);
	}
	
	public String getHurtSound()
	{
		return "mob.chicken.hurt";
	}
}
