package ehnetwork.game.arcade.game.games.playerpop;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.SoloGame;
import ehnetwork.game.arcade.game.games.playerpop.kits.KitHealer;
import ehnetwork.game.arcade.game.games.playerpop.kits.KitHider;
import ehnetwork.game.arcade.game.games.playerpop.kits.KitPopper;
import ehnetwork.game.arcade.game.games.playerpop.kits.KitTracker;
import ehnetwork.game.arcade.game.games.quiver.QuiverScore;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PlayerPop extends SoloGame
{
	private ArrayList<QuiverScore> _ranks = new ArrayList<QuiverScore>();
	public PlayerPop(ArcadeManager manager)
	{
		super(manager, GameType.PlayerPop,
				new Kit[]
						{
								new KitPopper(manager),
								new KitTracker(manager),
								new KitHealer(manager),
								new KitHider(manager)

						},

				new String[]
						{
								"Punch players in order to one-hit kill them.",
								"First to 20 kills is the winner. There is no fall damage."
						});
		this.CompassEnabled = true;
		this.CompassGiveItem = false;
		//this.DisableKillCommand = false;
		this.BlockPlace = false;
		this.BlockBreak = false;
		this.PrepareFreeze = true;
		this.DamageFall = false;
		this.HungerSet = 14;
		this.DeathOut = false;
		this.StrictAntiHack = true;
		this.DamageTeamOther = true;
		this.Damage = true;
		this.DamageTeamSelf = true;
		// whoopsie this did not work lmao this.WorldWaterDamage = 5;

	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		SortScores();

		//Wipe Last
		Scoreboard.Reset();

		Scoreboard.WriteBlank();

		Scoreboard.Write(C.cYellow + "Scores:");

		//Write New
		for (QuiverScore score : _ranks)
		{
			Scoreboard.WriteOrdered("Score", C.cGreen + score.Player.getName(), score.Kills, true);
		}

		Scoreboard.Draw();
	}


	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		SortScores();

		if ((!_ranks.isEmpty() && _ranks.get(0).Kills >= 20) || GetPlayers(true).size() <= 1)
		{
			//Set Places
			ArrayList<Player> places = new ArrayList<Player>();
			for (int i=0 ; i<_ranks.size() ; i++)
				places.add(i, _ranks.get(i).Player);

			//Award Gems
			if (_ranks.size() >= 1)
				AddGems(_ranks.get(0).Player, 20, "1st Place", false, false);

			if (_ranks.size() >= 2)
				AddGems(_ranks.get(1).Player, 15, "2nd Place", false, false);

			if (_ranks.size() >= 3)
				AddGems(_ranks.get(2).Player, 10, "3rd Place", false, false);


			//Participation
			for (Player player : GetPlayers(false))
				if (player.isOnline())
				{
					AddGems(player, 10, "Participation", false, false);
				}
			SetState(GameState.End);
			AnnounceEnd(places);
		}
	}

	private void SortScores()
	{
		for (int i=0 ; i<_ranks.size() ; i++)
		{
			for (int j=_ranks.size()-1 ; j>0 ; j--)
			{
				if (_ranks.get(j).Kills > _ranks.get(j-1).Kills)
				{
					QuiverScore temp = _ranks.get(j);
					_ranks.set(j, _ranks.get(j-1));
					_ranks.set(j-1, temp);
				}
			}
		}
	}

	@EventHandler
	public void Death(CombatDeathEvent event){
		if (event.GetLog().GetKiller() == null)
			return;

		if (!event.GetLog().GetKiller().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		AddKill(player);
	}

	@EventHandler
	public void FistDamage(CustomDamageEvent event)
	{
		if(event.GetCause() == EntityDamageEvent.DamageCause.FALL)
		{
			return;
		}
		event.AddMod("Fist", "Attack", 10, false);
	}

	public void AddKill(Player player)
	{

		//Rank
		for (QuiverScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				score.Kills += 1;
				EndCheck();
				return;
			}
		}

		_ranks.add(new QuiverScore(player, 1));
	}

}
