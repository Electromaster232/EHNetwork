package ehnetwork.game.arcade.game.games.champions.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.classcombat.Class.ClientClass;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;

public class KitMage extends Kit 
{
	private HashMap<Player, ClientClass> _class = new HashMap<Player, ClientClass>();
	
	public KitMage(ArcadeManager manager)
	{
		super(manager, "Mage", KitAvailability.Free, 
				new String[] 
				{
					"Trained in the ways of magic, the mage",
					"can unleash hell upon his opponents.",
				}, 
				new Perk[] 
				{
					
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.IRON_SWORD));

	}
	
	@Override
	public void Deselected(Player player)
	{
		_class.remove(player);
	}
	
	@Override
	public void Selected(Player player)
	{
		Manager.Clear(player);
		
		_class.put(player, Manager.getClassManager().Get(player));
		ClientClass clientClass = _class.get(player);
		IPvpClass pvpClass = Manager.getClassManager().GetClass("Mage");
		
		clientClass.SetGameClass(pvpClass);
		pvpClass.ApplyArmor(player);
		clientClass.ClearDefaults();
		clientClass.EquipCustomBuild(clientClass.GetCustomBuilds(pvpClass).get(0));
		
		if (!Manager.GetGame().InProgress())
			clientClass.SetActiveCustomBuild(pvpClass, pvpClass.getDefaultBuild());
		
		Manager.openClassShop(player);
	}
	
	@Override
	public void GiveItems(Player player)
	{
		_class.get(player).ResetToDefaults(true, true);
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
	}
	
	@Override
	public void DisplayDesc(Player player) 
	{

	}
}
