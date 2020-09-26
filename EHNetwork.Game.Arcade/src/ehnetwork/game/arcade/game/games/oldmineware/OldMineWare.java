package ehnetwork.game.arcade.game.games.oldmineware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.SoloGame;
import ehnetwork.game.arcade.game.games.oldmineware.order.Order;
import ehnetwork.game.arcade.game.games.oldmineware.random.ActionMilkCow;
import ehnetwork.game.arcade.game.games.oldmineware.random.ActionShearSheep;
import ehnetwork.game.arcade.game.games.oldmineware.random.CraftLadder;
import ehnetwork.game.arcade.game.games.oldmineware.random.CraftStoneShovel;
import ehnetwork.game.arcade.game.games.oldmineware.random.DamageChicken;
import ehnetwork.game.arcade.game.games.oldmineware.random.DamageFall;
import ehnetwork.game.arcade.game.games.oldmineware.random.DamageGhast;
import ehnetwork.game.arcade.game.games.oldmineware.random.GatherCobble;
import ehnetwork.game.arcade.game.games.oldmineware.random.GatherRedFlower;
import ehnetwork.game.arcade.game.games.oldmineware.random.GatherSand;
import ehnetwork.game.arcade.game.games.oldmineware.random.GatherYellowFlower;
import ehnetwork.game.arcade.game.games.oldmineware.random.PlaceDoor;
import ehnetwork.game.arcade.game.games.oldmineware.random.RideBoat;
import ehnetwork.game.arcade.game.games.oldmineware.random.RidePig;
import ehnetwork.game.arcade.game.games.oldmineware.random.StandAlone;
import ehnetwork.game.arcade.game.games.oldmineware.random.StandShelter;
import ehnetwork.game.arcade.game.games.oldmineware.random.StandStone;
import ehnetwork.game.arcade.game.games.oldmineware.random.StandWater;
import ehnetwork.game.arcade.game.games.runner.kits.KitLeaper;
import ehnetwork.game.arcade.kit.Kit;

public class OldMineWare extends SoloGame
{
	private HashMap<Player, Integer> _lives = new HashMap<Player, Integer>();
	
	private Order _order = null;
	private long _orderTime = 0;
	private int _orderCount = 0;
	
	private ArrayList<Order> _orders = new ArrayList<Order>();
	private ArrayList<Order> _ordersCopy = new ArrayList<Order>();
	
	private Location _ghastLoc = null;
	private Location _ghastTarget = null;
	private Ghast _ghast	= null;
	private ArrayList<Location> _mobLocs = new ArrayList<Location>();
	private ArrayList<Creature> _mobs = new ArrayList<Creature>();
	
	public OldMineWare(ArcadeManager manager) 
	{
		super(manager, GameType.OldMineWare,

				new Kit[]
						{
				new KitLeaper(manager),
						},

				new String[]
						{
				"Follow the orders given in chat!",
				"First half to follow it win the round.",
				"Other players lose one life.",
				"Last player with lives wins!"
						});
		
		this.PrepareFreeze = false;
		
		this.DamagePvP = false;
		
		this.BlockPlace = true;
		this.BlockBreak = true;
		
		this.ItemDrop = true;
		this.ItemPickup = true;
		
		InventoryOpenBlock = true;
		InventoryOpenChest = true;
		InventoryClick = true;
		
		PopulateOrders();
	}
	
	@Override
	public void ParseData() 
	{
		_ghastLoc = WorldData.GetDataLocs("WHITE").get(0);

		while (_mobLocs.size() < 100)
		{
			Location loc = WorldData.GetRandomXZ();
			
			while (UtilBlock.airFoliage(loc.getBlock()))
				loc.add(0, -1, 0);
			
			Material mat = loc.getBlock().getType();
			
			if (mat == Material.STONE ||
				mat == Material.GRASS ||
				mat == Material.SAND)
				_mobLocs.add(loc);
		}
	}
	
	public void PopulateOrders()
	{
		_orders.add(new ActionMilkCow(this));
		_orders.add(new ActionShearSheep(this));
		
		_orders.add(new CraftLadder(this));
		_orders.add(new CraftStoneShovel(this));
		
		_orders.add(new DamageChicken(this));
		_orders.add(new DamageFall(this));
		_orders.add(new DamageGhast(this));
		
		_orders.add(new GatherCobble(this));
		_orders.add(new GatherRedFlower(this));
		_orders.add(new GatherYellowFlower(this));
		_orders.add(new GatherSand(this));
		
		_orders.add(new PlaceDoor(this));
		
		_orders.add(new RideBoat(this));
		_orders.add(new RidePig(this));
		
		_orders.add(new StandAlone(this));
		_orders.add(new StandShelter(this));
		_orders.add(new StandStone(this));
		_orders.add(new StandWater(this));
	}
	
	public Order GetOrder()
	{
		if (_ordersCopy.isEmpty())
		{
			for (Order order : _orders)
			{
				_ordersCopy.add(order);
			}
		}
		
		return _ordersCopy.remove(UtilMath.r(_ordersCopy.size()));
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;
		
		for (Player player : GetPlayers(true))
			_lives.put(player, 10);
	}
	
	@EventHandler
	public void UpdateOrder(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		//New Order
		if (_order == null)
		{
			if (!UtilTime.elapsed(_orderTime, 1000))
				return;
			
			_order = GetOrder();
			
			if (_order == null)
			{
				SetState(GameState.Dead);
				return;
			}
			
			//Register
			UtilServer.getServer().getPluginManager().registerEvents(_order, Manager.getPlugin());
			_order.StartOrder(_orderCount++);

			Announce(C.cYellow + C.Bold + _order.GetOrder().toUpperCase());
			
			/* XXX
			GetObjectiveSide().setDisplayName(
					ChatColor.WHITE + "§lMineWare " + C.cGreen + "§l"
							+ "Round " + _orderCount);
							*/
		}
		//Update Order
		else
		{
			if (_order.Finish())
			{
				_orderTime = System.currentTimeMillis();
				
				if (_order.PlayerHasCompleted())
				{
					for (Player player : GetPlayers(true))
					{
						if (!_order.IsCompleted(player))
						{
							LoseLife(player);
							
							if (IsAlive(player))
								_order.FailItems(player);
						}
					}
				}
				
				
				//Deregister
				HandlerList.unregisterAll(_order);
				_order.EndOrder();
				_order = null;
			}
			else
			{
				//Set Level
				for (Player player : UtilServer.getPlayers())
				{
					player.setLevel(_order.GetRemainingPlaces());
					player.setExp(_order.GetTimeLeftPercent());
				}
			}
		}
	}

	private int GetLives(Player player)
	{
		if (!_lives.containsKey(player))
			return 0;
		
		if (!IsAlive(player))
			return 0;
		
		return _lives.get(player);
	}
	
	private void LoseLife(Player player) 
	{
		int lives = GetLives(player) - 1;
		
		if (lives > 0)
		{
			UtilPlayer.message(player, C.cRed + C.Bold + "You failed the task!");
			UtilPlayer.message(player, C.cRed + C.Bold + "You have " + lives + " lives left!");
			player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 2f, 0.5f);
			
			_lives.put(player, lives);
		} 
		else
		{
			UtilPlayer.message(player, C.cRed + C.Bold + "You are out of the game!");
			player.playSound(player.getLocation(), Sound.EXPLODE, 2f, 1f);
			
			player.damage(5000);
			
			Scoreboard.ResetScore(player.getName());
		}
	}
	
	@EventHandler
	public void UpdateMobs(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (!InProgress())
			return;
		
		Iterator<Creature> mobIterator = _mobs.iterator();

		while (mobIterator.hasNext())
		{	
			Creature mob = mobIterator.next();

			if (!mob.isValid())
			{
				mob.remove();
				mobIterator.remove();
			}
		}
		
		if (_mobs.size() < 200)
		{
			Location loc = _mobLocs.get(UtilMath.r(_mobLocs.size())).clone().add(new Vector(0.5,1,0.5));
			double r = Math.random();
			
			this.CreatureAllowOverride = true;
			
			if (r > 0.75)			_mobs.add(loc.getWorld().spawn(loc, Pig.class));
			else if (r > 0.5)		_mobs.add(loc.getWorld().spawn(loc, Cow.class));
			else if (r > 0.25)		_mobs.add(loc.getWorld().spawn(loc, Chicken.class));
			else					_mobs.add(loc.getWorld().spawn(loc, Sheep.class));
			
			this.CreatureAllowOverride = false;
		}
		
		if (_ghast == null || !_ghast.isValid())
		{
			if (_ghast != null)
				_ghast.remove();
			
			this.CreatureAllowOverride = true;
			_ghast = _ghastLoc.getWorld().spawn(_ghastLoc, Ghast.class);
			this.CreatureAllowOverride = false;
			
			_ghast.setMaxHealth(10000);
			_ghast.setHealth(_ghast.getMaxHealth());
		}
		else
		{
			//New Target
			if (_ghastTarget == null || UtilMath.offset(_ghast.getLocation(), _ghastTarget) < 5)
			{
				_ghastTarget = _ghastLoc.clone().add(40 - 80*Math.random(), -20*Math.random(), 40 - 80*Math.random());
			}
			
			_ghast.teleport(_ghast.getLocation().add(UtilAlg.getTrajectory(_ghast.getLocation(), _ghastTarget).multiply(0.1)));
		}
	}
	
	@EventHandler
	public void GhastTarget(EntityTargetEvent event)
	{
		if (event.getEntity().equals(_ghast))
			event.setCancelled(true);
	}
		
	@Override
	public int GetScoreboardScore(Player player)
	{
		return GetLives(player);
	}
	
	@EventHandler
	public void ItemDrop(PlayerDropItemEvent event)
	{
		event.getItemDrop().remove();
	}
}
