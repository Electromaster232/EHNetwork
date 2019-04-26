package mineplex.core.command;

import java.util.Collection;
import java.util.List;

import mineplex.core.common.Rank;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommand
{
	void SetCommandCenter(CommandCenter commandCenter);
	void Execute(Player caller, String[] args);

	Collection<String> Aliases();

	void SetAliasUsed(String name);

	Rank GetRequiredRank();
	Rank[] GetSpecificRanks();

	List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args);
}
