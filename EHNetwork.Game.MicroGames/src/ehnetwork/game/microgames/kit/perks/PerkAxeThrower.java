package ehnetwork.game.microgames.kit.perks;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Perk;

public class PerkAxeThrower extends Perk implements IThrown
{
	public PerkAxeThrower(MicroGamesManager manager)
	{
		super("Axe Thrower",  new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axes to " + C.cGreen + "Throw Axe",
				});
	}
	
	@EventHandler
	public void Throw(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R))
			return;
		
		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;
		
		event.setCancelled(true);
		
		org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(player.getItemInHand().getType()));
		UtilAction.velocity(ent, player.getLocation().getDirection(), 1.2, false, 0, 0.2, 10, false);
		Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 0.6f);
		
		//Remove Axe
		player.setItemInHand(null);
		UtilInv.Update(player);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target == null)
			return;
		
		if (target instanceof Player)
			if (!Manager.GetGame().IsAlive((Player)target))
				return;
		
		Item item = (Item)data.GetThrown();
		
		int damage = 4;
		if (item.getItemStack().getType() == Material.STONE_AXE)		damage = 5;
		else if (item.getItemStack().getType() == Material.IRON_AXE)		damage = 6;
		else if (item.getItemStack().getType() == Material.DIAMOND_AXE)		damage = 7;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, damage, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());

		//Effect
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.ZOMBIE_WOOD, 1f, 1.6f);

		//Re-Drop
		data.GetThrown().getWorld().dropItem(data.GetThrown().getLocation(), ItemStackFactory.Instance.CreateStack(item.getItemStack().getType())).setPickupDelay(60);

		//Remove
		data.GetThrown().remove();
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		
	}
}
