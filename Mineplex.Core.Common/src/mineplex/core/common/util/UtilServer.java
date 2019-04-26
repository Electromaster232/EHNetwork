package mineplex.core.common.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UtilServer
{
	public static Player[] getPlayers()
	{
		return getServer().getOnlinePlayers().toArray(new Player[0]);
	}

	public static Server getServer()
	{
		return Bukkit.getServer();
	}
	
	public static void broadcast(String message)
	{
		for (Player cur : getPlayers())
			UtilPlayer.message(cur, message);
	}
	
	public static void broadcastSpecial(String event, String message)
	{
		for (Player cur : getPlayers())
		{
			UtilPlayer.message(cur, "§b§l" + event);
			UtilPlayer.message(cur, message);
			cur.playSound(cur.getLocation(), Sound.ORB_PICKUP, 2f, 0f);
			cur.playSound(cur.getLocation(), Sound.ORB_PICKUP, 2f, 0f);
		}
	}
	
	public static void broadcast(String sender, String message)
	{
		broadcast("§f§l" + sender + " " + "§b" + message);
	}
	
	public static void broadcastMagic(String sender, String message)
	{
		broadcast("§2§k" + message);
	}
	
	public static double getFilledPercent() 
	{
		return (double)getPlayers().length / (double)UtilServer.getServer().getMaxPlayers();
	}
}
