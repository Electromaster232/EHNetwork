package ehnetwork.game.microgames.game.games.sneakyassassins.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkRevealer;
import ehnetwork.game.microgames.kit.perks.PerkSmokebomb;

public class KitRevealer extends SneakyAssassinKit
{
	public KitRevealer(MicroGamesManager manager, EntityType disguiseType)
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
