package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkWolfPet;

public class KitBeastmaster extends Kit
{
	public KitBeastmaster(MicroGamesManager manager)
	{
		super(manager, "Beastmaster", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Woof woof woof!!"
						}, 

						new Perk[] 
								{
				new PerkWolfPet(30, 1, false, true)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.BONE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
