package mineplex.core.disguise.disguises;

import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;

public class DisguiseSkeleton extends DisguiseMonster
{
	public DisguiseSkeleton(org.bukkit.entity.Entity entity)
	{
		super(EntityType.SKELETON, entity);
		
		DataWatcher.a(13, Byte.valueOf((byte)0));
	}
	
	public void SetSkeletonType(SkeletonType skeletonType)
	{
		DataWatcher.watch(13, Byte.valueOf((byte)skeletonType.getId()));
	}
	
	public int GetSkeletonType()
	{
		return DataWatcher.getByte(13);
	}
	
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }
}
