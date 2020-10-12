package ehnetwork.game.microgames.game.games.cards.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitPlayer extends Kit
{
	public KitPlayer(MicroGamesManager manager)
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