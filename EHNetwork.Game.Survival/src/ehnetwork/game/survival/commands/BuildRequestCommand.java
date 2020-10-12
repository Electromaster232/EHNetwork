package ehnetwork.game.survival.commands;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.survival.SurvivalManager;

public class BuildRequestCommand extends CommandBase<SurvivalManager>
{
	private SurvivalManager _survivalManager;
	public BuildRequestCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.ALL, new Rank[]{}, new String[]{"build"});
		_survivalManager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		for(Player player :_survivalManager.getPlugin().getServer().getOnlinePlayers()){
			if (_survivalManager.GetClients().hasRank(player, Rank.ADMIN)){
				UtilPlayer.message(player, F.main("Build", "User " + caller.getName() + " has requested build access."));
				player.playEffect(player.getLocation(), Effect.HAPPY_VILLAGER, 1);
			}
		}
		UtilPlayer.message(caller, F.main("Build", "A request has been sent to all online Admins for build access."));
	}

}