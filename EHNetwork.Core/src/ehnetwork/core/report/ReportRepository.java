package ehnetwork.core.report;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.column.ColumnInt;
import ehnetwork.core.database.column.ColumnVarChar;

public class ReportRepository extends RepositoryBase
{	
	/*
	 * *ReportTicket
id, date, accountId reported player, server, accountId of staff who closed, result, reason

ReportSenders
id, date, reportId, accountId of Reporter, Reason for report

ReportHandlers
id, date, reportId, accountId of Staff

This will be used to determine if staff are handling
	 */
	
	private static String CREATE_TICKET_TABLE = "CREATE TABLE IF NOT EXISTS reportTickets (reportId INT NOT NULL, date LONG, eventDate LONG, playerId INT NOT NULL, server VARCHAR(50), closerId INT NOT NULL, result VARCHAR(25), reason VARCHAR(100), PRIMARY KEY (reportId), INDEX playerIdIndex (playerId), INDEX closerIdIndex (closerId));";
	private static String CREATE_HANDLER_TABLE = "CREATE TABLE IF NOT EXISTS reportHandlers (id INT NOT NULL AUTO_INCREMENT, reportId INT NOT NULL, eventDate LONG, handlerId INT NOT NULL, PRIMARY KEY (id), INDEX handlerIdIndex (handlerId) );";
	private static String CREATE_REPORTERS_TABLE = "CREATE TABLE IF NOT EXISTS reportSenders (id INT NOT NULL AUTO_INCREMENT, reportId INT NOT NULL, eventDate LONG, reporterId INT NOT NULL, reason VARCHAR(100), PRIMARY KEY (id), INDEX reporterIdIndex (reporterId));";
	
	private static String INSERT_TICKET = "INSERT INTO reportTickets (reportId, eventDate, playerId, server, closerId, result, reason) VALUES (?, now(), ?, ?, ?, ?, ?);";
	private static String INSERT_HANDLER = "INSERT INTO reportHandlers (eventDate, reportId, handlerId) VALUES(now(), ?, ?);";
	private static String INSERT_SENDER = "INSERT INTO reportSenders (eventDate, reportId, reporterId, reason) VALUES(now(), ?, ?, ?);";

	public ReportRepository(JavaPlugin plugin, String connectionString)
	{
		super(plugin, DBPool.ACCOUNT);
	}
	
	@Override
	protected void initialize()
	{
		executeUpdate(CREATE_TICKET_TABLE);
		executeUpdate(CREATE_HANDLER_TABLE);
		executeUpdate(CREATE_REPORTERS_TABLE);
	}

	@Override
	protected void update()
	{
		
	}
	
	public void logReportHandling(int reportId, int handlerId)
	{
		executeUpdate(INSERT_HANDLER, new ColumnInt("reportId", reportId), new ColumnInt("handlerId", handlerId));
	}
	
	public void logReportSending(int reportId, int reporterId, String reason)
	{
		executeUpdate(INSERT_SENDER, new ColumnInt("reportId", reportId), new ColumnInt("reporterId", reporterId), 
							new ColumnVarChar("reason", 100, reason));
	}
	
	public void logReport(int reportId, int playerId, String server, int closerId, ReportResult result, String reason)
	{
		executeUpdate(INSERT_TICKET, new ColumnInt("reportId", reportId), new ColumnInt("playerId", playerId),
							new ColumnVarChar("server", 50, server), new ColumnInt("closerId", closerId),
							new ColumnVarChar("result", 25, result.toString()), new ColumnVarChar("reason", 100, reason));
	}
	
}