package ehnetwork.core.packethandler;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.NautHashMap;

public class PacketHandler extends MiniPlugin
{
	private NautHashMap<Player, PacketVerifier> _playerVerifierMap = new NautHashMap<Player, PacketVerifier>();
	private HashSet<IPacketHandler> _packetHandlers = new HashSet<IPacketHandler>();

	public PacketHandler(JavaPlugin plugin)
	{
		super("PacketHandler", plugin);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
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
