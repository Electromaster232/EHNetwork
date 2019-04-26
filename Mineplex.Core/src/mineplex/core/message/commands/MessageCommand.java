package mineplex.core.message.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.message.MessageManager;

public class MessageCommand extends CommandBase<MessageManager>
{
    public MessageCommand(MessageManager plugin)
    {
        super(plugin, Rank.ALL, "m", "msg", "message", "tell", "t", "w", "whisper", "MSG");
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
            if (args.length == 0)
            {
                UtilPlayer.message(caller, F.main(Plugin.getName(), "Player argument missing."));
                return;
            }

            // Parse Message
            String message = "Beep!";
            if (args.length > 1)
            {
                message = F.combine(args, 1, null, false);
            }
            else
            {
                message = Plugin.GetRandomMessage();
            }

            Plugin.sendMessage(caller, args[0], message, false, false);
        }
    }
}
