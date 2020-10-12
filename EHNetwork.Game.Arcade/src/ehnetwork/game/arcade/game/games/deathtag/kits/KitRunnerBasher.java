package ehnetwork.game.arcade.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkCripple;

public class KitRunnerBasher extends AbstractKitRunner
{
	public KitRunnerBasher(ArcadeManager manager)
	{
		super(manager, "Runner Basher", KitAvailability.Free, 
				new String[] 
				{
					"Your attacks cripple Chasers breifly!"
				}, 
				new Perk[] 
				{
					new PerkCripple(3, 2),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.IRON_SWORD));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
	}
}
