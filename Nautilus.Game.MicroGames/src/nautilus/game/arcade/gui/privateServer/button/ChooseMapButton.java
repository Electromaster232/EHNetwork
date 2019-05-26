package nautilus.game.arcade.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.shop.item.IButton;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.gui.privateServer.PrivateServerShop;

/**
 * Created by WilliamTiger.
 * All the code and any API's associated with it
 * are not to be used anywhere else without written
 * consent of William Burns. 2015.
 * 08/07/2015
 */
public class ChooseMapButton implements IButton
{
	private ArcadeManager _arcadeManager;
	private PrivateServerShop _privateServerShop;
	private GameType _gameType;
	private String _map;

	public ChooseMapButton(ArcadeManager arcadeManager, PrivateServerShop privateServerShop, GameType gameType, String map)
	{
		_arcadeManager = arcadeManager;
		_privateServerShop = privateServerShop;
		_gameType = gameType;
		_map = map;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_arcadeManager.GetGameCreationManager().MapPref = _map;
		_arcadeManager.GetGame().setGame(_gameType, player, true);
		player.closeInventory();
		return;
	}
}
