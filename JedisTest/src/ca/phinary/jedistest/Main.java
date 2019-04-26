package ca.phinary.jedistest;

import javax.swing.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ca.phinary.jedistest.gui.ChatFrame;
import ca.phinary.jedistest.model.JedisChat;

public class Main
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				catch (UnsupportedLookAndFeelException e)
				{
					e.printStackTrace();
				}

				ChatFrame chatFrame = new ChatFrame();
				final JedisChat jedisChat = new JedisChat(chatFrame, "phinaryTest", "10.33.53.16", 6379);

				chatFrame.addWindowListener(new WindowListener()
				{
					@Override
					public void windowOpened(WindowEvent e)
					{

					}

					@Override
					public void windowClosing(WindowEvent e)
					{

					}

					@Override
					public void windowClosed(WindowEvent e)
					{
						jedisChat.close();
					}

					@Override
					public void windowIconified(WindowEvent e)
					{

					}

					@Override
					public void windowDeiconified(WindowEvent e)
					{

					}

					@Override
					public void windowActivated(WindowEvent e)
					{

					}

					@Override
					public void windowDeactivated(WindowEvent e)
					{

					}
				});
			}
		});

	}

}
