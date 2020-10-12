package ehnetwork.game.arcade.game.games.sneakyassassins.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSmokebomb;

public class KitBriber extends SneakyAssassinKit
{
	public KitBriber(ArcadeManager manager, EntityType disguiseType)
	{
		super(manager, "Briber", KitAvailability.Achievement,
				new String[]
						{
				"Pay Villagers to attack other players!"
						}, 
						new Perk[]
								{
				new PerkSmokebomb(Material.INK_SACK, 3, true),
								}, 
								new ItemStack(Material.EMERALD),
				disguiseType);
		
		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.SNEAK_ASSASSINS_I_SEE_YOU,
				Achievement.SNEAK_ASSASSINS_INCOMPETENCE,
				Achievement.SNEAK_ASSASSINS_MASTER_ASSASSIN,
				Achievement.SNEAK_ASSASSINS_THE_MASTERS_MASTER,
				Achievement.SNEAKY_ASSASSINS_WINS,
				});
	}

	@Override
	public void GiveItems(Player player)
	{
		super.GiveItems(player);
 
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.EMERALD, (byte) 0, 4,
				C.cYellow + C.Bold + "Right-Click Villager" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bribe Villager",
				new String[]
						{
								ChatColor.RESET + "Pay a villager to help you.",
								ChatColor.RESET + "It will attack the nearest",
								ChatColor.RESET + "enemy for 15 seconds.",

						}));
	}
}
