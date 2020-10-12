package ehnetwork.game.arcade.game.games.snowfight.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitMedic extends Kit
{

	public KitMedic(ArcadeManager manager)
	{
		super(manager, "Medic", KitAvailability.Free, 

				new String[] 
						{
				"To the rescue...!",
				" ",
				"Gets 1 Snowball every second tile.",
				"Left-Click Snow to pick up Snowballs (Max. 16)",
				"Right-Click Snowballs to throw them.",
				" ",
				"Gets 1 Healing Potion every 32 seconds [max. 1]",
				"Slowness II when hit.",
				" ",
				"Supports all nearby allies with REGENERATION."
						}, 

						new Perk[] 
								{
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.POTION));
	

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setItem(2, ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
	}

	@EventHandler
	public void Aura(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		 
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!HasKit(player))
				continue;
			
			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (other.equals(player))
					continue;
				
				if (UtilMath.offset(player, other) > 4)
					continue;
				
				if( Manager.GetGame().GetTeam(player).equals(Manager.GetGame().GetTeam(other)))
					Manager.GetCondition().Factory().Regen("Aura", other, player, 1.9, 0, false, false, false);
			}
		}
	}
	
	@EventHandler
	public void KitItems(UpdateEvent event)
	{
		if(!Manager.GetGame().IsLive())
			return;
		
		if (event.getType() == UpdateType.SLOWEST)
		{
			for (Player player : Manager.GetGame().GetPlayers(true))
			{
				if (!HasKit(player))
					continue;
				
				Potion potion = new Potion(PotionType.INSTANT_HEAL);
				potion.setSplash(true);
				player.getInventory().setItem(1, potion.toItemStack(1));
			}
		}
	}
	
	@EventHandler
	public void Splash(PotionSplashEvent event) 
	{
		if(event.getEntity().getShooter() instanceof Player)
		{
			if(!HasKit((Player) event.getEntity().getShooter()))
				return;
			
			for(Entity entity : event.getAffectedEntities()) 
			{
				if(entity instanceof Player) 
				{
					Manager.GetCondition().Factory().Slow("Heal Potion", (Player)entity, (Player)event.getEntity().getShooter(), 5.0, 1, false, false, false, false);
				}
			}
			
		}
	}
	
}
