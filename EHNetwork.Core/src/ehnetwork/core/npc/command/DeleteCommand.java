package ehnetwork.core.npc.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.npc.NpcManager;

public class DeleteCommand extends CommandBase<NpcManager>
{
	public DeleteCommand(NpcManager plugin)
	{
		super(plugin, Rank.DEVELOPER, "del");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null)
			Plugin.help(caller);
		else
		{
			Plugin.prepDeleteNpc(caller);

			UtilPlayer.message(caller, F.main(Plugin.getName(), "Now right click npc."));
		}
	}
}
