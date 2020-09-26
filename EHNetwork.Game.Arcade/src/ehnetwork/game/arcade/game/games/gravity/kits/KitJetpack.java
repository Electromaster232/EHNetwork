package ehnetwork.game.arcade.game.games.gravity.kits;

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

public class KitJetpack extends Kit
{
	public KitJetpack(ArcadeManager manager)
	{
		super(manager, "Astronaut", KitAvailability.Free, 

				new String[] 
						{
				"SPAAAAAAAAAAAAAACE"
						}, 

						new Perk[] 
								{ 
				
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_AXE));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		//player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.SHEARS, (byte)0, 1, "Block Cannon"));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, "Space Suit"));
		
		//player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, "Space Shooter"));	
		//player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ARROW, (byte)0, 64, "Space Arrows"));	
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.GLASS));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.GOLD_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.GOLD_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.GOLD_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.GLASS));
		ent.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
	}
}
