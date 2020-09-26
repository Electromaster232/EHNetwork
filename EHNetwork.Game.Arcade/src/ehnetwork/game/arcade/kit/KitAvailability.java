package ehnetwork.game.arcade.kit;

import org.bukkit.ChatColor;

public enum KitAvailability
{
	Free(ChatColor.YELLOW),
	Gem(ChatColor.GREEN),
	Achievement(ChatColor.LIGHT_PURPLE),
	Hide(ChatColor.YELLOW),
	Null(ChatColor.BLACK);
	
	ChatColor _color;
	
	KitAvailability(ChatColor color)
	{
		_color = color;
	}
	
	public ChatColor GetColor()
	{
		return _color;
	}
}
