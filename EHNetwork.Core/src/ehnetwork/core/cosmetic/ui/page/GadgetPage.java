package ehnetwork.core.cosmetic.ui.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.C;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.cosmetic.ui.CosmeticShop;
import ehnetwork.core.cosmetic.ui.button.ActivateGadgetButton;
import ehnetwork.core.cosmetic.ui.button.DeactivateGadgetButton;
import ehnetwork.core.cosmetic.ui.button.GadgetButton;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.gadget.gadgets.MorphBlock;
import ehnetwork.core.gadget.gadgets.MorphNotch;
import ehnetwork.core.gadget.types.Gadget;
import ehnetwork.core.gadget.types.GadgetType;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ConfirmationPage;
import ehnetwork.core.shop.page.ShopPageBase;

public class GadgetPage extends ShopPageBase<CosmeticManager, CosmeticShop>
{
    public GadgetPage(CosmeticManager plugin, CosmeticShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player)
    {
        super(plugin, shop, clientManager, donationManager, name, player, 54);
        
        buildPage();
    }
    
    protected void buildPage()
    {
    	int slot = 19;
    	
        for (Gadget gadget : getPlugin().getGadgetManager().getGadgets(GadgetType.Item))
        {
        	addGadget(gadget, slot);
        	
        	if (getPlugin().getInventoryManager().Get(getPlayer()).getItemCount(gadget.GetDisplayName()) > 0)
        		addGlow(slot);
        	
        	slot++;
        	
        	if (slot == 26)
        		slot = 28;
        }
        
		addButton(4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(getPlayer(), new Menu(getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});
    }
    
    protected void addGadget(Gadget gadget, int slot)
    {
    	if (gadget instanceof MorphNotch)
    	{
    		//setItem(slot, new ShopItem(gadget.GetDisplayMaterial(), gadget.GetDisplayData(), "Disabled " + gadget.GetName(), new String[] { "Sorry! Currently disabled until fix is made for 1.8 players!" }, 1, false, false));
    		//return;
    	}
		else if (gadget instanceof MorphBlock)
		{
			//Prevent stacker bug
			if (getPlayer().getPassenger() != null)
				return;
		}

    	List<String> itemLore = new ArrayList<String>();
    	
    	if (gadget.GetCost(CurrencyType.Coins) >= 0)
    	{
    		itemLore.add(C.cYellow + gadget.GetCost(CurrencyType.Coins) + " Coins");
    	}
    	else if (gadget.GetCost(CurrencyType.Coins) == -2)
    	{
    		itemLore.add(C.cGold + "Found in Treasure Chests.");
    	}
    	
    	itemLore.add(C.cBlack);
    	itemLore.addAll(Arrays.asList(gadget.GetDescription()));
    	
    	if (gadget instanceof ItemGadget)
    	{
    		itemLore.add(C.cBlack);
    		itemLore.add(C.cGreen + "Right-Click To Purchase:");
    		itemLore.add(C.cWhite + ((ItemGadget)gadget).getAmmo().GetDisplayName() + " for " + C.cYellow + ((ItemGadget)gadget).getAmmo().GetCost(CurrencyType.Coins) + " Coins");
    		itemLore.add(C.cBlack);
    		itemLore.add(C.cWhite + "Your Ammo : " + C.cGreen + getPlugin().getInventoryManager().Get(getPlayer()).getItemCount(gadget.GetName()));
    	}
    	
    	if (gadget.IsFree() || getDonationManager().Get(getPlayer().getName()).OwnsUnknownPackage(gadget.GetName()))
    	{
    		if (gadget.GetActive().contains(getPlayer()))
    		{
    			addButton(slot, new ShopItem(gadget.GetDisplayMaterial(), gadget.GetDisplayData(), "Deactivate " + gadget.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, false, false), new DeactivateGadgetButton(gadget, this));
    		}
    		else
    		{
    			addButton(slot, new ShopItem(gadget.GetDisplayMaterial(), gadget.GetDisplayData(), "Activate " + gadget.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, false, false), new ActivateGadgetButton(gadget, this));
    		}
    	}
    	else
    	{
    		if (gadget.GetCost(CurrencyType.Coins) > 0 && getDonationManager().Get(getPlayer().getName()).GetBalance(CurrencyType.Coins) >= gadget.GetCost(CurrencyType.Coins))
    			addButton(slot, new ShopItem(Material.INK_SACK, (byte) 8, (gadget.GetCost(CurrencyType.Coins) < 0 ? "" : "Purchase ") + gadget.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, false, false), new GadgetButton(gadget, this));
    		else
    			setItem(slot, new ShopItem(Material.INK_SACK, (byte)8, (gadget.GetCost(CurrencyType.Coins) < 0 ? "" : "Purchase ") + gadget.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, true, false));
    	}     
    }

	public void purchaseGadget(final Player player, final Gadget gadget)
	{
		getShop().openPageForPlayer(getPlayer(), new ConfirmationPage<CosmeticManager, CosmeticShop>(getPlugin(), getShop(), getClientManager(), getDonationManager(), new Runnable()
		{
			public void run()
			{
				getPlugin().getInventoryManager().addItemToInventory(getPlayer(), gadget.getGadgetType().name(), gadget.GetName(), (gadget instanceof ItemGadget ? ((ItemGadget) gadget).getAmmo().getQuantity() : gadget.getQuantity()));
				refresh();
			}
		}, this, (gadget instanceof ItemGadget ? ((ItemGadget) gadget).getAmmo() : gadget), CurrencyType.Coins, getPlayer()));
	}

	public void activateGadget(Player player, Gadget gadget)
	{
		if (gadget instanceof ItemGadget)
		{
			if (getPlugin().getInventoryManager().Get(player).getItemCount(gadget.GetName()) <= 0)
			{
				purchaseGadget(player, gadget);
				return;
			}
		}
		
		playAcceptSound(player);
		gadget.Enable(player);
		
		getShop().openPageForPlayer(getPlayer(), new Menu(getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
	}

	public void handleRightClick(Player player, Gadget gadget)
	{
		if (gadget instanceof ItemGadget)
		{
			purchaseGadget(player, gadget);
		}
	}

	public void deactivateGadget(Player player, Gadget gadget)
	{
		playAcceptSound(player);
		gadget.Disable(player);
		refresh();
	}
}