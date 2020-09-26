package ehnetwork.core.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class UtilPlayer
{
	private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max)
	{
		final double epsilon = 0.0001f;

		Vector3D d = p2.subtract(p1).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();

		if (Math.abs(c.x) > e.x + ad.x)
			return false;
		if (Math.abs(c.y) > e.y + ad.y)
			return false;
		if (Math.abs(c.z) > e.z + ad.z)
			return false;

		if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
			return false;
		if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
			return false;
		if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
			return false;

		return true;
	}

	private static class Vector3D
	{

		// Use protected members, like Bukkit
		private final double x;
		private final double y;
		private final double z;

		private Vector3D(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		private Vector3D(Location location)
		{
			this(location.toVector());
		}

		private Vector3D(Vector vector)
		{
			if (vector == null)
				throw new IllegalArgumentException("Vector cannot be NULL.");
			this.x = vector.getX();
			this.y = vector.getY();
			this.z = vector.getZ();
		}

		private Vector3D abs()
		{
			return new Vector3D(Math.abs(x), Math.abs(y), Math.abs(z));
		}

		private Vector3D add(double x, double y, double z)
		{
			return new Vector3D(this.x + x, this.y + y, this.z + z);
		}

		private Vector3D add(Vector3D other)
		{
			if (other == null)
				throw new IllegalArgumentException("other cannot be NULL");

			return new Vector3D(x + other.x, y + other.y, z + other.z);
		}

		private Vector3D multiply(double factor)
		{
			return new Vector3D(x * factor, y * factor, z * factor);
		}

		private Vector3D multiply(int factor)
		{
			return new Vector3D(x * factor, y * factor, z * factor);
		}

		private Vector3D subtract(Vector3D other)
		{
			if (other == null)
				throw new IllegalArgumentException("other cannot be NULL");
			return new Vector3D(x - other.x, y - other.y, z - other.z);
		}
	}

	public static Player getPlayerInSight(Player p, int range, boolean lineOfSight)
	{
		Location observerPos = p.getEyeLocation();
		Vector3D observerDir = new Vector3D(observerPos.getDirection());
		Vector3D observerStart = new Vector3D(observerPos);
		Vector3D observerEnd = observerStart.add(observerDir.multiply(range));

		Player hit = null;

		for (Entity entity : p.getNearbyEntities(range, range, range))
		{

			if (entity == p || UtilPlayer.isSpectator(entity))
				continue;

			double theirDist = p.getEyeLocation().distance(entity.getLocation());

			if (lineOfSight
					&& p.getLastTwoTargetBlocks(UtilBlock.blockAirFoliageSet, (int) Math.ceil(theirDist)).get(0).getLocation()
					.distance(p.getEyeLocation()) + 1 < theirDist)
				continue;

			Vector3D targetPos = new Vector3D(entity.getLocation());
			Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
			Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

			if (hasIntersection(observerStart, observerEnd, minimum, maximum))
			{
				if (hit == null
						|| hit.getLocation().distanceSquared(observerPos) > entity.getLocation().distanceSquared(observerPos))
				{
					hit = (Player) entity;
				}
			}
		}
		return hit;
	}

	/**
	 * AviodAllies doesn't work. Leaving as a param as it sounds like something you may want in the future.
	 */
	public static Entity getEntityInSight(Player player, int rangeToScan, boolean avoidAllies, boolean avoidNonLiving,
			boolean lineOfSight, float expandBoxesPercentage)
	{
		Location observerPos = player.getEyeLocation();
		Vector3D observerDir = new Vector3D(observerPos.getDirection());
		Vector3D observerStart = new Vector3D(observerPos);
		Vector3D observerEnd = observerStart.add(observerDir.multiply(rangeToScan));

		Entity hit = null;

		for (Entity entity : player.getNearbyEntities(rangeToScan, rangeToScan, rangeToScan))
		{
			if (entity == player || UtilPlayer.isSpectator(entity))
				continue;

			if (avoidNonLiving && !(entity instanceof LivingEntity))
				continue;

			double theirDist = player.getEyeLocation().distance(entity.getLocation());
			if (lineOfSight
					&& player.getLastTwoTargetBlocks(UtilBlock.blockAirFoliageSet, (int) Math.ceil(theirDist)).get(0)
					.getLocation().distance(player.getEyeLocation()) + 1 < theirDist)
				continue;

			Vector3D targetPos = new Vector3D(entity.getLocation());

			float width = (((CraftEntity) entity).getHandle().width / 1.8F) * expandBoxesPercentage;

			Vector3D minimum = targetPos.add(-width, -0.1 / expandBoxesPercentage, -width);
			Vector3D maximum = targetPos.add(width, ((CraftEntity) entity).getHandle().length * expandBoxesPercentage, width);

			if (hasIntersection(observerStart, observerEnd, minimum, maximum))
			{
				if (hit == null
						|| hit.getLocation().distanceSquared(observerPos) > entity.getLocation().distanceSquared(observerPos))
				{
					hit = entity;
				}
			}
		}
		return hit;
	}

	public static void message(Entity client, LinkedList<String> messageList)
	{
		message(client, messageList, false);
	}

	public static void message(Entity client, String message)
	{
		message(client, message, false);
	}

	public static void message(Entity client, LinkedList<String> messageList, boolean wiki)
	{
		for (String curMessage : messageList)
		{
			message(client, curMessage, wiki);
		}
	}

	public static void message(Entity client, String message, boolean wiki)
	{
		if (client == null)
			return;

		if (!(client instanceof Player))
			return;

		/*
        if (wiki)
        	message = UtilWiki.link(message);
		 */

		((Player) client).sendMessage(message);
	}

	public static Player searchExact(String name)
	{
		for (Player cur : UtilServer.getPlayers())
			if (cur.getName().equalsIgnoreCase(name))
				return cur;

		return null;
	}

	public static Player searchExact(UUID uuid)
	{
		return UtilServer.getServer().getPlayer(uuid);
	}

	public static String searchCollection(Player caller, String player, Collection<String> coll, String collName, boolean inform)
	{
		LinkedList<String> matchList = new LinkedList<String>();

		for (String cur : coll)
		{
			if (cur.equalsIgnoreCase(player))
				return cur;

			if (cur.toLowerCase().contains(player.toLowerCase()))
				matchList.add(cur);
		}

		// No / Non-Unique
		if (matchList.size() != 1)
		{
			if (!inform)
				return null;

			// Inform
			message(caller,
					F.main(collName + " Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem + player
							+ C.mBody + "]."));

			if (matchList.size() > 0)
			{
				String matchString = "";
				for (String cur : matchList)
					matchString += cur + " ";

				message(caller,
						F.main(collName + " Search", "" + C.mBody + " Matches [" + C.mElem + matchString + C.mBody + "]."));
			}

			return null;
		}

		return matchList.get(0);
	}

	public static Player searchOnline(Player caller, String player, boolean inform)
	{
		LinkedList<Player> matchList = new LinkedList<Player>();

		for (Player cur : UtilServer.getPlayers())
		{
			if (cur.getName().equalsIgnoreCase(player))
				return cur;

			if (cur.getName().toLowerCase().contains(player.toLowerCase()))
				matchList.add(cur);
		}

		// No / Non-Unique
		if (matchList.size() != 1)
		{
			if (!inform)
				return null;

			// Inform
			message(caller,
					F.main("Online Player Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem
							+ player + C.mBody + "]."));

			if (matchList.size() > 0)
			{
				String matchString = "";
				for (Player cur : matchList)
					matchString += F.elem(cur.getName()) + ", ";
				if (matchString.length() > 1)
					matchString = matchString.substring(0, matchString.length() - 2);

				message(caller,
						F.main("Online Player Search", "" + C.mBody + "Matches [" + C.mElem + matchString + C.mBody + "]."));
			}

			return null;
		}

		return matchList.get(0);
	}

	public static void searchOffline(List<String> matches, final Callback<String> callback, final Player caller,
			final String player, final boolean inform)
	{
		// No / Non-Unique
		if (matches.size() != 1)
		{
			if (!inform || !caller.isOnline())
			{
				callback.run(null);
				return;
			}

			// Inform
			message(caller,
					F.main("Offline Player Search", "" + C.mCount + matches.size() + C.mBody + " matches for [" + C.mElem
							+ player + C.mBody + "]."));

			if (matches.size() > 0)
			{
				String matchString = "";
				for (String cur : matches)
					matchString += cur + " ";
				if (matchString.length() > 1)
					matchString = matchString.substring(0, matchString.length() - 1);

				message(caller,
						F.main("Offline Player Search", "" + C.mBody + "Matches [" + C.mElem + matchString + C.mBody + "]."));
			}

			callback.run(null);
			return;
		}

		callback.run(matches.get(0));
	}

	public static LinkedList<Player> matchOnline(Player caller, String players, boolean inform)
	{
		LinkedList<Player> matchList = new LinkedList<Player>();

		String failList = "";

		for (String cur : players.split(","))
		{
			Player match = searchOnline(caller, cur, inform);

			if (match != null)
				matchList.add(match);

			else
				failList += cur + " ";
		}

		if (inform && failList.length() > 0)
		{
			failList = failList.substring(0, failList.length() - 1);
			message(caller, F.main("Online Player(s) Search", "" + C.mBody + "Invalid [" + C.mElem + failList + C.mBody + "]."));
		}

		return matchList;
	}

	public static LinkedList<Player> getNearby(Location loc, double maxDist)
	{
		LinkedList<Player> nearbyMap = new LinkedList<Player>();

		for (Player cur : loc.getWorld().getPlayers())
		{
			if (UtilPlayer.isSpectator(cur))
				continue;

			if (cur.isDead())
				continue;

			double dist = loc.toVector().subtract(cur.getLocation().toVector()).length();

			if (dist > maxDist)
				continue;

			for (int i = 0; i < nearbyMap.size(); i++)
			{
				if (dist < loc.toVector().subtract(nearbyMap.get(i).getLocation().toVector()).length())
				{
					nearbyMap.add(i, cur);
					break;
				}
			}

			if (!nearbyMap.contains(cur))
				nearbyMap.addLast(cur);
		}

		return nearbyMap;
	}

	public static Player getClosest(Location loc, Collection<Player> ignore)
	{
		Player best = null;
		double bestDist = 0;

		for (Player cur : loc.getWorld().getPlayers())
		{
			if (UtilPlayer.isSpectator(cur))
				continue;

			if (cur.isDead())
				continue;

			if (ignore != null && ignore.contains(cur))
				continue;

			double dist = UtilMath.offset(cur.getLocation(), loc);

			if (best == null || dist < bestDist)
			{
				best = cur;
				bestDist = dist;
			}
		}

		return best;
	}

	public static Player getClosest(Location loc, Entity ignore)
	{
		Player best = null;
		double bestDist = 0;

		for (Player cur : loc.getWorld().getPlayers())
		{
			if (UtilPlayer.isSpectator(cur))
				continue;

			if (cur.isDead())
				continue;

			if (ignore != null && ignore.equals(cur))
				continue;

			double dist = UtilMath.offset(cur.getLocation(), loc);

			if (best == null || dist < bestDist)
			{
				best = cur;
				bestDist = dist;
			}
		}

		return best;
	}

	public static void kick(Player player, String module, String message)
	{
		kick(player, module, message, true);
	}

	public static void kick(Player player, String module, String message, boolean log)
	{
		if (player == null)
			return;

		String out = ChatColor.RED + module + ChatColor.WHITE + " - " + ChatColor.YELLOW + message;
		player.kickPlayer(out);

		// Log
		if (log)
			System.out.println("Kicked Client [" + player.getName() + "] for [" + module + " - " + message + "]");
	}

	public static HashMap<Player, Double> getInRadius(Location loc, double dR)
	{
		HashMap<Player, Double> players = new HashMap<Player, Double>();

		for (Player cur : loc.getWorld().getPlayers())
		{
			if (UtilPlayer.isSpectator(cur))
				continue;

			double offset = UtilMath.offset(loc, cur.getLocation());

			if (offset < dR)
				players.put(cur, 1 - (offset / dR));
		}

		return players;
	}

	public static HashMap<Player, Double> getPlayersInPyramid(Player player, double angleLimit, double distance)
	{
		HashMap<Player, Double> players = new HashMap<Player, Double>();

		for (Player cur : player.getWorld().getPlayers())
		{
			if (UtilPlayer.isSpectator(cur))
				continue;

			//Get lower offset (eye to eye, eye to feet)
			double offset = Math.min(UtilMath.offset(player.getEyeLocation(), cur.getEyeLocation()), 
					UtilMath.offset(player.getEyeLocation(), cur.getLocation()));

			if (offset < distance && UtilAlg.isTargetInPlayerPyramid(player, cur, angleLimit))
				players.put(cur, 1 - (offset / distance));
		}

		return players;
	}

	public static void health(Player player, double mod)
	{
		if (player.isDead())
			return;

		double health = player.getHealth() + mod;

		if (health < 0)
			health = 0;

		if (health > player.getMaxHealth())
			health = player.getMaxHealth();

		player.setHealth(health);
	}

	public static void hunger(Player player, int mod)
	{
		if (player.isDead())
			return;

		int hunger = player.getFoodLevel() + mod;

		if (hunger < 0)
			hunger = 0;

		if (hunger > 20)
			hunger = 20;

		player.setFoodLevel(hunger);
	}

	public static boolean isOnline(String name)
	{
		return (searchExact(name) != null);
	}

	public static String safeNameLength(String name)
	{
		if (name.length() > 16)
			name = name.substring(0, 16);

		return name;
	}

	public static boolean isChargingBow(Player player)
	{
		if (!UtilGear.isMat(player.getItemInHand(), Material.BOW))
			return false;

		return (((CraftEntity) player).getHandle().getDataWatcher().getByte(0) & 1 << 4) != 0;
	}

	public static boolean is1_8(Player player)
	{
		return ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() >= 47;
	}

	public static void sendPacket(Player player, Packet... packets)
	{
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

		for (Packet packet : packets)
		{
			connection.sendPacket(packet);
		}
	}

	public static boolean isSpectator(Entity player)
	{
		if (player instanceof Player)
			return ((CraftPlayer) player).getHandle().spectating;
		return false;
	}

	/*
    public void setListName(Player player, CoreClient client) 
    {
    	StringBuilder playerNameBuilder = new StringBuilder();

    	String prefixChar = "*";

    	if (client.NAC().IsUsing())							playerNameBuilder.append(ChatColor.GREEN + prefixChar);
    	else												playerNameBuilder.append(ChatColor.DARK_GRAY + prefixChar);

    	if (client.Rank().Has(Rank.OWNER, false))			playerNameBuilder.append(ChatColor.AQUA + prefixChar + ChatColor.RED);
    	else if (client.Rank().Has(Rank.MODERATOR, false))	playerNameBuilder.append(ChatColor.AQUA + prefixChar + ChatColor.GOLD);
    	else if (client.Rank().Has(Rank.DIAMOND, false))	playerNameBuilder.append(ChatColor.AQUA + prefixChar + ChatColor.WHITE);
    	else if (client.Rank().Has(Rank.EMERALD, false))	playerNameBuilder.append(ChatColor.GREEN + prefixChar + ChatColor.WHITE);
    	else if (client.Donor().HasDonated())				playerNameBuilder.append(ChatColor.YELLOW + prefixChar + ChatColor.WHITE);
    	else												playerNameBuilder.append(ChatColor.DARK_GRAY + prefixChar + ChatColor.WHITE);

    	playerNameBuilder.append(player.getName());

    	String playerName = playerNameBuilder.toString();

    	if (playerNameBuilder.length() > 16)
    	{
    		playerName = playerNameBuilder.substring(0, 16);
    	}

    	player.setPlayerListName(playerName);
    }
	 */
}
