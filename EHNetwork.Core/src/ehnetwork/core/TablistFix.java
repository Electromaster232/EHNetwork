package ehnetwork.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TablistFix extends MiniPlugin
{
	public TablistFix(JavaPlugin plugin)
	{
		super("Tablist Fix", plugin);
	}

	// This is sort of experimental!
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		runSyncLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (!player.isOnline())
					return;

				/*PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo()zzzz(((CraftPlayer) player).getHandle());

				if (UtilPlayer.is1_8(player))
				{
					UtilPlayer.sendPacket(player, packet);
				}

				for (Player other : UtilServer.getPlayers())
				{
					if (other.equals(player) || !other.canSee(player))
						continue;

					if (UtilPlayer.is1_8(other))
						UtilPlayer.sendPacket(other, packet);
				}

				 */
			}
		}, 20L);
	}
}
