package ehnetwork.game.arcade.game.games.build.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitBuilder extends Kit
{
	public KitBuilder(ArcadeManager manager)
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