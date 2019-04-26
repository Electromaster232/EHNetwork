package mineplex.core.npc.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.npc.NpcManager;

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
