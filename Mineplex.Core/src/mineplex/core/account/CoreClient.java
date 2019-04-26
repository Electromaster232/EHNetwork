package mineplex.core.account;

import mineplex.core.common.Rank;

import org.bukkit.entity.Player;

public class CoreClient 
{
    private int _accountId = -1;
	private String _name;
	private Player _player;
	private Rank _rank;
	
	public CoreClient(Player player)
	{
		_player = player;
		_name = player.getName();
	}
	
	public CoreClient(String name)
	{
		_name = name;
	}

	public String GetPlayerName()
	{
		return _name;
	}
	
	public Player GetPlayer()
	{
		return _player;
	}
	
    public void SetPlayer(Player player)
    {
        _player = player;
    }
    
    public int getAccountId()
    {
        return _accountId;
    }

	public void Delete() 
	{
		_name = null;
		_player = null;
	}

	public void setAccountId(int accountId)
	{
		_accountId = accountId;
	}

	public Rank GetRank()
	{
		return _rank;
	}
	
	public void SetRank(Rank rank)
	{
		_rank = rank;
	}
}
