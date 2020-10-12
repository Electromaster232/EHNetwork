package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkWolfPet;

public class KitBeastmaster extends Kit
{
	public KitBeastmaster(ArcadeManager manager)
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
