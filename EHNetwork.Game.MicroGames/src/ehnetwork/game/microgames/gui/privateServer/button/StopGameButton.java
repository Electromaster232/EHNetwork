package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game;

public class StopGameButton implements IButton
{
	private MicroGamesManager _microGamesManager;

	public StopGameButton(MicroGamesManager microGamesManager)
	{
		_microGamesManager = microGamesManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		if (_microGamesManager.GetGame().GetState() == Game.GameState.End || _microGamesManager.GetGame().GetState() == Game.GameState.End)
		{
			player.sendMessage("Game is already ending..."); 
			return;
		}
		else if (_microGamesManager.GetGame().GetState() == Game.GameState.Recruit)
		{
			_microGamesManager.GetGame().SetState(Game.GameState.Dead);
		}
		else
		{
			_microGamesManager.GetGame().SetState(Game.GameState.End);
		}


		_microGamesManager.GetGame().Announce(C.cAqua + C.Bold + player.getName() + " has stopped the game.");
	}
}
