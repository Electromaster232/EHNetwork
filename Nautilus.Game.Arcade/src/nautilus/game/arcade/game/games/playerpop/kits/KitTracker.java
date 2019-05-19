package nautilus.game.arcade.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkRadar;
import nautilus.game.arcade.kit.perks.PerkSmokebomb;
import nautilus.game.arcade.kit.perks.PerkSpeed;

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
