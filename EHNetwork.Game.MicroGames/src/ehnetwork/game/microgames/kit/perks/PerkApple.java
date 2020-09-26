package ehnetwork.game.microgames.kit.perks;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import net.minecraft.server.v1_7_R4.Item;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Perk;

public class PerkApple extends Perk implements IThrown
{

	public PerkApple(MicroGamesManager manager)
	{
		super("Apple Thrower",  new String[] 
				{
				C.cGray + "Receive 1 Apple every 10 seconds",
				C.cYellow + "Left-Click" + C.cGray + " with Apple to " + C.cGreen + "Throw Apple",
				});
	}
	
	@EventHandler
	public void AppleSpawn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (Manager.GetGame() == null)
			return;
			
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			if (!Manager.GetGame().IsAlive(player))
				continue;
			
			if (!Recharge.Instance.use(player, "Apple Spawn", 10000, false, false))
				continue;
			
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(260));
			player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
		}
	}
	
	@EventHandler
	public void ThrowApple(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (event.getPlayer().getItemInHand().getType() != Material.APPLE)
			return;
		
		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;
		
		event.setCancelled(true);
		
		UtilInv.remove(player, Material.APPLE, (byte)0, 1);
		UtilInv.Update(player);
		
		org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(Material.APPLE));
		UtilAction.velocity(ent, player.getLocation().getDirection(), 1.2, false, 0, 0.2, 10, false);
		Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 0.5f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target == null)
			return;
		
		if (target instanceof Player)
		{
			if (!Manager.GetGame().IsAlive((Player)target))
			{
				return;
			}
		}

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, 3, true, false, false,
				UtilEnt.getName(data.GetThrower()), GetName());

		//Effect
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1.6f);

		//Re-Drop
		if (data.GetThrown() instanceof Item)
			data.GetThrown().getWorld().dropItem(data.GetThrown().getLocation(), ItemStackFactory.Instance.CreateStack(Material.APPLE)).setPickupDelay(60);

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
