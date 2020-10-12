package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;

/**
 * Created by WilliamTiger.
 * All the code and any API's associated with it
 * are not to be used anywhere else without written
 * consent of William Burns. 2015.
 * 08/07/2015
 */
public class ChooseMapButton implements IButton
{
	private MicroGamesManager _microGamesManager;
	private PrivateServerShop _privateServerShop;
	private GameType _gameType;
	private String _map;

	public ChooseMapButton(MicroGamesManager microGamesManager, PrivateServerShop privateServerShop, GameType gameType, String map)
	{
		_microGamesManager = microGamesManager;
		_privateServerShop = privateServerShop;
		_gameType = gameType;
		_map = map;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_microGamesManager.GetGameCreationManager().MapPref = _map;
		_microGamesManager.GetGame().setGame(_gameType, player, true);
		player.closeInventory();
		return;
	}
}
