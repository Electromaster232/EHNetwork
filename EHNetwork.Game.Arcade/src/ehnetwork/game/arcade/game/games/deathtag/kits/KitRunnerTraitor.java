package ehnetwork.game.arcade.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkKnockback;

public class KitRunnerTraitor extends AbstractKitRunner
{
	public KitRunnerTraitor(ArcadeManager manager)
	{
		super(manager, "Runner Traitor", KitAvailability.Gem, 5000,
				new String[] 
				{
					"You can deal knockback to other runners!"
				}, 
				new Perk[] 
				{
					new PerkKnockback(0.8),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.IRON_AXE));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
	}
}
