package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkSpidersNest extends SmashPerk
{
	private WeakHashMap<LivingEntity, Double> _preHealth = new WeakHashMap<LivingEntity, Double>();
	
	public PerkSpidersNest() 
	{
		super("Spider Nest", new String[] {});
	}
	
	@Override
	public void addSuperCustom(Player player)
	{
		//Nest
		HashMap<Block, Double> blocks = UtilBlock.getInRadius(player.getLocation().getBlock(), 16);
		
		for (Block block : blocks.keySet())
		{
			if (blocks.get(block) > 0.07)
				continue;
			
			if (!UtilBlock.airFoliage(block))
				continue;
			
			if (block.getY() > player.getLocation().getY() + 10)
				continue;
			
			if (block.getY() < player.getLocation().getY() - 10)
				continue;
			
			Manager.GetBlockRestore().Add(block, 30, (byte)0, (long) (30000 + 5000 * Math.random()));	
		}
		
		//Regen
		Manager.GetCondition().Factory().Regen(GetName(), player, player, 30, 0, false, false, false);
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void damagePre(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK &&
			event.GetCause() != DamageCause.PROJECTILE &&
			event.GetCause() != DamageCause.CUSTOM)
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)
			return;
		
		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)
			return;
		
		if (!isSuperActive(damager))
			return;
		
		_preHealth.put(damagee, damagee.getHealth());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void damagePost(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)
			return;
		
		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)
			return;
				
		if (!isSuperActive(damager))
			return;
		
		if (!_preHealth.containsKey(damagee))
			return;

		double diff = (_preHealth.remove(damagee) - damagee.getHealth())/2d;
		
		if (diff <= 0)
			return;
		
		damager.setMaxHealth(Math.min(60, damager.getMaxHealth() + diff));
		damager.setHealth(damager.getHealth() + diff);
		
		UtilParticle.PlayParticle(ParticleType.HEART, damager.getLocation().add(0, 1, 0), 0, 0, 0, 0, 1,
				ViewDist.LONG, UtilServer.getPlayers());
		
		UtilParticle.PlayParticle(ParticleType.RED_DUST, damagee.getLocation().add(0, 1, 0), 0.4f, 0.4f, 0.4f, 0, 12,
				ViewDist.LONG, UtilServer.getPlayers());
		
		if (event.GetCause() == DamageCause.ENTITY_ATTACK)
			damager.getWorld().playSound(damager.getLocation(), Sound.SPIDER_IDLE, 1.5f, 1f);		
	}
}
