package org.bukkit.scoreboard;

import java.util.Set;

import org.bukkit.OfflinePlayer;

public abstract interface Team
{
	public abstract String getName()
			throws IllegalStateException;

	public abstract String getDisplayName()
			throws IllegalStateException;

	public abstract void setDisplayName(String paramString)
			throws IllegalStateException, IllegalArgumentException;

	public abstract String getPrefix()
			throws IllegalStateException;

	public abstract void setPrefix(String paramString)
			throws IllegalStateException, IllegalArgumentException;

	public abstract String getSuffix()
			throws IllegalStateException;

	public abstract void setSuffix(String paramString)
			throws IllegalStateException, IllegalArgumentException;

	public abstract boolean allowFriendlyFire()
			throws IllegalStateException;

	// Mineplex
	public abstract void setNameTagVisibility(TeamNameTagVisibility visibility)
			throws IllegalStateException;

	public abstract void setAllowFriendlyFire(boolean paramBoolean)
			throws IllegalStateException;

	public abstract boolean canSeeFriendlyInvisibles()
			throws IllegalStateException;

	public abstract void setCanSeeFriendlyInvisibles(boolean paramBoolean)
			throws IllegalStateException;

	public abstract Set<OfflinePlayer> getPlayers()
			throws IllegalStateException;

	public abstract Set<String> getEntries()
			throws IllegalStateException;

	public abstract int getSize()
			throws IllegalStateException;

	public abstract Scoreboard getScoreboard();

	public abstract void addPlayer(OfflinePlayer paramOfflinePlayer)
			throws IllegalStateException, IllegalArgumentException;

	public abstract void addEntry(String paramString)
			throws IllegalStateException, IllegalArgumentException;

	public abstract boolean removePlayer(OfflinePlayer paramOfflinePlayer)
			throws IllegalStateException, IllegalArgumentException;

	public abstract boolean removeEntry(String paramString)
			throws IllegalStateException, IllegalArgumentException;

	public abstract void unregister()
			throws IllegalStateException;

	public abstract boolean hasPlayer(OfflinePlayer paramOfflinePlayer)
			throws IllegalArgumentException, IllegalStateException;

	public abstract boolean hasEntry(String paramString)
			throws IllegalArgumentException, IllegalStateException;
}
