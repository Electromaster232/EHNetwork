package mineplex.hub.mail;

import java.util.ArrayList;
import java.util.List;

import mineplex.database.tables.records.MailRecord;

public class PlayerMailData
{
	private List<MailMessage> _messages;

	public PlayerMailData()
	{
		_messages = new ArrayList<MailMessage>();
	}

	public List<MailMessage> getMessages()
	{
		return _messages;
	}

	public List<MailMessage> getUnreadMessages()
	{
		ArrayList<MailMessage> unreadMessages = new ArrayList<MailMessage>();

		for (MailMessage message : _messages)
		{
			if (!(message.isArchived() || message.isDeleted()))
				unreadMessages.add(message);
		}

		return unreadMessages;
	}
 }
