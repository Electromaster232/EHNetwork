package ehnetwork.game.microgames.game.games.colorswap.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkKnockback;

public class KitPlayer extends Kit
{
	public KitPlayer(MicroGamesManager manager)
	{
		super(manager, "Player", KitAvailability.Free, 0,

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
										new PerkKnockback(1),
								}, 
								EntityType.PIG,
								new ItemStack(Material.PORK));

	}
	
	@Override
	public void GiveItems(Player player) 
	{

	}
}
