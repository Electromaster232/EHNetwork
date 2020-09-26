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
import ehnetwork.core.cosmetic.ui.button.ActivateMountButton;
import ehnetwork.core.cosmetic.ui.button.DeactivateMountButton;
import ehnetwork.core.cosmetic.ui.button.MountButton;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.mount.Mount;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;

public class MountPage extends ShopPageBase<CosmeticManager, CosmeticShop>
{
    public MountPage(CosmeticManager plugin, CosmeticShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player)
    {
        super(plugin, shop, clientManager, donationManager, name, player, 54);
        
        buildPage();
    }
    
    protected void buildPage()
    {
    	int slot = 19;
        
        for (Mount<?> mount : getPlugin().getMountManager().getMounts())
        {
        	addMount(mount, slot);
        	slot++;
        	
        	if (slot == 26)
        		slot = 28;
        }
    }
    
    protected void addMount(Mount<?> mount, int slot)
    {
    	List<String> itemLore = new ArrayList<String>();
    	
    	if (mount.GetCost(CurrencyType.Coins) != -1)
    	{
    		itemLore.add(C.cYellow + mount.GetCost(CurrencyType.Coins) + " Coins");
    	}
    	
    	itemLore.add(C.cBlack);
    	itemLore.addAll(Arrays.asList(mount.GetDescription()));
    	
    	if (getDonationManager().Get(getPlayer().getName()).OwnsUnknownPackage(mount.GetName()))
    	{
    		if (mount.GetActive().containsKey(getPlayer()))
    		{
    			addButton(slot, new ShopItem(mount.GetDisplayMaterial(), mount.GetDisplayData(), "Deactivate " + mount.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, false, false), new DeactivateMountButton(mount, this));
    		}
    		else
    		{
    			addButton(slot, new ShopItem(mount.GetDisplayMaterial(), mount.GetDisplayData(), "Activate " + mount.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, false, false), new ActivateMountButton(mount, this));
    		}
    	}
    	else
    	{
    		if (mount.GetCost(CurrencyType.Coins) != -1 && getDonationManager().Get(getPlayer().getName()).GetBalance(CurrencyType.Coins) >= mount.GetCost(CurrencyType.Coins))
    			addButton(slot, new ShopItem(mount.GetDisplayMaterial(), mount.GetDisplayData(), (mount.GetCost(CurrencyType.Coins) < 0 ? "" : "Purchase ") + mount.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, false, false), new MountButton(mount, this));
    		else
    			setItem(slot, new ShopItem(mount.GetDisplayMaterial(), mount.GetDisplayData(), (mount.GetCost(CurrencyType.Coins) < 0 ? "" : "Purchase ") + mount.GetName(), itemLore.toArray(new String[itemLore.size()]), 1, true, false));
    	}
    	
		addButton(4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(getPlayer(), new Menu(getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});
    }
}