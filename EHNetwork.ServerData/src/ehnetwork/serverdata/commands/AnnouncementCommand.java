package ehnetwork.serverdata.commands;


public class AnnouncementCommand extends ServerCommand
{
	private boolean _displayTitle;
	private String _message;
	
	public boolean getDisplayTitle() { return _displayTitle; }
	public String getMessage() { return _message; }
	
	public AnnouncementCommand(boolean displayTitle, String message)
	{
		_displayTitle = displayTitle;
		_message = message;
	}
	
	@Override
	public void run() 
	{
		// Utilitizes a callback functionality to seperate dependencies
	}
}
