package mineplex.core.preferences.command;

import org.bukkit.entity.Player;

import mineplex.core.chat.Chat;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.UtilServer;
import mineplex.core.preferences.PreferencesManager;

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
