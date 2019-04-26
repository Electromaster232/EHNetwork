package mineplex.hub.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.MultiCommandBase;
import mineplex.core.common.Rank;
import mineplex.hub.HubManager;

public class NewsCommand extends MultiCommandBase<HubManager>
{	
	public NewsCommand(HubManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV}, "news");
		
		AddCommand(new NewsAddCommand(plugin));
		AddCommand(new NewsDeleteCommand(plugin));
		AddCommand(new NewsConfirmCommand(plugin));
		AddCommand(new NewsListCommand(plugin));
		AddCommand(new NewsSetCommand(plugin));
	}

	@Override
	protected void Help(Player caller, String args[])
	{
		Plugin.GetNewsManager().Help(caller);
	}
}
