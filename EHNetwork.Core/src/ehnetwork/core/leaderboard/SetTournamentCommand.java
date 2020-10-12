package ehnetwork.core.leaderboard;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;

public class SetTournamentCommand extends CommandBase<LeaderboardManager>
{
	public SetTournamentCommand(LeaderboardManager plugin)
	{
		super(plugin, Rank.ADMIN, "settournament", "set-tournament");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		// TODO: Implement set tournament command.
		/*if (args.length == 3)
		{
			String statType = args[0];
			int gamemode = Integer.parseInt(args[1]);
			int value = Integer.parseInt(args[2]);
			LeaderboardManager.getInstance().attemptStatEvent(caller, statType, gamemode, value);
		}*/
	}
}