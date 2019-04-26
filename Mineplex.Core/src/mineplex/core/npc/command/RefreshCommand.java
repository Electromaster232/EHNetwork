package mineplex.core.npc.command;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.npc.NpcManager;

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
