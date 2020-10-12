package ehnetwork.core.cosmetic.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.messaging.PluginMessageListener;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.cosmetic.ui.page.GadgetPage;
import ehnetwork.core.cosmetic.ui.page.Menu;
import ehnetwork.core.cosmetic.ui.page.PetTagPage;
import ehnetwork.core.cosmetic.ui.page.TreasurePage;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.gadget.event.ItemGadgetOutOfAmmoEvent;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class CosmeticShop extends ShopBase<CosmeticManager> implements PluginMessageListener
{
	public CosmeticShop(CosmeticManager plugin, CoreClientManager clientManager, DonationManager donationManager, String name)
	{
		super(plugin, clientManager, donationManager, name, CurrencyType.Gems, CurrencyType.Coins);
		
		plugin.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(plugin.getPlugin(), "MC|ItemName", this);
	}

	@Override
	protected ShopPageBase<CosmeticManager, ? extends ShopBase<CosmeticManager>> buildPagesFor(Player player)
	{
		return new Menu(getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
        if (!channel.equalsIgnoreCase("MC|ItemName"))
            return;
        
        if (getPlayerPageMap().containsKey(player.getName()) && getPlayerPageMap().get(player.getName()) instanceof PetTagPage)
        {
	        if (message != null && message.length >= 1)
	        {
	            String tagName = new String(message);
	            
	            ((PetTagPage) getPlayerPageMap().get(player.getName())).SetTagName(tagName);
	        }
	    }
	}

	@EventHandler
	public void itemGadgetEmptyAmmo(ItemGadgetOutOfAmmoEvent event)
	{
		new GadgetPage(getPlugin(), this, getClientManager(), getDonationManager(), "Gadgets", event.getPlayer()).purchaseGadget(event.getPlayer(), event.getGadget());
	}
	
	@EventHandler
	public void updateTreasure(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (ShopPageBase<CosmeticManager, ? extends ShopBase<CosmeticManager>> shop : getPlayerPageMap().values())
		{
			if (shop instanceof TreasurePage)
				((TreasurePage) shop).update();
		}
	}
}
