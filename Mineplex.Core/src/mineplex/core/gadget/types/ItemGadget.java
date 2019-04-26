package mineplex.core.gadget.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.event.ItemGadgetOutOfAmmoEvent;
import mineplex.core.gadget.gadgets.Ammo;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;

public abstract class ItemGadget extends Gadget
{
	private Ammo _ammo;
	protected long _recharge;

	public ItemGadget(GadgetManager manager, String name, String[] desc, int cost, Material mat, byte data, 
			long recharge, Ammo ammo) 
	{
		super(manager, GadgetType.Item, name, desc, cost, mat, data);
		
		_ammo = ammo;
		_recharge = recharge;
		Free = true;
	}
	
	@Override
	public void EnableCustom(Player player) 
	{
		ApplyItem(player, true);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		RemoveItem(player);
	}

	public HashSet<Player> GetActive()
	{
		return _active;
	}

	public boolean IsActive(Player player)
	{
		return _active.contains(player);
	}

	public void ApplyItem(Player player, boolean inform)
	{
		Manager.RemoveItem(player);

		_active.add(player);

		List<String> itemLore = new ArrayList<String>();
		itemLore.addAll(Arrays.asList(GetDescription()));
		itemLore.add(C.cBlack);
		itemLore.add(C.cWhite + "Your Ammo : " + Manager.getInventoryManager().Get(player).getItemCount(GetName()));
		
		player.getInventory().setItem(Manager.getActiveItemSlot(), ItemStackFactory.Instance.CreateStack(GetDisplayMaterial(), GetDisplayData(), 1, F.item(Manager.getInventoryManager().Get(player).getItemCount(GetName()) + " " + GetName())));
		
		if (inform)
			UtilPlayer.message(player, F.main("Gadget", "You equipped " + F.elem(GetName()) + "."));
	}

	@EventHandler
	public void orderThatChest(PlayerDropItemEvent event)
	{  
		if (IsActive(event.getPlayer()) && event.getItemDrop().getItemStack().getType() == GetDisplayMaterial())
		{
		    final Player player = event.getPlayer();
		    
			Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					if (player.isOnline())
					{
					    player.getInventory().remove(GetDisplayMaterial());
					    player.getInventory().setItem(Manager.getActiveItemSlot(), ItemStackFactory.Instance.CreateStack(GetDisplayMaterial(), GetDisplayData(), 1, F.item(Manager.getInventoryManager().Get(player).getItemCount(GetName()) + " " + GetName())));
						UtilInv.Update(player);
					}
				}
			});
		}
	}

	public void RemoveItem(Player player)
	{
		if (_active.remove(player))
		{
			player.getInventory().setItem(Manager.getActiveItemSlot(), null);
			
			UtilPlayer.message(player, F.main("Gadget", "You unequipped " + F.elem(GetName()) + "."));
		}
	}
	
	public Ammo getAmmo()
	{
		return _ammo;
	}

	public boolean IsItem(Player player)
	{
		return UtilInv.IsItem(player.getItemInHand(), GetDisplayMaterial(), GetDisplayData());
	}

	@EventHandler
	public void Activate(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), this.GetDisplayMaterial()))
			return;

		Player player = event.getPlayer();

		if (!IsActive(player))
			return; 
		
		event.setCancelled(true); 
		
		//Stock
		if (Manager.getInventoryManager().Get(player).getItemCount(GetName()) <= 0)
		{
			
			UtilPlayer.message(player, F.main("Gadget", "You do not have any " + GetName() + " left."));
		
			ItemGadgetOutOfAmmoEvent ammoEvent = new ItemGadgetOutOfAmmoEvent(event.getPlayer(), this);
			Bukkit.getServer().getPluginManager().callEvent(ammoEvent);
			
			return;
		}
		
		//Recharge
		if (!Recharge.Instance.use(player, GetName(), _recharge, _recharge > 1000, false))
		{
			UtilInv.Update(player);
			return;	
		}	
		
		Manager.getInventoryManager().addItemToInventory(player, getGadgetType().name(), GetName(), -1);

		player.getInventory().setItem(Manager.getActiveItemSlot(), ItemStackFactory.Instance.CreateStack(GetDisplayMaterial(), GetDisplayData(), 1, F.item(Manager.getInventoryManager().Get(player).getItemCount(GetName()) + " " + GetName())));
		
		ActivateCustom(event.getPlayer());
	}

	public abstract void ActivateCustom(Player player);
}
