package ehnetwork.game.microgames.game.games.snowfight.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkFallDamage;

public class KitSportsman extends Kit
{
	
	public KitSportsman(MicroGamesManager manager)
	{
		super(manager, "Sportsman", KitAvailability.Free, 

				new String[] 
						{
				"Trained to be the fastest on snow and ice.",
				"",
				"Gets 1 Snowball every tile",
				"Left-Click Snow to pick up Snowballs (Max. 16)",
				"Right-Click Snowballs to throw them.",
				"",
				"Supports all nearby allies with SPEED."
						}, 

						new Perk[] 
								{
							new PerkFallDamage(3)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.SNOW_BALL));

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
					Manager.GetCondition().Factory().Speed("Aura", other, player, 1.9, 0, false, false, false);
			}
		}
	}
	
}
