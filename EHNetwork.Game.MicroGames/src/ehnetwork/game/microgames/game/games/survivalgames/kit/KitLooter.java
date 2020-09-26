package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkLooter;

public class KitLooter extends Kit
{
	public KitLooter(MicroGamesManager manager)
	{
		super(manager, "Looter", KitAvailability.Free, 

				new String[] 
						{
				"Defeat your opponents with your swag loots!"
						}, 

						new Perk[] 
								{
				new PerkLooter(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.CHEST));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
