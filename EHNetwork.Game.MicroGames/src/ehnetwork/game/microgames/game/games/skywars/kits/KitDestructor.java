package ehnetwork.game.microgames.game.games.skywars.kits;

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
		super(manager, "Destructor", KitAvailability.Achievement, 

				new String[] 
						{
				"Your Ender Pearls make the world crumble!"
						}, 

						new Perk[] 
								{
				new PerkDestructor(40, 2, 2500, true)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.ENDER_PEARL));

		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.SKYWARS_BOMBER,
				Achievement.SKYWARS_NOARMOR,
				Achievement.SKYWARS_NOCHEST,
				Achievement.SKYWARS_PLAYER_KILLS,
				Achievement.SKYWARS_TNT,
				Achievement.SKYWARS_WINS,
				Achievement.SKYWARS_ZOMBIE_KILLS
				});
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
		player.getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE));
	}
}
