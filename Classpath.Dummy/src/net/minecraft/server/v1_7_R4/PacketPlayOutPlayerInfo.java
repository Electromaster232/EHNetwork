//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraft.server.v1_7_R4;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import org.bukkit.craftbukkit.v1_7_R4.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftChatMessage;
import org.bukkit.scoreboard.Team;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.com.mojang.authlib.properties.PropertyMap;

public class PacketPlayOutPlayerInfo extends Packet {

	public static final String NOTCH_SKIN = "eyJ0aW1lc3RhbXAiOjE0Mjc4MjgyMzMwNDUsInByb2ZpbGVJZCI6IjA2OWE3OWY0NDRlOTQ3MjZhNWJlZmNhOTBlMzhhYWY1IiwicHJvZmlsZU5hbWUiOiJOb3RjaCIsImlzUHVibGljIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTExNmU2OWE4NDVlMjI3ZjdjYTFmZGRlOGMzNTdjOGM4MjFlYmQ0YmE2MTkzODJlYTRhMWY4N2Q0YWU5NCJ9LCJDQVBFIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y2ODhlMGU2OTliM2Q5ZmU0NDhiNWJiNTBhM2EyODhmOWM1ODk3NjJiM2RhZTgzMDg4NDIxMjJkY2I4MSJ9fX0=";
	public static final String NOTCH_SIGNATURE = "WrBmNqkpkjh6VJY26jOOMNS6oSOPi0MTm9WWc0t4EMUHchUbTd6/1sT2O2zz2s7xwmCeUxuIhvRREa+1bIPzIIbXJLjhxiBWMdTJbQhv6zBb1U2SZb7eb5cYrFTD6rvxy0rOarScxCBdeOXpr1coxrvN8a6VkgLhc/dGhFx0ZmORxELBLFiCNi+4WE//MI+KioAq84Gdf0ltT9ZLWdlHNFV2ynBgcx2MfNTA2lrpdKEUVOYD7xhPoOdHa5d1hzdDxbYPGDgM0FzYjzNUlBx8SLvHEpyBB7XyOsIGnfqrS0ltIDTq82wgLrEwDRncuQN18w6IiQbNK06MZBDyNnIp79mmUYvRj+Zl0dPBrZok2q2uQ08hZ87ufU3jhjY39kr+iEaPYMvfWaBxt3ALjopsZRCGSlEukMzITjeYxhfVKuQ0fhWKRfwWn/Jv2de2h+i+t7nulvN3MV3rJVrS6OXsx87p/vm9biU7Hs07T8VSGONfkxXmsgYEtY6m2egU5pmqFnsKM0MwwnZJ7Sxz2EjiPikoGzJzpv4ncj3rhelIKJKjDk9jSAz7nPzc8/UdOiTrfy4ezr3jFVAVatiKr+kS/HNXHWiCFdufhpG4DVCrSkwkFBJw030pJ6ICVhpuYq5yOswQB5QOp0JDWc2Rdth7SVmvxthSCL9G2ksfm+v7sKw=";


	public static final int ADD_PLAYER = 0;
	public static final int UPDATE_GAMEMODE = 1;
	public static final int UPDATE_LATENCY = 2;
	public static final int UPDATE_DISPLAY_NAME = 3;
	public static final int REMOVE_PLAYER = 4;
	public int action;
	public GameProfile player;
	public int gamemode;
	public int ping;
	public String username;
	public String _tabName;

	public PacketPlayOutPlayerInfo() {
	}

	public static PacketPlayOutPlayerInfo addPlayer(EntityPlayer player) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		packet.action = 0;
		packet.username = player.listName;
		packet.player = player.getProfile();
		packet.ping = player.ping;
		packet.gamemode = player.playerInteractManager.getGameMode().getId();

		packet._tabName = getFormattedName(player);
		return packet;
	}

	public static PacketPlayOutPlayerInfo updatePing(EntityPlayer player) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		packet.action = 2;
		packet.username = player.listName;
		packet.player = player.getProfile();
		packet.ping = player.ping;
		return packet;
	}

	public static PacketPlayOutPlayerInfo updateGamemode(EntityPlayer player) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		packet.action = 1;
		packet.username = player.listName;
		packet.player = player.getProfile();
		packet.gamemode = player.playerInteractManager.getGameMode().getId();
		return packet;
	}

	public static PacketPlayOutPlayerInfo updateDisplayName(EntityPlayer player) {
		return updateDisplayName(player, getFormattedName(player));
	}

	public static PacketPlayOutPlayerInfo updateDisplayName(EntityPlayer player, String displayName)
	{
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		packet.action = 3;
		packet.username = player.listName;
		packet.player = player.getProfile();
		packet._tabName = displayName;
		return packet;
	}

	public static PacketPlayOutPlayerInfo removePlayer(EntityPlayer player) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		packet.action = 4;
		packet.username = player.listName;
		packet.player = player.getProfile();
		return packet;
	}

	public void a(PacketDataSerializer packetdataserializer) throws IOException {
	}

	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		if(packetdataserializer.version >= 20) {

			Calendar c = Calendar.getInstance();

			packetdataserializer.b(this.action);
			packetdataserializer.b(1);
			packetdataserializer.writeUUID(this.player.getId());
			switch(this.action) {
				case 0:
					packetdataserializer.a(this.player.getName());
					PropertyMap properties = this.player.getProperties();

					// April Fools
					if (isAprilFools() && properties.size() == 0)
					{
						// add texture if no textures exist
						properties.put("textures", new Property("textures", NOTCH_SKIN, NOTCH_SIGNATURE));

					}

					packetdataserializer.b(properties.size());
					Iterator var3 = properties.values().iterator();

					while(var3.hasNext()) {
						Property property = (Property)var3.next();

						// April Fools
						if (isAprilFools() && property.getName().equalsIgnoreCase("textures"))
						{
							System.out.println("Applying notch texture for : " + property.getName());
							System.out.println("Skin: " + property.getValue());
							System.out.println("Signature: " + property.getSignature());
							packetdataserializer.a(property.getName());
							packetdataserializer.a(NOTCH_SKIN);
							packetdataserializer.writeBoolean(true);
							packetdataserializer.a(NOTCH_SIGNATURE);
						}
						else
						{
							packetdataserializer.a(property.getName());
							packetdataserializer.a(property.getValue());

							packetdataserializer.writeBoolean(property.hasSignature());
							if(property.hasSignature()) {
								packetdataserializer.a(property.getSignature());
							}
						}
					}

					packetdataserializer.b(this.gamemode);
					packetdataserializer.b(this.ping);
					packetdataserializer.writeBoolean(this.username != null);
					if(this.username != null) {
//						packetdataserializer.a(ChatSerializer.a(CraftChatMessage.fromString(this.username)[0]));
						packetdataserializer.a(ChatSerializer.a(CraftChatMessage.fromString(this._tabName)[0]));
					}
					break;
				case 1:
					packetdataserializer.b(this.gamemode);
					break;
				case 2:
					packetdataserializer.b(this.ping);
					break;
				case 3:
					packetdataserializer.writeBoolean(this.username != null);
					if(this.username != null) {
//						packetdataserializer.a(ChatSerializer.a(CraftChatMessage.fromString(this.username)[0]));
						packetdataserializer.a(ChatSerializer.a(CraftChatMessage.fromString(this._tabName)[0]));
					}
				case 4:
			}
		} else {
			packetdataserializer.a(this.username);
			packetdataserializer.writeBoolean(this.action != 4);
			packetdataserializer.writeShort(this.ping);
		}

	}

	public void a(PacketPlayOutListener packetplayoutlistener) {
		packetplayoutlistener.a(this);
	}

	public void handle(PacketListener packetlistener) {
		this.a((PacketPlayOutListener)((PacketPlayOutListener)packetlistener));
	}

	private static String getFormattedName(EntityPlayer player)
	{
		String name = player.getName();

		if (isAprilFools()) name = "Notch";

		CraftScoreboard scoreboard = player.getBukkitEntity().getScoreboard();
		if (scoreboard != null)
		{
			Team team = scoreboard.getPlayerTeam(player.getBukkitEntity());
			if (team != null)
				name = team.getPrefix() + name + team.getSuffix();
		}

		return name;
	}

	public static boolean isAprilFools()
	{
		Calendar c = Calendar.getInstance();

//		return true;
		return c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1;
	}
}
