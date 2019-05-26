package nautilus.game.arcade.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.achievement.Achievement;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitDestructor extends Kit
{
	public KitDestructor(ArcadeManager manager)
	{
		super(manager, "Destructor", KitAvailability.Achievement, 99999,

				new String[] 
						{
				"Has the ability to make the world crumble!"
						}, 

						new Perk[] 
								{
				new PerkDestructor(40, 2, 400, false)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.ENDER_PEARL));

		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.BRIDGES_DEATH_BOMBER,
				Achievement.BRIDGES_FOOD,
				Achievement.BRIDGES_FORTUNE_BOMBER,
				Achievement.BRIDGES_RAMPAGE,
				Achievement.BRIDGES_SNIPER,
				Achievement.BRIDGES_WINS 
				});
	}

	@Override
	public void GiveItems(Player player) 
	{

	}
	
	public void SetEnabled(boolean var)
	{
		for (Perk perk : this.GetPerks())
		{
			if (perk instanceof PerkDestructor)
			{
				((PerkDestructor)perk).setEnabled(var);
			}
		}
	}
}
