package ehnetwork.game.microgames.managers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.achievement.AchievementData;
import ehnetwork.core.achievement.AchievementLog;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.microgames.ArcadeFormat;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.Game.GameState;

public class GameAchievementManager implements Listener
{
	MicroGamesManager Manager;

	public GameAchievementManager(MicroGamesManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}	

	//Ensure that past achievement progress is ignored
	@EventHandler
	public void clearAchievementLog(PlayerJoinEvent event)
	{
		Manager.GetAchievement().clearLog(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void processAchievementLog(final GameStateChangeEvent event)
	{
		if (!Manager.IsRewardAchievements())
			return;

		if (event.GetState() != GameState.Dead)
			return;

		UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				for (Player player : UtilServer.getPlayers())
				{
					displayAchievementLog(player, event.GetGame(), Manager.GetAchievement().getLog(player));
				}
			}
		}, 120);
		//Delay after Gems
	}

	public void displayAchievementLog(final Player player, Game game, NautHashMap<Achievement, AchievementLog> log)
	{
		if (!Manager.IsRewardAchievements())
			return;
		
		if (log == null)
			return;
		
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

		UtilPlayer.message(player, "");
		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, "§f§lAchievement Progress");
		
		int out = 0;

		//Display
		for (final Achievement type : log.keySet())
		{
			
			
			AchievementData data = Manager.GetAchievement().get(player, type);
			
			String nameLevel = F.elem(C.cGold + C.Bold + type.getName());
			if (type.getMaxLevel() > 1) 
				nameLevel = F.elem(C.cGold + C.Bold + type.getName() + " " + ChatColor.RESET + C.cYellow + data.getLevel() + C.cGold +  "/" + C.cYellow + type.getMaxLevel());
			
			String progress = F.elem(C.cGreen + "+" + log.get(type).Amount);
			
			boolean displayDesc = true;
			 
			//Completed Achievement
			if (data.getLevel() >= type.getMaxLevel())
			{
				//Finishing for the first time 
				if (!Manager.GetTaskManager().hasCompletedTask(player, type.getName()))		
				{
					UtilPlayer.message(player, "");
					UtilPlayer.message(player, nameLevel + "   " + F.elem(C.cAqua + C.Bold + "Completed!") +
							"   " + F.elem(C.cGreen + C.Bold + "+" + type.getGemReward() + " Gems"));
					
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
					
					Manager.GetTaskManager().completedTask(new Callback<Boolean>()  
					{
						public void run(Boolean completed)
						{
							Manager.GetDonation().RewardGems(new Callback<Boolean>()  
							{
								public void run(Boolean completed)
								{
									
								}
							}, type.getName(), player.getName(), player.getUniqueId(), type.getGemReward());		
						}
					}, player, type.getName());
				}
				else
				{
					//Display nothing because already complete bro :O
					displayDesc = false;
				}
			}
			//Multi-Level Achievements
			else if (log.get(type).LevelUp)
			{
				UtilPlayer.message(player, "");
				UtilPlayer.message(player, nameLevel + "   " + progress +
						"   " + F.elem(C.cAqua + C.Bold + "LEVEL UP!"));
				
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
			}
			else
			{
				//Single Level 
				if (type.getMaxLevel() == 1)
				{
					UtilPlayer.message(player, "");
					UtilPlayer.message(player, nameLevel + "   " + progress +
							"   " + F.elem(ChatColor.YELLOW + "" + (data.getExpNextLevel() - data.getExpRemainder()) + " to Complete"));
				}
				else
				{
					//Multi Level - Almost Finished
					if (data.getLevel() == type.getMaxLevel() - 1)
					{
						UtilPlayer.message(player, "");
						UtilPlayer.message(player, nameLevel + "   " + progress +
								"   " + F.elem(ChatColor.YELLOW + "" + (data.getExpNextLevel() - data.getExpRemainder()) + " to Complete"));
					}
					//Multi Level - Many levels to go
					else
					{
						UtilPlayer.message(player, "");
						UtilPlayer.message(player, nameLevel + "   " + progress +
								"   " + F.elem(ChatColor.YELLOW + "" + (data.getExpNextLevel() - data.getExpRemainder()) + " to Next Level"));
					}
				}
			}
			
			if (displayDesc)
				for (String desc : type.getDesc())
				{ 
					UtilPlayer.message(player, desc);
					out++;
				}
				
			
			out++;
		}
		
		while (out < 5)
		{
			//UtilPlayer.message(player, "");
			out++;
		}
			
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, ArcadeFormat.Line);	
	}
}
