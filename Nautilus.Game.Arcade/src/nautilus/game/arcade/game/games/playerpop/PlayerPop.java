package nautilus.game.arcade.game.games.playerpop;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.playerpop.kits.KitHealer;
import nautilus.game.arcade.game.games.playerpop.kits.KitPopper;
import nautilus.game.arcade.game.games.quiver.QuiverScore;
import nautilus.game.arcade.kit.Kit;

public class PlayerPop extends SoloGame
{
	private ArrayList<QuiverScore> _ranks = new ArrayList<QuiverScore>();
	public PlayerPop(ArcadeManager manager)
	{
		super(manager, GameType.PlayerPop,
				new Kit[]
						{
								new KitPopper(manager),
								new KitHealer(manager)
						},

				new String[]
						{
								"Using your fist, attack other players in order to kill them",
								"First to 20 kills wins",
								"You do not take fall damage"
						});

		this.BlockPlace = false;
		this.BlockBreak = false;
		this.HealthSet = 2;
		this.PrepareFreeze = true;
		this.DamageFall = false;
		this.HungerSet = 20;
		this.DeathOut = false;
		this.StrictAntiHack = true;
		this.DamageTeamOther = true;
		this.Damage = true;
		this.DamageTeamSelf = true;

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

		Player player = event.GetDamageePlayer();
		Kit currentkit = GetKit(player);
		if(currentkit.GetName().contains("Heal")){
			event.AddMod("Fist", "Instagib", 1, false);
		}
		else{
			event.AddMod("Fist", "Instagib", 2, false);
		}

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
