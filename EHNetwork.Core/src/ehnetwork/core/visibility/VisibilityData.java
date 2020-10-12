package ehnetwork.core.visibility;

import java.util.Iterator;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.timing.TimingManager;

public class VisibilityData 
{
	private NautHashMap<Player, Boolean> _shouldHide = new NautHashMap<Player, Boolean>();
	private NautHashMap<Player, Boolean> _lastState = new NautHashMap<Player, Boolean>();
	
	public void updatePlayer(Player player, Player target, boolean hide) 
	{
		TimingManager.stopTotal("VisData updatePlayer");
		
		if (_lastState.containsKey(target) && _lastState.get(target) == hide)
		{
			//Already this state, do nothing
			TimingManager.stopTotal("VisData updatePlayer");
			return;
		}
		
		if (attemptToProcess(player, target, hide))
		{
			//Clear old
			_shouldHide.remove(target);
		}
		else
		{
			//Store
			_shouldHide.put(target, hide);
		}
		
		TimingManager.stopTotal("VisData updatePlayer");
	}
	
	//Process New
	private boolean attemptToProcess(Player player, Player target, boolean hide)
	{
		TimingManager.startTotal("VisData attemptToProcess");
		
		if (Recharge.Instance.use(player, "VIS " + target.getName(), 250, false, false))
		{
			//Use craftplayer because i recall jon added something where
			//it would still send the packet, even if the client thought it was already the state.
			
			if (hide)
			{
				TimingManager.startTotal("Hide Player");
				((CraftPlayer)player).hidePlayer(target, true, true);	
				TimingManager.stopTotal("Hide Player");
			}
			else
			{
				TimingManager.startTotal("Show Player");
				player.showPlayer(target);
				TimingManager.stopTotal("Show Player");
			}
			
			_lastState.put(target, hide);
			
			TimingManager.stopTotal("VisData attemptToProcess");
			return true;
		}

		TimingManager.stopTotal("VisData attemptToProcess");
		return false;
	}
	
	//Process Update
	public void attemptToProcessUpdate(Player player)
	{
		TimingManager.startTotal("VisData attemptToProcessUpdate shouldHide");
		if (!_shouldHide.isEmpty())
		{
			for (Iterator<Player> targetIter = _shouldHide.keySet().iterator(); targetIter.hasNext();)
			{
				Player target = targetIter.next();
				boolean hide = _shouldHide.get(target);
				
				if (!target.isOnline() || !target.isValid() || attemptToProcess(player, target, hide))
				{
					targetIter.remove();
				}
			}
		}
		TimingManager.stopTotal("VisData attemptToProcessUpdate shouldHide");
		
		
		TimingManager.startTotal("VisData attemptToProcessUpdate lastState");
		if (!_lastState.isEmpty())
		{
			for (Iterator<Player> targetIter = _lastState.keySet().iterator(); targetIter.hasNext();)
			{
				Player target = targetIter.next();
				
				if (!target.isOnline() || !target.isValid())
				{
					targetIter.remove();
				}
			}
		}
		TimingManager.stopTotal("VisData attemptToProcessUpdate lastState");
	}
}
