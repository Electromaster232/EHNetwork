package ehnetwork.game.microgames.game.games.draw.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitSlowAndSteady extends Kit
{
	public KitSlowAndSteady(MicroGamesManager manager)
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
