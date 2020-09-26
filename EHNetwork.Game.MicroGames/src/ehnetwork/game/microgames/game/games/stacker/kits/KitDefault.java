package ehnetwork.game.microgames.game.games.stacker.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitDefault extends Kit
{
	public KitDefault(MicroGamesManager manager)
	{
		super(manager, "Default", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{ 
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.WOOD_BUTTON));

	}
	
	@Override
	public void GiveItems(Player player)
	{

	}
}
