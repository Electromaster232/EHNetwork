package ehnetwork.game.arcade.gui.privateServer.page;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.gui.privateServer.PrivateServerShop;

public abstract class PlayerPage extends BasePage
{
	public PlayerPage(ArcadeManager plugin, PrivateServerShop shop, String pageName, Player player)
	{
		super(plugin, shop, pageName, player);
	}

	@Override
	protected void buildPage()
	{
		addBackButton(4);

		Player[] players = UtilServer.getPlayers();

		int slot = 9;
		for (Player player : players)
		{
			if (showPlayer(player))
			{
				ItemStack head = getPlayerHead(player.getName(), C.cGreen + C.Bold + player.getName(), new String[]{ ChatColor.RESET + C.cGray + getDisplayString(player) });
				addButton(slot, head, new Button(slot, player));

				slot++;
			}
		}
	}

	public abstract boolean showPlayer(Player player);

	public abstract void clicked(int slot, Player player);

	public abstract String getDisplayString(Player player);

	private class Button implements IButton
	{
		private int _slot;
		private Player _player;

		public Button(int slot, Player player)
		{
			_slot = slot;
			_player = player;
		}

		@Override
		public void onClick(Player player, ClickType clickType)
		{
			clicked(_slot, _player);
		}
	}


}
