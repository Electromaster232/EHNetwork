package ehnetwork.game.arcade.game.games.christmas;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFallingSand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;

public class SleighPart 
{
	public Chicken Ent;
	public FallingBlock Block;
	public double OffsetX;
	public double OffsetZ;
	
	public SleighPart(int rise, int id, int data, Location loc, double x, double z)
	{
		//Base
		Ent = loc.getWorld().spawn(loc.add(x, 0, z), Chicken.class);
		Ent.setBaby();
		Ent.setAgeLock(true);
		UtilEnt.Vegetate(Ent, true);
		UtilEnt.ghost(Ent, true, true);
		
		//Height
		Chicken top = Ent;
		for (int i=0 ; i<rise ; i++)
		{
			Chicken newTop = loc.getWorld().spawn(loc.add(x, 0, z), Chicken.class);
			newTop.setBaby();
			newTop.setAgeLock(true);
			UtilEnt.Vegetate(newTop, true);
			UtilEnt.ghost(newTop, true, true);
			
			top.setPassenger(newTop);
			top = newTop;
		}
		
		//Block
		if (id != 0)
		{
			Block = loc.getWorld().spawnFallingBlock(loc.add(0, 1, 0), id, (byte)data);
			top.setPassenger(Block);
		}
		
		OffsetX = x;
		OffsetZ = z;
	}

	public void RefreshBlocks() 
	{
		if (Ent == null)
			return;
		
		Entity ent = Ent;
		
		while (ent.getPassenger() != null)
		{
			ent = ent.getPassenger();
			
			if (ent instanceof FallingBlock)
				((CraftFallingSand)ent).getHandle().ticksLived = 1;
		}	
	}

	public void SetPresent() 
	{
		if (Ent == null)
			return;
		
		Block = Ent.getWorld().spawnFallingBlock(Ent.getLocation().add(0, 1, 0), 35, (byte)UtilMath.r(15));
		
		Entity top = Ent;
		while (top.getPassenger() != null)
			top = top.getPassenger();
		
		top.setPassenger(Block);
	}

	public void AddSanta() 
	{
		if (Ent == null)
			return;
		
		Skeleton skel = Ent.getWorld().spawn(Ent.getLocation().add(0, 1, 0), Skeleton.class);
		UtilEnt.Vegetate(skel);
		UtilEnt.ghost(skel, true, false);
		
		ItemStack head = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta)head.getItemMeta();
		meta.setColor(Color.RED);
		head.setItemMeta(meta);
		skel.getEquipment().setHelmet(head);
		
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		meta = (LeatherArmorMeta)chest.getItemMeta();
		meta.setColor(Color.RED);
		chest.setItemMeta(meta);
		skel.getEquipment().setChestplate(chest);
		
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
		meta = (LeatherArmorMeta)legs.getItemMeta();
		meta.setColor(Color.RED);
		legs.setItemMeta(meta);
		skel.getEquipment().setLeggings(legs);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		meta = (LeatherArmorMeta)boots.getItemMeta();
		meta.setColor(Color.RED);
		boots.setItemMeta(meta);
		skel.getEquipment().setBoots(boots);
		
		skel.setCustomName(C.Bold + "Santa Claus");
		skel.setCustomNameVisible(true);
		
		Entity top = Ent;
		while (top.getPassenger() != null)
			top = top.getPassenger();
		
		top.setPassenger(skel);
	}

	public boolean HasEntity(LivingEntity ent) 
	{
		if (Ent.equals(ent))
			return true;
		
		Entity top = Ent;
		
		while (top.getPassenger() != null)
		{
			top = top.getPassenger();
			
			if (top.equals(ent))
				return true;
		}
		
		return false;
	}

	public Entity GetTop() 
	{
		Entity ent = Ent;
		
		while (ent.getPassenger() != null)
		{
			ent = ent.getPassenger();
		}
		
		return ent;
	}
}
