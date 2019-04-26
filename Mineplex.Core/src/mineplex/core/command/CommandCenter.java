package mineplex.core.command;

import java.util.List;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.recharge.Recharge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.TabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandCenter implements Listener
{
	public static CommandCenter Instance;

	protected JavaPlugin Plugin;
	protected CoreClientManager ClientManager;
	protected NautHashMap<String, ICommand> Commands;
	
	public static void Initialize(JavaPlugin plugin)
	{
		if (Instance == null)
			Instance = new CommandCenter(plugin);
	}
	
	public CoreClientManager GetClientManager()
	{
		return ClientManager;
	}
	
	private CommandCenter(JavaPlugin instance)
	{
		Plugin = instance;
		Commands = new NautHashMap<String, ICommand>();
		Plugin.getServer().getPluginManager().registerEvents(this, Plugin);
	}
	
	public void setClientManager(CoreClientManager clientManager)
	{
		ClientManager = clientManager;
	}
	
	@EventHandler
	public void OnPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		String commandName = event.getMessage().substring(1);
		String[] args = null;
		
		if (commandName.contains(" "))
		{
			commandName = commandName.split(" ")[0];
			args = event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ");
		}
		
		ICommand command = Commands.get(commandName.toLowerCase());
		
		if (command != null)
		{
			event.setCancelled(true);

			if (ClientManager.Get(event.getPlayer()).GetRank().Has(event.getPlayer(), command.GetRequiredRank(), command.GetSpecificRanks(), true))
			{
    			if (!Recharge.Instance.use(event.getPlayer(), "Command", 500, false, false))
    			{
    				event.getPlayer().sendMessage(F.main("Command Center", "You can't spam commands that fast."));
    				return;
    			}
    			
    			command.SetAliasUsed(commandName.toLowerCase());
    			command.Execute(event.getPlayer(), args);
    		}
		}
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent event)
	{
		ICommand command = Commands.get(event.getCommand().toLowerCase());

		if (command != null)
		{
			List<String> suggestions = command.onTabComplete(event.getSender(), event.getCommand(), event.getArgs());

			if (suggestions != null)
				event.setSuggestions(suggestions);
		}
	}

	public void AddCommand(ICommand command)
	{
		for (String commandRoot : command.Aliases())
		{
			Commands.put(commandRoot.toLowerCase(), command);
			command.SetCommandCenter(this);
		}
	}

	public void RemoveCommand(ICommand command)
	{
		for (String commandRoot : command.Aliases())
		{
			Commands.remove(commandRoot.toLowerCase());
			command.SetCommandCenter(null);
		}
	}
}
