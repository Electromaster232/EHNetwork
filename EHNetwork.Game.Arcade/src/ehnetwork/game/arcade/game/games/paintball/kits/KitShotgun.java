package ehnetwork.game.arcade.game.games.paintball.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkPaintballShotgun;
import ehnetwork.game.arcade.kit.perks.PerkSpeed;

public class KitShotgun extends Kit
{
	public KitShotgun(ArcadeManager manager)
	{
		super(manager, "Shotgun", KitAvailability.Gem, 

				new String[] 
						{ 
				"Pump action paintball shotgun.",
				C.cGold + "8 Pellets, 4 Pellet Hits Kill"
						}, 

						new Perk[] 
								{
					new PerkPaintballShotgun(),
					new PerkSpeed(1)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.GOLD_BARDING));
	} 

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.GOLD_BARDING, (byte)0, 1, "Paintball Shotgun"));

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
