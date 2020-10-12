package ehnetwork.game.microgames.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkBatWave extends SmashPerk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	private HashMap<Player, Location> _direction = new HashMap<Player, Location>();
	private HashMap<Player, ArrayList<Bat>> _bats = new HashMap<Player, ArrayList<Bat>>();
	private HashSet<Player> _pulling = new HashSet<Player>();
	private HashSet<Player> _allowLeash = new HashSet<Player>();
	
	public PerkBatWave() 
	{
		super("Bat Wave", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Bat Wave",
				C.cYellow + "Double Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Bat Leash"
				});
	}
	
	@EventHandler
	public void Deactivate(CustomDamageEvent event)
	{
		Player player = event.GetDamageePlayer();
		if (player == null)		return;
		
		if (_pulling.remove(player))
		{
			for (Bat bat : _bats.get(player))
				bat.setLeashHolder(null);
		}
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
		
		if (!Recharge.Instance.use(player, GetName(), 8000, false, true))
		{
			if (_active.containsKey(player))
			{
				if (!Recharge.Instance.use(player, "Leash Bats", 500, false, false))
					return;
				
				if (!_pulling.remove(player))
				{
					if (_allowLeash.remove(player))
					{
						_pulling.add(player);
						
						for (Bat bat : _bats.get(player))
							bat.setLeashHolder(player);
					}
				}
				else
				{
					for (Bat bat : _bats.get(player))
						bat.setLeashHolder(null);
				}
			}
			else
			{
				//Inform
				Recharge.Instance.use(player, GetName(), 8000, true, true);
			}
		}
		else
		{
			//Start
			_direction.put(player, player.getEyeLocation());
			_active.put(player, System.currentTimeMillis());
			_allowLeash.add(player);
			
			_bats.put(player, new ArrayList<Bat>());
			
			for (int i=0 ; i<32 ; i++)
			{
				Manager.GetGame().CreatureAllowOverride = true;
				Bat bat = player.getWorld().spawn(player.getEyeLocation(), Bat.class);
				_bats.get(player).add(bat);
				Manager.GetGame().CreatureAllowOverride = false;
			}
			
			//Inform
			UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
		}
	}
	
	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player cur : UtilServer.getPlayers())
		{
			if (!_active.containsKey(cur))
				continue;
			
			if (UtilTime.elapsed(_active.get(cur), 2500))
			{
				Clear(cur);
				continue;
			}
			
			Location loc = _direction.get(cur);
			
			Vector batVec = new Vector(0,0,0);
			double batCount = 0;
			
			//Bat Movement
			for (Bat bat : _bats.get(cur))
			{
				if (!bat.isValid())
					continue;
				
				batVec.add(bat.getLocation().toVector());
				batCount++;
				
				Vector rand = new Vector((Math.random() - 0.5)/2, (Math.random() - 0.5)/2, (Math.random() - 0.5)/2);
				bat.setVelocity(loc.getDirection().clone().multiply(0.5).add(rand));
				
				for (Player other : Manager.GetGame().GetPlayers(true))
				{
					if (other.equals(cur))
						continue;
					
					if (!Recharge.Instance.usable(other, "Hit by Bat"))
						continue;
					
					if (UtilEnt.hitBox(bat.getLocation(), other, 2, null))
					{
						//Damage Event
						Manager.GetDamage().NewDamageEvent(other, cur, null, 
								DamageCause.CUSTOM, 2.5, true, true, false,
								cur.getName(), GetName());	
						
						//Effect
						bat.getWorld().playSound(bat.getLocation(), Sound.BAT_HURT, 1f, 1f);
						UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, bat.getLocation(), 0, 0, 0, 0, 3,
								ViewDist.LONG, UtilServer.getPlayers());
						
						bat.remove();
						
						//Recharge on hit
						Recharge.Instance.useForce(other, "Hit by Bat", 200);
					}
				}
			}
			
			//Player Pull
			if (_pulling.contains(cur))
			{
				batVec.multiply(1/batCount);
				
				Location batLoc = batVec.toLocation(cur.getWorld());
				
				UtilAction.velocity(cur, UtilAlg.getTrajectory(cur.getLocation(), batLoc), 0.35, false, 0, 0, 10, false);
			}
		}
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Clear(event.getPlayer());
	}
	
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event)
	{
		Clear(event.getEntity());
	}
	
	public void Clear(Player player)
	{
		_active.remove(player);
		_direction.remove(player);
		_pulling.remove(player);
		if (_bats.containsKey(player))
		{
			for (Bat bat : _bats.get(player))
			{
				if (bat.isValid())
					UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, bat.getLocation(), 0, 0, 0, 0, 3,
							ViewDist.LONG, UtilServer.getPlayers());
				
				bat.remove();
			}
				
			
			_bats.remove(player);
		}
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 1.75);
	}
}
