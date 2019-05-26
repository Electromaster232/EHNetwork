package nautilus.game.arcade;

import java.util.EnumSet;
import java.util.Set;

public enum TournamentType
{
	SUPER_SMASH_MOBS(GameType.Smash),
	SURVIVAL_GAMES(GameType.SurvivalGames),
	MIXED_ARCADE(
			GameType.Dragons,
			GameType.DragonEscape,
			GameType.DeathTag,
			GameType.Runner,
			GameType.Snake,
			GameType.Spleef,
			GameType.SneakyAssassins,
			GameType.Quiver);

	private final Set<GameType> _gameTypes;

	TournamentType(GameType firstGameType, GameType... rest)
	{
		_gameTypes = EnumSet.of(firstGameType, rest);
	}

	public Set<GameType> getGameTypes()
	{
		return _gameTypes;
	}

	public static TournamentType getTournamentType(GameType gameType)
	{
		for (TournamentType type : values())
		{
			if (type.getGameTypes().contains(gameType))
				return type;
		}

		return null;
	}
}
