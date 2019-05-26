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

public class KitSnowballer extends Kit
{
	public KitSnowballer(ArcadeManager manager)
	{
		super(manager, "Snowballer", KitAvailability.Free, 

				new String[] 
						{
				"Throw snowballs to break blocks!",
				"Receives 1 Snowball when you punch blocks!"
						}, 

						new Perk[] 
								{
				new PerkKnockback(0.3)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.SNOW_BALL));

	}
	
	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.SNOW_BALL));
	}
}
