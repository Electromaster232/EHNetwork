package nautilus.game.arcade.command;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetCommand extends CommandBase<ArcadeManager>
{ 
	public SetCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.MAPLEAD, Rank.JNR_DEV}, "set");
	}
 
	@Override 
	public void Execute(Player caller, String[] args)
	{ 
		if (Plugin.GetGame() == null)
			return;
		 
		if (args == null || args.length == 0)
		{
			caller.sendMessage(F.help("/game set <GameType> (MapSource) (Map)", "Set the current game or next game", Rank.ADMIN));
			return; 
		}
		
		String game = args[0].toLowerCase();
			
		if (args.length >= 2)
		{
			String map = "";
			String source = "";
			if(args.length == 3)
			{
				Plugin.GetGameCreationManager().MapSource = args[1];
				Plugin.GetGameCreationManager().MapPref = args[2];
				source = args[1];
				map = args[2]; 
			} 
			else 
			{
				Plugin.GetGameCreationManager().MapSource = args[0];
				Plugin.GetGameCreationManager().MapPref = args[1];
				source = args[0];
				map = args[1];
			}
			UtilPlayer.message(caller, C.cAqua + C.Bold + "Map Preference: " + ChatColor.RESET + source + ":" + map);
		}
		
		//Parse Game
		ArrayList<GameType> matches = new ArrayList<>();
		for (GameType type : GameType.values())
		{
			if (type.toString().toLowerCase().equals(game))
			{
				matches.clear();
				matches.add(type);
				break;
			}
			
			if (type.toString().toLowerCase().contains(game))
			{
				matches.add(type);
			}
		}
		
		if (matches.size() == 0)
		{
			caller.sendMessage("No results for: " + game);
			return;
		}
		
		if (matches.size() > 1)
		{
			caller.sendMessage("Matched multiple games;");
			for (GameType cur : matches)
				caller.sendMessage(cur.toString());
			return;
		}
		
		GameType type = matches.get(0);
		Plugin.GetGame().setGame(type, caller, true);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args)
	{
		String lastArg = args[args.length - 1];

		return getMatches(lastArg, GameType.values());
	}
}
