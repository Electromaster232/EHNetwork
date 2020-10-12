package ca.phinary.jedistest.gui;

import javax.swing.*;

import java.awt.*;

import ca.phinary.jedistest.api.Chat;
import ca.phinary.jedistest.api.Console;
import ca.phinary.jedistest.api.Messenger;

public class ChatFrame extends JFrame implements Messenger
{
	private ConsolePane _console;
	private ChatPane _chat;

	public ChatFrame()
	{
		setLayout(new BorderLayout());

		_console = new ConsolePane();
		_chat = new ChatPane(this);

		add(_console, BorderLayout.CENTER);
		add(_chat, BorderLayout.SOUTH);

		setTitle("Phinary's Redis Chat");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public Console getConsole()
	{
		return _console;
	}

	public Chat getChat()
	{
		return _chat;
	}
}
