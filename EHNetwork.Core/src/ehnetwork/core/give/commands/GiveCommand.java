package ehnetwork.core.give.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.give.Give;

public class GiveCommand extends CommandBase<Give>
{
    public GiveCommand(Give plugin)
    {
        super(plugin, Rank.ADMIN, "give", "g", "item", "i");
    }

    @Override
    public void Execute(final Player caller, final String[] args)
    {
       Plugin.parseInput(caller, args);
    }
}
