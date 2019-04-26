package mineplex.hub.modules;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import mineplex.core.MiniPlugin;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextTop;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.core.gadget.gadgets.MorphWither;
import mineplex.core.gadget.types.Gadget;
import mineplex.core.gadget.types.GadgetType;
import mineplex.core.mount.Mount;
import mineplex.core.mount.types.MountDragon;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.HubManager;
import mineplex.hub.HubRepository;

public class NewsManager extends MiniPlugin
{
	public HubManager Manager;
	
	private String[] _news;
	private int _newsIndex = 0;
	private long _newsTime = System.currentTimeMillis();
	
	private int _mineplexIndex = 0;

	private HubRepository _repository = new HubRepository();
	
	public NewsManager(HubManager manager)
	{
		super("News Manager", manager.getPlugin());
	
		Manager = manager;
		
		_repository.initialize(manager.getPlugin().getConfig().getBoolean("serverstatus.us"));
		
		_news = new String[] 
				{
				"News Line 1",
				"News Line 2",
				"News Line 3",
				"News Line 4",
				};

		RefreshNews();
	}
	
	private void RefreshNews()
	{		
		RetriveNewsEntries(new Callback<HashMap<String, String>>()
		{
			public void run(final HashMap<String, String> newsEntries)
			{
				// Order newsEntries set or its output by newsPosition, not hash order...
				RetrieveMaxNewsPosition(new Callback<Integer>()
				{
					public void run(Integer maxPosition)
					{
						String[] newsStrings = new String[maxPosition];
						for (Iterator<String> iterator = newsEntries.keySet().iterator(); iterator.hasNext();)
						{
							int newsPos = Integer.parseInt(iterator.next());
							
							ChatColor col = ChatColor.RED;
							if (newsPos == 1)	col = ChatColor.GOLD;
							else if (newsPos == 2)	col = ChatColor.YELLOW;
							else if (newsPos == 3)	col = ChatColor.GREEN;
							else if (newsPos == 4)	col = ChatColor.AQUA;
							else if (newsPos == 5)	col = ChatColor.LIGHT_PURPLE;
							
							newsStrings[newsPos - 1] = col + C.Bold + "EHPlex" + ChatColor.RESET + " - " + newsEntries.get(newsPos + "");
							
							if (newsStrings[newsPos - 1].length() > 64)
								newsStrings[newsPos - 1] = newsStrings[newsPos - 1].substring(0, 64);
						}
					
						_news = newsStrings;
					}
				});
			}
		});
	}

	public void RetriveNewsEntries(final Callback<HashMap<String, String>> callback)
	{
		if (callback == null)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				final HashMap<String, String> newsEntries = _repository.retrieveNewsEntries();
				
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						callback.run(newsEntries);
					}
				});
			}
		});
	}
	
	public void SetNewsEntry(final String newsEntry, final int newsPosition, final Callback<Boolean> callback)
	{
		if (callback == null)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				final Boolean success = _repository.setNewsEntry(newsEntry, newsPosition);
				
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						callback.run(success);
					}
				});
			}
		});
	}
	
	public void AddNewsEntry(final String newsEntry, final Callback<Boolean> callback)
	{
		if (callback == null)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				final Boolean success = _repository.addNewsEntry(newsEntry);
				
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						callback.run(success);
					}
				});
			}
		});
	}
	
	public void DeleteNewsEntry(final int newsPosition, final Callback<Boolean> callback)
	{
		if (callback == null)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				final Boolean success = _repository.deleteNewsEntry(newsPosition);
				
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						callback.run(success);
					}
				});
			}
		});
	}

	public void RetrieveMaxNewsPosition(final Callback<Integer> callback)
	{
		if (callback == null)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				final Integer position = _repository.retrieveMaxNewsPosition();
				
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						callback.run(position);
					}
				});
			}
		});
	}
	
	public void Help(Player caller, String message)
	{		
		UtilPlayer.message(caller, F.main(_moduleName, "Available news arguments for this command:"));
		UtilPlayer.message(caller, F.help(C.cGold + "/news list", "Lists (numbered) stored news messages from database.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help(C.cGold + "/news add <newsEntry>", "Adds specified news entry string to database at end of table.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help(C.cGold + "/news delete #", "Removes specified (numbered) news entry string from database.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help(C.cGold + "/news set # <newsEntry>", "Updates specified (numbered) news entry string in database.", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("*Please Note:", "Updates to server news entries from the database are on a 4 minute refresh cycle!", Rank.ADMIN));
		
		if (message != null)
			UtilPlayer.message(caller, F.main(_moduleName, ChatColor.RED + message));
	}
	
	public void Help(Player caller)
	{
		Help(caller, null);
	}
	
	@EventHandler
	public void NewsUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.MIN_04)
			return;
		
		RefreshNews();
	}
	
	@EventHandler
	public void DragonBarUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		_mineplexIndex = (_mineplexIndex + 1)%6;
		
		//News Change
		if (UtilTime.elapsed(_newsTime, 4500))
		{
			_newsIndex = (_newsIndex + 1)%_news.length;
			_newsTime = System.currentTimeMillis();
		}
		if (_newsIndex >= _news.length)
		{
			// Resets newsIndex if outside of bounds of news array after RefreshNews but before UtilTime.elapsed above
			_newsIndex = 0;
		}

		double healthPercent = (double)_newsIndex/(double)(_news.length-1);
		String text = _news[_newsIndex];
		
		UtilTextTop.display(text, UtilServer.getPlayers());
		for (Creature pet : Manager.getPetManager().getPets())
		{
		    if (pet instanceof Wither)
		    {
		        pet.setCustomName(text);
		    }
		}
		
		for (Mount mount : Manager.GetMount().getMounts())
		{
			if (mount instanceof MountDragon)
			{
				((MountDragon)mount).SetName(text);
				//((MountDragon)mount).setHealthPercent(healthPercent);
			}
		}
		
		for (Gadget gadget : Manager.GetGadget().getGadgets(GadgetType.Morph))
		{
			if (gadget instanceof MorphWither)
			{
				((MorphWither)gadget).setWitherData(text, healthPercent);
			}
		}
	}
	
	@EventHandler
	public void joinNewsOverlay(final PlayerJoinEvent event)
	{
		for (int i=0 ; i<_news.length ; i++)
		{
			final int count = i;
			
			UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					UtilTextMiddle.display(C.cGold + C.Bold + "MINEPLEX" + ChatColor.RESET, _news[_news.length - 1 - count] + ChatColor.RESET, (count == 0) ? 20 : 0, 60, 20, event.getPlayer());
				}
			}, 60 * i + (i != 0 ? 20 : 0));		
		}
	}
}
