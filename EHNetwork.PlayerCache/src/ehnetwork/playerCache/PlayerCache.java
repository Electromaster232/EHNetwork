package ehnetwork.playerCache;

import java.util.UUID;

import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.redis.RedisDataRepository;
import ehnetwork.serverdata.servers.ServerManager;

public class PlayerCache
{
	private RedisDataRepository<PlayerInfo> _repository;

	public PlayerCache()
	{
		_repository = new RedisDataRepository<PlayerInfo>(
				ServerManager.getMasterConnection(), 
				ServerManager.getSlaveConnection(),
				Region.ALL, 
				PlayerInfo.class, 
				"playercache");
	}
	
	public void addPlayer(PlayerInfo player)
	{
		_repository.addElement(player, 60 * 60 * 6);  // 6 Hours
	}
	
	public PlayerInfo getPlayer(UUID uuid)
	{
		return _repository.getElement(uuid.toString());
	}
	
	public void clean()
	{
		_repository.clean();
	}
}
