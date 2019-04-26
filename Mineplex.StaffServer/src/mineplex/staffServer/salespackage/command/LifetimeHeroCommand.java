package mineplex.staffServer.salespackage.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.staffServer.salespackage.SalesPackageManager;

public class LifetimeHeroCommand extends CommandBase<SalesPackageManager>
{
	public LifetimeHeroCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "lifetimehero");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales rank " + args[0] + " HERO true"));
		
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales coin " + args[0] + " 30000"));
	}
}
