package ehnetwork.game.arcade.gui.privateServer.page;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.game.GameCategory;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.gui.privateServer.PrivateServerShop;

public class SetGamePage extends BasePage
{
	public SetGamePage(ArcadeManager plugin, PrivateServerShop shop, Player player)
	{
		super(plugin, shop, "Set Game", player);
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		addBackButton(4);

		//Old code I'm not removing in-case you want the old style - William.
		/*
		int arcadeSlot = 9;
		int classicSlot = 13;
		int survivalSlot = 15;
		int championSlot = 17;

		for (GameType type : _manager.getAvailableGames(getPlayer()))
		{
			int slot = -1;

			switch(type.getGameCategory())
			{
				case CLASSICS:
					slot = classicSlot;
					classicSlot += 9;
					break;
				case SURVIVAL:
					slot = survivalSlot;
					survivalSlot += 9;
					break;
				case CHAMPIONS:
					slot = championSlot;
					championSlot += 9;
					break;
				case ARCADE:
					slot = arcadeSlot;
					arcadeSlot++;
					if (arcadeSlot % 9 == 3)
						arcadeSlot += 6;
			}

			if (slot == -1) continue; // ignore EXTRA and TEAM_VARIANT games

			addGameButton(slot, type);
		}
		*/

		ArrayList<GameCategory> allowedCats = new ArrayList<GameCategory>();
		allowedCats.add(GameCategory.ARCADE);
		allowedCats.add(GameCategory.CLASSICS);
		allowedCats.add(GameCategory.SURVIVAL);
		allowedCats.add(GameCategory.CHAMPIONS);
		allowedCats.add(GameCategory.EXTRA);
		allowedCats.add(GameCategory.TEAM_VARIANT);
		
		int gameSlot = 9;
		for (GameCategory cat : _manager.getGames(getPlayer()).keySet())
		{
			if (!allowedCats.contains(cat))
				continue;

			for (GameType type : _manager.getGames(getPlayer()).get(cat))
			{	
				addGameButton(gameSlot, type);
				gameSlot++;
			}
		}
	}

	private void addGameButton(int slot, final GameType type)
	{
		String infoString = ChatColor.RESET + C.cGray + "Make this next Game Type";
		String space = "§1";
		String left = ChatColor.YELLOW + "Left-Click " + C.cGray + "for a §fRandom Map§7.";
		String right = ChatColor.YELLOW + "Right-Click " + C.cGray + "to §fChoose Map§7.";
		String[] normLore = new String[]{infoString, space, left, right};
		if (_manager.hasWarning().contains(type))
		{
			normLore = new String[]{infoString, space, left, right, "§2", "§c§lWARNING: §fThis game was rejected!"};
		}
		ShopItem shopItem = new ShopItem(type.GetMaterial(), type.GetMaterialData(), type.GetName(), normLore, 1, false, false);
		addButton(slot, shopItem, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				if (clickType == ClickType.LEFT)
				{
					getPlugin().GetGame().setGame(type, player, true);
					player.closeInventory();
					return;
				}
				else if (clickType == ClickType.RIGHT)
				{
					getShop().openPageForPlayer(player, new ChooseMapPage(getPlugin(), getShop(), player, type));
					return;
				}

			}
		});
	}
}
