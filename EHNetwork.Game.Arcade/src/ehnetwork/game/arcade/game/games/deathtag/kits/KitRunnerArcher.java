package ehnetwork.game.arcade.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;
import ehnetwork.game.arcade.kit.perks.PerkKnockbackArrow;

public class KitRunnerArcher extends AbstractKitRunner
{
	public KitRunnerArcher(ArcadeManager manager)
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
