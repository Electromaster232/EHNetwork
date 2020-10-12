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
import ehnetwork.game.microgames.kit.perks.PerkBomber;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitSeekerTNT extends KitSeeker
{
	public KitSeekerTNT(MicroGamesManager manager)
	{
		super(manager, "TNT Hunter", KitAvailability.Gem, 

				new String[] 
						{ 
				"Throw TNT to flush out the Hiders!"
						}, 

						new Perk[] 
								{
					new PerkBomber(15, 2, -1)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.TNT));
	}

	@Override
	public void GiveItems(Player player) 
	{
		//Sword
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
		
		//Bow
		ItemStack bow = ItemStackFactory.Instance.CreateStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		player.getInventory().setItem(1, bow);
		player.getInventory().setItem(28, ItemStackFactory.Instance.CreateStack(Material.ARROW));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.GOLD_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.GOLD_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.GOLD_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.GOLD_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
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
