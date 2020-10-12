package ehnetwork.core.cosmetic.ui.page;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.cosmetic.ui.CosmeticShop;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.gadget.types.Gadget;
import ehnetwork.core.gadget.types.GadgetType;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;

public class MorphPage extends GadgetPage
{
    public MorphPage(CosmeticManager plugin, CosmeticShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player)
    {
        super(plugin, shop, clientManager, donationManager, name, player);
    }
    
    @Override
    protected void buildPage()
    {
    	int slot = 19;
    	
        for (Gadget gadget : getPlugin().getGadgetManager().getGadgets(GadgetType.Morph))
        {
        	addGadget(gadget, slot);
        	
        	if (getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Morph) == gadget)
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
}