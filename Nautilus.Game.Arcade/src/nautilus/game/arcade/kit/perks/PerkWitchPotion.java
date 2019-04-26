package nautilus.game.arcade.kit.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkWitchPotion extends SmashPerk
{
	private ArrayList<Projectile> _proj = new ArrayList<Projectile>();
	
	public PerkWitchPotion() 
	{
		super("Daze Potion", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Daze Potion"
				});
	}
	
	
	@EventHandler
	public void Activate(PlayerInteractEvent event)
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
		
		if (isSuperActive(player))
			return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 2000, true, true))
			return;
		
		//Start
		ThrownPotion potion = player.launchProjectile(ThrownPotion.class);
		UtilAction.velocity(potion, player.getLocation().getDirection(), 1, false, 0, 0.2, 10, false);
		
		_proj.add(potion);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void Hit(ProjectileHitEvent event)
	{
		if (!_proj.remove(event.getEntity()))
			return;
		
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (player.equals(event.getEntity().getShooter()))
				continue;
			
			if (!(event.getEntity().getShooter() instanceof Player))
				continue;
			
			Player thrower = (Player)event.getEntity().getShooter();
			
			double range = 3;
			if (isSuperActive(thrower))
				range = 4;
			
			if (UtilMath.offset(player.getLocation().add(0,1,0), event.getEntity().getLocation()) > range)
				continue;
						
			//Standard
			if (!isSuperActive(thrower))
			{
				//Damage Event
				Manager.GetDamage().NewDamageEvent(player, thrower, null, 
					DamageCause.CUSTOM, 5, true, true, false,
					UtilEnt.getName((LivingEntity)event.getEntity().getShooter()), GetName());	
			}
			//Super Effect
			else
			{
				//Bonus Damage
				double bonus = 5;
				
				//Damage Event
				Manager.GetDamage().NewDamageEvent(player, thrower, null, 
					DamageCause.CUSTOM, 5 + bonus, true, true, false,
					UtilEnt.getName((LivingEntity)event.getEntity().getShooter()), GetName());	
				
				//Manager.GetCondition().Factory().Confuse(reason, ent, source, duration, mult, extend, showIndicator, ambient)
			}
		}
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Projectile> potionIterator = _proj.iterator();
		
		while (potionIterator.hasNext())
		{
			Projectile proj = potionIterator.next();
			
			if (!proj.isValid())
			{
				potionIterator.remove();
				continue;
			}
			
			UtilParticle.PlayParticle(ParticleType.MOB_SPELL, proj.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.LONGER, UtilServer.getPlayers());
			
			//Super
			if (!(proj.getShooter() instanceof Player))
				continue;
			
			Player thrower = (Player)proj.getShooter();
			
			//Super Effect
			if (!isSuperActive(thrower))
			{
				//XXX
			}
		}
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2);
	}
}
