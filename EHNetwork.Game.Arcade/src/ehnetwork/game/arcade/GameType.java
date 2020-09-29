package ehnetwork.game.arcade;

import org.bukkit.Material;

import ehnetwork.core.game.GameCategory;
import ehnetwork.core.game.GameDisplay;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.amongus.AmongUs;
import ehnetwork.game.arcade.game.games.event.EventGame;
import ehnetwork.game.arcade.game.games.lobbers.BombLobbers;
import ehnetwork.game.arcade.game.games.sheep.SheepGame;
import ehnetwork.game.arcade.game.games.skywars.SoloSkywars;
import ehnetwork.game.arcade.game.games.skywars.TeamSkywars;
import ehnetwork.game.arcade.game.games.smash.SoloSuperSmash;
import ehnetwork.game.arcade.game.games.smash.SuperSmashDominate;
import ehnetwork.game.arcade.game.games.smash.TeamSuperSmash;
import ehnetwork.game.arcade.game.games.survivalgames.SoloSurvivalGames;
import ehnetwork.game.arcade.game.games.survivalgames.TeamSurvivalGames;
import ehnetwork.game.arcade.game.games.tug.turfforts.TurfForts;
import ehnetwork.game.arcade.game.games.wither.WitherGame;

public enum GameType
{	
	//Mini
	BaconBrawl(ehnetwork.game.arcade.game.games.baconbrawl.BaconBrawl.class, GameDisplay.BaconBrawl),
	Barbarians(ehnetwork.game.arcade.game.games.barbarians.Barbarians.class, GameDisplay.Barbarians),
	Bridge(ehnetwork.game.arcade.game.games.bridge.Bridge.class, GameDisplay.Bridge),
	CastleSiege(ehnetwork.game.arcade.game.games.castlesiege.CastleSiege.class, GameDisplay.CastleSiege),
	ChampionsDominate(ehnetwork.game.arcade.game.games.champions.ChampionsDominate.class, GameDisplay.ChampionsDominate),
	//ChampionsMOBA(ChampionsMOBA.class, "Champions MOBA", "Champions", Material.SKULL_ITEM, (byte)0, GameCategory.CHAMPIONS, 7),
	ChampionsTDM(ehnetwork.game.arcade.game.games.champions.ChampionsTDM.class, GameDisplay.ChampionsTDM),
	Christmas(ehnetwork.game.arcade.game.games.christmas.Christmas.class, GameDisplay.Christmas, "http://chivebox.com/file/c/xmas.zip", true),
	DeathTag(ehnetwork.game.arcade.game.games.deathtag.DeathTag.class, GameDisplay.DeathTag),
	DragonEscape(ehnetwork.game.arcade.game.games.dragonescape.DragonEscape.class, GameDisplay.DragonEscape),
	DragonEscapeTeams(ehnetwork.game.arcade.game.games.dragonescape.DragonEscapeTeams.class, GameDisplay.DragonEscapeTeams),
	DragonRiders(ehnetwork.game.arcade.game.games.dragonriders.DragonRiders.class, GameDisplay.DragonRiders),
	Dragons(ehnetwork.game.arcade.game.games.dragons.Dragons.class, GameDisplay.Dragons),
	DragonsTeams(ehnetwork.game.arcade.game.games.dragons.DragonsTeams.class, GameDisplay.DragonsTeams),
	Draw(ehnetwork.game.arcade.game.games.draw.Draw.class, GameDisplay.Draw),
	Evolution(ehnetwork.game.arcade.game.games.evolution.Evolution.class, GameDisplay.Evolution),
	//FlappyBird(FlappyBird.class, "Flappy Bird", Material.FEATHER, (byte)0, GameCategory.ARCADE, 17),
	Gravity(ehnetwork.game.arcade.game.games.gravity.Gravity.class, GameDisplay.Gravity),
	Halloween(ehnetwork.game.arcade.game.games.halloween.Halloween.class, GameDisplay.Halloween, "http://chivebox.com/file/c/hh.zip", true),
	HideSeek(ehnetwork.game.arcade.game.games.hideseek.HideSeek.class, GameDisplay.HideSeek),
	HoleInTheWall(ehnetwork.game.arcade.game.games.holeinwall.HoleInTheWall.class, GameDisplay.HoleInTheWall),
	Horse(ehnetwork.game.arcade.game.games.horsecharge.Horse.class, GameDisplay.Horse),
	Lobbers(BombLobbers.class, GameDisplay.Lobbers),
	Micro(ehnetwork.game.arcade.game.games.micro.Micro.class, GameDisplay.Micro),
	MilkCow(ehnetwork.game.arcade.game.games.milkcow.MilkCow.class, GameDisplay.MilkCow),
	MineStrike(ehnetwork.game.arcade.game.games.minestrike.MineStrike.class, GameDisplay.MineStrike, "http://chivebox.com/file/c/assets.zip", true),// Temp set to CHAMPIONS to fix UI bug
	MineWare(ehnetwork.game.arcade.game.games.mineware.MineWare.class, GameDisplay.MineWare),
	OldMineWare(ehnetwork.game.arcade.game.games.oldmineware.OldMineWare.class, GameDisplay.OldMineWare),
	Paintball(ehnetwork.game.arcade.game.games.paintball.Paintball.class, GameDisplay.Paintball),
	Quiver(ehnetwork.game.arcade.game.games.quiver.Quiver.class, GameDisplay.Quiver),
	QuiverTeams(ehnetwork.game.arcade.game.games.quiver.QuiverTeams.class, GameDisplay.QuiverTeams),
	Runner(ehnetwork.game.arcade.game.games.runner.Runner.class, GameDisplay.Runner),
	SearchAndDestroy(ehnetwork.game.arcade.game.games.searchanddestroy.SearchAndDestroy.class, GameDisplay.SearchAndDestroy),
	Sheep(SheepGame.class, GameDisplay.Sheep),
	PlayerPop(ehnetwork.game.arcade.game.games.playerpop.PlayerPop.class, GameDisplay.PlayerPop),
	Smash(SoloSuperSmash.class, GameDisplay.Smash),
	AmongUs(AmongUs.class, GameDisplay.AmongUs),
	SmashDomination(SuperSmashDominate.class, GameDisplay.SmashDomination),
	SmashTeams(TeamSuperSmash.class, GameDisplay.SmashTeams, new GameType[]{GameType.Smash}, false),
	Snake(ehnetwork.game.arcade.game.games.snake.Snake.class, GameDisplay.Snake),
	SneakyAssassins(ehnetwork.game.arcade.game.games.sneakyassassins.SneakyAssassins.class, GameDisplay.SneakyAssassins),
	SnowFight(ehnetwork.game.arcade.game.games.snowfight.SnowFight.class, GameDisplay.SnowFight),
	Spleef(ehnetwork.game.arcade.game.games.spleef.Spleef.class, GameDisplay.Spleef),
	SpleefTeams(ehnetwork.game.arcade.game.games.spleef.SpleefTeams.class, GameDisplay.SpleefTeams),
	SquidShooter(ehnetwork.game.arcade.game.games.squidshooter.SquidShooter.class, GameDisplay.SquidShooter),
	Stacker(ehnetwork.game.arcade.game.games.stacker.Stacker.class, GameDisplay.Stacker),
	SurvivalGames(SoloSurvivalGames.class, GameDisplay.SurvivalGames),
	SurvivalGamesTeams(TeamSurvivalGames.class, GameDisplay.SurvivalGamesTeams, new GameType[]{GameType.SurvivalGames}, false),
	Tug(ehnetwork.game.arcade.game.games.tug.Tug.class, GameDisplay.Tug),
	TurfWars(TurfForts.class, GameDisplay.TurfWars),
	UHC(ehnetwork.game.arcade.game.games.uhc.UHC.class, GameDisplay.UHC),
	WitherAssault(WitherGame.class, GameDisplay.WitherAssault),
	Wizards(ehnetwork.game.arcade.game.games.wizards.Wizards.class, GameDisplay.Wizards, "http://chivebox.com/file/c/ResWizards.zip", true),
	ZombieSurvival(ehnetwork.game.arcade.game.games.zombiesurvival.ZombieSurvival.class, GameDisplay.ZombieSurvival),
	Build(ehnetwork.game.arcade.game.games.build.Build.class, GameDisplay.Build),
	Murder(ehnetwork.game.arcade.game.games.murder.Murder.class, GameDisplay.Murder),
	Cards(ehnetwork.game.arcade.game.games.cards.Cards.class, GameDisplay.Cards),
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
		GameType.WitherAssault, GameType.Wizards, GameType.ZombieSurvival, GameType.PlayerPop}, true);

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