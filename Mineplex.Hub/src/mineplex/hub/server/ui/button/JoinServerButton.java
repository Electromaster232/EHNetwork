package mineplex.hub.server.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.shop.item.IButton;
import mineplex.hub.server.ServerInfo;
import mineplex.hub.server.ui.IServerPage;

public class JoinServerButton implements IButton
{
	private IServerPage _page;
	private ServerInfo _serverInfo;
	
	public JoinServerButton(IServerPage page, ServerInfo serverInfo)
	{
		_page = page;
		_serverInfo = serverInfo;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.SelectServer(player, _serverInfo);
	}
}
