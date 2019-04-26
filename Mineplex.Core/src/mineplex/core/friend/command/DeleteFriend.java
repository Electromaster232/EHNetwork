package mineplex.core.friend.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.friend.FriendManager;

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
