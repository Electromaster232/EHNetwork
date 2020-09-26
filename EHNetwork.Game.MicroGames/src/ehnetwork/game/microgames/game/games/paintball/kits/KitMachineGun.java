package ehnetwork.game.microgames.game.games.paintball.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkPaintballMachineGun;

public class KitMachineGun extends Kit
{
	public KitMachineGun(MicroGamesManager manager)
	{
		super(manager, "Machine Gun", KitAvailability.Gem, 5000,

				new String[] 
						{ 
				"Full-automatic paintball gun.",
				C.cGold + "4 Hit Kill"
						}, 

						new Perk[] 
								{
					new PerkPaintballMachineGun()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.DIAMOND_BARDING));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_BARDING, (byte)0, 1, "Paintball Machine Gun"));

		ItemStack potion = new ItemStack(Material.POTION, 3, (short)16429); // 16422
		PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
		potionMeta.setDisplayName("Water Bomb");
		potion.setItemMeta(potionMeta);
		player.getInventory().addItem(potion);
	
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta metaHelm = (LeatherArmorMeta)helm.getItemMeta();
		metaHelm.setColor(Color.WHITE);
		helm.setItemMeta(metaHelm);
		player.getInventory().setHelmet(helm);
		
		ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
		meta.setColor(Color.WHITE);
		armor.setItemMeta(meta);
		player.getInventory().setChestplate(armor);
		
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta metaLegs = (LeatherArmorMeta)armor.getItemMeta();
		metaLegs.setColor(Color.WHITE);
		legs.setItemMeta(metaLegs);
		player.getInventory().setLeggings(legs);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta metaBoots = (LeatherArmorMeta)armor.getItemMeta();
		metaBoots.setColor(Color.WHITE);
		boots.setItemMeta(metaBoots);
		player.getInventory().setBoots(boots);
	}
}
