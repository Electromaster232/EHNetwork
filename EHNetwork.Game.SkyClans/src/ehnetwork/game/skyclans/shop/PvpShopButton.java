package ehnetwork.game.skyclans.shop;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.InventoryUtil;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.skyclans.clans.ClansManager;

public class PvpShopButton<PageType extends ShopPageBase<ClansManager, ?>> implements IButton
{
	protected PageType Page;
	protected PvpItem Item;
	
	public PvpShopButton(PageType page, PvpItem item)
	{
		Page = page;
		Item = item;
	}

	@Override
	public void onClick(final Player player, ClickType clickType)
	{
		int balance = Page.getDonationManager().Get(player.getName()).getGold();
		int cost = Item.getPrice();
		int tempAmount = Item.getAmount();
		
		if (clickType == ClickType.SHIFT_LEFT)
		{
			cost *= Item.getBulkCount() == -1 ? 1 : Item.getBulkCount();
			tempAmount = Item.getBulkCount() == -1 ? Item.getAmount() : Item.getBulkCount();
		}
		
		final int deliveryAmount = tempAmount;
		
		if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT)
		{
			if (cost > balance)
			{
				Page.playDenySound(player);
				UtilPlayer.message(player, F.main(Page.getPlugin().getName(), "You do not have enough funds to purchase " + deliveryAmount + " " + Item.GetName() + "."));
				return;
			}
			else
			{
				Page.getDonationManager().RewardGold(new Callback<Boolean>()
				{
					public void run(Boolean result)
					{
						if (result)
						{
							Page.playAcceptSound(player);
							ShopItem item = Item.clone();
							item.setAmount(deliveryAmount);
							
							player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Item.getType(), Item.getData().getData(), Item.getAmount(), Item.GetName()));
						}
						else
						{
							Page.playDenySound(player);
							UtilPlayer.message(player, F.main(Page.getPlugin().getName(), "An error occurred processing your purchase."));
						}
					}
				}, "Clans", player.getName(), Page.getClientManager().Get(player).getAccountId(), -cost);
			}
		}
		else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT)
		{
			int removed = 1;
			ItemStack dumbItem = new ItemStack(Item.getType(), Item.getAmount(), Item.getDurability());
			
			if (InventoryUtil.first((CraftInventory)player.getInventory(), 36, dumbItem, true) == -1)
			{
				Page.playDenySound(player);
				UtilPlayer.message(player, F.main(Page.getPlugin().getName(), "You do not have " + deliveryAmount + " " + Item.GetName() + " in your inventory."));
				return;
			}
			
			if (clickType == ClickType.RIGHT)
			{
				if (player.getInventory().contains(Item.getType(), Item.getAmount()))
					InventoryUtil.removeItem((CraftInventory)player.getInventory(), 36, dumbItem);
			}
			else
				removed = InventoryUtil.getCountOfObjectsRemoved((CraftInventory)player.getInventory(), 36, dumbItem);
			
			final int creditAmount = removed * Item.getPrice() / 2;
			System.out.println("Crediting " + player.getName() + " with " + creditAmount + " gold.");
			Page.getDonationManager().RewardGold(new Callback<Boolean>()
			{
				public void run(Boolean result)
				{
					if (result)
					{
						Page.playAcceptSound(player);
						System.out.println("Credited " + player.getName() + " with " + creditAmount + " gold.");
					}
					else
					{
						Page.playDenySound(player);
						UtilPlayer.message(player, F.main(Page.getPlugin().getName(), "An error occurred processing your return."));
					}
				}
			}, "Clans", player.getName(), Page.getClientManager().Get(player).getAccountId(), creditAmount);
		}
	}
}
