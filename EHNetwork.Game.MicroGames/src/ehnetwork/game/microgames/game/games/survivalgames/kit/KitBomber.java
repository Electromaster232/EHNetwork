package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkBomberHG;
import ehnetwork.game.microgames.kit.perks.PerkTNTArrow;

public class KitBomber extends Kit
{
	public KitBomber(MicroGamesManager manager)
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
