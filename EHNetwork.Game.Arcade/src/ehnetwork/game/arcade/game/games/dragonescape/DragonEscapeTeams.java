package ehnetwork.game.arcade.game.games.dragonescape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.events.PlayerPrepareTeleportEvent;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.dragonescape.kits.KitDigger;
import ehnetwork.game.arcade.game.games.dragonescape.kits.KitLeaper;
import ehnetwork.game.arcade.game.games.dragonescape.kits.KitWarper;
import ehnetwork.game.arcade.kit.Kit;

public class DragonEscapeTeams extends TeamGame
{
	private ArrayList<DragonScore> _ranks = new ArrayList<DragonScore>();
	private ArrayList<String> _lastScoreboard = new ArrayList<String>();
	
	private NautHashMap<Player, Long> _warpTime = new NautHashMap<Player, Long>();

	private Location _dragon;
	private ArrayList<Location> _waypoints;
	
	private HashMap<Location, Double> _waypointScore = new HashMap<Location, Double>();

	private DragonEscapeTeamsData _dragonData;

	private double _speedMult = 1;

	public DragonEscapeTeams(ArcadeManager manager) 
	{
		super(manager, GameType.DragonEscapeTeams,

				new Kit[]
						{
				new KitLeaper(manager),
				new KitWarper(manager),
				new KitDigger(manager),
						},

						new String[]
								{
				"Douglas the Dragon is after you!",
				"RUN!!!!!!!!!!",
				"Last player alive wins!"
								});

		this.DamagePvP = false;
		this.HungerSet = 20;
		
		this.TeamArmorHotbar = true;
	}

	@Override
	public void ParseData() 
	{
		_dragon = WorldData.GetDataLocs("RED").get(0);
		_waypoints = new ArrayList<Location>();

		//Order Waypoints
		Location last = _dragon;

		while (!WorldData.GetDataLocs("BLACK").isEmpty())
		{	
			Location best = null;
			double bestDist = 0;

			//Get Best
			for (Location loc : WorldData.GetDataLocs("BLACK"))
			{
				double dist = UtilMath.offset(loc, last);

				if (best == null || dist < bestDist)
				{
					best = loc;
					bestDist = dist;
				}
			}
			
			//Ignore Close
			if (bestDist < 3 && WorldData.GetDataLocs("BLACK").size() > 1)
			{
				System.out.println("Ignoring Node");
				WorldData.GetDataLocs("BLACK").remove(best);
				continue;
			}

			_waypoints.add(best);
			WorldData.GetDataLocs("BLACK").remove(best);
			best.subtract(new Vector(0,1,0));

			last = best;
		}
		
		//Score Waypoints
		double dist = 0;
		Location lastLoc = null;
		for (int i=0 ; i<_waypoints.size() ; i++)
		{
			Location newLoc = _waypoints.get(i);
			
			//First
			if (lastLoc == null)
			{
				_waypointScore.put(newLoc, 0d);
				lastLoc = newLoc;
				continue;
			}
			
			dist += UtilMath.offset(lastLoc, newLoc);
			_waypointScore.put(newLoc, dist);
			lastLoc = newLoc;
		}
		
		if (!WorldData.GetDataLocs("GREEN").isEmpty())
			_speedMult = WorldData.GetDataLocs("GREEN").get(0).getX()/100d;
			
		if (WorldData.MapName.contains("Hell"))
			this.WorldTimeSet = 16000;	
	}
	
	@EventHandler
	public void PlayerTeleport(PlayerPrepareTeleportEvent event)
	{
		Manager.GetCondition().Factory().Invisible(GetName(), event.GetPlayer(), event.GetPlayer(), 16, 1, false, false, false);
	}

	@EventHandler
	public void SpawnDragon(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		this.CreatureAllowOverride = true;
		EnderDragon dragon = _dragon.getWorld().spawn(_dragon, EnderDragon.class);
		this.CreatureAllowOverride = false;

		dragon.setCustomName(ChatColor.YELLOW + C.Bold + "Douglas the Dragon");

		_dragonData = new DragonEscapeTeamsData(this, dragon, _waypoints.get(0));
	}

	@EventHandler 
	public void MoveDragon(UpdateEvent event)
	{		
		if (event.getType() != UpdateType.TICK)
			return;

		if (_dragonData == null)
			return;
		
		_dragonData.Target = _waypoints.get(Math.min(_waypoints.size()-1, (GetWaypointIndex(_dragonData.Location) + 1)));

		_dragonData.Move();
		
		Set<Block> blocks = UtilBlock.getInRadius(_dragonData.Location, 10d).keySet();
		
		Iterator<Block> blockIterator = blocks.iterator();
		while (blockIterator.hasNext())
		{
			Block block = blockIterator.next();
			
			if (block.isLiquid())
				blockIterator.remove();
			
			else if (block.getRelative(BlockFace.UP).isLiquid())
				blockIterator.remove();

			else if (WorldData.MapName.contains("Hell") && block.getY() < 30)
				blockIterator.remove();
			
			else if (WorldData.MapName.contains("Pirate") && (block.getY() < 6))
				blockIterator.remove();
		}

		Manager.GetExplosion().BlockExplosion(blocks, _dragonData.Location, false);
	}

	@EventHandler
	public void UpdateScores(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (!IsLive())
			return;

		if (_dragonData == null)
			return;

		double dragonScore = GetScore(_dragonData.Dragon);

		for (Player player : GetPlayers(true))
		{
			double playerScore = GetScore(player);

			SetScore(player, playerScore);

			if (dragonScore > playerScore)
				player.damage(50);
		}
	}
	
	public ArrayList<DragonScore> GetScores()
	{
		return _ranks;
	}
	
	public double GetPlayerScore(Player player)
	{
		for (DragonScore score : _ranks)
		{
			if (score.Player.equals(player))
				return Math.max(0, score.Score);
		}
		
		return 0;
	}

	public void SetScore(Player player, double playerScore)
	{
		//Rank
		for (DragonScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				//debug
				int preNode = (int)(score.Score/10000);
				int postNode = (int)(playerScore/10000);

				//Backwards
				if (preNode - postNode >= 3)
				{
					return;
				}
				
				//Shortcut
				if (postNode - preNode >= 3)
				{
					if (!_warpTime.containsKey(score.Player) || UtilTime.elapsed(_warpTime.get(score.Player), 1000))
					{
						score.Player.damage(500);
						UtilPlayer.message(player, F.main("Game", "You were killed for trying to cheat!"));
						return;
					}					
				}

				if (playerScore > score.Score)
					score.Score = playerScore;
				
				return;
			}
		}

		_ranks.add(new DragonScore(player, playerScore));

		return;
	}

	public double GetScore(Entity ent)
	{
		int index = GetWaypointIndex(ent.getLocation());

		//Not at last waypoint
		if (index < _waypoints.size() - 1)
		{
			double score =  _waypointScore.get(_waypoints.get(index + 1));
			score -= UtilMath.offset(ent.getLocation(), _waypoints.get(index+1));
			return score;
		}
		
		//Finished, max score
		return _waypointScore.get(_waypoints.get(index));
		
	}

	public int GetWaypointIndex(Location loc)
	{
		int best = -1;
		double bestDist = 0;

		for (int i=0 ; i<_waypoints.size() ; i++) 
		{
			Location waypoint = _waypoints.get(i);

			double dist = UtilMath.offset(waypoint, loc);

			if (best == -1 || dist < bestDist)
			{
				best = i;
				bestDist = dist;
			}
		}

		return best;
	}
	
	public Location GetWaypoint(Location loc)
	{
		Location best = null;
		double bestDist = 0;

		for (int i=0 ; i<_waypoints.size() ; i++) 
		{
			Location waypoint = _waypoints.get(i);

			double dist = UtilMath.offset(waypoint, loc);

			if (best == null || dist < bestDist)
			{
				best = waypoint;
				bestDist = dist;
			}
		}

		return best;
	}
	
	public HashMap<GameTeam, Double> GetTeamScores()
	{
		HashMap<GameTeam, Double> scores = new HashMap<GameTeam, Double>();
		
		for (GameTeam team : GetTeamList())
		{
			double score = 0;
			for (Player player : team.GetPlayers(false))
				score += GetPlayerScore(player);
			
			scores.put(team, score);
		}
		
		return scores;
	}

	private void SortScores() 
	{
		for (int i=0 ; i<_ranks.size() ; i++)
		{
			for (int j=_ranks.size()-1 ; j>0 ; j--)
			{
				if (_ranks.get(j).Score > _ranks.get(j-1).Score)
				{
					DragonScore temp = _ranks.get(j);
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

		//Wipe Last
		Scoreboard.Reset();

		HashMap<GameTeam, Double> scores = GetTeamScores();
		for (GameTeam team : scores.keySet())
		{
			//Time
			int score = scores.get(team).intValue();
			
			Scoreboard.WriteBlank();
			Scoreboard.Write(team.GetColor() + team.GetName());
			Scoreboard.Write(score + "" + team.GetColor() + " Score");
		}
		
		Scoreboard.Draw();
	}

	@Override
	public Location GetSpectatorLocation()
	{
		if (SpectatorSpawn == null)
		{
			SpectatorSpawn = new Location(this.WorldData.World, 0,0,0);
		}

		Vector vec = new Vector(0,0,0);
		double count = 0;

		for (Player player : GetPlayers(true))
		{				
			count++;
			vec.add(player.getLocation().toVector());
		}

		if (count == 0)
			count++;

		vec.multiply(1d/count);

		SpectatorSpawn.setX(vec.getX());
		SpectatorSpawn.setY(vec.getY() + 10);
		SpectatorSpawn.setZ(vec.getZ());

		return SpectatorSpawn;
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(true).size() > 0)
				teamsAlive.add(team);

		if (teamsAlive.size() <= 0)
		{
			//Get Winner
			GameTeam winner = null;
			double bestScore = 0;
			
			HashMap<GameTeam, Double> scores = GetTeamScores();
			for (GameTeam team : scores.keySet())
			{
				if (winner == null || scores.get(team) > bestScore)
				{
					winner = team;
					bestScore = scores.get(team);
				}
			}
			
			//Announce
			if (winner != null)
			{
				AnnounceEnd(winner);
			}
			
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
			
			//End
			SetState(GameState.End);	
		}
	}
	
	public double GetSpeedMult()
	{
		return _speedMult;
	}
	
	@EventHandler
	public void Warp(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK &&
			event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		
		Player player = event.getPlayer();
		
		if (!UtilInv.IsItem(player.getItemInHand(), Material.ENDER_PEARL, (byte)0))
			return;
						
		event.setCancelled(true);
		
		SortScores();
		
		Player target = null;
		boolean self = false;
		
		for (int i=_ranks.size()-1 ; i>=0 ; i--)
		{
			DragonScore score = _ranks.get(i);
			
			if (score.Player.equals(player))
			{
				self = true;
			}
			else if (self)
			{
				if (IsAlive(score.Player))
				{
					target = score.Player;
					break;
				}
			}
		}
		
		if (target != null)
		{
			UtilInv.remove(player, Material.ENDER_PEARL, (byte)0, 1);
			UtilInv.Update(player);
			
			//Firework
			UtilFirework.playFirework(player.getEyeLocation(), Type.BALL, Color.BLACK, false, false);
			player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
						
			//Teleport
			player.teleport(target.getLocation().add(0, 0.5, 0));
			player.setVelocity(new Vector(0,0,0));
			player.setFallDistance(0);
			
			//Record
			_warpTime.put(player, System.currentTimeMillis());
			
			//Inform
			UtilPlayer.message(player, F.main("Game", "You warped to " + F.name(target.getName()) + "!"));
			
			//Effect
			player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 1f, 1f);
			

			//Firework
			UtilFirework.playFirework(player.getEyeLocation(), Type.BALL, Color.BLACK, false, false);
			player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
		}
		else
		{
			UtilPlayer.message(player, F.main("Game", "There is no one infront of you!"));
		}
	}
}
