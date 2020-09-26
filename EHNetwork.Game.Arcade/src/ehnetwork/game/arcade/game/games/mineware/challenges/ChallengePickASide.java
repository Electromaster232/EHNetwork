package ehnetwork.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextBottom;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.game.games.mineware.Challenge;
import ehnetwork.game.arcade.game.games.mineware.MineWare;

public class ChallengePickASide extends Challenge
{
	private long _stageExpires;
	private long _lastSound;
	private boolean _isFalling;

	public ChallengePickASide(MineWare host)
	{
		super(host, ChallengeType.LastStanding, "Stand on the side with the least players! Don't be caught on the white blocks!");
	}

	public int getMinPlayers()
	{
		return 3;
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> spawns = new ArrayList<Location>();

		for (int x = -8; x <= 8; x++)
		{
			for (int z = -8; z <= 8; z++)
			{
				if (x % 2 == 0 && z % 2 == 0)
				{
					spawns.add(getCenter().clone().add(x + 0.5, 1.1, z + 0.5));
				}
			}
		}

		return spawns;
	}

	@Override
	public void cleanupRoom()
	{
	}

	@Override
	public void setupPlayers()
	{
		setBorder(-10, 10, 0, 10, -10, 10);
		_stageExpires = System.currentTimeMillis() + 4000;
	}

	@EventHandler
	public void OnTick(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		if (!Host.IsLive())
		{
			return;
		}

		Player[] players = getChallengers().toArray(new Player[0]);

		if (_stageExpires < System.currentTimeMillis())
		{
			UtilTextBottom.displayProgress(0, players);

			if (_isFalling)
			{
				_isFalling = false;
				_stageExpires = System.currentTimeMillis() + 1500;
			}
			else
			{
				_isFalling = true;
				_stageExpires = System.currentTimeMillis() + 4000;

				ArrayList<Player> red = new ArrayList<Player>();
				ArrayList<Player> blue = new ArrayList<Player>();
				ArrayList<Player> misc = new ArrayList<Player>();

				for (Player player : getChallengers())
				{
					Location loc = player.getLocation();
					loc.setY(0);

					byte data = loc.getBlock().getType() == Material.STAINED_CLAY ? loc.getBlock().getData() : 0;

					if (data == 14)
					{
						red.add(player);
					}
					else if (data == 11)
					{
						blue.add(player);
					}
					else
					{
						misc.add(player);
					}
				}

				Host.Announce(C.cRed + "RED: " + red.size(), true);
				Host.Announce(C.cRed + "BLUE: " + blue.size(), false);

				if (misc.size() > 0)
				{
					Host.Announce(C.cRed + "MISC: " + misc.size(), false);
				}

				if (!red.isEmpty() && !blue.isEmpty())
				{
					if (red.size() < blue.size())
					{
						misc.addAll(blue);
					}
					else if (blue.size() < red.size())
					{
						misc.addAll(red);
					}

					for (Player player : misc)
					{
						setLost(player);
					}
				}
			}
		}
		else if (!_isFalling)
		{
			UtilTextBottom.displayProgress(1 - ((_stageExpires - System.currentTimeMillis()) / 8000D), players);

			if (_lastSound < System.currentTimeMillis())
			{
				_lastSound = System.currentTimeMillis() + 1000;

				for (Player player : UtilServer.getPlayers())
					player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
			}
		}
		else
		{
			UtilTextBottom.displayProgress(0, players);
		}
	}

	@Override
	public void generateRoom()
	{
		for (int x = -10; x <= 10; x++)
		{
			for (int z = -10; z <= 10; z++)
			{
				for (int y = 0; y <= 2; y++)
				{
					Block b = getCenter().getBlock().getRelative(x, y, z);

					if (y == 0 || Math.abs(x) == 10 || Math.abs(z) == 10)
					{
						if (y > 0 && Math.abs(z) != 10)
							continue;

						b.setType(Material.STAINED_CLAY);
						b.setData((byte) (z < 0 ? 11 : z > 0 ? 14 : 0));
						addBlock(b);
					}
				}
			}
		}
	}

}
