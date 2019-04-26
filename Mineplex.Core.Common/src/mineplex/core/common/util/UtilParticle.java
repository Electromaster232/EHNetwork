package mineplex.core.common.util;

import java.lang.reflect.Field;

import mineplex.core.common.util.UtilParticle.ViewDist;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class UtilParticle
{
	public enum ViewDist
	{
		SHORT(8),
		NORMAL(24),
		LONG(48),
		LONGER(96),
		MAX(256);
		
		private int _dist;
		
		ViewDist(int dist)
		{
			_dist = dist;
		}
		
		public int getDist()
		{
			return _dist;
		}
	}
	
	public enum ParticleType
	{
		ANGRY_VILLAGER("angryVillager", "Lightning Cloud", Material.INK_SACK, (byte) 11),

		BLOCK_CRACK("blockcrack_1_0")
		{
			@Override
			public String getParticle(Material type, int data)
			{
				return "blockcrack_" + type.getId() + "_" + data;
			}
		},

		BLOCK_DUST("blockdust_1_0")
		{
			@Override
			public String getParticle(Material type, int data)
			{
				return "blockdust_" + type.getId() + "_" + data;
			}
		},

		BUBBLE("bubble"),

		CLOUD("cloud", "White Smoke", Material.INK_SACK, (byte) 7),

		CRIT("crit", "Brown Magic", Material.INK_SACK, (byte) 14),

		DEPTH_SUSPEND("depthSuspend"),

		DRIP_LAVA("dripLava", "Lava Drip", Material.LAVA_BUCKET, (byte) 0),

		DRIP_WATER("dripWater", "Water Drop", Material.WATER_BUCKET, (byte) 0),

		DROPLET("droplet", "Water Splash", Material.INK_SACK, (byte) 4),

		ENCHANTMENT_TABLE("enchantmenttable", "Enchantment Words", Material.BOOK, (byte) 0),

		EXPLODE("explode", "Big White Smoke", Material.INK_SACK, (byte) 15),

		FIREWORKS_SPARK("fireworksSpark", "White Sparkle", Material.GHAST_TEAR, (byte) 0),

		FLAME("flame", "Flame", Material.BLAZE_POWDER, (byte) 0),

		FOOTSTEP("footstep", "Foot Step", Material.LEATHER_BOOTS, (byte) 0),

		HAPPY_VILLAGER("happyVillager", "Emerald Sparkle", Material.EMERALD, (byte) 0),

		HEART("heart", "Love Heart", Material.APPLE, (byte) 0),

		HUGE_EXPLOSION("hugeexplosion", "Huge Explosion", Material.TNT, (byte) 0),

		ICON_CRACK("iconcrack_1_0")
		{
			@Override
			public String getParticle(Material type, int data)
			{
				return "iconcrack_" + type.getId() + "_" + data;
			}
		},

		INSTANT_SPELL("instantSpell"),

		LARGE_EXPLODE("largeexplode", "Explosion", Material.FIREBALL, (byte) 0),

		LARGE_SMOKE("largesmoke", "Black Smoke", Material.INK_SACK, (byte) 0),

		LAVA("lava", "Lava Debris", Material.LAVA, (byte) 0),

		MAGIC_CRIT("magicCrit", "Teal Magic", Material.INK_SACK, (byte) 6),

		/**
		 * Can be colored if count is 0, color is RGB and depends on the offset of xyz
		 */
		MOB_SPELL("mobSpell", "Black Swirls", Material.getMaterial(2263), (byte) 0),

		/**
		 * Can be colored if count is 0, color is RGB and depends on the offset of xyz
		 */
		MOB_SPELL_AMBIENT("mobSpellAmbient", "Transparent Black Swirls", Material.getMaterial(2266), (byte) 0),

		NOTE("note", "Musical Note", Material.JUKEBOX, (byte) 0),

		PORTAL("portal", "Portal Effect", Material.INK_SACK, (byte) 5),

		/**
		 * Can be colored if count is 0, color is RGB and depends on the offset of xyz. Offset y if 0 will default to 1, counter by making it 0.0001
		 */
		RED_DUST("reddust", "Red Smoke", Material.INK_SACK, (byte) 1),

		SLIME("slime", "Slime Particles", Material.SLIME_BALL, (byte) 0),

		SNOW_SHOVEL("snowshovel", "Snow Puffs", Material.SNOW_BALL, (byte) 0),

		SNOWBALL_POOF("snowballpoof"),

		SPELL("spell", "White Swirls", Material.getMaterial(2264), (byte) 0),

		SPLASH("splash"),

		SUSPEND("suspended"),

		TOWN_AURA("townaura", "Black Specks", Material.COAL, (byte) 0),

		WITCH_MAGIC("witchMagic", "Purple Magic", Material.INK_SACK, (byte) 13);

		public String particleName;
		private boolean _friendlyData;
		private String _friendlyName;
		private Material _material;
		private byte _data;

		ParticleType(String particleName)
		{
			this.particleName = particleName;
			_friendlyData = false;
		}

		ParticleType(String particleName, String friendlyName, Material material, byte data)
		{
			this.particleName = particleName;
			_friendlyData = true;
			_friendlyName = friendlyName;
			_material = material;
			_data = data;
		}

		public String getParticle(Material type, int data)
		{
			return particleName;
		}

		public boolean hasFriendlyData()
		{
			return _friendlyData;
		}

		public String getFriendlyName()
		{
			if (_friendlyName == null)
			{
				return toString();				
			}
			
			return _friendlyName;
		}

		public Material getMaterial()
		{
			return _material;
		}

		public byte getData()
		{
			return _data;
		}

		public static ParticleType getFromFriendlyName(String name)
		{
			for (ParticleType type : values())
			{
				if (type.hasFriendlyData() && type.getFriendlyName().equals(name))
					return type;
			}
			return null;
		}
	}

	private static PacketPlayOutWorldParticles getPacket(String particleName, Location location, float offsetX, float offsetY,
			float offsetZ, float speed, int count, boolean displayFar)
	{

		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particleName, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count, displayFar);
		return packet;
	}

	public static void PlayParticle(ParticleType type,Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count,  ViewDist dist, Player... players)
	{
		PlayParticle(type.particleName, location, offsetX, offsetY, offsetZ, speed, count, dist, players);
	}

	public static void PlayParticle(String particle, Location location, float offsetX, float offsetY, float offsetZ, 
			float speed, int count, ViewDist dist, Player... players) 
	{
		PacketPlayOutWorldParticles packet = getPacket(particle, location, offsetX, offsetY, offsetZ, speed, count, true);

		for (Player player : players)
		{
			//Out of range for player
			if (UtilMath.offset(player.getLocation(), location) > dist.getDist())
				continue;
			
			UtilPlayer.sendPacket(player, packet);
		}
	}
}