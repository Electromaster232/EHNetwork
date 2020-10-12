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
import ehnetwork.game.microgames.game.games.build.gui.OptionsShop;

public class OptionsPage extends ShopPageBase<MicroGamesManager, OptionsShop>
{
	private Build _game;

	public OptionsPage(Build game, MicroGamesManager plugin, OptionsShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Options", player, 9);
		_game = game;
		buildPage();
	}

	@Override
	protected void buildPage()
	{

		ShopItem ground = new ShopItem(Material.GRASS, "Change Ground", 0, false);
		final ShopItem flightSpeed = new ShopItem(Material.FEATHER, "Flight Speed", new String[] {C.cWhite + "Left-Click to Increase Speed", C.cWhite + "Shift Left-Click to Decrease Speed" } , 0, false);
		ShopItem particles = new ShopItem(Material.NETHER_STAR, "Particles", 0, false);
		ShopItem weather = new ShopItem(Material.SAPLING, "Weather", 0, false);
		ShopItem time = new ShopItem(Material.WATCH, "Time of Day", 0, false);

		addButton(0, ground, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new GroundPage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});

		final int maxLevel = 8;
		final float minLevel = 0.1F;
		final float perLevel = (1F - minLevel) / (maxLevel - 1);
		flightSpeed.setAmount((int)((getPlayer().getFlySpeed() - minLevel) / perLevel) + 1);
		addButton(2, flightSpeed, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				if (clickType == ClickType.SHIFT_LEFT)
				{
					int newLevel = Math.max(1, flightSpeed.getAmount() - 1);
					player.setFlySpeed(minLevel + ((newLevel - 1) * perLevel));
				}
				else if (clickType == ClickType.LEFT)
				{
					int newLevel = Math.min(maxLevel, flightSpeed.getAmount() + 1);
					player.setFlySpeed(minLevel + ((newLevel - 1) * perLevel));
				}

				buildPage();
			}
		});

		addButton(4, particles, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new ParticlesPage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});

		addButton(6, weather, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new WeatherPage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});

		addButton(8, time, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new TimePage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});
	}
}
