package ehnetwork.hub.server.ui;

import org.bukkit.entity.Player;

import ehnetwork.hub.server.ServerInfo;

public interface IServerPage
{
	void SelectServer(Player player, ServerInfo _serverInfo);
}
