package mineplex.hub.mail;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.UtilTime;
import mineplex.hub.notification.api.Notification;
import mineplex.hub.notification.api.NotificationPriority;

public class MailMessage implements Notification
{
	private MailManager _manager;

	private int _messageId;
	private String _sender;
	private String _message;
	private boolean _archived;
	private boolean _deleted;
	private Timestamp _timeSent;

	public MailMessage(MailManager manager, int messageId, String sender, String message, boolean archived, boolean deleted, Timestamp timeSent)
	{
		_manager = manager;

		_messageId = messageId;
		_sender = sender;
		_message = message;
		_archived = archived;
		_deleted = deleted;
		_timeSent = timeSent;
	}

	public int getMessageId()
	{
		return _messageId;
	}

	public void setMessageId(int messageId)
	{
		_messageId = messageId;
	}

	public String getSender()
	{
		return _sender;
	}

	public void setSender(String sender)
	{
		_sender = sender;
	}

	public String getMessage()
	{
		return _message;
	}

	public void setMessage(String message)
	{
		_message = message;
	}

	public boolean isArchived()
	{
		return _archived;
	}

	public void setArchived(boolean archived)
	{
		_archived = archived;
	}

	public boolean isDeleted()
	{
		return _deleted;
	}

	public void setDeleted(boolean deleted)
	{
		_deleted = deleted;
	}

	public Timestamp getTimeSent()
	{
		return _timeSent;
	}

	public void setTimeSent(Timestamp timeSent)
	{
		_timeSent = timeSent;
	}

	@Override
	public String getTitle()
	{
		return "Mail Message";
	}

	@Override
	public String[] getText()
	{
		ArrayList<String> lines = new ArrayList<String>();

		ArrayList<String> message = formatMessage();

		long timeDifference = System.currentTimeMillis() - getTime();


		lines.add(ChatColor.RESET + C.cYellow + "From: " + C.cWhite + _sender);
		lines.add(ChatColor.RESET + C.cYellow + "Sent: " + C.cWhite + UtilTime.convertString(timeDifference, 0, UtilTime.TimeUnit.FIT) + " Ago");
		lines.add(" ");
		lines.addAll(message);

		return lines.toArray(new String[lines.size()]);
	}

	@Override
	public long getTime()
	{
		return _timeSent.getTime();
	}

	@Override
	public Material getMaterial()
	{
		return Material.PAPER;
	}

	@Override
	public byte getData()
	{
		return 0;
	}

	@Override
	public void clicked(final Player player, ClickType clickType)
	{
		if (clickType == ClickType.SHIFT_RIGHT)
		{
			_manager.archive(this, new Callback<Boolean>()
			{
				@Override
				public void run(Boolean data)
				{
					if (data)
					{
						player.playSound(player.getLocation(), Sound.SPLASH, 1, 0);
					}
				}
			});
		}
	}

	@Override
	public NotificationPriority getPriority()
	{
		return NotificationPriority.NORMAL;
	}

	private ArrayList<String> formatMessage()
	{
		String mailMessage = ChatColor.translateAlternateColorCodes('&', _message); // Color the message

		ArrayList<String> parts = new ArrayList<String>();
		int breakIndex = 0;
		int charCount = 0;

		for (String s : mailMessage.split("\\\\n"))
		{
			for (int currIndex = 0; currIndex < s.length(); currIndex++)
			{
				charCount++;
				char c = s.charAt(currIndex);

				if ((charCount >= 36 && c == ' ') || c == '\n')
				{
					// New Line
					parts.add(ChatColor.RESET + s.substring(breakIndex, currIndex).trim());
					breakIndex = currIndex;
					charCount = 0;
				}
			}
			// Add final part
			parts.add(ChatColor.RESET + s.substring(breakIndex).trim());
			charCount = 0;
			breakIndex = 0;
		}

		return parts;
	}
}
