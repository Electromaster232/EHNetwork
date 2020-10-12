package ehnetwork.game.microgames.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSmokebomb;

public class KitHider extends Kit
{
	public KitHider(MicroGamesManager manager)
	{
		super(manager, "Hider", KitAvailability.Gem, 1500,

				new String[]
						{
								"Sneak Sneak... Sneak around town!"
						},

				new Perk[]
						{
								new PerkSmokebomb(Material.INK_SACK, 15, true, "Right click with Ink Sac to go invisible for 5 seconds!")
						},
				EntityType.SQUID,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setItem(1, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)0, 2, "Invisibility Potion"));
		player.setHealth(10);
		player.setFoodLevel(14);
		return;
	}
}
