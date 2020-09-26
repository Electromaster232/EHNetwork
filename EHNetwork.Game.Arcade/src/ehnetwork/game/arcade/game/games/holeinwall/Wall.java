package ehnetwork.game.arcade.game.games.holeinwall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;

public class Wall
{
	private class WallEntry
	{
		public WallEntry(byte data)
		{
			this.data = data;
		}

		int chicken = UtilEnt.getNewEntityId();
		int block = UtilEnt.getNewEntityId();
		byte data;
	}

	private HashMap<Location, WallEntry> _wallEntities = new HashMap<Location, WallEntry>();
	private Vector _catchup = new Vector();
	private double _minX = -0.501;
	private double _maxX = 0.501;
	private double _minZ = -0.501;
	private double _maxZ = 0.501;
	private ArrayList<UUID> _knockedPlayers = new ArrayList<UUID>();

	public Wall(Location corner, Collection<Entry<Integer, Integer>> blocks, boolean wallXWise, double wallWidth)
	{
		int i = UtilMath.r(16);

		for (Entry<Integer, Integer> entry : blocks)
		{
			Location loc = corner.clone().add(wallXWise ? entry.getKey() - (wallWidth / 2) : 0, entry.getValue(),
					wallXWise ? 0 : entry.getKey() - (wallWidth / 2));

			_wallEntities.put(loc, new WallEntry((byte) ((i + (entry.getKey() / 3D)) % 16)));
		}

		if (!wallXWise)
		{
			_minX = -0.05;
			_maxX = 0.05;
		}
		else
		{
			_minZ = -0.05;
			_maxZ = 0.05;
		}
	}

	public ArrayList<UUID> getKnockedPlayers()
	{
		return _knockedPlayers;
	}

	public Location getLocation()
	{
		return _wallEntities.keySet().iterator().next();
	}

	public boolean hasInterception(Location playerLocation, Vector vec)
	{
		for (Location l : _wallEntities.keySet())
		{
			if (hasInterception(playerLocation, l, l.clone().subtract(vec)))
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasInterception(Location pointA, Location pointB)
	{
		Location loc1 = pointA.clone();
		Location loc2 = pointB.clone();

		loc1.setX(Math.min(pointA.getX(), pointB.getX()) - .3);
		loc1.setY(Math.min(pointA.getY(), pointB.getY()));
		loc1.setZ(Math.min(pointA.getZ(), pointB.getZ()) - .3);
		loc2.setX(Math.max(pointA.getX(), pointB.getX()) + .3);
		loc2.setY(Math.max(pointA.getY(), pointB.getY()));
		loc2.setZ(Math.max(pointA.getZ(), pointB.getZ()) + .3);

		double[] box = new double[]
			{
					(loc2.getX() - loc1.getX()) / 2, (loc2.getY() - loc1.getY()) / 2, (loc2.getZ() - loc1.getZ()) / 2
			};
		double[] bBox = new double[]
			{
					_maxX, 1.2D, _maxZ
			};

		Location mid = loc1.add(loc2).multiply(0.5);

		for (Location loc : _wallEntities.keySet())
		{
			if (checkCollision(mid, loc.clone().add(0, -0.9, 0), box, bBox))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Used to get the bounding box of a block, pointA and pointB is for when the wall moves to expand the box.
	 */
	private Location[] getBoundingBox(Location pointA, Location pointB)
	{
		Location loc1 = pointA.clone();
		Location loc2 = pointB.clone();

		loc1.setX(Math.min(pointA.getX(), pointB.getX()));
		loc1.setZ(Math.min(pointA.getZ(), pointB.getZ()));

		loc2.setX(Math.max(pointA.getX(), pointB.getX()));
		loc2.setZ(Math.max(pointA.getZ(), pointB.getZ()));

		loc1.add(_minX, -0.45 - 1.65 + 0.5, _minZ);
		loc2.add(_maxX, 0.3 + 0.5, _maxZ);

		return new Location[]
			{
					loc1, loc2
			};
	}

	private boolean hasInterception(Location pLoc, Location fLoc, Location sLoc)
	{
		Location[] loc = getBoundingBox(fLoc, sLoc);

		return UtilAlg.inBoundingBox(pLoc, loc[0], loc[1]);
	}

	public void spawnWall(Player player)
	{
		Packet[] packets = new Packet[3 * _wallEntities.size()];
		int i = 0;

		for (Entry<Location, WallEntry> entry : _wallEntities.entrySet())
		{
			WallEntry ids = entry.getValue();

			PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving();
			packet1.a = ids.chicken;
			packet1.b = EntityType.SILVERFISH.getTypeId();
			packet1.c = (int) Math.floor(entry.getKey().getX() * 32);
			packet1.d = (int) Math.floor((entry.getKey().getY() - 0.15625) * 32);
			packet1.e = (int) Math.floor(entry.getKey().getZ() * 32);
			DataWatcher watcher = new DataWatcher(null);
			watcher.a(0, (byte) 32);
			watcher.a(1, 0);
			packet1.l = watcher;

			PacketPlayOutSpawnEntity packet2 = new PacketPlayOutSpawnEntity(((CraftPlayer) player).getHandle(), 70,
					Material.STAINED_GLASS.getId() | ids.data << 16);
			packet2.a = ids.block;
			packet2.b = (int) Math.floor(entry.getKey().getX() * 32);
			packet2.c = (int) Math.floor(entry.getKey().getY() * 32);
			packet2.d = (int) Math.floor(entry.getKey().getZ() * 32);

			PacketPlayOutAttachEntity packet3 = new PacketPlayOutAttachEntity();

			packet3.b = ids.block;
			packet3.c = ids.chicken;

			packets[i++] = packet1;
			packets[i++] = packet2;
			packets[i++] = packet3;
		}

		UtilPlayer.sendPacket(player, packets);
	}

	public void moveWall(Vector vector)
	{
		_catchup.add(vector);

		byte x = (byte) (_catchup.getX() * 32);
		byte z = (byte) (_catchup.getZ() * 32);

		_catchup.subtract(new Vector(x / 32D, 0, z / 32D));
		Packet[] packets = new Packet[_wallEntities.size()];

		int i = 0;

		for (Entry<Location, WallEntry> entry : _wallEntities.entrySet())
		{
			entry.getKey().add(vector);

			PacketPlayOutRelEntityMove packet = new PacketPlayOutRelEntityMove();
			packet.a = entry.getValue().chicken;
			packet.b = x;
			packet.d = z;

			packets[i++] = packet;
		}

		for (Player player : Bukkit.getOnlinePlayers())
		{
			UtilPlayer.sendPacket(player, packets);
		}
	}

	private boolean checkCollision(Location loc1, Location loc2, double[] box1, double[] box2)
	{
		// check the X axis
		if (Math.abs(loc1.getX() - loc2.getX()) < box1[0] + box2[0])
		{
			// check the Y axis
			if (Math.abs(loc1.getY() - loc2.getY()) < box1[1] + box2[1])
			{
				// check the Z axis
				if (Math.abs(loc1.getZ() - loc2.getZ()) < box1[2] + box2[2])
				{
					return true;
				}
			}
		}

		return false;
	}

	public void destroyWall()
	{
		int[] ids = new int[_wallEntities.size() * 2];

		int i = 0;

		for (WallEntry entry : _wallEntities.values())
		{
			ids[i++] = entry.chicken;
			ids[i++] = entry.block;
		}

		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ids);

		for (Player player : Bukkit.getOnlinePlayers())
		{
			UtilPlayer.sendPacket(player, packet);
		}
	}
}
