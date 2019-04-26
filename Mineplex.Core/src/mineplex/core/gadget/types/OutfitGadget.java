package mineplex.core.gadget.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.itemstack.ItemStackFactory;

public abstract class OutfitGadget extends Gadget
{
	public enum ArmorSlot
	{
		Helmet,
		Chest,
		Legs,
		Boots
	}
	
	private ArmorSlot _slot;
	
	public OutfitGadget(GadgetManager manager, String name, String[] desc, int cost, ArmorSlot slot, Material mat, byte data) 
	{
		super(manager, GadgetType.Costume, name, desc, cost, mat, data);
		
		_slot = slot;
	}	
	
	public ArmorSlot GetSlot() 
	{
		return _slot;
	}
	
	public void ApplyArmor(Player player)
	{
		Manager.RemoveMorph(player);
		
		Manager.RemoveOutfit(player, _slot);
		
		_active.add(player);
		
		UtilPlayer.message(player, F.main("Gadget", "You put on " + F.elem(GetName()) + "."));
		
		if (_slot == ArmorSlot.Helmet)	player.getInventory().setHelmet(
				ItemStackFactory.Instance.CreateStack(GetDisplayMaterial().getId(), GetDisplayData(), 1, GetName()));
		
		else if (_slot == ArmorSlot.Chest)	player.getInventory().setChestplate(
				ItemStackFactory.Instance.CreateStack(GetDisplayMaterial().getId(), GetDisplayData(), 1, GetName()));
		
		else if (_slot == ArmorSlot.Legs)	player.getInventory().setLeggings(
				ItemStackFactory.Instance.CreateStack(GetDisplayMaterial().getId(), GetDisplayData(), 1, GetName()));
		
		else if (_slot == ArmorSlot.Boots)	player.getInventory().setBoots(
				ItemStackFactory.Instance.CreateStack(GetDisplayMaterial().getId(), GetDisplayData(), 1, GetName()));
	}
	
	public void RemoveArmor(Player player)
	{
		if (!_active.remove(player))
			return;
		
		UtilPlayer.message(player, F.main("Gadget", "You took off " + F.elem(GetName()) + "."));
		
		if (_slot == ArmorSlot.Helmet)		player.getInventory().setHelmet(null);
		else if (_slot == ArmorSlot.Chest)	player.getInventory().setChestplate(null);
		else if (_slot == ArmorSlot.Legs)	player.getInventory().setLeggings(null);
		else if (_slot == ArmorSlot.Boots)	player.getInventory().setBoots(null);
	}
}
