package mineplex.core.command;

import java.util.ArrayList;
import java.util.List;

import mineplex.core.MiniPlugin;
import mineplex.core.common.Rank;
import mineplex.core.common.util.NautHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MultiCommandBase<PluginType extends MiniPlugin> extends CommandBase<PluginType>
{
	protected NautHashMap<String, ICommand> Commands;
	
	public MultiCommandBase(PluginType plugin, Rank rank, String...aliases)
	{
		super(plugin, rank, aliases);
		
		Commands = new NautHashMap<String, ICommand>();
	}
	
	public MultiCommandBase(PluginType plugin, Rank rank, Rank[] specificRanks, String...aliases)
	{
		super(plugin, rank, specificRanks, aliases);
		
		Commands = new NautHashMap<String, ICommand>();
	}
	
	public void AddCommand(ICommand command)
	{
		for (String commandRoot : command.Aliases())
		{
			Commands.put(commandRoot, command);
			command.SetCommandCenter(CommandCenter);
		}
	}
	@Override
	public void Execute(Player caller, String[] args)
	{
		String commandName = null;
		String[] newArgs = null;
		
		if (args != null && args.length > 0)
		{
			commandName = args[0];
			
			if (args.length > 1)
			{
				newArgs = new String[args.length - 1];
				
				for (int i = 0 ; i < newArgs.length; i++) 
				{
					newArgs[i] = args[i+1];
				}
			}
		}
		
		ICommand command = Commands.get(commandName);
		
		if (command != null && CommandCenter.ClientManager.Get(caller).GetRank().Has(caller, command.GetRequiredRank(), command.GetSpecificRanks(), true))
		{
			command.SetAliasUsed(commandName);

			command.Execute(caller, newArgs);
		}
		else
		{
			Help(caller, args);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args)
	{
		if (args.length == 1)
		{
			List<String> possibleMatches = new ArrayList<String>();

			for (ICommand command : Commands.values())
			{
				possibleMatches.addAll(command.Aliases());
			}

			return getMatches(args[0], possibleMatches);
		}
		else if (args.length > 1)
		{
			String commandName = args[0];

			ICommand command = Commands.get(commandName);

			if (command != null)
			{
				return command.onTabComplete(sender, commandLabel, args);
			}
		}

		return null;
	}

	protected abstract void Help(Player caller, String[] args);
}
