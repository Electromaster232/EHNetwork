package mineplex.core.leaderboard;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.spawn.command.SpawnCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages dynamic Leaderboard statistics.
 * 
 * Used for recording stat events, retrieving customized leaderboards, etc.
 * @author MrTwiggy
 *
 */
public class LeaderboardManager extends MiniPlugin
{
	
	private static LeaderboardManager _instance;	// Singleton instance of Leaderboard Manager
	private StatEventsRepository _statEvents;		// 'statEvents' table repository.
	private CoreClientManager _clientManager;	
	private String _serverGroup;	
	
	/**
	 * Private class constructor to prevent non-singleton instances.
	 */
	public LeaderboardManager(JavaPlugin plugin, CoreClientManager clientManager)
	{
		super("Leaderboard Manager", plugin);
		
		_instance = this;
		_clientManager = clientManager;
		_statEvents = new StatEventsRepository(plugin);
		_serverGroup = _plugin.getConfig().getString("serverstatus.group");
	}
	
	/**
	 * Attempt to trigger a stat event.
	 * @param player - the player responsible for the statistic
	 * @param stat - the display name of the statistic to be added
	 * @param value - the counter value used to increment the statistic
	 * @return true, if a stat event was successfully triggered and logged, false otherwise.
	 */
	public boolean attemptStatEvent(Player player, String stat, int gamemode, int value)
	{
		StatType type = StatType.getType(stat);			
		
		return (type == null) ? false : onStatEvent(player, type, gamemode, value);
	}
	
	/**
	 * Trigger a stat event to be recorded.
	 * @param player - the player responsible for the statistic
	 * @param type - the unique type id designating the statistic being recorded
	 * @param gamemode - the unique gamemode id associated with the stat event
	 * @param value - the counter value used to increment the statistic
	 * @return true, if the stat event was successfully triggered and logged, false otherwise.
	 */
	public boolean onStatEvent(final Player player, final StatType type, final int gamemode, final int value)
	{
		/*
		// Asynchronously make DB call to insert stat event.
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				_statEvents.insertStatEvent(player.getName(), gamemode, _serverGroup, type.getTypeId(), value);
			}
		});
		*/
		
		return true;
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new SetTournamentCommand(this));
	}
	
	/**
	 * @return the singleton instance for {@link LeaderboardManager}.
	 */
	public static LeaderboardManager getInstance()
	{
		return _instance;
	}
}
