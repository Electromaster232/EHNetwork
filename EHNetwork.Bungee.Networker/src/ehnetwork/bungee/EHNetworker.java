package ehnetwork.bungee;

import java.io.File;

import ehnetwork.bungee.motd.MotdManager;
import ehnetwork.bungee.playerCount.PlayerCount;
import ehnetwork.bungee.playerTracker.PlayerTracker;
import ehnetwork.bungee.lobbyBalancer.LobbyBalancer;
import ehnetwork.bungee.playerStats.PlayerStats;
import net.md_5.bungee.api.plugin.Plugin;

public class EHNetworker extends Plugin
{	
	@Override
	public void onEnable()
	{		
		new MotdManager(this);
		if(!new File("IgnoreBalance.dat").exists()){
			new LobbyBalancer(this);
		}
		PlayerCount playerCount = new PlayerCount(this);
		new FileUpdater(this);
		new PlayerStats(this);
		//new InternetStatus(this);
		new PlayerTracker(this);
	}
}
