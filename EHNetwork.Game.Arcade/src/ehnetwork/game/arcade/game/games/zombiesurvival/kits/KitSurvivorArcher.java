package ehnetwork.game.arcade.game.games.zombiesurvival.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBarrage;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;

public class KitSurvivorArcher extends Kit
{
	public KitSurvivorArcher(ArcadeManager manager)
	{
		super(manager, "Survivor Archer", KitAvailability.Gem,

				new String[] 
						{
				"Survive with the help of your trusty bow!"
						}, 

						new Perk[] 
								{ 
				new PerkFletcher(2, 8, true),
				new PerkBarrage(5, 250, true, false)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.BOW));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
	}
}
