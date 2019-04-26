package nautilus.game.arcade.game.games.evolution;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.evolution.kits.*;
import nautilus.game.arcade.game.games.evolution.mobs.*;
import nautilus.game.arcade.kit.Kit;

public class Evolution extends SoloGame
{
	private ArrayList<EvoScore> _ranks = new ArrayList<EvoScore>();
	private ArrayList<String> _lastScoreboard = new ArrayList<String>();

	private HashMap<Player, Kit> _bonusKit = new HashMap<Player, Kit>();

	private Objective _scoreObj;
	
	public Evolution(ArcadeManager manager) 
	{
		super(manager, GameType.Evolution,

				new Kit[]
						{
				new KitHealth(manager),
				new KitAgility(manager),
				new KitRecharge(manager),

				new KitGolem(manager),			//Iron	
				new KitBlaze(manager),			//Chainmail
				new KitSlime(manager),			//Chainmail
				new KitCreeper(manager),		//None
				new KitEnderman(manager),		//Chainmail
				new KitSkeleton(manager),		//Leather
				new KitSpider(manager),			//Chainmail
				new KitSnowman(manager),		//Leather
				new KitWolf(manager),			//Leather
				new KitChicken(manager),		

						},

						new String[]
								{
				"You evolve when you get a kill.",
				"Each evolution has unique skills.",
				"",
				"First to get through 10 evolutions wins!"
								});

		this.DamageTeamSelf = true;
		
		this.HungerSet = 20;
		
		this.DeathOut = false;
		
		this.PrepareFreeze = false;
		
		_scoreObj = Scoreboard.GetScoreboard().registerNewObjective("Evolutions", "dummy");
		_scoreObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void RegisterMobKits(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;
	}

	//Double Kit
	@EventHandler(priority = EventPriority.MONITOR)
	public void StoreBonusKits(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		for (Player player : GetPlayers(true))
		{
			_bonusKit.put(player, GetKit(player));
			UpgradeKit(player, true);
		}
	}

	@Override
	public boolean HasKit(Player player, Kit kit)
	{
		if (GetKit(player) == null)
			return false;

		if (GetKit(player).equals(kit))
			return true;

		//Bonus Kit
		if (_bonusKit.containsKey(player))
			if (_bonusKit.get(player).equals(kit))
				return true;

		return false;
	}

	@EventHandler
	public void PlayerKillAward(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		if (event.GetLog().GetKiller() == null)
			return;

		final Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (killer == null)
			return;

		if (killer.equals(event.GetEvent().getEntity()))
			return;

		Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				UpgradeKit(killer, false);
			}
		}, 0);
	}

	public void UpgradeKit(Player player, boolean first)
	{
		if (!Recharge.Instance.use(player, "Evolve", 500, false, false))
			return;
		
		//Remove Old Recharges
		Recharge.Instance.Reset(player);
		Recharge.Instance.useForce(player, "Evolve", 500);
		
		Kit kit = GetKit(player);

		for (int i=3 ; i<GetKits().length ; i++)
		{
			if (kit.equals(GetKits()[i]) || first)
			{
				if (!first)
					i++;

				if (i<GetKits().length)
				{
					SetKit(player, GetKits()[i], false);

					//Store Level
					SetScore(player, i-3);

					//Apply
					GetKits()[i].ApplyKit(player);

					//Firework
					UtilFirework.playFirework(player.getEyeLocation(), Type.BALL, Color.LIME, false, false);
										
					//Teleport
					player.teleport(GetTeam(player).GetSpawn());

					return;
				}
				else
				{
					End();
					return;
				}
			}
		}
	}

	public void SetScore(Player player, int level)
	{
		_scoreObj.getScore(player).setScore(level);
		
		//Rank
		for (EvoScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				score.Kills = level;
				return;
			}
		}
		
		_ranks.add(new EvoScore(player, level));
	}

	public int GetScore(Player player)
	{
		if (!IsAlive(player))
			return 0;

		//Rank
		for (EvoScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				return score.Kills;
			}
		}
		
		return 0;
	}

	private void SortScores() 
	{
		for (int i=0 ; i<_ranks.size() ; i++)
		{
			for (int j=_ranks.size()-1 ; j>0 ; j--)
			{
				if (_ranks.get(j).Kills > _ranks.get(j-1).Kills)
				{
					EvoScore temp = _ranks.get(j);
					_ranks.set(j, _ranks.get(j-1));
					_ranks.set(j-1, temp);
				}
			}
		}
	}

	private void End()
	{
		SortScores();

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
				AddGems(player, 10, "Participation", false, false);

		SetState(GameState.End);
		AnnounceEnd(places);
	}

	@Override
	public void EndCheck()
	{

	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		Scoreboard.Reset();

		for (Player player : GetPlayers(true))
		{
			Scoreboard.WriteOrdered("Score", C.cGreen + player.getName(), GetScore(player), true);
		}
		
		Scoreboard.Draw();
	}
}
