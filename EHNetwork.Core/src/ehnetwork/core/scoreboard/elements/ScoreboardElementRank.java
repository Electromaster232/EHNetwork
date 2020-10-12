package ehnetwork.core.scoreboard.elements;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import ehnetwork.core.common.Rank;
import ehnetwork.core.scoreboard.ScoreboardManager;

public class ScoreboardElementRank extends ScoreboardElement
{
	@Override
	public ArrayList<String> GetLines(ScoreboardManager manager, Player player)
	{
		ArrayList<String> output = new ArrayList<String>();

		if (manager.getClients().Get(player).GetRank().Has(Rank.ULTRA))
		{
			output.add(manager.getClients().Get(player).GetRank().Name);
		}
		else if (manager.getDonation().Get(player.getName()).OwnsUnknownPackage("SuperSmashMobs ULTRA") ||
				manager.getDonation().Get(player.getName()).OwnsUnknownPackage("Survival Games ULTRA") ||
				manager.getDonation().Get(player.getName()).OwnsUnknownPackage("Minigames ULTRA") ||
				manager.getDonation().Get(player.getName()).OwnsUnknownPackage("CastleSiege ULTRA") ||
				manager.getDonation().Get(player.getName()).OwnsUnknownPackage("Champions ULTRA"))
		{
			output.add("Single Ultra");
		}
		else
		{
			output.add("No Rank");
		}
		
		return output;
	}

}
