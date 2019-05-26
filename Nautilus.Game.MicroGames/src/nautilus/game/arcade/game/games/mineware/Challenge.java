package nautilus.game.arcade.game.games.mineware;

import java.util.ArrayList;
import java.util.HashSet;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.hologram.Hologram;
import mineplex.core.hologram.Hologram.HologramTarget;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Challenge implements Listener
{
	public enum ChallengeType
	{
		LastStanding, FirstComplete
	}

	public MineWare Host;

	private String _order;

	protected long StartTime;
	protected long Duration;

	private Location _center;

	protected HashSet<Player> Completed = new HashSet<Player>();
	protected HashSet<Player> Lost = new HashSet<Player>();
	private HashSet<Block> _modifiedBlocks = new HashSet<Block>();
	protected int Places;
	private ChallengeType _challengeType;

	public Challenge(MineWare host, ChallengeType challengeType, String challengeName)
	{
		Host = host;
		_order = challengeName;
		_center = new Location(host.WorldData.World, 0, 0, 0);
		_challengeType = challengeType;

		setBorder(-100, 100, 0, 256, -100, 100);
	}
	
	public long getMaxTime()
	{
		return 60000;
	}

	public HashSet<Block> getModifiedBlocks()
	{
		return _modifiedBlocks;
	}

	public String getMessage(Player player)
	{
		return _order;
	}

	public void setBorder(int minX, int maxX, int minY, int maxY, int minZ, int maxZ)
	{
		Host.WorldData.MinX = minX;
		Host.WorldData.MaxX = maxX;
		Host.WorldData.MinY = minY;
		Host.WorldData.MaxY = maxY;
		Host.WorldData.MinZ = minZ;
		Host.WorldData.MaxZ = maxZ;
	}

	protected void addBlock(Block block)
	{
		_modifiedBlocks.add(block);
	}

	public void StartOrder()
	{
		setupPlayers();

		Completed.clear();

		StartTime = System.currentTimeMillis();

		Duration = getMaxTime();

		Places = (int) Math.ceil(getChallengers().size() / 2D);
	}

	public void EndOrder()
	{
		cleanupRoom();
	}

	protected void displayCount(Player player, Location loc, String string)
	{
		final Hologram hologram = new Hologram(Host.Manager.getHologramManager(), loc, string);
		hologram.setHologramTarget(HologramTarget.WHITELIST);
		hologram.addPlayer(player);
		hologram.start();
		final long expires = System.currentTimeMillis() + 500;

		new BukkitRunnable()
		{
			public void run()
			{
				if (!Host.IsLive() || expires < System.currentTimeMillis())
				{
					hologram.stop();
					cancel();
				}
				else
				{
					hologram.setLocation(hologram.getLocation().add(0, 0.1, 0));
				}
			}
		}.runTaskTimer(Host.Manager.getPlugin(), 0, 0);
	}

	public abstract ArrayList<Location> getSpawns();

	public abstract void cleanupRoom();

	/**
	 * Register border
	 */
	public abstract void setupPlayers();

	public abstract void generateRoom();

	public String GetOrder()
	{
		return _order.toUpperCase();
	}

	public boolean isInsideMap(Player player)
	{
		return Host.isInsideMap(player.getLocation());
	}

	public boolean Finish()
	{
		ArrayList<Player> players = getChallengers();

		if (players.size() <= Completed.size())
			return true;

		if (_challengeType == ChallengeType.LastStanding)
		{
			if (players.size() <= Places)
			{
				for (Player player : players)
				{
					SetCompleted(player);
				}
				return true;
			}
		}
		else if (_challengeType == ChallengeType.FirstComplete)
		{
			if (Completed.size() >= Places)
			{
				return true;
			}
		}

		return UtilTime.elapsed(StartTime, Duration);
	}

	public int GetTimeLeft()
	{
		return (int) ((Duration - (System.currentTimeMillis() - StartTime)) / 1000);
	}

	public void SetCompleted(Player player)
	{
		if (Completed.contains(player))
			return;

		Completed.add(player);
		UtilPlayer.message(player, C.cGreen + C.Bold + "You completed the task!");
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);
		UtilInv.Clear(player);
	}

	public HashSet<Player> getLost()
	{
		return Lost;
	}

	public void setLost(Player player)
	{
		Host.LoseLife(player, false);
		UtilInv.Clear(player);
	}

	/**
	 * Get all players that are alive, regardless of having won or not.
	 */
	public ArrayList<Player> getChallengers()
	{
		return Host.getChallengers();
	}

	public boolean IsCompleted(Player player)
	{
		return Completed.contains(player);
	}

	public float GetTimeLeftPercent()
	{
		float a = (float) (Duration - (System.currentTimeMillis() - StartTime));
		float b = (float) (Duration);
		return a / b;
	}

	public final int GetRemainingPlaces()
	{
		if (_challengeType == ChallengeType.FirstComplete)
		{
			return Places - Completed.size();
		}
		else if (_challengeType == ChallengeType.LastStanding)
		{
			return getChallengers().size() - Places;
		}

		return 0;
	}

	public int getMinPlayers()
	{
		return 2;
	}

	public Location getCenter()
	{
		return _center.clone();
	}

	public boolean hasWinner()
	{
		return !Completed.isEmpty();
	}

}
