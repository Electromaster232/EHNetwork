package net.minecraft.server.v1_7_R4;

import java.io.IOException;
import java.util.HashMap;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;
import net.minecraft.server.v1_7_R4.PacketPlayOutListener;

public class PacketPlayOutWorldParticles extends Packet {
	private String a;
	private float b;
	private float c;
	private float d;
	private float e;
	private float f;
	private float g;
	private float h;
	private int i;
	private boolean _displayFar;

	public PacketPlayOutWorldParticles() {
	}

	public PacketPlayOutWorldParticles(String s, float f, float f1, float f2, float f3, float f4, float f5, float f6, int i) {
		this(s, f, f1, f2, f3, f4, f5, f6, i, false);
	}

	public PacketPlayOutWorldParticles(String s, float f, float f1, float f2, float f3, float f4, float f5, float f6, int i, boolean displayFar) {
		this.a = s;
		this.b = f;
		this.c = f1;
		this.d = f2;
		this.e = f3;
		this.f = f4;
		this.g = f5;
		this.h = f6;
		this.i = i;

		_displayFar = displayFar;
	}

	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.c(64);
		this.b = packetdataserializer.readFloat();
		this.c = packetdataserializer.readFloat();
		this.d = packetdataserializer.readFloat();
		this.e = packetdataserializer.readFloat();
		this.f = packetdataserializer.readFloat();
		this.g = packetdataserializer.readFloat();
		this.h = packetdataserializer.readFloat();
		this.i = packetdataserializer.readInt();
	}

	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		String[] parts = this.a.split("_");
		PacketPlayOutWorldParticles.Particle particle = PacketPlayOutWorldParticles.Particle.find(parts[0]);
		if(particle == null) {
			particle = PacketPlayOutWorldParticles.Particle.CRIT;
		}

		if(packetdataserializer.version < 17) {
			packetdataserializer.a(this.a);
		} else {
			packetdataserializer.writeInt(particle.ordinal());
			packetdataserializer.writeBoolean(_displayFar);
		}

		packetdataserializer.writeFloat(this.b);
		packetdataserializer.writeFloat(this.c);
		packetdataserializer.writeFloat(this.d);
		packetdataserializer.writeFloat(this.e);
		packetdataserializer.writeFloat(this.f);
		packetdataserializer.writeFloat(this.g);
		packetdataserializer.writeFloat(this.h);
		packetdataserializer.writeInt(this.i);
		if(packetdataserializer.version >= 17) {
			for(int i = 0; i < particle.extra; ++i) {
				int toWrite = 0;
				if(parts.length - 1 > i) {
					try {
						toWrite = Integer.parseInt(parts[i + 1]);
						if(particle.extra == 1 && parts.length == 3) {
							++i;
							toWrite |= Integer.parseInt(parts[i + 1]) << 12;
						}
					} catch (NumberFormatException var7) {
						;
					}
				}

				packetdataserializer.b(toWrite);
			}
		}

	}

	public void a(PacketPlayOutListener packetplayoutlistener) {
		packetplayoutlistener.a(this);
	}

	public void handle(PacketListener packetlistener) {
		this.a((PacketPlayOutListener)packetlistener);
	}

	private static enum Particle {
		EXPLOSION_NORMAL("explode"),
		EXPLOSION_LARGE("largeexplode"),
		EXPLOSION_HUGE("hugeexplosion"),
		FIREWORKS_SPARK("fireworksSpark"),
		WATER_BUBBLE("bubble"),
		WATER_SPLASH("splash"),
		WATER_WAKE("wake"),
		SUSPENDED("suspended"),
		SUSPENDED_DEPTH("depthsuspend"),
		CRIT("crit"),
		CRIT_MAGIC("magicCrit"),
		SMOKE_NORMAL("smoke"),
		SMOKE_LARGE("largesmoke"),
		SPELL("spell"),
		SPELL_INSTANT("instantSpell"),
		SPELL_MOB("mobSpell"),
		SPELL_MOB_AMBIENT("mobSpellAmbient"),
		SPELL_WITCH("witchMagic"),
		DRIP_WATER("dripWater"),
		DRIP_LAVA("dripLava"),
		VILLAGER_ANGRY("angryVillager"),
		VILLAGER_HAPPY("happyVillager"),
		TOWN_AURA("townaura"),
		NOTE("note"),
		PORTAL("portal"),
		ENCHANTMENT_TABLE("enchantmenttable"),
		FLAME("flame"),
		LAVA("lava"),
		FOOTSTEP("footstep"),
		CLOUD("cloud"),
		REDSTONE("reddust"),
		SNOWBALL("snowballpoof"),
		SNOW_SHOVEL("snowshovel"),
		SLIME("slime"),
		HEART("heart"),
		BARRIER("barrier"),
		ICON_CRACK("iconcrack", 2),
		BLOCK_CRACK("blockcrack", 1),
		BLOCK_DUST("blockdust", 1),
		WATER_DROP("droplet"),
		ITEM_TAKE("take"),
		MOB_APPEARANCE("mobappearance");

		public final String name;
		public final int extra;
		private static final HashMap<String, PacketPlayOutWorldParticles.Particle> particleMap;

		private Particle(String name) {
			this(name, 0);
		}

		private Particle(String name, int extra) {
			this.name = name;
			this.extra = extra;
		}

		public static PacketPlayOutWorldParticles.Particle find(String part) {
			return (PacketPlayOutWorldParticles.Particle)particleMap.get(part);
		}

		static {
			particleMap = new HashMap();
			PacketPlayOutWorldParticles.Particle[] var0 = values();
			int var1 = var0.length;

			for(int var2 = 0; var2 < var1; ++var2) {
				PacketPlayOutWorldParticles.Particle particle = var0[var2];
				particleMap.put(particle.name, particle);
			}

		}
	}
}