package ehnetwork.minecraft.game.core.damage;

import java.util.ArrayList;
import java.util.HashMap;

import ehnetwork.core.common.util.C;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CustomDamageEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();

	private DamageCause _eventCause;
	private double _initialDamage;

	private ArrayList<DamageChange> _damageMult = new ArrayList<DamageChange>();
	private ArrayList<DamageChange> _damageMod = new ArrayList<DamageChange>();

	private ArrayList<String> _cancellers = new ArrayList<String>();

	private HashMap<String, Double> _knockbackMod = new HashMap<String, Double>();

	//Ents
	private LivingEntity _damageeEntity;
	private Player _damageePlayer;
	private LivingEntity _damagerEntity;
	private Player _damagerPlayer;
	private Projectile _projectile;
	private Location _knockbackOrigin = null;

	//Flags
	private boolean _ignoreArmor = false; 
	private boolean _ignoreRate = false;		
	private boolean _knockback = true;
	private boolean _damageeBrute = false;
	private boolean _damageToLevel = true;

	public CustomDamageEvent(LivingEntity damagee, LivingEntity damager, Projectile projectile, 
			DamageCause cause, double damage, boolean knockback, boolean ignoreRate, boolean ignoreArmor,
			String initialSource, String initialReason, boolean cancelled)
	{
		_eventCause = cause;

		//if (initialSource == null || initialReason == null)
			_initialDamage = damage;

		_damageeEntity = damagee;
		if (_damageeEntity != null && _damageeEntity instanceof Player)			_damageePlayer = (Player)_damageeEntity;

		_damagerEntity = damager;
		if (_damagerEntity != null && _damagerEntity instanceof Player)			_damagerPlayer = (Player)_damagerEntity;

		_projectile = projectile;

		_knockback = knockback;
		_ignoreRate = ignoreRate;
		_ignoreArmor = ignoreArmor;

		if (initialSource != null && initialReason != null)
			AddMod(initialSource, initialReason, 0, true);
		
		if (_eventCause == DamageCause.FALL)
			_ignoreArmor = true;
		
		if (cancelled)
			SetCancelled("Pre-Cancelled");
	}

	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers; 
	}

	public void AddMult(String source, String reason, double mod, boolean useAttackName)
	{
		_damageMult.add(new DamageChange(source, reason, mod, useAttackName));
	}


	public void AddMod(String source, String reason, double mod, boolean useAttackName)
	{
		_damageMod.add(new DamageChange(source, reason, mod, useAttackName));
	}

	public void AddKnockback(String reason, double d)
	{
		_knockbackMod.put(reason, d);
	}

	public boolean IsCancelled()
	{
		return !_cancellers.isEmpty();
	}

	public void SetCancelled(String reason)
	{
		_cancellers.add(reason);
	}

	public double GetDamage()
	{
		double damage = GetDamageInitial();

		for (DamageChange mult : _damageMod)
			damage += mult.GetDamage();
		
		for (DamageChange mult : _damageMult)
			damage *= mult.GetDamage();

		return damage;
	}

	public LivingEntity GetDamageeEntity()
	{
		return _damageeEntity;
	}

	public Player GetDamageePlayer()
	{
		return _damageePlayer;
	}

	public LivingEntity GetDamagerEntity(boolean ranged)
	{
		if (ranged)
			return _damagerEntity;

		else if (_projectile == null)	
			return _damagerEntity;

		return null;
	}

	public Player GetDamagerPlayer(boolean ranged)
	{
		if (ranged)
			return _damagerPlayer;

		else if (_projectile == null)	
			return _damagerPlayer;

		return null;
	}

	public Projectile GetProjectile()
	{
		return _projectile;
	}

	public DamageCause GetCause()
	{
		return _eventCause;
	}

	public double GetDamageInitial()
	{
		return _initialDamage;
	}

	public void SetIgnoreArmor(boolean ignore)
	{
		_ignoreArmor = ignore;
	}

	public void SetIgnoreRate(boolean ignore) 
	{
		_ignoreRate = ignore;
	}

	public void SetKnockback(boolean knockback) 
	{
		_knockback = knockback;
	}

	public void SetBrute()
	{
		_damageeBrute = true;
	}

	public boolean IsBrute()
	{
		return _damageeBrute;
	}

	public String GetReason() 
	{
		String reason = "";

		//Get Reason
		for (DamageChange change : _damageMod)
			if (change.UseReason())
				reason += C.mSkill + change.GetReason() + ChatColor.GRAY + ", ";

		//Trim Reason
		if (reason.length() > 0)
		{
			reason = reason.substring(0, reason.length() - 2);
			return reason;
		}

		return null;
	}

	public boolean IsKnockback() 
	{
		return _knockback;
	}

	public boolean IgnoreRate() 
	{
		return _ignoreRate;
	}

	public boolean IgnoreArmor() 
	{
		return _ignoreArmor;
	}

	public void SetDamager(LivingEntity ent) 
	{
		if (ent == null)
			return;

		_damagerEntity = ent;

		_damagerPlayer = null;
		if (ent instanceof Player)
			_damagerPlayer = (Player)ent;
	}
	
	public void setDamagee(LivingEntity ent) 
	{
		_damageeEntity = ent;
		
		_damageePlayer = null;
		if (ent instanceof Player)
			_damageePlayer = (Player)ent;
	}
	
	public void changeReason(String initial, String reason)
	{
		for (DamageChange change : _damageMod)
			if (change.GetReason().equals(initial))
				change.setReason(reason);
	}
	
	public void setKnockbackOrigin(Location loc)
	{
		_knockbackOrigin = loc;
	}
	
	public Location getKnockbackOrigin()
	{
		return _knockbackOrigin;
	}

	public ArrayList<DamageChange> GetDamageMod()
	{
		return _damageMod;
	}

	public ArrayList<DamageChange> GetDamageMult()
	{
		return _damageMult;
	}

	public HashMap<String, Double> GetKnockback() 
	{
		return _knockbackMod;
	}

	public ArrayList<String> GetCancellers() 
	{
		return _cancellers;
	}
	
	public void SetDamageToLevel(boolean val)
	{
		_damageToLevel = val;
	}

	public boolean DisplayDamageToLevel() 
	{
		return _damageToLevel;
	}

    @Override
    public boolean isCancelled()
    {
        return IsCancelled();
    }

    @Override
    @Deprecated
    /**
     * Don't call this method. Use SetCancelled(String) instead.
     * 
     * You will be made the butt of jokes if you use this method.
     */
    public void setCancelled(boolean isCancelled)
    {
        SetCancelled("No reason given because SOMEONE IS AN IDIOT");
    }

	
}
