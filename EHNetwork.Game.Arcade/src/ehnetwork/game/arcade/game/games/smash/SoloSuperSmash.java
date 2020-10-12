package ehnetwork.game.arcade.game.games.smash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.GameTeam;

public class SoloSuperSmash extends SuperSmash
{

	private GameTeam _players;
	
	public SoloSuperSmash(ArcadeManager manager)
	{
		super(manager, GameType.Smash, new String[]
				{
				"Each player has 3 respawns",
				"Attack to restore hunger!",
				"Last player alive wins!"
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

		//Wipe Last
		Scoreboard.Reset();

		if (GetPlayers(true).size() > 14)
		{
			Scoreboard.WriteBlank();
			Scoreboard.Write(C.cGreen + "Players Alive");
			Scoreboard.Write(GetPlayers(true).size() + " ");

			Scoreboard.WriteBlank();
			Scoreboard.Write(C.cRed + "Players Dead");
			Scoreboard.Write((GetPlayers(false).size() - GetPlayers(true).size()) + "  ");
		}
		else
		{
			Scoreboard.WriteBlank();

			//Write New
			for (Player player : GetPlayers(true))
			{
				int lives = GetLives(player);

				String out;
				if (lives >= 4)			out = C.cGreen + player.getName();
				else if (lives == 3)	out = C.cYellow + player.getName();
				else if (lives == 2)	out = C.cGold + player.getName();
				else if (lives == 1)	out = C.cRed + player.getName();
				else if (lives == 0)	out = C.cRed + player.getName();
				else
					continue;

				Scoreboard.WriteOrdered("Lives", out, lives, true);
			}
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
