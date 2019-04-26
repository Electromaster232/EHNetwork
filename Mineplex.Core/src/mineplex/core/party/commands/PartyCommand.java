package mineplex.core.party.commands;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.jsonchat.ChildJsonMessage;
import mineplex.core.common.jsonchat.ClickEvent;
import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.party.Party;
import mineplex.core.party.PartyManager;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PartyCommand extends CommandBase<PartyManager>
{
	public PartyCommand(PartyManager plugin)
	{
		super(plugin, Rank.ALL, new String[]
			{
					"party", "z"
			});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0 || (args[0].equalsIgnoreCase("kick") && args.length < 2))
		{
			UtilPlayer.message(caller, F.main("Party", "Listing Party Commands;"));
			UtilPlayer.message(caller, F.value(0, "/party <Player>", "Join/Create/Invite Player"));
			UtilPlayer.message(caller, F.value(0, "/party leave", "Leave your current Party"));
			UtilPlayer.message(caller, F.value(0, "/party kick <Player>", "Kick player from your Party"));

			return;
		}

		// Callers Party
		Party party = Plugin.GetParty(caller);

		// Leave
		if (args[0].equalsIgnoreCase("leave"))
		{
			if (party == null)
			{
				UtilPlayer.message(caller, F.main("Party", "You are not in a Party."));
			}
			else
			{
				party.LeaveParty(caller);
			}

			return;
		}

		// Leave
		if (args[0].equalsIgnoreCase("kick"))
		{
			if (party == null)
			{
				UtilPlayer.message(caller, F.main("Party", "You are not in a Party."));
			}
			else
			{
				if (party.GetLeader().equals(caller.getName()))
				{
					String target = UtilPlayer.searchCollection(caller, args[1], party.GetPlayers(), "Party ", true);
					if (target == null)
						return;

					if (target.equals(caller.getName()))
					{
						UtilPlayer.message(caller, F.main("Party", "You cannot kick yourself from the Party."));
						return;
					}

					party.KickParty(target);
				}
				else
				{
					UtilPlayer.message(caller, F.main("Party", "You are not the Party Leader."));
				}
			}

			return;
		}

		// Main
		Player target = UtilPlayer.searchOnline(caller, args[0], true);
		if (target == null)
			return;

		if (target.equals(caller))
		{
			UtilPlayer.message(caller, F.main("Party", "You cannot Party with yourself."));
			return;
		}

		// Preference check
		if (!Plugin.getPreferenceManager().Get(target).PartyRequests)
		{
			UtilPlayer.message(
					caller,
					F.main("Party", "You may not party with " + F.name(UtilEnt.getName(target))
							+ "! They are not accepting party requests!"));
			return;
		}

		// Invite or Suggest
		if (party != null)
		{
			if (party.GetPlayers().size() + party.GetInvitees().size() >= 16)
			{
				UtilPlayer.message(caller, "Your party cannot be larger than 16 players.");
				caller.playSound(caller.getLocation(), Sound.NOTE_BASS, 1f, 1.5f);
			}
			// Decline
			else if (party.GetPlayers().contains(target.getName()))
			{
				UtilPlayer.message(caller, F.main("Party", F.name(target.getName()) + " is already in the Party."));
				caller.playSound(caller.getLocation(), Sound.NOTE_BASS, 1f, 1.5f);
			}
			// Decline
			else if (party.GetInvitees().contains(target.getName()))
			{
				UtilPlayer.message(caller, F.main("Party", F.name(target.getName()) + " is already invited to the Party."));
				caller.playSound(caller.getLocation(), Sound.NOTE_BASS, 1f, 1.5f);
			}
			// Invite
			else if (party.GetLeader().equals(caller.getName()))
			{
				party.InviteParty(target, Plugin.GetParty(target) != null);
			}
			// Suggest
			else
			{
				party.Announce(F.name(caller.getName()) + " suggested " + F.name(target.getName()) + " be invited to the Party.");

				Player leader = Bukkit.getPlayerExact(party.GetLeader());

				if (leader != null)
				{
					ChildJsonMessage message = new JsonMessage("").extra(C.mHead + "Party> " + C.mBody + "Type ");

					message.add(F.link("/party " + target.getName())).click(ClickEvent.RUN_COMMAND, "/party " + target.getName());

					message.add(C.mBody + " to invite them.");

					message.sendToPlayer(leader);
				}
			}
		}
		// Create or Join
		else
		{
			Party targetParty = Plugin.GetParty(target);

			// Try to Join
			if (targetParty != null)
			{
				if (targetParty.GetInvitees().contains(caller.getName()))
				{
					targetParty.JoinParty(caller);
					return;
				}
			}

			// Create
			party = Plugin.CreateParty(caller);
			party.InviteParty(target, Plugin.GetParty(target) != null);
		}
	}
}
