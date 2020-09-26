package ehnetwork.game.microgames.gui.privateServer.page;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.game.GameCategory;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;

public class EditRotationPage extends BasePage
{
	public EditRotationPage(MicroGamesManager plugin, PrivateServerShop shop, Player player)
	{
		super(plugin, shop, "Edit Rotation", player);
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		addBackButton(4);

		List<GameType> enabledGames = getPlugin().GetServerConfig().GameList;

		//Old code I'm not removing in-case the old style is wanted. - William
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

			if (slot == -1) continue;

			addGameButton(slot, type, enabledGames.contains(type));
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
				addGameButton(gameSlot, type, enabledGames.contains(type));
				gameSlot++;
			}
		}
	}

	private void addGameButton(int slot, final GameType type, boolean enabled)
	{
		String titleString = ChatColor.RESET + (enabled ? C.cGreen : C.cRed) + ChatColor.BOLD + type.GetName();
		String infoString = ChatColor.RESET + C.cGray + (enabled ? "Click to Disable" : "Click to Enable");
		String[] lore = new String[]{infoString};
		if (_manager.hasWarning().contains(type))
		{
			lore = new String[]{infoString, "§1", "§c§lWARNING: §fThis game was rejected!"};
		}
		ShopItem shopItem = new ShopItem(type.GetMaterial(), type.GetMaterialData(), titleString, lore, 1, false, false);
		int itemCount = enabled ? 1 : 0;

		addButtonFakeCount(slot, shopItem, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				String announceString = C.Bold + type.GetLobbyName();

				if (getPlugin().GetServerConfig().GameList.contains(type))
				{
					if (getPlugin().GetServerConfig().GameList.size() > 1)
					{
						getPlugin().GetServerConfig().GameList.remove(type);
						announceString = C.cRed + announceString + " removed from rotation.";
					}
					else
					{
						UtilPlayer.message(getPlayer(), "You must keep at least one game in rotation!");
						getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_BASS_GUITAR, 1f, 1f);
						announceString = null;
					}
				}
				else
				{
					getPlugin().GetServerConfig().GameList.add(type);
					announceString = C.cGreen + announceString + " added to rotation.";
				}

				getPlugin().GetGame().Announce(announceString);
				refresh();
			}
		}, itemCount);

		if (enabled)
			addGlow(slot);
	}
}
