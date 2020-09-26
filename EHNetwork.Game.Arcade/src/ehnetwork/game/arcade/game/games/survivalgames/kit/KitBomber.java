package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBomberHG;
import ehnetwork.game.arcade.kit.perks.PerkTNTArrow;

public class KitBomber extends Kit
{
	public KitBomber(ArcadeManager manager)
	{
		super(manager, "Bomber", KitAvailability.Gem, 5000,

				new String[] 
						{
				"BOOM! BOOM! BOOM!"
						}, 

						new Perk[] 
								{
				new PerkBomberHG(30, 2),
				new PerkTNTArrow()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.TNT));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
