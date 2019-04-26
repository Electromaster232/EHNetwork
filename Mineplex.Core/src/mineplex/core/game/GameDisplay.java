package mineplex.core.game;

import org.bukkit.Material;

public enum GameDisplay
{
	//Mini
	BaconBrawl("Bacon Brawl", Material.PORK, (byte)0, GameCategory.ARCADE, 1),
	Barbarians("A Barbarians Life", Material.WOOD_AXE, (byte)0, GameCategory.EXTRA, 2),
	Bridge("The Bridges", Material.IRON_PICKAXE, (byte)0, GameCategory.SURVIVAL, 3),
	CastleSiege("Castle Siege", Material.DIAMOND_CHESTPLATE, (byte)0, GameCategory.CLASSICS, 4),
	ChampionsDominate("Champions Domination", "Champions", Material.BEACON, (byte)0, GameCategory.CHAMPIONS, 6),
	//ChampionsMOBA(ChampionsMOBA.class, "Champions MOBA", "Champions", Material.SKULL_ITEM, (byte)0, GameCategory.CHAMPIONS, 7),
	ChampionsTDM("Champions TDM", "Champions", Material.GOLD_SWORD, (byte)0, GameCategory.CHAMPIONS, 5),
	Christmas("Christmas Chaos", Material.SNOW_BALL, (byte)0, GameCategory.CLASSICS, 8),
	DeathTag("Death Tag", Material.SKULL_ITEM, (byte)0, GameCategory.ARCADE, 9),
	DragonEscape("Dragon Escape", Material.DRAGON_EGG, (byte)0, GameCategory.ARCADE, 10),
	DragonEscapeTeams("Dragon Escape Teams", Material.DRAGON_EGG, (byte)0, GameCategory.TEAM_VARIANT, 11),
	DragonRiders("Dragon Riders", Material.DRAGON_EGG, (byte)0, GameCategory.ARCADE, 12),
	Dragons("Dragons", Material.ENDER_STONE, (byte)0, GameCategory.ARCADE, 13),
	DragonsTeams("Dragons Teams", Material.ENDER_STONE, (byte)0, GameCategory.TEAM_VARIANT, 14),
	Draw("Draw My Thing", Material.BOOK_AND_QUILL, (byte)0, GameCategory.CLASSICS, 15),
	Evolution("Evolution", Material.EMERALD, (byte)0, GameCategory.ARCADE, 16),
	//FlappyBird(FlappyBird.class, "Flappy Bird", Material.FEATHER, (byte)0, GameCategory.ARCADE, 17),
	Gravity("Gravity", Material.ENDER_PORTAL, (byte)0, GameCategory.EXTRA, 18),
	Halloween("Halloween Horror", Material.PUMPKIN, (byte)0, GameCategory.CLASSICS, 19),
	HideSeek("Block Hunt", Material.GRASS, (byte)0, GameCategory.CLASSICS, 20),
	HoleInTheWall("Hole in the Wall", Material.STAINED_GLASS, (byte) 2, GameCategory.ARCADE, 52),
	Horse("Horseback", Material.IRON_BARDING, (byte)0, GameCategory.ARCADE, 21),
	Lobbers("Bomb Lobbers", Material.FIREBALL, (byte) 0, GameCategory.ARCADE, 53),
	Micro("Micro Battle", Material.LAVA_BUCKET, (byte)0, GameCategory.ARCADE, 24),
	MilkCow("Milk the Cow", Material.MILK_BUCKET, (byte)0, GameCategory.ARCADE, 27),
	MineStrike("MineStrike", Material.TNT, (byte)0, GameCategory.CHAMPIONS, 25),// Temp set to CHAMPIONS to fix UI bug
	MineWare("MineWare", Material.PAPER, (byte)0, GameCategory.EXTRA, 26),
	OldMineWare("Old MineWare", Material.PAPER, (byte)0, GameCategory.EXTRA, 26),
	Paintball("Super Paintball", Material.ENDER_PEARL, (byte)0, GameCategory.ARCADE, 28),
	Quiver("One in the Quiver", Material.ARROW, (byte)0, GameCategory.ARCADE, 29),
	QuiverTeams("One in the Quiver Teams", Material.ARROW, (byte)0, GameCategory.TEAM_VARIANT, 30),
	Runner("Runner", Material.LEATHER_BOOTS, (byte)0, GameCategory.ARCADE, 31),
	SearchAndDestroy("Search and Destroy", Material.TNT, (byte)0, GameCategory.SURVIVAL, 32),
	Sheep("Sheep Quest", Material.WOOL, (byte)4, GameCategory.ARCADE, 33),

	Smash("Super Smash Mobs", Material.SKULL_ITEM, (byte)4, GameCategory.CLASSICS, 34),
	SmashDomination("Super Smash Mobs Domination", "Super Smash Mobs", Material.SKULL_ITEM, (byte)4, GameCategory.EXTRA, 36),
	SmashTeams("Super Smash Mobs Teams", "Super Smash Mobs", Material.SKULL_ITEM, (byte)4, GameCategory.TEAM_VARIANT, 35),
	Snake("Snake", Material.WOOL, (byte)0, GameCategory.ARCADE, 37),
	SneakyAssassins("Sneaky Assassins", Material.INK_SACK, (byte)0, GameCategory.ARCADE, 38),
	SnowFight("Snow Fight", Material.SNOW_BALL, (byte)0, GameCategory.EXTRA, 39),
	Spleef("Super Spleef", Material.IRON_SPADE, (byte)0, GameCategory.ARCADE, 40),
	SpleefTeams("Super Spleef Teams", Material.IRON_SPADE, (byte)0, GameCategory.TEAM_VARIANT, 41),
	SquidShooter("Squid Shooter", Material.FIREWORK_CHARGE, (byte)0, GameCategory.ARCADE, 43),
	Stacker("Super Stacker", Material.BOWL, (byte)0, GameCategory.ARCADE, 42),
	SurvivalGames("Survival Games", Material.IRON_SWORD, (byte)0, GameCategory.SURVIVAL, 22),
	SurvivalGamesTeams("Survival Games", Material.IRON_SWORD, (byte)0, GameCategory.TEAM_VARIANT, 23),
	Tug("Tug of Wool", Material.WHEAT, (byte)0, GameCategory.ARCADE, 44),
	TurfWars("Turf Wars", Material.STAINED_CLAY, (byte)14, GameCategory.ARCADE, 45),
	UHC("Ultra Hardcore", Material.GOLDEN_APPLE, (byte)0, GameCategory.SURVIVAL, 46),
	WitherAssault("Wither Assault", Material.SKULL_ITEM, (byte)1, GameCategory.ARCADE, 47),
	Wizards("Wizards", Material.BLAZE_ROD, (byte)0, GameCategory.SURVIVAL, 48),
	ZombieSurvival("Zombie Survival", Material.SKULL_ITEM, (byte)2, GameCategory.SURVIVAL, 49),

	Build("Master Builders", Material.WOOD, (byte)0, GameCategory.CLASSICS, 50),
	Cards("Craft Against Humanity", Material.MAP, (byte)0, GameCategory.CLASSICS, 51),
	Skywars("Skywars", "Skywars", Material.FEATHER, (byte) 0, GameCategory.SURVIVAL, 52),
	SkywarsTeams("Skywars Teams", "Skywars",Material.FEATHER, (byte)5, GameCategory.TEAM_VARIANT, 53),

	Event("Mineplex Event", Material.CAKE, (byte)0, GameCategory.EVENT, 999);

	String _name;
	String _lobbyName;
	Material _mat;
	byte _data;
	GameCategory _gameCategory;

	private int _gameId;	// Unique identifying id for this gamemode (used for statistics)
	public int getGameId() { return _gameId; }

	GameDisplay(String name, Material mat, byte data, GameCategory gameCategory, int gameId)
	{
		this(name, name, mat, data, gameCategory, gameId);
	}

	GameDisplay(String name, String lobbyName, Material mat, byte data, GameCategory gameCategory, int gameId)
	{
		_name = name;
		_lobbyName = lobbyName;
		_mat = mat;
		_data = data;
		_gameCategory = gameCategory;
		_gameId = gameId;
	}

	public String getName()
	{
		return _name;
	}

	public String getLobbyName()
	{
		return _lobbyName;
	}

	public Material getMaterial()
	{
		return _mat;
	}

	public byte getMaterialData()
	{
		return _data;
	}

	public GameCategory getGameCategory()
	{
		return _gameCategory;
	}


	public static GameDisplay matchName(String name)
	{
		for (GameDisplay display : values())
		{
			if (display.getName().equalsIgnoreCase(name))
				return display;
		}
		return null;
	}

}