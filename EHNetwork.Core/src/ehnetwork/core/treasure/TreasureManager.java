package ehnetwork.core.treasure;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.hologram.HologramManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.reward.Reward;
import ehnetwork.core.reward.RewardManager;
import ehnetwork.core.reward.RewardType;

/**
 * Created by Shaun on 8/27/2014.
 */
public class TreasureManager extends MiniPlugin
{
	private RewardManager _rewardManager;
	private InventoryManager _inventoryManager;
	private BlockRestore _blockRestore;
	private HologramManager _hologramManager;
	private List<TreasureLocation> _treasureLocations;

	public TreasureManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, InventoryManager inventoryManager, PetManager petManager, BlockRestore blockRestore, HologramManager hologramManager)
	{
		super("Treasure", plugin);

		_inventoryManager = inventoryManager;
		_blockRestore = blockRestore;
		_hologramManager = hologramManager;
		_rewardManager = new RewardManager(clientManager, donationManager, inventoryManager, petManager,
				100, 250,
				500, 1000,
				4000, 6000,
				12000, 32000,
				true);

		World world = Bukkit.getWorlds().get(0);

		_treasureLocations = new ArrayList<TreasureLocation>();
		{
			Block chestBlock = world.getBlockAt(-19, 72, -19);
			Block chestLoc1 = world.getBlockAt(-16, 72, -20);
			Block chestLoc2 = world.getBlockAt(-18, 72, -22);
			Block chestLoc3 = world.getBlockAt(-20, 72, -22);
			Block chestLoc4 = world.getBlockAt(-22, 72, -20);
			Block chestLoc5 = world.getBlockAt(-22, 72, -18);
			Block chestLoc6 = world.getBlockAt(-20, 72, -16);
			Block chestLoc7 = world.getBlockAt(-18, 72, -16);
			Block chestLoc8 = world.getBlockAt(-16, 72, -18);
			Location resetLocation = new Location(world, -23.5, 72, -23.5);
			_treasureLocations.add(new TreasureLocation(this, _inventoryManager, clientManager, donationManager, chestBlock, new Block[]{chestLoc1, chestLoc2, chestLoc3, chestLoc4, chestLoc5, chestLoc6, chestLoc7, chestLoc8}, resetLocation, _hologramManager));
		}

		{
			Block chestBlock = world.getBlockAt(19, 72, 19);
			Block chestLoc1 = world.getBlockAt(16, 72, 20);
			Block chestLoc2 = world.getBlockAt(18, 72, 22);
			Block chestLoc3 = world.getBlockAt(20, 72, 22);
			Block chestLoc4 = world.getBlockAt(22, 72, 20);
			Block chestLoc5 = world.getBlockAt(22, 72, 18);
			Block chestLoc6 = world.getBlockAt(20, 72, 16);
			Block chestLoc7 = world.getBlockAt(18, 72, 16);
			Block chestLoc8 = world.getBlockAt(16, 72, 18);
			Location resetLocation = new Location(world, 23.5, 72, 23.5);
			_treasureLocations.add(new TreasureLocation(this, _inventoryManager, clientManager, donationManager, chestBlock, new Block[]{chestLoc1, chestLoc2, chestLoc3, chestLoc4, chestLoc5, chestLoc6, chestLoc7, chestLoc8}, resetLocation, _hologramManager));
		}

		{
			Block chestBlock = world.getBlockAt(19, 72, -19);
			Block chestLoc1 = world.getBlockAt(16, 72, -20);
			Block chestLoc2 = world.getBlockAt(18, 72, -22);
			Block chestLoc3 = world.getBlockAt(20, 72, -22);
			Block chestLoc4 = world.getBlockAt(22, 72, -20);
			Block chestLoc5 = world.getBlockAt(22, 72, -18);
			Block chestLoc6 = world.getBlockAt(20, 72, -16);
			Block chestLoc7 = world.getBlockAt(18, 72, -16);
			Block chestLoc8 = world.getBlockAt(16, 72, -18);
			Location resetLocation = new Location(world, 23.5, 72, -23.5);
			_treasureLocations.add(new TreasureLocation(this, _inventoryManager, clientManager, donationManager, chestBlock, new Block[]{chestLoc1, chestLoc2, chestLoc3, chestLoc4, chestLoc5, chestLoc6, chestLoc7, chestLoc8}, resetLocation, _hologramManager));
		}

		{
			Block chestBlock = world.getBlockAt(-19, 72, 19);
			Block chestLoc1 = world.getBlockAt(-16, 72, 20);
			Block chestLoc2 = world.getBlockAt(-18, 72, 22);
			Block chestLoc3 = world.getBlockAt(-20, 72, 22);
			Block chestLoc4 = world.getBlockAt(-22, 72, 20);
			Block chestLoc5 = world.getBlockAt(-22, 72, 18);
			Block chestLoc6 = world.getBlockAt(-20, 72, 16);
			Block chestLoc7 = world.getBlockAt(-18, 72, 16);
			Block chestLoc8 = world.getBlockAt(-16, 72, 18);
			Location resetLocation = new Location(world, -23.5, 72, 23.5);
			_treasureLocations.add(new TreasureLocation(this, _inventoryManager, clientManager, donationManager, chestBlock, new Block[]{chestLoc1, chestLoc2, chestLoc3, chestLoc4, chestLoc5, chestLoc6, chestLoc7, chestLoc8}, resetLocation, _hologramManager));
		}

		for (TreasureLocation treasureLocation : _treasureLocations)
		{
			_plugin.getServer().getPluginManager().registerEvents(treasureLocation, _plugin);
		}
	}

	@Override
	public void disable()
	{
		for (TreasureLocation treasureLocation : _treasureLocations)
		{
			treasureLocation.cleanup();
		}
	}

	public Reward[] getRewards(Player player, RewardType rewardType)
	{
		return _rewardManager.getRewards(player, rewardType);
	}

	public boolean isOpening(Player player)
	{
		for (TreasureLocation treasureLocation : _treasureLocations)
		{
			Treasure treasure = treasureLocation.getCurrentTreasure();

			if (treasure == null)
				continue;

			if (treasure.getPlayer().equals(player))
				return true;
		}

		return false;
	}

	public BlockRestore getBlockRestore()
	{
		return _blockRestore;
	}
}
