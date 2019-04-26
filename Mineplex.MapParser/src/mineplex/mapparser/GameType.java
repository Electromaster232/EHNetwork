package mineplex.mapparser;

public enum GameType
{	
	//Mini
	Other("Other"),
	Unknown("Unknown"),
	Lobby("Lobby"),
	Event("Mineplex Event"),
	
	BaconBrawl("Bacon Brawl"),
	Barbarians("A Barbarians Life"),
	Bridge("The Bridges"),
	Build("Master Builders"),
	CastleSiege("Castle Siege"),
	ChampionsTDM("Champions TDM", "Champions"),
	ChampionsDominate("Champions Domination", "Champions"),
	ChampionsMOBA("Champions MOBA", "Champions"),
	Christmas("Christmas Chaos"),
	DeathTag("Death Tag"),
	DragonEscape("Dragon Escape"),
	DragonEscapeTeams("Dragon Escape Teams"),
	DragonRiders("Dragon Riders"),
	Dragons("Dragons"),
	DragonsTeams("Dragons Teams"),
	Draw("Draw My Thing"),
	Evolution("Evolution"),
	FlappyBird("Flappy Bird"),
	Gravity("Gravity"),
	Halloween("Halloween Horror"),
	HideSeek("Block Hunt"),
	Horse("Horseback"),
	SurvivalGames("Survival Games"),
	SurvivalGamesTeams("Survival Games Teams"),
	Micro("Micro Battle"),
	MineStrike("MineStrike"),
	MineWare("MineWare"),
	MilkCow("Milk the Cow"),
	Paintball("Super Paintball"),
	Quiver("One in the Quiver"),
	QuiverTeams("One in the Quiver Teams"),
	Runner("Runner"),
	SearchAndDestroy("Search and Destroy"),
	Sheep("Sheep Quest"),
	Skywars("Skywars"),
	Smash("Super Smash Mobs"),
	SmashTeams("Super Smash Mobs Teams", "Super Smash Mobs"),
	SmashDomination("Super Smash Mobs Domination", "Super Smash Mobs"),
	Snake("Snake"),
	SneakyAssassins("Sneaky Assassins"),
	SnowFight("Snow Fight"),
	Spleef("Super Spleef"),
	SpleefTeams("Super Spleef Teams"),
	Stacker("Super Stacker"),
	SquidShooter("Squid Shooter"),
	Tug("Tug of Wool"),
	TurfWars("Turf Wars"),
	UHC("Ultra Hardcore"),
	WitherAssault("Wither Assault"),
	Wizards("Wizards"),
	ZombieSurvival("Zombie Survival"),
	
	Upload("Upload"),
	Submissions("Submissions"),
	InProgress("In Progress"),
	
	
	None("None");
	
	String _name;
	String _lobbyName;

	GameType(String name)
	{
		_name = name;
		_lobbyName = name;
	}
	
	GameType(String name, String lobbyName)
	{
		_name = name;
		_lobbyName = lobbyName;
	}

	public String GetName()
	{
		return _name;
	}
	
	public String GetLobbyName()
	{
		return _lobbyName;
	}

	public static GameType match(String string)
	{
		GameType gameType = null;
		string = string.toLowerCase();
		for (GameType type : values())
		{
			if (type.name().toLowerCase().startsWith(string) || type.GetName().toLowerCase().startsWith(string))
			{
				gameType = type;
			}
		}
		return gameType;
	}
}