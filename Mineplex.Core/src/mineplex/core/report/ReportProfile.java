package mineplex.core.report;

import mineplex.serverdata.data.Data;

public class ReportProfile implements Data
{
	
	private String _playerName;
	
	private int _playerId;
	
	private int _totalReports;
	
	private int _successfulReports;
	
	private int _reputation;
	public int getReputation() { return _reputation; }
	
	private boolean _banned;
	
	public ReportProfile(String playerName, int playerId)
	{
		_playerName = playerName;
		_playerId = playerId;
		_totalReports = 0;
		_successfulReports = 0;
		_reputation = 0;
		_banned = false;
	}
	
	@Override
	public String getDataId()
	{
		return String.valueOf(_playerId);
	}
	
	public boolean canReport()
	{
		return !_banned;
	}
	
	/**
	 * Called when a report made by this player is closed.
	 * @param result - the result of the closed report.
	 */
	public void onReportClose(ReportResult result)
	{
		_totalReports++;
		
		if (result == ReportResult.MUTED || result == ReportResult.BANNED)
		{
			_successfulReports++;
			_reputation++;
		}
		else if (result == ReportResult.ABUSE)
		{
			_reputation = -1;
			_banned = true;
		}
	}
}