package mineplex.core.npc.command;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.npc.NpcManager;

public class ClearCommand extends CommandBase<NpcManager>
{
	public ClearCommand(NpcManager plugin)
	{
		super(plugin, Rank.DEVELOPER, "clear");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null)
			Plugin.help(caller);
		else
		{
			try
			{
				Plugin.clearNpcs(true);

				UtilPlayer.message(caller, F.main(Plugin.getName(), "Cleared NPCs."));
			}
			catch (SQLException e)
			{
				Plugin.help(caller, "Database error.");
			}
		}
	}
}
