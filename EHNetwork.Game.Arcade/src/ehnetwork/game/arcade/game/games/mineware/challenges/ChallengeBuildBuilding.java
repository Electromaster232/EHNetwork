package ehnetwork.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.game.arcade.game.games.mineware.ChallengeSeperateRooms;
import ehnetwork.game.arcade.game.games.mineware.MineWare;

public class ChallengeBuildBuilding extends ChallengeSeperateRooms
{
	private ArrayList<Material> _materials = new ArrayList<Material>();
	private HashMap<Location, Material> _build = new HashMap<Location, Material>();

	public ChallengeBuildBuilding(MineWare host, String challengeName)
	{
		super(host, ChallengeType.FirstComplete, "Replicate the building!");

		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 5; y++)
			{
				for (int z = 0; z < 3; z++)
				{

				}
			}
		}
	}

	@Override
	public void generateRoom(Location loc)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getBorderX()
	{
		return 10;
	}

	@Override
	public int getBorderY()
	{
		return 20;
	}

	@Override
	public int getBorderZ()
	{
		return 10;
	}

	@Override
	public int getDividersX()
	{
		return 5;
	}

	@Override
	public int getDividersZ()
	{
		return 5;
	}

	@Override
	public void cleanupRoom()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setupPlayers()
	{
		for (Player player : getChallengers())
		{
			player.setGameMode(GameMode.CREATIVE);
		}
	}

}
