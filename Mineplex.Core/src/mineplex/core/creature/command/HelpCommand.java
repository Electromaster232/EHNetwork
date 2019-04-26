package mineplex.core.creature.command;
import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.creature.Creature;

public class HelpCommand extends CommandBase<Creature>
{
	public HelpCommand(Creature plugin)
	{
		super(plugin, Rank.ADMIN, "help");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		UtilPlayer.message(caller, F.main(Plugin.getName(), "Commands List;"));
		UtilPlayer.message(caller, F.help("/mob", "List Entities", Rank.MODERATOR));
		UtilPlayer.message(caller, F.help("/mob kill <Type>", "Remove Entities of Type", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/mob <Type> (# baby lock angry s# <Prof>)", "Create", Rank.ADMIN));
		UtilPlayer.message(caller, F.desc("Professions", "Butcher, Blacksmith, Farmer, Librarian, Priest"));
	}
}
