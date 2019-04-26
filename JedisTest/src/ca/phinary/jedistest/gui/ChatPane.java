package ca.phinary.jedistest.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ca.phinary.jedistest.api.Chat;
import ca.phinary.jedistest.api.ChatListener;

public class ChatPane extends JPanel implements ActionListener, Chat
{
	private ArrayList<ChatListener> _chatListeners;

	private JTextField _textField;
	private JButton _sendButton;

	public ChatPane(JFrame frame)
	{
		_chatListeners = new ArrayList<ChatListener>();

		_textField = new JTextField();
		_sendButton = new JButton("Send");

		setBorder(new EmptyBorder(5, 10, 5, 10));
		setLayout(new BorderLayout());

		add(_textField, BorderLayout.CENTER);
		add(_sendButton, BorderLayout.EAST);

		_sendButton.addActionListener(this);
		frame.getRootPane().setDefaultButton(_sendButton);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String text = _textField.getText();
		for (ChatListener listener : _chatListeners)
		{
			listener.onChat(text);
		}
		_textField.setText("");
	}

	@Override
	public void addListener(ChatListener chatListener)
	{
		_chatListeners.add(chatListener);
	}

	@Override
	public void clearListeners()
	{
		_chatListeners.clear();
	}
}
