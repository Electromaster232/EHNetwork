package mineplex.core.scoreboard.elements;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import mineplex.core.scoreboard.ScoreboardManager;

public abstract class ScoreboardElement
{
	public abstract ArrayList<String> GetLines(ScoreboardManager manager, Player player);
}
