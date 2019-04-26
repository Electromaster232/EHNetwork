package mineplex.core.notifier;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class NotificationManager extends MiniPlugin
{
	private boolean _enabled = true;
	
	private CoreClientManager _clientManager;
	
	private String _summerLine = 
			C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + 
			C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + 
			C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + 
			C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + 
			C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + 
			C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█" + C.cGold + "█" + C.cYellow + "█";

	public NotificationManager(JavaPlugin plugin, CoreClientManager client) 
	{
		super("Notification Manager", plugin);
		
		_clientManager = client;
	}
	
	@EventHandler
	public void notify(UpdateEvent event)
	{
		if (!_enabled)
			return;
		
//		if (event.getType() == UpdateType.MIN_08)
//			hugeSale();
		
//		if (event.getType() == UpdateType.MIN_16)
//			sale();
	}

	private void sale() 
	{
		for (Player player : UtilServer.getPlayers())
		{
			Rank rank = _clientManager.Get(player).GetRank();
			
			if (rank.Has(Rank.LEGEND))
				continue;
			
			if (rank == Rank.ALL)
			{
				UtilPlayer.message(player, C.cWhite + "Summer Sale! " + " Purchase " + C.cAqua + C.Bold + "Ultra RANK" + C.cWhite + " for $15");
			}
			else if (rank == Rank.ULTRA)
			{
				UtilPlayer.message(player, C.cWhite + "Summer Sale! " + " Upgrade to " + C.cPurple + C.Bold + "HERO RANK" + C.cWhite + " for $15!");
			} 
			else if (rank == Rank.HERO)
			{
				UtilPlayer.message(player, C.cWhite + "Summer Sale! " + "Upgrade to " + C.cGreen + C.Bold + "LEGEND RANK" + C.cWhite + " for $15!");
			} 
			
			UtilPlayer.message(player, C.cWhite + " Visit " + F.link("discord.gg/FttmSEQ") + " for free Ranks!");
		}
	}
	
	private void hugeSale() 
	{
		for (Player player : UtilServer.getPlayers())
		{
			Rank rank = _clientManager.Get(player).GetRank();
			
			if (rank.Has(Rank.LEGEND))
				continue;
			
			UtilPlayer.message(player, _summerLine);
			UtilPlayer.message(player, " ");
			UtilPlayer.message(player, "          " + 
					C.cGreen + C.Bold + "75% OFF" + 
					C.cYellow + C.Bold + "  SUMMER SUPER SALE  " + 
					C.cGreen + C.Bold + "75% OFF");
			UtilPlayer.message(player, " ");
			
			if (rank == Rank.ALL)
			{
				UtilPlayer.message(player, C.cWhite + " " + player.getName() + ", you can get 75% Off " + C.cAqua + C.Bold + "All Lifetime Ranks" + C.cWhite + "!");
				UtilPlayer.message(player, C.cWhite + " This is our biggest sale ever, available " + C.cRed + C.Line + "this weekend only" +  C.cWhite + "!");
			}
			else if (rank == Rank.ULTRA)
			{
				UtilPlayer.message(player, C.cWhite + " Hello " + player.getName() + ", upgrade to " + C.cPurple + C.Bold + "HERO RANK" + C.cWhite + " for only $7.50!");
				UtilPlayer.message(player, C.cWhite + " This is our biggest sale ever, available " + C.cRed + C.Line + "this weekend only" +  C.cWhite + "!");
			} 
			else if (rank == Rank.HERO)
			{
				UtilPlayer.message(player, C.cWhite + " Hello " + player.getName() + ", upgrade to " + C.cGreen + C.Bold + "LEGEND RANK" + C.cWhite + " for only $7.50!");
				UtilPlayer.message(player, C.cWhite + " This is our biggest sale ever, available " + C.cRed + C.Line + "this weekend only" +  C.cWhite + "!");
			} 
			
			UtilPlayer.message(player, " ");
			UtilPlayer.message(player, "                         " + C.cGreen + "discord.gg/FttmSEQ");
			UtilPlayer.message(player, " ");
			//UtilPlayer.message(player, C.cRed + C.Bold + "                  This Weekend Only!");
			UtilPlayer.message(player, _summerLine);
		}
	}
}
