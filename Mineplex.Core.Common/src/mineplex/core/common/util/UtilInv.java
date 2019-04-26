package mineplex.core.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class UtilInv
{
	private static Field _enchantmentNew;
	private static DullEnchantment _enchantment;
	
	static 
	{
		try
		{
			_enchantmentNew = Enchantment.class.getDeclaredField("acceptingNew");
			_enchantmentNew.setAccessible(true);
			_enchantmentNew.set(null, true);
			
			_enchantment = new DullEnchantment();
			EnchantmentWrapper.registerEnchantment(_enchantment);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void addDullEnchantment(ItemStack itemStack)
	{
		itemStack.addEnchantment(_enchantment, 1);
	}
	
	public static void removeDullEnchantment(ItemStack itemStack)
	{
	    itemStack.removeEnchantment(_enchantment);
	}
	
	public static DullEnchantment getDullEnchantment()
	{
	    return _enchantment;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean insert(Player player, ItemStack stack)
	{
		//CHECK IF FIT
		
		//Insert
		player.getInventory().addItem(stack);
		player.updateInventory();
		return true;
	}
	
	public static boolean contains(Player player, Material item, byte data, int required)
	{
		return contains(player, null, item, data, required);
	}

	public static boolean contains(Player player, String itemNameContains, Material item, byte data, int required)
	{
		return contains(player, itemNameContains, item, data, required, true, true);
	}

	public static boolean contains(Player player, String itemNameContains, Material item, byte data, int required, boolean checkArmor, boolean checkCursor)
	{
		
		for (ItemStack stack : getItems(player, checkArmor, checkCursor))
		{
			if (required <= 0)
			{
				return true;
			}
			
			if (stack == null)
				continue;
			
			if (stack.getType() != item)
				continue;
			
			if (stack.getAmount() <= 0)
				continue;
			
			if (data >=0 && 
				stack.getData() != null && stack.getData().getData() != data)
				continue;
			
			if (itemNameContains != null && 
				(stack.getItemMeta().getDisplayName() == null || !stack.getItemMeta().getDisplayName().contains(itemNameContains)))
				continue;
			
			required -= stack.getAmount();
		}
		
		if (required <= 0)
		{
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean remove(Player player, Material item, byte data, int toRemove) 
	{
		if (!contains(player, item, data, toRemove))
			return false;
		
		for (int i : player.getInventory().all(item).keySet()) 
		{
			if (toRemove <= 0)
				continue;
			
			ItemStack stack = player.getInventory().getItem(i);

			if (stack.getData() == null || stack.getData().getData() == data)
			{
				int foundAmount = stack.getAmount();

				if (toRemove >= foundAmount) 
				{
					toRemove -= foundAmount;
					player.getInventory().setItem(i, null);
				} 

				else 
				{
					stack.setAmount(foundAmount - toRemove);
					player.getInventory().setItem(i, stack);
					toRemove = 0;
				}
			} 
		}
		
		player.updateInventory();
		return true;
	}

	public static void Clear(Player player)
	{
		//player.getOpenInventory().close();
		
		PlayerInventory inv = player.getInventory();
		
		inv.clear();
        inv.setArmorContents(new ItemStack[4]);
	    player.setItemOnCursor(new ItemStack(Material.AIR));
		
		player.saveData();
	}

	public static ArrayList<ItemStack> getItems(Player player)
	{
		return getItems(player, true, true);
	}

	public static ArrayList<ItemStack> getItems(Player player, boolean getArmor, boolean getCursor)
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		PlayerInventory inv = player.getInventory();

		for (ItemStack item : inv.getContents())
		{
			if (item != null && item.getType() != Material.AIR)
			{
				items.add(item.clone());
			}
		}

		if (getArmor)
		{
			for (ItemStack item : inv.getArmorContents())
			{
				if (item != null && item.getType() != Material.AIR)
				{
					items.add(item.clone());
				}
			}
		}

		if (getCursor)
		{
			ItemStack cursorItem = player.getItemOnCursor();

			if (cursorItem != null && cursorItem.getType() != Material.AIR)
				items.add(cursorItem.clone());
		}

		return items;
	}
	
	public static void drop(Player player, boolean clear)
	{
		for (ItemStack cur : getItems(player))
		{
			player.getWorld().dropItemNaturally(player.getLocation(), cur);
		}
		
		if (clear)
			Clear(player);
	}

	@SuppressWarnings("deprecation")
	public static void Update(Entity player) 
	{
		if (!(player instanceof Player))
			return;
		
		((Player)player).updateInventory();
	}

	public static int removeAll(Player player, Material type, byte data) 
	{
		HashSet<ItemStack> remove = new HashSet<ItemStack>();
		int count = 0;
		
		for (ItemStack item : player.getInventory().getContents())
			if (item != null)
				if (item.getType() == type)
					if (data == -1 || item.getData() == null || (item.getData() != null && item.getData().getData() == data))
					{
						count += item.getAmount();
						remove.add(item);
					}
	
		for (ItemStack item : remove)
			player.getInventory().remove(item);	

		return count;
	}
	
	public static byte GetData(ItemStack stack)
	{
		if (stack == null)
			return (byte)0;
		
		if (stack.getData() == null)
			return (byte)0;
		
		return stack.getData().getData();
	}

	public static boolean IsItem(ItemStack item, Material type, byte data)
	{
		return IsItem(item, null, type.getId(), data);
	}
	
	public static boolean IsItem(ItemStack item, String name, Material type, byte data)
	{
		return IsItem(item, name, type.getId(), data);
	}
	
	public static boolean IsItem(ItemStack item, String name, int id, byte data)
	{
		if (item == null)
			return false;
		
		if (item.getTypeId() != id)
			return false;
		
		if (data != -1 && GetData(item) != data)
			return false;
		
		if (name != null && (item.getItemMeta().getDisplayName() == null || !item.getItemMeta().getDisplayName().contains(name)))
			return false;
		
		return true;
	}
	
	public static void DisallowMovementOf(InventoryClickEvent event, String name, Material type, byte data, boolean inform) 
	{
		DisallowMovementOf(event, name, type, data, inform, false);
	}
	
	public static void DisallowMovementOf(InventoryClickEvent event, String name, Material type, byte data, boolean inform, boolean allInventorties) 
	{
		/*
		System.out.println("Inv Type: " + event.getInventory().getType());
		System.out.println("Click: " + event.getClick());
		System.out.println("Action: " + event.getAction());
		 
		System.out.println("Slot: " + event.getSlot());
		System.out.println("Slot Raw: " + event.getRawSlot());
		System.out.println("Slot Type: " + event.getSlotType());
		
		System.out.println("Cursor: " + event.getCursor());
		System.out.println("Current: " + event.getCurrentItem());
		
		System.out.println("View Type: " + event.getView().getType());
		System.out.println("View Top Type: " + event.getView().getTopInventory().getType());
		System.out.println("HotBar Button: " + event.getHotbarButton());
		*/
		
		//Do what you want in Crafting Inv
		if (!allInventorties && event.getInventory().getType() == InventoryType.CRAFTING)
			return;
		
		//Hotbar Swap
		if (event.getAction() == InventoryAction.HOTBAR_SWAP ||
			event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD)
		{
			boolean match = false;
			
			if (IsItem(event.getCurrentItem(), name, type, data))
				match = true;

			if (IsItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()), name, type, data))
				match = true;
			
			if (!match) 
				return; 
			
			//Inform
			UtilPlayer.message(event.getWhoClicked(), F.main("Inventory", "You cannot hotbar swap " + F.item(name) + "."));
			event.setCancelled(true);
		}
		//Other
		else
		{
			if (event.getCurrentItem() == null)
				return;

			IsItem(event.getCurrentItem(), name, type, data);
			
			//Type
			if (!IsItem(event.getCurrentItem(), name, type, data))
				return;
			//Inform
			UtilPlayer.message(event.getWhoClicked(), F.main("Inventory", "You cannot move " + F.item(name) + "."));
			event.setCancelled(true);
		}
	}

	public static void UseItemInHand(Player player)
	{
		if (player.getItemInHand().getAmount() > 1)
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		else
			player.setItemInHand(null);
		
		Update(player);
	}
	
	
}
