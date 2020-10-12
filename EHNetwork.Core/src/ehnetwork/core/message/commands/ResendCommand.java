package ehnetwork.core.message.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.message.MessageManager;

public class ResendCommand extends CommandBase<MessageManager>
{
    public ResendCommand(MessageManager plugin)
    {
        super(plugin, Rank.ALL, "r");
    }

    @Override
    public void Execute(Player caller, String[] args)
    {
        if (args == null)
        {
            Plugin.Help(caller);
        }
        else
        {
            String lastTo = Plugin.Get(caller).LastTo;

            // Get To
            if (lastTo == null)
            {
                UtilPlayer.message(caller, F.main(Plugin.getName(), "You have not messaged anyone recently."));
                return;
            }

            // Parse Message
            String message = "Beep!";
            if (args.length > 0)
            {
                message = F.combine(args, 0, null, false);
            }
            else
            {
                message = Plugin.GetRandomMessage();
            }

            Plugin.sendMessage(caller, lastTo, message, true, false);
        }
    }
}
