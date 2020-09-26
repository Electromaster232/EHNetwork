package ehnetwork.core.ignore.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.ignore.IgnoreManager;

public class Ignore extends CommandBase<IgnoreManager>
{
    public Ignore(IgnoreManager plugin)
    {
        super(plugin, Rank.ALL, "ignore");
    }

    @Override
    public void Execute(final Player caller, final String[] args)
    {
        if (args == null)
        {
            Plugin.showIgnores(caller);
        }
        else
        {
            CommandCenter.GetClientManager().checkPlayerName(caller, args[0], new Callback<String>()
            {
                public void run(String result)
                {
                    if (result != null)
                    {
                        Plugin.addIgnore(caller, result);
                    }
                }
            });
        }
    }
}
