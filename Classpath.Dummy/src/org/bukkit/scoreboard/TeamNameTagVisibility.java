package org.bukkit.scoreboard;

/**
 * Created by shaun on 14-10-05.
 */
public enum TeamNameTagVisibility
{
	NEVER("never"), HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"), HIDE_FOR_OWN_TEAM("hideForOwnTeam"), ALWAYS("always");

	private String _keyword;

	TeamNameTagVisibility(String keyword)
	{
		_keyword = keyword;
	}

	@Override
	public String toString()
	{
		return _keyword;
	}
}
