package ehnetwork.game.arcade.game.games.draw.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

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
