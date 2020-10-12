package ehnetwork.core.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ehnetwork.core.MiniClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.chat.Chat;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.friend.data.FriendData;
import ehnetwork.core.friend.data.FriendStatus;
import ehnetwork.core.ignore.IgnoreManager;
import ehnetwork.core.message.commands.AdminCommand;
import ehnetwork.core.message.commands.AnnounceCommand;
import ehnetwork.core.message.commands.GlobalCommand;
import ehnetwork.core.message.commands.MessageAdminCommand;
import ehnetwork.core.message.commands.MessageCommand;
import ehnetwork.core.message.commands.ResendAdminCommand;
import ehnetwork.core.message.commands.ResendCommand;
import ehnetwork.core.message.redis.AnnouncementHandler;
import ehnetwork.core.message.redis.MessageHandler;
import ehnetwork.core.message.redis.RedisMessage;
import ehnetwork.core.message.redis.RedisMessageCallback;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.punish.Punish;
import ehnetwork.core.punish.PunishClient;
import ehnetwork.core.punish.Punishment;
import ehnetwork.core.punish.PunishmentSentence;
import ehnetwork.serverdata.commands.AnnouncementCommand;
import ehnetwork.serverdata.commands.ServerCommandManager;

public class MessageManager extends MiniClientPlugin<ClientMessage>
{
	private CoreClientManager _clientManager;
	private FriendManager _friendsManager;
	private IgnoreManager _ignoreManager;
	private HashMap<UUID, BukkitRunnable> _messageTimeouts = new HashMap<UUID, BukkitRunnable>();
	private PreferencesManager _preferences;
	private Punish _punish;
	private Chat _chat;
	private ArrayList<String> _randomMessage;
	private String _serverName;

	public MessageManager(JavaPlugin plugin, CoreClientManager clientManager, PreferencesManager preferences,
			IgnoreManager ignoreManager, Punish punish, FriendManager friendManager, Chat chat)
	{
		super("Message", plugin);

		_clientManager = clientManager;
		_preferences = preferences;
		_ignoreManager = ignoreManager;
		_punish = punish;
		_friendsManager = friendManager;
		_chat = chat;
		_serverName = getPlugin().getConfig().getString("serverstatus.name");

		MessageHandler messageHandler = new MessageHandler(this);

		ServerCommandManager.getInstance().registerCommandType("AnnouncementCommand", AnnouncementCommand.class,
				new AnnouncementHandler());

		ServerCommandManager.getInstance().registerCommandType("RedisMessage", RedisMessage.class, messageHandler);
		ServerCommandManager.getInstance()
				.registerCommandType("RedisMessageCallback", RedisMessageCallback.class, messageHandler);
	}

	public void addCommands()
	{
		addCommand(new MessageCommand(this));
		addCommand(new ResendCommand(this));

		addCommand(new MessageAdminCommand(this));
		addCommand(new ResendAdminCommand(this));

		addCommand(new AnnounceCommand(this));
		addCommand(new GlobalCommand(this));

		addCommand(new AdminCommand(this));
	}

	@Override
	protected ClientMessage AddPlayer(String player)
	{
		Set(player, new ClientMessage());
		return Get(player);
	}

	public boolean canMessage(Player from, Player to)
	{
		if (!canSenderMessageThem(from, to.getName()))
		{
			return false;
		}

		String canMessage = canReceiverMessageThem(from.getName(), to);

		if (canMessage != null)
		{
			from.sendMessage(canMessage);

			return false;
		}

		return true;
	}

	public String canReceiverMessageThem(String sender, Player target)
	{
		// If the receiver has turned off private messaging and the sender isn't a mod
		if (!_preferences.Get(target).PrivateMessaging)
		{
			return C.cPurple + target.getName() + " has private messaging disabled.";

		}

		// If the receiver is ignoring the sender, and the sender isn't a mod
		if (_ignoreManager.isIgnoring(target, sender))
		{
			return F.main(_ignoreManager.getName(), ChatColor.GRAY + "That player is ignoring you");
		}

		return null;
	}

	public boolean isMuted(Player sender)
	{
		PunishClient client = _punish.GetClient(sender.getName());

		if (client != null && client.IsMuted())
		{
			Punishment punishment = client.GetPunishment(PunishmentSentence.Mute);

			sender.sendMessage(F.main(_punish.getName(), "Shh, you're muted because "

			+ punishment.GetReason()

			+ " by "

			+ punishment.GetAdmin()

			+ " for "

			+ C.cGreen

			+ UtilTime.convertString(punishment.GetRemaining(), 1, UtilTime.TimeUnit.FIT) + "."));

			return true;
		}

		return false;
	}

	public boolean canSenderMessageThem(Player sender, String target)
	{
		if (isMuted(sender))
		{
			return false;
		}

		if (_ignoreManager.isIgnoring(sender, target))
		{
			sender.sendMessage(F.main(_ignoreManager.getName(), ChatColor.GRAY + "You are ignoring that player"));

			return false;
		}

		return true;
	}

	public void DoMessage(Player from, Player to, String message)
	{
		PrivateMessageEvent pmEvent = new PrivateMessageEvent(from, to, message);
		Bukkit.getServer().getPluginManager().callEvent(pmEvent);
		if (pmEvent.isCancelled())
			return;

		if (!canMessage(from, to))
		{
			return;
		}

		// My attempt at trying to mitigate some of the spam bots - Phinary
		// Triggers if they are whispering a new player
		if (!GetClientManager().Get(from).GetRank().Has(Rank.HELPER) && Get(from).LastTo != null
				&& !Get(from).LastTo.equalsIgnoreCase(to.getName()))
		{
			long delta = System.currentTimeMillis() - Get(from).LastToTime;

			if (Get(from).SpamCounter > 3 && delta < Get(from).SpamCounter * 1000)
			{
				from.sendMessage(F.main("Cooldown", "Try sending that message again in a few seconds"));
				Get(from).LastTo = to.getName();
				return;
			}
			else if (delta < 8000)
			{
				// Silently increment spam counter whenever delta is less than 8 seconds
				Get(from).SpamCounter++;
			}
		}

		message = _chat.getFilteredMessage(from, message);

		// Inform
		UtilPlayer.message(from, C.cGold + "§l" + from.getName() + " > " + to.getName() + C.cYellow + " §l" + message);

		// Save
		Get(from).LastTo = to.getName();
		Get(from).LastToTime = System.currentTimeMillis();

		// Chiss or defek7
		if (to.getName().equals("Chiss") || to.getName().equals("defek7") || to.getName().equals("Phinary") || to.getName().equals("fooify"))
		{
			UtilPlayer.message(from, C.cPurple + to.getName() + " is often AFK or minimized, due to plugin development.");
			UtilPlayer.message(from, C.cPurple + "Please be patient if he does not reply instantly.");
		}

		// Log
		// Logger().logChat("Private Message", from, to.getName(), message);

		// Sound
		from.playSound(to.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
		to.playSound(to.getLocation(), Sound.NOTE_PIANO, 2f, 2f);

		// Send
		UtilPlayer.message(to, C.cGold + "§l" + from.getName() + " > " + to.getName() + C.cYellow + " §l" + message);
	}

	public void DoMessageAdmin(Player from, Player to, String message)
	{
		// Inform
		UtilPlayer.message(from, C.cPurple + "-> " + F.rank(_clientManager.Get(to).GetRank()) + " " + to.getName() + " "
				+ C.cPurple + message);

		// Inform Admins
		for (Player staff : UtilServer.getPlayers())
		{
			if (!to.equals(staff) && !from.equals(staff))
			{
				if (_clientManager.Get(staff).GetRank().Has(Rank.HELPER))
				{
					UtilPlayer.message(staff, F.rank(_clientManager.Get(from).GetRank()) + " " + from.getName() + C.cPurple
							+ " -> " + F.rank(_clientManager.Get(to).GetRank()) + " " + to.getName() + " " + C.cPurple + message);
				}
			}
		}

		// Save
		Get(from).LastAdminTo = to.getName();

		// Send
		UtilPlayer.message(to, C.cPurple + "<- " + F.rank(_clientManager.Get(from).GetRank()) + " " + from.getName() + " "
				+ C.cPurple + message);

		// Sound
		from.playSound(to.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
		to.playSound(to.getLocation(), Sound.NOTE_PIANO, 2f, 2f);

		// Log XXX
		// Logger().logChat("Staff Message", from, to.getName(), message);
	}

	// Module Functions
	@Override
	public void enable()
	{
		_randomMessage = new ArrayList<String>();
		_randomMessage.clear();
		_randomMessage.add("Hello, do you have any wild boars for purchase?");
		_randomMessage.add("There's a snake in my boot!");
		_randomMessage.add("Monk, I need a Monk!");
		_randomMessage.add("Hi, I'm from planet minecraft, op me plz dooooood!");
		_randomMessage.add("Somebody's poisoned the waterhole!");
		_randomMessage.add("MORE ORBZ MORE ORBZ MORE ORBZ MORE ORBZ!");
		_randomMessage.add("Chiss is a chiss and chiss chiss.");
		_randomMessage.add("*_*");
		_randomMessage.add("#swag");
		_randomMessage.add("Everything went better then I thought.");
		_randomMessage.add("HAVE A CHICKEN!");
		_randomMessage.add("follow me, i have xrays");
		_randomMessage.add("I'm making a java");
		_randomMessage.add("Do you talk to strangers?  I have candy if it helps.");
		_randomMessage.add("Solid 2.9/10");
		_randomMessage.add("close your eyes to sleep");
		_randomMessage.add("I crashed because my internet ran out.");
		_randomMessage.add("I saw morgan freeman on a breaking bad ad on a bus.");
		_randomMessage.add("Where is the volume control?");
		_randomMessage.add("I saw you playing on youtube with that guy and stuff.");
		_randomMessage.add("Your worms must be worse than useless.");
		_randomMessage.add("meow");
		_randomMessage.add("7");
		_randomMessage.add("Don't you wish your girlfriend was hot like me?");
		_randomMessage.add("how do you play mindcrafts?");
		_randomMessage.add("7 cats meow meow meow meow meow meow meow");
		_randomMessage.add("For King Jonalon!!!!!");
		_randomMessage.add("Do you like apples?");
		_randomMessage.add("I'm Happy Happy Happy.");
		_randomMessage.add("kthxbye");
		_randomMessage.add("i like pie.");
		_randomMessage.add("Do you play Clash of Clans?");
		_randomMessage.add("Mmm...Steak!");
		_randomMessage.add("Poop! Poop everywhere!");
		_randomMessage.add("I'm so forgetful. Like I was going to say somethin...wait what were we talking about?");
		_randomMessage.add("Mmm...Steak!");
	}

	public CoreClientManager GetClientManager()
	{
		return _clientManager;
	}

	public String GetRandomMessage()
	{
		if (_randomMessage.isEmpty())
			return "meow";

		return _randomMessage.get(UtilMath.r(_randomMessage.size()));
	}

	public ArrayList<String> GetRandomMessages()
	{
		return _randomMessage;
	}

	public void Help(Player caller)
	{
		Help(caller, null);
	}

	public void Help(Player caller, String message)
	{
		UtilPlayer.message(caller, F.main(_moduleName, ChatColor.RED + "Err...something went wrong?"));
	}

	public void receiveMessage(Player to, RedisMessage globalMessage)
	{
		if (globalMessage.isStaffMessage())
		{
			// Message the receiver
			UtilPlayer.message(to, C.cPurple + "<- " + globalMessage.getRank() + " " + globalMessage.getSender() + " "
					+ C.cPurple + globalMessage.getMessage());

			to.playSound(to.getLocation(), Sound.NOTE_PIANO, 2f, 2f);

			String toRank = F.rank(_clientManager.Get(to).GetRank());

			// Message the sender
			RedisMessageCallback message = new RedisMessageCallback(globalMessage, true, to.getName(),

			C.cPurple + "-> " + toRank + " " + to.getName() + " " + C.cPurple + globalMessage.getMessage());

			// Inform Admins
			for (Player staff : UtilServer.getPlayers())
			{
				if (!to.equals(staff))
				{
					if (_clientManager.Get(staff).GetRank().Has(Rank.HELPER))
					{
						UtilPlayer.message(staff,

						globalMessage.getRank() + " " + globalMessage.getSender() + C.cPurple + " " + message.getMessage());
					}
				}
			}

			message.publish();
		}
		else
		{
			String canMessage = canReceiverMessageThem(globalMessage.getSender(), to);

			if (canMessage != null)
			{

				RedisMessageCallback message = new RedisMessageCallback(globalMessage, false, null, canMessage);

				message.publish();

				return;
			}

			String message = C.cGold + "§l" + globalMessage.getSender() + " > " + to.getName() + C.cYellow + " §l"
					+ globalMessage.getMessage();

			// Message the receiver
			UtilPlayer.message(to, message);

			to.playSound(to.getLocation(), Sound.NOTE_PIANO, 2f, 2f);

			// Message the sender
			RedisMessageCallback redisMessage = new RedisMessageCallback(globalMessage, false, to.getName(), message);

			redisMessage.publish();
		}
	}

	public void receiveMessageCallback(RedisMessageCallback message)
	{
		BukkitRunnable runnable = _messageTimeouts.remove(message.getUUID());

		if (runnable != null)
		{
			runnable.cancel();
		}

		Player target = Bukkit.getPlayerExact(message.getTarget());

		if (target != null)
		{
			target.sendMessage(message.getMessage());

			target.playSound(target.getLocation(), Sound.NOTE_PIANO, 2f, 2f);

			if (message.getLastReplied() != null)
			{
				if (message.isStaffMessage())
				{
					Get(target).LastAdminTo = message.getLastReplied();
				}
				else
				{
					Get(target).LastTo = message.getLastReplied();
				}
			}

			if (message.isStaffMessage() && message.getLastReplied() != null)
			{
				String recevierRank = F.rank(_clientManager.Get(target).GetRank());

				// Inform Admins
				for (Player staff : UtilServer.getPlayers())
				{
					if (!target.equals(staff))
					{
						if (_clientManager.Get(staff).GetRank().Has(Rank.HELPER))
						{
							UtilPlayer.message(staff,

							recevierRank + " " + target.getName() + " " + C.cPurple + message.getMessage());
						}
					}
				}
			}
		}
	}

	public void sendMessage(final Player sender, final String target, final String message, final boolean isReply,
			final boolean adminMessage)
	{
		FriendData friends = _friendsManager.Get(sender);
		FriendStatus friend = null;

		if (!adminMessage)
		{

			for (FriendStatus friendInfo : friends.getFriends())
			{

				// We don't grab this guy even if name matches because he is offline. This way, we can get a free message without
				// extra coding as we can't do anything extra with a offline friend..
				if ((isReply || friendInfo.Online) && friendInfo.Name.equalsIgnoreCase(target))
				{
					friend = friendInfo;
					break;
				}

				// If this isn't a reply, no other matches found, friend is online and name begins with param.. Our first guess.
				if (!isReply && friend == null && friendInfo.Online
						&& friendInfo.Name.toLowerCase().startsWith(target.toLowerCase()))
				{
					friend = friendInfo;
				}
			}
		}

		final FriendStatus friendInfo = friend;

		new BukkitRunnable()
		{
			final String newMessage = _chat.getFilteredMessage(sender, message);

			@Override
			public void run()
			{
				new BukkitRunnable()
				{

					@Override
					public void run()
					{
						sendMessage(sender, target, newMessage, adminMessage, isReply, friendInfo);
					}

				}.runTask(getPlugin());
			}

		}.runTaskAsynchronously(getPlugin());
	}

	private void sendMessage(final Player sender, String target, String message, final boolean adminMessage, boolean isReply,
			FriendStatus friend)
	{
		// We now have the friend object, if its not null. We are sending the message to that player.

		// Only notify player if friend is null and its not a reply
		Player to = UtilPlayer.searchOnline(sender, target, !adminMessage && friend == null && !isReply);

		// If isn't admin message, friend is null and target is null. Return because location of receiver is unknown.
		if (!adminMessage && (friend == null || !friend.Online) && to == null)
		{
			// We need to notify them that the player they are replying to is gone
			if (isReply)
			{
				UtilPlayer.message(sender, F.main(getName(), F.name(target) + " is no longer online."));
			}

			return;
		}

		// If this is a message inside the server
		if (to != null)
		{
			if (adminMessage)
			{
				DoMessageAdmin(sender, to, message);
			}
			else
			{
				DoMessage(sender, to, message);
			}
		}
		else
		{
			// Looks like we will be using redis to send a message

			// First get the full name of the player and make it a final String for use in a runnable
			final String playerTarget = adminMessage ? target : friend.Name;

			// If this is a admin message, or the sender isn't muted/ignoring the target
			if (adminMessage || canSenderMessageThem(sender, playerTarget))
			{
				// Construct the command to send to redis
				RedisMessage globalMessage = new RedisMessage(_serverName,

				sender.getName(),

				adminMessage ? null : friend.ServerName,

				playerTarget,

				message,

				// Include the sender's rank if this is a admin message. So we can format the receivers chat.
						adminMessage ? F.rank(_clientManager.Get(sender).GetRank()) : null);

				final UUID uuid = globalMessage.getUUID();

				// A backup for the rare case where the message fails to deliver. Server doesn't respond
				BukkitRunnable runnable = new BukkitRunnable()
				{
					public void run()
					{
						_messageTimeouts.remove(uuid);

						// Inform the player that the message failed to deliver
						UtilPlayer.message(
								sender,
								F.main((adminMessage ? "Admin " : "") + "Message", C.mBody + " Failed to send message to ["
										+ C.mElem + playerTarget + C.mBody + "]."));
					}
				};

				// This will activate in 2 seconds
				runnable.runTaskLater(getPlugin(), 40);

				// The key is the UUID its trading between servers
				_messageTimeouts.put(uuid, runnable);

				// Time to send the message!
				globalMessage.publish();
			}
		}
	}
}
