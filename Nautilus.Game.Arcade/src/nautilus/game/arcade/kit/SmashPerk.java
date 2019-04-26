package nautilus.game.arcade.kit;

import java.util.HashSet;

import org.bukkit.entity.Player;

public abstract class SmashPerk extends Perk
{
	private HashSet<Player> _superActive = new HashSet<Player>();
	
	public SmashPerk(String name, String[] perkDesc)
	{
		super(name, perkDesc);
	}
	
	public SmashPerk(String name, String[] perkDesc, boolean display)
	{
		super(name, perkDesc, display);
	}

	public void addSuperActive(Player player)
	{
		_superActive.add(player);
		
		addSuperCustom(player); 
	}

	public void addSuperCustom(Player player)
	{
		
	}

	public void removeSuperActive(Player player)
	{
		if (_superActive.remove(player))
			removeSuperCustom(player);
	}
	
	public void removeSuperCustom(Player player)
	{
		
	}

	public boolean isSuperActive(Player player)
	{
		return _superActive.contains(player);
	}
}
