package ehnetwork.core.ignore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniDbClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.jsonchat.ChildJsonMessage;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.ignore.command.Ignore;
import ehnetwork.core.ignore.command.Unignore;
import ehnetwork.core.ignore.data.IgnoreData;
import ehnetwork.core.ignore.data.IgnoreRepository;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;

public class IgnoreManager extends MiniDbClientPlugin<IgnoreData>
{
    private PreferencesManager _preferenceManager;
    private IgnoreRepository _repository;
    private Portal _portal;

    public IgnoreManager(JavaPlugin plugin, CoreClientManager clientManager, PreferencesManager preferences, Portal portal)
    {
        super("Ignore", plugin, clientManager);

        _preferenceManager = preferences;
        _repository = new IgnoreRepository(plugin);
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

    public boolean isIgnoring(Player caller, Player target)
    {
        return isIgnoring(caller, target.getName());
    }

    public boolean isIgnoring(Player caller, String target)
    {
        IgnoreData data = Get(caller);

        for (String ignored : data.getIgnored())
        {
            if (ignored.equalsIgnoreCase(target))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addCommands()
    {
        addCommand(new Ignore(this));
        addCommand(new Unignore(this));
    }

    @Override
    protected IgnoreData AddPlayer(String player)
    {
        return new IgnoreData();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        if (ClientManager.Get(event.getPlayer()).GetRank().Has(Rank.HELPER))
            return;

        Iterator<Player> itel = event.getRecipients().iterator();

        while (itel.hasNext())
        {
            Player player = itel.next();

            IgnoreData info = Get(player);

            for (String ignored : info.getIgnored())
            {
                if (ignored.equalsIgnoreCase(event.getPlayer().getName()))
                {
                    itel.remove();

                    break;
                }
            }
        }
    }

    public void addIgnore(final Player caller, final String name)
    {
        if (caller.getName().equalsIgnoreCase(name))
        {
            caller.sendMessage(F.main(getName(), ChatColor.GRAY + "You cannot ignore yourself"));
            return;
        }

        for (String status : Get(caller).getIgnored())
        {
            if (status.equalsIgnoreCase(name))
            {
                caller.sendMessage(F.main(getName(), ChatColor.GREEN + name + ChatColor.GRAY + " has already been ignored."));
                return;

            }
        }

        IgnoreData ignoreData = Get(caller);

        if (ignoreData != null)
        {
            ignoreData.getIgnored().add(name);
        }

        Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
        {
            public void run()
            {
                _repository.addIgnore(caller, name);

                Bukkit.getServer().getScheduler().runTask(_plugin, new Runnable()
                {
                    public void run()
                    {
                        caller.sendMessage(F.main(getName(), "Now ignoring " + ChatColor.GREEN + name));
                    }
                });
            }
        });
    }

    public void removeIgnore(final Player caller, final String name)
    {
        IgnoreData ignoreData = Get(caller);

        if (ignoreData != null)
        {
            Iterator<String> itel = ignoreData.getIgnored().iterator();

            while (itel.hasNext())
            {
                String ignored = itel.next();

                if (ignored.equalsIgnoreCase(name))
                {
                    itel.remove();
                    break;
                }
            }
        }

        caller.sendMessage(F.main(getName(), "No longer ignoring " + ChatColor.GREEN + name + ChatColor.GRAY + "!"));

        Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
        {
            public void run()
            {
                _repository.removeIgnore(caller.getName(), name);
            }
        });
    }

    public void showIgnores(Player caller)
    {
        List<String> ignoredPlayers = Get(caller).getIgnored();

        caller.sendMessage(C.cAqua + C.Strike + "=====================[" + ChatColor.RESET + C.cWhite + C.Bold + "Ignoring"
                + ChatColor.RESET + C.cAqua + C.Strike + "]======================");

        ArrayList<ChildJsonMessage> sentLines = new ArrayList<ChildJsonMessage>();

        for (String ignored : ignoredPlayers)
        {

            ChildJsonMessage message = new JsonMessage("").color("white").extra("").color("white");

            message.add("Ignoring " + ignored).color("gray");

            message.add(" - ").color("white");

            message.add("Unignore").color("red").bold().click("run_command", "/unignore " + ignored)
                    .hover("show_text", "Stop ignoring " + ignored);

            sentLines.add(message);
        }

        // Send In Order
        for (JsonMessage msg : sentLines)
            msg.sendToPlayer(caller);

        if (sentLines.isEmpty())
        {
            caller.sendMessage(" ");
            caller.sendMessage("Welcome to your Ignore List!");
            caller.sendMessage(" ");
            caller.sendMessage("To ignore people, type " + C.cGreen + "/ignore <Player Name>");
            caller.sendMessage(" ");
            caller.sendMessage("Type " + C.cGreen + "/ignore" + ChatColor.RESET + " at any time to view the ignored!");
            caller.sendMessage(" ");
        }

        ChildJsonMessage message = new JsonMessage("").extra(C.cAqua + C.Strike
                + "=====================================================");

        message.sendToPlayer(caller);
    }

    @Override
    public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
    {
        Set(playerName, _repository.loadClientInformation(resultSet));
    }

    @Override
    public String getQuery(int accountId, String uuid, String name)
    {
        return "SELECT tA.Name FROM accountIgnore INNER Join accounts AS fA ON fA.uuid = uuidIgnorer INNER JOIN accounts AS tA ON tA.uuid = uuidIgnored LEFT JOIN playerMap ON tA.name = playerName WHERE uuidIgnorer = '"
                + uuid + "';";
    }
}
