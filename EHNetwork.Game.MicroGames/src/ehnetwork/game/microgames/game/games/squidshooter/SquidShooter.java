package ehnetwork.game.microgames.game.games.squidshooter;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.SoloGame;
import ehnetwork.game.microgames.game.games.quiver.QuiverScore;
import ehnetwork.game.microgames.game.games.squidshooter.kits.KitRifle;
import ehnetwork.game.microgames.game.games.squidshooter.kits.KitShotgun;
import ehnetwork.game.microgames.game.games.squidshooter.kits.KitSniper;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class SquidShooter extends SoloGame
{
	private ArrayList<QuiverScore> _ranks = new ArrayList<QuiverScore>();

	public SquidShooter(MicroGamesManager manager)
	{
		super(manager, GameType.SquidShooter,

				new Kit[]
						{
				new KitRifle(manager),
				new KitShotgun(manager),
				new KitSniper(manager),
						},

						new String[]
								{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Attack",
				C.cYellow + "Hold Crouch" + C.cGray + " to use " + C.cGreen + "Squid Swim",
				C.cYellow + "Tap Crouch Quickly" + C.cGray + " to use " + C.cGreen + "Squid Thrust",
				"First player to 20 kills wins."
								});

		this.DeathOut = false;
		this.DamageSelf = false;
		this.DamageTeamSelf = true;
		this.PrepareFreeze = false;
		this.CompassEnabled = true;
		this.KitRegisterState = GameState.Prepare;
	}	
	@EventHandler
	public void Death(CombatDeathEvent event)
	{
		if (event.GetLog().GetKiller() == null)
			return;

		if (!event.GetLog().GetKiller().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (player == null)	
			return;

		//Score
		AddKill(player);
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

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		SortScores();
		
		//Wipe Last
		Scoreboard.Reset();

		//Write New
		for (QuiverScore score : _ranks)
			Scoreboard.WriteOrdered("Kills", C.cGreen + score.Player.getName(), score.Kills, true);
		
		Scoreboard.Draw();
	}
	
	@EventHandler
	public void AirDamage(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;
		
		if (!IsLive())
			return;
		
		for (Player player : GetPlayers(true))
		{
			if (player.getLocation().getBlock().isLiquid())
			{
				player.setFoodLevel(20);
				continue;
			}
			
			//Damage
			if (player.getFoodLevel() == 0)
				player.damage(1);
			
			//Hunger Lower
			UtilPlayer.hunger(player, -1);
				
			//Slow
			if (UtilEnt.isGrounded(player))
				Manager.GetCondition().Factory().Slow("On Land", player, player, 0.9, 2, false, false, false, false);
		}
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		SortScores();

		if ((!_ranks.isEmpty() && _ranks.get(0).Kills >= 20) || GetPlayers(true).size() <= 1)
		{
			ArrayList<Player> places = new ArrayList<Player>();
			
			//Set Places
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
					AddGems(player, 10, "Participation", false, false);

			SetState(GameState.End);
			AnnounceEnd(places);
		}
	}
}
