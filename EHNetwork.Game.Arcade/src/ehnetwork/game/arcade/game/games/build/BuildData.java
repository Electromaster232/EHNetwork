package ehnetwork.game.arcade.game.games.build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.minecraft.server.v1_7_R4.EntityLightning;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;

public class BuildData 
{
	public Player Player;
	
	public boolean Judged = false;
	
	public Location Spawn;
	
	public Location CornerA;
	public Location CornerB;

	public HashSet<Block> Blocks = new HashSet<Block>();
	
	public HashSet<Entity> Entities = new HashSet<Entity>();
	
	public HashSet<Entity> Items = new HashSet<Entity>();
	
	public NautHashMap<Location, ParticleType> Particles = new NautHashMap<Location, ParticleType>();
	
	public int Time = 6000;
	
	public HashSet<String> AbuseVotes = new HashSet<String>();
	public boolean IsAbusive = false;
	
	// This is used to show the player to use their inventory to grab items
	public boolean ClickedInventory = false;
	
	public WeatherType Weather = WeatherType.SUNNY;
	
	private double _totalPoints = 0; 
	
	public BuildData(Player player, Location spawn, ArrayList<Location> buildBorders)
	{
		Player = player;
		Spawn = spawn;
		
		CornerA = UtilAlg.findClosest(spawn, buildBorders);
		buildBorders.remove(CornerA);
		CornerB = UtilAlg.findClosest(spawn, buildBorders);
		buildBorders.remove(CornerB);
	}
	
	public boolean addItem(Item item) 
	{
		if (Items.size() >= 16)
		{
			UtilPlayer.message(Player, F.main("Game", "You cannot drop more than 16 Items!"));
			item.remove();
			return false;
		}
		
		Items.add(item);
		
		ItemMeta meta = item.getItemStack().getItemMeta();
		meta.setDisplayName(item.getUniqueId() + " NoStack");
		item.getItemStack().setItemMeta(meta);
		return true;
	}
	
	public boolean addParticles(ParticleType particleType) 
	{
		if (Particles.size() >= 24)
		{
			UtilPlayer.message(Player, F.main("Game", "You cannot spawn more than 24 Particles!"));
			return false;
		}
		
		Location toPlace = Player.getEyeLocation().add(Player.getLocation().getDirection());
		
		if (!inBuildArea(toPlace.getBlock()))
		{
			UtilPlayer.message(Player, F.main("Game", "You cannot place particles outside your plot!"));
			return false;
		}
		
		Particles.put(toPlace, particleType);
		
		UtilPlayer.message(Player, F.main("Game", "You placed " + particleType.getFriendlyName() + "!"));
		
		return true;
	}
	
	public void resetParticles() 
	{
		Particles.clear();
		
		UtilPlayer.message(Player, F.main("Game", "You cleared your Particles!"));
	}
	
	public boolean addEntity(Entity entity) 
	{
		if (entity instanceof Ghast)
		{
			UtilPlayer.message(Player, F.main("Game", "You cannot spawn Ghasts!"));
			
			entity.remove();
			return false;
		}
		
		if (Entities.size() >= 16)
		{
			UtilPlayer.message(Player, F.main("Game", "You cannot spawn more than 16 Entities!"));
			
			entity.remove();
			return false;
		}
		
		if (entity instanceof LivingEntity)
		{
			((LivingEntity)entity).setRemoveWhenFarAway(false);
			((LivingEntity)entity).setCustomName(UtilEnt.getName(entity));
			
		}
		
		Entities.add(entity);
		UtilEnt.Vegetate(entity, true);
		UtilEnt.ghost(entity, true, false);
		return true;
	}
	
	public void removeEntity(Entity entity) 
	{
		if (Entities.remove(entity))
		{
			entity.remove();
			
			UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, entity.getLocation().add(0, 0.5, 0), 0, 0, 0, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());
		}	
	}
	
	public void addBlock(Block block)
	{
		Blocks.add(block);
	}

	public boolean inBuildArea(Block block) 
	{
		if (block.getX() < Math.min(CornerA.getBlockX(), CornerB.getBlockX()))
			return false;
		
		if (block.getY() < Math.min(CornerA.getBlockY(), CornerB.getBlockY()))
			return false;
		
		if (block.getZ() < Math.min(CornerA.getBlockZ(), CornerB.getBlockZ()))
			return false;
		
		if (block.getX() > Math.max(CornerA.getBlockX(), CornerB.getBlockX()))
			return false;
		
		if (block.getY() > Math.max(CornerA.getBlockY(), CornerB.getBlockY()))
			return false;
		
		if (block.getZ() > Math.max(CornerA.getBlockZ(), CornerB.getBlockZ()))
			return false;
		
		return true;
	}

	public enum WeatherType
	{
		SUNNY, RAINING, STORMING;
	}

	public void clean() 
	{
		//Clean Ents
		Iterator<Entity> entIter = Entities.iterator();
		
		while (entIter.hasNext())
		{
			Entity ent = entIter.next();
			if (!ent.isValid() || !inBuildArea(ent.getLocation().getBlock()))
			{
				entIter.remove();
				ent.remove();
				UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, ent.getLocation().add(0, 0.5, 0), 0, 0, 0, 0, 1,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
				
		}
		
		//Clean Items
		Iterator<Entity> itemIter = Items.iterator();
		
		while (itemIter.hasNext())
		{
			Entity ent = itemIter.next();
			if (!ent.isValid() || !inBuildArea(ent.getLocation().getBlock()))
			{
				itemIter.remove();
				ent.remove();
				UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, ent.getLocation().add(0, 0.5, 0), 0, 0, 0, 0, 1,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
	}

	public void playParticles(boolean all) 
	{
		for (Location loc : Particles.keySet())
		{
			int amount = 8;
			
			ParticleType type = Particles.get(loc);
			
			if (type == ParticleType.HUGE_EXPLOSION ||
				type == ParticleType.LARGE_EXPLODE ||
				type == ParticleType.NOTE)
				amount = 1;
				
			if (all)
				UtilParticle.PlayParticle(type, loc, 0.4f, 0.4f, 0.4f, 0, amount,
						ViewDist.LONGER, UtilServer.getPlayers());
			else
				UtilParticle.PlayParticle(type, loc, 0.4f, 0.4f, 0.4f, 0, amount, 
						ViewDist.LONGER, Player);
		}
	}

	public void playWeather(boolean b)
	{
		org.bukkit.WeatherType type = org.bukkit.WeatherType.CLEAR;
		if (Weather == WeatherType.STORMING || Weather == WeatherType.RAINING)
			type = org.bukkit.WeatherType.DOWNFALL;
		
		if (b)
		{
			for (Player player : UtilServer.getPlayers())
			{
				playWeather(player, type);
			}
		}
		else
		{
			playWeather(Player, type);
		}
	}
	
	public void playWeather(Player player, org.bukkit.WeatherType type)
	{		
		player.setPlayerWeather(type);
		player.setPlayerTime(Time, false); 
		
		if (Weather == WeatherType.STORMING)
		{
			if (Math.random() > 0.7)
				player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 4f, 1f);
			
			//Strike Lightning Here
			if (Math.random() > 0.9)
			{
				Location loc = UtilBlock.getHighest(player.getWorld(),
						(int) (Spawn.getX() + Math.random() * 200 - 100),
						(int) (Spawn.getX() + Math.random() * 200 - 100)).getLocation();

				EntityLightning entity = new EntityLightning(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), true);
				PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather(entity);
				UtilPlayer.sendPacket(player, packet);
			}
		}
	}

	public void setGround(GroundData ground)
	{ 
		if (!Recharge.Instance.use(Player, "Change Ground", 2000, true, false))
		{
			Player.playSound(Player.getLocation(), Sound.NOTE_BASS_GUITAR, 1f, 0.1f);
			return;
		}
		
		Material mat = ground.getMaterial();
		byte data = ground.getData();

		if (mat == Material.LAVA_BUCKET) mat = Material.LAVA;
		else if (mat == Material.WATER_BUCKET) mat = Material.WATER;

		int y = Math.min(CornerA.getBlockY(), CornerB.getBlockY()) - 1;
		for (int x= Math.min(CornerA.getBlockX(), CornerB.getBlockX()) ; x <= Math.max(CornerA.getBlockX(), CornerB.getBlockX()) ; x++)
			for (int z= Math.min(CornerA.getBlockZ(), CornerB.getBlockZ()) ; z <= Math.max(CornerA.getBlockZ(), CornerB.getBlockZ()) ; z++)
			{
				MapUtil.QuickChangeBlockAt(Player.getWorld(), x, y, z, mat, data);
			}
	}

	public void addPoints(double d) 
	{
		if (IsAbusive)
			return;
		
		_totalPoints += d;
	}
	
	public double getPoints()
	{
		return _totalPoints;
	}

	public void clearPoints() 
	{
		_totalPoints = 0;
	}

	public void addAbuseVote(Player voter) 
	{
		AbuseVotes.add(voter.getName());
	}
	
	public void setAbusive()
	{
		IsAbusive = true;
	}
}
