package mineplex.core.packethandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R4.IPacketVerifier;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.PacketPlayOutUpdateAttributes;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketVerifier implements IPacketVerifier
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
    
    @Override
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
    	((CraftPlayer)_owner).getHandle().playerConnection.networkManager.handle(packet, new GenericFutureListener[0]);
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
