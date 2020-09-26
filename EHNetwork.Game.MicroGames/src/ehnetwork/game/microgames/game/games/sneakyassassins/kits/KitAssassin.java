package ehnetwork.game.microgames.game.games.sneakyassassins.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSmokebomb;

public class KitAssassin extends SneakyAssassinKit
{
	public KitAssassin(MicroGamesManager manager, EntityType disguiseType)
	{
		super(manager, "Ranged Assassin", KitAvailability.Gem,
				new String[]
						{
				"Skilled at ranged assassination!"
						}, 
						new Perk[]
								{
				new PerkSmokebomb(Material.INK_SACK, 3, true)
								}, 
								new ItemStack(Material.BOW),
				disguiseType);
	}

	@Override
	public void GiveItems(Player player)
	{
		super.GiveItems(player);

		player.getInventory().addItem(new ItemStack(Material.BOW));
		player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
	}
}
