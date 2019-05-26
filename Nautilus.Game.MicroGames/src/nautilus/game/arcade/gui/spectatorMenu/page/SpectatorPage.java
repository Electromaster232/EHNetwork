package nautilus.game.arcade.gui.spectatorMenu.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilColor;
import mineplex.core.common.util.UtilMath;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.addons.CompassAddon;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.gui.spectatorMenu.SpectatorShop;
import nautilus.game.arcade.gui.spectatorMenu.button.SpectatorButton;

/**
 * Created by shaun on 14-09-24.
 */

public class SpectatorPage extends ShopPageBase<CompassAddon, SpectatorShop>
{
	private ArcadeManager _arcadeManager;

	public SpectatorPage(CompassAddon plugin, ArcadeManager arcadeManager, SpectatorShop shop, CoreClientManager clientManager,
			DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Spectator Menu", player);
		_arcadeManager = arcadeManager;
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		int playerCount = _arcadeManager.GetGame().GetPlayers(true).size();
		List<GameTeam> teamList = _arcadeManager.GetGame().GetTeamList();

		if (teamList.size() == 1 && playerCount < 28)
			buildSingleTeam(teamList.get(0), playerCount);
		else
			buildMultipleTeams(teamList, playerCount);

	}

	private void buildSingleTeam(GameTeam team, int playerCount)
	{
		setItem(13, getTeamItem(team, playerCount));

		ArrayList<Player> players = team.GetPlayers(true);

		Collections.sort(players, new Comparator<Player>()
		{

			@Override
			public int compare(Player o1, Player o2)
			{
				return o1.getName().compareToIgnoreCase(o2.getName());
			}

		});

		int slot = 19;

		for (Player other : players)
		{
			addPlayerItem(slot, team, other);

			if ((slot + 2) % 9 == 0)
				slot += 3;
			else
				slot++;
		}
	}

	private void buildMultipleTeams(List<GameTeam> teamList, int playerCount)
	{
		int currentRow = 0;

		for (GameTeam team : teamList)
		{
			ArrayList<Player> teamPlayers = team.GetPlayers(true);
			int rowsNeeded = (int) Math.ceil(teamPlayers.size() / 8.0);

			Collections.sort(teamPlayers, new Comparator<Player>()
			{

				@Override
				public int compare(Player o1, Player o2)
				{
					return o1.getName().compareToIgnoreCase(o2.getName());
				}

			});

			for (int row = 0; row < rowsNeeded; row++)
			{
				int woolSlot = (row * 9) + (currentRow * 9);

				// TODO Need to handle too many players in a better way
				if (woolSlot >= getSize())
					continue;

				setItem(woolSlot, getTeamItem(team, teamPlayers.size()));

				int playerIndex = row * 8;
				for (int i = 0; i < 8 && playerIndex < teamPlayers.size(); i++, playerIndex++)
				{
					Player other = teamPlayers.get(playerIndex);
					int slot = woolSlot + 1 + i;

					// TODO Need to handle too many players in a better way
					if (slot >= getSize())
						continue;

					addPlayerItem(slot, team, other);
				}
			}

			// Add a line in between teams if the player count is low enough and there are less than 4 teams
			if (rowsNeeded == 1 && teamList.size() < 4 && playerCount <= 26)
				currentRow += 2;
			else
				currentRow += rowsNeeded;
		}
	}

	private void addPlayerItem(int slot, GameTeam team, Player other)
	{
		ItemStack playerItem = getPlayerItem(team, other);
		ShopItem shopItem = new ShopItem(playerItem, other.getName(), other.getName(), 1, false, false);
		addButton(slot, shopItem, new SpectatorButton(_arcadeManager, getPlayer(), other));
	}

	private ItemStack getTeamItem(GameTeam team, int playerCount)
	{
		ItemStack item = new ItemStack(Material.WOOL, 1, (short) 0, UtilColor.chatColorToWoolData(team.GetColor()));

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(team.GetFormattedName());
		meta.setLore(Arrays.asList(" ", ChatColor.RESET + C.cYellow + "Players Alive: " + C.cWhite + playerCount));
		item.setItemMeta(meta);

		return item;
	}

	private ItemStack getPlayerItem(GameTeam team, Player other)
	{
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

		double distance = UtilMath.offset(getPlayer(), other);
		double heightDifference = other.getLocation().getY() - getPlayer().getLocation().getY();

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.RESET + C.cYellow + "Kit: " + C.cWhite + _arcadeManager.GetGame().GetKit(other).GetName());
		lore.add(ChatColor.RESET + C.cYellow + "Distance: " + C.cWhite + UtilMath.trim(1, distance));
		lore.add(ChatColor.RESET + C.cYellow + "Height Difference: " + C.cWhite + UtilMath.trim(1, heightDifference));
		lore.add(" ");
		lore.add(ChatColor.RESET + C.Line + "Click to Spectate");
		SkullMeta skullMeta = ((SkullMeta) item.getItemMeta());
		skullMeta.setOwner(other.getName());
		skullMeta.setDisplayName(team.GetColor() + other.getName());
		skullMeta.setLore(lore);
		item.setItemMeta(skullMeta);

		return item;
	}

}