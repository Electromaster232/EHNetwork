package mineplex.core.scoreboard.elements;

import java.util.ArrayList;

import mineplex.core.common.CurrencyType;
import mineplex.core.scoreboard.ScoreboardManager;

import org.bukkit.entity.Player;

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
