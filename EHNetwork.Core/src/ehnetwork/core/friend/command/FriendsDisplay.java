package ehnetwork.core.friend.command;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.friend.ui.FriendsGUI;
import ehnetwork.core.preferences.UserPreferences;

public class FriendsDisplay extends CommandBase<FriendManager>
{
    public FriendsDisplay(FriendManager plugin)
    {
        super(plugin, Rank.ALL, "friendsdisplay");
    }

    @Override
    public void Execute(Player caller, final String[] args)
    {
        UserPreferences preferences = Plugin.getPreferenceManager().Get(caller);

        preferences.friendDisplayInventoryUI = !preferences.friendDisplayInventoryUI;

        Plugin.getPreferenceManager().savePreferences(caller);

        caller.playSound(caller.getLocation(), Sound.NOTE_PLING, 1, 1.6f);

        if (preferences.friendDisplayInventoryUI)
        {
            new FriendsGUI(Plugin, caller);
        }
        else
        {
            Plugin.showFriends(caller);
        }
    }
}
