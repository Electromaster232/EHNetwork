package ehnetwork.game.arcade.game.games.build.gui;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.build.BuildData;
import ehnetwork.game.arcade.game.games.build.gui.page.MobPage;

public class MobShop extends ShopBase<ArcadeManager>
{
	public MobShop(ArcadeManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Mob Options");
	}

	protected ShopPageBase<ArcadeManager, ? extends ShopBase<ArcadeManager>> buildPagesFor(Player player, BuildData data, Entity entity)
	{
		return new MobPage(getPlugin(), this, getClientManager(), getDonationManager(), player, data, entity);
	}

	public boolean attemptShopOpen(Player player, BuildData data, Entity entity)
	{
		if (!getOpenedShop().contains(player.getName()))
		{
			if (!canOpenShop(player))
				return false;

			getOpenedShop().add(player.getName());

			openShopForPlayer(player);
			if (!getPlayerPageMap().containsKey(player.getName()))
			{
				getPlayerPageMap().put(player.getName(), buildPagesFor(player, data, entity));
			}

			openPageForPlayer(player, getOpeningPageForPlayer(player));

			return true;
		}

		return false;
	}

	@Override
	protected ShopPageBase<ArcadeManager, ? extends ShopBase<ArcadeManager>> buildPagesFor(Player player)
	{
		return null;
	}
}
