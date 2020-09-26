package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkMammoth;
import ehnetwork.game.microgames.kit.perks.PerkSeismicSlamHG;

public class KitBrawler extends Kit
{
	public KitBrawler(MicroGamesManager manager)
	{
		super(manager, "Brawler", KitAvailability.Gem, 

				new String[] 
						{
				"Giant and muscular, easily smacks others around."
						}, 

						new Perk[] 
								{
				new PerkMammoth(),
				
				new PerkSeismicSlamHG()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SWORD));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
