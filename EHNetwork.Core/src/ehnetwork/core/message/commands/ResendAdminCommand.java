package ehnetwork.core.message.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.message.MessageManager;

public class ResendAdminCommand extends CommandBase<MessageManager>
{
	public ResendAdminCommand(MessageManager plugin)
	{
		super(plugin, Rank.ALL, "ra");
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
			if (!Plugin.GetClientManager().Get(caller).GetRank().Has(caller, Rank.HELPER, true))
				return;

            String lastTo = Plugin.Get(caller).LastAdminTo;

            // Get To
            if (lastTo == null)
            {
                UtilPlayer.message(caller, F.main(Plugin.getName(), "You have not admin messaged anyone recently."));
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

            Plugin.sendMessage(caller, lastTo, message, true, true);
		}
	}
}
