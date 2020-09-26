package ehnetwork.core.achievement.ui.page;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.achievement.AchievementCategory;
import ehnetwork.core.achievement.AchievementData;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.achievement.ui.AchievementShop;
import ehnetwork.core.achievement.ui.button.ArcadeButton;
import ehnetwork.core.achievement.ui.button.CategoryButton;
import ehnetwork.core.common.util.C;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.itemstack.ItemLayout;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.core.stats.StatsManager;

public class AchievementMainPage extends ShopPageBase<AchievementManager, AchievementShop>
{
	protected Player _target;
	protected StatsManager _statsManager;

	public AchievementMainPage(AchievementManager plugin, StatsManager statsManager, AchievementShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player, Player target)
	{
		super(plugin, shop, clientManager, donationManager, name, player, 9 * 4);

		_target = target;
		_statsManager = statsManager;

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		ArrayList<Integer> pageLayout = new ItemLayout(
		"XXXXOXXXX",
		"OXOXOXOXO",
		"OXOXOXOXO",
		"XXOXOXOXX").getItemSlots();
		int listSlot = 0;
 
		for (AchievementCategory category : AchievementCategory.values())
		{
			if (category.getGameCategory() == AchievementCategory.GameCategory.ARCADE)
				continue;

			CategoryButton button = new CategoryButton(getShop(), getPlugin(), _statsManager, category, getDonationManager(),
					getClientManager(), _target);

			ArrayList<String> lore = new ArrayList<String>();
			lore.add(" ");
			category.addStats(getClientManager(), _statsManager, lore, category == AchievementCategory.GLOBAL ? 5 : 2,
					getPlayer(), _target);
			lore.add(" ");
			addAchievements(category, lore, 9);
			lore.add(" ");
			lore.add(ChatColor.RESET + "Click for more details!");

			ShopItem shopItem = new ShopItem(category.getIcon(), category.getIconData(), C.Bold + category.getFriendlyName(),
					lore.toArray(new String[0]), 1, false, false);
			addButton(pageLayout.get(listSlot++), shopItem, button);
		}

		addArcadeButton(pageLayout.get(listSlot++));
	}

	protected void addArcadeButton(int slot)
	{
		ArcadeButton button = new ArcadeButton(getShop(), getPlugin(), _statsManager, getDonationManager(), getClientManager(), _target);
		ShopItem shopItem = new ShopItem(Material.BOW, (byte) 0, C.Bold + "Arcade Games", new String[] {" ", ChatColor.RESET + "Click for more!"}, 1, false, false);

		addButton(slot, shopItem, button);
	}

	protected void addAchievements(AchievementCategory category, List<String> lore, int max)
	{
		int achievementCount = 0;
		for (int i = 0; i < Achievement.values().length && achievementCount < max; i++)
		{
			Achievement achievement = Achievement.values()[i];
			if (achievement.getCategory() == category)
			{
				// Don't display achievements that have multiple levels
				if (achievement.getMaxLevel() > 1)
					continue;

				AchievementData data = getPlugin().get(_target, achievement);
				boolean finished = data.getLevel() >= achievement.getMaxLevel();

				lore.add((finished ? C.cGreen : C.cRed) + achievement.getName());

				achievementCount++;
			}
		}
	}
}
