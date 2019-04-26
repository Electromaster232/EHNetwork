package mineplex.serverdata.commands;


public class CommandType
{

	private Class<? extends ServerCommand> _commandClazz;
	public Class<? extends ServerCommand> getCommandType() { return _commandClazz; }
	
	private CommandCallback _commandCallback;
	public CommandCallback getCallback() { return _commandCallback; }
	
	public CommandType(Class<? extends ServerCommand> commandClazz, CommandCallback commandCallback)
	{
		_commandClazz = commandClazz;
		_commandCallback = commandCallback;
	}
}
