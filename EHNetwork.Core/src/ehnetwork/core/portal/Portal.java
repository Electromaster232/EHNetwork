package ehnetwork.core.portal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTabTitle;
import ehnetwork.core.portal.Commands.SendCommand;
import ehnetwork.core.portal.Commands.ServerCommand;
import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.commands.ServerCommandManager;
import ehnetwork.serverdata.commands.ServerTransfer;
import ehnetwork.serverdata.commands.TransferCommand;
import ehnetwork.serverdata.data.MinecraftServer;
import ehnetwork.serverdata.servers.ServerManager;
import ehnetwork.serverdata.servers.ServerRepository;

public class Portal extends MiniPlugin
{
	// The singleton instance of Portal
	private static Portal instance;
	public static Portal getInstance() { return instance; }
	
	private ServerRepository _repository;
	private CoreClientManager _clientManager;
	private HashSet<String> _connectingPlayers = new HashSet<String>();
	
	private Region _region;
	private String _serverName;
	
	public Portal(JavaPlugin plugin, CoreClientManager clientManager, String serverName)
	{
		super("Portal", plugin);
		
		instance = this;
		_clientManager = clientManager;
		
		_region = plugin.getConfig().getBoolean("serverstatus.us") ? Region.US : Region.EU;
		_serverName = serverName;
		_repository = ServerManager.getServerRepository(_region);
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(getPlugin(), "BungeeCord");
		
		// Register the server command type for future use
		ServerCommandManager.getInstance().registerCommandType("TransferCommand", TransferCommand.class, new TransferHandler());
	}

	@EventHandler
	public void join(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		//Player List
		String serverName = _plugin.getConfig().getString("serverstatus.name");
		UtilTabTitle.setHeaderAndFooter(player, C.Bold + "Endless Hosting Network   " + C.cGreen + serverName, "Visit " + C.cGreen + "www.theendlessweb.com" + ChatColor.RESET + " for News, Forums and (not-a-)Shop");
	}
	
	public void sendAllPlayers(String serverName)
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			sendPlayerToServer(player, serverName);
		}
	}
	
	public void sendPlayerToServer(Player player, String serverName)
	{
		sendPlayerToServer(player, serverName, true);
	}
	
	public void sendPlayerToServer(final Player player, final String serverName, boolean callEvent)
	{
		if (_connectingPlayers.contains(player.getName()))
			return;
		
		if (callEvent)
		{
			ServerTransferEvent event = new ServerTransferEvent(player, serverName);
			Bukkit.getPluginManager().callEvent(event);
		}

		final boolean override = serverName.equalsIgnoreCase("Lobby");
		final Rank playerRank = _clientManager.Get(player).GetRank();
		
		if (override)
		{
			sendPlayer(player, serverName);
		}
		else
		{
			runAsync(new Runnable()
			{
				public void run()
				{
					final MinecraftServer server = _repository.getServerStatus(serverName);
					
					if (server == null)
						return;
					
					Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
					{
						public void run()
						{
							if (server.getPlayerCount() < server.getMaxPlayerCount() || playerRank.Has(Rank.ULTRA))
							{
								sendPlayer(player, serverName);
							}
						else
							UtilPlayer.message(
									player,
									F.main(getName(), C.cGold + serverName + C.cRed + " is full!"));
						}
					});
				}
			});
		}
	}

	public static void transferPlayer(String playerName, String serverName)
	{		
		ServerTransfer serverTransfer = new ServerTransfer(playerName, serverName);
		TransferCommand transferCommand = new TransferCommand(serverTransfer);
		transferCommand.publish();
	}
	
	public void doesServerExist(final String serverName, final Callback<Boolean> callback)
	{
		if (callback == null)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				final boolean serverExists = ServerManager.getServerRepository(_region).serverExists(serverName);
				
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						callback.run(serverExists);
					}
				});
			}
		});
	}

	public void addCommands()
	{
		addCommand(new ServerCommand(this));
		addCommand(new SendCommand(this));
	}

	public void sendToHub(Player player, String message)
	{
		if (message != null)
		{
			UtilPlayer.message(player, "  ");
			UtilPlayer.message(player, C.cGold + C.Bold + message);
			UtilPlayer.message(player, "  ");
		}

		player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 1f);
		sendPlayerToServer(player, "Lobby");
	}
	
	private void sendPlayer(final Player player, String serverName)
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		 
		try 
		{
		    out.writeUTF("Connect");
		    out.writeUTF(serverName);
		}
		catch (IOException e) 
		{
		    // Can never happen
		}
		finally
		{
			try
			{
				out.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		player.sendPluginMessage(getPlugin(), "BungeeCord", b.toByteArray());
		_connectingPlayers.add(player.getName());
		
		getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
		{
			public void run()
			{
				_connectingPlayers.remove(player.getName());
			}
		}, 20L);
		
		UtilPlayer.message(
				player,
				F.main(getName(), "You have been sent from " + C.cGold + _serverName + C.cGray + " to " + C.cGold + serverName));
	}
}
