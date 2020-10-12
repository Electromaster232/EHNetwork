package ehnetwork.game.microgames.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.game.microgames.game.games.mineware.Challenge;
import ehnetwork.game.microgames.game.games.mineware.MineWare;

public class ChallengeDiamondFall extends Challenge
{

	public ChallengeDiamondFall(MineWare host)
	{
		super(host, ChallengeType.LastStanding, "Loot the most diamonds from chests!");
	}

	@Override
	public boolean Finish()
	{
		if (super.Finish())
		{
			final HashMap<Player, Integer> diamonds = new HashMap<Player, Integer>();
			ArrayList<Player> players = getChallengers();

			for (Player player : players)
			{
				int diamond = 0;

				for (ItemStack item : UtilInv.getItems(player))
				{
					if (item != null && item.getType() == Material.DIAMOND)
					{
						diamond += item.getAmount();
					}
				}

				diamonds.put(player, diamond);
			}

			Collections.sort(players, new Comparator<Player>()
			{

				@Override
				public int compare(Player o1, Player o2)
				{
					return diamonds.get(o2).compareTo(diamonds.get(o1));
				}
			});

			for (int places = 0; places < Math.min(players.size(), Places); places++)
			{
				SetCompleted(players.get(places));
			}
		}

		return super.Finish();
	}

	@Override
	public long getMaxTime()
	{
		return 30000;
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> locations = new ArrayList<Location>();

		for (int x = -3; x <= 3; x++)
		{
			for (int z = -3; z <= 3; z++)
			{
				locations.add(getCenter().add(x + 0.5, 201.5, z + 0.5));
			}
		}

		return locations;
	}

	@Override
	public void generateRoom()
	{
		for (int x = -3; x <= 3; x++)
		{
			for (int z = -3; z <= 3; z++)
			{
				Block block = getCenter().getBlock().getRelative(x, 200, z);
				block.setType(Material.STAINED_GLASS);
				addBlock(block);
			}
		}

		for (int x = -5; x <= 5; x++)
		{
			for (int z = -5; z <= 5; z++)
			{
				Block block = getCenter().getBlock().getRelative(x, 0, z);
				block.setType(Material.STAINED_CLAY);
				block.setData((byte) UtilMath.r(16));
				addBlock(block);
			}
		}

		for (int i = 0; i < 70; i++)
		{
			int y = UtilMath.r(160) + 10;

			Block b = getCenter().getBlock().getRelative(UtilMath.r(16) - 8, y, UtilMath.r(16) - 8);
			boolean chestFound = false;

			for (BlockFace face : new BlockFace[]
				{
						BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH
				})
			{
				Block block = b.getRelative(face);

				if (block.getType() != Material.AIR)
				{
					chestFound = true;
					break;
				}
			}

			if (chestFound)
			{
				continue;
			}

			b.setType(Material.CHEST);

			addBlock(b);

			Inventory inventory = ((Chest) b.getState()).getInventory();

			for (int a = 0; a < UtilMath.r(5); a++)
			{
				inventory.setItem(UtilMath.r(inventory.getSize()), new ItemStack(Material.DIAMOND));
			}
		}
	}

	@Override
	public void cleanupRoom()
	{
	}

	@Override
	public void setupPlayers()
	{
	}

}
