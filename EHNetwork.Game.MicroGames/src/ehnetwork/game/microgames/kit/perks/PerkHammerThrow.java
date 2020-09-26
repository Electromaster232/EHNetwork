package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkHammerThrow extends Perk implements IThrown
{	
	private HashMap<Item, Player> _thrown = new HashMap<Item, Player>();
	
	public PerkHammerThrow() 
	{
		super("Hammer Throw", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Diamond Axe to " + C.cGreen + "Hammer Throw"
				});
	}

	@EventHandler
	public void Skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("DIAMOND_AXE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		player.setItemInHand(null);
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
		
		//Throw
		Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.DIAMOND_AXE));
		UtilAction.velocity(item, player.getLocation().getDirection(), 1.2, false, 0, 0.2, 10, true);
		
		//Projectile
		Manager.GetProjectile().AddThrow(item, player, this, -1, true, true, true, false, 0.6f);
		
		//Store
		_thrown.put(item, player);
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (!_thrown.containsKey(event.getItem()))
			return;
		
		event.setCancelled(true);
		event.getItem().remove();
		
		Player player = _thrown.remove(event.getItem());
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE, (byte)0, 1, "Thor Hammer"));
	}
	
	@EventHandler
	public void Timeout(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Iterator<Item> itemIterator = _thrown.keySet().iterator();
		
		while (itemIterator.hasNext())
		{
			Item item = itemIterator.next();
			
			if (item.getTicksLived() > 200)
			{
				_thrown.get(item).getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE, (byte)0, 1, "Thor Hammer"));
				item.remove();
				itemIterator.remove();
			}
		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
		
		if (target == null)
			return;
		
		double damage = 16;
		if (target instanceof Giant)
			damage = 8;
		
		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.LIGHTNING, damage, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
	}

	public void Rebound(LivingEntity player, Entity ent)
	{
		ent.getWorld().playSound(ent.getLocation(), Sound.ZOMBIE_METAL, 0.6f, 0.5f);
		
		double mult = 0.5 + (0.6 * (UtilMath.offset(player.getLocation(), ent.getLocation())/16d));
		
		//Velocity
		ent.setVelocity(player.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().add(new Vector(0, 0.4, 0)).multiply(mult));
		
		//Ticks
		if (ent instanceof Item)
			((Item)ent).setPickupDelay(5);
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2);
	}
}
