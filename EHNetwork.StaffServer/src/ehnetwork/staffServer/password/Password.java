package ehnetwork.staffServer.password;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.F;

public class Password extends MiniPlugin
{
	private PasswordRepository _repository;
	private HashSet<Player> _accepted = new HashSet<Player>();

	private String _serverName;
	private String _password = null;
	
	public Password(JavaPlugin plugin, String serverName)
	{
		super("Password", plugin);
		
		_serverName = serverName;
		
		_repository = new PasswordRepository(plugin, serverName);
		_password = _repository.retrievePassword();
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new PasswordCommand(this));
		addCommand(new ChangePasswordCommand(this));
		addCommand(new RemovePasswordCommand(this));
		addCommand(new CreatePasswordCommand(this));
	}
	
	@EventHandler
	public void promptForPassword(final PlayerJoinEvent event)
	{
		if (_password == null)
			return;
		
		event.getPlayer().sendMessage(F.main(getName(), "Please enter the server password within 10 seconds."));
		
		getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
		{
			public void run()
			{
				if (!_accepted.contains(event.getPlayer()))
					event.getPlayer().kickPlayer("You don't know the password!");
			}
		}, 200L);
	}

	public void checkPassword(Player caller, String attempt)
	{
		if (_password == null)
			return;
		
		if (attempt.equals(_password))
		{
			_accepted.add(caller);
			caller.sendMessage(F.main(getName(), "That is correct, enjoy your time here...and GET TO WORK ;)"));
		}
	}

	public void changePassword(Player caller, String password)
	{
		_password = password;
		
		runAsync(new Runnable()
		{
			public void run()
			{
				_repository.updatePassword(_password);
			}
		});
		
		caller.sendMessage(F.main(getName(), "Password changed to " + _password));
	}

	public void removePassword(Player caller)
	{
		runAsync(new Runnable()
		{
			public void run()
			{
				_password = null;
				_repository.removePassword();
			}
		});
		caller.sendMessage(F.main(getName(), "Password removed for " + _serverName));
	}

	public void createPassword(Player caller, String password)
	{
		_password = password;
		
		runAsync(new Runnable()
		{
			public void run()
			{
				_repository.createPassword(_password);
			}
		});
		
		caller.sendMessage(F.main(getName(), "Password created : " + _password));
	}
}
