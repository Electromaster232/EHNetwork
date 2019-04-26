package mineplex.core.visibility;

import java.util.Iterator;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilServer;
import mineplex.core.timing.TimingManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VisibilityManager extends MiniPlugin
{
	public static VisibilityManager Instance;

	private NautHashMap<Player, VisibilityData> _data = new NautHashMap<Player, VisibilityData>();
	
	protected VisibilityManager(JavaPlugin plugin)
	{
		super("Visibility Manager", plugin);
	}

	public static void Initialize(JavaPlugin plugin)
	{
		Instance = new VisibilityManager(plugin);
	}
	
	public VisibilityData getDataFor(Player player)
	{
		if (!_data.containsKey(player))
			_data.put(player, new VisibilityData());
		
		return _data.get(player);
	}
	
	public void setVisibility(Player target, boolean isVisible, Player... viewers)
	{
		TimingManager.startTotal("VisMan SetVis");
		
		for (Player player : viewers)
		{
			if (player.equals(target))
				continue;
			
			getDataFor(player).updatePlayer(player, target, !isVisible);
		}
		
		TimingManager.stopTotal("VisMan SetVis");
	}
	
	public void refreshPlayerToAll(Player player) 
	{
		setVisibility(player, false, UtilServer.getPlayers());
		setVisibility(player, true, UtilServer.getPlayers());
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		TimingManager.startTotal("VisMan Update");
		
		Iterator<Player> playerIter = _data.keySet().iterator();
		
		while (playerIter.hasNext())
		{
			Player player = playerIter.next();
			
			if (!player.isOnline() || !player.isValid())
			{
				playerIter.remove();
				continue;
			}
			
			_data.get(player).attemptToProcessUpdate(player);
		}
		
		TimingManager.stopTotal("VisMan Update");
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		_data.remove(event.getPlayer());
	}

	//@EventHandler  DISABLED
	public void updateDebug(UpdateEvent event)
	{
		if (event.getType() != UpdateType.MIN_01)
			return;
		
		System.out.println("          ");
		TimingManager.endTotal("VisMan update", true);
		TimingManager.endTotal("VisMan setVis", true);
		TimingManager.endTotal("VisData attemptToProcess", true);
		TimingManager.endTotal("VisData updatePlayer", true);
		TimingManager.endTotal("VisData attemptToProcessUpdate shouldHide", true);
		TimingManager.endTotal("VisData attemptToProcessUpdate lastState", true);
		TimingManager.endTotal("Hide Player", true);
		TimingManager.endTotal("Show Player", true);
		System.out.println("          ");
	}
}
