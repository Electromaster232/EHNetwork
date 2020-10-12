package ehnetwork.game.microgames.game.games.moba;

import java.util.ArrayList;

import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.TeamGame;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.NullKit;

public class Moba extends TeamGame
{
	private ArrayList<String> _lastScoreboard = new ArrayList<String>();

	public Moba(MicroGamesManager manager)
	{
		super(manager, GameType.Sheep,

				new Kit[]
						{
				new NullKit(manager)
						},

						new String[]
								{
				"..."
								});

		this.DeathOut = false;
		this.DeathSpectateSecs = 8;
		
		this.HungerSet = 20;
	}

	@Override
	public void ParseData() 
	{
		
	}
}