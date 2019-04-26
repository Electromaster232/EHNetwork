package ca.phinary.jedistest.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import ca.phinary.jedistest.api.Console;

public class ConsolePane extends JPanel implements Console
{
	private JScrollPane _scrollPane;
	private JTextArea _textArea;

	public ConsolePane()
	{
		_textArea = new JTextArea();
		_textArea.setEditable(false);
		_textArea.setPreferredSize(new Dimension(800, 400));
		_scrollPane = new JScrollPane(_textArea);
		_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		setLayout(new BorderLayout());
		setBorder(new TitledBorder("Console"));

		add(_scrollPane, BorderLayout.CENTER);
	}

	public synchronized void println(String line)
	{
		_textArea.append(line + "\n");
	}

}
