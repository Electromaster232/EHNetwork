package ehnetwork.game.microgames.game.games.spleef.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkKnockback;
import ehnetwork.game.microgames.kit.perks.PerkLeap;
import ehnetwork.game.microgames.kit.perks.PerkSmasher;

public class KitBrawler extends Kit
{
	public KitBrawler(MicroGamesManager manager)
	{
		super(manager, "Brawler", KitAvailability.Gem, 

				new String[] 
						{
				"Very leap. Such knockback. Wow."
						}, 

						new Perk[] 
								{
				new PerkLeap("Leap", 1.2, 1.2, 6000),
				new PerkSmasher(),
				new PerkKnockback(0.6)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.IRON_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
	}
}
