package mineplex.minecraft.game.core.condition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import mineplex.core.MiniPlugin;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.minecraft.game.core.condition.Condition.ConditionType;
import mineplex.minecraft.game.core.condition.events.ConditionApplyEvent;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ConditionManager extends MiniPlugin
{
	private ConditionFactory _factory;
	private ConditionApplicator _applicator;
	protected ConditionEffect Effect;

	private WeakHashMap<LivingEntity, LinkedList<Condition>> _conditions = new WeakHashMap<LivingEntity, LinkedList<Condition>>();
	private WeakHashMap<LivingEntity, LinkedList<ConditionActive>> _activeConditions = new WeakHashMap<LivingEntity, LinkedList<ConditionActive>>();

	private HashSet<Entity> _items = new HashSet<Entity>();

	public ConditionManager(JavaPlugin plugin) 
	{
		super("Condition Manager", plugin);

		Factory();
		Applicator();
		Effect();
	} 

	public ConditionFactory Factory()
	{
		if (_factory == null)
			_factory = new ConditionFactory(this);

		return _factory;
	}

	public ConditionApplicator Applicator()
	{
		if (_applicator == null)
			_applicator = new ConditionApplicator();

		return _applicator;
	}

	public ConditionEffect Effect()
	{
		if (Effect == null)
			Effect = new ConditionEffect(this);		

		return Effect;
	}

	public Condition AddCondition(Condition newCon)
	{
		//Event
		ConditionApplyEvent condEvent = new ConditionApplyEvent(newCon);
		getPlugin().getServer().getPluginManager().callEvent(condEvent);

		if (condEvent.isCancelled())
			return null;

		//Add Condition
		if (!_conditions.containsKey(newCon.GetEnt()))
			_conditions.put(newCon.GetEnt(), new LinkedList<Condition>());

		_conditions.get(newCon.GetEnt()).add(newCon);

		//Condition Add
		newCon.OnConditionAdd();

		//Indicator
		HandleIndicator(newCon);

		return newCon;
	}

	public void HandleIndicator(Condition newCon)
	{
		ConditionActive ind = GetIndicatorType(newCon);

		//New Condition
		if (ind == null)
		{
			AddIndicator(newCon);
		}
		//Condition Exists
		else
		{
			UpdateActive(ind, newCon);
		}
	}

	public ConditionActive GetIndicatorType(Condition newCon)
	{
		if (!_activeConditions.containsKey(newCon.GetEnt()))
			_activeConditions.put(newCon.GetEnt(), new LinkedList<ConditionActive>());

		for (ConditionActive ind : _activeConditions.get(newCon.GetEnt()))
			if (ind.GetCondition().GetType() == newCon.GetType())
				return ind;

		return null;
	}

	public void AddIndicator(Condition newCon)
	{
		//Create
		ConditionActive newInd = new ConditionActive(newCon);

		//Get Inds
		if (!_activeConditions.containsKey(newCon.GetEnt()))
			_activeConditions.put(newCon.GetEnt(), new LinkedList<ConditionActive>());
		
		LinkedList<ConditionActive> entInds = _activeConditions.get(newCon.GetEnt());

		//Add
		entInds.addFirst(newInd);

		//Inform
		if (newCon.GetInformOn() != null)
			UtilPlayer.message(newCon.GetEnt(), F.main("Condition", newCon.GetInformOn()));
	}

	public void UpdateActive(ConditionActive active, Condition newCon)
	{
		//Not Additive

		if (!active.GetCondition().IsExpired())
			if (active.GetCondition().IsBetterOrEqual(newCon, newCon.IsAdd()))
				return;

		active.SetCondition(newCon);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void ExpireConditions(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		/** Conditions **/
		for (LivingEntity ent : _conditions.keySet())
		{
			Iterator<Condition> conditionIterator = _conditions.get(ent).iterator();

			while (conditionIterator.hasNext())
			{
				Condition cond = conditionIterator.next();

				if (cond.Tick())
					conditionIterator.remove();
			}
		}

		/** Indicators **/
		for (LivingEntity ent : _activeConditions.keySet())
		{
			Iterator<ConditionActive> conditionIndicatorIterator = _activeConditions.get(ent).iterator();

			while (conditionIndicatorIterator.hasNext())
			{
				ConditionActive conditionIndicator = conditionIndicatorIterator.next();

				if (conditionIndicator.GetCondition().IsExpired())
				{
					Condition replacement = GetBestCondition(ent, conditionIndicator.GetCondition().GetType());

					if (replacement == null)
					{
						conditionIndicatorIterator.remove();

						//Inform
						if (conditionIndicator.GetCondition().GetInformOff() != null)
							UtilPlayer.message(conditionIndicator.GetCondition().GetEnt(), F.main("Condition", conditionIndicator.GetCondition().GetInformOff()));
					}
					else
						UpdateActive(conditionIndicator, replacement);
				}
			}
		}
	}

	public Condition GetBestCondition(LivingEntity ent, ConditionType type)
	{
		if (!_conditions.containsKey(ent))
			return null;

		Condition best = null;

		for (Condition con : _conditions.get(ent))
		{
			if (con.GetType() != type)
				continue;

			if (con.IsExpired())
				continue;

			if (best == null)
			{
				best = con;
				continue;
			}

			if (con.IsBetterOrEqual(best, false))
				best = con;
		}

		return best;
	}

	public Condition GetActiveCondition(LivingEntity ent, ConditionType type)
	{
		if (!_activeConditions.containsKey(ent))
			return null;

		for (ConditionActive ind : _activeConditions.get(ent))
		{
			if (ind.GetCondition().GetType() != type)
				continue;

			if (ind.GetCondition().IsExpired())
				continue;

			return ind.GetCondition();
		}

		return null;
	}

	@EventHandler
	public void Remove(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		HashSet<Entity> expired = new HashSet<Entity>();

		for (Entity cur : _items)
			if (UtilEnt.isGrounded(cur) || cur.isDead() || !cur.isValid())
				expired.add(cur);

		for (Entity cur : expired)
		{
			_items.remove(cur);
			cur.remove(); 
		}
	}

	@EventHandler
	public void Respawn(PlayerRespawnEvent event)
	{
		Clean(event.getPlayer());
	}

	@EventHandler
	public void Quit(PlayerQuitEvent event)
	{
		Clean(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void Death(EntityDeathEvent event)
	{
		//Still Alive - SHOULD IGNORE DEATHS FROM DOMINATE
		if (event.getEntity() instanceof Player)
			if (event.getEntity().getHealth() > 0)
				return;

		Clean(event.getEntity());
	}

	public void Clean(LivingEntity ent)
	{
		//Wipe Conditions
		_conditions.remove(ent);
		_activeConditions.remove(ent);	
	}

	@EventHandler
	public void Debug(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (LivingEntity ent : _activeConditions.keySet())
		{
			if (!(ent instanceof Player))
				continue;

			Player player = (Player)ent;
			if (player.getItemInHand() == null)
				continue;
			
			if (player.getItemInHand().getType() != Material.PAPER)
				continue;
			
			if (!player.isOp())
				continue;

			UtilPlayer.message(player, C.cGray + _activeConditions.get(ent).size() + " Indicators ----------- " + _conditions.get(ent).size() + " Conditions");
			for (ConditionActive ind : _activeConditions.get(ent))
				UtilPlayer.message(player, 
						F.elem(ind.GetCondition().GetType() + " " + (ind.GetCondition().GetMult()+1)) + " for " + 
								F.time(UtilTime.convertString(ind.GetCondition().GetTicks()*50L, 1, TimeUnit.FIT)) + " via " +
								F.skill(ind.GetCondition().GetReason()) + " from " + 
								F.name(UtilEnt.getName(ind.GetCondition().GetSource())) + ".");
		}
	}

	@EventHandler
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (_items.contains(event.getItem()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void HopperPickup(InventoryPickupItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (_items.contains(event.getItem()))
			event.setCancelled(true);
	}

	public void EndCondition(LivingEntity target, ConditionType type, String reason)
	{
		if (!_conditions.containsKey(target))
			return;

		for (Condition cond : _conditions.get(target))
			if (reason == null || cond.GetReason().equals(reason))
				if (type == null || cond.GetType() == type)
				{
					cond.Expire();
					
					Condition best = GetBestCondition(target, cond.GetType());
					if (best != null)	best.Apply();
				}
	}

	public boolean HasCondition(LivingEntity target, ConditionType type, String reason)
	{
		if (!_conditions.containsKey(target))
			return false;

		for (Condition cond : _conditions.get(target))
			if (reason == null || cond.GetReason().equals(reason))
				if (type == null || cond.GetType() == type)
					return true;

		return false;
	}

	public WeakHashMap<LivingEntity, LinkedList<ConditionActive>> GetActiveConditions()
	{
		return _activeConditions;
	}

	public boolean IsSilenced(LivingEntity ent, String ability)
	{
		if (!_activeConditions.containsKey(ent))
			return false;

		for (ConditionActive ind : _activeConditions.get(ent))
			if (ind.GetCondition().GetType() == ConditionType.SILENCE)
			{
				if (ability != null)
				{
					if (ent instanceof Player)
					{
						if (Recharge.Instance.use((Player)ent, "Silence Feedback", 200, false, false))
						{
							//Inform
							UtilPlayer.message(ent, F.main("Condition", "Cannot use " + F.skill(ability) + " while silenced."));
							
							//Effect
							((Player)ent).playSound(ent.getLocation(), Sound.BAT_HURT, 0.8f, 0.8f);
						}
					}			
				}
				return true;
			}

		return false;
	}

	public boolean IsInvulnerable(LivingEntity ent)
	{
		if (!_activeConditions.containsKey(ent))
			return false;

		for (ConditionActive ind : _activeConditions.get(ent))
			if (ind.GetCondition().GetType() == ConditionType.INVULNERABLE)
				return true;

		return false;
	}
	
	public boolean IsCloaked(LivingEntity ent)
	{
		if (!_activeConditions.containsKey(ent))
			return false;

		for (ConditionActive ind : _activeConditions.get(ent))
			if (ind.GetCondition().GetType() == ConditionType.CLOAK)
				if (!ind.GetCondition().IsExpired())
					return true;

		return false;
	}

	@EventHandler
	public void CleanUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		Iterator<Entry<LivingEntity, LinkedList<ConditionActive>>> conditionIndIterator = _activeConditions.entrySet().iterator();
		
		while (conditionIndIterator.hasNext())
		{
			Entry<LivingEntity, LinkedList<ConditionActive>> entry = conditionIndIterator.next();
			LivingEntity ent = entry.getKey();
			
			if (ent.isDead() || !ent.isValid() || (ent instanceof Player && !((Player)ent).isOnline()))
			{
				ent.remove();
				conditionIndIterator.remove();
			}
		}
		
		Iterator<Entry<LivingEntity, LinkedList<Condition>>> conditionIterator = _conditions.entrySet().iterator();
		
		while (conditionIterator.hasNext())
		{
			Entry<LivingEntity, LinkedList<Condition>> entry = conditionIterator.next();
			LivingEntity ent = entry.getKey();
			
			if (ent.isDead() || !ent.isValid() || (ent instanceof Player && !((Player)ent).isOnline()))
			{
				ent.remove();
				conditionIterator.remove();
			}
		}
	}
	
	@EventHandler
	public void Debug(PlayerCommandPreprocessEvent event)
	{
		if (event.getPlayer().getName().equals("Chiss"))
		{
			if (event.getMessage().equals("/debugcond1"))
			{
				_factory.Regen("Debug", event.getPlayer(), event.getPlayer(), 30, 0, false, false, false);
				event.getPlayer().sendMessage("Regen 1 for 30s");
				event.setCancelled(true);
			}
			else if (event.getMessage().equals("/debugcond2"))
			{
				_factory.Regen("Debug", event.getPlayer(), event.getPlayer(), 15, 1, false, false, false);
				event.getPlayer().sendMessage("Regen 2 for 15s");
				event.setCancelled(true);
			}
			else if (event.getMessage().equals("/debugcond3"))
			{
				_factory.Regen("Debug", event.getPlayer(), event.getPlayer(), 5, 2, false, false, false);
				event.getPlayer().sendMessage("Regen 3 for 5s");
				event.setCancelled(true);
			}
			else if (event.getMessage().equals("/debugcond4"))
			{
				_factory.Slow("Debug", event.getPlayer(), event.getPlayer(), 5, 0, true, false, false, false);
				event.setCancelled(true);
			}
			else if (event.getMessage().equals("/debugcond5"))
			{
				_factory.Ignite("Debug", event.getPlayer(), event.getPlayer(), 5, true, false);
				event.getPlayer().sendMessage("Regen 1 for 30s");
				event.setCancelled(true);
			}
		}
	}
}
