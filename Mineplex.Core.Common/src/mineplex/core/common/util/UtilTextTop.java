package mineplex.core.common.util;

import mineplex.core.common.DummyEntity;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class UtilTextTop 
{
	//Base Commands
	public static void display(String text, Player... players)
	{
		displayProgress(text, 1, players);
	}
	
	public static void displayProgress(String text, double progress, Player... players)
	{
		for (Player player : players)
			displayTextBar(player, progress, text);
	}
	
	//Logic
	public static final int EntityDragonId = 777777;
	public static final int EntityWitherId = 777778;
	
	//Display
	public static void displayTextBar(final Player player, double healthPercent, String text)
	{
		deleteOld(player);
		
		healthPercent = Math.min(1, healthPercent);
		boolean halfHealth = UtilPlayer.is1_8(player);
		
		//Display Dragon
		{
			Location loc = player.getLocation().subtract(0, 200, 0);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(getDragonPacket(text, healthPercent, halfHealth, loc));
		}
		
	
		//Display Wither (as well as Dragon)
		if (UtilPlayer.is1_8(player))
		{
			Location loc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(24));
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(getWitherPacket(text, healthPercent, halfHealth, loc));
		}
		
		//Remove
		Bukkit.getServer().getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
		{
			public void run()
			{
				deleteOld(player);
			}
		}, 20);
	}

	private static void deleteOld(Player player)
	{
		//Delete Dragon (All Clients)
		PacketPlayOutEntityDestroy destroyDragonPacket = new PacketPlayOutEntityDestroy(EntityDragonId);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(destroyDragonPacket); 
		
		//Delete Wither (1.8+ Only)
		if (UtilPlayer.is1_8(player))
		{
			PacketPlayOutEntityDestroy destroyWitherPacket = new PacketPlayOutEntityDestroy(EntityWitherId);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(destroyWitherPacket); 
		}
	}
	
	public static PacketPlayOutSpawnEntityLiving getDragonPacket(String text, double healthPercent, boolean halfHealth, Location loc)
	{
		PacketPlayOutSpawnEntityLiving mobPacket = new PacketPlayOutSpawnEntityLiving();

		mobPacket.a = (int) EntityDragonId; 							//Entity ID
		mobPacket.b = (byte) EntityType.ENDER_DRAGON.getTypeId(); 		//Mob type
		mobPacket.c = (int) Math.floor(loc.getBlockX() * 32.0D); 		//X position
		mobPacket.d = (int) MathHelper.floor(loc.getBlockY() * 32.0D); 	//Y position
		mobPacket.e = (int) Math.floor(loc.getBlockZ() * 32.0D); 		//Z position
		mobPacket.f = (byte) 0; 										//Pitch
		mobPacket.g = (byte) 0; 										//Head Pitch
		mobPacket.h = (byte) 0; 										//Yaw
		mobPacket.i = (short) 0; 										//X velocity
		mobPacket.j = (short) 0; 										//Y velocity
		mobPacket.k = (short) 0; 										//Z velocity
	
		//Health
		double health = healthPercent * 199.9 + 0.1;
		//if (halfHealth)
		//	health = healthPercent * 99 + 101;
 
		//Watcher
		DataWatcher watcher = getWatcher(text, health, loc.getWorld());
		mobPacket.l = watcher;

		return mobPacket;
	}
	
	public static PacketPlayOutSpawnEntityLiving getWitherPacket(String text, double healthPercent, boolean halfHealth, Location loc)
	{
		PacketPlayOutSpawnEntityLiving mobPacket = new PacketPlayOutSpawnEntityLiving();

		mobPacket.a = (int) EntityWitherId; 							//Entity ID
		mobPacket.b = (byte) EntityType.WITHER.getTypeId(); 			//Mob type
		mobPacket.c = (int) Math.floor(loc.getBlockX() * 32.0D); 		//X position
		mobPacket.d = (int) MathHelper.floor(loc.getBlockY() * 32.0D); 	//Y position
		mobPacket.e = (int) Math.floor(loc.getBlockZ() * 32.0D); 		//Z position
		mobPacket.f = (byte) 0; 										//Pitch
		mobPacket.g = (byte) 0; 										//Head Pitch
		mobPacket.h = (byte) 0; 										//Yaw
		mobPacket.i = (short) 0; 										//X velocity
		mobPacket.j = (short) 0; 										//Y velocity
		mobPacket.k = (short) 0; 										//Z velocity
	
		//Health
		double health = healthPercent * 299.9 + 0.1;
		//if (halfHealth)
		//	health = healthPercent * 149 + 151;
		 
		//Watcher
		DataWatcher watcher = getWatcher(text, health, loc.getWorld());
		mobPacket.l = watcher;

		return mobPacket;
	}
	
	public static DataWatcher getWatcher(String text, double health, World world)
	{
		DataWatcher watcher = new DataWatcher(new DummyEntity(((CraftWorld)world).getHandle()));

		watcher.a(0, (Byte) (byte) 0); 			//Flags, 0x20 = invisible
		watcher.a(6, (Float) (float) health);
		watcher.a(2, (String) text); 			//Entity name
		watcher.a(10, (String) text); 			//Entity name
		watcher.a(3, (Byte) (byte) 0); 			//Show name, 1 = show, 0 = don't show
		watcher.a(11, (Byte) (byte) 0); 		//Show name, 1 = show, 0 = don't show
		watcher.a(16, (Integer) (int) health); 	//Health
		watcher.a(20, (Integer) (int) 881); 		//Inv
		
	    int i1 = watcher.getInt(0);
	    watcher.watch(0, Byte.valueOf((byte)(i1 | 1 << 5)));

		return watcher;
	}
}