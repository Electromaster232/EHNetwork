package ehnetwork.game.microgames.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkApple;

public class KitApple extends Kit
{
	public KitApple(MicroGamesManager manager)
	{
		super(manager, "Apple", KitAvailability.Free, 

				new String[] 
						{
				"Possess the rare skill of finding apples frequently!"
						}, 

						new Perk[] 
								{
				new PerkApple(manager)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.APPLE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
