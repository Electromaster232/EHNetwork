package nautilus.game.arcade.game.games.halloween.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilMath;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.AbbreviatedKit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.*;

public class KitThor extends AbbreviatedKit
{
	public KitThor(ArcadeManager manager)
	{
		super(manager, "Thor", KitAvailability.Free, 

				new String[] 
						{
				"Smash and kill with your Thor Hammer!",
				"",
				"Nearby allies receive " + C.cGreen + "Strength 1"
						}, 

						new Perk[] 
								{
				new PerkKnockbackAttack(2),
				new PerkFletcher(2, 2, true),
				new PerkSeismicHammer(),
				new PerkHammerThrow(),
								}, 

								EntityType.ZOMBIE, new ItemStack(Material.IRON_SWORD));

	}

	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, "Seismic Hammer"));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE, (byte)0, 1, "Thor Hammer"));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, "Bow"));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.JACK_O_LANTERN));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_BOOTS));
	}

	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
		ent.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	}
	
//	@EventHandler
//	public void DamageBoost(CustomDamageEvent event)
//	{
//		Player damagee = event.GetDamageePlayer();
//		if (damagee == null)	return;
//		
//		if (HasKit(damagee))
//			event.AddMod("Thor Boost", "Thor Boost", -2, false);
//	}
	
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
				
				if (UtilMath.offset(player, other) > 8)
					continue;
				
				Manager.GetCondition().Factory().Strength("Aura", other, player, 1.9, 0, false, false, false);
			}
		}
	}
}
