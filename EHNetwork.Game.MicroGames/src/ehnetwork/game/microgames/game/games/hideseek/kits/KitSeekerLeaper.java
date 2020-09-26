package ehnetwork.game.microgames.game.games.hideseek.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkLeap;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitSeekerLeaper extends KitSeeker
{
	public KitSeekerLeaper(MicroGamesManager manager)
	{
		super(manager, "Leaper Hunter", KitAvailability.Free, 
				new String[] 
				{
					"Leap after those pretty blocks!",
				}, 
				new Perk[] 
				{
					new PerkLeap("Leap", 1.1, 1, 8000),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.COMPASS));

	}
		
	@Override
	public void GiveItems(Player player)
	{
		//Sword
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		
		//Bow
		ItemStack bow = ItemStackFactory.Instance.CreateStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		player.getInventory().setItem(1, bow);
		player.getInventory().setItem(28, ItemStackFactory.Instance.CreateStack(Material.ARROW));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.IRON_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_EXPLOSION)
			return;
		
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;
		
		if (HasKit(damagee))
			event.SetCancelled("TNT Resistant");
	}
}
