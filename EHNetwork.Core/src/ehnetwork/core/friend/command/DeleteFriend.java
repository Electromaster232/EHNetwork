package ehnetwork.core.friend.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.friend.FriendManager;

public class DeleteFriend extends CommandBase<FriendManager>
{
	public DeleteFriend(FriendManager plugin)
	{
		super(plugin, Rank.ALL, "unfriend");
	}

	@Override
	public void Execute(final Player caller, final String[] args)
	{
		if (args == null)
			F.main(Plugin.getName(), "You need to include a player's name.");
		else
		{
			CommandCenter.GetClientManager().checkPlayerName(caller, args[0], new Callback<String>()
			{
				public void run(String result)
				{
					if (result != null)
					{
						Plugin.removeFriend(caller, result);
					}
				}
			});			
		}
	}
}
