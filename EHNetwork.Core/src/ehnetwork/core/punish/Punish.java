package ehnetwork.core.punish;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.account.event.ClientWebResponseEvent;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.punish.Command.PunishCommand;
import ehnetwork.core.punish.Tokens.PunishClientToken;
import ehnetwork.core.punish.Tokens.PunishmentToken;
import ehnetwork.serverdata.commands.ServerCommandManager;

public class Punish extends MiniPlugin
{
	private HashMap<String, PunishClient> _punishClients;
	private PunishRepository _repository;
	private CoreClientManager _clientManager;
	
	public Punish(JavaPlugin plugin, String webServerAddress, CoreClientManager clientManager) 
	{
		super("Punish", plugin);

        _punishClients = new HashMap<String, PunishClient>();
        _clientManager = clientManager;
        _repository = new PunishRepository(webServerAddress);
        
        ServerCommandManager.getInstance().registerCommandType("PunishCommand", ehnetwork.serverdata.commands.PunishCommand.class, new PunishmentHandler(this));
	}
	
	public PunishRepository GetRepository()
	{
		return _repository;
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new PunishCommand(this));
	}
	
	@EventHandler
	public void OnClientWebResponse(ClientWebResponseEvent event)
	{
		PunishClientToken token = new Gson().fromJson(event.GetResponse(), PunishClientToken.class);
		LoadClient(token);
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		_punishClients.remove(event.getPlayer().getName().toLowerCase());
	}
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerLogin(AsyncPlayerPreLoginEvent event)
    {
    	if (_punishClients.containsKey(event.getName().toLowerCase()))
		{
    		PunishClient client = GetClient(event.getName());
    		
    		if (client.IsBanned())
    		{
    			Punishment punishment = client.GetPunishment(PunishmentSentence.Ban);
    			String time = UtilTime.convertString(punishment.GetRemaining(), 0, UtilTime.TimeUnit.FIT);
    			
    			if (punishment.GetHours() == -1)
    				time = "Permanent";
    			
                String reason = C.cRed + C.Bold + "You are banned for " + time +
                		"\n" + C.cWhite + punishment.GetReason() +
                		"\n" + C.cDGreen + "Unfairly banned? Appeal at " + C.cGreen + "support.theendlessweb.com"
                		;
                
                event.disallow(Result.KICK_BANNED, reason);
    		}
		}
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void PunishChatEvent(AsyncPlayerChatEvent event)
    {
        PunishClient client = GetClient(event.getPlayer().getName());
        
        if (client != null && client.IsMuted())
        {
        	event.getPlayer().sendMessage(F.main(getName(), "Shh, you're muted because " + client.GetPunishment(PunishmentSentence.Mute).GetReason() + " by " + client.GetPunishment(PunishmentSentence.Mute).GetAdmin() + " for " + C.cGreen + UtilTime.convertString(client.GetPunishment(PunishmentSentence.Mute).GetRemaining(), 1, UtilTime.TimeUnit.FIT) + "."));
        	event.setCancelled(true);
        }
    }

	public void Help(Player caller)
	{
		UtilPlayer.message(caller, F.main(_moduleName, "Commands List:"));
		UtilPlayer.message(caller, F.help("/punish", "<player> <reason>", Rank.MODERATOR));
	}
	
	public void AddPunishment(final String playerName, final Category category, final String reason, final Player caller, final int severity, boolean ban, long duration)
	{
		if (!_punishClients.containsKey(playerName.toLowerCase()))
		{
			_punishClients.put(playerName.toLowerCase(), new PunishClient());
		}
		
		final PunishmentSentence sentence = !ban ? PunishmentSentence.Mute : PunishmentSentence.Ban;
		
		final long finalDuration = duration;
		
		_repository.Punish(new Callback<String>()
		{
			public void run(String result)
			{
				PunishmentResponse banResult = PunishmentResponse.valueOf(result);
				
				if (banResult == PunishmentResponse.AccountDoesNotExist)
				{
					if (caller != null)
						caller.sendMessage(F.main(getName(), "Account with name " + F.elem(playerName) + " does not exist."));
					else
						System.out.println(F.main(getName(), "Account with name " + F.elem(playerName) + " does not exist."));
				}
				else if (banResult == PunishmentResponse.InsufficientPrivileges)
				{
					if (caller != null)
						caller.sendMessage(F.main(getName(), "You have insufficient rights to punish " + F.elem(playerName) + "."));
					else
						System.out.println(F.main(getName(), "You have insufficient rights to punish " + F.elem(playerName) + "."));
				}
				else if (banResult == PunishmentResponse.Punished)
				{
					final String durationString = UtilTime.convertString(finalDuration < 0 ? -1 : (long)(finalDuration * 3600000), 1, UtilTime.TimeUnit.FIT);
					
					if (sentence == PunishmentSentence.Ban)
					{
						if (caller == null)
							System.out.println(F.main(getName(), F.elem(caller == null ? "Mineplex Anti-Cheat" : caller.getName()) + " banned " + F.elem(playerName) + " because of " + F.elem(reason) + " for " + durationString + "."));
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
						{
							public void run()
							{
								String kickReason = C.cRed + C.Bold + "You were banned for " + durationString + " by " + (caller == null ? "Mineplex Anti-Cheat" : caller.getName()) +
					                		"\n" + C.cWhite + reason +
					                		"\n" + C.cDGreen + "Unfairly banned? Appeal at " + C.cGreen + "support.theendlessweb.com"
					                		;
								
								Player target = UtilPlayer.searchOnline(null, playerName, false);
								if (target != null)
									target.kickPlayer(kickReason);
								else
									new ehnetwork.serverdata.commands.PunishCommand(playerName, true, false, kickReason).publish();
							}
						});
						
						informOfPunish(playerName, F.main(getName(), caller == null ? "Mineplex Anti-Cheat" : caller.getName() + " banned " + playerName + " for " + durationString + "."));
					}
					else
					{
						if (caller == null)
							System.out.println(F.main(getName(), F.elem(caller == null ? "Mineplex Anti-Cheat" : caller.getName()) + " muted " + F.elem(playerName) + " because of " + F.elem(reason) + " for " +
									durationString + "."));
						
						//Warning
						if (finalDuration == 0)
							informOfPunish(playerName, F.main(getName(), caller == null ? "Mineplex Anti-Cheat" : caller.getName() + " issued a friendly warning to " + playerName + "."));
						else
							informOfPunish(playerName, F.main(getName(), caller == null ? "Mineplex Anti-Cheat" : caller.getName() + " muted " + playerName + " for " + durationString + "."));
						
						//Inform
						Player target = UtilPlayer.searchExact(playerName);
						if (target != null)
						{
							UtilPlayer.message(target, F.main("Punish", F.elem(C.cGray + C.Bold + "Reason: ") + reason));
							target.playSound(target.getLocation(), Sound.CAT_MEOW, 1f, 1f);
						}
						else
							new ehnetwork.serverdata.commands.PunishCommand(playerName, false, finalDuration != 0, F.main("Punish", F.elem(C.cGray + C.Bold + (finalDuration != 0 ? "Mute" : "Warning") + " Reason: ") + reason)).publish();
						
						_repository.LoadPunishClient(playerName, new Callback<PunishClientToken>()
						{
							public void run(PunishClientToken token)
							{
								LoadClient(token);
							}
						});
					}
				}
			}

			
		}, playerName, category.toString(), sentence, reason, duration, caller == null ? "Mineplex Anti-Cheat" : caller.getName(), severity);
	}
	
	private void informOfPunish(String punishee, String msg)
	{
		for (Player player : UtilServer.getPlayers())
		{
			if (_clientManager.Get(player).GetRank().Has(Rank.HELPER) || player.getName().equals(punishee))
			{
				player.sendMessage(msg);
			}
		}
	}
	
	public void LoadClient(PunishClientToken token)
	{
		PunishClient client = new PunishClient();
		
		long timeDifference = token.Time;
		
		for (PunishmentToken punishment : token.Punishments)
		{
			client.AddPunishment(Category.valueOf(punishment.Category), new Punishment(punishment.PunishmentId, PunishmentSentence.valueOf(punishment.Sentence), Category.valueOf(punishment.Category), punishment.Reason, punishment.Admin, punishment.Duration, punishment.Severity, timeDifference, punishment.Active, punishment.Removed, punishment.RemoveAdmin, punishment.RemoveReason));
		}
		
		_punishClients.put(token.Name.toLowerCase(), client);
	}
	
	public PunishClient GetClient(String name)
	{
		synchronized (this)
		{
			return _punishClients.get(name.toLowerCase());
		}
	}

	public void RemovePunishment(int punishmentId, String target, final Player admin, String reason, Callback<String> callback)
	{
		_repository.RemovePunishment(callback, punishmentId, target, reason, admin.getName());
	}

	public void RemoveBan(String name, String reason)
	{
		_repository.RemoveBan(name, reason);
	}
	
	public CoreClientManager GetClients()
	{
		return _clientManager;
	}
	
	public int factorial(int n)
	{
		if (n == 0)
			return 1;
		
		return n * (factorial(n-1)); 
	}
}
