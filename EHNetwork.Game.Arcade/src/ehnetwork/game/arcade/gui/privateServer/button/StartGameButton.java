package ehnetwork.game.arcade.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game;

public class StartGameButton implements IButton
{
	private ArcadeManager _arcadeManager;

	public StartGameButton(ArcadeManager arcadeManager)
	{
		_arcadeManager = arcadeManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		if (_arcadeManager.GetGame().GetState() != Game.GameState.Recruit)
		{
			player.sendMessage("Game is already in progress...");
			return;
		}

		_arcadeManager.GetGameManager().StateCountdown(_arcadeManager.GetGame(), 20, true);

		_arcadeManager.GetGame().Announce(C.cAqua + C.Bold + player.getName() + " has started the game.");
	}
}
