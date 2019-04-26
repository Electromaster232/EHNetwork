package mineplex.core.packethandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;

import net.minecraft.server.v1_7_R4.EnumProtocol;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.ProtocolInjector;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.NautHashMap;

public class PacketHandler extends MiniPlugin
{
	private NautHashMap<Player, PacketVerifier> _playerVerifierMap = new NautHashMap<Player, PacketVerifier>();
	private HashSet<IPacketHandler> _packetHandlers = new HashSet<IPacketHandler>();

	public PacketHandler(JavaPlugin plugin)
	{
		super("PacketHandler", plugin);

		try
		{
			// TODO Remove this when if incoming packets are ever listened to in the future.

			for (Class clss : new Class[]
				{
						PacketPlayResourcePackStatus.class, PacketPlayUseEntity.class
				})
			{
				Field field = clss.getDeclaredField("_packetHandler");

				field.setAccessible(true);
				field.set(null, this);
			}

			EnumProtocol.PLAY.a().put(25, PacketPlayResourcePackStatus.class);
			EnumProtocol.PLAY.a().put(PacketPlayResourcePackStatus.class, 25);

			EnumProtocol.PLAY.a().put(2, PacketPlayUseEntity.class);
			EnumProtocol.PLAY.a().put(PacketPlayUseEntity.class, 2);
			
			Method method = ProtocolInjector.class.getDeclaredMethod("addPacket", EnumProtocol.class,boolean.class, int.class, Class.class);
			method.setAccessible(true);
			
			method.invoke(null, EnumProtocol.PLAY, true, 68, PacketPlayOutWorldBorder.class);
			
		//	EnumProtocol.PLAY.b().put(68, PacketPlayOutWorldBorder.class);
		//	EnumProtocol.PLAY.b().put(PacketPlayOutWorldBorder.class, 68);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		_playerVerifierMap.put(event.getPlayer(), new PacketVerifier(event.getPlayer()));
		((CraftPlayer) event.getPlayer()).getHandle().playerConnection.PacketVerifier.addPacketVerifier(_playerVerifierMap
				.get(event.getPlayer()));

		for (IPacketHandler packetHandler : _packetHandlers)
		{
			_playerVerifierMap.get(event.getPlayer()).addPacketHandler(packetHandler);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		((CraftPlayer) event.getPlayer()).getHandle().playerConnection.PacketVerifier.clearVerifiers();
		_playerVerifierMap.remove(event.getPlayer()).clearHandlers();
	}

	public PacketVerifier getPacketVerifier(Player player)
	{
		return _playerVerifierMap.get(player);
	}

	public void addPacketHandler(IPacketHandler packetHandler)
	{
		_packetHandlers.add(packetHandler);

		for (PacketVerifier verifier : _playerVerifierMap.values())
		{
			verifier.addPacketHandler(packetHandler);
		}
	}

	public HashSet<IPacketHandler> getPacketHandlers()
	{
		return _packetHandlers;
	}

	public void removePacketHandler(IPacketHandler packetHandler)
	{
		_packetHandlers.remove(packetHandler);

		for (PacketVerifier verifier : _playerVerifierMap.values())
		{
			verifier.removePacketHandler(packetHandler);
		}
	}
}
