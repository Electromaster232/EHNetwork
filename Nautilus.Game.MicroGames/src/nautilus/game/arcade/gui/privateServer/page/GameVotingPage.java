package nautilus.game.arcade.gui.privateServer.page;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.game.GameCategory;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.ShopItem;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.gui.privateServer.PrivateServerShop;

/**
 * Created by WilliamTiger.
 * All the code and any API's associated with it
 * are not to be used anywhere else without written
 * consent of William Burns. 2015.
 * 24/07/15
 */
public class GameVotingPage extends BasePage
{
	public GameVotingPage(ArcadeManager plugin, PrivateServerShop shop, Player player)
	{
		super(plugin, shop, "Game Voting Menu", player, 54);
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		boolean host = getPlugin().GetGameHostManager().isHost(getPlayer());

		if (host)
		{
			addBackButton(4);
			addStartVoteButton(0);
			addEndVoteButton(8);
		}
		else
		{
			addCloseButton(4);
		}

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

	private void addCloseButton(int slot)
	{
		ShopItem item = new ShopItem(Material.BED, (byte)0, "§cClose Menu", new String[]{}, 1, false, false);
		addButton(slot, item, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				player.closeInventory();
			}
		});
	}

	private void addEndVoteButton(int slot)
	{
		ShopItem item = new ShopItem(Material.REDSTONE_BLOCK, (byte)0, "§c§lEnd Vote", new String[]{}, 1, false, false);
		addButton(slot, item, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				if (!_manager.isHost(player)) //Double Check...
					return;

				getPlugin().GetGameHostManager().setVoteInProgress(false);
				for (Player p : UtilServer.getPlayers())
				{
					UtilPlayer.message(p, F.main("Vote", "The vote has ended!"));
				}
				refresh();
			}
		});
	}

	private void addStartVoteButton(int slot)
	{
		String warning = "§c§lWARNING: §fThis will reset current votes!";
		ShopItem item = new ShopItem(Material.EMERALD_BLOCK, (byte)0, "Start Vote", new String[]{warning}, 1, false, false);
		addButton(slot, item, new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				if (!_manager.isHost(player)) //Double Check...
					return;

				getPlugin().GetGameHostManager().setVoteInProgress(true);
				getPlugin().GetGameHostManager().getVotes().clear();
				for (Player p : UtilServer.getPlayers())
				{
					UtilPlayer.message(p, F.main("Vote", "A vote has started! Use " + F.skill("/vote") + " to vote."));
					p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
				}
				refresh();
			}
		});
	}

	private void addGameButton(int slot, final GameType type)
	{
		String click = "§7Click to vote for this Game Type";
		int votes = 0;
		for (GameType cur : getPlugin().GetGameHostManager().getVotes().values())
		{
			if (cur.equals(type))
				votes++;
		}
		String curVotes = "§7Votes: §e" + votes;
		String[] lore = new String[]{click, curVotes};
		if (_manager.hasWarning().contains(type))
		{
			lore = new String[]{click, curVotes, "§1", "§c§lWARNING: §fThis game was rejected!"};
		}
		ShopItem item = new ShopItem(type.GetMaterial(), type.GetMaterialData(), type.GetName(), new String[]{click, curVotes}, 1, false, false);
		if (votes >= 1)
		{
			addButton(slot, item, new IButton()
			{
				@Override
				public void onClick(Player player, ClickType clickType)
				{
					getPlugin().GetGameHostManager().getVotes().put(player.getName(), type);
					refresh();
				}
			});
			return;
		}
		else
		{
			addButtonFakeCount(slot, item, new IButton()
			{
				@Override
				public void onClick(Player player, ClickType clickType)
				{
					getPlugin().GetGameHostManager().getVotes().put(player.getName(), type);
					refresh();
				}
			}, votes);
			return;
		}
	}
}
