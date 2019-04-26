package mineplex.core.npc.command;

import org.bukkit.entity.Player;

import mineplex.core.command.MultiCommandBase;
import mineplex.core.common.Rank;
import mineplex.core.npc.NpcManager;

public class NpcCommand extends MultiCommandBase<NpcManager>
{
	public NpcCommand(NpcManager plugin)
	{
		super(plugin, Rank.DEVELOPER, new Rank[] {Rank.JNR_DEV}, "npc");

		AddCommand(new AddCommand(plugin));
		AddCommand(new DeleteCommand(plugin));
		AddCommand(new HomeCommand(plugin));
		AddCommand(new ClearCommand(plugin));
		AddCommand(new RefreshCommand(plugin));
	}

	@Override
	protected void Help(Player caller, String args[])
	{
		Plugin.help(caller);
	}
}
