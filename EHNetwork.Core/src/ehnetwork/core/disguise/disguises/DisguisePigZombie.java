package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;

public class DisguisePigZombie extends DisguiseZombie
{
	public DisguisePigZombie(org.bukkit.entity.Entity entity)
	{
		super(EntityType.PIG_ZOMBIE, entity);
	}
	
    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }
}
