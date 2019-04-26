package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;

public class DisguiseVillager extends DisguiseAgeable
{
	public DisguiseVillager(Entity entity)
	{
		super(EntityType.VILLAGER, entity);
	}

	@Override
	protected String getHurtSound()
	{
		return "mob.villager.hit";
	}
}
