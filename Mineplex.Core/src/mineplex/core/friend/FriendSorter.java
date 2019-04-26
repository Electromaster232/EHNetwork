package mineplex.core.friend;

import java.util.Comparator;

import mineplex.core.friend.data.FriendStatus;

public class FriendSorter implements Comparator<FriendStatus>
{	
	public int compare(FriendStatus a, FriendStatus b)
	{
		if (a.Online && !b.Online)
		{
			return 1;
		}

		if (b.Online && !a.Online)
		{
			return -1;
		}
		
		// If online we sort by mutual
		if (a.Online && b.Online)
		{
			if (a.Status == FriendStatusType.Accepted && b.Status != FriendStatusType.Accepted)
				return 1;
			else if (b.Status == FriendStatusType.Accepted && a.Status != FriendStatusType.Accepted)
				return -1;
			
			if (a.Name.compareTo(b.Name) > 0)
				return 1;
			else if (b.Name.compareTo(a.Name) > 0)
				return -1;
		}
		
		if (a.LastSeenOnline < b.LastSeenOnline)
			return 1;
		
		if (b.LastSeenOnline < a.LastSeenOnline)
			return -1;

		return 0;
	}
}