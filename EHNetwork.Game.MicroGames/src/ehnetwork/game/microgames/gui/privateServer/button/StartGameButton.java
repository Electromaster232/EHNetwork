package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game;

public class StartGameButton implements IButton
{
	private MicroGamesManager _microGamesManager;

	public StartGameButton(MicroGamesManager microGamesManager)
	{
		_microGamesManager = microGamesManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		if (_microGamesManager.GetGame().GetState() != Game.GameState.Recruit)
		{
			player.sendMessage("Game is already in progress...");
			return;
		}

		_microGamesManager.GetGameManager().StateCountdown(_microGamesManager.GetGame(), 20, true);

		_microGamesManager.GetGame().Announce(C.cAqua + C.Bold + player.getName() + " has started the game.");
	}
}
