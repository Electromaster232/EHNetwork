package ehnetwork.core.treasure;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.gadget.event.GadgetBlockEvent;
import ehnetwork.core.hologram.Hologram;
import ehnetwork.core.hologram.HologramManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.reward.Reward;
import ehnetwork.core.treasure.event.TreasureFinishEvent;
import ehnetwork.core.treasure.event.TreasureStartEvent;
import ehnetwork.core.treasure.gui.TreasureShop;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class TreasureLocation implements Listener
{
	private TreasureManager _treasureManager;
	private InventoryManager _inventoryManager;
	private Hologram _hologram;
	private HologramManager _hologramManager;
	private Treasure _currentTreasure;
	private Block _chestBlock;
	private byte _chestBlockData;
	private Block[] _chestSpawns;
	private TreasureShop _shop;
	private Location _resetLocation;

	public TreasureLocation(TreasureManager treasureManager, InventoryManager inventoryManager, CoreClientManager clientManager, DonationManager donationManager, Block chestBlock, Block[] chestSpawns, Location resetLocation, HologramManager hologramManager)
	{
		_treasureManager = treasureManager;
		_resetLocation = resetLocation;
		_inventoryManager = inventoryManager;
		_chestBlock = chestBlock;
		_chestBlockData = _chestBlock.getData();
		_chestSpawns = chestSpawns;
		_hologramManager = hologramManager;
		_currentTreasure = null;
		_hologram = new Hologram(_hologramManager, chestBlock.getLocation().add(0.5, 2.5, 0.5), C.cGreen + C.Bold + "Open Treasure");
		setHoloChestVisible(true);
		_shop = new TreasureShop(treasureManager, _inventoryManager, clientManager, donationManager, this);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().equals(_chestBlock))
		{
			openShop(event.getPlayer());
			event.setCancelled(true);
		}
	}

	public void attemptOpenTreasure(Player player, TreasureType treasureType)
	{
		if (isTreasureInProgress())
		{
			player.sendMessage(F.main("Treasure", "Please wait for the current chest to be opened"));
			return;
		}

		if (!chargeAccount(player, treasureType))
		{
			player.sendMessage(F.main("Treasure", "You dont have any chests to open!"));
			return;
		}

		TreasureStartEvent event = new TreasureStartEvent(player);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
		{
			return;
		}

		setHoloChestVisible(false);

		if (treasureType == TreasureType.ANCIENT)
			Bukkit.broadcastMessage(F.main("Treasure", F.name(player.getName()) + " is opening an " + treasureType.getName()));
		
		if (treasureType == TreasureType.MYTHICAL)
			Bukkit.broadcastMessage(F.main("Treasure", F.name(player.getName()) + " is opening a " + treasureType.getName()));

		Reward[] rewards = _treasureManager.getRewards(player, treasureType.getRewardType());
		Treasure treasure = new Treasure(player, rewards, _chestBlock, _chestSpawns, treasureType, _treasureManager.getBlockRestore(), _hologramManager);
		_currentTreasure = treasure;

		UtilTextMiddle.display(treasureType.getName(), "Choose 4 Chests To Open", 20, 180, 20, player);
		UtilPlayer.message(player, F.main("Treasure", "Choose 4 Chests To Open"));

		Location teleportLocation = treasure.getCenterBlock().getLocation().add(0.5, 0, 0.5);
		teleportLocation.setPitch(player.getLocation().getPitch());
		teleportLocation.setYaw(player.getLocation().getYaw());

		for (Entity entity :  player.getNearbyEntities(3, 3, 3))
		{
			UtilAction.velocity(entity, UtilAlg.getTrajectory(entity.getLocation(), treasure.getCenterBlock().getLocation()).multiply(-1), 1.5, true, 0.8, 0, 1.0, true);
		}

		player.teleport(teleportLocation);

	}

	private boolean chargeAccount(Player player, TreasureType treasureType)
	{
		int itemCount = _inventoryManager.Get(player).getItemCount(treasureType.getItemName());
		if (itemCount > 0)
		{
			_inventoryManager.addItemToInventory(player, "Item", treasureType.getItemName(), -1);
			return true;
		}
		return false;
	}

	private void setHoloChestVisible(boolean visible)
	{
		if (visible)
		{
			_hologram.start();
			_chestBlock.setType(Material.CHEST);
			_chestBlock.setData(_chestBlockData);
		}
		else
		{
			_hologram.stop();
			_chestBlock.setType(Material.AIR);
		}
	}

	public void cleanup()
	{
		if (_currentTreasure != null)
		{
			_currentTreasure.cleanup();
			_currentTreasure = null;
		}
	}

	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (isTreasureInProgress())
		{
			Treasure treasure = _currentTreasure;

			treasure.update();

			if (!treasure.getPlayer().isOnline() || (treasure.isFinished() && treasure.getFinishedTickCount() >= 80))
			{
				treasure.cleanup();

				TreasureFinishEvent finishEvent = new TreasureFinishEvent(treasure.getPlayer(), treasure);
				Bukkit.getPluginManager().callEvent(finishEvent);
			}
		}
	}

	@EventHandler
	public void onTreasureFinish(TreasureFinishEvent event)
	{
		if (event.getTreasure().equals(_currentTreasure))
		{
			Player player = _currentTreasure.getPlayer();
			player.teleport(_resetLocation);
			_currentTreasure = null;
			setHoloChestVisible(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void interact(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (isTreasureInProgress())
		{
			if (_currentTreasure.getPlayer().equals(player))
			{
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					_currentTreasure.openChest(event.getClickedBlock());
				}
				event.setCancelled(true);
			}
			else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				ChestData chestData = _currentTreasure.getChestData(event.getClickedBlock());
				if (chestData != null)
				{
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void inventoryOpen(InventoryOpenEvent event)
	{
		// Ignore punish gui
		if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Punish"))
			return;

		if (isTreasureInProgress() && event.getPlayer().equals(_currentTreasure.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void cancelMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		if (isTreasureInProgress())
		{
			if (_currentTreasure.getPlayer().equals(player))
			{
				Treasure treasure = _currentTreasure;
				Location centerLocation = treasure.getCenterBlock().getLocation().add(0.5, 0.5, 0.5);
				if (event.getTo().distanceSquared(centerLocation) > 9)
				{
					Location newTo = event.getFrom();
					newTo.setPitch(event.getTo().getPitch());
					newTo.setYaw(event.getTo().getYaw());
					event.setTo(newTo);
				}
			}
			else
			{
				Location fromLocation = event.getFrom();
				Location toLocation = event.getTo();
				Location centerLocation = _currentTreasure.getCenterBlock().getLocation().add(0.5, 1.5, 0.5);
				double toDistanceFromCenter = centerLocation.distanceSquared(toLocation);

				if (toDistanceFromCenter <= 16)
				{
					// Only cancel movement if they are moving towards the center
					double fromDistanceFromCenter = centerLocation.distanceSquared(fromLocation);
					if (toDistanceFromCenter < fromDistanceFromCenter)
					{
						Location spawnLocation = new Location(player.getWorld(), 0, 64, 0);
						UtilAction.velocity(player, UtilAlg.getTrajectory(player.getLocation(), spawnLocation).multiply(-1), 1.5, true, 0.8, 0, 1.0, true);
//						event.setTo(event.getFrom());
					}
				}
			}
		}
	}

	@EventHandler
	public void cancelVelocity(PlayerVelocityEvent event)
	{
		Player player = event.getPlayer();
		if (isTreasureInProgress() && _currentTreasure.getPlayer().equals(player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void preventGadgetBlockEvent(GadgetBlockEvent event)
	{
		List<Block> blocks = event.getBlocks();

		int x = _chestBlock.getX();
		int y = _chestBlock.getY();
		int z = _chestBlock.getZ();

		for (Block block : blocks)
		{
			int dx = Math.abs(x - block.getX());
			int dy = Math.abs(y - block.getY());
			int dz = Math.abs(z - block.getZ());

			if (dx <= 4 && dz <= 4 && dy <= 4)
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		if (isTreasureInProgress() && _currentTreasure.getPlayer().equals(event.getPlayer()))
		{
			reset();
		}
	}

	public boolean isTreasureInProgress()
	{
		return _currentTreasure != null;
	}

	public void reset()
	{
		cleanup();
		_chestBlock.setType(Material.CHEST);
		_chestBlock.setData(_chestBlockData);
		_hologram.start();
	}

	public Treasure getCurrentTreasure()
	{
		return _currentTreasure;
	}

	public void openShop(Player player)
	{
		_shop.attemptShopOpen(player);
	}
}
