package ehnetwork.game.microgames.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSpeed;

public class KitTracker extends Kit
{
	public KitTracker(MicroGamesManager manager)
	{
		super(manager, "Tracker", KitAvailability.Gem, 500,

				new String[]
						{
								"Use your compass to track other players!"
						},

				new Perk[]
						{
								new PerkSpeed(1)
						},
				EntityType.ZOMBIE,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.COMPASS, (byte)0, 1, "Player Tracker"));
		player.setHealth(10);
		player.setFoodLevel(14);
		return;
	}
}
