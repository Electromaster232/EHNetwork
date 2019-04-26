package mineplex.core.friend.redis;

import mineplex.serverdata.commands.ServerCommand;

public class FriendRequest extends ServerCommand
{
	private String _requester;
	private String _requested;
	
	public String getRequester() { return _requester; }
	public String getRequested() { return _requested; }
	
	public FriendRequest(String requester, String requested)
	{
		_requester = requester;
		_requested = requested;
	}
	
	@Override
	public void run() 
	{
		// Utilitizes a callback functionality to seperate dependencies
	}
}