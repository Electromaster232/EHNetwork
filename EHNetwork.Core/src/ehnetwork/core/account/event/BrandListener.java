package ehnetwork.core.account.event;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;


public class BrandListener implements PluginMessageListener {
	public ArrayList playerClientList = new ArrayList();

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
		try
		{
			if(new String(msg, "UTF-8").substring(1).equals("ehnetwork")){
				playerClientList.add(p);
			}
		}
		catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}

	public void removePlayerOnQuit(Player player){
		if(playerClientList.contains(player)){
			playerClientList.remove(player);
		}
	}

	public ArrayList getPlayerClientList(){
		return playerClientList;
	}

	public boolean playerUsingClient(Player player){
		return (playerClientList.contains(player));
	}
}

