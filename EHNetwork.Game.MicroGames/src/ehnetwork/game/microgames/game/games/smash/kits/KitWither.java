package ehnetwork.game.microgames.game.games.smash.kits;

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

import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkSkullShot;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;

public class KitWither extends SmashKit
{
	public KitWither(MicroGamesManager manager)
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
