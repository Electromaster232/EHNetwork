package ehnetwork.game.microgames.gui.privateServer.page;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;
import ehnetwork.game.microgames.managers.GameHostManager;

public abstract class BasePage extends ShopPageBase<MicroGamesManager, PrivateServerShop>
{
	protected GameHostManager _manager;

	public BasePage(MicroGamesManager plugin, PrivateServerShop shop, String pageName, Player player)
	{
		this(plugin, shop, pageName, player, 54);
	}

	public BasePage(MicroGamesManager plugin, PrivateServerShop shop, String pageName, Player player, int slots)
	{
		super(plugin, shop, plugin.GetClients(), plugin.GetDonation(), pageName, player, slots);

		_manager = plugin.GetGameHostManager();
	}

	public void addBackButton(int slot)
	{
		addButton(4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new MenuPage(getPlugin(), getShop(), player));
			}
		});
	}

	public void addBackToSetGamePage()
	{
		addButton(4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new SetGamePage(getPlugin(), getShop(), player));
			}
		});
	}

	public ItemStack getPlayerHead(String playerName, String title)
	{
		return getPlayerHead(playerName, title, null);
	}

	public ItemStack getPlayerHead(String playerName, String title, String[] lore)
	{
		ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

		SkullMeta meta = ((SkullMeta) is.getItemMeta());
		meta.setOwner(playerName);
		meta.setDisplayName(title);
		if (lore != null)
			meta.setLore(Arrays.asList(lore));
		is.setItemMeta(meta);

		return is;
	}
}
