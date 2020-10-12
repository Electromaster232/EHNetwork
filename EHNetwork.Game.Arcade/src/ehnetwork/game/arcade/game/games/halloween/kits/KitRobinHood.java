package ehnetwork.game.arcade.game.games.halloween.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.AbbreviatedKit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBarrage;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;
import ehnetwork.game.arcade.kit.perks.PerkQuickshotRobinHood;

public class KitRobinHood extends AbbreviatedKit
{
	public KitRobinHood(ArcadeManager manager)
	{
		super(manager, "Robin Hood", KitAvailability.Free, 

				new String[] 
						{
				"Trick or treating from the rich...",
				"",
				"Nearby allies receive " + C.cGreen + "Regeneration 1"
						}, 

						new Perk[] 
								{	 
				new PerkFletcher(1, 8, true),
				new PerkBarrage(8, 125, true, true),
				new PerkQuickshotRobinHood()
								}, 

								EntityType.ZOMBIE, new ItemStack(Material.BOW));

	}

	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, "Sword"));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, "Bow"));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));


		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.JACK_O_LANTERN));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}

	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
		ent.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
	}
	
	@EventHandler
	public void Aura(UpdateEvent event)
	{
		if (event.getType() == UpdateType.FAST)
		{
			for (Player player : Manager.GetGame().GetPlayers(true))
			{
				if (!HasKit(player))
					continue;
				
				for (Player other : Manager.GetGame().GetPlayers(true))
				{
					if (other.equals(player))
						continue;
					
					if (UtilMath.offset(player, other) > 8)
						continue;
					
					Manager.GetCondition().Factory().Regen("Aura", other, player, 1.9, 0, false, false, false);
				}
			}
		}
		
		if (event.getType() == UpdateType.SLOW)
		{
			for (Player player : Manager.GetGame().GetPlayers(true))
			{
				if (!HasKit(player))
					continue;
				
				for (Player other : Manager.GetGame().GetPlayers(true))
				{
					if (other.equals(player))
						continue;
					
					if (UtilMath.offset(player, other) > 8)
						continue;
					
					UtilPlayer.health(other, 1);
				}
			}
		}
	}
}
