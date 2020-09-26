package ehnetwork.core.friend.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.friend.ui.FriendsGUI;

public class AddFriend extends CommandBase<FriendManager>
{
    public AddFriend(FriendManager plugin)
    {
        super(plugin, Rank.ALL, "friends", "friend", "f");
    }

    @Override
    public void Execute(final Player caller, final String[] args)
    {
        if (args == null || args.length < 1)
        {
            if (Plugin.getPreferenceManager().Get(caller).friendDisplayInventoryUI)
            {
                new FriendsGUI(Plugin, caller);
            }
            else
            {
                Plugin.showFriends(caller);
            }
        }
        else
        {
            CommandCenter.GetClientManager().checkPlayerName(caller, args[0], new Callback<String>()
            {
                public void run(String result)
                {
                    if (result != null)
                    {
                        Plugin.addFriend(caller, result);
                    }
                }
            });
        }
    }
}
