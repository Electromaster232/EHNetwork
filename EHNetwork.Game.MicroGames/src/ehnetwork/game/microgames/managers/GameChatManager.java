package ehnetwork.game.microgames.managers;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.party.Party;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam;

public class GameChatManager implements Listener
{
	MicroGamesManager Manager;

	public GameChatManager(MicroGamesManager manager)
	{
		Manager = manager; 

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}  

	@EventHandler
	public void MeCancel(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().startsWith("/me "))
		{
			event.getPlayer().sendMessage(F.main("Mirror", "You can't see /me messages, are you a vampire?"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void HandleChat(AsyncPlayerChatEvent event) 
	{
		if (event.isCancelled())
			return;

		Player sender = event.getPlayer();
		String senderName = sender.getName();
		
		//Dead Prefix 
		String dead = "";
		if (Manager.GetGame() != null)
			if (Manager.GetGame().GetTeam(sender) != null)
				if (!Manager.GetGame().IsAlive(sender))
					dead = C.cGray + "Dead ";

		Rank rank = Manager.GetClients().Get(sender).GetRank();
		boolean ownsUltra = false;

		if (Manager.GetGame() != null)
			ownsUltra= Manager.GetDonation().Get(sender.getName()).OwnsUnknownPackage(Manager.GetServerConfig().ServerType + " ULTRA");

		//Level
		String levelStr = "";
		if (!Manager.GetGameHostManager().isPrivateServer())
			levelStr = Manager.GetAchievement().getMineplexLevel(sender, rank);
				
		String rankStr = "";
		//Rank Prefix  & MPS Host Prefix
		if (Manager.GetGameHostManager().isHost(event.getPlayer()))
		{
			if (Manager.GetGameHostManager().isEventServer())
				rankStr = C.cDGreen + C.Bold + "Event Host ";
			else
				rankStr = C.cDGreen + C.Bold + "MPS Host ";
		}
		else if (Manager.GetGameHostManager().isAdmin(event.getPlayer(), false))
		{
			if (Manager.GetGameHostManager().isEventServer())
				rankStr = C.cDGreen + C.Bold + "Event Admin ";
			else
				rankStr = C.cDGreen + C.Bold +  "MPS Admin ";
		}
		else
		{
			if  (rank != Rank.ALL)
				rankStr = rank.GetTag(true, true) + " ";

			if (ownsUltra && !rank.Has(Rank.ULTRA))
				rankStr = Rank.ULTRA.GetTag(true, true) + " ";
		}
		
		if (event.getMessage().charAt(0) == '@')
		{
			//Party Chat
			Party party = Manager.getPartyManager().GetParty(sender);

			if (party != null)
			{
				event.getRecipients().clear();

				event.setMessage(event.getMessage().substring(1, event.getMessage().length()));
				event.setFormat(levelStr + C.cDPurple + C.Bold + "Party " + C.cWhite + C.Bold + senderName + " " + C.cPurple + "%2$s");

				event.getRecipients().addAll(party.GetPlayersOnline());

				return;
			}
		}
		
		//Base Format
		event.setFormat(dead + levelStr + rankStr + Manager.GetColor(sender) + senderName + " " + ChatColor.WHITE + "%2$s");

		//Public/Private (Not If Player Dead)
		if (Manager.GetGame() != null && Manager.GetGame().GetState() == Game.GameState.Live)
		{
			boolean globalMessage = false;

			//Team
			GameTeam team = Manager.GetGame().GetTeam(sender);

			if (team != null) 
			{
				//Team Chat
				if (event.getMessage().charAt(0) == '@')
				{
					event.setMessage(event.getMessage().substring(1, event.getMessage().length()));
					event.setFormat(C.cWhite + C.Bold + "Team" + " " + dead + levelStr + rankStr + team.GetColor() + senderName + " " + C.cWhite + "%2$s");
				}
				//All Chat
				else
				{
					globalMessage = true;
					event.setFormat(dead + levelStr + rankStr + team.GetColor() + senderName + " " + C.cWhite + "%2$s");
				}
			}

			if (globalMessage)
				return;

			//Team Message Remove Recipient
			Iterator<Player> recipientIterator = event.getRecipients().iterator();

			while (recipientIterator.hasNext())
			{
				Player receiver = recipientIterator.next();

				if (!Manager.GetServerConfig().Tournament && Manager.GetClients().Get(receiver).GetRank().Has(Rank.MODERATOR))
					continue;
				
				GameTeam recTeam = Manager.GetGame().GetTeam(receiver);
				GameTeam sendTeam = Manager.GetGame().GetTeam(sender);
				
				if (recTeam == null || sendTeam == null)
				{
					continue;
				}

				if (!recTeam.equals(sendTeam))
					recipientIterator.remove();
			}
		}
	}
}
