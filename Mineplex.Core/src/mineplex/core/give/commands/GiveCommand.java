package mineplex.core.give.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.give.Give;

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
