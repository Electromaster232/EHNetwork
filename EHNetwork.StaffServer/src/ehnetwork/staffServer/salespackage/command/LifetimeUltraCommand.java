package ehnetwork.staffServer.salespackage.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class LifetimeUltraCommand extends CommandBase<SalesPackageManager>
{
	public LifetimeUltraCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "lifetimeultra");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales rank " + args[0] + " ULTRA true"));
		
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales coin " + args[0] + " 15000"));
	}
}
