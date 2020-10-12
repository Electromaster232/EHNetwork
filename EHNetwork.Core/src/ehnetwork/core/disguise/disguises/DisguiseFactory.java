package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;


public class DisguiseFactory
{
	public static MobDisguise createDisguise(Entity disguised, EntityType disguiseType)
	{
		MobDisguise r1 = new MobDisguise(DisguiseType.getType(disguiseType));
		r1.setEntity(disguised);
		return r1;
	}
}
