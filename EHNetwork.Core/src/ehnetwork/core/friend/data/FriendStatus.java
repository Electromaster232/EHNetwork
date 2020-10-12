package ehnetwork.core.friend.data;

import ehnetwork.core.friend.FriendStatusType;

public class FriendStatus
{
    public String Name;
    public String ServerName;
    public boolean Online;
    /**
     * This seems like it should be unmodified without current time subtracted when set
     */
    public long LastSeenOnline;
    public FriendStatusType Status;
}
