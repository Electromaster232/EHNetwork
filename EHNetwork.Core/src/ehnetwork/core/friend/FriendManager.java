package ehnetwork.core.friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniDbClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.jsonchat.ChildJsonMessage;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.friend.command.AddFriend;
import ehnetwork.core.friend.command.DeleteFriend;
import ehnetwork.core.friend.command.FriendsDisplay;
import ehnetwork.core.friend.data.FriendData;
import ehnetwork.core.friend.data.FriendRepository;
import ehnetwork.core.friend.data.FriendStatus;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class FriendManager extends MiniDbClientPlugin<FriendData>
{
    private static FriendSorter _friendSorter = new FriendSorter();

    private PreferencesManager _preferenceManager;
    private FriendRepository _repository;
    private Portal _portal;

    public FriendManager(JavaPlugin plugin, CoreClientManager clientManager, PreferencesManager preferences, Portal portal)
    {
        super("Friends", plugin, clientManager);

        _preferenceManager = preferences;
        _repository = new FriendRepository(plugin);
        _portal = portal;
    }

    public PreferencesManager getPreferenceManager()
    {
        return _preferenceManager;
    }

    public Portal getPortal()
    {
        return _portal;
    }

    @Override
    public void addCommands()
    {
        addCommand(new AddFriend(this));
        addCommand(new DeleteFriend(this));
        addCommand(new FriendsDisplay(this));
    }

    @Override
    protected FriendData AddPlayer(String player)
    {
        return new FriendData();
    }

    @EventHandler
    public void updateFriends(UpdateEvent event)
    {
        if (event.getType() != UpdateType.SLOW || Bukkit.getOnlinePlayers().size() == 0)
            return;

        final Player[] onlinePlayers = UtilServer.getPlayers();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(_plugin, new Runnable()
        {
            public void run()
            {
                final NautHashMap<String, FriendData> newData = _repository.getFriendsForAll(onlinePlayers);

                Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
                {
                    public void run()
                    {
                        for (Player player : Bukkit.getOnlinePlayers())
                        {
                            if (newData.containsKey(player.getUniqueId().toString()))
                            {
                                Get(player).setFriends(newData.get(player.getUniqueId().toString()).getFriends());
                            }
                            else
                            {
                                Get(player).getFriends().clear();
                            }
                        }
                    }
                });
            }
        });
    }

    public void addFriend(final Player caller, final String name)
    {
        if (caller.getName().equalsIgnoreCase(name))
        {
            caller.sendMessage(F.main(getName(), ChatColor.GRAY + "You cannot add yourself as a friend"));
            return;
        }

        boolean update = false;
        for (FriendStatus status : Get(caller).getFriends())
        {
            if (status.Name.equalsIgnoreCase(name))
            {
                if (status.Status == FriendStatusType.Pending || status.Status == FriendStatusType.Blocked)
                {
                    update = true;
                    break;
                }
                else if (status.Status == FriendStatusType.Denied)
                {
                    caller.sendMessage(F.main(getName(), ChatColor.GREEN + name + ChatColor.GRAY
                            + " has denied your friend request."));
                    return;
                }
                else if (status.Status == FriendStatusType.Accepted)
                {
                    caller.sendMessage(F.main(getName(), "You are already friends with " + ChatColor.GREEN + name));
                    return;
                }
                else if (status.Status == FriendStatusType.Sent)
                {
                    caller.sendMessage(F.main(getName(), ChatColor.GREEN + name + ChatColor.GRAY
                            + " has yet to respond to your friend request."));
                    return;
                }
            }
        }

        final boolean updateFinal = update;

        Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
        {
            public void run()
            {
                if (updateFinal)
                {
                    _repository.updateFriend(caller.getName(), name, "Accepted");
                    _repository.updateFriend(name, caller.getName(), "Accepted");

                    Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
                    {
                        public void run()
                        {
                            for (Iterator<FriendStatus> statusIterator = Get(caller).getFriends().iterator(); statusIterator
                                    .hasNext();)
                            {
                                FriendStatus status = statusIterator.next();

                                if (status.Name.equalsIgnoreCase(name))
                                {
                                    status.Status = FriendStatusType.Accepted;
                                    break;
                                }
                            }
                        }
                    });
                }
                else
                {
                    _repository.addFriend(caller, name);

                    Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
                    {
                        public void run()
                        {
                            for (Iterator<FriendStatus> statusIterator = Get(caller).getFriends().iterator(); statusIterator
                                    .hasNext();)
                            {
                                FriendStatus status = statusIterator.next();

                                if (status.Name.equalsIgnoreCase(name))
                                {
                                    status.Status = FriendStatusType.Sent;
                                    break;
                                }
                            }
                        }
                    });
                }

                Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
                {
                    public void run()
                    {
                        if (updateFinal)
                            caller.sendMessage(F.main(getName(), "You and " + ChatColor.GREEN + name + ChatColor.GRAY
                                    + " are now friends!"));
                        else
                            caller.sendMessage(F.main(getName(), "Added " + ChatColor.GREEN + name + ChatColor.GRAY
                                    + " to your friends list!"));
                    }
                });
            }
        });
    }

    public void removeFriend(final Player caller, final String name)
    {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
        {
            public void run()
            {
                _repository.removeFriend(caller.getName(), name);
                _repository.removeFriend(name, caller.getName());

                Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
                {
                    public void run()
                    {
                        for (Iterator<FriendStatus> statusIterator = Get(caller).getFriends().iterator(); statusIterator
                                .hasNext();)
                        {
                            FriendStatus status = statusIterator.next();

                            if (status.Name.equalsIgnoreCase(name))
                            {
                                status.Status = FriendStatusType.Blocked;
                                break;
                            }
                        }

                        caller.sendMessage(F.main(getName(), "Deleted " + ChatColor.GREEN + name + ChatColor.GRAY
                                + " from your friends list!"));
                    }
                });
            }
        });
    }

    public void showFriends(Player caller)
    {
        boolean isStaff = ClientManager.Get(caller).GetRank().Has(Rank.HELPER);
        boolean gotAFriend = false;
        List<FriendStatus> friendStatuses = Get(caller).getFriends();
        Collections.sort(friendStatuses, _friendSorter);

        caller.sendMessage(C.cAqua + C.Strike + "======================[" + ChatColor.RESET + C.cWhite + C.Bold + "Friends"
                + ChatColor.RESET + C.cAqua + C.Strike + "]======================");

        ArrayList<ChildJsonMessage> sentLines = new ArrayList<ChildJsonMessage>();
        ArrayList<ChildJsonMessage> pendingLines = new ArrayList<ChildJsonMessage>();
        ArrayList<ChildJsonMessage> onlineLines = new ArrayList<ChildJsonMessage>();
        ArrayList<ChildJsonMessage> offlineLines = new ArrayList<ChildJsonMessage>();

        for (FriendStatus friend : friendStatuses)
        {
            if (friend.Status == FriendStatusType.Blocked || friend.Status == FriendStatusType.Denied)
                continue;

            if (!_preferenceManager.Get(caller).PendingFriendRequests && friend.Status == FriendStatusType.Pending)
                continue;

            gotAFriend = true;

            ChildJsonMessage message = new JsonMessage("").color("white").extra("").color("white");

            if (friend.Status == FriendStatusType.Accepted)
            {
                // Online Friend
                if (friend.Online)
                {
                    if (friend.ServerName.contains("Staff") || friend.ServerName.contains("CUST"))
                    {
                        if (isStaff && friend.ServerName.contains("Staff"))
                            message.add("Teleport").color("green").bold().click("run_command", "/server " + friend.ServerName)
                                    .hover("show_text", "Teleport to " + friend.Name + "'s server.");
                        else
                            message.add("No Teleport").color("yellow").bold();
                    }
                    else
                        message.add("Teleport").color("green").bold().click("run_command", "/server " + friend.ServerName)
                                .hover("show_text", "Teleport to " + friend.Name + "'s server.");

                    message.add(" - ").color("white");
                    message.add("Delete").color("red").bold().click("run_command", "/unfriend " + friend.Name)
                            .hover("show_text", "Remove " + friend.Name + " from your friends list.");
                    message.add(" - ").color("white");
                    message.add(friend.Name).color(friend.Online ? "green" : "gray");
                    message.add(" - ").color("white");

                    if (friend.ServerName.contains("Staff") || friend.ServerName.contains("CUST"))
                    {
                        if (isStaff && friend.ServerName.contains("Staff"))
                            message.add(friend.ServerName).color("dark_green");
                        else
                            message.add("Private Staff Server").color("dark_green");
                    }
                    else
                        message.add(friend.ServerName).color("dark_green");

                    onlineLines.add(message);
                }
                // Offline Friend
                else
                {
                    message.add("Delete").color("red").bold().click("run_command", "/unfriend " + friend.Name)
                            .hover("show_text", "Remove " + friend.Name + " from your friends list.");
                    message.add(" - ").color("white");
                    message.add(friend.Name).color(friend.Online ? "green" : "gray");
                    message.add(" - ").color("white");
                    message.add("Offline for ").color("gray").add(UtilTime.MakeStr(friend.LastSeenOnline)).color("gray");

                    offlineLines.add(message);
                }
            }
            // Pending
            else if (friend.Status == FriendStatusType.Pending)
            {
                message.add("Accept").color("green").bold().click("run_command", "/friend " + friend.Name)
                        .hover("show_text", "Accept " + friend.Name + "'s friend request.");
                message.add(" - ").color("white");
                message.add("Deny").color("red").bold().click("run_command", "/unfriend " + friend.Name)
                        .hover("show_text", "Deny " + friend.Name + "'s friend request.");
                message.add(" - ").color("white");
                message.add(friend.Name + " Requested Friendship").color("gray");

                pendingLines.add(message);
            }
            // Sent
            else if (friend.Status == FriendStatusType.Sent)
            {
                message.add("Cancel").color("red").bold().click("run_command", "/unfriend " + friend.Name)
                        .hover("show_text", "Cancel friend request to " + friend.Name);
                message.add(" - ").color("white");
                message.add(friend.Name + " Friendship Request").color("gray");

                sentLines.add(message);
            }
        }

        // Send In Order
        for (JsonMessage msg : sentLines)
            msg.sendToPlayer(caller);

        for (JsonMessage msg : offlineLines)
            msg.sendToPlayer(caller);

        for (JsonMessage msg : pendingLines)
            msg.sendToPlayer(caller);

        for (JsonMessage msg : onlineLines)
            msg.sendToPlayer(caller);

        if (!gotAFriend)
        {
            caller.sendMessage(" ");
            caller.sendMessage("Welcome to your Friends List!");
            caller.sendMessage(" ");
            caller.sendMessage("To add friends, type " + C.cGreen + "/friend <Player Name>");
            caller.sendMessage(" ");
            caller.sendMessage("Type " + C.cGreen + "/friend" + ChatColor.RESET + " at any time to interact with your friends!");
            caller.sendMessage(" ");
        }

        ChildJsonMessage message = new JsonMessage("").extra(C.cAqua + C.Strike + "======================");

        message.add(C.cDAqua + "Toggle GUI").click("run_command", "/friendsdisplay");

        message.hover("show_text", C.cAqua + "Toggle friends to display in a inventory");

        message.add(C.cAqua + C.Strike + "======================");

        message.sendToPlayer(caller);
    }

    public boolean isFriends(Player player, String friend)
    {
        FriendData friendData = Get(player);

        for (FriendStatus friendStatus : friendData.getFriends())
        {
            if (friendStatus.Name.equalsIgnoreCase(friend))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
    {
        Set(playerName, _repository.loadClientInformation(resultSet));
    }

    @Override
    public String getQuery(int accountId, String uuid, String name)
    {
        return "SELECT tA.Name, status, tA.lastLogin, now() FROM accountFriend INNER Join accounts AS fA ON fA.uuid = uuidSource INNER JOIN accounts AS tA ON tA.uuid = uuidTarget WHERE uuidSource = '"
                + uuid + "';";
    }
}
