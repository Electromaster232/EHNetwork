package ehnetwork.core.gadget.gadgets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.event.ItemGadgetOutOfAmmoEvent;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ItemGemBomb extends ItemGadget
{
	private HashMap<Item, Long> _activeBombs = new HashMap<Item, Long>();
	private HashSet<Item> _gems = new HashSet<Item>();
	
	public ItemGemBomb(GadgetManager manager)
	{
		super(manager, "Gem Party Bomb", new String[] 
				{
					C.cWhite + "It's party time! You will be",
					C.cWhite + "everyones favourite player",
					C.cWhite + "when you use one of these!",
					" ", 
					C.cRed +C.Bold + "WARNING: " + ChatColor.RESET + "This uses 2000 Gems" 
				}, 
				-1,  
				Material.EMERALD, (byte)0, 
				30000, new Ammo("Gem Party Bomb", "10 Gem Party Bomb", Material.EMERALD, (byte)0, new String[] { C.cWhite + "10 Coin Party Bomb to PARTY!" }, 10, 10));
	}

	@Override
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
		
		//Gems
		if (Manager.getDonationManager().Get(player.getName()).GetBalance(CurrencyType.Gems) < 2000)
		{
			UtilPlayer.message(player, F.main("Inventory", "You do not have the required " + C.cGreen + "2000 Gems") + ".");
			return;
		}
		
		//Already In Use
		if (!_activeBombs.isEmpty())
		{
			UtilPlayer.message(player, F.main("Inventory", "There is already a " + F.elem(C.cGreen + "Gem Bomb")) + " being used.");
			return;
		}
		
		//Recharge
		if (!Recharge.Instance.use(player, GetName(), _recharge, _recharge > 1000, false))
		{
			UtilInv.Update(player);
			return;	
		}	
		
		//Use Stock/Gems
		Manager.getInventoryManager().addItemToInventory(player, getGadgetType().name(), GetName(), -1);
		Manager.getDonationManager().RewardGems(null, GetName(), event.getPlayer().getName(), event.getPlayer().getUniqueId(), -2000);
		
		player.getInventory().setItem(Manager.getActiveItemSlot(), ItemStackFactory.Instance.CreateStack(GetDisplayMaterial(), GetDisplayData(), 1, F.item(GetName())));
		
		ActivateCustom(event.getPlayer());
	}
	
	@Override
	public void ActivateCustom(Player player)
	{
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), new ItemStack(Material.EMERALD_BLOCK));
		UtilAction.velocity(item, player.getLocation().getDirection(), 1, false, 0, 0.2, 1, false);
		_activeBombs.put(item, System.currentTimeMillis());
		
		//Inform
		for (Player other : UtilServer.getPlayers())
			UtilPlayer.message(other, C.cGreen + C.Bold + player.getName() + C.cWhite + C.Bold + " has thrown a " + C.cGreen + C.Bold + "Gem Party Bomb" + C.cWhite + C.Bold + "!");
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Item> itemIterator = _activeBombs.keySet().iterator();
		
		while (itemIterator.hasNext())
		{
			Item item = itemIterator.next();
			long time = _activeBombs.get(item);

			if (UtilTime.elapsed(time, 3000))
			{
				if (Math.random() > 0.80)
					UtilFirework.playFirework(item.getLocation(), FireworkEffect.builder().flicker(false).withColor(Color.GREEN).with(Type.BURST).trail(false).build());
				else
					item.getWorld().playSound(item.getLocation(), Sound.FIREWORK_LAUNCH, 1f, 1f);
				
				Item gem = item.getWorld().dropItem(item.getLocation().add(0, 1, 0), new ItemStack(Material.EMERALD));
				
				//Velocity
				long passed = System.currentTimeMillis() - time;
				Vector vel = new Vector(Math.sin(passed/300d), 0, Math.cos(passed/300d));
				
				UtilAction.velocity(gem, vel, Math.abs(Math.sin(passed/3000d)), false, 0, 0.2 + Math.abs(Math.cos(passed/3000d))*0.8, 1, false);
				
				gem.setPickupDelay(40);
				
				_gems.add(gem);
			}
			
			if (UtilTime.elapsed(time, 23000))
			{
				item.remove();
				itemIterator.remove();
			}
		}
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (_activeBombs.keySet().contains(event.getItem()))
		{
			event.setCancelled(true);
		}
		else if (_gems.contains(event.getItem()))
		{
			event.setCancelled(true);
			event.getItem().remove();
			
			Manager.getDonationManager().RewardGemsLater(GetName() + " Pickup", event.getPlayer(), 4);
			
			event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1f, 2f);
		}
	}
	
	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Iterator<Item> gemIterator = _gems.iterator();
		
		while (gemIterator.hasNext())
		{
			Item gem = gemIterator.next();
			
			if (!gem.isValid() || gem.getTicksLived() > 1200)
			{
				gem.remove();
				gemIterator.remove();
			}
		}
	}
}
