package ehnetwork.game.arcade.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.Navigation;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkWolf extends SmashPerk
{
	private HashMap<Wolf, Player> _owner = new HashMap<Wolf, Player>();
	private HashMap<Wolf, LivingEntity> _tackle = new HashMap<Wolf, LivingEntity>();

	private HashMap<Player, Long> _strike = new HashMap<Player, Long>();

	private HashMap<Player, ArrayList<Long>> _repeat = new HashMap<Player, ArrayList<Long>>();
	
	private HashMap<LivingEntity, Long> _tackleStrike = new HashMap<LivingEntity, Long>();

	public PerkWolf() 
	{
		super("Wolf Skills", new String[] 
				{
				C.cGray + "Attacks give +1 Damage for 3 seconds. Stacks.",
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Cub Tackle",
				C.cYellow + "Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Wolf Strike",
				C.cGray + "Wolf Strike deals 300% Knockback to tackled opponents.",
				});
	}

	@EventHandler
	public void TackleTrigger(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return; 

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, "Cub Tackle", isSuperActive(player) ? 800 : 8000, !isSuperActive(player), !isSuperActive(player)))
			return;

		//Get Nearest Wolf
		Manager.GetGame().CreatureAllowOverride = true;
		Wolf wolf = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), Wolf.class);
		Manager.GetGame().CreatureAllowOverride = false;

		wolf.setBaby();

		wolf.setAngry(true);

		UtilEnt.Vegetate(wolf);

		wolf.setMaxHealth(30);
		wolf.setHealth(wolf.getMaxHealth());

		UtilAction.velocity(wolf, player.getLocation().getDirection(), 1.8, false, 0, 0.2, 1.2, true);

		player.getWorld().playSound(wolf.getLocation(), Sound.WOLF_BARK, 1f, 1.8f);

		//Record
		_owner.put(wolf, player);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Cub Tackle") + "."));
	}

	@EventHandler
	public void TackleCollide(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		//Collide
		Iterator<Wolf> wolfIterator = _owner.keySet().iterator();

		while (wolfIterator.hasNext())
		{
			Wolf wolf = wolfIterator.next();

			//Hit Player
			for (Player other : Manager.GetGame().GetPlayers(true))
				if (!Manager.isSpectator(other))
					if (UtilEnt.hitBox(wolf.getLocation(), other, 2, null))
					{
						if (other.equals(TackleGetOwner(wolf)))
							continue;

						TackleCollideAction(TackleGetOwner(wolf), other, wolf);
						wolfIterator.remove();
						return;
					}

			if (!wolf.isValid() ||( UtilEnt.isGrounded(wolf) && wolf.getTicksLived() > 20))  
			{
				wolf.remove();
				wolfIterator.remove();	
			}
		}	
	}

	public void TackleCollideAction(Player damager, LivingEntity damagee, Wolf wolf)
	{
		if (damager == null)
			return;

		_tackle.put(wolf, damagee);

		wolf.setVelocity(new Vector(0,-0.6,0));
		damagee.setVelocity(new Vector(0,0,0));

		//Damage
		Manager.GetDamage().NewDamageEvent(damagee, damager, null, 
				DamageCause.CUSTOM, 5, false, true, false,
				damager.getName(), "Cub Tackle");	

		//Sound
		damagee.getWorld().playSound(damagee.getLocation(), Sound.WOLF_GROWL, 1.5f, 1.5f);

		//Inform
		UtilPlayer.message(damager, F.main("Game", "You hit " + F.name(UtilEnt.getName(damagee)) + " with " + F.skill("Cub Tackle") + "."));
		UtilPlayer.message(damagee, F.main("Game", F.name(damager.getName()) + " hit you with " + F.skill("Cub Tackle") + "."));
	}

	@EventHandler
	public void TackleUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Wolf> wolfIterator = _tackle.keySet().iterator();

		while (wolfIterator.hasNext())
		{
			Wolf wolf = wolfIterator.next();
			LivingEntity ent = _tackle.get(wolf);

			if (!wolf.isValid() || !ent.isValid() || wolf.getTicksLived() > 80)
			{
				wolf.remove();
				wolfIterator.remove();
				continue;
			}

			if (UtilMath.offset(wolf, ent) < 2.5)
			{
				Manager.GetCondition().Factory().Slow("Cub Table", ent, wolf, 0.9, 1, false, false, false, false);
				ent.setVelocity(new Vector(0,-0.3,0));
			}
				
			//Move
			Location loc = ent.getLocation();
			loc.add(UtilAlg.getTrajectory2d(ent, wolf).multiply(1));

			EntityCreature ec = ((CraftCreature)wolf).getHandle();
			Navigation nav = ec.getNavigation();
			nav.a(loc.getX(), loc.getY(), loc.getZ(), 1);
		}
	}

	public Player TackleGetOwner(Wolf wolf)
	{
		if (_owner.containsKey(wolf))
			return _owner.get(wolf);

		return null;
	}

	@EventHandler
	public void TackleTargetCancel(EntityTargetEvent event)
	{
		if (_owner.containsKey(event.getEntity()))
			if (_owner.get(event.getEntity()).equals(event.getTarget()))
				event.setCancelled(true);
	}

	@EventHandler
	public void TackleDamage(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		LivingEntity damager = event.GetDamagerEntity(false);
		if (damager == null)			return;

		if (damager instanceof Wolf)
			event.SetCancelled("Wolf Cub");
	}

	@EventHandler
	public void StrikeTrigger(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE"))
			return; 

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, "Wolf Strike", isSuperActive(player) ? 800 : 8000, !isSuperActive(player), !isSuperActive(player)))
			return;

		//Velocity
		UtilAction.velocity(player, player.getLocation().getDirection(), 1.6, false, 1, 0.2, 1.2, true);

		//Record
		_strike.put(player, System.currentTimeMillis());

		player.getWorld().playSound(player.getLocation(), Sound.WOLF_BARK, 1f, 1.2f);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Wolf Strike") + "."));
	}

	@EventHandler
	public void StrikeEnd(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		//Collide
		Iterator<Player> playerIterator = _strike.keySet().iterator();

		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();

			for (Player other : Manager.GetGame().GetPlayers(true))
				if (!player.equals(other))
					if (!Manager.isSpectator(other))
						if (UtilEnt.hitBox(player.getLocation().add(0, 1, 0), other, 2, null))
						{
							StrikeHit(player, other);
							playerIterator.remove();
							return;
						}

			if (!UtilEnt.isGrounded(player))
				continue;

			if (!UtilTime.elapsed(_strike.get(player), 1500))  
				continue;

			playerIterator.remove();	
		}	
	}

	public void StrikeHit(Player damager, LivingEntity damagee)
	{
		damager.setVelocity(new Vector(0,0,0));

		//Remove Tackle
		Iterator<Wolf> wolfIterator = _tackle.keySet().iterator();
		while (wolfIterator.hasNext())
		{
			Wolf wolf = wolfIterator.next();

			if (_tackle.get(wolf).equals(damagee))
			{
				wolf.remove();
				wolfIterator.remove();
				
				_tackleStrike.put(damagee, System.currentTimeMillis());
			}	
		}

		Manager.GetDamage().NewDamageEvent(damagee, damager, null, 
				DamageCause.CUSTOM, 7, true, true, false,
				damager.getName(), "Wolf Strike");	

		
		//Sound
		damagee.getWorld().playSound(damagee.getLocation(), Sound.WOLF_BARK, 1.5f, 1f);

		//Inform
		UtilPlayer.message(damager, F.main("Game", "You hit " + F.name(UtilEnt.getName(damagee)) + " with " + F.skill("Wolf Strike") + "."));
		UtilPlayer.message(damagee, F.main("Game", F.name(damager.getName()) + " hit you with " + F.skill("Wolf Strike") + "."));
	}

	@EventHandler
	public void StrikeKnockback(CustomDamageEvent event)
	{
		if (event.GetReason() != null && event.GetReason().contains("Wolf Strike"))
		{
			if (_tackleStrike.containsKey(event.GetDamageeEntity()) && !UtilTime.elapsed(_tackleStrike.get(event.GetDamageeEntity()), 100))
			{
				event.AddKnockback(GetName(), 3.0);
				
				//Blood
				event.GetDamageeEntity().getWorld().playEffect(event.GetDamageeEntity().getLocation(), Effect.STEP_SOUND, 55);
				
				//Double Sound
				event.GetDamageeEntity().getWorld().playSound(event.GetDamageeEntity().getLocation(), Sound.WOLF_BARK, 2f, 1.5f);
			}
			else
			{
				event.AddKnockback(GetName(), 1.5);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void RepeatDamage(CustomDamageEvent event)
	{	
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)			return;

		if (!Kit.HasKit(damager))
			return;

		if (!_repeat.containsKey(damager))
		{
			_repeat.put(damager, new ArrayList<Long>());
			_repeat.get(damager).add(System.currentTimeMillis());
			
			//Exp
			damager.setExp(Math.min(0.9999f, _repeat.get(damager).size()/9f));
			
			return;
		}

		int count = _repeat.get(damager).size();

		if (count > 0)
		{
			event.AddMod(damager.getName(), "Ravage", Math.min(2, count), false);

			//Sound
			damager.getWorld().playSound(damager.getLocation(), Sound.WOLF_BARK, (float)(0.5 + count*0.25), (float)(1 + count*0.25));
		}

		_repeat.get(damager).add(System.currentTimeMillis());
		
		//Exp
		damager.setExp(Math.min(0.9999f, _repeat.get(damager).size()/9f));
	}

	@EventHandler
	public void RepeatExpire(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> playerIterator = _repeat.keySet().iterator();

		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();

			Iterator<Long> timeIterator = _repeat.get(player).iterator();

			while (timeIterator.hasNext())
			{
				long time = timeIterator.next();

				if (UtilTime.elapsed(time, 3000))
					timeIterator.remove();	
			}
			
			//Exp
			player.setExp(Math.min(0.9999f, _repeat.get(player).size()/9f));
		}
	}
	
	@Override
	public void addSuperCustom(Player player)
	{
		Recharge.Instance.recharge(player, "Wolf Strike");
		Recharge.Instance.recharge(player, "Cub Tackle");
	}
}
