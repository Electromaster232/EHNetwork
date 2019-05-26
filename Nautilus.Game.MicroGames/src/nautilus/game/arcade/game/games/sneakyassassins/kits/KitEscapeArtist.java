package nautilus.game.arcade.game.games.sneakyassassins.kits;

import nautilus.game.arcade.*;
import nautilus.game.arcade.kit.*;
import nautilus.game.arcade.kit.perks.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

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
