package mineplex.serverdata.data;

public class MinecraftServer 
{
	// The name of this server.
	private String _name;
	public String getName() { return _name; }

	// The ServerGroup that this MinecraftServer belongs to.
	private String _group;
	public String getGroup() { return _group; }
	
	// The current message of the day (MOTD) of the server.
	private String _motd;
	public String getMotd() { return _motd; }
	
	// The number of players currently online.
	private int _playerCount;
	public int getPlayerCount() { return _playerCount; }
	public void incrementPlayerCount(int amount) { this._playerCount += amount; }
	
	// The maximum number of players allowed on the server.
	private int _maxPlayerCount;
	public int getMaxPlayerCount() { return _maxPlayerCount; }
	
	// The ticks per second (TPS) of the server.
	private int _tps;
	public int getTps() { return _tps; }
	
	// The current amount of RAM allocated to the server.
	private int _ram;
	public int getRam() { return _ram; }
	
	// The maximum amount of available RAM that can be allocated to the server.
	private int _maxRam;
	public int getMaxRam() { return _maxRam; }
	
	// The public I.P address used by players to connect to the server.
	private String _publicAddress;
	public String getPublicAddress() { return _publicAddress; }
	
	// The port the server is currently running/listening on.
	private int _port;
	public int getPort() { return _port; }
	
	private long _startUpDate;
	
	/**
	 * Class constructor
	 * @param name
	 * @param group
	 * @param motd
	 * @param publicAddress
	 * @param port
	 * @param playerCount
	 * @param maxPlayerCount
	 * @param tps
	 * @param ram
	 * @param maxRam
	 */
	public MinecraftServer(String name, String group, String motd, String publicAddress, int port,
							int playerCount, int maxPlayerCount, int tps, int ram, int maxRam, long startUpDate)
	{
		this._name = name;
		this._group = group;
		this._motd = motd;
		this._playerCount = playerCount;
		this._maxPlayerCount = maxPlayerCount;
		this._tps = tps;
		this._ram = ram;
		this._maxRam = maxRam;
		this._publicAddress = publicAddress;
		this._port = port;
		this._startUpDate = startUpDate;
	}
	
	/**
	 * @return true, if {@value _playerCount} equals 0, false otherwise.
	 */
	public boolean isEmpty()
	{
		return _playerCount == 0;
	}
	
	/**
	 * @return the amount of time (in seconds) that this {@link MinecraftServer} has been online for.
	 */
	public double getUptime()
	{
		return (System.currentTimeMillis() / 1000d - _startUpDate);
	}
	
	/**
	 * @return true, if this server is currently joinable by players, false otherwise.
	 */
	public boolean isJoinable()
	{
		if (_motd != null && (_motd.contains("Starting") || _motd.contains("Recruiting")
								|| _motd.contains("Waiting") || _motd.contains("Open in") || _motd.isEmpty() || _motd.contains("Generating")))
		{
			if (_playerCount < _maxPlayerCount)
			{
				int availableSlots = _maxPlayerCount - _playerCount;
				return _motd.isEmpty() ? (availableSlots > 20) : true;
			}
		}
		return false;
	}
	
	public void setGroup(String group)
	{
		_group = group;
	}
	public void setName(String name)
	{
		_name = name;
	}
}
