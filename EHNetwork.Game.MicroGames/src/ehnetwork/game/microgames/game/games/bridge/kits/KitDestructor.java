package ehnetwork.game.microgames.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDestructor;

public class KitDestructor extends Kit
{
	public KitDestructor(MicroGamesManager manager)
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
