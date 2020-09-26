package ehnetwork.game.arcade.game.games.sneakyassassins.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSmokebomb;

public class KitEscapeArtist extends SneakyAssassinKit
{
	public KitEscapeArtist(ArcadeManager manager, EntityType disguiseType)
	{
		super(manager, "Escape Artist", KitAvailability.Free,
				new String[]
						{
				"Carries two extra Smoke Bombs!"
						}, 
						new Perk[]
								{
				new PerkSmokebomb(Material.INK_SACK, 3, true)
								}, 
								new ItemStack(Material.INK_SACK),
				disguiseType);
	}

	@Override
	public void GiveItems(Player player)
	{
		super.GiveItems(player);

		ItemStack inkSack = player.getInventory().getItem(player.getInventory().first(Material.INK_SACK));
		inkSack.setAmount(inkSack.getAmount() + 2);
	}
}
