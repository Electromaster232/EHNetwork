package ehnetwork.game.microgames.game.games.build.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitBuilder extends Kit
{
	public KitBuilder(MicroGamesManager manager)
	{
		super(manager, "Builder", KitAvailability.Free, 

				new String[] 
						{
				"Can I build it!? Yes I can!"
						}, 

						new Perk[] 
								{ 
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.WOOD));

	}
	
	@Override
	public void GiveItems(Player player)
	{

	}
}