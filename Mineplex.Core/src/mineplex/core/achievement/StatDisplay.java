package mineplex.core.achievement;

/**
 * The purpose of extracting stats to this class is so we can display stats that are a combination
 * of different stat values. For example, since we don't have a specific stat for games played of a game,
 * we can use this class to display the stat "Games Played" that sums up "Wins" and "Losses"
 * See: StatDisplay.GAMES_PLAYED
 */
public class StatDisplay
{
	public String _displayName;
	public String[] _stats;

	public StatDisplay(String stat)
	{
		_displayName = stat;
		_stats = new String[] { stat };
	}

	public StatDisplay(String displayName, String... stats)
	{
		_displayName = displayName;
		_stats = stats;
	}

	public String getDisplayName()
	{
		return _displayName;
	}

	public String[] getStats()
	{
		return _stats;
	}

	public static final StatDisplay WINS = new StatDisplay("Wins");
	public static final StatDisplay LOSSES = new StatDisplay("Losses");
	public static final StatDisplay KILLS = new StatDisplay("Kills");
	public static final StatDisplay DEATHS = new StatDisplay("Deaths");
	public static final StatDisplay GEMS_EARNED = new StatDisplay("Gems Earned", "GemsEarned");
	public static final StatDisplay TIME_IN_GAME = new StatDisplay("Time In Game", "TimeInGame");
	public static final StatDisplay GAMES_PLAYED = new StatDisplay("Games Played", "Wins", "Losses");

}
