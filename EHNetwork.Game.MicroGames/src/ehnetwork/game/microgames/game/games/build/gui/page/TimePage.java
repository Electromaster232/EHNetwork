package ehnetwork.game.microgames.game.games.build.gui.page;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.build.Build;
import ehnetwork.game.microgames.game.games.build.BuildData;
import ehnetwork.game.microgames.game.games.build.gui.OptionsShop;

public class TimePage extends ShopPageBase<MicroGamesManager, OptionsShop>
{
	private Build _game;

	public TimePage(Build game, MicroGamesManager plugin, OptionsShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Set Time", player, 18);
		_game = game;
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		final BuildData buildData = _game.getBuildData(getPlayer());

		if (buildData == null)
		{
			getPlayer().closeInventory();
			return;
		}

		for (int i = 0; i < 9; i++)
		{
			final int ticks = 3000 * i;
			boolean am = (ticks >= 0 && ticks < 6000) || (ticks >= 18000);
			int time = (6 + (ticks / 1000)) % 12;
			if (time == 0) time = 12;

			Material material = buildData.Time == ticks ? Material.WATCH : Material.INK_SACK;
			byte data = (byte) (buildData.Time == ticks ? 0 : 8);
			ShopItem item = new ShopItem(material, data, time + (am ? "am" : "pm"), null, 0, false, false);
			addButton(i, item, new IButton()
			{
				@Override
				public void onClick(Player player, ClickType clickType)
				{
					buildData.Time = ticks;
					buildPage();
				}
			});
		}

		addButton(9 + 4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new OptionsPage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});
	}
}
