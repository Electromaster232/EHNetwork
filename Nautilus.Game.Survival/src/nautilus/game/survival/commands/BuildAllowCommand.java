package nautilus.game.survival.commands;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.survival.SurvivalManager;

public class BuildAllowCommand extends CommandBase<SurvivalManager>
{
	private SurvivalManager _survivalManager;
	public BuildAllowCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[]{}, new String[]{"build-allow"});
		_survivalManager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Player player = _survivalManager.GetClients().Get(args[0]).GetPlayer();
		UtilPlayer.message(caller, F.main("Build", "Authorized build for " + player.getName()));
		player.setGameMode(GameMode.CREATIVE);
		player.playEffect(player.getLocation(), Effect.HAPPY_VILLAGER, 1);
		UtilPlayer.message(player, F.main("Build", "An Admin has authorized build mode for you."));
	}
}