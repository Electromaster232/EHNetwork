package mineplex.hub.poll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.util.com.google.gson.JsonObject;
import mineplex.core.MiniDbClientPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.donation.DonationManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.poll.command.PollCommand;

public class PollManager extends MiniDbClientPlugin<PlayerPollData>
{
	private PollRepository _repository;
	private DonationManager _donationManager;
	private List<Poll> _polls;

	public PollManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("PollManager", plugin, clientManager);

		_repository = new PollRepository(plugin);

		_donationManager = donationManager;

		plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				_polls = _repository.retrievePolls();
			}
		}, 1L, 1200L);
	}

	@Override
	protected PlayerPollData AddPlayer(String player)
	{
		return new PlayerPollData();
	}

	@EventHandler
	public void join(PlayerJoinEvent event)
	{
		PlayerPollData pollData = Get(event.getPlayer());
		pollData.setPollCooldown(5000);
	}

	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;

		if (_polls == null || _polls.size() == 0)
			return;

		for (Player player : _plugin.getServer().getOnlinePlayers())
		{
			PlayerPollData pollData = Get(player);

			if (pollData.shouldPoll())
			{
				Rank playerRank = ClientManager.Get(player).GetRank();
				Poll nextPoll = getNextPoll(pollData, playerRank);
				if (nextPoll != null)
					displayPoll(player, nextPoll);

				// Update the poll cooldown even if there isn't a poll available
				pollData.updatePollCooldown();
			}
		}
	}

	public Poll getNextPoll(PlayerPollData pollData, Rank playerRank)
	{
		for (Poll poll : _polls)
		{
			if (poll.isEnabled() && poll.getDisplayType().shouldDisplay(playerRank) && !pollData.hasAnswered(poll))
				return poll;
		}

		return null;
	}

	public void displayPoll(Player player, Poll poll)
	{
		String[] answers = poll.getAnswers();

		player.sendMessage(C.cGold + C.Bold + "--------------" + C.cYellow + C.Bold + "POLL" + C.cGold + C.Bold + "--------------");
		player.sendMessage(poll.getQuestion());
		player.sendMessage("");
		for (int i = 1; i <= answers.length; i++)
		{
			if (answers[i-1] != null && answers[i-1].length() > 0)
			{
				String message = C.cGreen + i + ". " + answers[i - 1];
				// Base message object
				JsonObject textObject = new JsonObject();
				textObject.addProperty("text", message);

				// Object for click event
				JsonObject clickObject = new JsonObject();
				clickObject.addProperty("action", "run_command");
				clickObject.addProperty("value", "/poll " + poll.getId() + " " + i);

				// Object for hover event
				JsonObject hoverObject = new JsonObject();
				hoverObject.addProperty("action", "show_text");
				hoverObject.addProperty("value", "Click to Select " + F.elem("#" + i));

				// Add the event objects to the base message
				textObject.add("clickEvent", clickObject);
				textObject.add("hoverEvent", hoverObject);

				PacketPlayOutChat chatPacket = new PacketPlayOutChat(ChatSerializer.a(textObject.toString()), true);
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatPacket);
			}
		}
		player.sendMessage("");
		player.sendMessage("Click an answer to receive " + C.cGreen + poll.getCoinReward() + " Gems");
		player.sendMessage(C.cGold + C.Bold + "--------------------------------");

		player.playSound(player.getEyeLocation(), Sound.ORB_PICKUP, 2f, 0f);
	}

	public void answerPoll(final Player player, final Poll poll, final int answer)
	{
		final String name = player.getName();
		final UUID uuid = player.getUniqueId();

		// First update answer locally so we know it was answered
		Get(player).addAnswer(poll.getId(), answer);

		// Here we add the answer into the database, and once that is successful we give the coin reward
		_plugin.getServer().getScheduler().runTaskAsynchronously(_plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if (_repository.addPollAnswer(uuid, poll.getId(), answer))
				{
					// Poll response successful, give coins
					_donationManager.RewardGems(new Callback<Boolean>()
					{
						@Override
						public void run(Boolean completed)
						{
							if (completed)
							{
								// Need to get out of Async code
								_plugin.getServer().getScheduler().runTask(_plugin, new Runnable()
								{
									@Override
									public void run()
									{
										UtilPlayer.message(player, F.main("Poll", "Thanks for your response!"));
										player.playSound(player.getEyeLocation(), Sound.LEVEL_UP, 1F, 0);
										UtilPlayer.message(player, F.main("Gem", "You received " + F.elem(poll.getCoinReward() + "") + " Gems!"));
									}
								});
							}
						}
					}, "Poll", name, uuid, poll.getCoinReward());
				}
			}
		});
	}

	public Poll getPoll(int pollId)
	{
		for (Poll poll : _polls)
		{
			if (poll.getId() == pollId)
				return poll;
		}
		return null;
	}

	public PollStats getPollStats(int pollId)
	{
		return _repository.getPollStats(pollId);
	}

	public List<Poll> getPolls()
	{
		return _polls;
	}

	@Override
	public void addCommands()
	{
		addCommand(new PollCommand(this));
	}

	@Override
	public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
	{
		Set(playerName, _repository.loadPollData(resultSet));
	}

	@Override
	public String getQuery(int accountId, String uuid, String name)
	{
		return "SELECT pollId, value FROM accountPolls WHERE accountPolls.accountId = '" + accountId + "';";
	}
}
