package ehnetwork.hub.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.hub.server.ServerManager;

public class ServerNPCCommand extends CommandBase<ServerManager>
{

	private ServerManager _serverManager;
	public ServerNPCCommand(ServerManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {}, new String[] {"servernpc"});

		_serverManager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if(args == null || args.length < 1){
			Help(caller);
			return;
		}
		if(args[0].equalsIgnoreCase("addserver")){
			if(args[1] == null || args[2] == null){
				UtilPlayer.message(caller, F.main("Server NPC Manager", "You did not enter all the required arguments."));
				return;
			}
			_serverManager.AddServerNpc(args[1], args[2]);
			UtilPlayer.message(caller, F.main("Server NPC Manager", "Server added to NPC successfully."));
		}
		else if(args[0].equalsIgnoreCase("removeserver")){
			if(args.length < 2){
				UtilPlayer.message(caller, F.main("Server NPC Manager", "You did not enter all the required arguments."));
				return;
			}
			_serverManager.RemoveServerNpc(args[1]);
			UtilPlayer.message(caller, F.main("Server NPC Manager", "Server removed from NPC successfully."));
		}
		else if(args[0].equalsIgnoreCase("listnpcs")){
			_serverManager.ListServerNpcs(caller);
		}
		else if(args[0].equalsIgnoreCase("listservers")){
			if(args.length < 2){
				UtilPlayer.message(caller, F.main("Server NPC Manager", "You did not enter all the required arguments."));
				return;
			}
			_serverManager.ListServers(caller, args[1]);
		}
		else if(args[0].equalsIgnoreCase("listoffline")){
			_serverManager.ListOfflineServers(caller);
		}

	}

	public void Help(Player caller)
	{
		UtilPlayer.message(caller, F.main("Server NPC Manager", "Commands List:"));
		UtilPlayer.message(caller, F.help("/servernpc addserver <servernpc> | <name>", "Adds server.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/servernpc removeserver <servernpc>", "Removes server.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/servernpc listnpcs", "Lists all server npcs.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/servernpc listservers <servernpc>", "Lists all servers for an NPC.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/servernpc listoffline", "Shows all servers offline.", Rank.ADMIN));
		UtilPlayer.message(caller, F.main("Tip", "Use "+ C.cGreen + "/npc" + C.cGray + " to create NPCs for this command!"));

	}

}
