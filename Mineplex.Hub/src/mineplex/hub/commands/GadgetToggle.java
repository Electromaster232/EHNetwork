package mineplex.hub.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.hub.HubManager;

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
