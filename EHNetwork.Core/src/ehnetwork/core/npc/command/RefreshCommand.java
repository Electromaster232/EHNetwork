package ehnetwork.core.npc.command;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.npc.NpcManager;

public class RefreshCommand extends CommandBase<NpcManager>
{
	public RefreshCommand(NpcManager plugin)
	{
		super(plugin, Rank.SNR_MODERATOR, "refresh");
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
				Plugin.clearNpcs(false);
				Plugin.loadNpcs();

				UtilPlayer.message(caller, F.main(Plugin.getName(), "Refreshed NPCs."));
			}
			catch (SQLException e)
			{
				Plugin.help(caller, "Database error.");
			}
		}
	}
}
