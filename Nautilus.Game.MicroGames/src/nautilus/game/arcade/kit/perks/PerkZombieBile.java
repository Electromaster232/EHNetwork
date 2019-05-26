package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PerkZombieBile extends SmashPerk implements IThrown
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	
	public PerkZombieBile() 
	{
		super("Spew Bile", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to use " + C.cGreen + "Spew Bile"
				});
	}
	
	@EventHandler
	public void activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;
		
		Player player = event.getPlayer();
				
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return;
		
		_active.put(player, System.currentTimeMillis());

		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void deactivateDeath(PlayerDeathEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;
		
		if (!Kit.HasKit(event.getEntity()))
			return;
		
		if (_active.containsKey(event.getEntity()))
		{
			_active.remove(event.getEntity());
		}
	}
	
	@EventHandler
	public void update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> activeIter = _active.keySet().iterator();
		
		while (activeIter.hasNext())
		{
			Player player = activeIter.next();
			
			//Expire
			if (UtilTime.elapsed(_active.get(player), 2000))
			{
				activeIter.remove();
				continue;				
			}
			
			//Sound
			if (Math.random() > 0.85)
				player.getWorld().playSound(player.getLocation(), Sound.BURP, 1f, (float)(Math.random() + 0.5));
			
			//Projectiles
			for (int i=0 ; i<3 ; i++)
			{
				Vector rand = new Vector((Math.random()-0.5)*0.6,(Math.random()-0.5)*0.6,(Math.random()-0.5)*0.6);
				
				org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()).subtract(0, 0.5, 0), ItemStackFactory.Instance.CreateStack(Material.ROTTEN_FLESH));
				UtilAction.velocity(ent, player.getLocation().getDirection().add(rand), 0.8, false, 0, 0.2, 10, false);
				Manager.GetProjectile().AddThrow(ent, player, this, System.currentTimeMillis() + 2000, true, true, true, false, 0.5f);
			}
		}
	}
	
	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		data.GetThrown().remove();
		
		if (target == null)
			return;
		
		if (target instanceof Player)
			if (!Manager.GetGame().IsAlive((Player)target))
				return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, 3, true, false, false,
				UtilEnt.getName(data.GetThrower()), GetName());

		data.GetThrown().remove();
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		data.GetThrown().remove();
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		data.GetThrown().remove();
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 1);
	}
}
