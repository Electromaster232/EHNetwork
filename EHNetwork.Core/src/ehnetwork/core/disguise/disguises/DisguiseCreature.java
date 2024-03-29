package ehnetwork.core.disguise.disguises;

import org.bukkit.entity.EntityType;
import net.minecraft.server.v1_7_R4.EnumEntitySize;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

public abstract class DisguiseCreature extends DisguiseInsentient
{
	private final EntityType _disguiseType;

	public DisguiseCreature(EntityType disguiseType, org.bukkit.entity.Entity entity)
	{
		super(entity);

		_disguiseType = disguiseType;
	}

	public EntityType getDisguiseType()
	{
		return _disguiseType;
	}

	@SuppressWarnings("deprecation")
	public Packet GetSpawnPacket()
	{
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		packet.a = Entity.getId();
		packet.b = (byte) getDisguiseType().getTypeId();
		packet.c = (int)EnumEntitySize.SIZE_2.a(Entity.locX);
		packet.d = (int)MathHelper.floor(Entity.locY * 32.0D);
		packet.e = (int)EnumEntitySize.SIZE_2.a(Entity.locZ);
		packet.i = (byte) ((int) (Entity.yaw * 256.0F / 360.0F));
		packet.j = (byte) ((int) (Entity.pitch * 256.0F / 360.0F));
		packet.k = (byte) ((int) (Entity.yaw * 256.0F / 360.0F));

        double var2 = 3.9D;
        double var4 = 0;
        double var6 = 0;
        double var8 = 0;

        if (var4 < -var2)
        {
            var4 = -var2;
        }

        if (var6 < -var2)
        {
            var6 = -var2;
        }

        if (var8 < -var2)
        {
            var8 = -var2;
        }

        if (var4 > var2)
        {
            var4 = var2;
        }

        if (var6 > var2)
        {
            var6 = var2;
        }

        if (var8 > var2)
        {
            var8 = var2;
        }

        packet.f = (int)(var4 * 8000.0D);
        packet.g = (int)(var6 * 8000.0D);
        packet.h = (int)(var8 * 8000.0D);
		
        packet.l = DataWatcher;
		packet.m = DataWatcher.b();
		
		return packet;
	}
}
