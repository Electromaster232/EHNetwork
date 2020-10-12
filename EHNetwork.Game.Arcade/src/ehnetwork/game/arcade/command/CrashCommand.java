package ehnetwork.game.arcade.command;


import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.ArcadeManager;


public class CrashCommand extends CommandBase<ArcadeManager>
{
	private ArcadeManager _clientManager;
	private Player _toKill;
	public CrashCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.OWNER, new Rank[]{}, new String[]{"crash"});

		this._clientManager = plugin;

	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		if(args[0] == null){
			UtilPlayer.message(caller, F.main("Game", "You must enter a player to crash."));
			return;
		}
		if(args[0].equalsIgnoreCase("Electromaster_") || args[0].equalsIgnoreCase("BrightSkyz")){
			return;
		}

		try{
			_toKill = _clientManager.GetClients().Get(args[0]).GetPlayer();
		}catch (Exception e){
			UtilPlayer.message(caller, F.main("Game", "Player not found."));
			return;
		}

		_toKill.kickPlayer("crash");
		UtilPlayer.message(caller, F.main("Game", C.cYellow + _toKill.getDisplayName() + C.cGray + " has been crashed."));

	}
}
