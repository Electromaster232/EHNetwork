package ehnetwork.game.survival.commands;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.survival.SurvivalManager;


public class CosmeticsCommand extends CommandBase<SurvivalManager>
{
	private SurvivalManager _manager;
	public CosmeticsCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.ALL, new Rank[]{}, new String[]{"cosmetics"});
		_manager = plugin;
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		if(caller.getGameMode() == GameMode.SURVIVAL){
			UtilPlayer.message(caller, F.main("Game", "Cosmetics Enabled."));
			caller.setGameMode(GameMode.ADVENTURE);
			_manager.getCosmeticManager().enableItemsForPlayer(caller);
		}
		else if(caller.getGameMode() == GameMode.ADVENTURE){
			UtilPlayer.message(caller, F.main("Game", "Cosmetics Disabled."));
			caller.setGameMode(GameMode.SURVIVAL);
			_manager.getCosmeticManager().disableItemsForPlayer(caller);
		}
	}
}
