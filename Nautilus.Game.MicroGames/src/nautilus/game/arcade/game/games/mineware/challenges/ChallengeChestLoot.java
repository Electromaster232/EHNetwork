package nautilus.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;

import mineplex.core.common.util.UtilMath;
import nautilus.game.arcade.game.games.mineware.Challenge;
import nautilus.game.arcade.game.games.mineware.MineWare;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChallengeChestLoot extends Challenge
{
	public ChallengeChestLoot(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Find a diamond in the chests");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> spawns = new ArrayList<Location>();

		for (int x = -7; x <= 7; x++)
		{
			for (int z = -7; z <= 7; z++)
			{
				if (x % 2 == 0 && z % 2 == 0)
				{
					spawns.add(getCenter().clone().add(x, 2.1, z));
				}
			}
		}

		return spawns;
	}

	@Override
	public void cleanupRoom()
	{
	}

	@Override
	public void setupPlayers()
	{
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (!(event.getInventory().getHolder() instanceof Player))
		{
			event.setCancelled(true);

			if (event.getInventory().getHolder() instanceof DoubleChest || event.getInventory().getHolder() instanceof Chest)
			{
				if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.DIAMOND)
				{
					event.setCurrentItem(new ItemStack(Material.AIR));
					SetCompleted((Player) event.getWhoClicked());
				}
			}
		}
	}

	@Override
	public void generateRoom()
	{
		ArrayList<Inventory> invs = new ArrayList<Inventory>();

		for (int x = -7; x <= 7; x++)
		{
			for (int z = -7; z <= 7; z++)
			{
				Block b = getCenter().getBlock().getRelative(x, 0, z);
				b.setType(Material.STAINED_CLAY);
				b.setData((byte) UtilMath.r(16));

				addBlock(b);

				if (Math.abs(x) % 2 == 0 && Math.abs(z) % 2 == 0)
				{
					Block block = b.getRelative(0, 1, 0);
					block.setType(Material.CHEST);
					addBlock(block);

					Inventory inventory = ((Chest) block.getState()).getInventory();
					invs.add(inventory);

					for (int i = 0; i < inventory.getSize(); i++)
					{
						ItemStack item = new ItemStack(Material.values()[UtilMath.r(Material.values().length)]);

						if (item.getType() == Material.DIAMOND || item.getType() == Material.AIR)
						{
							i--;
							continue;
						}

						inventory.setItem(i, item);
					}
				}
				else
				{
					Block block = b.getRelative(BlockFace.UP);
					block.setType(Material.STAINED_CLAY);
					block.setData((byte) UtilMath.r(16));
					addBlock(block);
				}
			}
		}

		for (int i = 0; i < 10 + getChallengers().size(); i++)
		{
			Inventory inv = invs.get(UtilMath.r(invs.size()));
			inv.setItem(UtilMath.r(inv.getSize()), new ItemStack(Material.DIAMOND));
		}
	}
}
