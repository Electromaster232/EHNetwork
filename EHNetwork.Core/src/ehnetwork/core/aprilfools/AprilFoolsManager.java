package ehnetwork.core.aprilfools;

import java.util.Calendar;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.disguise.disguises.DisguiseCow;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class AprilFoolsManager extends MiniPlugin
{
	public static AprilFoolsManager Instance;
	
	private boolean _enabled;
	private DisguiseManager _disguiseManager;
	private CoreClientManager _clientManager;
	 
	protected AprilFoolsManager(JavaPlugin plugin, CoreClientManager clientManager, DisguiseManager disguiseManager) 
	{
		super("April Fools", plugin);
		
		_disguiseManager = disguiseManager;
		_clientManager = clientManager;
		
		Calendar c = Calendar.getInstance();
		_enabled = (c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1);
	}
	
	public static void Initialize(JavaPlugin plugin, CoreClientManager clientManager, DisguiseManager disguiseManager)
	{
		Instance = new AprilFoolsManager(plugin, clientManager, disguiseManager);
	}

	@EventHandler
	public void updateEnabled(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		Calendar c = Calendar.getInstance();
		_enabled = (c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void chatAdd(AsyncPlayerChatEvent event)
	{
		if (!_enabled)
			return;
		
		String[] words = event.getMessage().split(" ");
		
		String out = "";
		for (String word : words)
		{
			//Prefix
			if (Math.random() > 0.85)
			{
				out += "moo";
				
				for (int i=0 ; i<UtilMath.r(2) ; i++)
					out += "o";
				
				out += " " + word + " ";
				
			}
			
			//Suffix
			else if (Math.random() > 0.85)
			{
				out += word + " ";
				
				out += "moo";
				
				for (int i=0 ; i<UtilMath.r(2) ; i++)
					out += "o";
				
				out += " ";
			}
			
			//Swap
			else if (Math.random() > 0.99)
			{
				out += "moo";
				
				for (int i=3 ; i<word.length() ; i++)
					out += "o";
				
				out += " ";
			}
			else
			{
				out += word + " ";
			}
		}
		
		event.setMessage(out);
	}
	
	@EventHandler
	public void updateText(UpdateEvent event)
	{
		if (!_enabled)
			return;
		
		if (event.getType() != UpdateType.SLOW)
			return;

		if (Math.random() <= 0.99)
			return;
		
		UtilTextMiddle.display("Moo", null, 5, 20, 5);
	}
		
	@EventHandler
	public void updateCow(UpdateEvent event)
	{
		if (!_enabled)
			return;
		
		if (event.getType() != UpdateType.FAST)
			return;

		//Disguise
		for (Player player : UtilServer.getPlayers())
		{
			if (_disguiseManager.getDisguise(player) != null)
			{
				//Moo
				if (Math.random() > 0.8)
				{
					if (_disguiseManager.getDisguise(player) instanceof DisguiseCow)
					{
						player.getWorld().playSound(player.getLocation(), Sound.COW_IDLE, (float)Math.random() + 0.5f, (float)Math.random() + 0.5f);
					}
				}
				
				continue;
			}

			//Disguise
			DisguiseCow disguise = new DisguiseCow(player);
			disguise.setName(getName(player), _clientManager.Get(player).GetRank());
			disguise.setCustomNameVisible(true);
			_disguiseManager.disguise(disguise);
		}
	}

	public boolean isActive() 
	{
		return _enabled;
	}

	public String getName(Player player) 
	{
		//Name
		int index = 0;
		boolean hitVowel = false;
		for (int i=0 ; i<player.getName().length()-2 && i<5 ; i++)
		{
			//Detect vowel ;o
			if (player.getName().toLowerCase().charAt(i) == 'a' || 
				player.getName().toLowerCase().charAt(i) == 'e' || 
				player.getName().toLowerCase().charAt(i) == 'i' || 
				player.getName().toLowerCase().charAt(i) == 'o' || 
				player.getName().toLowerCase().charAt(i) == 'u')
			{
				hitVowel = true;
			}
			//Post vowel consonant - stop here
			else if (hitVowel)
			{
				break;
			}
			
			index = i+1;
		}

		String name = "Moo" + player.getName().substring(index, player.getName().length());
		
		if (name.length() > 16)
			name = name.substring(0, 16);
		
		return name;
	}

	public void setEnabled(boolean b) 
	{
		Calendar c = Calendar.getInstance();
		_enabled = b && (c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1);
	}
}
