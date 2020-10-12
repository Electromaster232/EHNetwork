package ehnetwork.game.microgames.game.games.tug.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkMammoth;

public class KitSmasher extends Kit
{
	public KitSmasher(MicroGamesManager manager)
	{
		super(manager, "Smasher", KitAvailability.Free, 

				new String[] 
						{
				"Giant and muscular, easily smacks others around."
						}, 

						new Perk[] 
								{
				new PerkMammoth(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SWORD));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.IRON_BOOTS));
	}
}
