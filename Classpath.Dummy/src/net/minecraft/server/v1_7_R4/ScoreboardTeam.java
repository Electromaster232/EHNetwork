package net.minecraft.server.v1_7_R4;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScoreboardTeam
		extends ScoreboardTeamBase
{
	private final Scoreboard a;
	private final String b;
	private final Set c = new HashSet();
	private String d;
	private String e = "";
	private String f = "";
	private String _nametagVisibility = "always";
	private boolean g = true;
	private boolean h = true;

	public ScoreboardTeam(Scoreboard paramScoreboard, String paramString)
	{
		this.a = paramScoreboard;
		this.b = paramString;
		this.d = paramString;
	}

	public String getName()
	{
		return this.b;
	}

	public String getDisplayName()
	{
		return this.d;
	}

	public void setDisplayName(String paramString)
	{
		if (paramString == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		this.d = paramString;
		this.a.handleTeamChanged(this);
	}

	public Collection getPlayerNameSet()
	{
		return this.c;
	}

	public String getPrefix()
	{
		return this.e;
	}

	public void setPrefix(String paramString)
	{
		if (paramString == null) {
			throw new IllegalArgumentException("Prefix cannot be null");
		}
		this.e = paramString;
		this.a.handleTeamChanged(this);
	}

	public String getSuffix()
	{
		return this.f;
	}

	public void setSuffix(String paramString)
	{
		if (paramString == null) {
			throw new IllegalArgumentException("Suffix cannot be null");
		}
		this.f = paramString;
		this.a.handleTeamChanged(this);
	}

	public String getFormattedName(String paramString)
	{
		return getPrefix() + paramString + getSuffix();
	}

	public static String getPlayerDisplayName(ScoreboardTeamBase paramScoreboardTeamBase, String paramString)
	{
		if (paramScoreboardTeamBase == null) {
			return paramString;
		}
		return paramScoreboardTeamBase.getFormattedName(paramString);
	}

	public boolean allowFriendlyFire()
	{
		return this.g;
	}

	public void setAllowFriendlyFire(boolean paramBoolean)
	{
		this.g = paramBoolean;
		this.a.handleTeamChanged(this);
	}

	public boolean canSeeFriendlyInvisibles()
	{
		return this.h;
	}

	public void setCanSeeFriendlyInvisibles(boolean paramBoolean)
	{
		this.h = paramBoolean;
		this.a.handleTeamChanged(this);
	}

	public String getNametagVisibility()
	{
		return _nametagVisibility;
	}

	public void setNametagVisibility(String visibility)
	{
		_nametagVisibility = visibility;
	}

	public int packOptionData()
	{
		int i = 0;
		if (allowFriendlyFire()) {
			i |= 0x1;
		}
		if (canSeeFriendlyInvisibles()) {
			i |= 0x2;
		}
		return i;
	}
}
