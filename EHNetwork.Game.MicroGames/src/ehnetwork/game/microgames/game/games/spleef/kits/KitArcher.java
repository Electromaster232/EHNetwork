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
import ehnetwork.game.microgames.kit.perks.PerkFletcher;
import ehnetwork.game.microgames.kit.perks.PerkKnockback;

public class KitArcher extends Kit
{
	public KitArcher(MicroGamesManager manager)
	{
		super(manager, "Archer", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Arrows will damage spleef blocks in a small radius."
						}, 

						new Perk[] 
								{
				new PerkFletcher(2, 2, false),
				new PerkKnockback(0.3)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.BOW));

	}
	
	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
	}
}
