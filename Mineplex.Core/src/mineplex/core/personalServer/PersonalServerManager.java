package mineplex.core.personalServer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.jsonchat.ClickEvent;
import mineplex.core.common.jsonchat.Color;
import mineplex.core.common.jsonchat.HoverEvent;
import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;
import mineplex.serverdata.Region;
import mineplex.serverdata.data.ServerGroup;
import mineplex.serverdata.servers.ServerManager;
import mineplex.serverdata.servers.ServerRepository;

public class PersonalServerManager extends MiniPlugin
{
	private ServerRepository _repository;
	private CoreClientManager _clientManager;
	
	private boolean _us;
	
	private int _interfaceSlot = 6;
	private ItemStack _interfaceItem;
	private boolean _giveInterfaceItem = true;

	public PersonalServerManager(JavaPlugin plugin, CoreClientManager clientManager)
	{
		super("Personal Server Manager", plugin);

		_clientManager = clientManager;
		
		setupConfigValues();
		
		_us = plugin.getConfig().getBoolean("serverstatus.us");
		
		Region region = _us ? Region.US : Region.EU;
		_repository = ServerManager.getServerRepository(region);
		
		_interfaceItem = ItemStackFactory.Instance.CreateStack(Material.SPECKLED_MELON, (byte)0, 1, C.cGreen + "/hostserver");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		if (_giveInterfaceItem)
		{
			event.getPlayer().getInventory().setItem(_interfaceSlot, _interfaceItem);
		}
	}

	@EventHandler
	public void openServer(PlayerInteractEvent event)
	{
		if (_interfaceItem.equals(event.getPlayer().getItemInHand()))
		{
			if (!Recharge.Instance.use(event.getPlayer(), "Host Server Melon", 30000, false, false))
				return;

			if (_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.LEGEND))
			{
				showHostMessage(event.getPlayer());
			}
			else
			{
				UtilPlayer.message(event.getPlayer(), F.main("Server", "Only players with " + F.rank(Rank.LEGEND) + C.mBody + "+ can host private servers"));
			}
		}
	}

	public void showHostMessage(Player player)
	{
		UtilPlayer.message(player, C.cRed + "------------------------------------------------");
		UtilPlayer.message(player, "This will create a Mineplex Player Server for you.");
		UtilPlayer.message(player, "Here you can play your favorite games with friends!");
		
		new JsonMessage("Please ").click(ClickEvent.RUN_COMMAND, "/hostserver")
				.hover(HoverEvent.SHOW_TEXT, C.cGray + "Click to Create Server")
				.extra("CLICK HERE").color(Color.GREEN).extra(" to confirm you want to do this.")
				.color(Color.WHITE).send(JsonMessage.MessageType.CHAT_BOX, player);
		
		UtilPlayer.message(player, C.cRed + "------------------------------------------------");
	}

	@Override
	public void addCommands()
	{
		addCommand(new HostServerCommand(this));
		addCommand(new HostEventServerCommand(this));
	}
	
	private void setupConfigValues()
	{
	    try 
	    {			
			getPlugin().getConfig().addDefault("serverstatus.us", true);
			getPlugin().getConfig().set("serverstatus.us", getPlugin().getConfig().getBoolean("serverstatus.us"));
			
			getPlugin().saveConfig();
	    } 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	public void hostServer(Player player, String serverName, boolean eventServer)
	{
		int ram = 1024;
		int cpu = 1;

		Rank rank = _clientManager.Get(player).GetRank();

		if (eventServer || rank.Has(Rank.SNR_MODERATOR) || rank == Rank.YOUTUBE || rank == Rank.TWITCH)
		{
			ram = 2048;
			cpu = 4;
		}

		if (eventServer)
			createGroup(player, "EVENT", ram, cpu, 40, 80, "Event", eventServer);
		else
			createGroup(player, serverName, ram, cpu, 40, 80, "Smash", eventServer);
	}
	
	private void createGroup(final Player host, final String serverName, final int ram, final int cpu, final int minPlayers, final int maxPlayers, final String games, final boolean event)
	{
		getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				for (ServerGroup existingServerGroup : _repository.getServerGroups(null))
				{
					if (existingServerGroup.getPrefix().equalsIgnoreCase(serverName) || existingServerGroup.getName().equalsIgnoreCase(serverName))
					{
						if (host.getName().equalsIgnoreCase(existingServerGroup.getHost()))
							host.sendMessage(F.main(getName(), "Your server is still being created or already exists.  If you haven't been connected in 20 seconds, type /server " + serverName + "-1."));
						else
							host.sendMessage(C.cRed + "Sorry, but you're not allowed to create a MPS server because you have chosen a name to glitch the system :)");
						
						return;
					}
				}
				
				final ServerGroup serverGroup = new ServerGroup(serverName, serverName, host.getName(), ram, cpu, 1, 0, UtilMath.random.nextInt(250) + 19999, true, "arcade.zip", "Arcade.jar", "plugins/Arcade/", minPlayers, maxPlayers, 
						true, false, false, games, "Player", true, event, false, true, false, true, true, false, false, false, false, true, true, true, false, false, "", _us ? Region.US : Region.EU);
				
				getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
				{
					public void run()
					{
						_repository.updateServerGroup(serverGroup);
						Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
						{
							public void run()
							{
								host.sendMessage(F.main(getName(), serverName + "-1 successfully created.  You will be sent to it shortly."));
								host.sendMessage(F.main(getName(), "If you haven't been connected in 20 seconds, type /server " + serverName + "-1."));
							}
						});
					}
				});
			}
		});
	}
}
