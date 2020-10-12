package ehnetwork.game.microgames.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkFletcher;
import ehnetwork.game.microgames.kit.perks.PerkKnockbackArrow;

public class KitRunnerArcher extends AbstractKitRunner
{
	public KitRunnerArcher(MicroGamesManager manager)
	{
		super(manager, "Runner Archer", KitAvailability.Gem, 
				new String[] 
				{
					"Fight off the Chasers with Arrows!"
				}, 
				new Perk[] 
				{
					new PerkKnockbackArrow(3),
					new PerkFletcher(2, 2, true),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.BOW));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
	}
}
