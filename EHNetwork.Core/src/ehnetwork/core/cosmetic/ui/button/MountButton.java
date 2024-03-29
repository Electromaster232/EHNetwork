package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.cosmetic.ui.CosmeticShop;
import ehnetwork.core.cosmetic.ui.page.MountPage;
import ehnetwork.core.mount.Mount;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.page.ConfirmationPage;

public class MountButton implements IButton
{
	private Mount<?> _mount;
	private MountPage _page;
	
	public MountButton(Mount<?> mount, MountPage page)
	{
		_mount = mount;
		_page = page;
	}

	@Override
	public void onClick(final Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new ConfirmationPage<CosmeticManager, CosmeticShop>(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), new Runnable()
		{
			public void run()
			{
				_page.getPlugin().getInventoryManager().addItemToInventory(null, player, "Mount", _mount.GetName(), 1);
				_page.refresh();
			}
		}, _page, _mount, CurrencyType.Coins, player));
	}
}
