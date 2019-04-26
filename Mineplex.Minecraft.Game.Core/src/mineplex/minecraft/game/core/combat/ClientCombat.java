package mineplex.minecraft.game.core.combat;

import java.util.LinkedList;
import java.util.WeakHashMap;

import mineplex.core.common.util.UtilTime;

import org.bukkit.entity.LivingEntity;

public class ClientCombat
{
	private LinkedList<CombatLog> _kills = new LinkedList<CombatLog>();
	private LinkedList<CombatLog> _assists = new LinkedList<CombatLog>();
	private LinkedList<CombatLog> _deaths = new LinkedList<CombatLog>();

	private WeakHashMap<LivingEntity, Long> _lastHurt = new WeakHashMap<LivingEntity, Long>();
	private WeakHashMap<LivingEntity, Long> _lastHurtBy = new WeakHashMap<LivingEntity, Long>();
	private long _lastHurtByWorld = 0;
	
	public LinkedList<CombatLog> GetKills() 
	{
		return _kills;
	}

	public LinkedList<CombatLog> GetAssists() 
	{
		return _assists;
	}

	public LinkedList<CombatLog> GetDeaths() 
	{
		return _deaths;
	}

	public boolean CanBeHurtBy(LivingEntity damager)
	{
		if (damager == null)
		{
			if (UtilTime.elapsed(_lastHurtByWorld, 250))
			{
				_lastHurtByWorld = System.currentTimeMillis();
				return true;
			}
			else
			{
				return false;
			}
		}
			
		if (!_lastHurtBy.containsKey(damager))
		{
			_lastHurtBy.put(damager, System.currentTimeMillis());
			return true;
		}

		if (System.currentTimeMillis() - _lastHurtBy.get(damager) > 400)
		{
			_lastHurtBy.put(damager, System.currentTimeMillis());
			return true;
		}

		return false;
	}

	public boolean CanHurt(LivingEntity damagee) 
	{
		if (damagee == null)
			return true;

		if (!_lastHurt.containsKey(damagee))
		{
			_lastHurt.put(damagee, System.currentTimeMillis());
			return true;
		}

		if (System.currentTimeMillis() - _lastHurt.get(damagee) > 400)
		{
			_lastHurt.put(damagee, System.currentTimeMillis());
			return true;
		}

		return false;
	}
}
