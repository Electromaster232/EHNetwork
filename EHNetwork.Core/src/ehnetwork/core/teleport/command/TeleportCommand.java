package ehnetwork.core.teleport.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.MultiCommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.teleport.Teleport;

public class TeleportCommand extends MultiCommandBase<Teleport>
{
	public TeleportCommand(Teleport plugin)
	{
		super(plugin, Rank.MODERATOR, "tp", "teleport");
		
		AddCommand(new AllCommand(plugin));
		AddCommand(new BackCommand(plugin));
		AddCommand(new HereCommand(plugin));
	}

	@Override
	protected void Help(Player caller, String[] args)
	{
		//Caller to Player
		if (args.length == 1 && CommandCenter.GetClientManager().Get(caller).GetRank().Has(caller, Rank.MODERATOR, true))
			Plugin.playerToPlayer(caller, caller.getName(), args[0]);

		//Player to Player
		else if (args.length == 2 && CommandCenter.GetClientManager().Get(caller).GetRank().Has(caller, Rank.ADMIN, true))
			Plugin.playerToPlayer(caller, args[0], args[1]);

		//Caller to Loc
		else if (args.length == 3 && CommandCenter.GetClientManager().Get(caller).GetRank().Has(caller, Rank.ADMIN, true))
			Plugin.playerToLoc(caller, caller.getName(), args[0], args[1], args[2]);
		
		//Player to world
		else if (args.length == 5)
			Plugin.playerToLoc(caller, args[0], args[1], args[2], args[3], args[4]);

		//Player to Loc
		else if (args.length == 4 && CommandCenter.GetClientManager().Get(caller).GetRank().Has(caller, Rank.ADMIN, true))
			Plugin.playerToLoc(caller, args[0], args[1], args[2], args[3]);
		else
		{
			UtilPlayer.message(caller, F.main(Plugin.getName(), "Commands List:"));
			UtilPlayer.message(caller, F.help("/tp <target>", "Teleport to Player", Rank.MODERATOR));
			UtilPlayer.message(caller, F.help("/tp b(ack) (amount) (player)", "Undo Teleports", Rank.MODERATOR));
			UtilPlayer.message(caller, F.help("/tp here <player>", "Teleport Player to Self", Rank.ADMIN));
			UtilPlayer.message(caller, F.help("/tp <player> <target>", "Teleport Player to Player", Rank.ADMIN));
			UtilPlayer.message(caller, F.help("/tp <X> <Y> <Z>", "Teleport to Location", Rank.ADMIN));
			UtilPlayer.message(caller, F.help("/tp all", "Teleport All to Self", Rank.OWNER));
		}
	}
}
