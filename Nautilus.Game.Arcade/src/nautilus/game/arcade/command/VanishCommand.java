package nautilus.game.arcade.command;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerEvent;
import static org.bukkit.Bukkit.getServer;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.visibility.VisibilityManager;
import nautilus.game.arcade.ArcadeManager;

// 4/27/19 (DJ Electro)
public class VanishCommand extends CommandBase<ArcadeManager> implements Listener
{
	private ArcadeManager _clientManager;
	private HashSet<Player> _hiddenPlayers = new HashSet<Player>();
	public VanishCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.MODERATOR, new Rank[] {Rank.YOUTUBE, Rank.TWITCH}, new String[] {"vanish", "v"});
		getServer().getPluginManager().registerEvents(this, plugin.getPlugin());
		_clientManager = plugin;

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

	@EventHandler
	public void gameTickEvent(ServerEvent event)
	{
		for (Player toHide : _hiddenPlayers){
			for (Player other : UtilServer.getPlayers()){
				Rank rank = _clientManager.GetClients().Get(other).GetRank();
				if(rank == Rank.ADMIN || rank == Rank.OWNER){
					continue;
				}
				VisibilityManager.Instance.setVisibility(toHide, false, other);
			}

		}
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean hideMe = _hiddenPlayers.contains(caller);
		if(hideMe){
			removeHiddenPlayer(caller);
			UtilPlayer.message(caller, C.cBlue + "Vanish> " + C.cGray + "You are " + C.cRed + "no longer hidden.");
		}
		else{
			addHiddenPlayer(caller);
			UtilPlayer.message(caller, C.cBlue + "Vanish> " + C.cGray + "You are now " + C.cGreen + "hidden.");
		}
		for (Player other : UtilServer.getPlayers())
			{
				if (caller.equals(other))
					continue;


				Rank rank = _clientManager.GetClients().Get(other).GetRank();
				if(rank == Rank.ADMIN || rank == Rank.OWNER){
					continue;
				}

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
