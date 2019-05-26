package nautilus.game.arcade.game.games.dragonescape.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitWarper extends Kit
{
	public KitWarper(ArcadeManager manager)
	{
		super(manager, "Warper", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Use your Enderpearl to instantly warp",
				"to the player in front of you!"
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Leap", 1, 1, 8000, 2)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.ENDER_PEARL));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ENDER_PEARL));
		player.setExp(0.99f);
	}
}
