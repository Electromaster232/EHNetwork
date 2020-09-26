package ehnetwork.core.party.redis;

import ehnetwork.core.party.Party;
import ehnetwork.serverdata.commands.ServerCommand;

public class RedisPartyData extends ServerCommand
{
	private String[] _players;
	private String _leader;
	private String _previousServer;

	public RedisPartyData(Party party, String previousServer)
	{
		_players = party.GetPlayers().toArray(new String[0]);
		_leader = party.GetLeader();
		_previousServer = previousServer;
	}
	
	public String getPreviousServer()
	{
		return _previousServer;
	}

	public String[] getPlayers()
	{
		return _players;
	}

	public String getLeader()
	{
		return _leader;
	}
}
