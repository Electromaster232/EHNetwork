package ehnetwork.core.party.redis;

import ehnetwork.core.party.Party;
import ehnetwork.core.party.PartyManager;
import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.ServerCommand;

public class RedisPartyHandler implements CommandCallback
{
	private PartyManager _partyManager;

	public RedisPartyHandler(PartyManager partyManager)
	{
		_partyManager = partyManager;
	}

	@Override
	public void run(ServerCommand command)
	{
		final RedisPartyData data = (RedisPartyData) command;

		_partyManager.getPlugin().getServer().getScheduler().runTask(_partyManager.getPlugin(), new Runnable()
		{
			public void run()
			{
				_partyManager.addParty(new Party(_partyManager, data));
			}
		});
	}
}
