package ehnetwork.core.packethandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class PacketVerifier
{
	private static Field _destroyId;
	private Player _owner;
	
	private List<IPacketHandler> _packetHandlers = new ArrayList<IPacketHandler>();

    public PacketVerifier(Player owner) 
    {
        _owner = owner;
        
        if (_destroyId == null)
        {
			try
			{
				_destroyId = PacketPlayOutEntityDestroy.class.getDeclaredField("a");
				_destroyId.setAccessible(true);
			}
			catch (Exception exception)
			{
				System.out.println("Field exception in CustomTagFix : ");
				exception.printStackTrace();
			}
        }
    }

    public boolean verify(Packet o) 
    {
		PacketInfo packetInfo = new PacketInfo(_owner, o, this);
		
		for (IPacketHandler handler : _packetHandlers)
		{
			handler.handle(packetInfo);
		}
		
		return !packetInfo.isCancelled();
    }
    
    public void bypassProcess(Packet packet)
    {
    	((CraftPlayer)_owner).getHandle().playerConnection.networkManager.handle(packet);
    }
    
    public void Deactivate()
    {
		_owner = null;
    }

	public void process(Packet packet)
	{
		((CraftPlayer)_owner).getHandle().playerConnection.sendPacket(packet);
	}

	public void clearHandlers()
	{
		_packetHandlers.clear();
	}

	public void addPacketHandler(IPacketHandler packetHandler)
	{
		_packetHandlers.add(packetHandler);
	}
	
	public void removePacketHandler(IPacketHandler packetHandler)
    {
        _packetHandlers.remove(packetHandler);
    }
}
