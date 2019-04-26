package mineplex.core.packethandler;

import java.io.IOException;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class PacketPlayResourcePackStatus extends Packet
{
	public enum EnumResourcePackStatus
	{
		ACCEPTED,

		DECLINED,

		FAILED_DOWNLOAD,

		LOADED;
	}

	private static PacketHandler _packetHandler;

	public String ResourcePackUrl;
	private int _resourcePackStatus;

	public void a(PacketDataSerializer packetdataserializer) throws IOException
	{
		ResourcePackUrl = packetdataserializer.c(255);
		_resourcePackStatus = packetdataserializer.a();
	}

	public void b(PacketDataSerializer packetdataserializer) throws IOException
	{
	}

	public EnumResourcePackStatus getResourcePackStatus()
	{
		return EnumResourcePackStatus.values()[_resourcePackStatus];
	}

	public void handle(PacketListener packetListener)
	{
		Player player = ((PlayerConnection) packetListener).getPlayer();
		PacketVerifier verifier = _packetHandler.getPacketVerifier(player);

		PacketInfo packetInfo = new PacketInfo(player, this, verifier);

		for (IPacketHandler handler : _packetHandler.getPacketHandlers())
		{
			handler.handle(packetInfo);
		}
		// ((PlayerConnection) packetListener).PacketVerifier
		// .processPacket(this, ((PlayerConnection) packetListener).networkManager);

	}
}