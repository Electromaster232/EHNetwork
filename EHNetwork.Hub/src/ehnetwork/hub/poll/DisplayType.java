package ehnetwork.hub.poll;

import ehnetwork.core.common.Rank;

public enum DisplayType
{
	ALL,
	RANKED,
	NOT_RANKED;

	public boolean shouldDisplay(Rank rank)
	{
		switch (this)
		{
			case RANKED:
				return rank.Has(Rank.ULTRA);
			case NOT_RANKED:
				return !rank.Has(Rank.ULTRA);
			default:
				return true;
		}
	}
}
