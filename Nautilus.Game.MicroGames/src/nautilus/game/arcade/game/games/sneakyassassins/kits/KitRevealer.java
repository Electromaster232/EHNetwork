package nautilus.game.arcade.game.games.sneakyassassins.kits;

import mineplex.core.common.util.*;
import mineplex.core.itemstack.*;
import nautilus.game.arcade.*;
import nautilus.game.arcade.kit.*;
import nautilus.game.arcade.kit.perks.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class KitRevealer extends SneakyAssassinKit
{
	public KitRevealer(ArcadeManager manager, EntityType disguiseType)
	{
		super(manager, "Revealer", KitAvailability.Gem, 5000,
				new String[]
						{
				"Carries three Revealers which explode and reveal nearby assassins!"
						}, 
						new Perk[]
								{
				new PerkSmokebomb(Material.INK_SACK, 3, true),
				new PerkRevealer()
								}, 
								new ItemStack(Material.INK_SACK),
				disguiseType);
	}

	@Override
	public void GiveItems(Player player)
	{
		super.GiveItems(player);

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND, (byte) 0, 3,
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Reveal Assassins",
				new String[]
						{
								ChatColor.RESET + "Throw a revealer.",
								ChatColor.RESET + "Players within 5 blocks",
								ChatColor.RESET + "get revealed for 5 seconds.",

						}));
	}
}
