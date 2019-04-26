package mineplex.minecraft.game.classcombat.shop;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.Achievement;
import mineplex.core.achievement.AchievementManager;
import mineplex.core.common.Rank;
import mineplex.minecraft.game.classcombat.Class.ClassManager;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;
import mineplex.minecraft.game.classcombat.item.ItemFactory;

public class ClassShopManager extends MiniPlugin
{
	private ClassManager _classManager;
	private SkillFactory _skillFactory;
	private ItemFactory _itemFactory;
	private AchievementManager _achievementManager;
	private CoreClientManager _clientManager;
	
	public ClassShopManager(JavaPlugin plugin, ClassManager classManager, SkillFactory skillFactory, ItemFactory itemFactory, AchievementManager achievementManager, CoreClientManager clientManager)
	{
		super("Class Shop Manager", plugin);
		
		_classManager = classManager;
		_skillFactory = skillFactory;
		_itemFactory = itemFactory;
		_achievementManager = achievementManager;
		_clientManager = clientManager;
	}
	
	public ClassManager GetClassManager()
	{
		return _classManager;
	}

	public SkillFactory GetSkillFactory()
	{
		return _skillFactory;
	}

	public ItemFactory GetItemFactory()
	{
		return _itemFactory;
	}
	
	public boolean hasAchievements(Player player)
	{
		if (_clientManager.Get(player).GetRank().Has(Rank.HELPER))
			return true;
		
		return _achievementManager.hasCategory(player, new Achievement[] 
		{
				Achievement.CHAMPIONS_ACE,
				Achievement.CHAMPIONS_ASSASSINATION,
				Achievement.CHAMPIONS_EARTHQUAKE,
				Achievement.CHAMPIONS_FLAWLESS_VICTORY,
				Achievement.CHAMPIONS_MASS_ELECTROCUTION,
				Achievement.CHAMPIONS_THE_LONGEST_SHOT,
				Achievement.CHAMPIONS_WINS,
		});
	}
}
