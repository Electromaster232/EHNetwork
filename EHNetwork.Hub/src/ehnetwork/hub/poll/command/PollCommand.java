package ehnetwork.hub.poll.command;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.hub.poll.PlayerPollData;
import ehnetwork.hub.poll.Poll;
import ehnetwork.hub.poll.PollManager;
import ehnetwork.hub.poll.PollStats;

/**
 * Created by Shaun on 8/17/2014.
 */
public class PollCommand extends CommandBase<PollManager>
{
	public PollCommand(PollManager plugin)
	{
		super(plugin, Rank.ALL, "poll");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		PlayerPollData pollData = Plugin.Get(caller);

		if (args == null || args.length < 1)
		{
			UtilPlayer.message(caller, F.main("Poll", "There was an error processing your poll request"));
			return;
		}

		if (args[0].equalsIgnoreCase("list") && CommandCenter.GetClientManager().Get(caller).GetRank().Has(Rank.MODERATOR))
		{
			if (args.length == 1)
			{
				UtilPlayer.message(caller, F.main("Poll", "Listing Polls;"));

				List<Poll> polls = Plugin.getPolls();
				for (int i = 0; i < polls.size(); i++)
				{
					Poll poll = polls.get(i);
					new JsonMessage(ChatColor.GREEN + poll.getQuestion())
							.hover("show_text", "Poll id: " + poll.getId())
							.click("run_command", "/poll list " + poll.getId())
						.sendToPlayer(caller);
				}

				UtilPlayer.message(caller, F.main("Poll", "Click a poll to view responses!"));
			}
			else
			{
				int id = 0;
				try
				{
					id = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e)
				{
					UtilPlayer.message(caller, F.main("Poll", "Invalid Integer: " + F.elem(args[1])));
					return;
				}

				final int pollId = id;
				final Poll poll = Plugin.getPoll(pollId);

				if (poll == null)
				{
					UtilPlayer.message(caller, F.main("Poll", "Could not find a poll with that id. Try again!"));
					return;
				}

				UtilPlayer.message(caller, "Fetching Poll Stats....");
				Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable()
				{
					@Override
					public void run()
					{
						final PollStats stats = Plugin.getPollStats(pollId);
						Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable()
						{
							@Override
							public void run()
							{
								for (int i = 0; i < 5; i++)
								{
									UtilPlayer.message(caller, "");
								}

								UtilPlayer.message(caller, "Question: " + C.cYellow + poll.getQuestion());
								UtilPlayer.message(caller, "Enabled: " + C.cYellow + poll.isEnabled());
								UtilPlayer.message(caller, "Poll Id: " + C.cYellow + poll.getId());
								UtilPlayer.message(caller, "Reward: " + C.cYellow + poll.getCoinReward());
								UtilPlayer.message(caller, "Display Type: " + C.cYellow + poll.getDisplayType());
								UtilPlayer.message(caller, "");

								DecimalFormat decimalFormat = new DecimalFormat("#.#");
								double aPercent = stats.getAPercent();
								double bPercent = stats.getBPercent();
								double cPercent = stats.getCPercent();
								double dPercent = stats.getDPercent();
								for (int i = 0; i < poll.getAnswers().length; i++)
								{
									String answer = poll.getAnswers()[i];
									if (answer != null)
									{
										double percent = 0;
										if (i == 0)
											percent = aPercent;
										if (i == 1)
											percent = bPercent;
										if (i == 2)
											percent = cPercent;
										if (i == 3)
											percent = dPercent;

										UtilPlayer.message(caller, answer);
										UtilPlayer.message(caller, getProgressBar(percent, 60) + "   " + C.cWhite + "(" + C.cYellow + decimalFormat.format(100d*percent) + "%" + C.cWhite + ")");
									}
								}
								UtilPlayer.message(caller, " ");
								UtilPlayer.message(caller, "Total Responses: " + C.cYellow + stats.getTotal());
							}
						});
					}
				});
			}
		}
		else if (args.length == 2)
		{
			int pollId = 0;
			int answer = 0;
			try
			{
				pollId = Integer.parseInt(args[0]);
				answer = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				UtilPlayer.message(caller, F.main("Poll", "Failed to parse your response. Please try again later."));
				return;
			}

			Poll poll = Plugin.getPoll(pollId);

			if (poll == null)
			{
				UtilPlayer.message(caller, F.main("Poll", "That poll no longer exists. Sorry!"));
				return;
			}

			if (!poll.hasAnswer(answer))
			{
				UtilPlayer.message(caller, F.main("Poll", "That is not a valid response for that poll"));
				return;
			}

			if (!poll.isEnabled())
			{
				UtilPlayer.message(caller, F.main("Poll", "That poll is no longer enabled!"));
				return;
			}

			if (pollData.hasAnswered(poll))
			{
				UtilPlayer.message(caller, F.main("Poll", "You already answered that poll!"));
				return;
			}

			Plugin.answerPoll(caller, poll, answer);
			// They answered the poll, queue up the next poll for 5 seconds from now
			pollData.setPollCooldown(5000);
		}
		else
		{
			UtilPlayer.message(caller, F.main("Poll", "Please click in chat using your mouse to answer!"));
		}
	}

	private String getProgressBar(double percent, int barCount)
	{
		int greenCount = (int) (barCount * percent);
		StringBuilder sb = new StringBuilder(C.cBlue + "[");

		for (int i = 0; i < barCount; i++)
		{
			String color = (i < greenCount ? C.cGreen : C.cGray);
			sb.append(color + "|");
		}

		sb.append(C.cBlue + "]");

		return sb.toString();
	}
}
