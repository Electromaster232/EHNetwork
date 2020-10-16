package ehnetwork.game.survival.commands;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.game.survival.SurvivalManager;


public class DoubleJumpCommand extends CommandBase<SurvivalManager> implements Listener
{
	private final SurvivalManager _plugin;

	public DoubleJumpCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.LEGEND, new Rank[]{}, "launch","l");
		_plugin = plugin;
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean add = _plugin.getJumpManager().checkPlayer().contains(caller);
		if(!add)
		{
			_plugin.getJumpManager().addPlayer(caller);
			caller.setAllowFlight(true);
		}
		else
		{
			_plugin.getJumpManager().delPlayer(caller);
			caller.setAllowFlight(false);
		}
		caller.sendMessage(F.main("Jump", "Double jump has been toggled!"));
	}
}