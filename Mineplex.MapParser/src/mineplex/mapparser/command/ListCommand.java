package mineplex.mapparser.command;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.mapparser.GameType;
import mineplex.mapparser.MapParser;

/**
 * Created by Shaun on 8/15/2014.
 */
public class ListCommand extends BaseCommand
{
	private boolean _colorSwitch = false;
	
	public ListCommand(MapParser plugin)
	{
		super(plugin, "list");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (args.length == 0)
		{
			UtilPlayer.message(player, F.main("Parser", "Listing Maps;"));

			boolean colorSwitch = false;
			
			for (GameType gameType : GameType.values())
			{
				if (gameType == GameType.InProgress)
					continue;
				
				if (listMaps(player, gameType, colorSwitch))
					colorSwitch = !colorSwitch;
			}
		}
		else if (args.length == 1)
		{
			GameType gameType = null;
			if (args[0].equalsIgnoreCase("p"))
			{
				gameType = GameType.InProgress;
			}
			else
			{
				try
				{
					gameType = GameType.valueOf(args[0]);
				}
				catch (Exception e)
				{
					getPlugin().sendValidGameTypes(player);
				}
			}
			
			UtilPlayer.message(player, F.main("Parser", "Listing Maps for gametype " + F.elem(gameType.GetName())));
			listMaps(player, gameType, false);
		}

		return true;
	}

	private boolean listMaps(Player player, GameType gameType, boolean colorSwitch)
	{
			String maps = "";
			ChatColor color = ChatColor.AQUA;
			if (colorSwitch)
				color = ChatColor.GREEN;

			File mapsFolder = new File("map" + File.separator + gameType.GetName());
			if (!mapsFolder.exists())
				return false;

			for (File file : mapsFolder.listFiles())
			{
				if (!file.isDirectory())
					continue;

				maps += color + file.getName() + " ";

				if (color == ChatColor.AQUA)
					color = ChatColor.DARK_AQUA;
				else if (color == ChatColor.DARK_AQUA)
					color = ChatColor.AQUA;
				
				else if (color == ChatColor.GREEN)
					color = ChatColor.DARK_GREEN;
				else if (color == ChatColor.DARK_GREEN)
					color = ChatColor.GREEN;
			}

			// Print line of maps for specific gametype
			if (maps.length() > 0)
			{
				maps = F.elem(ChatColor.RESET + C.Scramble + "!" + ChatColor.RESET + C.Bold + gameType.name()) + "> " + maps;
				player.sendMessage(maps);
				
				return true;
			}
			
			return false;
	}
}
