package mineplex.core.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.BlockContainer;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.ChunkCoordIntPair;
import net.minecraft.server.v1_7_R4.ChunkSection;
import net.minecraft.server.v1_7_R4.EnumSkyBlock;
import net.minecraft.server.v1_7_R4.ExceptionWorldConflict;
import net.minecraft.server.v1_7_R4.IContainer;
import net.minecraft.server.v1_7_R4.IProgressUpdate;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PacketPlayOutMapChunkBulk;
import net.minecraft.server.v1_7_R4.PacketPlayOutMultiBlockChange;
import net.minecraft.server.v1_7_R4.RegionFile;
import net.minecraft.server.v1_7_R4.RegionFileCache;
import net.minecraft.server.v1_7_R4.TileEntity;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MapUtil
{
	public static void ReplaceOreInChunk(Chunk chunk, Material replacee, Material replacer)
	{
		net.minecraft.server.v1_7_R4.Chunk c = ((CraftChunk) chunk).getHandle();

		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 18; y++)
				{
					int bX = c.locX << 4 | x & 0xF;
					int bY = y & 0xFF;
					int bZ = c.locZ << 4 | z & 0xF;

					if (c.getType(bX & 0xF, bY, bZ & 0xF).k() == replacee.getId())
					{
						c.b(bX & 0xF, bY, bZ & 0xF, replacer.getId());
					}
				}
			}
		}

		c.initLighting();
	}

	public static void QuickChangeBlockAt(Location location, Material setTo)
	{
		QuickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), setTo);
	}

	public static void QuickChangeBlockAt(Location location, int id, byte data)
	{
		QuickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), id,
				data);
	}

	public static void QuickChangeBlockAt(World world, int x, int y, int z, Material setTo)
	{
		QuickChangeBlockAt(world, x, y, z, setTo, 0);
	}

	public static void QuickChangeBlockAt(World world, int x, int y, int z, Material setTo, int data)
	{
		QuickChangeBlockAt(world, x, y, z, setTo.getId(), data);
	}

	public static void QuickChangeBlockAt(World world, int x, int y, int z, int id, int data)
	{
		Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
		net.minecraft.server.v1_7_R4.Chunk c = ((CraftChunk) chunk).getHandle();

		c.a(x & 0xF, y, z & 0xF, Block.getById(id), data);
		((CraftWorld) world).getHandle().notify(x, y, z);
	}

	public static int GetHighestBlockInCircleAt(World world, int bx, int bz, int radius)
	{
		int count = 0;
		int totalHeight = 0;

		final double invRadiusX = 1 / radius;
		final double invRadiusZ = 1 / radius;

		final int ceilRadiusX = (int) Math.ceil(radius);
		final int ceilRadiusZ = (int) Math.ceil(radius);

		double nextXn = 0;
		forX: for (int x = 0; x <= ceilRadiusX; ++x)
		{
			final double xn = nextXn;
			nextXn = (x + 1) * invRadiusX;
			double nextZn = 0;
			forZ: for (int z = 0; z <= ceilRadiusZ; ++z)
			{
				final double zn = nextZn;
				nextZn = (z + 1) * invRadiusZ;

				double distanceSq = xn * xn + zn * zn;
				if (distanceSq > 1)
				{
					if (z == 0)
					{
						break forX;
					}
					break forZ;
				}

				totalHeight += world.getHighestBlockAt(bx + x, bz + z).getY();
				count++;
			}
		}

		return totalHeight / count;
	}

	public static void ChunkBlockChange(Location location, int id, byte data, boolean notifyPlayers)
	{
		ChunkBlockChange(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), id,
				data, notifyPlayers);
	}

	public static void ChunkBlockChange(World world, int x, int y, int z, int id, byte data, boolean notifyPlayers)
	{
		if (changeChunkBlock(x & 15, y, z & 15, ((CraftWorld) world).getHandle().getChunkAt(x >> 4, z >> 4),
				Block.getById(id), data))
		{
			if (notifyPlayers)
				((CraftWorld) world).getHandle().notify(x, y, z);
		}
	}
	
	public static void ChunkBlockSet(World world, int x, int y, int z, int id, byte data, boolean notifyPlayers)
	{
		world.getBlockAt(x, y, z).setTypeIdAndData(id, data, notifyPlayers);
	}

	private static boolean changeChunkBlock(int x, int y, int z, net.minecraft.server.v1_7_R4.Chunk chunk, Block block,
			byte data)
	{
		return chunk.a(x, y, z, block, data);
	}

	public static void SendChunkForPlayer(net.minecraft.server.v1_7_R4.Chunk chunk, Player player)
	{
		SendChunkForPlayer(chunk.locX, chunk.locZ, player);
	}

	@SuppressWarnings("unchecked")
	public static void SendChunkForPlayer(int x, int z, Player player)
	{
		// System.out.println("Sending Chunk " + x + ", " + z);
		((CraftPlayer) player).getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(x, z));
	}

	@SuppressWarnings("unchecked")
	public static void SendMultiBlockForPlayer(int x, int z, short[] dirtyBlocks, int dirtyCount, World world,
			Player player)
	{
		// System.out.println("Sending MultiBlockChunk " + x + ", " + z);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutMultiBlockChange(dirtyCount,
				dirtyBlocks, ((CraftWorld) world).getHandle().getChunkAt(x, z)));
	}

	public static void UnloadWorld(JavaPlugin plugin, World world)
	{
		UnloadWorld(plugin, world, false);
	}

	public static void UnloadWorld(JavaPlugin plugin, World world, boolean save)
	{
		if (save)
		{
			try
			{
				((CraftWorld) world).getHandle().save(true, (IProgressUpdate) null);
			}
			catch (ExceptionWorldConflict e)
			{
				e.printStackTrace();
			}
			
			((CraftWorld) world).getHandle().saveLevel();
		}

		world.setAutoSave(save);

		CraftServer server = (CraftServer) plugin.getServer();
		CraftWorld craftWorld = (CraftWorld) world;

		Bukkit.getPluginManager().callEvent(new WorldUnloadEvent(((CraftWorld) world).getHandle().getWorld()));

		Iterator<net.minecraft.server.v1_7_R4.Chunk> chunkIterator = ((CraftWorld) world).getHandle().chunkProviderServer.chunks
				.values().iterator();

		for (Entity entity : world.getEntities())
		{
			entity.remove();
		}

		while (chunkIterator.hasNext())
		{
			net.minecraft.server.v1_7_R4.Chunk chunk = chunkIterator.next();
			chunk.removeEntities();
		}

		((CraftWorld) world).getHandle().chunkProviderServer.chunks.clear();
		((CraftWorld) world).getHandle().chunkProviderServer.unloadQueue.clear();

		try
		{
			Field f = server.getClass().getDeclaredField("worlds");
			f.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<String, World> worlds = (Map<String, World>) f.get(server);
			worlds.remove(world.getName().toLowerCase());
			f.setAccessible(false);
		}
		catch (IllegalAccessException ex)
		{
			System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
		}
		catch (NoSuchFieldException ex)
		{
			System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
		}

		MinecraftServer ms = null;

		try
		{
			Field f = server.getClass().getDeclaredField("console");
			f.setAccessible(true);
			ms = (MinecraftServer) f.get(server);
			f.setAccessible(false);
		}
		catch (IllegalAccessException ex)
		{
			System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
		}
		catch (NoSuchFieldException ex)
		{
			System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
		}

		ms.worlds.remove(ms.worlds.indexOf(craftWorld.getHandle()));
	}

	@SuppressWarnings({ "rawtypes" })
	public static boolean ClearWorldReferences(String worldName)
	{
		HashMap regionfiles = (HashMap) RegionFileCache.a;

		try
		{
			for (Iterator<Object> iterator = regionfiles.entrySet().iterator(); iterator.hasNext();)
			{
				Map.Entry e = (Map.Entry) iterator.next();
				RegionFile file = (RegionFile) e.getValue();

				try
				{
					file.c();
					iterator.remove();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println("Exception while removing world reference for '" + worldName + "'!");
			ex.printStackTrace();
		}

		return true;
	}
}
