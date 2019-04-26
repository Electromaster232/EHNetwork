package nautilus.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import mineplex.core.common.util.UtilMath;
import nautilus.game.arcade.game.games.mineware.Challenge;
import nautilus.game.arcade.game.games.mineware.MineWare;

public class ChallengeSmashOff extends Challenge
{
	private ArrayList<Location> _spawns = new ArrayList<Location>();

	public ChallengeSmashOff(MineWare host)
	{
		super(host, ChallengeType.LastStanding, "Knock the other players off!");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		return _spawns;
	}

	@Override
	public void cleanupRoom()
	{
		Host.DamagePvP = false;

		for (Player player : getChallengers())
		{
			player.setHealth(player.getMaxHealth());
		}
	}

	@Override
	public void setupPlayers()
	{
		Host.DamagePvP = true;
	}

	@Override
	public void generateRoom()
	{
		int amount = (int) Math.ceil(Math.sqrt(getChallengers().size()));
		int a = UtilMath.r(16);

		for (int pX = 0; pX < amount; pX++)
		{
			for (int pZ = 0; pZ < amount; pZ++)
			{
				_spawns.add(getCenter().add((pX * 4) + 1.5, 1.1, (pZ * 4) + 1.5));

				for (int x = pX * 4; x < (pX * 4) + 2; x++)
				{
					for (int z = pZ * 4; z < (pZ * 4) + 2; z++)
					{
						Block b = getCenter().getBlock().getRelative(x, 0, z);
						b.setType(Material.STAINED_CLAY);
						b.setData((byte) a);

						addBlock(b);
					}
				}

				if (++a > 15)
				{
					a = 0;
				}
			}
		}
	}

}
