package mineplex.core.friend.command;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.friend.FriendManager;
import mineplex.core.friend.ui.FriendsGUI;
import mineplex.core.preferences.UserPreferences;

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
