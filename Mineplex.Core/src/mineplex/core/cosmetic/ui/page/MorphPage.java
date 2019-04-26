package mineplex.core.cosmetic.ui.page;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.cosmetic.CosmeticManager;
import mineplex.core.cosmetic.ui.CosmeticShop;
import mineplex.core.donation.DonationManager;
import mineplex.core.gadget.types.Gadget;
import mineplex.core.gadget.types.GadgetType;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.item.IButton;

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