package ehnetwork.core.common.util;

import org.bukkit.entity.*;

public class UtilSkull
{
	public static byte getSkullData(Entity entity)
	{
		if (entity instanceof Skeleton)
		{
			Skeleton sk = ((Skeleton) entity);
			if (sk.getSkeletonType() == Skeleton.SkeletonType.WITHER)
				return 1;
			else return 0;
		}
		else if (entity instanceof Zombie || entity instanceof Giant)
		{
			return 2;
		}
		else if (entity instanceof Creeper)
		{
			return 4;
		}
		else
			return 3;
	}

	public static boolean isPlayerHead(byte data)
	{
		return data == 3;
	}

	public static String getPlayerHeadName(Entity entity)
	{
		String name = "MHF_Alex";

		// order is important for some of these
		if (entity instanceof Blaze)
			name = "MHF_Blaze";
		else if (entity instanceof CaveSpider)
			name = "MHF_CaveSpider";
		else if (entity instanceof Spider)
			name = "MHF_Spider";
		else if (entity instanceof Chicken)
			name = "MHF_Chicken";
		else if (entity instanceof MushroomCow)
			name = "MHF_MushroomCow";
		else if (entity instanceof Cow)
			name = "MHF_Cow";
		else if (entity instanceof Creeper)
			name = "MHF_Creeper";
		else if (entity instanceof Enderman)
			name = "MHF_Enderman";
		else if (entity instanceof Ghast)
			name = "MHF_Ghast";
		else if (entity instanceof Golem)
			name = "MHF_Golem";
		else if (entity instanceof PigZombie)
			name = "MHF_PigZombie";
		else if (entity instanceof MagmaCube)
			name = "MHF_LavaSlime";
		else if (entity instanceof Slime)
			name = "MHF_Slime";
		else if (entity instanceof Ocelot)
			name = "MHF_Ocelot";
		else if (entity instanceof PigZombie)
			name = "MHF_PigZombie";
		else if (entity instanceof Pig)
			name = "MHF_Pig";
		else if (entity instanceof Sheep)
			name = "MHF_Pig";
		else if (entity instanceof Squid)
			name = "MHF_Squid";
		else if (entity instanceof HumanEntity)
			name = "MHF_Steve";
		else if (entity instanceof Villager)
			name = "MHF_Villager";

		return name;
	}

}
