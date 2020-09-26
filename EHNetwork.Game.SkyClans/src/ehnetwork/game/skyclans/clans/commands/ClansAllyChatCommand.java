package ehnetwork.game.skyclans.clans.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.skyclans.clans.ClanInfo;
import ehnetwork.game.skyclans.clans.ClansManager;

public class ClansAllyChatCommand extends CommandBase<ClansManager>
{	
	public ClansAllyChatCommand(ClansManager plugin)
	{
		super(plugin, Rank.ALL, "ac");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0)
		{
			Plugin.Get(caller).setAllyChat(!Plugin.Get(caller).isAllyChat());
			UtilPlayer.message(caller, F.main("Clans", "Ally Chat: " + F.oo(Plugin.Get(caller).isAllyChat())));
			return;
		}

		//Single Clan
		if (!Plugin.Get(caller).isAllyChat())
		{
			ClanInfo clan = Plugin.getClanUtility().getClanByPlayer(caller);
			if (clan == null)	UtilPlayer.message(caller, F.main("Clans", "You are not in a Clan."));
			else				Plugin.chatAlly(clan, caller, F.combine(args, 0, null, false));
		}
	}
}
