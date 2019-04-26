package nautilus.game.arcade;

import mineplex.core.game.GameCategory;
import mineplex.core.game.GameDisplay;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.games.baconbrawl.BaconBrawl;
import nautilus.game.arcade.game.games.barbarians.Barbarians;
import nautilus.game.arcade.game.games.bridge.Bridge;
import nautilus.game.arcade.game.games.build.Build;
import nautilus.game.arcade.game.games.cards.Cards;
import nautilus.game.arcade.game.games.castlesiege.CastleSiege;
import nautilus.game.arcade.game.games.champions.ChampionsDominate;
import nautilus.game.arcade.game.games.champions.ChampionsTDM;
import nautilus.game.arcade.game.games.christmas.Christmas;
import nautilus.game.arcade.game.games.deathtag.DeathTag;
import nautilus.game.arcade.game.games.dragonescape.DragonEscape;
import nautilus.game.arcade.game.games.dragonescape.DragonEscapeTeams;
import nautilus.game.arcade.game.games.dragonriders.DragonRiders;
import nautilus.game.arcade.game.games.dragons.Dragons;
import nautilus.game.arcade.game.games.dragons.DragonsTeams;
import nautilus.game.arcade.game.games.draw.Draw;
import nautilus.game.arcade.game.games.event.EventGame;
import nautilus.game.arcade.game.games.evolution.Evolution;
import nautilus.game.arcade.game.games.gravity.Gravity;
import nautilus.game.arcade.game.games.halloween.Halloween;
import nautilus.game.arcade.game.games.hideseek.HideSeek;
import nautilus.game.arcade.game.games.holeinwall.HoleInTheWall;
import nautilus.game.arcade.game.games.horsecharge.Horse;
import nautilus.game.arcade.game.games.lobbers.BombLobbers;
import nautilus.game.arcade.game.games.micro.Micro;
import nautilus.game.arcade.game.games.milkcow.MilkCow;
import nautilus.game.arcade.game.games.minestrike.MineStrike;
import nautilus.game.arcade.game.games.mineware.MineWare;
import nautilus.game.arcade.game.games.oldmineware.OldMineWare;
import nautilus.game.arcade.game.games.paintball.Paintball;
import nautilus.game.arcade.game.games.quiver.Quiver;
import nautilus.game.arcade.game.games.quiver.QuiverTeams;
import nautilus.game.arcade.game.games.runner.Runner;
import nautilus.game.arcade.game.games.searchanddestroy.SearchAndDestroy;
import nautilus.game.arcade.game.games.sheep.SheepGame;
import nautilus.game.arcade.game.games.skywars.SoloSkywars;
import nautilus.game.arcade.game.games.skywars.TeamSkywars;
import nautilus.game.arcade.game.games.smash.SoloSuperSmash;
import nautilus.game.arcade.game.games.smash.SuperSmashDominate;
import nautilus.game.arcade.game.games.smash.TeamSuperSmash;
import nautilus.game.arcade.game.games.snake.Snake;
import nautilus.game.arcade.game.games.sneakyassassins.SneakyAssassins;
import nautilus.game.arcade.game.games.snowfight.SnowFight;
import nautilus.game.arcade.game.games.spleef.Spleef;
import nautilus.game.arcade.game.games.spleef.SpleefTeams;
import nautilus.game.arcade.game.games.squidshooter.SquidShooter;
import nautilus.game.arcade.game.games.stacker.Stacker;
import nautilus.game.arcade.game.games.survivalgames.SoloSurvivalGames;
import nautilus.game.arcade.game.games.survivalgames.TeamSurvivalGames;
import nautilus.game.arcade.game.games.tug.Tug;
import nautilus.game.arcade.game.games.turfforts.TurfForts;
import nautilus.game.arcade.game.games.uhc.UHC;
import nautilus.game.arcade.game.games.wither.WitherGame;
import nautilus.game.arcade.game.games.wizards.Wizards;
import nautilus.game.arcade.game.games.zombiesurvival.ZombieSurvival;

import org.bukkit.Material;

public enum GameType
{	
	//Mini
	BaconBrawl(BaconBrawl.class, GameDisplay.BaconBrawl),
	Barbarians(Barbarians.class, GameDisplay.Barbarians),
	Bridge(Bridge.class, GameDisplay.Bridge),
	CastleSiege(CastleSiege.class, GameDisplay.CastleSiege),
	ChampionsDominate(ChampionsDominate.class, GameDisplay.ChampionsDominate),
	//ChampionsMOBA(ChampionsMOBA.class, "Champions MOBA", "Champions", Material.SKULL_ITEM, (byte)0, GameCategory.CHAMPIONS, 7),
	ChampionsTDM(ChampionsTDM.class, GameDisplay.ChampionsTDM),
	Christmas(Christmas.class, GameDisplay.Christmas, "http://chivebox.com/file/c/xmas.zip", true),
	DeathTag(DeathTag.class, GameDisplay.DeathTag),
	DragonEscape(DragonEscape.class, GameDisplay.DragonEscape),
	DragonEscapeTeams(DragonEscapeTeams.class, GameDisplay.DragonEscapeTeams),
	DragonRiders(DragonRiders.class, GameDisplay.DragonRiders),
	Dragons(Dragons.class, GameDisplay.Dragons),
	DragonsTeams(DragonsTeams.class, GameDisplay.DragonsTeams),
	Draw(Draw.class, GameDisplay.Draw),
	Evolution(Evolution.class, GameDisplay.Evolution),
	//FlappyBird(FlappyBird.class, "Flappy Bird", Material.FEATHER, (byte)0, GameCategory.ARCADE, 17),
	Gravity(Gravity.class, GameDisplay.Gravity),
	Halloween(Halloween.class, GameDisplay.Halloween, "http://chivebox.com/file/c/hh.zip", true),
	HideSeek(HideSeek.class, GameDisplay.HideSeek),
	HoleInTheWall(HoleInTheWall.class, GameDisplay.HoleInTheWall),
	Horse(Horse.class, GameDisplay.Horse),
	Lobbers(BombLobbers.class, GameDisplay.Lobbers),
	Micro(Micro.class, GameDisplay.Micro),
	MilkCow(MilkCow.class, GameDisplay.MilkCow),
	MineStrike(MineStrike.class, GameDisplay.MineStrike, "http://chivebox.com/file/c/assets.zip", true),// Temp set to CHAMPIONS to fix UI bug
	MineWare(MineWare.class, GameDisplay.MineWare),
	OldMineWare(OldMineWare.class, GameDisplay.OldMineWare),
	Paintball(Paintball.class, GameDisplay.Paintball),
	Quiver(Quiver.class, GameDisplay.Quiver),
	QuiverTeams(QuiverTeams.class, GameDisplay.QuiverTeams),
	Runner(Runner.class, GameDisplay.Runner),
	SearchAndDestroy(SearchAndDestroy.class, GameDisplay.SearchAndDestroy),
	Sheep(SheepGame.class, GameDisplay.Sheep),
	
	Smash(SoloSuperSmash.class, GameDisplay.Smash),
	SmashDomination(SuperSmashDominate.class, GameDisplay.SmashDomination),
	SmashTeams(TeamSuperSmash.class, GameDisplay.SmashTeams, new GameType[]{GameType.Smash}, false),
	Snake(Snake.class, GameDisplay.Snake),
	SneakyAssassins(SneakyAssassins.class, GameDisplay.SneakyAssassins),
	SnowFight(SnowFight.class, GameDisplay.SnowFight),
	Spleef(Spleef.class, GameDisplay.Spleef),
	SpleefTeams(SpleefTeams.class, GameDisplay.SpleefTeams),
	SquidShooter(SquidShooter.class, GameDisplay.SquidShooter),
	Stacker(Stacker.class, GameDisplay.Stacker),
	SurvivalGames(SoloSurvivalGames.class, GameDisplay.SurvivalGames),
	SurvivalGamesTeams(TeamSurvivalGames.class, GameDisplay.SurvivalGamesTeams, new GameType[]{GameType.SurvivalGames}, false),
	Tug(Tug.class, GameDisplay.Tug),
	TurfWars(TurfForts.class, GameDisplay.TurfWars),
	UHC(UHC.class, GameDisplay.UHC),
	WitherAssault(WitherGame.class, GameDisplay.WitherAssault),
	Wizards(Wizards.class, GameDisplay.Wizards, "http://chivebox.com/file/c/ResWizards.zip", true),
	ZombieSurvival(ZombieSurvival.class, GameDisplay.ZombieSurvival),
	Build(Build.class, GameDisplay.Build),
	Cards(Cards.class, GameDisplay.Cards),
	Skywars(SoloSkywars.class, GameDisplay.Skywars),
	SkywarsTeams(TeamSkywars.class, GameDisplay.SkywarsTeams, new GameType[]{GameType.Skywars}, false),
	
	Event(EventGame.class, GameDisplay.Event, new GameType[]{
		GameType.BaconBrawl, GameType.Barbarians, GameType.Bridge, GameType.Build, GameType.Build,
		GameType.Cards, GameType.CastleSiege, GameType.ChampionsDominate, GameType.ChampionsTDM, GameType.Christmas, 
		GameType.DeathTag, GameType.DragonEscape, GameType.DragonEscapeTeams, GameType.DragonRiders, GameType.Dragons,
		GameType.Draw, GameType.Evolution, GameType.Gravity, GameType.Halloween, GameType.HideSeek,
		GameType.HoleInTheWall, GameType.Horse, GameType.Micro, GameType.MilkCow, GameType.MineStrike, GameType.MineWare,
		GameType.OldMineWare, GameType.Paintball, GameType.Quiver, GameType.QuiverTeams, GameType.Runner, GameType.SearchAndDestroy,
		GameType.Sheep, GameType.Skywars, GameType.SkywarsTeams, GameType.Smash, GameType.SmashDomination, GameType.SmashTeams,
		GameType.Snake, GameType.SneakyAssassins, GameType.SnowFight, GameType.Spleef, GameType.SpleefTeams, GameType.SquidShooter,
		GameType.Stacker, GameType.SurvivalGames, GameType.SurvivalGamesTeams, GameType.Tug, GameType.TurfWars, GameType.UHC,
		GameType.WitherAssault, GameType.Wizards, GameType.ZombieSurvival}, true);

	GameDisplay _display;
	boolean _enforceResourcePack;
	GameType[] _mapSource;
	boolean _ownMaps;
	String _resourcePack;
	Class<? extends Game> _gameClass;
	
	private int _gameId;	// Unique identifying id for this gamemode (used for statistics)
	public int getGameId() { return _gameId; }

	GameType(Class<? extends Game> gameClass, GameDisplay display)
	{
		this(gameClass, display, null, false, null, true);
	}

	GameType(Class<? extends Game> gameClass, GameDisplay display, String resourcePackUrl, boolean enforceResourcePack)
	{
		this(gameClass, display, resourcePackUrl, enforceResourcePack, null, true);
	}
	
	GameType(Class<? extends Game> gameClass, GameDisplay display, GameType[] mapSource, boolean ownMap)
	{
		this(gameClass, display, null, false, mapSource, ownMap);
	}
	
	GameType(Class<? extends Game> gameClass, GameDisplay display, String resourcePackUrl, boolean enforceResourcePack, GameType[] mapSource, boolean ownMaps)
	{
		_display = display;
		_gameClass = gameClass;
		_resourcePack = resourcePackUrl;
		_enforceResourcePack = enforceResourcePack;
		_mapSource = mapSource;
		_ownMaps = ownMaps;
	}
	
	public Class<? extends Game> getGameClass()
	{
		return _gameClass;
	}

	public boolean isEnforceResourcePack()
	{
		return _enforceResourcePack;
	}	

	public String getResourcePackUrl()
	{
		return _resourcePack;
	}
	
	public GameType[] getMapSource()
	{
		return _mapSource;
	}
	
	public boolean ownMaps()
	{
		return _ownMaps;
	}

	public String GetName()
	{
		return _display.getName();
	}
	
	public String GetLobbyName()
	{
		return _display.getLobbyName();
	}
	
	public Material GetMaterial()
	{
		return _display.getMaterial();
	}
	
	public byte GetMaterialData()
	{
		return _display.getMaterialData();
	}

	public GameCategory getGameCategory()
	{
		return _display.getGameCategory();
	}
	
}