package ehnetwork.game.microgames.game.games.snowfight.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitTactician extends Kit
{

	public KitTactician(MicroGamesManager manager)
	{
		super(manager, "Tactician", KitAvailability.Free, 
					new String[] 
						{
						"No Snowfight is complete without a tactical game!",
						" ",
						"Gets 1 Snowball every second tile.",
						"Left-Click Snow to pick up Snowballs (Max. 16)",
						"Right-Click Snowballs to throw them.",
						" ",
						"Gets 1 Barrier every 32 seconds [max. 2]",
						"Place Barriers to improve your defense.",
						"You cant place Barriers above Ice, Packed Ice or Fences.",
						" ",
						"Supports all nearby allies with RESISTANCE."
								}, new Perk[] 
								{
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.FENCE));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setItem(2, ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().setItem(7, ItemStackFactory.Instance.CreateStack(Material.COMPASS.getId(), (byte) 0, 1, "§a§lTracking Compass"));
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
					Manager.GetCondition().Factory().Protection("Aura", other, player, 1.9, 0, false, false, false);
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
				
				int amount = 0;
				if (player.getInventory().getItem(1) != null && UtilInv.contains(player, Material.FENCE, (byte) 0, 1))
					amount = 2;
				else 
					amount = 1;
				player.getInventory().setItem(1, ItemStackFactory.Instance.CreateStack(Material.FENCE, (byte) 0, amount,  "Barrier"));
			}
		}
	}

}
