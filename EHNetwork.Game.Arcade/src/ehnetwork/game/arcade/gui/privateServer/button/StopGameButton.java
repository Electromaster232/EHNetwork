package ehnetwork.game.arcade.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game;

public class StopGameButton implements IButton
{
	private ArcadeManager _arcadeManager;

	public StopGameButton(ArcadeManager arcadeManager)
	{
		_arcadeManager = arcadeManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		if (_arcadeManager.GetGame().GetState() == Game.GameState.End || _arcadeManager.GetGame().GetState() == Game.GameState.End)
		{
			player.sendMessage("Game is already ending..."); 
			return;
		}
		else if (_arcadeManager.GetGame().GetState() == Game.GameState.Recruit)
		{
			_arcadeManager.GetGame().SetState(Game.GameState.Dead);
		}
		else
		{
			_arcadeManager.GetGame().SetState(Game.GameState.End);
		}


		_arcadeManager.GetGame().Announce(C.cAqua + C.Bold + player.getName() + " has stopped the game.");
	}
}
