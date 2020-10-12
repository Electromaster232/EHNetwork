package ehnetwork.core.achievement.ui.button;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.AchievementCategory;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.achievement.ui.AchievementShop;
import ehnetwork.core.achievement.ui.page.AchievementPage;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.stats.StatsManager;

/**
 * Created by Shaun on 8/22/2014.
 */
public class CategoryButton implements IButton
{
	private AchievementShop _shop;
	private AchievementCategory _category;
	private AchievementManager _achievementManager;
	private StatsManager _statsManager;
	private DonationManager _donationManager;
	private CoreClientManager _clientManager;
	private Player _target;

	public CategoryButton(AchievementShop shop, AchievementManager achievementManager, StatsManager statsManager, AchievementCategory category, DonationManager donationManager, CoreClientManager clientManager, Player target)
	{
		_category = category;
		_shop = shop;
		_achievementManager = achievementManager;
		_statsManager = statsManager;
		_donationManager = donationManager;
		_clientManager = clientManager;
		_target = target;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_shop.openPageForPlayer(player, new AchievementPage(_achievementManager, _statsManager, _category, _shop, _clientManager, _donationManager, player, _target));
		player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
	}

}
