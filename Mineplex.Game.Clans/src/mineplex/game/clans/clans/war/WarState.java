package mineplex.game.clans.clans.war;

public enum WarState
{
	FORM_ENEMIES("Enemies can now be formed"),
	WAR("War is in progress"),
	INVADE("Winning teams can invade enemies");

	private String _description;

	WarState(String description)
	{
		_description = description;
	}

	public String getDescription()
	{
		return _description;
	}
}
