package org.bukkit.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class TabCompleteEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	private CommandSender _sender;
	private String _command;
	private String[] _args;

	private List<String> _suggestions;

	public TabCompleteEvent(CommandSender sender, String command, String[] args)
	{
		_sender = sender;
		_command = command;
		_args = args;

		_suggestions = new ArrayList<String>();
	}

	public CommandSender getSender()
	{
		return _sender;
	}

	public String getCommand()
	{
		return _command;
	}

	public String[] getArgs()
	{
		return _args;
	}

	public List<String> getSuggestions()
	{
		return _suggestions;
	}

	public void setSuggestions(List<String> suggestions)
	{
		_suggestions = suggestions;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
