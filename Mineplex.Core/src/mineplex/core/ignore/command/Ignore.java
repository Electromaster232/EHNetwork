package mineplex.core.ignore.command;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.ignore.IgnoreManager;

import org.bukkit.entity.Player;

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
