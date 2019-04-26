package nautilus.game.arcade.game.games.champions.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.minecraft.game.classcombat.Class.ClientClass;
import mineplex.minecraft.game.classcombat.Class.IPvpClass;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

public class KitBrute extends Kit 
{
	private HashMap<Player, ClientClass> _class = new HashMap<Player, ClientClass>();
	
	public KitBrute(ArcadeManager manager)
	{
		super(manager, "Brute", KitAvailability.Free, 
				new String[] 
				{
					"A giant of a man, able to smash and",
					"destroy anything in his path.",
					"",
					"Takes additional damage to counter",
					"the strength of Diamond Armor."
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
		IPvpClass pvpClass = Manager.getClassManager().GetClass("Brute");
		
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
		ent.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	}
	
	@Override
	public void DisplayDesc(Player player) 
	{

	}
}
