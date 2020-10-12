package ehnetwork.game.survival.commands;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.survival.SurvivalManager;

public class BuildRemoveCommand extends CommandBase<SurvivalManager>
{
	private SurvivalManager _survivalManager;
	public BuildRemoveCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[]{}, new String[]{"build-remove"});
		_survivalManager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Player player = _survivalManager.GetClients().Get(args[0]).GetPlayer();
		UtilPlayer.message(caller, F.main("Build", "Removed build for " + player.getName()));
		player.setGameMode(GameMode.SURVIVAL);
		player.playEffect(player.getLocation(), Effect.ITEM_BREAK, 1);
		UtilPlayer.message(player, F.main("Build", "An Admin has removed build mode for you."));
	}
}
