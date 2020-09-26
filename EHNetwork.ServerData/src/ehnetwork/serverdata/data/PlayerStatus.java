package ehnetwork.serverdata.data;

public class PlayerStatus implements Data
{
	// The name of this server.
	private String _name;
	public String getName() { return _name; }
	
	// The current message of the day (MOTD) of the server.
	private String _server;
	public String getServer() { return _server; }
	
	/**
	 * Class constructor
	 * @param name
	 * @param server
	 */
	public PlayerStatus(String name, String server)
	{
		_name = name;
		_server = server;
	}
	
	/**
	 * Unique identifying String ID associated with this {@link PlayerStatus}.
	 */
	public String getDataId()
	{
		return _name;
	}
}
