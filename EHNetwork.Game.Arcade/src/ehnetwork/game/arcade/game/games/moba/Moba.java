package ehnetwork.game.arcade.game.games.moba;

import java.util.ArrayList;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.NullKit;

public class Moba extends TeamGame
{
	private ArrayList<String> _lastScoreboard = new ArrayList<String>();

	public Moba(ArcadeManager manager) 
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