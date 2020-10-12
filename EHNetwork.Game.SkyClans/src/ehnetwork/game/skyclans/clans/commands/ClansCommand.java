package ehnetwork.game.skyclans.clans.commands;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilInput;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilTime.TimeUnit;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.skyclans.clans.ClanInfo;
import ehnetwork.game.skyclans.clans.ClansUtility;
import ehnetwork.game.skyclans.clans.ClanRole;
import ehnetwork.game.skyclans.clans.ClansManager;
import ehnetwork.game.skyclans.clans.ClientClan;

public class ClansCommand extends CommandBase<ClansManager>
{
	public ClansCommand(ClansManager plugin)
	{
		super(plugin, Rank.ALL, "c", "clans", "f", "factions");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0)
		{
			if (Plugin.getClanMemberMap().containsKey(caller.getName()))	
				infoClan(caller, Plugin.getClanMemberMap().get(caller.getName()).getName());

			else									
				UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;	
		}

		if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h"))
			help(caller);

		else if (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("x"))
			Plugin.getClanAdmin().command(caller, args);

		else if (args[0].equalsIgnoreCase("create"))
			create(caller, args);

		else if (args[0].equalsIgnoreCase("disband") || args[0].equalsIgnoreCase("delete"))
			delete(caller, args);

		else if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i"))
			invite(caller, args);

		else if (args[0].equalsIgnoreCase("promote") || args[0].equalsIgnoreCase("+"))
			promote(caller, args);

		else if (args[0].equalsIgnoreCase("demote") || args[0].equalsIgnoreCase("-"))
			demote(caller, args);

		else if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("j"))
			join(caller, args);

		else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("l"))
			leave(caller, args);

		else if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("k"))
			kick(caller, args);

		else if (args[0].equalsIgnoreCase("ally") || args[0].equalsIgnoreCase("a"))
			ally(caller, args);

		else if (args[0].equalsIgnoreCase("trust"))
			trust(caller, args);

		else if (args[0].equalsIgnoreCase("neutral") || args[0].equalsIgnoreCase("neut") || args[0].equalsIgnoreCase("n"))
			neutral(caller, args);

		else if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c"))
			claim(caller, args);

		else if (args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("uc"))
			unclaim(caller, args);

		else if (args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("m"))
			map(caller, args);

		else if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h"))
			home(caller, args);

		else if (args[0].equalsIgnoreCase("sethome"))
			homeSet(caller);

		else if (args[0].equalsIgnoreCase("enemy") || args[0].equals("e"))
			enemy(caller, args);

		else if (args[0].equalsIgnoreCase("territory") || args[0].equalsIgnoreCase("t"))
			infoTerritory(caller, args);

		else if (args[0].equalsIgnoreCase("who") || args[0].equalsIgnoreCase("w"))
		{
			if (args.length > 1)	infoClan(caller, args[1]);
			else					infoClan(caller, null);
		}

		else
			infoClan(caller, args[0]);
	}

	public void enemy(Player caller, String[] args)
	{
		if (args.length == 2)
		{
			ClanInfo playerClan = Plugin.getClanUtility().getClanByPlayer(caller);
			ClanInfo otherClan = Plugin.getClanUtility().searchClan(caller, args[1], true);

			if (playerClan == null)
			{
				UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
				return;
			}
			else if (otherClan == null)
			{
				return;
			}

			attemptEnemy(caller, playerClan, otherClan);
		}
		else
		{
			infoEnemy(caller);
		}
	}

	private void attemptEnemy(Player caller, ClanInfo initiatorClan, ClanInfo otherClan)
	{
		Plugin.getWarManager().attemptEnemy(caller, initiatorClan, otherClan);
	}

	private void infoEnemy(Player caller)
	{
		ClanInfo playerClan = Plugin.getClanUtility().getClanByPlayer(caller);

		UtilPlayer.message(caller, playerClan.mEnemy());
	}

	public void commandChat(Player caller, String[] args)
	{
		if (args.length == 0)
		{
			Plugin.Get(caller).setClanChat(!Plugin.Get(caller).isClanChat());
			UtilPlayer.message(caller, F.main("Clans", "Clan Chat: " + F.oo(Plugin.Get(caller).isClanChat())));
			return;
		}

		//Single Clan
		if (!Plugin.Get(caller).isClanChat())
		{
			ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);
			if (clan == null)	
				UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			else
				Plugin.chatClan(clan, caller, F.combine(args, 0, null, false));
		}
	}
	
	public void commandAllyChat(Player caller, String[] args)
	{
		if (args.length == 0)
		{
			Plugin.Get(caller).setAllyChat(!Plugin.Get(caller).isAllyChat());
			UtilPlayer.message(caller, F.main("Clans", "Ally Chat: " + F.oo(Plugin.Get(caller).isAllyChat())));
			return;
		}

		//Single Clan
		if (!Plugin.Get(caller).isAllyChat())
		{
			ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);
			if (clan == null)	UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			else				Plugin.chatAlly(clan, caller, F.combine(args, 0, null, false));
		}
	}

	private void help(Player caller) 
	{
		UtilPlayer.message(caller, F.main("Clans", "Commands List;"));
		UtilPlayer.message(caller, F.help("/c create <clan>", "Create new Clan", Rank.ALL));
		UtilPlayer.message(caller, F.help("/c join <clan>", "Join a Clan", Rank.ALL));
		UtilPlayer.message(caller, F.help("/c leave <clan>", "Leave your Clan", Rank.ALL));
		UtilPlayer.message(caller, F.help("/c map <toggle>", "View Clan Map", Rank.ALL));
		UtilPlayer.message(caller, F.help("/cc (Message)", "Clan Chat (Toggle)", Rank.ALL));

		UtilPlayer.message(caller, F.help("/c promote <player>", "Promote Player in Clan", Rank.MODERATOR));
		UtilPlayer.message(caller, F.help("/c demote <player>", "Demote Player in Clan", Rank.MODERATOR));

		UtilPlayer.message(caller, F.help("/c home (set)", "Teleport to Clan Home", Rank.MODERATOR));

		UtilPlayer.message(caller, F.help("/c invite <player>", "Invite Player to Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c kick <player>", "Kick Player from Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c neutral <clan>", "Request Neutrality with Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c enemy <clan>", "Declare War with Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c ally <clan>", "Send Alliance to Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c trust <clan>", "Give Trust to Clan", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c claim", "Claim Territory", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/c unclaim (all)", "Unclaim Territory", Rank.ADMIN));

		UtilPlayer.message(caller, F.help("/c delete", "Delete your Clan", Rank.OWNER));

		UtilPlayer.message(caller, F.help("/c <clan>", "View Clan Information", Rank.ALL));
	}

	public void create(Player caller, String[] args)
	{
		ClientClan client = Plugin.Get(caller);

		if (Plugin.getClanMemberMap().containsKey(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You are already in a Clan."));
			return;
		}

		/* TODO
		if (!client.canJoin())
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot join a Clan for " + 
					C.mTime + UtilTime.convertString(System.currentTimeMillis() - client.getDelay(), 1, TimeUnit.FIT) + 
					C.mBody + "."));
			return;
		}
		*/

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Clan name."));
			return;
		}

		if (!UtilInput.valid(args[1]))
		{
			UtilPlayer.message(caller, F.main("Clans", "Invalid characters in Clan name."));
			return;
		}

		if (args[1].length() < Plugin.getNameMin())
		{
			UtilPlayer.message(caller, F.main("Clans", "Clan name too short. Minimum length is " + (Plugin.getNameMin()) + "."));
			return;
		}

		if (args[1].length() > Plugin.getNameMax())
		{
			UtilPlayer.message(caller, F.main("Clans", "Clan name too long. Maximum length is + " + (Plugin.getNameMax()) + "."));
			return;
		}

		for (String cur : Plugin.denyClan)
		{
			if (cur.equalsIgnoreCase(args[1]))
			{
				UtilPlayer.message(caller, F.main("Clans", "Clan name cannot be a Clan command."));
				return;
			}
		}

		for (String cur : Plugin.getClanMap().keySet())
		{
			if (cur.equalsIgnoreCase(args[1]))
			{
				UtilPlayer.message(caller, F.main("Clans", "Clan name is already used by another Clan."));
				return;
			}
		}

		//Inform
		UtilServer.broadcast(F.main("Clans", F.name(caller.getName()) + " formed " + F.elem("Clan " + args[1]) + "."));

		
		ClanInfo clan = Plugin.getClanDataAccess().create(caller.getName(), args[1], false);
		
		Plugin.getClanDataAccess().join(clan, caller.getName(), ClanRole.LEADER);
	}

	public void delete(Player caller, String[] args)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (Plugin.getClanUtility().getRole(caller) != ClanRole.LEADER)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader can disband the Clan."));
			return;
		}

		//Task
		Plugin.getClanDataAccess().delete(clan);

		//Inform
		UtilServer.broadcast(F.main("Clans", F.name(caller.getName()) + " disbanded " + F.elem("Clan " + clan.getName()) + "."));
	}

	public void invite(Player caller, String[] args)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (clan.getMembers().get(caller.getName()) != ClanRole.LEADER &&
				clan.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can send invites."));
			return;
		}

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input an invitee."));
			return;
		}

		Player target = UtilPlayer.searchOnline(caller, args[1], true);
		if (target == null)
			return;

		if (target.getName().equals(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot invite yourself."));
			return;
		}

		//Inform
		clan.inform(F.name(caller.getName()) + " invited " + F.name(target.getName()) + " to join your Clan.", caller.getName());
		UtilPlayer.message(caller, F.main("Clans", "You invited " + F.name(target.getName()) + " to join your Clan."));
		UtilPlayer.message(target, F.main("Clans", F.name(caller.getName()) + " invited you to join " + F.elem("Clan " + clan.getName()) + "."));
		UtilPlayer.message(target, F.main("Clans", "Type " + F.elem("/c join " + clan.getName()) + " to accept!"));

		//Task
		Plugin.getClanDataAccess().invite(clan, target.getName(), caller.getName());
	}

	public void join(Player caller, String[] args)
	{
		if (Plugin.getClanMemberMap().containsKey(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You are already in a Clan."));
			return;
		}

		if (!Plugin.Get(caller).canJoin())
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot join a Clan for " + 
					C.mTime + UtilTime.convertString(System.currentTimeMillis() - Plugin.Get(caller).getDelay(), 1, TimeUnit.FIT) + 
					C.mBody + "."));
			return;
		}

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Clan name."));
			return;
		}

		if (!UtilInput.valid(args[1]))
		{
			UtilPlayer.message(caller, F.main("Clans", "Invalid characters in Clan name."));
			return;
		}

		ClanInfo clan = Plugin.getClanUtility().searchClanPlayer(caller, args[1], true);
		if (clan == null)
			return;

		if (!clan.isInvited(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not invited to " + F.elem("Clan " + clan.getName()) + "."));
			return;
		}

		//Task
		Plugin.getClanDataAccess().join(clan, caller.getName(), ClanRole.RECRUIT);

		//Inform
		UtilPlayer.message(caller, F.main("Clans", "You joined " + F.elem("Clan " + clan.getName()) + "."));
		clan.inform(F.name(caller.getName()) + " has joined your Clan.", caller.getName());
	}

	public void leave(Player caller, String[] args)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (clan.getMembers().get(caller.getName()) == ClanRole.LEADER && clan.getMembers().size() > 1)
		{
			UtilPlayer.message(caller, F.main("Clans", "You must pass on " + F.elem("Leadership") + " before leaving."));
			return;
		}

		//Leave or Delete
		if (clan.getMembers().size() > 1)
		{
			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You left " + F.elem("Clan " + clan.getName()) + "."));

			//Task
			Plugin.getClanDataAccess().leave(clan, caller.getName());

			//Inform
			clan.inform(F.name(caller.getName()) + " has left your Clan.", null);
		}
		else
		{
			delete(caller, args);
		}
	}

	public void kick(Player caller, String[] args)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Player to kick."));
			return;
		}

		String target = UtilPlayer.searchCollection(caller, args[1], clan.getMembers().keySet(), "Clan Member", true);

		if (target == null)
			return;

		if (clan.getMembers().get(caller.getName()) != ClanRole.LEADER &&
				clan.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can kick members."));
			return;
		}

		if ((clan.getMembers().get(target) == ClanRole.LEADER && clan.getMembers().get(caller.getName()) == ClanRole.ADMIN) ||
				(clan.getMembers().get(target) == ClanRole.ADMIN && clan.getMembers().get(caller.getName()) == ClanRole.ADMIN))
		{
			UtilPlayer.message(caller, F.main("Clans", "You do not outrank " + F.name(target) + "."));
			return;
		}


		//Task
		Plugin.getClanDataAccess().leave(clan, target);

		//Inform
		UtilPlayer.message(UtilPlayer.searchOnline(null, target, false), F.main("Clans", F.name(caller.getName()) + " kicked you from " + F.elem("Clan " + clan.getName()) + "."));
		UtilPlayer.message(caller, F.main("Clans", "You kicked " + F.name(target) + " from your Clan."));
		clan.inform(F.main("Clans", F.name(caller.getName()) + " kicked " + F.name(target) + " from your Clan."), caller.getName());
	}

	public void promote(Player caller, String[] args)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input player to promote."));
			return;
		}

		String target = UtilPlayer.searchCollection(caller, args[1], clan.getMembers().keySet(), "Clan Member", true);

		if (target == null)
			return;

		if (target.equals(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot promote yourself."));
			return;
		}

		if (clan.getMembers().get(caller.getName()).ordinal() <= clan.getMembers().get(target).ordinal())
		{
			UtilPlayer.message(caller, F.main("Clans", "You do not outrank " + F.name(target) + "."));
			return;
		}

		//Task
		String newRank = "?";
		if (clan.getMembers().get(target) == ClanRole.RECRUIT)
		{
			Plugin.getClanDataAccess().role(clan, target, ClanRole.MEMBER);
			newRank = "Member";
		}
		else if (clan.getMembers().get(target) == ClanRole.MEMBER)
		{
			Plugin.getClanDataAccess().role(clan, target, ClanRole.ADMIN);
			newRank = "Admin";
		}
		else if (clan.getMembers().get(target) == ClanRole.ADMIN)
		{
			Plugin.getClanDataAccess().role(clan, target, ClanRole.LEADER);
			newRank = "Leader";

			//Give Leader
			Plugin.getClanDataAccess().role(clan, caller.getName(), ClanRole.ADMIN);
		}

		//Inform
		clan.inform(F.name(caller.getName()) + " promoted " + F.name(target) + " to " + F.elem(newRank) + ".", null);
	}

	public void demote(Player caller, String[] args)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input player to demote."));
			return;
		}

		String target = UtilPlayer.searchCollection(caller, args[1], clan.getMembers().keySet(), "Clan Member", true);

		if (target == null)
			return;

		if (target.equals(caller.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot demote yourself."));
			return;
		}

		if (clan.getMembers().get(caller.getName()).ordinal() <= clan.getMembers().get(target).ordinal())
		{
			UtilPlayer.message(caller, F.main("Clans", "You do not outrank " + F.name(target) + "."));
			return;
		}

		if (clan.getMembers().get(target) == ClanRole.RECRUIT)
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot demote " + F.name(target) + " any further."));
			return;
		}

		//Task
		String newRank = "?";
		if (clan.getMembers().get(target) == ClanRole.MEMBER)
		{
			Plugin.getClanDataAccess().role(clan, target, ClanRole.RECRUIT);
			newRank = "Recruit";
		}
		else if (clan.getMembers().get(target) == ClanRole.ADMIN)
		{
			Plugin.getClanDataAccess().role(clan, target, ClanRole.MEMBER);
			newRank = "Member";
		}

		//Inform
		clan.inform(F.main("Clans", F.name(caller.getName()) + " demoted " + F.name(target) + " to " + F.elem(newRank) + "."), null);
	}

	public void ally(Player caller, String[] args)
	{
		ClanInfo cA = Plugin.getClanUtility().getClanByPlayer(caller);

		if (cA == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (cA.getMembers().get(caller.getName()) != ClanRole.LEADER && cA.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can manage Alliances."));
			return;
		}

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Clan to ally."));
			return;
		}

		ClanInfo cB = Plugin.getClanUtility().searchClanPlayer(caller, args[1], true);

		if (cB == null)
			return;

		if (cA.isSelf(cB.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot ally with yourself."));
			return;
		}

		if (cA.isAlly(cB.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You are already allies with " + F.elem("Clan " + cB.getName()) + "."));
			return;
		}

		if (cA.getAllies() >= cA.getAlliesMax())
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot have any more allies."));
			return;
		}

		if (cB.getAllies() >= cB.getAlliesMax())
		{
			UtilPlayer.message(caller, F.main("Clans", F.elem("Clan " + cB.getName()) + " cannot have any more allies."));
			return;
		}

		if (cB.isRequested(cA.getName()))
		{
			//Task
			Plugin.getClanDataAccess().ally(cA, cB, caller.getName());

			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You accepted alliance with " + F.elem("Clan " + cB.getName()) + "."));
			cA.inform(F.name(caller.getName()) + " accepted alliance with " + F.elem("Clan " + cB.getName()) + ".", caller.getName());
			cB.inform(F.elem("Clan " + cA.getName()) + " has accepted alliance with you.", null);	
		}
		else
		{
			//Task 
			Plugin.getClanDataAccess().requestAlly(cA, cB, caller.getName());

			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You requested alliance with " + F.elem("Clan " + cB.getName()) + "."));
			cA.inform(F.name(caller.getName()) + " has requested alliance with " + F.elem("Clan " + cB.getName()) + ".", caller.getName());
			cB.inform(F.elem("Clan " + cA.getName()) + " has requested alliance with you.", null);	
		}
	}

	public void trust(Player caller, String[] args)
	{
		ClanInfo cA = Plugin.getClan(Plugin.getClanMemberMap().get(caller.getName()).getName());

		if (cA == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}		

		if (cA.getMembers().get(caller.getName()) != ClanRole.LEADER && cA.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can manage Trust."));
			return;
		}

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Clan to enemy."));
			return;
		}

		ClanInfo cB = Plugin.getClanUtility().searchClanPlayer(caller, args[1], true);

		if (cB == null)
			return;

		if (!cA.isAlly(cB.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot give trust to enemies."));
			return;
		}

		//Task
		if (Plugin.getClanDataAccess().trust(cA, cB, caller.getName()))
		{
			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You gave trust to " + F.elem("Clan " + cB.getName()) + "."));
			cA.inform(F.name(caller.getName()) + " has given trust to " + F.elem("Clan " + cB.getName()) + ".", caller.getName());
			cB.inform(F.elem("Clan " + cA.getName()) + " has given trust to you.", null);	
		}
		else
		{
			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You revoked trust to " + F.elem("Clan " + cB.getName()) + "."));
			cA.inform(F.name(caller.getName()) + " has revoked trust to " + F.elem("Clan " + cB.getName()) + ".", caller.getName());
			cB.inform(F.elem("Clan " + cA.getName()) + " has revoked trust to you.", null);	
		}
	}

	public void neutral(Player caller, String[] args)
	{
		ClanInfo cA = Plugin.getClan(Plugin.getClanMemberMap().get(caller.getName()).getName());

		if (cA == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}	

		if (cA.getMembers().get(caller.getName()) != ClanRole.LEADER && cA.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can manage relationships."));
			return;
		}

		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a Clan to set neutrality with."));
			return;
		}

		ClanInfo cB = Plugin.getClanUtility().searchClanPlayer(caller, args[1], true);

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
			Plugin.getClanDataAccess().neutral(cA, cB, caller.getName(), true);

			//Inform
			UtilPlayer.message(caller, F.main("Clans", "You revoked alliance with " + F.elem("Clan " + cB.getName()) + "."));
			cA.inform(F.name(caller.getName()) + " revoked alliance with " + F.elem("Clan " + cB.getName()) + ".", caller.getName());
			cB.inform(F.elem("Clan " + cA.getName()) + " has revoked alliance with you.", null);

			return;
		}
	}

	public void claim(Player caller, String args[])
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}

		if (clan.getMembers().get(caller.getName()) != ClanRole.LEADER && clan.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can claim Territory."));
			return;
		}

		if (caller.getLocation().getChunk().getX() < -24 ||
				caller.getLocation().getChunk().getX() > 23 ||
				caller.getLocation().getChunk().getZ() < -24 ||
				caller.getLocation().getChunk().getZ() > 23)
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot claim Territory this far away."));
			return;
		}

		String chunk = UtilWorld.chunkToStr(caller.getLocation().getChunk());
		ClanInfo ownerClan = Plugin.getClanUtility().getOwner(caller.getLocation());	

		//Try to Steal
		if (ownerClan != null && !ownerClan.equals(clan))
			if (unclaimSteal(caller, clan, ownerClan))
			{
				return;
			}
			else
			{
				UtilPlayer.message(caller, F.main("Clans", "This Territory is owned by " + 
						Plugin.getClanUtility().mRel(Plugin.getClanUtility().relPC(caller.getName(), ownerClan), ownerClan.getName(), true) + "."));
				return;
			}

		if (clan.getClaims() >= clan.getClaimsMax())
		{
			UtilPlayer.message(caller, F.main("Clans", "Your Clan cannot claim more Territory."));
			return;
		}

		//Boxed
		if (checkBox(caller.getLocation().getChunk(), 4))
		{
			UtilPlayer.message(caller, F.main("Clans", "You cannot claim this Territory, it causes a box."));
			UtilPlayer.message(caller, F.main("Clans", "This means a Territory has all sides claimed."));
			return;
		}

		//Adjacent
		boolean selfAdj = false;
		for (int x=-1 ; x<=1 ; x++)
			for (int z=-1 ; z<=1 ; z++)
			{
				if (x== 0 && z == 0)
					continue;

				String other = UtilWorld.chunkToStr(caller.getWorld().getChunkAt(
						caller.getLocation().getChunk().getX()+x, 
						caller.getLocation().getChunk().getZ()+z));

				ClanInfo adjClan = Plugin.getClanUtility().getOwner(other);

				if (adjClan == null)
					continue;

				if (x == 0 || z == 0)
				{
					if (checkBox(caller.getWorld().getChunkAt(
							caller.getLocation().getChunk().getX()+x, 
							caller.getLocation().getChunk().getZ()+z), 3))
					{
						UtilPlayer.message(caller, F.main("Clans", "You cannot claim this Territory, it causes a box."));
						UtilPlayer.message(caller, F.main("Clans", "This means a Territory has all sides claimed."));
						return;
					}

					if (Plugin.getClanUtility().rel(clan, adjClan) == ClansUtility.ClanRelation.SELF)
					{
						selfAdj = true;
					}

					else if (Plugin.getClanUtility().rel(clan, adjClan) != ClansUtility.ClanRelation.SELF)
					{
						UtilPlayer.message(caller, F.main("Clans", "You cannot claim Territory next to " + 
								Plugin.getClanUtility().mRel(Plugin.getClanUtility().rel(ownerClan, adjClan), adjClan.getName(), true) + "."));
						return;
					}
				}	
			}

		//Not Next to Self
		if (!selfAdj && !clan.getClaimSet().isEmpty())
		{
			UtilPlayer.message(caller, F.main("Clans", "You must claim next to your other Territory."));
			return;
		}

		//Claim Timer
		if (Plugin.getUnclaimMap().containsKey(chunk))
		{
			if (!UtilTime.elapsed(Plugin.getUnclaimMap().get(chunk), Plugin.getReclaimTime()))
			{
				UtilPlayer.message(caller, F.main("Clans", "This Territory cannot be claimed for " + 
						F.time(UtilTime.convertString(Plugin.getReclaimTime() - (System.currentTimeMillis()-Plugin.getUnclaimMap().get(chunk)), 1, TimeUnit.FIT)) + "."));

				return;
			}
			else
			{
				Plugin.getUnclaimMap().remove(chunk);
			}
		}

		//Enemies in Land
		for (Player cur : UtilServer.getPlayers())
			if (UtilMath.offset(cur, caller) < 16)
				if (Plugin.getClanUtility().playerEnemy(caller.getName(), cur.getName()))
				{
					UtilPlayer.message(caller, F.main("Clans", "You cannot claim while enemies are nearby."));
					return;
				}		

		//Recharge
		if (!Recharge.Instance.use(caller, "Territory Claim", 60000, true, false))
			return;

		//Task
		Plugin.getClanDataAccess().claim(clan.getName(), chunk, caller.getName(), false);

		//Inform
		UtilPlayer.message(caller, F.main("Clans", "You claimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + "."));
		clan.inform(F.name(caller.getName()) + " claimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + ".", caller.getName());
	}

	public boolean checkBox(Chunk chunk, int req)
	{
		int boxed = 0;
		for (int x=-1 ; x<=1 ; x++)
			for (int z=-1 ; z<=1 ; z++)
			{
				if (x == 0 && z == 0 || x != 0 && z != 0)
					continue;

				String other = UtilWorld.chunkToStr(chunk.getWorld().getChunkAt(
						chunk.getX()+x, chunk.getZ()+z));

				ClanInfo adjClan = Plugin.getClanUtility().getOwner(other);

				if (adjClan == null)
					continue;

				boxed++;
			}

		return (boxed >= req);
	}

	public void unclaim(Player caller, String args[])
	{
		if (args.length > 1)
		{
			if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("a"))
			{
				unclaimall(caller);
				return;
			}
		}

		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}

		String chunk = UtilWorld.chunkToStr(caller.getLocation().getChunk());
		ClanInfo ownerClan = Plugin.getClanUtility().getOwner(caller.getLocation());

		//Try to Steal
		if (ownerClan != null && !ownerClan.equals(clan))
			if (unclaimSteal(caller, clan, ownerClan))
				return;

		//Role
		if (clan.getMembers().get(caller.getName()) != ClanRole.LEADER && clan.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can unclaim Territory."));
			return;
		}

		//Not Claimed
		if (ownerClan == null || !ownerClan.equals(clan))
		{		
			UtilPlayer.message(caller, F.main("Clans", "This Territory is not owned by you."));
			return;
		}

		//Task
		Plugin.getClanDataAccess().unclaim(chunk, caller.getName(), true);

		//Inform
		UtilPlayer.message(caller, F.main("Clans", "You unclaimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + "."));
		clan.inform(F.name(caller.getName()) + " unclaimed Territory " + F.elem(UtilWorld.chunkToStrClean(caller.getLocation().getChunk())) + ".", caller.getName());
	}

	public boolean unclaimSteal(Player caller, ClanInfo clientClan, ClanInfo ownerClan)
	{
		if (ownerClan.getClaims() > ownerClan.getClaimsMax())
		{
			//Nothing Extra
		}
		else
		{
			return false;
		}

		//Change Inform
		UtilPlayer.message(caller, F.main("Clans", "You can no longer 'steal' territory. " +
				"You simply unclaim it and it can not be reclaimed by anyone for 30 mintes." +
				"This was done to improve gameplay. Enjoy!"));

		//Inform
		UtilServer.broadcast(F.main("Clans", F.elem(clientClan.getName()) + " unclaimed from " + 
				F.elem(ownerClan.getName()) + " at " + F.elem(UtilWorld.locToStrClean(caller.getLocation())) + "."));

		//Unclaim
		Plugin.getClanDataAccess().unclaim(UtilWorld.chunkToStr(caller.getLocation().getChunk()), caller.getName(), true);

		return true;
	}

	public void unclaimall(Player caller)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}

		if (clan.getMembers().get(caller.getName()) != ClanRole.LEADER)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader can unclaim all Territory."));
			return;
		}

		//Unclaim
		ArrayList<String> toUnclaim = new ArrayList<String>();

		for (String chunk : clan.getClaimSet())
			toUnclaim.add(chunk);

		for (String chunk : toUnclaim)
			Plugin.getClanDataAccess().unclaim(chunk, caller.getName(), true);

		//Inform
		UtilPlayer.message(caller, F.main("Clans", "You unclaimed all your Clans Territory."));
		clan.inform(F.name(caller.getName()) + " unclaimed all your Clans Territory.", caller.getName());
	}

	public void map(Player caller, String[] args)
	{
		if (args.length > 1)
		{
			if (args[1].equals("toggle") || args[1].equals("t"))
			{
				Plugin.Get(caller).setMapOn(!Plugin.Get(caller).isMapOn());
				UtilPlayer.message(caller, 
						F.main("Clans", "You toggled Clan Map: " + F.oo(Plugin.Get(caller).isMapOn())));
				return;
			}
		}

		//Display
		Plugin.getClanDisplay().displayOwner(caller);
		Plugin.getClanDisplay().displayMap(caller);
	}

	public void home(Player caller, String[] args)
	{
		if (args.length > 1)
		{
			if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("s"))	
			{
				homeSet(caller);
				return;
			}
		}

		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}

		if (clan.getHome() == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "Your Clan has not set a Home."));
			return;
		}

		if (!clan.getClaimSet().contains(UtilWorld.chunkToStr(clan.getHome().getChunk())))
		{
			UtilPlayer.message(caller, F.main("Clans", "Your Clan has lost its Home Territory."));
			return;
		}

		if (!Plugin.getClanUtility().isSafe(caller.getLocation()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You can only use Clan Home from Spawn."));
			return;
		}

		if (!Plugin.getClanUtility().isSpecial(caller.getLocation(), "Spawn"))
		{
			UtilPlayer.message(caller, F.main("Clans", "You can only use Clan Home from Spawn."));
			return;
		}
		
		/*
		CoreClient client = _plugin.getClientManager().Get(caller);
		for (Player cur : clan.GetHome().getWorld().getPlayers())
			if (client.Clan().GetRelation(cur.getName()) == ClanRelation.NEUTRAL)
				if (clan.GetClaimSet().contains(UtilWorld.chunkToStr(cur.getLocation().getChunk())))
				{
					UtilPlayer.message(caller, F.main("Clans", "You cannot use Clan Home with enemies in your Territory."));
					return;
				}
		*/
		
		if (!Recharge.Instance.use(caller, "Clans Teleport", 300000, true, false))
			return;

		//Do
		Plugin.getTeleport().TP(caller, clan.getHome());

		//Inform
		UtilPlayer.message(caller, F.main("Clans", "You teleported to your Clan Home " + UtilWorld.locToStrClean(caller.getLocation()) + "."));
	}

	public void homeSet(Player caller)
	{
		ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);

		if (clan == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			return;
		}

		if (clan.getMembers().get(caller.getName()) != ClanRole.LEADER && clan.getMembers().get(caller.getName()) != ClanRole.ADMIN)
		{
			UtilPlayer.message(caller, F.main("Clans", "Only the Clan Leader and Admins can manage Clan Home."));
			return;
		}

		if (Plugin.getClanUtility().getOwner(caller.getLocation()) == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You must set your Clan Home in your own Territory."));
			return;
		}

		if (!Plugin.getClanUtility().getOwner(caller.getLocation()).isSelf(clan.getName()))
		{
			UtilPlayer.message(caller, F.main("Clans", "You must set your Clan Home in your own Territory."));
			return;
		}

		//Task
		Plugin.getClanDataAccess().home(clan, caller.getLocation(), caller.getName());

		//Inform
		UtilPlayer.message(caller, F.main("Clans", "You set Clan Home to " + UtilWorld.locToStrClean(caller.getLocation()) + "."));
		clan.inform(caller.getName() + " set Clan Home to " + UtilWorld.locToStrClean(caller.getLocation()) + ".", caller.getName());
	}


	public void infoClan(Player caller, String search)
	{
		if (search == null)
		{
			UtilPlayer.message(caller, F.main("Clans", "You did not input a search parameter."));
			return;
		}

		ClanInfo clan = Plugin.getClanUtility().searchClanPlayer(caller, search, true);
		if (clan == null)
			return;

		UtilPlayer.message(caller, clan.mDetails(caller.getName()));
	}

	public void infoTerritory(Player caller, String[] args)
	{
		ClanInfo clan;
		if (args.length < 2)
		{
			clan = Plugin.getClanUtility().getClanByPlayer(caller);

			if (clan == null)
			{
				UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
				return;
			}
		}

		else
			clan = Plugin.getClanUtility().searchClan(caller, args[1], true);

		if (clan == null)
			return;

		UtilPlayer.message(caller, clan.mTerritory());
	}
}
