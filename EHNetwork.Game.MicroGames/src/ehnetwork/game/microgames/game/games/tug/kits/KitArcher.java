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
import ehnetwork.game.microgames.kit.perks.PerkBarrage;
import ehnetwork.game.microgames.kit.perks.PerkFletcher;

public class KitArcher extends Kit
{
	public KitArcher(MicroGamesManager manager)
	{
		super(manager, "Archer", KitAvailability.Free, 

				new String[] 
						{
				"Passively crafts arrows from surrounding terrain."
						}, 

						new Perk[] 
								{
				new PerkFletcher(20, 3, true),
				new PerkBarrage(5, 250, true, false),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.BOW));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
		player.getInventory().addItem(new ItemStack(Material.BOW));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
}
