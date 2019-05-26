package nautilus.game.arcade.game.games.spleef.kits;

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

public class KitBrawler extends Kit
{
	public KitBrawler(ArcadeManager manager)
	{
		super(manager, "Brawler", KitAvailability.Gem, 

				new String[] 
						{
				"Very leap. Such knockback. Wow."
						}, 

						new Perk[] 
								{
				new PerkLeap("Leap", 1.2, 1.2, 6000),
				new PerkSmasher(),
				new PerkKnockback(0.6)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.IRON_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
	}
}
