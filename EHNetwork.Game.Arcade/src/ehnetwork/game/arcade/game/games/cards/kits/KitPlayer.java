package ehnetwork.game.arcade.game.games.cards.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitPlayer extends Kit
{
	public KitPlayer(ArcadeManager manager)
	{
		super(manager, "Player", KitAvailability.Free, 

				new String[] 
						{
				";dsgoasdyay"
						}, 

						new Perk[] 
								{ 
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.MAP));

	}
	
	@Override
	public void GiveItems(Player player)
	{

	}
}