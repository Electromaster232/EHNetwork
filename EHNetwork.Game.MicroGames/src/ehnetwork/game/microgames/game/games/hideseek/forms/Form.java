package ehnetwork.game.microgames.game.games.hideseek.forms;

import org.bukkit.entity.Player;

import ehnetwork.game.microgames.game.games.hideseek.HideSeek;

public abstract class Form 
{
	public HideSeek Host;
	public Player Player;
	
	public Form(HideSeek host, Player player)
	{
		Host = host;
		Player = player;
	}
	
	public abstract void Apply();
	public abstract void Remove();
}
