package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public abstract class DisguiseMonster extends DisguiseCreature
{
	public DisguiseMonster(EntityType disguiseType, org.bukkit.entity.Entity entity)
	{
		super(disguiseType, entity);
	}
}
