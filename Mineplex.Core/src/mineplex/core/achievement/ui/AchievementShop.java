package mineplex.core.achievement.ui;

import org.bukkit.entity.Player;

import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.achievement.ui.page.AchievementMainPage;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.stats.StatsManager;

/**
 * Created by Shaun on 8/21/2014.
 */
public class AchievementShop extends ShopBase<AchievementManager>
{
	private StatsManager _statsManager;

	public AchievementShop(AchievementManager plugin, StatsManager statsManager, CoreClientManager clientManager, DonationManager donationManager, String name)
	{
		super(plugin, clientManager, donationManager, name);
		_statsManager = statsManager;
	}

	@Override
	protected ShopPageBase<AchievementManager, ? extends ShopBase<AchievementManager>> buildPagesFor(Player player)
	{
		return BuildPagesFor(player, player);
	}

	protected ShopPageBase<AchievementManager, ? extends ShopBase<AchievementManager>> BuildPagesFor(Player player, Player target)
	{
		return new AchievementMainPage(getPlugin(), _statsManager, this, getClientManager(), getDonationManager(), target.getName() + "'s Stats", player, target);
	}

	public boolean attemptShopOpen(Player player, Player target)
	{
		if (!getOpenedShop().contains(player.getName()))
		{
			if (!canOpenShop(player))
				return false;

			getOpenedShop().add(player.getName());

			openShopForPlayer(player);
			if (!getPlayerPageMap().containsKey(player.getName()))
			{
				getPlayerPageMap().put(player.getName(), BuildPagesFor(player, target));
			}

			openPageForPlayer(player, getOpeningPageForPlayer(player));

			return true;
		}

		return false;
	}
}
