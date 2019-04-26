package mineplex.core.common.util;

import org.bukkit.ChatColor;

/**
 * Created by Shaun on 11/12/2014.
 */
public class UtilColor
{

	public static byte chatColorToClayData(ChatColor chatColor)
	{
		//TODO
		return 1;
	}

	public static byte chatColorToWoolData(ChatColor chatColor)
	{
		switch (chatColor)
		{
			// 0: white
			// 1: orange
			// 2: magenta
			// 3: light blue
			// 4: yellow
			// 5: lime
			// 6: pink
			// 7: gray
			// 8: light gray
			// 9: cyan
			// 10: purple
			// 11: blue
			// 12: brown
			// 13: green
			// 14: red
			// 15: black
			case BLACK:
				return 1;
			case DARK_BLUE:
				return 11;
			case DARK_GREEN:
				return 13;
			case DARK_AQUA:
				return 9;
			case DARK_PURPLE:
				return 10;
			case GOLD:
				return 1;
			case GRAY:
				return 8;
			case DARK_GRAY:
				return 7;
			case BLUE:
				return 11;
			case GREEN:
				return 5;
			case AQUA:
				return 3;
			case RED:
				return 14;
			case LIGHT_PURPLE:
				return 2;
			case YELLOW:
				return 4;
			default:
				return 0;
		}
	}
}
