package ehnetwork.game.skyclans.clans;

import java.util.ArrayList;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilInput;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.game.skyclans.clans.repository.ClanTerritory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClansAdmin 
{
	private ClansManager Clans;
	
	public ClansAdmin(ClansManager clans)
	{
		Clans = clans;
	}

	public void command(Player caller, String[] args) 
	{
		if (args.length == 1)
			help(caller);
		
		else if (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("h"))
			help(caller);

		else if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("mimic"))
			setMimic(caller, args);

		else if (args[1].equalsIgnoreCase("create"))
			create(caller, args);

		else if (args[1].equalsIgnoreCase("disband") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("d"))
			delete(caller, args);

		else if (args[1].equalsIgnoreCase("invite") || args[1].equalsIgnoreCase("i"))
			invite(caller, args);

		else if (args[1].equalsIgnoreCase("promote"))
			promote(caller, args);
		
		else if (args[1].equalsIgnoreCase("demote"))
			demote(caller, args);
		
		else if (args[1].equalsIgnoreCase("kick") || args[1].equalsIgnoreCase("k"))
			kick(caller, args);

		else if (args[1].equalsIgnoreCase("ally") || args[1].equalsIgnoreCase("a"))
			ally(caller, args);

		else if (args[1].equalsIgnoreCase("trust"))
			trust(caller, args);

		else if (args[0].equalsIgnoreCase("neutral") || args[0].equalsIgnoreCase("neut") || args[0].equalsIgnoreCase("n"))
			neutral(caller, args);

		else if (args[1].equalsIgnoreCase("claim") || args[1].equalsIgnoreCase("c"))
			claim(caller);

		else if (args[1].equalsIgnoreCase("unclaim") || args[1].equalsIgnoreCase("uc"))
			unclaim(caller, args);

		else if (args[1].equalsIgnoreCase("home") || args[1].equalsIgnoreCase("h"))
			home(caller, args);
		
		else if (args[1].equalsIgnoreCase("safe"))
			safe(caller);
		
		else if (args[1].equalsIgnoreCase("autoclaim"))
			autoclaim(caller);

		else
			help(caller);
	}

	private void help(Player caller) 
	{
		UtilPlayer.message(caller, F.main("Clans Admin", "Admin Commands List;"));

		UtilPlayer.message(caller, F.help("/c x create <clan>", "Create Admin Clan", Rank.ADMIN));
		
		UtilPlayer.message(caller, F.help("/c x set <clan>", "Set Mimic Clan", Rank.ALL));

		UtilPlayer.message(caller, F.help("/c x home (set)", "Teleport to Mimic Home", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x invite <player>", "Invite Player to Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x promote <player>", "Promote Player in Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x demote <player>", "Demote Player in Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x kick <player>", "Kick Player from Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x ally <clan>", "Send Alliance to Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x trust <clan>", "Give Trust to Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x neutral <clan>", "Set Neutrality", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x enemy <clan>", "Start Invasion", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x claim", "Claim Territory for Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x unclaim (all)", "Unclaim Territory for Mimic", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x delete", "Delete Mimic Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c x autoclaim", "AutoClaim for Mimic Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.main("Mimic Clan", Clans.Get(caller).getMimic()));
	}
	
	private void autoclaim(Player caller) 
	{
		Clans.Get(caller).setAutoClaim(!Clans.Get(caller).isAutoClaim());
		
		UtilPlayer.message(caller, F.main("Clans Admin", F.oo("Auto Claim", Clans.Get(caller).isAutoClaim())));
	}

	public void setMimic(Player caller, String[] args)
	{
		if (args.length < 3)
		{
			if (Clans.Get(caller).getMimic().length() > 0)
			{
				UtilPlayer.message(caller, F.main("Clans Admin", "You are no longer mimicing " + F.elem("Clan " + Clans.Get(caller).getMimic()) + "."));
				Clans.Get(caller).setMimic("");
			}
			else
				UtilPlayer.message(caller, F.main("Clans Admin", "You did not input a Clan/Player."));
			
			return;
		}

		ClanInfo clan = Clans.getClanUtility().searchClanPlayer(caller, args[2], true);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "Invalid Clan/Player."));
			return;
		}

		//Set Mimic
		Clans.Get(caller).setMimic(clan.getName());

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You are mimicing " + F.elem("Clan " + clan.getName()) + "."));
	}

	public ClanInfo getMimic(Player caller, boolean inform)
	{
		String mimic = Clans.Get(caller).getMimic();
		
		if (mimic.length() == 0)
			return null;
		
		ClanInfo clan = Clans.getClanUtility().searchClanPlayer(caller, mimic, true);

		if (clan == null)
		{
			if (inform)
				UtilPlayer.message(caller, F.main("Clans Admin", "You are not mimicing a Clan."));

			return null;	
		}

		return clan;	
	}

	public void create(final Player caller, final String[] args)
	{
			if (args.length < 3)
			{
				UtilPlayer.message(caller, F.main("Clans Admin", "You did not input a Clan name."));
				return;
			}

			if (!UtilInput.valid(args[2]))
			{
				UtilPlayer.message(caller, F.main("Clans Admin", "Invalid characters in Clan name."));
				return;
			}

			if (args[2].length() < Clans.getNameMin())
			{
				UtilPlayer.message(caller, F.main("Clans Admin", "Clan name too short. Minimum length is " + (Clans.getNameMin()) + "."));
				return;
			}

			if (args[2].length() > Clans.getNameMax())
			{
				UtilPlayer.message(caller, F.main("Clans Admin", "Clan name too long. Maximum length is + " + (Clans.getNameMax()) + "."));
				return;
			}

			for (String cur : Clans.denyClan)
			{
				if (cur.equalsIgnoreCase(args[2]))
				{
					UtilPlayer.message(caller, F.main("Clans Admin", "Clan name cannot be a Clan command."));
					return;
				}
			}

			if (Clans.getClan(args[2]) != null)
			{
				UtilPlayer.message(caller, F.main("Clans Admin", F.elem("Clan " + args[2]) + " already exists."));
				return;
			}
			
			Clans.getClientManager().checkPlayerNameExact(new Callback<Boolean>()
			{
				public void run(final Boolean nameExists)
				{
					Bukkit.getServer().getScheduler().runTask(Clans.getPlugin(), new Runnable()
					{
						public void run()
						{
							if (nameExists)
								UtilPlayer.message(caller, F.main("Clans Admin", "Clan name cannot be a Player name."));
							else
							{
								//Inform
								UtilServer.broadcast(F.main("Clans Admin", caller.getName() + " formed " + F.elem("Admin Clan " + args[2]) + "."));

								// Create and Join
								Clans.getClanDataAccess().create(caller.getName(), args[2], true);

								// Set Mimic
								Clans.Get(caller).setMimic(args[2]);

								// Inform
								UtilPlayer.message(caller, F.main("Clans Admin", "You are mimicing Clan " + args[2] + "."));
							}
						}
					});
				}
			}, args[2]);
	}

	public void delete(Player caller, String[] args)
	{
		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;	

		//Task
		Clans.getClanDataAccess().delete(clan);

		//Inform
		UtilServer.broadcast(F.main("Clans Admin", caller.getName() + " disbanded " + F.elem("Clan " + clan.getName()) + "."));
	}

	public void invite(Player caller, String[] args)
	{
		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;	

		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You did not input an invitee."));
			return;
		}

		Player target = UtilPlayer.searchOnline(caller, args[2], true);
		if (target == null)
			return;

		if (target.getName().equals(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You cannot invite yourself."));
			return;
		}

		//Inform
		clan.inform(caller.getName() + " invited " + target.getName() + " to join Clan " + clan.getName() + ".", caller.getName());
		UtilPlayer.message(caller, F.main("Clans Admin", "You invited " + target.getName() + " to join " + F.elem("Clan " + clan.getName()) + "."));
		UtilPlayer.message(target, F.main("Clans Admin", caller.getName() + " invited you to join " + F.elem("Clan " + clan.getName()) + "."));

		//Task
		Clans.getClanDataAccess().invite(clan, target.getName(), caller.getName());
	}
	
	public void promote(Player caller, String[] args)
	{
		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;	

		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You did not input player to promote."));
			return;
		}

		String target = UtilPlayer.searchCollection(caller, args[2], clan.getMembers().keySet(), "Clan Member", true);

		if (target == null)
			return;

		if (clan.getMembers().get(target) == ClanRole.LEADER)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You cannot promote " + F.name(target) + " any further."));
			return;
		}
		
		//Task
		String newRank = "?";
		if (clan.getMembers().get(target) == ClanRole.RECRUIT)
		{
			Clans.getClanDataAccess().role(clan, target, ClanRole.MEMBER);
			newRank = "Member";
		}
		else if (clan.getMembers().get(target) == ClanRole.MEMBER)
		{
			Clans.getClanDataAccess().role(clan, target, ClanRole.ADMIN);
			newRank = "Admin";
		}
		else if (clan.getMembers().get(target) == ClanRole.ADMIN)
		{
			Clans.getClanDataAccess().role(clan, target, ClanRole.LEADER);
			newRank = "Leader";
		}

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You promoted " + target + " to " + newRank + " in Mimic Clan."));
		clan.inform(caller.getName() + " promoted " + target + " to " + newRank + ".", null);
	}
	
	public void demote(Player caller, String[] args)
	{
		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;	

		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You did not input player to demote."));
			return;
		}

		String target = UtilPlayer.searchCollection(caller, args[2], clan.getMembers().keySet(), "Clan Member", true);
		if (target == null)
			return;

		if (clan.getMembers().get(target) == ClanRole.RECRUIT)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You cannot demote " + F.name(target) + " any further."));
			return;
		}
		
		//Task
		String newRank = "?";
		if (clan.getMembers().get(target) == ClanRole.MEMBER)
		{
			Clans.getClanDataAccess().role(clan, target, ClanRole.RECRUIT);
			newRank = "Recruit";
		}
		else if (clan.getMembers().get(target) == ClanRole.ADMIN)
		{
			Clans.getClanDataAccess().role(clan, target, ClanRole.MEMBER);
			newRank = "Member";
		}
		else if (clan.getMembers().get(target) == ClanRole.LEADER)
		{
			Clans.getClanDataAccess().role(clan, target, ClanRole.ADMIN);
			newRank = "Admin";
		}

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You demoted " + target + " to " + newRank + " in Mimic Clan."));
		clan.inform(F.main("Clans Admin", caller.getName() + " demoted " + target + " to " + newRank + "."), null);
	}

	public void kick(Player caller, String[] args)
	{
		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;		

		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You did not input a Player to kick."));
			return;
		}

		String targetName = UtilPlayer.searchCollection(caller, args[2], clan.getMembers().keySet(), "Clan Member", true);

		if (targetName == null)
			return;

		//Task
		Clans.getClanDataAccess().leave(clan, targetName);

		//Inform
		UtilPlayer.message(UtilPlayer.searchOnline(null, targetName, false), F.main("Clans Admin", caller.getName() + " kicked you from " + F.elem("Clan " + clan.getName()) + "."));
		UtilPlayer.message(caller, F.main("Clans Admin", "You kicked " + targetName + " from your Clan."));
		clan.inform(F.main("Clans Admin", caller.getName() + " kicked " + targetName + " from your Clan."), caller.getName());
	}

	public void ally(Player caller, String[] args)
	{
		ClanInfo cA = getMimic(caller, true);

		if (cA == null)
			return;			

		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You did not input a Clan to ally."));
			return;
		}

		ClanInfo cB = Clans.getClanUtility().searchClanPlayer(caller, args[2], true);

		if (cB == null)
			return;

		if (cA.isSelf(cB.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You cannot ally with yourself."));
			return;
		}

		if (cA.isAlly(cB.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You are already allies with " + F.elem("Clan " + cB.getName()) + "."));
			return;
		}

		if (cB.isRequested(cA.getName()))
		{
			//Task
			Clans.getClanDataAccess().ally(cA, cB, caller.getName());

			//Inform
			UtilPlayer.message(caller, F.main("Clans Admin", "You accepted alliance with Clan " + cB.getName() + "."));
			cA.inform(caller.getName() + " accepted alliance with Clan " + cB.getName() + ".", caller.getName());
			cB.inform("Clan " + cA.getName() + " has accepted alliance with you.", null);	
		}
		else
		{
			//Task 
			Clans.getClanDataAccess().requestAlly(cA, cB, caller.getName());

			//Inform
			UtilPlayer.message(caller, F.main("Clans Admin", "You requested alliance with Clan " + cB.getName() + "."));
			cA.inform(caller.getName() + " has requested alliance with Clan " + cB.getName() + ".", caller.getName());
			cB.inform("Clan " + cA.getName() + " has requested alliance with you.", null);	
		}
	}

	public void trust(Player caller, String[] args)
	{
		ClanInfo callerClan = getMimic(caller, true);

		if (callerClan == null)
			return;		

		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You did not input a Clan to enemy."));
			return;
		}

		ClanInfo otherClan = Clans.getClanUtility().searchClanPlayer(caller, args[2], true);

		if (otherClan == null)
			return;

		if (!callerClan.isAlly(otherClan.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You cannot give trust to enemies."));
			return;
		}

		//Task
		if (Clans.getClanDataAccess().trust(callerClan, otherClan, caller.getName()))
		{
			//Inform
			UtilPlayer.message(caller, F.main("Clans Admin", "You gave trust to Clan " + otherClan.getName() + "."));
			callerClan.inform(caller.getName() + " has given trust to Clan " + otherClan.getName() + ".", caller.getName());
			otherClan.inform("Clan " + callerClan.getName() + " has given trust to you.", null);	
		}
		else
		{
			//Inform
			UtilPlayer.message(caller, F.main("Clans Admin", "You revoked trust to Clan " + otherClan.getName() + "."));
			callerClan.inform(caller.getName() + " has revoked trust to Clan " + otherClan.getName() + ".", caller.getName());
			otherClan.inform("Clan " + callerClan.getName() + " has revoked trust to you.", null);	
		}
	}

	public void neutral(Player caller, String[] args)
	{
		ClanInfo cA = getMimic(caller, true);

		if (cA == null)
			return;		

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Clan to set neutrality with."));
			return;
		}

		ClanInfo cB = Clans.getClanUtility().searchClanPlayer(caller, args[1], true);

		if (cB == null)
			return;

		if (cB.isSelf(cA.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You prefer to think of yourself positively..."));
			return;
		}
		
		if (cB.isNeutral(cA.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You are already neutral with " + F.elem("Clan " + cB.getName()) + "."));
			return;
		}

		if (cB.isAlly(cA.getName()))
		{
			//Task
			Clans.getClanDataAccess().neutral(cA, cB, caller.getName(), true);

			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You revoked alliance with " + F.elem("Clan " + cB.getName()) + "."));
			cA.inform(F.name(caller.getName()) + " revoked alliance with " + F.elem("Clan " + cB.getName()) + ".", caller.getName());
			cB.inform(F.elem("Clan " + cA.getName()) + " has revoked alliance with you.", null);
			
			return;
		}
	}

	public void claim(Player caller)
	{
		ClanInfo clientClan = getMimic(caller, true);

		if (clientClan == null)
			return;	

		if (clientClan.getClaims() >= clientClan.getClaimsMax())
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "Your Clan cannot claim more Territory."));
			return;
		}

		String chunk = UtilWorld.chunkToStr(caller.getLocation().getChunk());
		ClanInfo ownerClan = Clans.getClanUtility().getOwner(caller.getLocation());

		//Already Claimed
		if (ownerClan != null)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "This Territory is claimed by " + 
					Clans.getClanUtility().mRel(Clans.getClanUtility().relPC(caller.getName(), ownerClan), ownerClan.getName(), true) + "."));
			return;
		}

		//Task
		Clans.getClanDataAccess().claim(clientClan.getName(), chunk, caller.getName(), false);

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You claimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + "."));
		clientClan.inform(caller.getName() + " claimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + ".", caller.getName());
	}

	public void unclaim(Player caller, String args[])
	{
		if (args.length > 2)
		{
			if (args[2].equalsIgnoreCase("all") || args[2].equalsIgnoreCase("a"))
			{
				unclaimall(caller);
				return;
			}
		}

		ClanInfo clientClan = getMimic(caller, true);

		if (clientClan == null)
			return;	

		String chunk = UtilWorld.chunkToStr(caller.getLocation().getChunk());
		ClanInfo ownerClan = Clans.getClanUtility().getOwner(caller.getLocation());

		//Not Claimed
		if (ownerClan == null)
		{		
			UtilPlayer.message(caller, F.main("Clans Admin", "Territory is not claimed."));
			return;
		}

		//Task
		Clans.getClanDataAccess().unclaim(chunk, caller.getName(), true);

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You unclaimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + "."));
		ownerClan.inform(caller.getName() + " unclaimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + ".", caller.getName());
	}

	public void unclaimall(Player caller)
	{
		ClanInfo clientClan = getMimic(caller, true);

		if (clientClan == null)
			return;	

		//Unclaim
		ArrayList<String> toUnclaim = new ArrayList<String>();

		for (String chunk : clientClan.getClaimSet())
			toUnclaim.add(chunk);

		for (String chunk : toUnclaim)
			Clans.getClanDataAccess().unclaim(chunk, caller.getName(), true);

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You unclaimed all your Clans Territory."));
		clientClan.inform(caller.getName() + " unclaimed all your Clans Territory.", caller.getName());
	}

	public void home(Player caller, String[] args)
	{
		if (args.length > 2)
		{
			if (args[2].equalsIgnoreCase("set") || args[2].equalsIgnoreCase("s"))	
			{
				homeSet(caller);
				return;
			}
		}

		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;	

		if (clan.getHome() == null)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "Your Clan has not set a Home."));
			return;
		}

		if (!clan.getClaimSet().contains(UtilWorld.chunkToStr(clan.getHome().getChunk())))
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "Your Clan has lost its Home Territory."));
			return;
		}

		//Do
		Clans.getTeleport().TP(caller, clan.getHome());

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You teleported to your Clan Home " + UtilWorld.locToStrClean(caller.getLocation()) + "."));
	}

	public void homeSet(Player caller)
	{
		ClanInfo clan = getMimic(caller, true);

		if (clan == null)
			return;	

		if (Clans.getClanUtility().getOwner(caller.getLocation()) == null)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You must set your Clan Home in your own Territory."));
			return;
		}

		if (!Clans.getClanUtility().getOwner(caller.getLocation()).isSelf(clan.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You must set your Clan Home in your own Territory."));
			return;
		}

		//Task
		Clans.getClanDataAccess().home(clan, caller.getLocation(), caller.getName());

		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "You set Clan Home to " + UtilWorld.locToStrClean(caller.getLocation()) + "."));
		clan.inform(caller.getName() + " set Clan Home to " + UtilWorld.locToStrClean(caller.getLocation()) + ".", caller.getName());
	}
	
	public void safe(Player caller)
	{
		ClanTerritory claim = Clans.getClanUtility().getClaim(caller.getLocation());

		if (claim == null)
		{
			UtilPlayer.message(caller, F.main("Clans Admin", "You can only Safe Zone on Claimed Territory."));
			return;
		}
		
		//Set
		Clans.getClanDataAccess().safe(claim, caller.getName());
		
		//Inform
		UtilPlayer.message(caller, F.main("Clans Admin", "Territory Safe Zone: " + F.tf(claim.Safe)));
	}
}
