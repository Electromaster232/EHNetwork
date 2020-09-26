package ehnetwork.game.microgames.gui.privateServer.page;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;
import ehnetwork.game.microgames.gui.privateServer.button.ChooseMapButton;

/**
 * Created by WilliamTiger.
 * All the code and any API's associated with it
 * are not to be used anywhere else without written
 * consent of William Burns. 2015.
 * 08/07/2015
 */
public class ChooseMapPage extends BasePage
{
	private GameType _gameType;

	public ChooseMapPage(MicroGamesManager plugin, PrivateServerShop shop, Player player, GameType gameType)
	{
		super(plugin, shop, "Choose Map", player);
		_gameType = gameType;

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		addBackToSetGamePage();

		int slot = 9;
		for (String cur : getPlugin().LoadFiles(_gameType.GetName())){
			ChooseMapButton btn = new ChooseMapButton(getPlugin(), getShop(), _gameType, cur);
			addButton(slot, new ShopItem(Material.PAPER, cur.split("_")[1], new String[]{"§7Click to select map."}, 1, false), btn);
			slot++;
		}
	}
}
