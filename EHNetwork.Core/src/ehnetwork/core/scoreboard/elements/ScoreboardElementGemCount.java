package ehnetwork.core.scoreboard.elements;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.scoreboard.ScoreboardManager;

public class ScoreboardElementGemCount extends ScoreboardElement
{
	@Override
	public ArrayList<String> GetLines(ScoreboardManager manager, Player player)
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add(manager.getDonation().Get(player).GetBalance(CurrencyType.Gems) + "");
		return output;
	}
}
