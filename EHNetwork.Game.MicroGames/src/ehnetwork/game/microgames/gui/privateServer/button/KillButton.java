package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.managers.GameHostManager;

public class KillButton implements IButton
{
	private MicroGamesManager _arcade;
	private GameHostManager _manager;

	public KillButton(MicroGamesManager microGamesManager)
	{
		_manager = microGamesManager.GetGameHostManager();
		_arcade = microGamesManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		if (clickType == clickType.SHIFT_RIGHT)
		{
			_manager.setHostExpired(true, "The host has closed this Mineplex Private Server.");
		}
	}
}
