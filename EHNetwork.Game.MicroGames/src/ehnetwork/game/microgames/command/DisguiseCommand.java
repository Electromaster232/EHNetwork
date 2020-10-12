package ehnetwork.game.microgames.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.ProfileLoader;
import ehnetwork.core.common.util.UUIDFetcher;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguisePlayer;
import ehnetwork.game.microgames.MicroGamesManager;

public class DisguiseCommand extends CommandBase<MicroGamesManager>
{
	public DisguiseCommand(MicroGamesManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.YOUTUBE, Rank.TWITCH, Rank.JNR_DEV}, "disguise");
	}

	@Override
	public void Execute(final Player caller, final String[] args)
	{
		if (args.length == 0)
		{
			UtilPlayer.message(caller, C.cRed + C.Bold + "/disguise <name>");
			return;
		}
		
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable()
		{
			@Override
			public void run() 
			{
				try
				{
					final GameProfile profile = new ProfileLoader(UUIDFetcher.getUUIDOf(args[0]).toString(), args[0]).loadProfile();
					
					Bukkit.getServer().getScheduler().runTask(Plugin.getPlugin(), new Runnable()
					{
						public void run()
						{
							DisguisePlayer playerDisguise = new DisguisePlayer(caller, profile);
							Plugin.GetDisguise().disguise(playerDisguise);
							
							UtilPlayer.message(caller, C.cGreen + C.Bold + "Disguise Active: " + ChatColor.RESET + args[0]);
						}
					});
				}
				catch (Exception e)
				{
					UtilPlayer.message(caller, C.cRed + C.Bold + "Invalid Disguise Name: " + ChatColor.RESET + args[0]);
					return;
				}
			}} 
		);
	}
}
