package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkWitherSkull extends SmashPerk
{
	private HashMap<WitherSkull, Vector> _active = new HashMap<WitherSkull, Vector>();
	private HashSet<Player> _ignoreControl = new HashSet<Player>();
	
	public PerkWitherSkull() 
	{
		super("Wither Skull", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to use " + C.cGreen + "Wither Skull"
				});
	}
	
	
	@EventHandler(priority = EventPriority.LOW) // Happen BEFORE super is triggered
	public void activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		
		if (!isSuperActive(player))
			if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
				return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
	
		if (!isSuperActive(player))
			if (!event.getPlayer().getItemInHand().getType().toString().contains("_SWORD"))
				return;

		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), isSuperActive(player) ? 1000 : 6000, !isSuperActive(player), !isSuperActive(player)))
			return;
		
		//Fire
		WitherSkull skull = player.launchProjectile(WitherSkull.class);
		
		if (!isSuperActive(player))
			skull.setDirection(player.getLocation().getDirection());
	
		_active.put(skull, player.getLocation().getDirection().multiply(0.6));
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 1f, 1f);

		//Inform
		if (!isSuperActive(player))
			UtilPlayer.message(player, F.main("Skill", "You launched " + F.skill(GetName()) + "."));
		
		//Control
		if (!isSuperActive(player))
			_ignoreControl.remove(player);
		else
			_ignoreControl.add(player);
	}
	
	@EventHandler
	public void cleanAndControl(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<WitherSkull> skullIterator = _active.keySet().iterator();
		
		while (skullIterator.hasNext())
		{
			WitherSkull skull = skullIterator.next();
			Player player = (Player)skull.getShooter();
			
			if (!skull.isValid())
			{
				skullIterator.remove();
				skull.remove();
				continue;
			}
			
			if (_ignoreControl.contains(player))
				continue;
			
			if (player.isBlocking() && !_ignoreControl.contains(player))
			{
				skull.setDirection(player.getLocation().getDirection());
				skull.setVelocity(player.getLocation().getDirection().multiply(0.6));
				_active.put(skull, player.getLocation().getDirection().multiply(0.6));
			}
			else
			{
				_ignoreControl.add(player);
				skull.setDirection(_active.get(skull));
				skull.setVelocity(_active.get(skull));
			}
		}
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent event)
	{
		if (!_active.containsKey(event.getEntity()))
			return;
		
		event.setCancelled(true);
		
		WitherSkull skull = (WitherSkull)event.getEntity();
		
		UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, skull.getLocation(), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		explode(skull, event.getLocation(), (LivingEntity)skull.getShooter());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void explodeDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetProjectile() != null && event.GetProjectile() instanceof WitherSkull)
			event.SetCancelled("Wither Skull Cancel");
	}
	
	//Sometimes wither skulls do entity attack damage (non-explosion)... cancel it!
	@EventHandler(priority = EventPriority.LOWEST)
	public void directHitDamageCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		if (event.GetDamageInitial() != 7)
			return;
		
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;
		
		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;
		
		event.SetCancelled("Wither Skull Direct Hit");
	}
	
	private void explode(WitherSkull skull, Location loc, LivingEntity shooter) 
	{	
		double scale = 0.4 + 0.6 * Math.min(1, skull.getTicksLived()/20d);
		
		//Players
		HashMap<Player, Double> players = UtilPlayer.getInRadius(skull.getLocation(), 7);
		for (Player player : players.keySet())
		{
			if (!Manager.GetGame().IsAlive(player))
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent(player, (LivingEntity)skull.getShooter(), null, 
					DamageCause.CUSTOM, 2 + 10 * players.get(player) * scale, true, true, false,
					UtilEnt.getName((LivingEntity)skull.getShooter()), GetName());
		}
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 1.5);
	}
}
