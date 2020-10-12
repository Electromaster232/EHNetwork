package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkAxeThrower;
import ehnetwork.game.microgames.kit.perks.PerkAxeman;

public class KitAxeman extends Kit
{
	public KitAxeman(MicroGamesManager manager)
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
