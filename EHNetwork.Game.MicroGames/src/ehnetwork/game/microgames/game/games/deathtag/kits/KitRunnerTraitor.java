package ehnetwork.game.microgames.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkKnockback;

public class KitRunnerTraitor extends AbstractKitRunner
{
	public KitRunnerTraitor(MicroGamesManager manager)
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
