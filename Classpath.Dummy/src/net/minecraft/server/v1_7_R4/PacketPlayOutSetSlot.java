package net.minecraft.server.v1_7_R4;

import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;
import net.minecraft.server.v1_7_R4.PacketPlayOutListener;

public class PacketPlayOutSetSlot extends Packet {
	public int a;
	public int b;
	public ItemStack c;

	public PacketPlayOutSetSlot() {
	}

	public PacketPlayOutSetSlot(int i, int j, ItemStack itemstack) {
		this.a = i;
		this.b = j;
		this.c = itemstack == null?null:itemstack.cloneItemStack();
	}

	public void a(PacketPlayOutListener packetplayoutlistener) {
		packetplayoutlistener.a(this);
	}

	public void a(PacketDataSerializer packetdataserializer) {
		this.a = packetdataserializer.readByte();
		this.b = packetdataserializer.readShort();
		this.c = packetdataserializer.c();
	}

	public void b(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.a);
		packetdataserializer.writeShort(this.b);
		packetdataserializer.a(this.c);
	}

	public void handle(PacketListener packetlistener) {
		this.a((PacketPlayOutListener)packetlistener);
	}
}
