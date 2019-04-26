package mineplex.minecraft.game.core.combat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilTime;
import mineplex.minecraft.game.core.damage.DamageChange;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CombatLog
{
	private LinkedList<CombatComponent> _damager = new LinkedList<CombatComponent>();	
	private CombatComponent _player;
	private long _expireTime;

	private long _deathTime = 0;
	private CombatComponent _killer;
	private int _assistants;

	private String _killedColor = ChatColor.YELLOW + "";
	private String _killerColor = ChatColor.YELLOW + "";

	protected CombatComponent LastDamager;
	protected long _lastDamaged;
	protected long _lastCombat;
	
	public CombatLog(Player player, long expireTime)
	{
		_expireTime = expireTime;
		_player = new CombatComponent(player.getName(), player);
	}

	public LinkedList<CombatComponent> GetAttackers()
	{
		return _damager;
	}

	public CombatComponent GetPlayer()
	{
		return _player;
	}

	public void Attacked(String damagerName, double damage,
			LivingEntity damagerEnt, String attackName, List<DamageChange> mod)
	{
		// Add Attacked
		CombatComponent comp = GetEnemy(damagerName, damagerEnt);

		comp.AddDamage(attackName, damage, mod);
		
		// Set Last
		LastDamager = comp;
		_lastDamaged = System.currentTimeMillis();
		_lastCombat = System.currentTimeMillis();
	}

	public CombatComponent GetEnemy(String name, LivingEntity ent)
	{
		ExpireOld();

		CombatComponent component = null;
		for (CombatComponent cur : _damager)
		{
			if (cur.GetName().equals(name))
				component = cur;
		}

		// Player has attacked in past
		if (component != null)
		{
			_damager.remove(component);
			_damager.addFirst(component);
			return _damager.getFirst();
		}

		_damager.addFirst(new CombatComponent(name, ent));
		return _damager.getFirst();
	}

	public void ExpireOld()
	{
		int expireFrom = -1;
		for (int i = 0; i < _damager.size(); i++)
		{
			if (UtilTime.elapsed(_damager.get(i).GetLastDamage(), _expireTime))
			{
				expireFrom = i;
				break;
			}
		}

		if (expireFrom != -1)
			while (_damager.size() > expireFrom)
				_damager.remove(expireFrom);
	}

	public LinkedList<String> Display()
	{
		LinkedList<String> out = new LinkedList<String>();

		for (int i = 0; i < 8; i++)
		{
			if (i < _damager.size())
				out.add(F.desc("#" + i, _damager.get(i).Display(_deathTime)));
		}

		return out;
	}
	
	public LinkedList<String> DisplayAbsolute()
	{
		HashMap<Long, String> components = new HashMap<Long, String>();

		for (CombatComponent cur : _damager)
		{
			for (CombatDamage dmg : cur.GetDamage())
			{
				components.put(dmg.GetTime(), cur.Display(_deathTime, dmg));
			}
		}
		
		int id = components.size();
		LinkedList<String> out = new LinkedList<String>();
		
		while (!components.isEmpty())
		{
			long bestTime = 0;
			String bestString = null;
			
			for (long time : components.keySet())
			{
				if (time > bestTime || bestString == null)
				{
					bestTime = time;
					bestString = components.get(time);
				}
			}
			
			components.remove(bestTime);
			
			out.addFirst(F.desc("#" + id, bestString));
			id--;
		}
		
		return out;
	}

	public CombatComponent GetKiller()
	{
		return _killer;
	}

	public void SetKiller(CombatComponent killer)
	{
		_killer = killer;
	}

	public int GetAssists()
	{
		return _assistants;
	}

	public void SetAssists(int assistants)
	{
		_assistants = assistants;
	}

	public CombatComponent GetLastDamager()
	{
		return LastDamager;
	}
	
	public long GetLastDamaged()
	{
		return _lastDamaged;
	}
	
	public long GetLastCombat()
	{
		return _lastCombat;
	}
	
	public void SetLastCombat(long time)
	{
		_lastCombat = time;
	}

	public long GetDeathTime()
	{
		return _deathTime;
	}

	public void SetDeathTime(long deathTime)
	{
		_deathTime = deathTime;
	}

	public String GetKilledColor()
	{
		return _killedColor;
	}

	public void SetKilledColor(String color)
	{
		_killedColor = color;
	}

	public String GetKillerColor()
	{
		return _killerColor;
	}

	public void SetKillerColor(String color)
	{
		_killerColor = color;
	}

	
}
