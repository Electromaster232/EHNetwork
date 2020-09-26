package ehnetwork.game.arcade.game.games.barbarians.kits;

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
import ehnetwork.game.arcade.kit.perks.PerkBodySlam;

public class KitBrute extends Kit 
{
	public KitBrute(ArcadeManager manager)
	{
		super(manager, "Barbarian Brute", KitAvailability.Free, 
				new String[] 
				{
					"A true barbarian, loves to kill people!"
				}, 
				new Perk[] 
				{
					new PerkBodySlam(8, 2)
				}, 
				EntityType.PLAYER,	
				new ItemStack(Material.IRON_AXE));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.IRON_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
	}
}
