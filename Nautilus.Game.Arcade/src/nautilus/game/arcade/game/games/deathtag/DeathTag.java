package nautilus.game.arcade.game.games.deathtag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.visibility.VisibilityManager;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.GameTeam.PlayerState;
import nautilus.game.arcade.game.games.deathtag.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.NullKit;
import nautilus.game.arcade.stats.ComeAtMeBroStatTracker;

public class DeathTag extends SoloGame
{
	private GameTeam _runners;
	private GameTeam _chasers;

	private NautHashMap<Player, Location> _deathLocation = new NautHashMap<Player, Location>();

	private int _currentSpeed = -1;
	
	private ArrayList<Location> _lights = new ArrayList<Location>();

	public DeathTag(ArcadeManager manager) 
	{
		super(manager, GameType.DeathTag,

				new Kit[]
						{
				new KitRunnerBasher(manager),
				new KitRunnerArcher(manager),
				new KitRunnerTraitor(manager),
				new NullKit(manager),
				new KitAlphaChaser(manager),
				new KitChaser(manager),
						},

						new String[]
								{
				"Run from the Undead!",
				"If you die, you become Undead!",
				"The last Runner alive wins!"
								});

		this.StrictAntiHack = true;
		
		this.DeathOut = false;
		this.HungerSet = 20;

		this.CompassEnabled = true;

		this.PrepareFreeze = false;

		registerStatTrackers(new ComeAtMeBroStatTracker(this));
	}
	
	@Override
	public void ParseData()
	{
		_lights = this.WorldData.GetCustomLocs("89");
		
		for (Location loc : _lights)
		{
			if (Math.random() > 0.5)
				loc.getBlock().setType(Material.GOLD_BLOCK);
			else
				loc.getBlock().setTypeIdAndData(35, (byte)15, false);
		}
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
					if (kit.GetName().contains("ZOMBIE"))
						team.GetRestrictedKits().add(kit);
				}
				else
				{
					if (kit.GetName().contains("Chaser"))
						team.GetRestrictedKits().add(kit);
				}
			}
		}
	}

	@Override
	@EventHandler
	public void CustomTeamGeneration(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Recruit)
			return;

		_runners = this.GetTeamList().get(0);
		_runners.SetName("Runners");

		//Undead Team
		_chasers = new GameTeam(this, "Chasers", ChatColor.RED, _runners.GetSpawns());	
		_chasers.SetVisible(false);
		GetTeamList().add(_chasers);

		RestrictKits();
	}

	@Override
	public GameTeam ChooseTeam(Player player) 
	{
		return _runners;
	}
	
	@EventHandler
	public void UpdateLights(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SEC)
			return;
	
		for (Location loc : _lights)
		{
			if (loc.getBlock().getType() == Material.GOLD_BLOCK)
				loc.getBlock().setTypeIdAndData(35, (byte)15, false);
			else
				loc.getBlock().setType(Material.GOLD_BLOCK);
		}
	}
		

	@EventHandler
	public void UpdateSpeed(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.SLOW)
			return;

		double ratio = (double)_chasers.GetPlayers(false).size() / (double)GetPlayers(false).size();

		if (_currentSpeed == -1 && ratio > 0.25)
		{
			Announce(C.cGreen + C.Bold + "Runners receive Speed I");
			_currentSpeed = 0;
		}
		else if (_currentSpeed == -1 && ratio > 0.50)
		{
			Announce(C.cGreen + C.Bold + "Runners receive Speed II");
			_currentSpeed = 1;
		}
		else if (_currentSpeed == -1 && ratio > 0.75)
		{
			Announce(C.cGreen + C.Bold + "Runners receive Speed III");
			_currentSpeed = 2;
		}
	}

	@EventHandler
	public void ApplyConditions(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		if (_currentSpeed >= 0)
			for (Player player : _runners.GetPlayers(false))
			{
				Manager.GetCondition().Factory().Speed("Runner", player, player, 1.9, _currentSpeed, false, false, true);
			}

		for (Player player : _chasers.GetPlayers(false))
		{
			Manager.GetCondition().Factory().Regen("Undying", player, player, 1.9, 4, false, false, false);

			if (_currentSpeed < 0)
				Manager.GetCondition().Factory().Speed("Haste", player, player, 1.9, 0, false, false, true);
		}
	}

	@EventHandler
	public void UpdateChasers(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		int req = 1 + (int) ((System.currentTimeMillis() - GetStateTime()) / 30000);

		while (_chasers.GetPlayers(true).size() < req && _runners.GetPlayers(true).size() > 0)
		{
			Player player = _runners.GetPlayers(true).get(UtilMath.r(_runners.GetPlayers(true).size()));
			SetChaser(player, true);
		}
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event) 
	{
		if (_runners.HasPlayer(event.getEntity()))
		{
			_deathLocation.put(event.getEntity(), event.getEntity().getLocation());
			SetChaser(event.getEntity(), false);
		}
	}

	public void SetChaser(Player player, boolean forced)
	{
		//Set them as OUT!
		if (GetTeam(player) != null)
			GetTeam(player).SetPlacement(player, PlayerState.OUT);

		SetPlayerTeam(player, _chasers, true);

		//Kit
		Kit newKit = 	GetKits()[5];			//Normal
		if (forced)		newKit = GetKits()[4];	//Alpha

		SetKit(player, newKit, false);
		newKit.ApplyKit(player);

		//Refresh
		VisibilityManager.Instance.refreshPlayerToAll(player);
		
		if (forced)
		{
			AddGems(player, 10, "Forced Chaser", false, false);

			Announce(F.main("Game", F.elem(_runners.GetColor() + player.getName()) + " has become an " + 
					F.elem(_chasers.GetColor() + newKit.GetName()) + "."));

			player.getWorld().strikeLightningEffect(player.getLocation());
		}

		UtilPlayer.message(player, C.cRed + C.Bold + "You are now a Chaser!");
		UtilPlayer.message(player, C.cRed + C.Bold + "KILL THEM ALL!!!!!!");
	}

	@Override
	public void RespawnPlayer(final Player player)
	{
		Manager.Clear(player);

		if (_chasers.HasPlayer(player))
		{
			player.eject();

			if (_deathLocation.containsKey(player))
				player.teleport(_deathLocation.remove(player));
			else
				player.teleport(_chasers.GetSpawn());
		}

		//Re-Give Kit
		Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				GetKit(player).ApplyKit(player);

				//Refresh on Spawn
				VisibilityManager.Instance.refreshPlayerToAll(player);
			} 
		}, 0);
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		if (_runners.GetPlayers(true).size() <= 1)
		{
			ArrayList<Player> places = _runners.GetPlacements(true);

			if (places.size() >= 1)
				AddGems(places.get(0), 15, "1st Place", false, false);

			if (places.size() >= 2)
				AddGems(places.get(1), 10, "2nd Place", false, false);

			if (places.size() >= 3)
				AddGems(places.get(2), 5, "3rd Place", false, false);

			for (Player player : GetPlayers(false))
				if (player.isOnline())
					AddGems(player, 10, "Participation", false, false);

			AnnounceEnd(places);

			SetState(GameState.End);
		}
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (_runners == null || _chasers == null)
			return;

		//Wipe Last
		Scoreboard.Reset();

		Scoreboard.WriteBlank();
		
		Scoreboard.Write(_runners.GetPlayers(true).size() + " " +_runners.GetColor() + " Runners");
		
		Scoreboard.WriteBlank();
		
		Scoreboard.Write(_chasers.GetPlayers(true).size() + " " +_chasers.GetColor() + " Chasers");

		Scoreboard.Draw();
	}

	@Override
	public boolean CanJoinTeam(GameTeam team)
	{
		if (team.GetColor() == ChatColor.RED)
		{
			return team.GetSize() < 1 + UtilServer.getPlayers().length/8;
		}

		return true;
	}

	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		if (GetTeam(killed).equals(_runners))
		{
			return 4;
		}

		return 0;
	}
	
	@Override
	public List<Player> getWinners()
	{
		if (GetState().ordinal() >= GameState.End.ordinal())
		{
			List<Player> places = _runners.GetPlayers(true);

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
		if (GetState().ordinal() >= GameState.End.ordinal())
		{
			return _chasers.GetPlayers(true);
		}
		else
			return null;
	}
}
