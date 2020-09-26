package ehnetwork.game.microgames.game.games.build.gui.page;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.build.Build;
import ehnetwork.game.microgames.game.games.build.BuildData;
import ehnetwork.game.microgames.game.games.build.GroundData;
import ehnetwork.game.microgames.game.games.build.gui.OptionsShop;

public class GroundPage extends ShopPageBase<MicroGamesManager, OptionsShop>
{
	private static GroundData[] GROUNDS = {
			new GroundData(Material.STONE), new GroundData(Material.GRASS), new GroundData(Material.DIRT),
			new GroundData(Material.SAND), new GroundData(Material.WATER_BUCKET), new GroundData(Material.LAVA_BUCKET),
			new GroundData(Material.WOOD), new GroundData(Material.COBBLESTONE), new GroundData(Material.NETHERRACK),
			new GroundData(Material.SMOOTH_BRICK), new GroundData(Material.ENDER_STONE), new GroundData(Material.MYCEL),
			new GroundData(Material.STAINED_CLAY, (byte) 0), new GroundData(Material.STAINED_CLAY, (byte) 15),
			new GroundData(Material.STAINED_CLAY, (byte) 4), new GroundData(Material.STAINED_CLAY, (byte) 3),
			new GroundData(Material.STAINED_CLAY, (byte) 5), new GroundData(Material.STAINED_CLAY, (byte) 6),
			new GroundData(Material.QUARTZ_BLOCK), new GroundData(Material.ICE), new GroundData(Material.IRON_BLOCK),
			new GroundData(Material.GOLD_BLOCK), new GroundData(Material.DIAMOND_BLOCK)};

	private Build _game;

	public GroundPage(Build game, MicroGamesManager plugin, OptionsShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Change Ground", player, 9 * 4);
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

		int index = 0;
		for (final GroundData data : GROUNDS)
		{
			ShopItem shopItem = new ShopItem(data.getMaterial(), data.getData(), ItemStackFactory.Instance.GetName(data.getMaterial(), data.getData(), true), null, 0, false, false);
			addButton(index, shopItem, new IButton()
			{
				@Override
				public void onClick(Player player, ClickType clickType)
				{
					buildData.setGround(data);
				}
			});
			index++;
		}

		addButton((9 * 3) + 4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new OptionsPage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});
	}
}
