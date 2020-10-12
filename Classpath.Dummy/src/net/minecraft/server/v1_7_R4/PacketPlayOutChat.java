package net.minecraft.server.v1_7_R4;

import java.io.IOException;

public class PacketPlayOutChat
		extends Packet
{
	private IChatBaseComponent a;
	private boolean b;
	private byte _chatType = 0;

	public PacketPlayOutChat()
	{
		this.b = true;
	}

	public PacketPlayOutChat(IChatBaseComponent ichatbasecomponent)
	{
		this(ichatbasecomponent, true);
	}

	public PacketPlayOutChat(IChatBaseComponent ichatbasecomponent, boolean flag)
	{
		this.b = true;
		this.a = ichatbasecomponent;
		this.b = flag;
	}

	public PacketPlayOutChat(String text)
	{
		this(ChatSerializer.a(text));
	}

	public void a(PacketDataSerializer packetdataserializer)
			throws IOException
	{
		this.a = ChatSerializer.a(packetdataserializer.c(32767));
	}

	public void b(PacketDataSerializer packetdataserializer)
			throws IOException
	{
		packetdataserializer.a(ChatSerializer.a(this.a));
		if (packetdataserializer.version >= 16) {
			packetdataserializer.writeByte(_chatType);
		}
	}

	public void setChatType(byte chatType)
	{
		_chatType = chatType;
	}

	public void a(PacketPlayOutListener packetplayoutlistener)
	{
		packetplayoutlistener.a(this);
	}

	public String b()
	{
		return String.format("message='%s'", new Object[] { this.a });
	}

	public boolean d()
	{
		return this.b;
	}

	public void handle(PacketListener packetlistener)
	{
		a((PacketPlayOutListener)packetlistener);
	}
}
