package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSkeletons;

public class KitNecromancer extends Kit
{
	public KitNecromancer(MicroGamesManager manager)
	{
		super(manager, "Necromancer", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Cool undead guy and stuff"
						}, 

						new Perk[] 
								{
					new PerkSkeletons(true)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.SKULL_ITEM));

	}

	@Override
	public void GiveItems(Player player)
	{
		
	}
}
