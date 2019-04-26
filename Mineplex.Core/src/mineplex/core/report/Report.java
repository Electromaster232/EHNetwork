package mineplex.core.report;

import java.util.HashSet;
import java.util.Set;

import mineplex.serverdata.data.Data;

public class Report implements Data
{
	
	private int _reportId;
	public int getReportId() { return _reportId; }
	
	private String _serverName;
	public String getServerName() { return _serverName; }
	
	private String _playerName;
	public String getPlayerName() { return _playerName; }
	
	// Set of account ids of players who contributed to reporting this player
	private Set<String> _reporters;
	public Set<String> getReporters() { return _reporters; }
	public void addReporter(String reporter) { _reporters.add(reporter); }
	
	/**
	 * Class constructor
	 * @param reportId
	 * @param playerName
	 * @param serverName
	 */
	public Report(int reportId, String playerName, String serverName)
	{
		_reportId = reportId;
		_playerName = playerName;
		_serverName = serverName;
		_reporters = new HashSet<String>();
	}
	
	@Override
	public String getDataId()
	{
		return String.valueOf(_reportId);
	}
}
