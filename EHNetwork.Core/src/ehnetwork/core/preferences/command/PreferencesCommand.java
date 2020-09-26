package ehnetwork.core.preferences.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.preferences.PreferencesManager;

public class PreferencesCommand extends CommandBase<PreferencesManager>
{
	public PreferencesCommand(PreferencesManager plugin)
	{
		super(plugin, Rank.ALL, "prefs");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.openShop(caller);
	}
}
