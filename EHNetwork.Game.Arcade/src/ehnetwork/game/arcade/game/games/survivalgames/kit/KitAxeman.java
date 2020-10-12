package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkAxeThrower;
import ehnetwork.game.arcade.kit.perks.PerkAxeman;

public class KitAxeman extends Kit
{
	public KitAxeman(ArcadeManager manager)
	{
		super(manager, "Axeman", KitAvailability.Free, 

				new String[] 
						{
				"Proficient in the art of axe combat!"
						}, 

						new Perk[] 
								{ 
				
				new PerkAxeman(),
				new PerkAxeThrower(manager)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
