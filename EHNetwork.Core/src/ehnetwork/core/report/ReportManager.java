package ehnetwork.core.report;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandCenter;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.report.command.ReportNotification;
import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.Utility;
import ehnetwork.serverdata.data.DataRepository;
import ehnetwork.serverdata.redis.RedisDataRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * ReportManager hooks into a synchronized network-wide report system 
 * with methods for updating/fetching/closing reports in real time.
 * @author Ty
 *
 */
public class ReportManager {
	
	private static ReportManager instance;
	
	// Holds active/open reports in a synchronized database.
	private DataRepository<Report> reportRepository;
	
	private DataRepository<ReportProfile> reportProfiles;
	
	// Stores/logs closed tickets, and various reporter/staff actions.
	private ReportRepository reportSqlRepository;
	
	// A mapping of PlayerName(String) to the ReportId(Integer) for all active reports on this server.
	private Map<String, Integer> activeReports;
	
	/**
	 * Private constructor to prevent non-singleton instances. 
	 */
	private ReportManager()
	{
		this.reportRepository = new RedisDataRepository<Report>(Region.ALL, Report.class, "reports");
		this.reportProfiles = new RedisDataRepository<ReportProfile>(Region.ALL, ReportProfile.class, "reportprofiles");
		this.activeReports = new HashMap<String, Integer>();
		
		// TODO: Get JavaPlugin instance and locate ConnectionString from config?
		this.reportSqlRepository = new ReportRepository(ReportPlugin.getPluginInstance(), "CONNECTION STRING HERE");
		reportSqlRepository.initialize();
	}
	
	public void retrieveReportResult(int reportId, Player reportCloser, String reason)
	{
		// Prompt the report closer with a menu of options to determine the result
		// of the report. When confirmation is received, THEN close report.
	}
	
	public void closeReport(int reportId, Player reportCloser, String reason)
	{
		retrieveReportResult(reportId, reportCloser, reason);
	}
	
	public void closeReport(int reportId, Player reportCloser, String reason,
								ReportResult result)
	{
		if (isActiveReport(reportId))
		{
			Report report = getReport(reportId);
			reportRepository.removeElement(String.valueOf(reportId));	// Remove report from redis database
			removeActiveReport(reportId);
			
			int closerId = getPlayerAccount(reportCloser).getAccountId();
			String playerName = getReport(reportId).getPlayerName();
			int playerId = getPlayerAccount(playerName).getAccountId();
			String server = null;	// TODO: Get current server name
			reportSqlRepository.logReport(reportId, playerId, server, closerId, result, reason);
			
			// Update the reputation/profiles of all reporters on this closing report.
			for (String reporterName : report.getReporters())
			{
				CoreClient reporterAccount = getPlayerAccount(reporterName);
				ReportProfile reportProfile = getReportProfile(String.valueOf(reporterAccount.getAccountId()));
				reportProfile.onReportClose(result);
				reportProfiles.addElement(reportProfile);
			}
			
			if (reportCloser != null)
			{
				// Notify staff that the report was closed.
				sendReportNotification(String.format("[Report %d] %s closed this report. (%s).", reportId, 
											reportCloser.getName(),	result.toDisplayMessage()));
			}
		}
		
	}
	
	public void handleReport(int reportId, Player reportHandler)
	{
		if (reportRepository.elementExists(String.valueOf(reportId)))
		{
			Report report = getReport(reportId);
			Portal.transferPlayer(reportHandler.getName(), report.getServerName());
			String handlerName = reportHandler.getName();
			sendReportNotification(String.format("[Report %d] %s is handling this report.", reportId, handlerName));
			
			// TODO: Send display message to handler when they arrive on the server
			// with info about the case/report.
			
			int handlerId = getPlayerAccount(reportHandler).getAccountId();
			reportSqlRepository.logReportHandling(reportId, handlerId);		// Log handling into sql database
		}
	}
	
	public void reportPlayer(Player reporter, Player reportedPlayer, String reason)
	{
		int reporterId = getPlayerAccount(reporter).getAccountId();
		ReportProfile reportProfile = getReportProfile(String.valueOf(reporterId));
		
		if (reportProfile.canReport())
		{
			Report report = null;
			
			if (hasActiveReport(reportedPlayer))
			{
				int reportId = getActiveReport(reportedPlayer.getName());
				report = getReport(reportId);
				report.addReporter(reporter.getName());
			}
			else
			{
				String serverName = null;	// TODO: Fetch name of current server
				int reportId = generateReportId();
				report = new Report(reportId, reportedPlayer.getName(), serverName);
				report.addReporter(reporter.getName());
				activeReports.put(reportedPlayer.getName().toLowerCase(), report.getReportId());
				reportRepository.addElement(report);
			}
		
			if (report != null)
			{
				// [Report 42] [MrTwiggy +7] [Cheater102 - 5 - Speed hacking]
				String message = String.format("[Report %d] [%s %d] [%s - %d - %s]", report.getReportId(),
									reporter.getName(), reportProfile.getReputation(),
									reportedPlayer.getName(), report.getReporters().size(), reason);
				sendReportNotification(message);
				reportSqlRepository.logReportSending(report.getReportId(), reporterId, reason);
			}	
		}
	}
	
	public void onPlayerQuit(Player player) 
	{
		if (hasActiveReport(player))
		{
			int reportId = getActiveReport(player.getName());
			this.closeReport(reportId, null, "Player Quit", ReportResult.UNDETERMINED);
			// TODO: Handle 'null' report closer in closeReport metohd for NPEs.
			
			sendReportNotification(String.format("[Report %d] %s has left the game.", reportId, player.getName()));
		}
	}
	
	public ReportProfile getReportProfile(String playerName)
	{
		ReportProfile profile = reportProfiles.getElement(playerName);
		
		if (profile == null)
		{
			profile = new ReportProfile(playerName, getAccountId(playerName));
			saveReportProfile(profile);
		}
		
		return profile;
	}
	
	private void saveReportProfile(ReportProfile profile)
	{
		reportProfiles.addElement(profile);
	}
	
	/**
	 * @return a uniquely generated report id.
	 */
	public int generateReportId()
	{
		JedisPool pool = Utility.getPool(true);
		Jedis jedis = pool.getResource();
		long uniqueReportId = -1;
		
		try
		{
			uniqueReportId = jedis.incr("reports.unique-id");
		}
		catch (JedisConnectionException exception)
		{
			exception.printStackTrace();
			pool.returnBrokenResource(jedis);
			jedis = null;
		}
		finally
		{
			if (jedis != null)
			{
				pool.returnResource(jedis);
			}
		}
		
		return (int) uniqueReportId;
	}
	
	public Report getReport(int reportId)
	{
		return reportRepository.getElement(String.valueOf(reportId));
	}
	
	private CoreClient getPlayerAccount(Player player)
	{
		return getPlayerAccount(player.getName());
	}
	
	private CoreClient getPlayerAccount(String playerName)
	{
		return CommandCenter.Instance.GetClientManager().Get(playerName);
	}
	
	private int getAccountId(String playerName)
	{
		return getPlayerAccount(playerName).getAccountId();
	}
	
	/**
	 * @param player - the player whose report notification settings are to be checked
	 * @return true, if the player should receive report notifications, false otherwise.
	 */
	public boolean hasReportNotifications(Player player)
	{
		// If player is not staff, return false.
		// If player is staff but has report notifications pref disabled, return false;
		// Else return true.
		return false;
	}
	
	/**
	 * Send a network-wide {@link ReportNotification} to all online staff.
	 * @param message - the report notification message to send.
	 */
	public void sendReportNotification(String message)
	{
		ReportNotification reportNotification = new ReportNotification(message);
		reportNotification.publish();
	}
	
	/**
	 * @param playerName - the name of the player whose active report id is being fetched
	 * @return the report id for the active report corresponding with playerName, if one
	 * currently exists, -1 otherwise.
	 */
	public int getActiveReport(String playerName)
	{
		if (activeReports.containsKey(playerName.toLowerCase()))
		{
			return activeReports.get(playerName.toLowerCase());
		}
		
		return -1;
	}
	
	public boolean hasActiveReport(Player player)
	{
		return getActiveReport(player.getName()) != -1;
	}
	
	public boolean isActiveReport(int reportId)
	{
		for (Entry<String, Integer> activeReport : activeReports.entrySet())
		{
			if (activeReport.getValue() == reportId)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeActiveReport(int reportId)
	{
		for (Entry<String, Integer> activeReport : activeReports.entrySet())
		{
			if (activeReport.getValue() == reportId)
			{
				activeReports.remove(activeReport.getKey());
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return the singleton instance of {@link ReportManager}.
	 */
	public static ReportManager getInstance() 
	{
		if (instance == null)
		{
			instance = new ReportManager();
		}
		
		return instance;
	}
}
