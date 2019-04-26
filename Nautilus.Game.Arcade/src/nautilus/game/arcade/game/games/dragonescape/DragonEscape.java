package nautilus.game.arcade.game.games.dragonescape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.data.BlockData;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.CombatComponent;
import mineplex.minecraft.game.core.combat.CombatDamage;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.events.PlayerKitGiveEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.dragonescape.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.stats.ParalympicsStatTracker;
import nautilus.game.arcade.stats.WinMapStatTracker;

public class DragonEscape extends SoloGame
{
	public static class PlayerFinishEvent extends PlayerEvent
	{
		private static final HandlerList HANDLER_LIST = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return HANDLER_LIST;
		}
 
		public PlayerFinishEvent(Player who)
		{
			super(who);
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}
	}

	private ArrayList<DragonScore> _ranks = new ArrayList<DragonScore>();
	
	private NautHashMap<Player, Long> _warpTime = new NautHashMap<Player, Long>();

	private Location _dragon;
	private ArrayList<Location> _waypoints;

	private DragonEscapeData _dragonData;

	private Player _winner = null;
	
	private double _speedMult = 1;
	
	private HashMap<BlockData, Player> _tunneled = new HashMap<BlockData, Player>();

	public DragonEscape(ArcadeManager manager) 
	{
		super(manager, GameType.DragonEscape,

				new Kit[]
						{
				new KitLeaper(manager),
				new KitDisruptor(manager),
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
		this.BlockPlace = true;

		registerStatTrackers(
				new ParalympicsStatTracker(this),
				new WinMapStatTracker(this)
		);
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
		
		if (!WorldData.GetDataLocs("GREEN").isEmpty())
			_speedMult = WorldData.GetDataLocs("GREEN").get(0).getX()/100d;
			
		if (WorldData.MapName.contains("Hell"))
			this.WorldTimeSet = 16000;	
		
		if (WorldData.MapName.contains("Pirate"))
			this.WorldWaterDamage = 2;	
	}

	@EventHandler
	public void SpawnDragon(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;
		
		for (Team team : GetScoreboard().GetScoreboard().getTeams())
			team.setCanSeeFriendlyInvisibles(true);
		
		this.CreatureAllowOverride = true;
		EnderDragon dragon = _dragon.getWorld().spawn(_dragon, EnderDragon.class);
		this.CreatureAllowOverride = false;

		dragon.setCustomName(ChatColor.YELLOW + C.Bold + "Douglas the Dragon");

		_dragonData = new DragonEscapeData(this, dragon, _waypoints.get(0)); 
	}
	
	@EventHandler
	public void Invisibility(PlayerKitGiveEvent event)
	{
		Manager.GetCondition().Factory().Invisible(GetName(), event.GetPlayer(), event.GetPlayer(), 40, 0, false, false, false);
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

			if (SetScore(player, playerScore))
				return;

			if (dragonScore > playerScore)
				player.damage(50);
		}
	}
	
	public ArrayList<DragonScore> GetScores()
	{
		return _ranks;
	}

	public boolean SetScore(Player player, double playerScore)
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
					return false;
				}
				
				//Shortcut
				if (postNode - preNode >= 3)
				{
					if (!_warpTime.containsKey(score.Player) || UtilTime.elapsed(_warpTime.get(score.Player), 1000))
					{
						score.Player.damage(500);
						UtilPlayer.message(player, F.main("Game", "You were killed for trying to cheat!"));
						return false;
					}					
				}

				//Finish
				if (GetWaypointIndex(player.getLocation()) == _waypoints.size() - 1)
				{
					//Only if NEAR end.
					if (UtilMath.offset(player.getLocation(), _waypoints.get(_waypoints.size()-1)) < 3)
					{
						_winner = player;
						this.SetCustomWinLine(player.getName() + " reached the end of the course!");

						Bukkit.getPluginManager().callEvent(new PlayerFinishEvent(player));

						return true;
					}
				}

				score.Score = playerScore;
				return false;
			}
		}

		_ranks.add(new DragonScore(player, playerScore));

		return false;
	}

	public double GetScore(Entity ent)
	{
		int index = GetWaypointIndex(ent.getLocation());

		double score =  10000 * index;

		score -= UtilMath.offset(ent.getLocation(), _waypoints.get(Math.min(_waypoints.size()-1, index+1)));

		return score;
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

		SortScores();
		
		//Wipe Last
		Scoreboard.Reset();
		
		Scoreboard.WriteBlank();
		
		//Write New
		for (int i=0 ; i<_ranks.size() && i<14 ; i++)
		{
			DragonScore score = _ranks.get(i);

			ChatColor col = ChatColor.GREEN;
			if (!IsAlive(score.Player))
				col = ChatColor.RED;

			Scoreboard.WriteOrdered("Rank", col + score.Player.getName(), i+1, true);
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

		if (GetPlayers(true).size() <= 0 || _winner != null)
		{	
			SortScores();

			ArrayList<Player> places = new ArrayList<Player>();
			for (DragonScore score : _ranks)
				places.add(score.Player);
			
			//Announce
			AnnounceEnd(places);

			//Gems
			if (_winner != null)
				AddGems(_winner, 10, "Course Complete", false, false);

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
	
	public double GetSpeedMult()
	{
		return _speedMult;
	}
	
	@EventHandler
	public void Warp(PlayerInteractEvent event)
	{
		if (!IsLive())
			return;
		
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
	
	@EventHandler
	public void Tunneler(BlockDamageEvent event)
	{
		Player player = event.getPlayer();
		
		if (!IsAlive(player))
			return;
						
		if (!player.getInventory().contains(Material.DIAMOND_PICKAXE))
			return;
		
		if (!UtilTime.elapsed(GetStateTime(), 10000))
		{
			UtilPlayer.message(player, F.main("Game", "You cannot dig for " + F.elem(UtilTime.MakeStr(10000 - (System.currentTimeMillis() - GetStateTime())) + ".")));
			return;
		}

		for (Player other : GetPlayers(true))
		{
			if (player.equals(other))
				continue;
			
			if (UtilMath.offset(event.getBlock().getLocation().add(0.5, 0.5, 0.5), other.getLocation()) < 1.5 ||
				UtilMath.offset(event.getBlock().getLocation().add(0.5, 1, 0.5), other.getLocation()) < 1.5)
			{
				UtilPlayer.message(player, F.main("Game", "You cannot dig near other players."));
				return;
			}
		}
		
		if (!Recharge.Instance.use(player, "Tunneler", 100, false, false))
			return;
		
		event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, event.getBlock().getType());
		
		player.getInventory().addItem(new ItemStack(event.getBlock().getType()));
		
		_tunneled.put(new BlockData(event.getBlock()), player);
	
		Manager.GetBlockRestore().Add(event.getBlock(), 0, (byte)0, 2400);

		UtilInv.remove(player, Material.DIAMOND_PICKAXE, (byte)0, 1);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void Tunneler(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;
				
		Manager.GetBlockRestore().Add(event.getBlock(), 
				event.getPlayer().getItemInHand().getType().getId(), event.getPlayer().getItemInHand().getData().getData(), 
				event.getBlockReplacedState().getTypeId(), event.getBlockReplacedState().getRawData(), 2400);
	}
	
	@EventHandler
	public void TunnelerUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<BlockData> tunnelIterator = _tunneled.keySet().iterator();
		
		while (tunnelIterator.hasNext())
		{
			BlockData data = tunnelIterator.next();
			
			if (data.Block.getType() != Material.AIR || UtilTime.elapsed(data.Time, 2400))
				tunnelIterator.remove();
			
			else
				for (Player other : UtilServer.getPlayers())
					if (!other.equals(_tunneled.get(data)))
						other.sendBlockChange(data.Block.getLocation(), data.Material, data.Data);
		}
	}
}
