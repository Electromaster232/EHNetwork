package ehnetwork.game.microgames.game.games.build.gui.page;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.build.Build;
import ehnetwork.game.microgames.game.games.build.BuildData;
import ehnetwork.game.microgames.game.games.build.gui.OptionsShop;

public class ParticlesPage extends ShopPageBase<MicroGamesManager, OptionsShop>
{
	private Build _game;

	public ParticlesPage(Build game, MicroGamesManager plugin, OptionsShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Add Particles", player);
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
		for (final UtilParticle.ParticleType particleType : UtilParticle.ParticleType.values())
		{
			if (particleType.hasFriendlyData())
			{
				ShopItem shopItem = new ShopItem(particleType.getMaterial(), particleType.getData(), particleType.getFriendlyName(), null, 0, false, false);
				addButton(index, shopItem, new IButton()
				{
					@Override
					public void onClick(Player player, ClickType clickType)
					{
						String[] lore = { ChatColor.GRAY + "Right click to place" };
						ItemStack itemStack = ItemStackFactory.Instance.CreateStack(particleType.getMaterial(), particleType.getData(), 1, ChatColor.GREEN + "Place " + particleType.getFriendlyName(), Arrays.asList(lore));
						player.getInventory().addItem(itemStack);
					}
				});

				index++;
			}
		}

		ShopItem clearButton = new ShopItem(Material.TNT, "Clear Particles", null, 0, false);
		addButton(53, clearButton, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				buildData.resetParticles();
			}
		});

		addButton((9 * 5) + 4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				getShop().openPageForPlayer(player, new OptionsPage(_game, getPlugin(), getShop(), getClientManager(), getDonationManager(), player));
			}
		});
	}
}
