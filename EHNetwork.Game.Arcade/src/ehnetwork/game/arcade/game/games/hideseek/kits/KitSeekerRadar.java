package ehnetwork.game.arcade.game.games.hideseek.kits;

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
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkRadar;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitSeekerRadar extends KitSeeker
{
	public KitSeekerRadar(ArcadeManager manager)
	{
		super(manager, "Radar Hunter", KitAvailability.Gem, 5000,
				new String[] 
				{
					"tick......tick...tick.tick.",
				}, 
				new Perk[] 
				{
					new PerkRadar(),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.COMPASS));

	}
		
	@Override
	public void GiveItems(Player player)
	{
		//Sword
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.WOOD_SWORD));
		
		//Bow
		ItemStack bow = ItemStackFactory.Instance.CreateStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		player.getInventory().setItem(1, bow);
		player.getInventory().setItem(28, ItemStackFactory.Instance.CreateStack(Material.ARROW));
		
		//Radar
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.COMPASS, (byte)0, 1, "Radar Scanner"));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
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
