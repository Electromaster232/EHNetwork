package mineplex.core.achievement.ui.page;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.Achievement;
import mineplex.core.achievement.AchievementCategory;
import mineplex.core.achievement.AchievementData;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.achievement.ui.AchievementShop;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.stats.StatsManager;

public class AchievementPage extends ShopPageBase<AchievementManager, AchievementShop>
{
	private static int ACHIEVEMENT_MIDDLE_INDEX = 31;

	private AchievementCategory _category;
	private StatsManager _statsManager;
	private Player _target;

	public AchievementPage(AchievementManager plugin, StatsManager statsManager, AchievementCategory category, AchievementShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player, Player target)
	{
		super(plugin, shop, clientManager, donationManager, category.getFriendlyName(), player);

		_statsManager = statsManager;
		_category = category;
		_target = target;

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		int currentIndex = ACHIEVEMENT_MIDDLE_INDEX - (getAchievements().size() / 2);
		boolean hasAllAchievements = true;
		int achievementCount = 0;

		ArrayList<String> masterAchievementLore = new ArrayList<String>();
		masterAchievementLore.add(" ");

		List<Achievement> achievements = getAchievements();
		for (Achievement achievement : achievements)
		{
			AchievementData data = getPlugin().get(_target, achievement);
			boolean singleLevel = achievement.isSingleLevel();
			boolean hasUnlocked = data.getLevel() >= achievement.getMaxLevel();

			if (!hasUnlocked)
			{
				hasAllAchievements = false;
			}

			{
				Material material = hasUnlocked ? Material.EXP_BOTTLE : Material.GLASS_BOTTLE;
				String itemName = (hasUnlocked ? C.cGreen : C.cRed) + achievement.getName();

				if (!singleLevel)
					itemName += ChatColor.WHITE + " Level " + data.getLevel() + "/" + achievement.getMaxLevel();

				ArrayList<String> lore = new ArrayList<String>();
				lore.add(" ");
				for (String descLine : achievement.getDesc())
				{
					lore.add(ChatColor.RESET + descLine);
				}
				

				if (!hasUnlocked && achievement.isOngoing())
				{
					lore.add(" ");
					lore.add(C.cYellow + (singleLevel ? "Progress: " : "Next Level: ") + C.cWhite + data.getExpRemainder() + "/" + data.getExpNextLevel());
				}
				
				if (!hasUnlocked && singleLevel)
				{
					lore.add(" ");
					lore.add(C.cYellow + "Reward: " + C.cGreen + achievement.getGemReward() + " Gems");
				}
				
				if (hasUnlocked && data.getLevel() == achievement.getMaxLevel())
				{
					lore.add(" ");
					lore.add(C.cAqua + "Complete!");
				}
					

				addItem(currentIndex, new ShopItem(material, (byte) (hasUnlocked ? 0 : 0), itemName, lore.toArray(new String[0]), 1, false, false));
			}

			masterAchievementLore.add((hasUnlocked ? C.cGreen : C.cRed) + achievement.getName());

			currentIndex++;
			achievementCount++;
		}

		// Master Achievement
		if (!_category.getFriendlyName().startsWith("Global") && achievementCount > 0)
		{
			String itemName = ChatColor.RESET + _category.getFriendlyName() + " Master Achievement";
			masterAchievementLore.add(" ");
			if (getPlayer().equals(_target))
			{
				if (_category.getReward() != null)
					masterAchievementLore.add(C.cYellow + C.Bold + "Reward: " + ChatColor.RESET + _category.getReward());
				else
					masterAchievementLore.add(C.cYellow + C.Bold + "Reward: " + ChatColor.RESET + "Coming Soon...");
			}
			
			addItem(40, new ShopItem(hasAllAchievements ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, (byte) 0, itemName, masterAchievementLore.toArray(new String[0]), 1, false, true));
		}

		addBackButton();
		addStats();
	}

	private void addBackButton()
	{
		addButton(4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			public void onClick(Player player, ClickType clickType)
			{
				AchievementMainPage page;
				if (_category.getGameCategory() == AchievementCategory.GameCategory.ARCADE)
					page = new ArcadeMainPage(getPlugin(), _statsManager, getShop(), getClientManager(), getDonationManager(), "Arcade Games", player, _target);
				else
					page = new AchievementMainPage(getPlugin(), _statsManager, getShop(), getClientManager(), getDonationManager(), _target.getName() + "'s Stats", player, _target);
				;

				getShop().openPageForPlayer(getPlayer(), page);
				player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
			}
		});
	}

	private void addStats()
	{
		// Don't show if this category has no stats to display
		if (_category.getStatsToDisplay().length == 0)
			return;

		Material material = Material.BOOK;
		String itemName = C.Bold + _category.getFriendlyName() + " Stats";
		List<String> lore = new ArrayList<String>();
		lore.add(" ");
		_category.addStats(getClientManager(), _statsManager, lore, getPlayer(), _target);

		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + itemName);
		meta.setLore(lore);
		item.setItemMeta(meta);

		setItem(22, item);
	}

	public List<Achievement> getAchievements()
	{
		List<Achievement> achievements = new ArrayList<Achievement>();

		for (Achievement achievement : Achievement.values())
		{
			if (achievement.getCategory() == _category)
				achievements.add(achievement);
		}

		return achievements;
	}
}
