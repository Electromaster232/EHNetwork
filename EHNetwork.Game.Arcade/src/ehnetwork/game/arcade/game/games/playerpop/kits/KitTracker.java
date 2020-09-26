package ehnetwork.game.arcade.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSpeed;

public class KitTracker extends Kit
{
	public KitTracker(ArcadeManager manager)
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
