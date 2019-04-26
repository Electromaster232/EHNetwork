package mineplex.hub.modules;

import org.bukkit.event.EventHandler;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.scoreboard.ScoreboardData;
import mineplex.core.scoreboard.ScoreboardManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.HubManager;

public class HubScoreboardManager extends MiniPlugin
{
	private ScoreboardManager _scoreboardManager;
	private String _serverName;
	
	public HubScoreboardManager(HubManager manager, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("Hub Scoreboard Manager", manager.getPlugin());
		
		_scoreboardManager = new ScoreboardManager(manager.getPlugin(), clientManager, donationManager);
		
		init();
	}
	
	private String getServerName()
	{
		if (_serverName == null)
			_serverName = getPlugin().getConfig().getString("serverstatus.name");
		
		return _serverName;
	}

	private void init()
	{
		ScoreboardData data = _scoreboardManager.getData("default", true);
		
		data.writeEmpty();
		
		//Server
		data.write(C.cAqua + C.Bold + "Server");
		data.write(getServerName());
		data.writeEmpty();
		
		//Gems
		data.write(C.cGreen + C.Bold + "Gems");
		data.writePlayerGems();
		data.writeEmpty();
		
		//Coins
		data.write(C.cYellow + C.Bold + "Coins");
		data.writePlayerCoins();
		data.writeEmpty();
		
		//Rank
		data.write(C.cGold + C.Bold + "Rank");
		data.writePlayerRank();
		data.writeEmpty();
		
		//Website
		data.write(C.cRed + C.Bold + "Website");
		data.write("theendlessweb.com");
	}
	
	@EventHandler
	public void drawUpdate(UpdateEvent event)
	{
		if (event.getType() == UpdateType.FAST)
			_scoreboardManager.draw();
	}
}
