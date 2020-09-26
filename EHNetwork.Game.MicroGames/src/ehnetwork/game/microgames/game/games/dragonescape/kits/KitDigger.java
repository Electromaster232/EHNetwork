package ehnetwork.game.microgames.game.games.dragonescape.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitDigger extends Kit
{
	public KitDigger(MicroGamesManager manager)
	{
		super(manager, "Digger", KitAvailability.Achievement, 5000,

				new String[] 
						{
				"Dig yourself a shortcut, and use the",
				"blocks to create another shortcut!",
				"",
				"Does not have any Leaps.",
						}, 

						new Perk[] 
								{ 
				//new PerkLeap("Leap", 1, 1, 8000, 1)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.DIAMOND_PICKAXE));
		
		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.DRAGON_ESCAPE_PARALYMPICS,
				Achievement.DRAGON_ESCAPE_PIRATE_BAY,
				Achievement.DRAGON_ESCAPE_SKYLANDS,
				Achievement.DRAGON_ESCAPE_THROUGH_HELL,
				Achievement.DRAGON_ESCAPE_WINS,
				});
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_PICKAXE, 6));
		player.setExp(0.99f);
	}
}
