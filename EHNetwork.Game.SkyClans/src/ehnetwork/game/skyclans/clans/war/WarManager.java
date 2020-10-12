package ehnetwork.game.skyclans.clans.war;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.skyclans.clans.ClanInfo;
import ehnetwork.game.skyclans.clans.ClansManager;

public class WarManager extends MiniPlugin
{
	private static final int INVADE_ENEMY_DATE = Calendar.SATURDAY;
	private static final int CREATE_ENEMY_DATE = Calendar.SUNDAY;

	private final ClansManager _clansManager;

	private WarState _warState;

	public WarManager(JavaPlugin plugin, ClansManager clansManager)
	{
		super("War Manager", plugin);
		_clansManager = clansManager;
		_warState = calculateWarState();
	}

	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOWEST)
			return;

		WarState currentState = calculateWarState();

		if (currentState != _warState)
		{
			WarStateChangeEvent warEvent = new WarStateChangeEvent(_warState, currentState);
			UtilServer.getServer().getPluginManager().callEvent(warEvent);
			_warState = currentState;
		}
	}

	private WarState calculateWarState()
	{
		// Get a calendar snapshot of the current time using server timezone
		Calendar calendar = Calendar.getInstance(_clansManager.getServerTimeZone());

		WarState warState = WarState.WAR;

		if (isEnemyTime(calendar))
			warState = WarState.FORM_ENEMIES;
		else if (isInvadeTime(calendar))
			warState = WarState.INVADE;

		return warState;
	}

	public void attemptEnemy(Player player, ClanInfo initiatingClan, ClanInfo otherClan)
	{
		attemptEnemy(player, initiatingClan, otherClan, true);
	}

	public void attemptEnemy(Player player, ClanInfo initiatingClan, ClanInfo otherClan, boolean notify)
	{
		String notifyMessage = null;

		if (_warState != WarState.FORM_ENEMIES)
		{
			notifyMessage = "Enemies cannot be formed at this time. Please see mineplex.com/clans for info";
		}
		else if (initiatingClan.getEnemyData() != null)
		{
			notifyMessage = "Your clan already has an enemy.";
		}
		else if (otherClan.getEnemyData() != null)
		{
			notifyMessage = "The clan " + F.elem(otherClan.getName()) + " already has an enemy.";
		}
		else
		{
			_clansManager.getClanDataAccess().enemy(initiatingClan, otherClan, player.getName());
			_clansManager.messageClan(initiatingClan, F.main("Clans", "Your clan is now enemies with " + F.elem(otherClan.getName())));
			_clansManager.messageClan(otherClan, F.main("Clans", "Your clan is now enemies with " + F.elem(initiatingClan.getName())));
		}

		if (notify && notifyMessage != null)
			UtilPlayer.message(player, F.main("Clans", notifyMessage));
	}

	@EventHandler
	public void handleDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		Player killer = player.getKiller();

		ClanInfo playerClan = _clansManager.getClan(player);
		ClanInfo killerClan = _clansManager.getClan(killer);

		if (playerClan != null && killerClan != null)
		{
			if (playerClan.getEnemyData() != null && playerClan.getEnemyData().getEnemyName().equalsIgnoreCase(killerClan.getName()))
			{
				// Only adjust score if we are in WAR or FORM ENEMIES state. Once invasion begins score should never change
				// Adjust kill stats no matter what war state we are in (so we track kills during invasion)

				if (_warState == WarState.WAR || _warState == WarState.FORM_ENEMIES)
				{
					playerClan.getEnemyData().addScore(-1);
					killerClan.getEnemyData().addScore(1);
				}

				killerClan.getEnemyData().addKill();
			}
		}

	}


	/**
	 * Check if a specific Calendar is currently in enemy time
	 * If this returns true, isInvadeTime should always return false for the same Calendar
	 *
	 * @param c {@link java.util.Calendar} instance that should be checked
	 */
	private boolean isEnemyTime(Calendar c)
	{
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek == CREATE_ENEMY_DATE)
		{
			return true;
		}

		return false;
	}

	/**
	 * Check if a specific Calendar is currently in invade time
	 * If this returns true, isEnemyTime should always return false for the same Calendar
	 *
	 * @param c {@link java.util.Calendar} instance that should be checked
	 */
	private boolean isInvadeTime(Calendar c)
	{
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek == INVADE_ENEMY_DATE)
		{
			return true;
		}
		return false;
	}

	/**
	 * Get the starting time of when enemies can be formed next
	 * @return The enemy start time in the form of {@link java.util.Date}
	 */
	private Date getNextEnemyTime()
	{
		Calendar c = Calendar.getInstance(_clansManager.getServerTimeZone());
		int currDayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		c.set(Calendar.DAY_OF_WEEK, CREATE_ENEMY_DATE);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		if (currDayOfWeek >= CREATE_ENEMY_DATE)
			c.add(Calendar.DATE, 7);

		return c.getTime();
	}

	/**
	 * Get the starting time of the next enemy invasion
	 * @return The invasion start time in the form of {@link java.util.Date}
	 */
	private Date getNextInvadeTime()
	{
		Calendar c = Calendar.getInstance(_clansManager.getServerTimeZone());
		int currDayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		c.set(Calendar.DAY_OF_WEEK, INVADE_ENEMY_DATE);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		if (currDayOfWeek >= INVADE_ENEMY_DATE)
			c.add(Calendar.DATE, 7);

		return c.getTime();
	}

	/**
	 * Get the current War State of the server
	 * War state determines what events are going on
	 * with respect to the current war.
	 */
	public WarState getWarState()
	{
		return _warState;
	}

	@EventHandler
	public void stateChange(WarStateChangeEvent event)
	{
		WarState state = event.getNewState();
		Bukkit.broadcastMessage(F.main("Clans", "War state changed: " + F.elem(state.getDescription())));
	}

	/**
	 * Send the current server time information to the player
	 */
	public LinkedList<String> mServerTime()
	{
		LinkedList<String> messageList = new LinkedList<String>();

		messageList.add(F.main("Clans", "Server Time"));
		Date currDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
		dateFormat.setTimeZone(_clansManager.getServerTimeZone());

		messageList.add(F.value("Server Time", dateFormat.format(currDate)));

		messageList.add(F.value("Status", _warState.getDescription()));

		if (_warState != WarState.INVADE)
		{
			long next = getNextInvadeTime().getTime();
			long currTime = System.currentTimeMillis();

			messageList.add(F.value("Invasion", UtilTime.convertString(next - currTime, 1, UtilTime.TimeUnit.FIT)));
		}

		if (_warState != WarState.FORM_ENEMIES)
		{
			long next = getNextEnemyTime().getTime();
			long currTime = System.currentTimeMillis();

			messageList.add(F.value("Enemy Reset", UtilTime.convertString(next - currTime, 1, UtilTime.TimeUnit.FIT)));
		}

		return messageList;
	}
}
