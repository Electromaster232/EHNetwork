package nautilus.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;

import mineplex.core.common.util.UtilPlayer;
import nautilus.game.arcade.game.games.mineware.Challenge;
import nautilus.game.arcade.game.games.mineware.MineWare;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChallengeNavigateMaze extends Challenge
{

	public ChallengeNavigateMaze(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Go to the other side of the maze");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> spawns = new ArrayList<Location>();

		for (int z = -9; z <= 12; z++)
		{
			Location loc = getCenter().clone().add(-16, 1.1, z);
			spawns.add(loc);
		}

		return spawns;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		if (UtilPlayer.isSpectator(event.getPlayer()))
			return;

		if (event.getTo().getY() >= 1 && event.getTo().getX() > 16)
		{
			SetCompleted(event.getPlayer());
		}
	}

	@Override
	public void cleanupRoom()
	{
	}

	@Override
	public void setupPlayers()
	{
		setBorder(-20, 22, 0, 10, -20, 22);
	}

	@Override
	public void generateRoom()
	{
		for (int x = -18; x <= 21; x++)
		{
			for (int z = -12; z <= 15; z++)
			{
				Block b = getCenter().getBlock().getRelative(x, 0, z);

				if (z == -12 || z == 15)
				{
					for (int y = 1; y <= 3; y++)
					{
						Block block = b.getRelative(0, y, 0);
						block.setType(Material.STONE);
						block.setData((byte) 0);
						addBlock(block);
					}
				}

				if (x > 15 || x < -12)
				{
					b.setType(Material.WOOL);
					b.setData((byte) (x < 0 ? 14 : 13));
				}
				else
				{
					b.setType(Material.STONE);
					b.setData((byte) 0);

					Block block = b.getRelative(0, 4, 0);

					if (block.getX() == -12)
					{
						block.setType(Material.STONE);
						block.setData((byte) 0);
					}
					else
					{
						block.setType(Material.STAINED_GLASS);
						block.setData((byte) 8);
					}

					addBlock(block);
				}

				addBlock(b);
			}
		}

		for (int i = 0; i < 30; i++)
		{
			ArrayList<Block> mazeBlocks = generateMaze();

			if (mazeValid())
			{
				for (Block b : mazeBlocks)
				{
					addBlock(b);
				}
				break;
			}
			else
			{
				System.out.print("Generated bad maze, trying again..");
				for (Block b : mazeBlocks)
				{
					b.setTypeIdAndData(Material.AIR.getId(), (byte) 0, false);
				}
			}
		}
	}

	private boolean mazeValid()
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Block> nextLoop = new ArrayList<Block>();

		nextLoop.add(getCenter().getBlock().getRelative(-15, 1, 0));

		blocks.addAll(nextLoop);

		while (!nextLoop.isEmpty())
		{
			Block block = nextLoop.remove(0);

			for (BlockFace face : new BlockFace[]
				{
						BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH
				})
			{
				Block b = block.getRelative(face);

				if (blocks.contains(b))
					continue;

				blocks.add(b);

				if (b.getType() == Material.STONE || b.getX() < -14 || b.getZ() < -12 || b.getZ() > 15)
					continue;

				if (b.getX() >= 15)
				{
					return true;
				}

				nextLoop.add(b);
			}
		}

		return false;
	}

	private ArrayList<Block> generateMaze()
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		int[][] maze = new MazeGenerator(11, 10).getMaze();

		// Divisable by 2's are up and down walls
		// Other's are left and right. All walls have a corner block.

		// This means I need to loop over all of the blocks, figure out if its a corner if yes then set block
		// Else if its a wall, then figure out if I'm making the wall or not
		for (int x = 1; x < 11; x++)
		{
			for (int z = 1; z < 10; z++)
			{
				Block b = getCenter().getBlock().getRelative((x - 5) * 3, 1, (z - 5) * 3);

				for (int y = 0; y < 3; y++)
				{
					Block block = b.getRelative(0, y, 0);

					if (block.getType() == Material.STONE)
						continue;

					block.setType(Material.STONE);
					block.setData((byte) 0);
					blocks.add(block);
				}

				// Set blocks for x
				if (x < 10 && (maze[x][z] & 8) == 0)
				{
					for (int i = 1; i <= 2; i++)
					{
						for (int y = 0; y < 3; y++)
						{
							Block block = b.getRelative(i, y, 0);

							if (block.getType() == Material.STONE)
								continue;

							block.setType(Material.STONE);
							block.setData((byte) 0);
							blocks.add(block);
						}
					}

				}

				// Set blocks for z
				if ((maze[x][z] & 1) == 0)
				{
					for (int i = 1; i <= 2; i++)
					{
						for (int y = 0; y < 3; y++)
						{
							Block block = b.getRelative(0, y, i);

							if (block.getType() == Material.STONE)
								continue;

							block.setType(Material.STONE);
							block.setData((byte) 0);
							blocks.add(block);
						}
					}

				}
			}
		}

		return blocks;
	}
}
