package ehnetwork.game.arcade.game.games.horsecharge;

import java.util.HashMap;

import org.bukkit.ChatColor;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.horsecharge.kits.KitDefenceArcher;
import ehnetwork.game.arcade.game.games.horsecharge.kits.KitHorseKnight;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.NullKit;

public class Horse extends TeamGame
{
	public Horse(ArcadeManager manager, HashMap<String, ChatColor> pastTeams)
	{
		super(manager, GameType.Horse,

				new Kit[] 
						{ 
				new KitHorseKnight(manager),
				new NullKit(manager),
				new KitDefenceArcher(manager),
						},

						new String[]
								{
				"Horsemen must charge the ruins",
				"Horsemen win if they rid the ruins of Undead.",
				"",
				"Undead must defend the ruins",
				"Undead win when all Horsemen are dead.",
				"",
				"Teams swap after game is over"
								});
	}
	
	@Override
	public void ParseData()
	{

	}
	
	@Override
	public void RestrictKits()
	{
		for (Kit kit : GetKits())
		{
			for (GameTeam team : GetTeamList())
			{
				if (team.GetColor() == ChatColor.RED)
				{
					if (kit.GetName().contains("Defence"))
						team.GetRestrictedKits().add(kit);
				}
				else
				{
					if (kit.GetName().contains("Horseback"))
						team.GetRestrictedKits().add(kit);
				}
			}
		}
	}
}
