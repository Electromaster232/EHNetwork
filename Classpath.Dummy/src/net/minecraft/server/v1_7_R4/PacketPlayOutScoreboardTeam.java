package net.minecraft.server.v1_7_R4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PacketPlayOutScoreboardTeam
		extends Packet
{
	private String a = "";
	private String b = "";
	private String c = "";
	private String d = "";
	private String _nameTagVisibility;
	private Collection e = new ArrayList();
	private int f;
	private int g;

	public PacketPlayOutScoreboardTeam() {}

	public PacketPlayOutScoreboardTeam(ScoreboardTeam scoreboardteam, int i)
	{
		this.a = scoreboardteam.getName();
		this.f = i;
		if ((i == 0) || (i == 2))
		{
			this.b = scoreboardteam.getDisplayName();
			this.c = scoreboardteam.getPrefix();
			this.d = scoreboardteam.getSuffix();
			this.g = scoreboardteam.packOptionData();
			this._nameTagVisibility = scoreboardteam.getNametagVisibility();
		}
		if (i == 0) {
			this.e.addAll(scoreboardteam.getPlayerNameSet());
		}
	}

	public PacketPlayOutScoreboardTeam(ScoreboardTeam scoreboardteam, Collection collection, int i)
	{
		if ((i != 3) && (i != 4)) {
			throw new IllegalArgumentException("Method must be join or leave for player constructor");
		}
		if ((collection != null) && (!collection.isEmpty()))
		{
			this.f = i;
			this.a = scoreboardteam.getName();
			this.e.addAll(collection);
			this._nameTagVisibility = _nameTagVisibility;
		}
		else
		{
			throw new IllegalArgumentException("Players cannot be null/empty");
		}
	}

	public void a(PacketDataSerializer packetdataserializer)
			throws IOException
	{
		this.a = packetdataserializer.c(16);
		this.f = packetdataserializer.readByte();
		if ((this.f == 0) || (this.f == 2))
		{
			this.b = packetdataserializer.c(32);
			this.c = packetdataserializer.c(16);
			this.d = packetdataserializer.c(16);
			this.g = packetdataserializer.readByte();
		}
		if ((this.f == 0) || (this.f == 3) || (this.f == 4))
		{
			short short1 = packetdataserializer.readShort();
			for (int i = 0; i < short1; i++) {
				this.e.add(packetdataserializer.c(40));
			}
		}
	}

	public void b(PacketDataSerializer packetdataserializer)
			throws IOException
	{
		packetdataserializer.a(this.a);
		packetdataserializer.writeByte(this.f);
		if ((this.f == 0) || (this.f == 2))
		{
			packetdataserializer.a(this.b);
			packetdataserializer.a(this.c);
			packetdataserializer.a(this.d);
			packetdataserializer.writeByte(this.g);
			if (packetdataserializer.version >= 16)
			{
				packetdataserializer.a(_nameTagVisibility);
				packetdataserializer.writeByte(EnumChatFormat.WHITE.ordinal());
			}
		}
		if ((this.f == 0) || (this.f == 3) || (this.f == 4))
		{
			if (packetdataserializer.version < 16) {
				packetdataserializer.writeShort(this.e.size());
			} else {
				packetdataserializer.b(this.e.size());
			}
			Iterator iterator = this.e.iterator();
			while (iterator.hasNext())
			{
				String s = (String)iterator.next();

				packetdataserializer.a(s);
			}
		}
	}

	public void a(PacketPlayOutListener packetplayoutlistener)
	{
		packetplayoutlistener.a(this);
	}

	public void handle(PacketListener packetlistener)
	{
		a((PacketPlayOutListener)packetlistener);
	}
}
