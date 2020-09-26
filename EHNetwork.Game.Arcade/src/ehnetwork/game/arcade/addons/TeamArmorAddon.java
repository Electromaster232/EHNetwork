package ehnetwork.game.arcade.addons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.events.PlayerKitGiveEvent;

public class TeamArmorAddon extends MiniPlugin
{
	public ArcadeManager Manager;
	
	public TeamArmorAddon(JavaPlugin plugin, ArcadeManager manager)
	{
		super("Team Armor Addon", plugin);
		
		Manager = manager;
	}

	@EventHandler
	public void GiveArmor(PlayerKitGiveEvent event)
	{		
		Player player = event.GetPlayer();
		
		if (event.GetGame().TeamArmor)
		{
			ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta metaHelm = (LeatherArmorMeta)helm.getItemMeta();
			metaHelm.setColor(Manager.GetGame().GetTeam(player).GetColorBase());
			helm.setItemMeta(metaHelm);
			player.getInventory().setHelmet(helm);
			
			ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
			meta.setColor(Manager.GetGame().GetTeam(player).GetColorBase());
			armor.setItemMeta(meta);
			player.getInventory().setChestplate(armor);
			
			ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
			LeatherArmorMeta metaLegs = (LeatherArmorMeta)legs.getItemMeta();
			metaLegs.setColor(Manager.GetGame().GetTeam(player).GetColorBase());
			legs.setItemMeta(metaLegs);
			player.getInventory().setLeggings(legs);
			
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			LeatherArmorMeta metaBoots = (LeatherArmorMeta)boots.getItemMeta();
			metaBoots.setColor(Manager.GetGame().GetTeam(player).GetColorBase());
			boots.setItemMeta(metaBoots);
			player.getInventory().setBoots(boots);
		}
		
		if (event.GetGame().TeamArmorHotbar && event.GetGame().InProgress())
		{
			ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
			meta.setColor(Manager.GetGame().GetTeam(player).GetColorBase());
			meta.setDisplayName(Manager.GetGame().GetTeam(player).GetFormattedName());
			armor.setItemMeta(meta);
			player.getInventory().setItem(8, armor.clone());
		}
	}
	
	@EventHandler
	public void EquipCancel(PlayerInteractEvent event)
	{
		if (Manager.GetGame() == null)
			return;
		
		if (!Manager.GetGame().TeamArmorHotbar)
			return;
		
		if (UtilGear.isMat(event.getPlayer().getItemInHand(), Material.LEATHER_CHESTPLATE))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void ClickCancel(InventoryClickEvent event)
	{
		if (Manager.GetGame() == null)
			return;
		
		if (!Manager.GetGame().TeamArmorHotbar)
			return;
		
		if (!Manager.GetGame().InProgress())
			return;
		
		event.setCancelled(true);
		event.getWhoClicked().closeInventory();
	}
}
