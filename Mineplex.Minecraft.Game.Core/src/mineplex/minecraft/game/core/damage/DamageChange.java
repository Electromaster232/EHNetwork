package mineplex.minecraft.game.core.damage;

public class DamageChange 
{
	private String _source;
	private String _reason;
	private double _modifier;
	private boolean _useReason;
	
	public DamageChange(String source, String reason, double modifier, boolean useReason)
	{
		_source = source;
		_reason = reason;
		_modifier = modifier;
		_useReason = useReason;
	}
	
	public String GetSource()
	{
		return _source;
	}
	
	public String GetReason()
	{
		return _reason;
	}
	
	public double GetDamage()
	{
		return _modifier;
	}
	
	public boolean UseReason()
	{
		return _useReason;
	}

	public void setReason(String reason)
	{
		_reason = reason;
	}
}
