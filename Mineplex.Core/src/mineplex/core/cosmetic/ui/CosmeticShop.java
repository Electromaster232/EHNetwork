package mineplex.core.cosmetic.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.messaging.PluginMessageListener;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.cosmetic.CosmeticManager;
import mineplex.core.cosmetic.ui.page.GadgetPage;
import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.cosmetic.ui.page.PetTagPage;
import mineplex.core.cosmetic.ui.page.TreasurePage;
import mineplex.core.donation.DonationManager;
import mineplex.core.gadget.event.ItemGadgetOutOfAmmoEvent;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

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
