package mineplex.hub.server.ui;

import mineplex.hub.server.ServerInfo;

import org.bukkit.entity.Player;

public interface IServerPage
{
	void SelectServer(Player player, ServerInfo _serverInfo);
}
