package ehnetwork.game.arcade.game.games.build.gui;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.build.Build;
import ehnetwork.game.arcade.game.games.build.gui.page.OptionsPage;

public class OptionsShop extends ShopBase<ArcadeManager>
{
	private Build _game;

	public OptionsShop(Build game, ArcadeManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Options");
		_game = game;
	}

	@Override
	protected ShopPageBase<ArcadeManager, ? extends ShopBase<ArcadeManager>> buildPagesFor(Player player)
	{
		return new OptionsPage(_game, getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
}
