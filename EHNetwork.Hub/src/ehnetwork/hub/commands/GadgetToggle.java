package ehnetwork.hub.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.hub.HubManager;

public class GadgetToggle extends CommandBase<HubManager>
{
	public GadgetToggle(HubManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV}, new String[] {"gadget"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.ToggleGadget(caller);
	}
}
