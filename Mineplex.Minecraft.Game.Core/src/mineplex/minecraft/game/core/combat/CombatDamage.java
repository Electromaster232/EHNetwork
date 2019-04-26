package mineplex.minecraft.game.core.combat;

import java.util.ArrayList;
import java.util.List;

import mineplex.minecraft.game.core.damage.DamageChange;

public class CombatDamage
{
	private String _name;
	private double _dmg;
	private long _time;
	private List<DamageChange> _mod = new ArrayList<>();

	public CombatDamage(String name, double dmg, List<DamageChange> mod)
	{
		_name = name;
		_dmg = dmg;
		_time = System.currentTimeMillis();
		_mod = mod;
	}

	public String GetName()
	{
		return _name;
	}

	public double GetDamage()
	{
		return _dmg;
	}
	
	public long GetTime()
	{
		return _time;
	}

	public List<DamageChange> getDamageMod()
	{
		return _mod;
	}
}
