package mineplex.staffServer.salespackage.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.staffServer.salespackage.SalesPackageManager;

public class UltraCommand extends CommandBase<SalesPackageManager>
{
	public UltraCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "ultra");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales rank " + args[0] + " ULTRA false"));
		
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales coin " + args[0] + " 7500"));
		
		resetCommandCharge(caller);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(caller, "/sales booster " + args[0] + " 30"));
	}
}
