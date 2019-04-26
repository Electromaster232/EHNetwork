package nautilus.game.arcade.game.games.smash.kits;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilServer;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkSmashStats;
import nautilus.game.arcade.kit.perks.PerkSkullShot;

public class KitWither extends SmashKit
{
	public KitWither(ArcadeManager manager)
	{
		super(manager, "Wither", KitAvailability.Gem, 5000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.2, 0.3, 6),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkSkullShot(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.BOW),
								"", 0, null);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(new ItemStack(Material.ARROW));
		
		ItemStack head = ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM, (byte)1, 1);
		player.getInventory().setHelmet(head);
		
		ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
		meta.setColor(Color.BLACK);
		armor.setItemMeta(meta);
		player.getInventory().setChestplate(armor);
		
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta metaLegs = (LeatherArmorMeta)armor.getItemMeta();
		metaLegs.setColor(Color.BLACK);
		armor.setItemMeta(metaLegs);
		player.getInventory().setLeggings(legs);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta metaBoots = (LeatherArmorMeta)armor.getItemMeta();
		metaBoots.setColor(Color.BLACK);
		boots.setItemMeta(metaBoots);
		player.getInventory().setBoots(boots);
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
	
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);
	}
	
	@EventHandler 
	public void InvisibilityUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		for (Player player : UtilServer.getPlayers())
		{
			if (!HasKit(player))
				continue;
			
			if (player.getInventory().getChestplate() == null)
				continue;
			
			Manager.GetCondition().Factory().Invisible("Wither Invis", player, player, 1.9, 0, false, false, false);
		}
	}
	
	@Override
	public Entity SpawnEntity(Location loc)
	{
		EntityType type = _entityType;
		if (type == EntityType.PLAYER)
			type = EntityType.ZOMBIE;

		LivingEntity entity = (LivingEntity) Manager.GetCreature().SpawnEntity(loc, type);

		entity.setRemoveWhenFarAway(false);
		entity.setCustomName(GetAvailability().GetColor() + GetName() + " Kit");
		entity.setCustomNameVisible(true);
		entity.getEquipment().setItemInHand(_itemInHand);
		
		Manager.GetCondition().Factory().Invisible("Kit Invis", entity, entity, 7777, 0, false, false, false);

		UtilEnt.Vegetate(entity);

		SpawnCustom(entity); 

		return entity;
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ItemStack head = ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM, (byte)1, 1);
		ent.getEquipment().setHelmet(head);
		
		ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
		meta.setColor(Color.BLACK);
		armor.setItemMeta(meta);
		ent.getEquipment().setChestplate(armor);
	}
}
