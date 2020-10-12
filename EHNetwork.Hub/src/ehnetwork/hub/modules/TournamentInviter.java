package ehnetwork.hub.modules;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.hub.HubManager;

public class TournamentInviter extends MiniPlugin
{
	private String[] invitees = new String[] 
			{
			"Bluestone_FTW", 
			"adrien5d", 
			"zed0155", 
			"SubSonicDPS", 
			"charger134", 
			"rensy69", 
			"ghikft", 
			"ParaJon", 
			"Mmmmhbeans", 
			"Zilcor", 
			"kwcd", 
			"orangeguyy", 
			"kingcamas", 
			"TheHyperAsian", 
			"BossesCraftCake", 
			"Shadowsnippp3r", 
			"MySquishyTurtle", 
			"pyrodamage", 
			"Blazespot1", 
			"WiiTarted", 
			"Ghostgunner97", 
			"bbran21", 
			"StudlyWafflez", 
			"FrozenAodC", 
			"PikaBoyCraft", 
			"NoNowGetOut", 
			"CandleBlob", 
			"sad6boy", 
			"RacgiMan", 
			"stafford9", 
			"Wincraft12", 
			"13en2000",
			};

	private String[] backups = new String[] 
			{
			"DeCouto_05", 
			"krusher430", 
			"duble11", 
			"IcyCaress", 
			"HeroG_ruha", 
			"TheCherry_Guy", 
			"Infernova86", 
			"epicswords02", 
			"lion2x", 
			"Brandon_Nish", 
			"LTplaysminecraft", 
			"BootFruit", 
			"Markus4445", 
			"jamescrafts8", 
			"iamlinked", 
			"axle1313", 
			"KainFTW", 
			"CreeperRain", 
			"thomasjinksybean", 
			"GamerFletch", 
			"Thepiggyassassin", 
			"Patu2010", 
			"Flying_pigglet", 
			"PowerMovingBacca", 
			"arduent", 
			"Aubble", 
			"MattaTackk22", 
			"boomdigity102", 
			"Creeper2341455", 
			"Amazing105",
			};

	public TournamentInviter(HubManager manager)
	{
		super("Map Manager", manager.getPlugin());
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		for (String name : invitees)
		{
			if (!name.equalsIgnoreCase(event.getPlayer().getName()))
				continue;

			inviteMessage(event.getPlayer());
		}

		for (String name : backups)
		{
			if (!name.equalsIgnoreCase(event.getPlayer().getName()))
				continue;

			backupMessage(event.getPlayer());
		}
	}

	private void inviteMessage(Player player)
	{
		UtilPlayer.message(player, C.cBlue + C.Strike + "----------------------------------------------------");
		UtilPlayer.message(player, C.cAqua + C.Bold + "                   Fall Invitational");
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cWhite + "Congratulations! You have earned an invite to the final event");
		UtilPlayer.message(player, C.cWhite + "for the 2014 Fall Invitational on September 27 at 3pm EST!");
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cWhite + "Please confirm that you can attend at this link;");
		UtilPlayer.message(player, C.cGreen + C.Line + "www.tinyurl.com/fallinvitational"); 
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cBlue + C.Strike + "----------------------------------------------------");
	}

	private void backupMessage(Player player)
	{
		UtilPlayer.message(player, C.cBlue + C.Strike + "----------------------------------------------------");
		UtilPlayer.message(player, C.cAqua + C.Bold + "           Fall Invitational");
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cWhite + "You have earned a backup invite to the final event for the");
		UtilPlayer.message(player, C.cWhite + "2014 Fall Invitational on September 27 at 3pm EST!");
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cWhite + "Please confirm that you can attend at this link;");
		UtilPlayer.message(player, C.cGreen + C.Line + "www.tinyurl.com/fallinvitational");
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cBlue + C.Strike + "----------------------------------------------------");
	}
}
