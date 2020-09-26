package ehnetwork.game.arcade.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkCleave;
import ehnetwork.game.arcade.kit.perks.PerkSeismicSlamCS;

public class KitHumanBrawler extends KitHuman
{
	public KitHumanBrawler(ArcadeManager manager)
	{
		super(manager, "Castle Brawler", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Extremely tanky, can smash the undead around."
						}, 

						new Perk[] 
								{
				new PerkSeismicSlamCS(),
				new PerkCleave(0.75, true),
								}, 

								EntityType.ZOMBIE, new ItemStack(Material.IRON_AXE));
	}
	
	@EventHandler
	public void FireItemResist(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (Manager.GetGame() == null)
			return;
			
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!HasKit(player))
				continue;
			
			Manager.GetCondition().Factory().FireItemImmunity(GetName(), player, player, 1.9, false);
		}
	}
	
	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ARROW, 16));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	}
}
