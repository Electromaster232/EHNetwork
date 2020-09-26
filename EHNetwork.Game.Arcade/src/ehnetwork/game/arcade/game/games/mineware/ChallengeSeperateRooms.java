package ehnetwork.game.arcade.game.games.mineware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.UtilPlayer;

public abstract class ChallengeSeperateRooms extends Challenge
{
	private HashMap<String, Integer[]> _borders = new HashMap<String, Integer[]>();
	private HashMap<Location, Entry<Integer[], Location>> _spawns = new HashMap<Location, Entry<Integer[], Location>>();
	private HashMap<String, Location> _rooms = new HashMap<String, Location>();
	private double _borderX, _borderZ;

	public ChallengeSeperateRooms(MineWare host, ChallengeType challengeType, String challengeName)
	{
		super(host, challengeType, challengeName);
	}

	public Location getRoom(Player player)
	{
		System.out.print("2. " + player.getName());
		return _rooms.get(player.getName()).clone();
	}

	public void assignRooms()
	{
		for (Player player : getChallengers())
		{
			for (Entry<Location, Entry<Integer[], Location>> entry : _spawns.entrySet())
			{
				if (entry.getKey().distance(player.getLocation()) < 1)
				{
					System.out.print("1. " + player.getName());
					_rooms.put(player.getName(), entry.getValue().getValue());
					_borders.put(player.getName(), entry.getValue().getKey());
					break;
				}
			}
		}
	}

	public final void generateRoom()
	{
		int size = getChallengers().size();

		int x = 1;
		int z = 1;

		while (size > x * z)
		{
			if (x > z)
			{
				z++;
			}
			else
			{
				x++;
			}
		}

		_borderX = (x * getBorderX()) + (x * getDividersX());
		_borderZ = (z * getBorderZ()) + (z * getDividersZ());
		_borderX /= 2;
		_borderZ /= 2;
		_borderX = Math.ceil(_borderX);
		_borderZ = Math.ceil(_borderZ);

		int players = 0;

		for (int x1 = 0; x1 < x; x1++)
		{
			for (int z1 = 0; z1 < z; z1++)
			{
				if (++players > size)
					continue;

				double lX = (x1 * getBorderX()) + (x1 * getDividersX());
				double lZ = (z1 * getBorderZ()) + (z1 * getDividersZ());
				lX -= _borderX;
				lZ -= _borderZ;

				Location loc = getCenter().clone().add(lX, 0, lZ);

				generateRoom(loc.clone());

				_spawns.put(getSpawn(loc.clone()), new HashMap.SimpleEntry(new Integer[]
					{
							loc.getBlockX(), loc.getBlockX() + getBorderX(),

							loc.getBlockY(), loc.getBlockY() + getBorderY(),

							loc.getBlockZ(), loc.getBlockZ() + getBorderZ()
					}, loc.clone()));
			}
		}
	}

	@Override
	public void StartOrder()
	{
		super.StartOrder();

		setBorder((int) -Math.ceil(_borderX), (int) Math.ceil(_borderX), 0, 256, (int) -Math.ceil(_borderZ),
				(int) Math.ceil(_borderZ));
	}

	public abstract void generateRoom(Location loc);

	public abstract int getBorderX();

	public abstract int getBorderY();

	public abstract int getBorderZ();

	public abstract int getDividersX();

	public abstract int getDividersZ();

	public Location getSpawn(Location roomLocation)
	{
		return roomLocation.clone().add((getBorderX() + 1) / 2D, 1.1, (getBorderZ() + 1) / 2D);
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		return new ArrayList<Location>(_spawns.keySet());
	}

	public boolean isInsideMap(Player player)
	{
		Location loc = player.getLocation();

		if (!_borders.containsKey(player.getName()) || UtilPlayer.isSpectator(player))
		{
			return super.isInsideMap(player);
		}

		Integer[] borders = _borders.get(player.getName());

		return !(loc.getX() >= borders[1] + 1 || loc.getX() <= borders[0] || loc.getZ() >= borders[5] + 1
				|| loc.getZ() <= borders[4] || loc.getY() >= borders[3] + 1 || loc.getY() <= borders[2]);
	}

}
