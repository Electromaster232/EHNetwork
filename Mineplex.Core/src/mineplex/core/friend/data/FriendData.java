package mineplex.core.friend.data;

import java.util.ArrayList;
import mineplex.core.friend.ui.FriendsGUI;

public class FriendData
{
    private ArrayList<FriendStatus> _friends = new ArrayList<FriendStatus>();
    private FriendsGUI _friendsPage;

    public ArrayList<FriendStatus> getFriends()
    {
        return _friends;
    }

    public void setFriends(ArrayList<FriendStatus> newFriends)
    {
        _friends = newFriends;
        updateGui();
    }

    private void updateGui()
    {
        if (_friendsPage != null)
        {
            _friendsPage.updateGui();
        }
    }

    public void setGui(FriendsGUI friendsPage)
    {
        _friendsPage = friendsPage;
    }

    public FriendsGUI getGui()
    {
        return _friendsPage;
    }
}
