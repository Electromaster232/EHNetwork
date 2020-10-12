package ehnetwork.game.microgames.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkCripple;

public class KitRunnerBasher extends AbstractKitRunner
{
	public KitRunnerBasher(MicroGamesManager manager)
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
