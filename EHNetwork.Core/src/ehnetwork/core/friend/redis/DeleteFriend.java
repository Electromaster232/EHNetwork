package ehnetwork.core.friend.redis;

import ehnetwork.serverdata.commands.ServerCommand;

public class DeleteFriend extends ServerCommand
{
	private String _deleter;
	private String _deleted;
	
	public String getDeleter() { return _deleter; }
	public String getDeleted() { return _deleted; }
	
	public DeleteFriend(String deleter, String deleted)
	{
		_deleter = deleter;
		_deleted = deleted;
	}
	
	@Override
	public void run() 
	{
		// Utilitizes a callback functionality to seperate dependencies
	}
}