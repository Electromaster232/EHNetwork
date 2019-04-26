package mineplex.core.packethandler;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

public class PacketPlayOutWorldBorder extends Packet
{
	/**
	 * @0 Set size - newRadius
	 * @1 Gradual Size - oldRadius, newRadius, speed
	 * @2 Set center - centerX, centerZ
	 * @3 Initialize - All feields
	 * @4 Set warning time - warningTime
	 * @5 Set warning blocks - warningBlocks
	 */
	public int worldBorderType;
	// public int damageBoundry;
	public double centerX;
	public double centerZ;
	public double newRadius;
	public double oldRadius;
	/**
	 * Time in millis until new border reached
	 */
	public long speed;
	public int warningBlocks;
	public int warningTime;

	public PacketPlayOutWorldBorder()
	{
	}

	@Override
	public void a(PacketDataSerializer paramPacketDataSerializer)
	{
	}

	@Override
	public void b(PacketDataSerializer paramPacketDataSerializer)
	{
		paramPacketDataSerializer.b(worldBorderType);

		switch (worldBorderType)
		{
		case 0:
			paramPacketDataSerializer.writeDouble(newRadius * 2);
			break;
		case 1:
			paramPacketDataSerializer.writeDouble(oldRadius * 2);
			paramPacketDataSerializer.writeDouble(newRadius * 2);
			paramPacketDataSerializer.b((int) speed);
			break;
		case 2:
			paramPacketDataSerializer.writeDouble(centerX);
			paramPacketDataSerializer.writeDouble(centerZ);
			break;
		case 3:
			paramPacketDataSerializer.writeDouble(centerX);
			paramPacketDataSerializer.writeDouble(centerZ);
			paramPacketDataSerializer.writeDouble(oldRadius * 2);
			paramPacketDataSerializer.writeDouble(newRadius * 2);
			paramPacketDataSerializer.b((int) speed);

			// paramPacketDataSerializer.b(damageBoundry);
			paramPacketDataSerializer.b(29999984);
			paramPacketDataSerializer.b(warningTime);
			paramPacketDataSerializer.b(warningBlocks);
			break;
		case 4:
			paramPacketDataSerializer.b(warningTime);
			break;
		case 5:
			paramPacketDataSerializer.b(warningBlocks);
			break;
		}
	}

	@Override
	public void handle(PacketListener arg0)
	{
	}
}