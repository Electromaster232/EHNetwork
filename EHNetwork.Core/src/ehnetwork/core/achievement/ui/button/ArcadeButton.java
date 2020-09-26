package ehnetwork.core.achievement.ui.button;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.achievement.ui.AchievementShop;
import ehnetwork.core.achievement.ui.page.ArcadeMainPage;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.stats.StatsManager;

public class ArcadeButton implements IButton
{
	private AchievementShop _shop;
	private AchievementManager _achievementManager;
	private StatsManager _statsManager;
	private DonationManager _donationManager;
	private CoreClientManager _clientManager;
	private Player _target;

	public ArcadeButton(AchievementShop shop, AchievementManager achievementManager, StatsManager statsManager, DonationManager donationManager, CoreClientManager clientManager, Player target)
	{
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
		_shop.openPageForPlayer(player, new ArcadeMainPage(_achievementManager, _statsManager, _shop, _clientManager, _donationManager, "Arcade Games", player, _target));
		player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
	}

}
