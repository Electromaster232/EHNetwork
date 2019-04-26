package nautilus.game.arcade.game.games.common;
  
import java.util.ArrayList;
import java.util.HashMap;





import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.elo.EloPlayer;
import mineplex.core.elo.EloTeam;
import mineplex.core.elo.GameResult;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.CombatComponent;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.events.PlayerGameRespawnEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.TeamGame;
import nautilus.game.arcade.game.games.champions.ChampionsDominate;
import nautilus.game.arcade.game.games.common.dominate_data.*;
import nautilus.game.arcade.kit.Kit;
  
public class Domination extends TeamGame
{       
	//Map Data 
	private ArrayList<CapturePoint> _points = new ArrayList<CapturePoint>(); 
	private ArrayList<Emerald> _emerald = new ArrayList<Emerald>();
	private ArrayList<Resupply> _resupply = new ArrayList<Resupply>();
	
	//Stats
	private HashMap<String, PlayerData> _stats = new HashMap<String, PlayerData>();
 
	//Scores   
	private int _victoryScore = 15000;
	private int _redScore = 0;    
	private int _blueScore = 0;

	public Domination(ArcadeManager manager, GameType type, Kit[] kits)
	{ 
		super(manager, type, kits,   
   
						new String[]
								{ 
				"Capture Beacons for Points", 
				"+300 Points for Emerald Powerups",
				"+50 Points for Kills",
				"First team to 15000 Points wins"

							 	});

		this.DeathOut = false;
		this.PrepareFreeze = true;   
		this.HungerSet = 20; 
		this.WorldTimeSet = 2000; 
  
		this.DeathSpectateSecs = 10;
		
		//this.QuitOut = false;
	} 
   
	@Override   
	public void ParseData()  
	{ 
		for (String pointName : WorldData.GetAllCustomLocs().keySet())
		{
			_points.add(new CapturePoint(this, pointName, WorldData.GetAllCustomLocs().get(pointName).get(0)));
		}
 
		for (Location loc : WorldData.GetDataLocs("YELLOW"))
		{
			_resupply.add(new Resupply(this, loc));
		}
 
		for (Location loc : WorldData.GetDataLocs("LIME"))
		{
			_emerald.add(new Emerald(this, loc));
		}
		
		_victoryScore = 3000 * _points.size();

		//Spawn Kits
		if (this instanceof ChampionsDominate)
		{
			CreatureAllowOverride = true;
			
			for (int i = 0; i < GetKits().length && i < WorldData.GetDataLocs("RED").size() && i < WorldData.GetDataLocs("BLUE").size(); i++)
			{
				Entity ent = GetKits()[i].SpawnEntity(WorldData.GetDataLocs("RED").get(i)); 
				Manager.GetLobby().AddKitLocation(ent, GetKits()[i], WorldData.GetDataLocs("RED").get(i));
				
				ent = GetKits()[i].SpawnEntity(WorldData.GetDataLocs("BLUE").get(i));
				Manager.GetLobby().AddKitLocation(ent, GetKits()[i], WorldData.GetDataLocs("BLUE").get(i));
			}
			 
			CreatureAllowOverride = false; 
		}
	}
	
	@EventHandler
	public void CustomTeamGeneration(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Recruit)
			return;

		for (GameTeam team : GetTeamList())
			if (team.GetColor() == ChatColor.AQUA)
				team.SetColor(ChatColor.BLUE);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockliquidFlow(BlockPhysicsEvent event)
	{
		Material matOfBlock = event.getBlock().getType();

		if (matOfBlock == Material.STATIONARY_WATER || matOfBlock == Material.SAND || matOfBlock == Material.GRAVEL || matOfBlock == Material.STATIONARY_LAVA)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTNTExplode(EntityExplodeEvent event)
	{
		if (!IsLive())
			return;

		if (event.getEntityType() == EntityType.PRIMED_TNT)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void Updates(UpdateEvent event)
	{  
		if (!IsLive())
			return;

		if (event.getType() == UpdateType.FAST)
			for (CapturePoint cur : _points)
				cur.Update();

		if (event.getType() == UpdateType.FAST)
			for (Emerald cur : _emerald) 
				cur.Update();

		if (event.getType() == UpdateType.FAST)
			for (Resupply cur : _resupply) 
				cur.Update();
	}

	@EventHandler
	public void PowerupPickup(PlayerPickupItemEvent event)
	{
		for (Emerald cur : _emerald)
			cur.Pickup(event.getPlayer(), event.getItem());

		for (Resupply cur : _resupply)
			cur.Pickup(event.getPlayer(), event.getItem());
	}

	@EventHandler
	public void KillScore(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player)event.GetEvent().getEntity();

		GameTeam killedTeam = GetTeam(killed);
		if (killedTeam == null) 	return;

		if (event.GetLog().GetKiller() == null)
			return;

		Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

		if (killer == null)
			return;

		GameTeam killerTeam = GetTeam(killer);
		if (killerTeam == null)	return;

		if (killerTeam.equals(killedTeam))
			return;

		AddScore(killerTeam, 50);
	}

	public void AddScore(GameTeam team, int score)
	{
		if (team.GetColor() == ChatColor.RED)
		{
			_redScore = Math.min(_victoryScore, _redScore + score);
		}
		else
		{
			_blueScore = Math.min(_victoryScore, _blueScore + score);
		}

		EndCheckScore();
	}

	//Dont allow powerups to despawn
	@EventHandler
	public void ItemDespawn(ItemDespawnEvent event)
	{
		event.setCancelled(true);
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		ScoreboardWrite();

	}

	private void ScoreboardWrite() 
	{
		if (!InProgress())
			return;

		//Wipe Last
		Scoreboard.Reset(); 

		//Scores
		Scoreboard.WriteBlank();
		Scoreboard.Write("First to " + _victoryScore);
		
		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cRed + "Red Team");
		Scoreboard.Write(_redScore + C.cRed);
		
		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cAqua + "Blue Team");
		Scoreboard.Write(_blueScore + C.cAqua);

		Scoreboard.WriteBlank();

		//Write CPs
		for (int i=0 ; i<_points.size() ; i++)
		{
			Scoreboard.Write(_points.get(i).GetScoreboardName());
		}  
		
		Scoreboard.Draw();
	}  
      
	public void EndCheckScore()   
	{
		if (!IsLive())
			return;
		
		GameTeam winner = null;
 
		if (_redScore >= _victoryScore)
			winner = GetTeam(ChatColor.RED);
		else if (_blueScore >= _victoryScore)
			winner = GetTeam(ChatColor.BLUE);

		if (winner == null)
			return;
 
		ScoreboardWrite();

		//Announce
		AnnounceEnd(winner);

		for (GameTeam team : GetTeamList())
		{
			if (WinnerTeam != null && team.equals(WinnerTeam))
			{
				for (Player player : team.GetPlayers(false))
					AddGems(player, 10, "Winning Team", false, false);
			}

			for (Player player : team.GetPlayers(false))
				if (player.isOnline())
					AddGems(player, 10, "Participation", false, false);
		}
		
		if (EloRanking)
		{
			EloTeam teamWinner = new EloTeam();
			EloTeam teamLoser = new EloTeam();
			
			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : WinnerTeam.GetPlayers(false))
					{
						EloPlayer eloPlayer = new EloPlayer();
						eloPlayer.UniqueId = player.getUniqueId().toString();
						eloPlayer.Rating = Manager.getEloManager().getElo(player.getUniqueId(), GetName());
						
						teamWinner.addPlayer(eloPlayer);
					}				
				}
				else
				{
					for (Player player : team.GetPlayers(false))
					{
						EloPlayer eloPlayer = new EloPlayer();
						eloPlayer.UniqueId = player.getUniqueId().toString();
						eloPlayer.Rating = Manager.getEloManager().getElo(player.getUniqueId(), GetName());
						
						teamLoser.addPlayer(eloPlayer);
					}
				}
			}
		
			for (EloPlayer eloPlayer : Manager.getEloManager().getNewRatings(teamWinner, teamLoser, GameResult.Win).getPlayers())
			{
				Manager.getEloManager().saveElo(eloPlayer.UniqueId, GetName(), eloPlayer.Rating);
			}
			
			for (EloPlayer eloPlayer : Manager.getEloManager().getNewRatings(teamLoser, teamWinner, GameResult.Loss).getPlayers())
			{
				Manager.getEloManager().saveElo(eloPlayer.UniqueId, GetName(), eloPlayer.Rating);
			}
		}
 
		//End
		SetState(GameState.End);	
	}

	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		return 1; 
	}
	  
	public String GetMode()
	{
		return "Domination";
	}
	
	@EventHandler
	public void RespawnRegen(PlayerGameRespawnEvent event)
	{
		Manager.GetCondition().Factory().Regen("Respawn", event.GetPlayer(), event.GetPlayer(), 8, 3, false, false, false);
		Manager.GetCondition().Factory().Protection("Respawn", event.GetPlayer(), event.GetPlayer(), 8, 3, false, false, false);
	}
	
	public PlayerData GetStats(Player player)
	{
		if (!_stats.containsKey(player.getName()))
			_stats.put(player.getName(), new PlayerData(player.getName()));
		
		return _stats.get(player.getName());
	}
	
	@EventHandler
	public void StatsKillAssistDeath(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;
		
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;
		
		Player killed = (Player)event.GetEvent().getEntity();
		GetStats(killed).Deaths++;

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer != null && !killer.equals(killed))
				GetStats(killer).Kills++;
		}

		for (CombatComponent log : event.GetLog().GetAttackers())
		{
			if (event.GetLog().GetKiller() != null && log.equals(event.GetLog().GetKiller()))
				continue;

			Player assist = UtilPlayer.searchExact(log.GetName());

			//Assist
			if (assist != null)
				GetStats(assist).Assists++;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void StatsKillAssistDeath(CustomDamageEvent event)
	{
		Player damager = event.GetDamagerPlayer(true);
		if (damager != null)
		{
			GetStats(damager).DamageDealt += event.GetDamage();
		}
		
		Player damagee = event.GetDamageePlayer();
		if (damagee != null)
		{
			GetStats(damagee).DamageTaken += event.GetDamage();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void UsableInteract(PlayerInteractEvent event)
	{
		if (UtilBlock.usable(event.getClickedBlock()))
			event.setCancelled(true);
	}
}
