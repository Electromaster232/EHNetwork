package ehnetwork.core.chat;

public class MessageData
{
	private String _message;
	private long _timeSent;

	public MessageData(String message)
	{
		this(message, System.currentTimeMillis());
	}

	public MessageData(String message, long timeSent)
	{
		_message = message;
		_timeSent = timeSent;
	}

	public String getMessage()
	{
		return _message;
	}

	public long getTimeSent()
	{
		return _timeSent;
	}
}
