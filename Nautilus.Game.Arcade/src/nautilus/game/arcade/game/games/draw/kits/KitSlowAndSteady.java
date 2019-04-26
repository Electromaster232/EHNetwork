package nautilus.game.arcade.game.games.draw.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

public class KitSlowAndSteady extends Kit
{
	public KitSlowAndSteady(ArcadeManager manager)
	{
		super(manager, "Extra Time", KitAvailability.Free, 

				new String[] 
						{
				"Receive +10 Seconds to draw!"
						}, 

						new Perk[] 
								{ 
				
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.WATCH));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
