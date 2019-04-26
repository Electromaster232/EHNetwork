package mineplex.minecraft.game.core.fire;

import org.bukkit.entity.LivingEntity;

public class FireData 
{
	private LivingEntity _owner;
	private long _expireTime; 
	private long _delayTime;
	private double _burnTime;
	private int _damage;
	private String _skillName;
	
	public FireData(LivingEntity owner, double expireTime, double delayTime, double burnTime, int damage, String skillName)
	{
		_owner = owner;
		_expireTime = System.currentTimeMillis() + (long)(1000 * expireTime);
		_delayTime = System.currentTimeMillis() + (long)(1000 * delayTime);
		_burnTime = burnTime;
		_damage = damage;
		_skillName = skillName;
	}
	
	public LivingEntity GetOwner()
	{
		return _owner;
	}
	
	public double GetBurnTime()
	{
		return _burnTime;
	}
	
	public int GetDamage()
	{
		return _damage;
	}
	
	public String GetName()
	{
		return _skillName;
	}
	
	public boolean IsPrimed()
	{
		return System.currentTimeMillis() > _delayTime;
	}
	
	public boolean Expired()
	{
		return System.currentTimeMillis() > _expireTime;
	}
}
