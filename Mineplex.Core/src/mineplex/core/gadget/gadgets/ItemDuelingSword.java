package mineplex.core.gadget.gadgets;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.ItemGadget;

public class ItemDuelingSword extends ItemGadget
{
	public ItemDuelingSword(GadgetManager manager) 
	{
		super(manager, "Dueling Sword", new String[] 
				{
				C.cWhite + "While active, you are able to fight",
				C.cWhite + "against other people who are also",
				C.cWhite + "wielding a dueling sword.",
				},
				-1,
				Material.WOOD_SWORD, (byte)3,
				1000, new Ammo("Dueling Sword", "10 Swords", Material.WOOD_SWORD, (byte)0, new String[] { C.cWhite + "10 Swords to duel with" }, 1000, 10));
	}

	@Override
	public void ActivateCustom(Player player)
	{
		ItemStack stack = new ItemStack(Material.GOLD_SWORD);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("Dueling Sword");
		stack.setItemMeta(meta);
		
		player.getInventory().setItem(Manager.getActiveItemSlot(), stack);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void damage(EntityDamageByEntityEvent event)
	{
		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
			return;
		
		Player damager = (Player)event.getDamager();
		Player damagee = (Player)event.getEntity();
		
		if (!UtilGear.isMat(damager.getItemInHand(), Material.GOLD_SWORD) || !UtilGear.isMat(damagee.getItemInHand(), Material.GOLD_SWORD))
			return;
		
		event.setCancelled(false);
		
		event.setDamage(4);
	}
}
