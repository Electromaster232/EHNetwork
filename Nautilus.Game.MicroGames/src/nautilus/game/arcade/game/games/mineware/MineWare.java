package nautilus.game.arcade.game.games.mineware;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.core.common.util.UtilTextTop;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.GameTeam.PlayerState;
import nautilus.game.arcade.game.games.holeinwall.KitNormal;
import nautilus.game.arcade.game.games.mineware.challenges.*;
import nautilus.game.arcade.kit.Kit;

public class MineWare extends SoloGame
{
	private HashMap<Player, Integer> _lives = new HashMap<Player, Integer>();

	private Challenge _order;
	private ArrayList<Block> _lastOrderBlocks;
	private long _orderTime;
	private boolean _orderWaiting = true;

	private ArrayList<Class<? extends Challenge>> _orders = new ArrayList<Class<? extends Challenge>>();
	private ArrayList<Class<? extends Challenge>> _ordersCopy = new ArrayList<Class<? extends Challenge>>();

	public MineWare(ArcadeManager manager)
	{
		super(manager, GameType.MineWare,

		new Kit[]
		{
			new KitNormal(manager),
		},

		new String[]
		{
				"Follow the orders given in chat!",
				"First half to follow it win the round.",
				"Other players lose one life.", "Last player with lives wins!"
		});

		DamageTeamSelf = true;
		DamagePvP = false;
		DamagePvE = false;
		DamageEvP = false;
		DamageFall = false;
		InventoryClick = true;
		DamageSelf = false;
		DeathOut = false;
		AutomaticRespawn = false;
		DeathMessages = false;

		Manager.GetCreature().SetDisableCustomDrops(true);

		PopulateOrders();
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		int living = 0;

		for (Player player : GetPlayers(false))
		{
			if (GetLives(player) > 0)
			{
				living++;
			}
		}

		if (living <= 1)
		{
			SetState(GameState.End);
			AnnounceEnd(getWinners());
		}
	}

	@Override
	public boolean isInsideMap(Player player)
	{
		if (_order != null && !_orderWaiting)
		{
			return _order.isInsideMap(player);
		}

		return true;
	}

	@EventHandler
	public void onLive(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
		{
			return;
		}

		_orderTime = System.currentTimeMillis();
	}

	public void PopulateOrders()
	{
		_orders.add(ChallengeShootChickens.class);
		_orders.add(ChallengeStandOnColor.class);
		_orders.add(ChallengeMilkACow.class);
		_orders.add(ChallengeDragonEgg.class);
		// _orders.add(ChallengeBlockShot.class);
		_orders.add(ChallengeChestLoot.class);
		_orders.add(ChallengeLavaRun.class);
		_orders.add(ChallengeNavigateMaze.class);
		// _orders.add(ChallengePickASide.class);
		_orders.add(ChallengeHitTargets.class);
		// _orders.add(ChallengeNameThatSound.class);
		_orders.add(ChallengeVolleyPig.class);
		// _orders.add(ChallengeSkyFall.class);
		_orders.add(ChallengeSmashOff.class);
		_orders.add(ChallengeTntLauncher.class);
		//_orders.add(ChallengeSpleef.class); TODO
		//_orders.add(ChallengeRunner.class); TODO
		// _orders.add(ChallengeDiamondFall.class);
	}

	public Challenge GetOrder()
	{
		for (int i = 0; i < _orders.size() * 4; i++)
		{
			try
			{
				if (_ordersCopy.isEmpty())
				{
					_ordersCopy.addAll(_orders);
				}

				Challenge challenge = _ordersCopy
						.remove(UtilMath.r(_ordersCopy.size()))
						.getConstructor(MineWare.class).newInstance(this);

				if (getChallengers().size() >= challenge.getMinPlayers())
				{
					System.out.print("Using challenge "
							+ challenge.getClass().getSimpleName());
					return challenge;
				}
				else
				{
					System.out.print("Cannot use challenge "
							+ challenge.getClass().getSimpleName()
							+ ", not enough players");

				}
			}
			catch (InvocationTargetException ex)
			{
				ex.getCause().printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;

		for (Player player : GetPlayers(true))
			_lives.put(player, 5);

		_order = GetOrder();
		_order.generateRoom();

		GetTeamList().get(0).SetSpawns(_order.getSpawns());
		SpectatorSpawn = UtilWorld.averageLocation(_order.getSpawns()).add(0,
				7, 0);
	}

	@EventHandler
	public void CancelOrder(GameStateChangeEvent event)
	{
		if (_order == null)
			return;

		if (_orderWaiting)
			return;

		if (event.GetState() == GameState.Live)
			return;

		// Deregister
		HandlerList.unregisterAll(_order);

		_order.EndOrder();
	}

	@EventHandler
	public void onDamage(CustomDamageEvent event)
	{
		event.SetDamageToLevel(false);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		if (_order == null)
			return;

		_order.getLost().add(event.getEntity());
		LoseLife(event.getEntity(), true);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		_lives.remove(event.getPlayer());
	}

	public ArrayList<Player> getChallengers()
	{
		ArrayList<Player> challengers = new ArrayList<Player>();

		for (Player player : GetPlayers(true))
		{
			if (!UtilPlayer.isSpectator(player))
			{
				challengers.add(player);
			}
		}

		return challengers;
	}

	public void sayChallenge(Challenge challenge)
	{
		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
			String message = C.cYellow
					+ C.Bold
					+ (IsAlive(player) ? challenge.getMessage(player)
							: challenge.GetOrder());

			UtilPlayer.message(player, message);
			UtilTextMiddle.display(message, null);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if (!_orderWaiting)
		{
			return;
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void PlayerMoveCancel(PlayerMoveEvent event)
	{
		if (!PrepareFreeze)
			return;

		if (!_orderWaiting)
			return;

		if (!IsLive())
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		if (UtilMath.offset2d(event.getFrom(), event.getTo()) <= 0)
			return;

		event.getFrom().setPitch(event.getTo().getPitch());
		event.getFrom().setYaw(event.getTo().getYaw());

		event.setTo(event.getFrom());
	}

	@EventHandler
	public void UpdateOrder(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (!IsLive())
			return;

		// New Order
		if (_order == null)
		{
			if (!UtilTime.elapsed(_orderTime, 1500))
				return;

			for (Player player : GetPlayers(false))
			{
				if (_lives.containsKey(player) && _lives.get(player) > 0)
				{
					Manager.Clear(player);
				}
			}

			_order = GetOrder();

			if (_order == null)
			{
				System.out.println("No challenge suitable");
				SetState(GameState.Dead);
				return;
			}

			for (Block block : _lastOrderBlocks)
			{
				if (block.getState() instanceof InventoryHolder)
				{
					((InventoryHolder) block.getState()).getInventory().clear();
				}

				block.setTypeIdAndData(Material.AIR.getId(), (byte) 0, false);
			}

			_order.generateRoom();

			ArrayList<Location> spawns = _order.getSpawns();

			GetTeamList().get(0).SetSpawns(_order.getSpawns());
			SpectatorSpawn = UtilWorld.averageLocation(spawns).add(0, 7, 0);

			ArrayList<Location> toTeleport = new ArrayList<Location>();

			for (int i = 0; i < spawns.size(); i++)
			{
				Location furthest = null;
				double furthestDist = 0;

				for (Location spawn : spawns)
				{
					if (toTeleport.contains(spawn))
						continue;

					double dist = 0;

					for (Location loc : toTeleport)
					{
						dist += loc.distance(spawn);
					}

					if (furthest == null || furthestDist < dist)
					{
						furthest = spawn;
						furthestDist = dist;
					}
				}

				toTeleport.add(furthest);
			}

			int i = 0;

			for (Player player : getChallengers())
			{
				player.teleport(toTeleport.get(i++));

				if (i >= toTeleport.size())
				{
					i = 0;
				}
			}

			for (Player player : GetPlayers(false))
			{
				if (!IsAlive(player))
				{
					player.teleport(SpectatorSpawn);
				}
			}

			if (_order instanceof ChallengeSeperateRooms)
			{
				((ChallengeSeperateRooms) _order).assignRooms();
			}

			_orderTime = System.currentTimeMillis();
			_orderWaiting = true;
			/*
			 * XXX GetObjectiveSide().setDisplayName( ChatColor.WHITE +
			 * "§lMineWare " + C.cGreen + "§l" + "Round " + _orderCount);
			 */
		}
		else if (_orderWaiting)
		{
			if (!UtilTime.elapsed(_orderTime, 1000))
				return;

			_orderWaiting = false;

			_order.StartOrder();

			// Register
			UtilServer.getServer().getPluginManager()
					.registerEvents(_order, Manager.getPlugin());

			sayChallenge(_order);
		}
		// Update Order
		else
		{
			if (_order.Finish())
			{
				_orderTime = System.currentTimeMillis();

				for (Player player : getChallengers())
				{
					getArcadeManager().GetDisguise().undisguise(player);
					UtilInv.Clear(player);

					if (_order.hasWinner() && !_order.IsCompleted(player))
					{
						LoseLife(player, true);
					}
				}

				// Deregister
				HandlerList.unregisterAll(_order);

				_order.EndOrder();

				_lastOrderBlocks = new ArrayList<Block>(
						_order.getModifiedBlocks());
				// Remove blocks from top to bottom, prevents blocks popping
				// off.
				Collections.sort(_lastOrderBlocks, new Comparator<Block>()
				{

					@Override
					public int compare(Block o1, Block o2)
					{
						return new Integer(o2.getY()).compareTo(o1.getY());
					}
				});

				_order = null;

				EndCheck();
			}
			else
			{
				// Set Level
				for (Player player : UtilServer.getPlayers())
				{
					UtilTextTop.display(C.cYellow
							+ C.Bold
							+ (IsAlive(player) ? _order.getMessage(player)
									: _order.GetOrder()), player);
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

		return _lives.get(player);
	}

	public void LoseLife(Player player, boolean isDeath)
	{
		if (!isDeath)
		{
			Manager.addSpectator(player, true);
		}

		if (_order == null || _order.IsCompleted(player))
			return;

		int lives = GetLives(player);

		lives -= 1;
		_lives.put(player, lives);

		if (lives > 0)
		{
			UtilPlayer
					.message(player, C.cRed + C.Bold + "You failed the task!");
			UtilPlayer.message(player, C.cRed + C.Bold + "You have " + lives
					+ " lives left!");
			player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 2f,
					0.5f);
		}
		else
		{
			UtilPlayer.message(player, C.cRed + C.Bold
					+ "You are out of the game!");
			player.playSound(player.getLocation(), Sound.EXPLODE, 2f, 1f);

			Scoreboard.ResetScore(player.getName());

			SetPlayerState(player, PlayerState.OUT);

			if (isDeath)
			{
				Manager.addSpectator(player, true);
			}
		}
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
