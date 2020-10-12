package ehnetwork.core.packethandler;

import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.PacketListener;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class PacketPlayUseEntity extends PacketPlayInUseEntity
{
	private static PacketHandler _packetHandler;


	public void handle(PacketListener packetlistener)
	{
		Player player = ((PlayerConnection) packetlistener).getPlayer();

		PacketVerifier verifier = _packetHandler.getPacketVerifier(player);

		PacketInfo packetInfo = new PacketInfo(player, this, verifier);

		for (IPacketHandler handler : _packetHandler.getPacketHandlers())
		{
			handler.handle(packetInfo);
		}

		if (!packetInfo.isCancelled())
		{
			//a((PacketPlayInListener) packetlistener);
		}
	}
}