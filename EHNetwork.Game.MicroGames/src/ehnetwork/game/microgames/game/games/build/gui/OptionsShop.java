package ehnetwork.game.microgames.game.games.build.gui;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.build.Build;
import ehnetwork.game.microgames.game.games.build.gui.page.OptionsPage;

public class OptionsShop extends ShopBase<MicroGamesManager>
{
	private Build _game;

	public OptionsShop(Build game, MicroGamesManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Options");
		_game = game;
	}

	@Override
	protected ShopPageBase<MicroGamesManager, ? extends ShopBase<MicroGamesManager>> buildPagesFor(Player player)
	{
		return new OptionsPage(_game, getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
}
