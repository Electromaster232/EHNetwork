package ehnetwork.game.microgames.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkBomber;

public class KitBomber extends Kit
{
	public KitBomber(MicroGamesManager manager)
	{
		super(manager, "Bomber", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Crazy bomb throwing guy."
						}, 

						new Perk[] 
								{
				new PerkBomber(30, 2, -1)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.TNT));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
