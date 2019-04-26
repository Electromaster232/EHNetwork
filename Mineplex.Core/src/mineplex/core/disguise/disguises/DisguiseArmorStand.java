package mineplex.core.disguise.disguises;

import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.EnumEntitySize;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

public class DisguiseArmorStand extends DisguiseInsentient
{
	private Vector _headPosition = new Vector();

	public DisguiseArmorStand(org.bukkit.entity.Entity entity)
	{
		super(entity);

		DataWatcher.a(10, (byte) 0);
		
		for (int i = 11; i < 17; i++)
		{
			DataWatcher.a(i, new Vector(0, 0, 0));
		}

		// Rotations are from -360 to 360
	}

	public Vector getHeadPosition()
	{
		return _headPosition.clone();
	}

	protected String getHurtSound()
	{
		return null;
	}

	@Override
	public Packet GetSpawnPacket()
	{
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		packet.a = Entity.getId();
		packet.b = (byte) 30;
		packet.c = (int) EnumEntitySize.SIZE_2.a(Entity.locX);
		packet.d = (int) MathHelper.floor(Entity.locY * 32.0D);
		packet.e = (int) EnumEntitySize.SIZE_2.a(Entity.locZ);
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

		packet.f = (int) (var4 * 8000.0D);
		packet.g = (int) (var6 * 8000.0D);
		packet.h = (int) (var8 * 8000.0D);

		packet.l = DataWatcher;
		packet.m = DataWatcher.b();

		return packet;
	}

	public void setBodyPosition(Vector vector)
	{
		DataWatcher.watch(12, vector);
	}

	public void setHasArms()
	{
		DataWatcher.watch(10, (byte) DataWatcher.getByte(10) | 4);
	}

	public void setHeadPosition(Vector vector)
	{
		_headPosition = vector;
		DataWatcher.watch(11, vector);
	}

	public void setLeftArmPosition(Vector vector)
	{
		DataWatcher.watch(13, vector);
	}

	public void setLeftLegPosition(Vector vector)
	{
		DataWatcher.watch(15, vector);
	}

	public void setRemoveBase()
	{
		DataWatcher.watch(10, (byte) DataWatcher.getByte(10) | 8);
	}

	public void setRightArmPosition(Vector vector)
	{
		DataWatcher.watch(14, vector);
	}

	public void setRightLegPosition(Vector vector)
	{
		DataWatcher.watch(16, vector);
	}

	public void setSmall()
	{
		DataWatcher.watch(10, (byte) DataWatcher.getByte(10) | 1);
	}

	public void setGravityEffected()
	{
		DataWatcher.watch(10, (byte) DataWatcher.getByte(10) | 2);
	}
}
