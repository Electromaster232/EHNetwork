package ehnetwork.game.skyclans.fields.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.skyclans.fields.FieldOre;

public class FieldOreCommand extends CommandBase<FieldOre>
{
	public FieldOreCommand(FieldOre plugin)
	{
		super(plugin, Rank.ADMIN, "fo");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0)
		{
			Plugin.help(caller);
			return;
		}
		
		if (args[0].equalsIgnoreCase("toggle"))
		{
			if (!Plugin.getActivePlayers().remove(caller.getName()))
				Plugin.getActivePlayers().add(caller.getName());
			
			UtilPlayer.message(caller, F.main(Plugin.getName(), "Interact Active: " + F.tf(Plugin.getActivePlayers().contains(caller.getName()))));
		}
		
		else if (args[0].equalsIgnoreCase("help"))
		{
			Plugin.help(caller);
		}
		
		else if (args[0].equalsIgnoreCase("reset"))
		{
			Plugin.reset(caller);
		}
		
		else if (args[0].equalsIgnoreCase("fill"))
		{
			Plugin.fill(caller);
		}
		
		else if (args[0].equalsIgnoreCase("list"))
		{
			Plugin.list(caller);
		}
		
		else if (args[0].equalsIgnoreCase("wipe"))
		{
			Plugin.wipe(caller);
		}
	}
}
