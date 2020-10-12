package ehnetwork.core.command;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ehnetwork.core.common.Rank;

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
