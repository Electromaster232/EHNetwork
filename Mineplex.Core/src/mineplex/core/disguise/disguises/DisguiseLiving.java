package mineplex.core.disguise.disguises;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;

public abstract class DisguiseLiving extends DisguiseBase
{
	private static Random _random = new Random();
	private boolean _invisible;
	private ItemStack[] _equipment = new ItemStack[5];

	public DisguiseLiving(org.bukkit.entity.Entity entity)
	{
		super(entity);

		DataWatcher.a(6, Float.valueOf(1.0F));
		DataWatcher.a(7, Integer.valueOf(0));
		DataWatcher.a(8, Byte.valueOf((byte) 0));
		DataWatcher.a(9, Byte.valueOf((byte) 0));
	}

	public ItemStack[] getEquipment()
	{
		return _equipment;
	}

	public void setEquipment(ItemStack[] equipment)
	{
		_equipment = equipment;
	}

	public void setHelmet(ItemStack item)
	{
		_equipment[3] = item;
	}

	public void setChestplate(ItemStack item)
	{
		_equipment[2] = item;
	}

	public void setLeggings(ItemStack item)
	{
		_equipment[1] = item;
	}

	public void setBoots(ItemStack item)
	{
		_equipment[0] = item;
	}

	public void setHeldItem(ItemStack item)
	{
		_equipment[4] = item;
	}

	public ArrayList<Packet> getEquipmentPackets()
	{
		ArrayList<Packet> packets = new ArrayList<Packet>();

		for (int nmsSlot = 0; nmsSlot < 5; nmsSlot++)
		{
			int armorSlot = nmsSlot - 1;

			if (armorSlot < 0)
				armorSlot = 4;

			ItemStack itemstack = _equipment[armorSlot];

			if (itemstack != null && itemstack.getType() != Material.AIR)
			{
				ItemStack item = null;

				if (Entity instanceof EntityLiving)
				{
					item = CraftItemStack.asBukkitCopy(((EntityLiving) Entity).getEquipment()[nmsSlot]);
				}

				if (item == null || item.getType() == Material.AIR)
				{
					PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();

					packet.a = GetEntityId();
					packet.b = nmsSlot;
					packet.c = CraftItemStack.asNMSCopy(itemstack);

					packets.add(packet);
				}
			}
		}

		return packets;
	}

	public void UpdateDataWatcher()
	{
		super.UpdateDataWatcher();
		byte b0 = DataWatcher.getByte(0);

		if (_invisible)
			DataWatcher.watch(0, Byte.valueOf((byte) (b0 | 1 << 5)));
		else
			DataWatcher.watch(0, Byte.valueOf((byte) (b0 & ~(1 << 5))));

		if (Entity instanceof EntityLiving)
		{
			DataWatcher.watch(6, Entity.getDataWatcher().getFloat(6));
			DataWatcher.watch(7, Entity.getDataWatcher().getInt(7));
			DataWatcher.watch(8, Entity.getDataWatcher().getByte(8));
			DataWatcher.watch(9, Entity.getDataWatcher().getByte(9));
		}
	}

	public boolean isInvisible()
	{
		return _invisible;
	}

	public void setInvisible(boolean invisible)
	{
		_invisible = invisible;
	}

	protected String getHurtSound()
	{
		return "damage.hit";
	}

	protected float getVolume()
	{
		return 1.0F;
	}

	protected float getPitch()
	{
		return (_random.nextFloat() - _random.nextFloat()) * 0.2F + 1.0F;
	}

	public void setHealth(float health)
	{
		DataWatcher.watch(6, Float.valueOf(health));
	}

	public float getHealth()
	{
		return DataWatcher.getFloat(6);
	}
}
