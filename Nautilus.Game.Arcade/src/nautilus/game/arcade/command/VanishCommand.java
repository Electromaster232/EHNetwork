package nautilus.game.arcade.command;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.visibility.VisibilityManager;
import nautilus.game.arcade.ArcadeManager;

public class VanishCommand extends CommandBase<ArcadeManager>
{
	private HashSet<Player> _hiddenPlayers = new HashSet<Player>();
	public VanishCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.YOUTUBE, Rank.TWITCH, Rank.JNR_DEV}, "vanish");
	}

	public void addHiddenPlayer(Player player)
	{
		_hiddenPlayers.add(player);
	}
	public void removeHiddenPlayer(Player player)
	{
		_hiddenPlayers.remove(player);
	}

	@EventHandler
	public void removeHiddenPlayerOnQuit(PlayerQuitEvent event)
	{
		_hiddenPlayers.remove(event.getPlayer());
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean hideMe = _hiddenPlayers.contains(caller);
		if(hideMe){
			removeHiddenPlayer(caller);
			UtilPlayer.message(caller, C.cBlue + "Vanish> " + C.cGray + "You are now " + C.cRed + "no longer hidden.");
		}
		else{
			addHiddenPlayer(caller);
			UtilPlayer.message(caller, C.cBlue + "Vanish> " + C.cGray + "You are now " + C.cGreen + "hidden.");
		}
		for (Player other : UtilServer.getPlayers())
			{
				if (caller.equals(other))
					continue;

				if (hideMe)
				{
					VisibilityManager.Instance.setVisibility(caller, true, other);
				}
				else
				{
					VisibilityManager.Instance.setVisibility(caller, false, other);
				}
			}
	}
}