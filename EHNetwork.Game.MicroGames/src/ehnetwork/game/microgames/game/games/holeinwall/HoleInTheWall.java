package ehnetwork.game.microgames.game.games.holeinwall;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.SoloGame;
import ehnetwork.game.microgames.kit.Kit;

public class HoleInTheWall extends SoloGame
{
	private HashMap<String, Field> _fields = new HashMap<String, Field>();
	private int _minX, _minZ, _maxX, _maxZ, _minY;
	private int _ticksSince = 999;
	private int _wallHeight = 3;
	private ArrayList<Wall> _walls = new ArrayList<Wall>();
	private Location _wallSpawn;
	private int _wallsSpawned;
	private Vector _wallVector;
	private int _wallWidth;

	public HoleInTheWall(MicroGamesManager manager)
	{
		super(manager, GameType.HoleInTheWall, new Kit[]
			{
				new KitNormal(manager)
			}, new String[]
			{
				"Hole in wall"
			});

		DamagePvP = false;
		DamagePvE = false;
		DamageEvP = false;
		DamageSelf = false;
		DamageFall = false;
		HungerSet = 20;
		WorldTimeSet = 8000;
		VersionRequire1_8 = true;
	}

	private ArrayList<Entry<Integer, Integer>> getWall()
	{
		// Each minute is 0.1
		double airBlockRatio = 0.2;// 1 - (0.6 + ((System.currentTimeMillis() - GetStateTime()) / 600000D));
		// airBlockRatio = Math.max(0, airBlockRatio);

		HashMap<Entry<Integer, Integer>, Boolean> wall = new HashMap<Entry<Integer, Integer>, Boolean>();

		while (true)
		{
			// Step 1, make a grid of blocks that's acceptable difficulty

			for (int x = 0; x <= _wallWidth; x++)
			{
				for (int y = 0; y < _wallHeight; y++)
				{
					SimpleEntry entry = new HashMap.SimpleEntry(x, y);

					if (!wall.containsKey(entry) || wall.get(entry))
					{
						wall.put(entry, UtilMath.random.nextFloat() >= airBlockRatio);
					}
				}
			}

			// Step 2, parse through that grid to make sure there's enough walking spots.
			// While walking places is not enough, remove a block on Y = 0-1

			ArrayList<Entry<Integer, Integer>> list = new ArrayList<Entry<Integer, Integer>>(wall.keySet());

			while (true)
			{
				Collections.shuffle(list);
				int walkingSpots = 0;

				// First check
				ArrayList<Entry<Integer, Integer>> accountedFor = new ArrayList<Entry<Integer, Integer>>();

				for (Entry<Integer, Integer> entry : list)
				{
					if (!accountedFor.contains(entry) && !wall.get(entry))
					{
						if (entry.getValue() <= 1)
						{
							for (Entry<Integer, Integer> entry1 : list)
							{
								if (entry != entry1 && !wall.get(entry1) && !accountedFor.contains(entry1)
										&& entry1.getKey() == entry.getKey()
										&& Math.abs(entry.getValue() - entry1.getValue()) <= 1)
								{
									accountedFor.add(entry);
									accountedFor.add(entry1);
									walkingSpots++;
									break;
								}
							}
						}
					}
				}

				if (walkingSpots < 2)
				{
					Iterator<Entry<Integer, Integer>> itel = list.iterator();

					while (itel.hasNext())
					{
						Entry<Integer, Integer> entry = itel.next();

						// If Y level is 0-1
						if (wall.get(entry) && entry.getValue() <= 1)
						{
							wall.put(entry, false);

							for (Entry<Integer, Integer> entry1 : list)
							{
								if (entry1.getValue() == entry.getValue() && Math.abs(entry.getKey() - entry1.getKey()) == 1)
								{
									wall.put(entry1, false);
									break;
								}
							}

							break;
						}
					}

					// Scan through the blocks, find a empty hole at Y = 0-1. After finding a hole, remove the block above it then
					// increment walkingSpots by one
					// Continue looping if check fails
					// Find a free spot, remove a block
				}
				else
				{
					break;
				}
			}

			// Step 3, remove freefloating blocks so they are all connected to at least another block
			Iterator<Entry<Integer, Integer>> itel = list.iterator();

			while (itel.hasNext())
			{
				Entry<Integer, Integer> entry = itel.next();

				if (wall.get(entry))
				{
					boolean hasConnecting = false;

					for (Entry<Integer, Integer> entry1 : list)
					{
						if (entry1 != entry)
						{
							if (Math.abs(entry.getKey() - entry1.getKey()) + Math.abs(entry.getValue() - entry1.getValue()) == 1)
							{
								hasConnecting = true;
								break;
							}
						}
					}

					wall.put(entry, hasConnecting);
				}
			}

			double airBlocks = 0;

			for (boolean b : wall.values())
			{
				if (!b)
				{
					airBlocks++;
				}
			}

			// Step 4, if there isn't enough blocks in the wall that the average isn't over solidBlockSpread, goto step 1
			if (airBlocks / wall.size() > airBlockRatio)
			{
				ArrayList<Entry<Integer, Integer>> list1 = new ArrayList<Entry<Integer, Integer>>();

				for (Entry<Entry<Integer, Integer>, Boolean> entry : wall.entrySet())
				{
					if (entry.getValue())
					{
						list1.add(entry.getKey());
					}
				}

				return list1;
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		for (Wall wall : _walls)
		{
			wall.spawnWall(event.getPlayer());
		}
	}

	private void onKnockback(Wall wall, Location wallLocation, Player player)
	{
		if (!wall.getKnockedPlayers().contains(player.getUniqueId()))
		{
			wall.getKnockedPlayers().add(player.getUniqueId());

			player.setVelocity(_wallVector.clone().normalize().multiply(5).setY(0.3));
			player.playSound(player.getLocation(), Sound.NOTE_BASS, 2, 1F);
		}
		/*Location toTeleport = player.getLocation();
		Location teleportDestination = wallLocation.clone().add(_wallVector).add(_wallVector.clone().normalize().multiply(0.101));
		Vector relativeOffset = new Vector();

		if (_wallVector.getX() != 0)
		{
			relativeOffset.setX(teleportDestination.getX() - toTeleport.getX());
		}
		else
		{
			relativeOffset.setZ(teleportDestination.getZ() - toTeleport.getZ());
		}

		teleport(player, toTeleport.add(relativeOffset), relativeOffset);*/
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		if (!IsLive())
		{
			return;
		}

		if (!IsAlive(event.getPlayer()))
		{
			return;
		}

		Vector vec = event.getTo().toVector().subtract(event.getFrom().toVector());

		if (_wallVector.getX() < 0 && vec.getX() < 0)
		{
			return;
		}
		else if (_wallVector.getX() > 0 && vec.getX() > 0)
		{
			return;
		}
		else if (_wallVector.getZ() < 0 && vec.getZ() < 0)
		{
			return;
		}
		else if (_wallVector.getZ() > 0 && vec.getZ() > 0)
		{
			return;
		}

		for (Wall wall : _walls)
		{
			if (wall.hasInterception(event.getFrom(), event.getTo()))
			{
				onKnockback(wall, wall.getLocation(), event.getPlayer());
				break;
			}
		}
	}

	@EventHandler
	public void onTick(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		if (_wallVector == null)
		{
			return;
		}

		if (IsLive())
		{
			_wallVector = _wallVector.normalize().multiply(0.15 + ((System.currentTimeMillis() - GetStateTime()) / 600000D));
		}

		Iterator<Wall> itel = _walls.iterator();

		while (itel.hasNext())
		{
			Wall wall = itel.next();
			Location loc = wall.getLocation().clone().subtract(_wallVector);

			if ((_wallVector.getX() < 0 && loc.getX() < _minX - 1) || (_wallVector.getX() > 0 && loc.getX() > _maxX + 2)
					|| (_wallVector.getZ() < 0 && loc.getZ() < _minZ - 1) || (_wallVector.getZ() > 0 && loc.getZ() > _maxZ + 2))
			{
				wall.destroyWall();
				itel.remove();
			}
			else
			{
				for (Player player : GetPlayers(true))
				{
					if (wall.hasInterception(player.getLocation(), _wallVector))
					{
						onKnockback(wall, wall.getLocation().clone().subtract(_wallVector), player);
					}
				}

				wall.moveWall(_wallVector);
			}
		}
	}

	@Override
	public void ParseData()
	{
		Block tnt1 = WorldData.GetCustomLocs("46").get(0).getBlock();
		Block tnt2 = WorldData.GetCustomLocs("46").get(1).getBlock();
		tnt1.setType(Material.AIR);
		tnt2.setType(Material.AIR);

		_minX = Math.min(tnt1.getX(), tnt2.getX());
		_minZ = Math.min(tnt1.getZ(), tnt2.getZ());
		_maxX = Math.max(tnt1.getX(), tnt2.getX());
		_maxZ = Math.max(tnt1.getZ(), tnt2.getZ());
		_minY = Math.min(tnt1.getY(), tnt2.getY());

		_wallHeight += Math.abs(tnt1.getY() - tnt2.getY());
	}

	private void setField(Object obj, String fieldName, Object value)
	{
		try
		{
			Field field = _fields.get(fieldName);

			if (field == null)
			{
				field = obj.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);

				_fields.put(fieldName, field);
			}

			field.set(obj, value);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private int getField(String fieldName, Object obj)
	{
		try
		{
			Field field = _fields.get(fieldName);

			if (field == null)
			{
				field = obj.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);

				_fields.put(fieldName, field);
			}

			return field.getInt(obj);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return 0;
	}

	@EventHandler
	public void SpawnWall(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		if (!IsLive())
		{
			return;
		}

		if (_wallVector != null && _wallVector.length() * _ticksSince++ < 10)
		{
			return;
		}

		_ticksSince = 0;

		if (_wallsSpawned >= 4)
		{
			if (!_walls.isEmpty())
			{
				return;
			}
			else
			{
				_wallsSpawned = 0;
			}
		}

		if (_wallsSpawned++ == 0)
		{
			int x = _maxX - _minX;
			int z = _maxZ - _minZ;

			Location spawn = new Location(WorldData.World, _minX + (x / 2D) + 0.5D, _minY, _minZ + (x / 2D) + 0.5D);
			_wallSpawn = spawn.clone();

			switch (UtilMath.r(4))
			{
			case 0:
				_wallSpawn.add((x / 2D) + 8, 0, 0);
				_wallWidth = x + 1;
				break;
			case 1:
				_wallSpawn.add((-x / 2D) - 8, 0, 0);
				_wallWidth = x + 1;
				break;
			case 2:
				_wallSpawn.add(0, 0, (z / 2D) + 8);
				_wallWidth = z + 1;
				break;
			case 3:
				_wallSpawn.add(0, 0, (-z / 2D) - 8);
				_wallWidth = z + 1;
				break;
			default:
				break;
			}

			_wallVector = spawn.subtract(_wallSpawn).toVector().setY(0);

			if (Math.abs(_wallVector.getZ()) > Math.abs(_wallVector.getX()))
			{
				_wallVector.setX(0);
			}
			else
			{
				_wallVector.setZ(0);
			}
		}

		Wall wall = new Wall(_wallSpawn, getWall(), _wallVector.getZ() != 0, _wallWidth);

		for (Player player : Bukkit.getOnlinePlayers())
		{
			wall.spawnWall(player);
		}

		_walls.add(wall);
	}

}
