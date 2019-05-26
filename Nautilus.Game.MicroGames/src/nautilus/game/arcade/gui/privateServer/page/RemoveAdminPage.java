package nautilus.game.arcade.gui.privateServer.page;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.shop.item.IButton;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.gui.privateServer.PrivateServerShop;

public class RemoveAdminPage extends BasePage
{
	public RemoveAdminPage(ArcadeManager plugin, PrivateServerShop shop, Player player)
	{
		super(plugin, shop, "Remove Admin", player);

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		addBackButton(4);

		HashSet<String> admins = _manager.getAdminList();
		Iterator<String> iterator = admins.iterator();

		int slot = 9;
		while (iterator.hasNext())
		{
			String name = iterator.next();
			ItemStack head = getPlayerHead(name, C.cGreen + C.Bold + name, new String[] {ChatColor.RESET + C.cGray + "Click to Remove Admin"});
			addButton(slot, head, getRemoveAdminButton(slot, name));

			slot++;
		}
	}

	private IButton getRemoveAdminButton(final int slot, final String playerName)
	{
		return new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				_manager.removeAdmin(playerName);
				removeButton(slot);
				UtilPlayer.message(getPlayer(), F.main("Server", "You removed admin power from " + F.name(playerName) + "."));
			}
		};
	}
}
