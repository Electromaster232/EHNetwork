package ehnetwork.core.npc.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.npc.NpcManager;

public class HomeCommand extends CommandBase<NpcManager>
{
	public HomeCommand(NpcManager plugin)
	{
		super(plugin, Rank.DEVELOPER, new Rank[] {Rank.JNR_DEV}, "home");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null)
			Plugin.help(caller);
		else
		{
			Plugin.teleportNpcsHome();

			UtilPlayer.message(caller, F.main(Plugin.getName(), "Npcs teleported to home locations."));
		}
	}
}
