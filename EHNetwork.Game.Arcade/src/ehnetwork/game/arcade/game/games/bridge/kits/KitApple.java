package ehnetwork.game.arcade.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkApple;

public class KitApple extends Kit
{
	public KitApple(ArcadeManager manager)
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
