package mineplex.core.party.redis;

import mineplex.core.party.Party;
import mineplex.core.party.PartyManager;
import mineplex.serverdata.commands.CommandCallback;
import mineplex.serverdata.commands.ServerCommand;

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
