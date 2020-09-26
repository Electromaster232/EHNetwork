package ehnetwork.bungee.motd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.data.DataRepository;
import ehnetwork.serverdata.redis.RedisDataRepository;
import ehnetwork.serverdata.servers.ServerManager;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class MotdManager implements Listener, Runnable
{
	private Plugin _plugin;
	
	private DataRepository<GlobalMotd> _repository;

	private Random _random = new Random();
	private String _firstLine = "                §b§l§m   §8§l§m[ §r §9§lMineplex§r §f§lGames§r §8§l§m ]§b§l§m   §r";
	private List<String> _motdLines;
	
	public MotdManager(Plugin plugin)
	{
		_plugin = plugin;
		
		_plugin.getProxy().getScheduler().schedule(_plugin, this, 5L, 30L, TimeUnit.SECONDS);
		_plugin.getProxy().getPluginManager().registerListener(_plugin, this);
		
		_repository = new RedisDataRepository<GlobalMotd>(ServerManager.getConnection(true, ServerManager.SERVER_STATUS_LABEL), ServerManager.getConnection(false, ServerManager.SERVER_STATUS_LABEL),
				Region.ALL, GlobalMotd.class, "globalMotd");
		run();
		
		if (new File("updateMOTD.dat").exists())
		{
			List<String> lines = new ArrayList<String>();
			lines.add("             §b§l◄§f§lNEW§b§l►     §f§l◄§b§lSKYWARS§f§l►     §b§l◄§f§lNEW§b§l►");
			//lines.add("                     §d§lRank Sale §a§l40% Off");
			//lines.add("                        §f§l◄§c§lMAINTENANCE§f§l►");
			
			updateMainMotd("                §b§l§m   §8§l§m[ §r §9§lMineplex§r §f§lGames§r §8§l§m ]§b§l§m   §r", lines);
			System.out.println("Updated Bungee MOTD");
		}
	}
 
	@EventHandler
	public void serverPing(ProxyPingEvent event)
	{
		net.md_5.bungee.api.ServerPing serverPing = event.getResponse();

		String motd = _firstLine;
		if (_motdLines != null && _motdLines.size() > 0)
		{
			motd += "\n" + _motdLines.get(_random.nextInt(_motdLines.size()));
		}
		
		event.setResponse(new net.md_5.bungee.api.ServerPing(serverPing.getVersion(), serverPing.getPlayers(), motd, serverPing.getFaviconObject()));
	}
	
	@Override
	public void run()
	{
		GlobalMotd motd = _repository.getElement("MainMotd");
		
		if (motd != null)
		{
			_motdLines = motd.getMotd();
			_firstLine = motd.getHeadline();
		}
	}
	
	/**
	 * Update the main {@link GlobalMotd} determining the MOTD for Bungee instances.
	 * @param motdLines - the lines to update the MOTD to.
	 */
	public void updateMainMotd(String headline, List<String> motdLines)
	{
		_repository.addElement(new GlobalMotd("MainMotd", headline, motdLines));
	}

	public List<String> getMotdLines()
	{
		return _motdLines;
	}
}
