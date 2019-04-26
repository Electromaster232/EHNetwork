package mineplex.core.hologram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;

public class Hologram
{

	public enum HologramTarget
	{
		BLACKLIST, WHITELIST;
	}

	private Packet _destroy1_7;
	private Packet _destroy1_8;
	/**
	 * 1.7 packets uses both EntityIDs while 1.8 uses only the first.
	 */
	private ArrayList<Entry<Integer, Integer>> _entityIds = new ArrayList<Entry<Integer, Integer>>();
	private Entity _followEntity;
	private HologramManager _hologramManager;
	private String[] _hologramText = new String[0];
	/**
	 * Keeps track of the holograms movements. This fixes offset that occasionally happens when moving a hologram around.
	 */
	private Vector _lastMovement;
	private Location _location;
	private boolean _makeDestroyPackets = true;
	private boolean _makeSpawnPackets = true;
	private Packet[] _packets1_7;
	private Packet[] _packets1_8;
	private HashSet<String> _playersInList = new HashSet<String>();
	private ArrayList<Player> _playersTracking = new ArrayList<Player>();
	private boolean _removeEntityDeath;
	private HologramTarget _target = HologramTarget.BLACKLIST;
	private int _viewDistance = 70;
	protected Vector relativeToEntity;

	public Hologram(HologramManager hologramManager, Location location, String... text)
	{
		_hologramManager = hologramManager;
		_location = location.clone();
		setText(text);
	}

	/**
	 * Adds the player to the Hologram to be effected by Whitelist or Blacklist
	 */
	public Hologram addPlayer(Player player)
	{
		return addPlayer(player.getName());
	}

	/**
	 * Adds the player to the Hologram to be effected by Whitelist or Blacklist
	 */
	public Hologram addPlayer(String player)
	{
		_playersInList.add(player);
		return this;
	}

	/**
	 * Is there a player entry in the hologram for Whitelist and Blacklist
	 */
	public boolean containsPlayer(Player player)
	{
		return _playersInList.contains(player.getName());
	}

	/**
	 * Is there a player entry in the hologram for Whitelist and Blacklist
	 */
	public boolean containsPlayer(String player)
	{
		return _playersInList.contains(player);
	}

	protected Packet getDestroyPacket(Player player)
	{
		if (_makeDestroyPackets)
		{
			makeDestroyPacket();
			_makeDestroyPackets = false;
		}

		return UtilPlayer.is1_8(player) ? _destroy1_8 : _destroy1_7;
	}

	public Entity getEntityFollowing()
	{
		return _followEntity;
	}

	/**
	 * Get who can see the hologram
	 * 
	 * @Whitelist = Only people added can see the hologram
	 * @Blacklist = Anyone but people added can see the hologram
	 */
	public HologramTarget getHologramTarget()
	{
		return _target;
	}

	/**
	 * Get the hologram location
	 */
	public Location getLocation()
	{
		return _location.clone();
	}

	protected ArrayList<Player> getNearbyPlayers()
	{
		ArrayList<Player> nearbyPlayers = new ArrayList<Player>();

		for (Player player : getLocation().getWorld().getPlayers())
		{
			if (isVisible(player))
			{
				nearbyPlayers.add(player);
			}
		}
		return nearbyPlayers;
	}

	protected ArrayList<Player> getPlayersTracking()
	{
		return _playersTracking;
	}

	protected Packet[] getSpawnPackets(Player player)
	{
		if (_makeSpawnPackets)
		{
			makeSpawnPackets();
			_makeSpawnPackets = false;
		}

		return UtilPlayer.is1_8(player) ? _packets1_8 : _packets1_7;
	}

	/**
	 * Get the text in the hologram
	 */
	public String[] getText()
	{
		// We reverse it again as the hologram would otherwise display the text from the bottom row to the top row
		String[] reversed = new String[_hologramText.length];

		for (int i = 0; i < reversed.length; i++)
		{
			reversed[i] = _hologramText[reversed.length - (i + 1)];
		}

		return reversed;
	}

	/**
	 * Get the view distance the hologram is viewable from. Default is 70
	 */
	public int getViewDistance()
	{
		return _viewDistance;
	}

	/**
	 * Is the hologram holograming?
	 */
	public boolean isInUse()
	{
		return _lastMovement != null;
	}

	public boolean isRemoveOnEntityDeath()
	{
		return _removeEntityDeath;
	}

	public boolean isVisible(Player player)
	{
		if (getLocation().getWorld() == player.getWorld())
		{
			if ((getHologramTarget() == HologramTarget.WHITELIST) == containsPlayer(player))
			{
				if (getLocation().distance(player.getLocation()) < getViewDistance())
				{
					return true;
				}
			}
		}

		return false;
	}

	private void makeDestroyPacket()
	{
		int[] entityIds1_7 = new int[_entityIds.size() * 2];
		int[] entityIds1_8 = new int[_entityIds.size()];

		for (int i = 0; i < _entityIds.size(); i++)
		{
			Entry<Integer, Integer> entry = _entityIds.get(i);

			entityIds1_7[i * 2] = entry.getKey();
			entityIds1_7[(i * 2) + 1] = entry.getValue();

			entityIds1_8[i] = entry.getKey();
		}

		_destroy1_7 = new PacketPlayOutEntityDestroy(entityIds1_7);
		_destroy1_8 = new PacketPlayOutEntityDestroy(entityIds1_8);
	}

	private void makeSpawnPackets()
	{
		_packets1_7 = new Packet[_hologramText.length * 3];
		_packets1_8 = new Packet[_hologramText.length * 1];

		if (_entityIds.size() < _hologramText.length)
		{
			_makeDestroyPackets = true;

			for (int i = _entityIds.size(); i < _hologramText.length; i++)
			{
				_entityIds.add(new HashMap.SimpleEntry(UtilEnt.getNewEntityId(), UtilEnt.getNewEntityId()));
			}
		}
		else
		{
			_makeDestroyPackets = true;

			while (_entityIds.size() > _hologramText.length)
			{
				_entityIds.remove(_hologramText.length);
			}
		}
		for (int textRow = 0; textRow < _hologramText.length; textRow++)
		{
			Entry<Integer, Integer> entityIds = this._entityIds.get(textRow);

			Packet[] packets1_7 = makeSpawnPackets1_7(textRow, entityIds.getKey(), entityIds.getValue(), _hologramText[textRow]);

			for (int i = 0; i < packets1_7.length; i++)
			{
				_packets1_7[(textRow * 3) + i] = packets1_7[i];
			}

			Packet[] packets1_8 = makeSpawnPackets1_8(textRow, entityIds.getKey(), _hologramText[textRow]);

			for (int i = 0; i < packets1_8.length; i++)
			{
				_packets1_8[textRow + i] = packets1_8[i];
			}
		}
	}

	private Packet[] makeSpawnPackets1_7(int height, int witherId, int horseId, String horseName)
	{
		// Spawn wither skull
		PacketPlayOutSpawnEntity spawnWitherSkull = new PacketPlayOutSpawnEntity();

		spawnWitherSkull.a = witherId;
		spawnWitherSkull.b = (int) (getLocation().getX() * 32);
		spawnWitherSkull.c = (int) ((getLocation().getY() + 54.6 + ((double) height * 0.285D)) * 32);
		spawnWitherSkull.d = (int) (getLocation().getZ() * 32);
		spawnWitherSkull.j = 66;

		// Spawn horse
		PacketPlayOutSpawnEntityLiving spawnHorse = new PacketPlayOutSpawnEntityLiving();
		DataWatcher watcher = new DataWatcher(null);

		spawnHorse.a = horseId;
		spawnHorse.b = 100;
		spawnHorse.c = (int) (getLocation().getX() * 32);
		spawnHorse.d = (int) ((getLocation().getY() + 54.83 + ((double) height * 0.285D) + 0.23D) * 32);
		spawnHorse.e = (int) (getLocation().getZ() * 32);
		spawnHorse.l = watcher;

		// Setup datawatcher
		watcher.a(0, (byte) 0);
		watcher.a(1, (short) 300);
		watcher.a(10, horseName);
		watcher.a(11, (byte) 1);
		watcher.a(12, -1700000);

		// Make horse ride wither
		PacketPlayOutAttachEntity attachEntity = new PacketPlayOutAttachEntity();

		attachEntity.b = horseId;
		attachEntity.c = witherId;

		return new Packet[]
			{
					spawnWitherSkull, spawnHorse, attachEntity
			};
	}

	private Packet[] makeSpawnPackets1_8(int textRow, int entityId, String lineOfText)
	{
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		DataWatcher watcher = new DataWatcher(null);

		packet.a = entityId;
		packet.b = 30;
		packet.c = (int) (getLocation().getX() * 32);
		packet.d = (int) ((getLocation().getY() + -2.1 + ((double) textRow * 0.285)) * 32);
		packet.e = (int) (getLocation().getZ() * 32);
		packet.l = watcher;

		// Setup datawatcher for armor stand
		watcher.a(0, (byte) 32);
		watcher.a(2, lineOfText);
		watcher.a(3, (byte) 1);
		// watcher.a(10, (byte) 16); // TODO Uncomment after we can enforce 1.8.3
		// Also correct hologram positioning

		return new Packet[]
			{
				packet
			};
	}

	/**
	 * Removes the player from the Hologram so they are no longer effected by Whitelist or Blacklist
	 */
	public Hologram removePlayer(Player player)
	{
		return addPlayer(player.getName());
	}

	/**
	 * Removes the player from the Hologram so they are no longer effected by Whitelist or Blacklist
	 */
	public Hologram removePlayer(String player)
	{
		_playersInList.remove(player);
		return this;
	}

	/**
	 * If the entity moves, the hologram will update its position to appear relative to the movement.
	 * 
	 * @Please note the hologram updates every tick.
	 */
	public Hologram setFollowEntity(Entity entityToFollow)
	{
		_followEntity = entityToFollow;
		relativeToEntity = entityToFollow == null ? null : this._location.clone().subtract(entityToFollow.getLocation())
				.toVector();

		return this;
	}

	/**
	 * Set who can see the hologram
	 * 
	 * @Whitelist = Only people added can see the hologram
	 * @Blacklist = Anyone but people added can see the hologram
	 */
	public Hologram setHologramTarget(HologramTarget newTarget)
	{
		this._target = newTarget;
		return this;
	}

	/**
	 * Sets the hologram to appear at this location
	 */
	public Hologram setLocation(Location newLocation)
	{
		_makeSpawnPackets = true;

		Location oldLocation = getLocation();
		_location = newLocation.clone();

		if (getEntityFollowing() != null)
		{
			relativeToEntity = _location.clone().subtract(getEntityFollowing().getLocation()).toVector();
		}
		if (isInUse())
		{
			ArrayList<Player> canSee = getNearbyPlayers();
			Iterator<Player> itel = _playersTracking.iterator();

			while (itel.hasNext())
			{
				Player player = itel.next();
				if (!canSee.contains(player))
				{
					itel.remove();

					if (player.getWorld() == getLocation().getWorld())
					{
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(getDestroyPacket(player));
					}
				}
			}
			itel = canSee.iterator();
			while (itel.hasNext())
			{
				Player player = itel.next();

				if (!_playersTracking.contains(player))
				{
					_playersTracking.add(player);
					itel.remove();

					for (Packet packet : getSpawnPackets(player))
					{
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}
			if (!canSee.isEmpty())
			{
				_lastMovement.add(new Vector(newLocation.getX() - oldLocation.getX(), newLocation.getY() - oldLocation.getY(),
						newLocation.getZ() - oldLocation.getZ()));

				int x = (int) Math.floor(32 * _lastMovement.getX());
				int y = (int) Math.floor(32 * _lastMovement.getY());
				int z = (int) Math.floor(32 * _lastMovement.getZ());

				Packet[] packets1_7 = new Packet[_hologramText.length];
				Packet[] packets1_8 = new Packet[_hologramText.length];

				int i = 0;

				if (x >= -128 && x <= 127 && y >= -128 && y <= 127 && z >= -128 && z <= 127)
				{
					_lastMovement.subtract(new Vector(x / 32D, y / 32D, z / 32D));
					for (Entry<Integer, Integer> entityId : this._entityIds)
					{
						PacketPlayOutRelEntityMove relMove = new PacketPlayOutRelEntityMove();

						relMove.a = entityId.getKey();
						relMove.b = (byte) x;
						relMove.c = (byte) y;
						relMove.d = (byte) z;

						packets1_7[i] = relMove;
						packets1_8[i] = relMove;
						i++;
					}
				}
				else
				{
					x = (int) Math.floor(32 * newLocation.getX());
					z = (int) Math.floor(32 * newLocation.getZ());

					_lastMovement = new Vector(newLocation.getX() - (x / 32D), 0, newLocation.getZ() - (z / 32D));

					for (Entry<Integer, Integer> entityId : this._entityIds)
					{
						for (int b = 0; b < 2; b++)
						{
							PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport();
							teleportPacket.a = entityId.getKey();
							teleportPacket.b = x;
							teleportPacket.c = (int) Math
									.floor((oldLocation.getY() + (b == 0 ? 54.6 : -2.1) + ((double) i * 0.285)) * 32);
							teleportPacket.d = z;

							if (b == 0)
							{
								packets1_7[i] = teleportPacket;
							}
							else
							{
								packets1_8[i] = teleportPacket;
							}
						}

						i++;
					}
				}

				for (Player player : canSee)
				{
					for (Packet packet : UtilPlayer.is1_8(player) ? packets1_8 : packets1_7)
					{
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}
		}
		return this;
	}

	public Hologram setRemoveOnEntityDeath()
	{
		_removeEntityDeath = true;
		return this;
	}

	/**
	 * Set the hologram text
	 */
	public Hologram setText(String... newLines)
	{
		String[] newText = new String[newLines.length];

		for (int i = 0; i < newText.length; i++)
		{
			newText[i] = newLines[newText.length - (i + 1)];
		}

		if (newText.equals(_hologramText))
			return this;

		_makeSpawnPackets = true;

		if (isInUse())
		{
			int[] destroy1_7 = new int[0];
			int[] destroy1_8 = new int[0];

			ArrayList<Packet> packets1_7 = new ArrayList<Packet>();
			ArrayList<Packet> packets1_8 = new ArrayList<Packet>();

			if (_hologramText.length != newText.length)
			{
				_makeDestroyPackets = true;
			}

			for (int i = 0; i < Math.max(_hologramText.length, newText.length); i++)
			{
				// If more lines than previously
				if (i >= _hologramText.length)
				{
					// Add entity id and send spawn packets
					// You add a entity id because the new hologram needs
					Entry<Integer, Integer> entry = new HashMap.SimpleEntry(UtilEnt.getNewEntityId(), UtilEnt.getNewEntityId());
					_entityIds.add(entry);

					packets1_7.addAll(Arrays.asList(makeSpawnPackets1_7(i, entry.getKey(), entry.getValue(), newText[i])));

					packets1_8.addAll(Arrays.asList(makeSpawnPackets1_8(i, entry.getKey(), newText[i])));
				}
				// If less lines than previously
				else if (i >= newText.length)
				{
					// Remove entity id and send destroy packets
					Entry<Integer, Integer> entry = _entityIds.remove(newText.length);

					destroy1_7 = Arrays.copyOf(destroy1_7, destroy1_7.length + 2);

					destroy1_7[destroy1_7.length - 2] = entry.getKey();
					destroy1_7[destroy1_7.length - 1] = entry.getValue();

					destroy1_8 = Arrays.copyOf(destroy1_8, destroy1_8.length + 1);
					destroy1_8[destroy1_8.length - 1] = entry.getKey();
				}
				else if (!newText[i].equals(_hologramText[i]))
				{
					// Send update metadata packets
					Entry<Integer, Integer> entry = _entityIds.get(i);
					PacketPlayOutEntityMetadata metadata1_7 = new PacketPlayOutEntityMetadata();

					metadata1_7.a = entry.getValue();

					DataWatcher watcher1_7 = new DataWatcher(null);

					watcher1_7.a(0, (byte) 0);
					watcher1_7.a(1, (short) 300);
					watcher1_7.a(10, newText[i]);
					watcher1_7.a(11, (byte) 1);
					watcher1_7.a(12, -1700000);

					metadata1_7.b = watcher1_7.c();

					packets1_7.add(metadata1_7);

					PacketPlayOutEntityMetadata metadata1_8 = new PacketPlayOutEntityMetadata();

					metadata1_8.a = entry.getKey();

					DataWatcher watcher1_8 = new DataWatcher(null);

					watcher1_8.a(0, (byte) 32);
					watcher1_8.a(2, newText[i]);
					watcher1_8.a(3, (byte) 1);
					// watcher1_8.a(10, (byte) 16);// TODO Uncomment after we can enforce 1.8.3
					// Also correct hologram positioning
					metadata1_8.b = watcher1_8.c();

					packets1_8.add(metadata1_8);
				}
			}

			if (destroy1_7.length > 0)
			{
				packets1_7.add(new PacketPlayOutEntityDestroy(destroy1_7));
			}

			if (destroy1_8.length > 0)
			{
				packets1_8.add(new PacketPlayOutEntityDestroy(destroy1_8));
			}

			for (Player player : _playersTracking)
			{
				for (Packet packet : UtilPlayer.is1_8(player) ? packets1_8 : packets1_7)
				{
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
				}
			}
		}

		_hologramText = newText;

		return this;
	}

	/**
	 * Set the distance the hologram is viewable from. Default is 70
	 */
	public Hologram setViewDistance(int newDistance)
	{
		this._viewDistance = newDistance;
		return setLocation(getLocation());
	}

	/**
	 * Start the hologram
	 */
	public Hologram start()
	{
		if (!isInUse())
		{
			_hologramManager.addHologram(this);
			_playersTracking.addAll(getNearbyPlayers());

			for (Player player : _playersTracking)
			{
				for (Packet packet : getSpawnPackets(player))
				{
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
				}
			}

			_lastMovement = new Vector();
		}
		return this;
	}

	/**
	 * Stop the hologram
	 */
	public Hologram stop()
	{
		if (isInUse())
		{
			_hologramManager.removeHologram(this);

			for (Player player : _playersTracking)
			{
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(getDestroyPacket(player));
			}

			_playersTracking.clear();
			_lastMovement = null;
		}
		return this;
	}

}
