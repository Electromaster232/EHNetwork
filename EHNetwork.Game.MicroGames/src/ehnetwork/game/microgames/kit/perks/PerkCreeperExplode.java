package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseCreeper;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkCreeperExplode extends SmashPerk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	private HashSet<Player> _super = new HashSet<Player>();
	
	public PerkCreeperExplode() 
	{
		super("Explode", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Shovel use " + C.cGreen + "Explosive Leap"
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

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE"))
			return;

		Player player = event.getPlayer();
		
		if (isSuperActive(player))
			return;

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 8000, true, true))
			return;

		_active.put(player, System.currentTimeMillis());

		IncreaseSize(player);

		UtilPlayer.message(player, F.main("Skill", "You are charging " + F.skill(GetName()) + "."));
	}

	@Override
	public void addSuperCustom(Player player)
	{
		_active.put(player, System.currentTimeMillis());
		_super.add(player);

		IncreaseSize(player);
	}
	
	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> chargeIterator = _active.keySet().iterator();

		while (chargeIterator.hasNext())
		{
			Player player = chargeIterator.next();

			double elapsed = (System.currentTimeMillis() - _active.get(player))/1000d;

			//Idle in Air
			player.setVelocity(new Vector(0,0,0));
			
			//Sound
			player.getWorld().playSound(player.getLocation(), Sound.CREEPER_HISS, (float)(0.5 + elapsed), (float)(0.5 + elapsed));

			IncreaseSize(player);
			
			player.setExp(Math.min(0.99f, (float)(elapsed/1.5)));

			//Not Detonated
			if (elapsed < 1.5)
				continue;

			chargeIterator.remove();

			//Unpower
			DecreaseSize(player);

			boolean isSuper = _super.remove(player);
			
			//Explode
			if (!isSuper)
			{
				//Effect
				UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, player.getLocation(), 0, 0, 0, 0, 1,
						ViewDist.MAX, UtilServer.getPlayers());
				player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 2f, 1f);
			}
			else
			{
				//Particles
				UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, player.getLocation(), 5f, 5f, 5f, 0, 20,
						ViewDist.MAX, UtilServer.getPlayers());
				
				//Sound
				for (int i=0 ; i<4 ; i++)
					player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, (float)(2 + Math.random()*4), (float)(Math.random() + 0.2));
				
				//Blocks
				Manager.GetExplosion().BlockExplosion(UtilBlock.getInRadius(player.getLocation(), 12).keySet(), player.getLocation(), false);
				
				//Remove Spawns
				Iterator<Location> spawnIterator = Manager.GetGame().GetTeam(player).GetSpawns().iterator();
				while (spawnIterator.hasNext())
				{
					Location spawn = spawnIterator.next();
					
					if (UtilMath.offset(player.getLocation(), spawn) < 14)
						spawnIterator.remove();
				}
			}
			
			//Damage
			for (Entity ent : player.getWorld().getEntities())
			{
				if (!(ent instanceof LivingEntity))
					continue;
				
				if (ent.equals(player))
					continue;

				double dist = UtilMath.offset(player.getLocation(), ent.getLocation());
				
				double maxRange = 8;
				if (isSuper)
					maxRange = 24;
				
				double damage = 20;
				if (isSuper)
					damage = 30;
				
				if (dist > maxRange)
					continue;

				if (ent instanceof Player)
					if (!Manager.GetGame().IsAlive((Player)ent))
						continue;

				LivingEntity livingEnt = (LivingEntity)ent;
				
				double scale = 0.1 + 0.9 * ((maxRange-dist)/maxRange);
				
				//Damage Event
				Manager.GetDamage().NewDamageEvent(livingEnt, player, null, 
						DamageCause.CUSTOM, damage * scale, true, true, false,
						player.getName(), isSuper ? "Atomic Blast" : GetName());
			}
			
			//Velocity
			UtilAction.velocity(player, 1.8, 0.2, 1.4, true);
			
			//Inform
			if (!isSuper)
				UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
		}
	}

	public DisguiseCreeper GetDisguise(Player player)
	{
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise == null)
			return null;

		if (!(disguise instanceof DisguiseCreeper))
			return null;

		return (DisguiseCreeper)disguise;
	}
	
	public int GetSize(Player player)
	{
		DisguiseCreeper creeper = GetDisguise(player);
		if (creeper == null)	return 0;
		
		return creeper.bV();
	}
	
	public void DecreaseSize(Player player)
	{
		DisguiseCreeper creeper = GetDisguise(player);
		if (creeper == null)	return;
		
		creeper.a(-1);
		
		Manager.GetDisguise().updateDisguise(creeper);
	}
	
	public void IncreaseSize(Player player)
	{
		DisguiseCreeper creeper = GetDisguise(player);
		if (creeper == null)	return;
		
		creeper.a(1);
		
		Manager.GetDisguise().updateDisguise(creeper);
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2.5);
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent event)
	{
		if (!Kit.HasKit(event.getEntity()))
			return;
		
		_active.remove(event.getEntity());
		_super.remove(event.getEntity());
		
		DecreaseSize(event.getEntity());
	}
}
