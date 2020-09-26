package ehnetwork.game.microgames.game.games.survivalgames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilTime.TimeUnit;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.GameTeam;

public class SoloSurvivalGames extends SurvivalGames
{
	
	private GameTeam _players;

	public SoloSurvivalGames(MicroGamesManager manager)
	{
		super(manager, GameType.SurvivalGames,
				new String[]
						{
				"Search for chests to find loot",

				"Slaughter your opponents",

				"Stay away from the borders!",

				"Last tribute alive wins!"
						});
		
		this.DamageTeamSelf = true;
		
	}
	
	@EventHandler
	public void CustomTeamGeneration(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Recruit)
			return;

		_players = GetTeamList().get(0);
		_players.SetColor(ChatColor.YELLOW);
		_players.SetName("Players");
		_players.setDisplayName(C.cYellow + C.Bold + "Players");
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (GetTeamList().isEmpty())
			return;

		Scoreboard.Reset();

		Scoreboard.WriteBlank();

		GameTeam team = GetTeamList().get(0);

		Scoreboard.Write(C.cGreen + C.Bold + "Time");
		Scoreboard.Write(UtilTime.convertString(this.getSecondsSinceStart() * 1000, 0, TimeUnit.FIT));

		Scoreboard.WriteBlank();

		Scoreboard.Write(C.cYellow + C.Bold + "Tributes");
		if (team.GetPlayers(true).size() > 7)
		{
			Scoreboard.Write("" + team.GetPlayers(true).size());
		}
		else
		{
			for (Player player : team.GetPlayers(true))
			{
				Scoreboard.Write(C.cWhite + player.getName());
			}
		}

		Scoreboard.WriteBlank();

		if (this.getChestRefillTime() > 0 && this.getDeathMatchTime() > 60)
		{
			Scoreboard.Write(C.cGold + C.Bold + "Chest Refill");
			Scoreboard.Write(UtilTime.convertString(this.getChestRefillTime() * 1000, 0, TimeUnit.FIT));
		}
		else if (this.getDeathMatchTime() > 0)
		{
			Scoreboard.Write(C.cRed + C.Bold + "Deathmatch");
			Scoreboard.Write(UtilTime.convertString(
					Math.min(this.getDeathMatchTime(), this.isDeathMatchTeleported() ? 10 : this.getDeathMatchTime()) * 1000, 0, TimeUnit.FIT));
		}
		else
		{
			Scoreboard.Write(C.cRed + C.Bold + "Game End");
			Scoreboard.Write(UtilTime.convertString(Math.max(0, this.getGameEndTime()) * 1000, 0, TimeUnit.FIT));
		}

		Scoreboard.Draw();
	}
	
	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		if (GetPlayers(true).size() <= 1)
		{	
			ArrayList<Player> places = GetTeamList().get(0).GetPlacements(true);
			
			//Announce
			AnnounceEnd(places);

			//Gems
			if (places.size() >= 1)
				AddGems(places.get(0), 20, "1st Place", false, false);

			if (places.size() >= 2)
				AddGems(places.get(1), 15, "2nd Place", false, false);

			if (places.size() >= 3)
				AddGems(places.get(2), 10, "3rd Place", false, false);

			for (Player player : GetPlayers(false))
				if (player.isOnline())
					AddGems(player, 10, "Participation", false, false);

			//End
			SetState(GameState.End);
		}
	}

	@Override
	public List<Player> getWinners()
	{
		if (GetState().ordinal() >= GameState.End.ordinal())
		{
			List<Player> places = GetTeamList().get(0).GetPlacements(true);

			if (places.isEmpty() || !places.get(0).isOnline())
				return Arrays.asList();
			else
				return Arrays.asList(places.get(0));
		}
		else
			return null;
	}

	@Override
	public List<Player> getLosers()
	{
		List<Player> winners = getWinners();

		if (winners == null)
			return null;

		List<Player> losers = GetTeamList().get(0).GetPlayers(false);

		losers.removeAll(winners);

		return losers;
	}

	@Override
	public String GetMode()
	{
		return "Solo Mode";
	}
}
