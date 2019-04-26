package nautilus.game.arcade.game.games.paintball.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import mineplex.core.common.util.C;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitRifle extends Kit
{
	public KitRifle(ArcadeManager manager)
	{
		super(manager, "Rifle", KitAvailability.Free, 

				new String[] 
						{ 
				"Semi-automatic paintball rifle.",
				C.cGold + "2 Hit Kill"
						}, 

						new Perk[] 
								{
					new PerkPaintballRifle(),
					new PerkSpeed(0)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_BARDING));
	} 

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_BARDING, (byte)0, 1, "Paintball Rifle"));
		
		//Potion
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
