package ehnetwork.game.arcade.game.games.halloween.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.AbbreviatedKit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBlizzardFinn;
import ehnetwork.game.arcade.kit.perks.PerkFlameSlam;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;

public class KitFinn extends AbbreviatedKit
{
	public KitFinn(ArcadeManager manager)
	{
		super(manager, "Finn the Human", KitAvailability.Free, 

				new String[] 
						{
				"Jake is hiding in his pocket.",
				"",
				"Nearby allies receive " + C.cGreen + "Speed 1"
						}, 

						new Perk[] 
								{
			
				new PerkFlameSlam(),
				new PerkBlizzardFinn(),
				new PerkFletcher(2, 2, true),
								}, 

								EntityType.ZOMBIE, new ItemStack(Material.GOLD_SWORD));

	}

	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.GOLD_AXE, (byte)0, 1, "Flaming Axe"));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_SWORD, (byte)0, 1, "Icy Sword"));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, "Bow"));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));


		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.JACK_O_LANTERN));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.IRON_BOOTS));
	}

	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
		ent.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
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
				
				if (UtilMath.offset(player, other) > 8)
					continue;
				
				Manager.GetCondition().Factory().Speed("Aura", other, player, 1.9, 0, false, false, false);
			}
		}
	}
}
