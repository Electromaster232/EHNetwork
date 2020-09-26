package ehnetwork.game.arcade.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.managers.GameHostManager;

public class KillButton implements IButton
{
	private ArcadeManager _arcade;
	private GameHostManager _manager;

	public KillButton(ArcadeManager arcadeManager)
	{
		_manager = arcadeManager.GetGameHostManager();
		_arcade = arcadeManager;
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
