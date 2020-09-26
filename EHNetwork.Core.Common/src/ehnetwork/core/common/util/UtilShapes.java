package ehnetwork.core.common.util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class UtilShapes
{
	private final static BlockFace[] radial =
		{
				BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH,
				BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST
		};

	public static ArrayList<Location> getCircle(Location loc, boolean hollow, double radius)
	{
		return getCircleBlocks(loc, radius, 0, hollow, false);
	}

	public static ArrayList<Location> getSphereBlocks(Location loc, double radius, double height, boolean hollow)
	{
		return getCircleBlocks(loc, radius, height, hollow, true);
	}

	private static ArrayList<Location> getCircleBlocks(Location loc, double radius, double height, boolean hollow, boolean sphere)
	{
		ArrayList<Location> circleblocks = new ArrayList<Location>();
		double cx = loc.getBlockX();
		double cy = loc.getBlockY();
		double cz = loc.getBlockZ();

		for (double y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height + 1); y++)
		{
			for (double x = cx - radius; x <= cx + radius; x++)
			{
				for (double z = cz - radius; z <= cz + radius; z++)
				{
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);

					if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1)))
					{
						Location l = new Location(loc.getWorld(), x, y, z);
						circleblocks.add(l);
					}
				}
			}
		}

		return circleblocks;
	}

	/**
	 * Gets the block at the exact corners, will return a diagonal.
	 * 
	 * @Yeah ik this code sucks.
	 */
	public static BlockFace[] getCornerBlockFaces(Block b, BlockFace facing)
	{
		BlockFace left = null;
		BlockFace right = null;
		for (int i = 0; i < radial.length; i++)
		{
			if (radial[i] == facing)
			{
				int high = i + 2;
				if (high >= radial.length)
					high = high - radial.length;
				int low = i - 2;
				if (low < 0)
					low = radial.length + low;
				left = radial[low];
				right = radial[high];
				return new BlockFace[]
					{
							left, right
					};
			}
		}
		return null;
	}

	public static Block[] getCornerBlocks(Block b, BlockFace facing)
	{
		BlockFace[] faces = getSideBlockFaces(facing);
		return new Block[]
			{
					b.getRelative(faces[0]), b.getRelative(faces[1])
			};
	}

	public static BlockFace getFacing(float yaw)
	{
		return radial[Math.round(yaw / 45f) & 0x7];
	}

	public static ArrayList<Location> getLinesDistancedPoints(Location startingPoint, Location endingPoint,
			double distanceBetweenParticles)
	{
		return getLinesLimitedPoints(startingPoint, endingPoint,
				(int) Math.ceil(startingPoint.distance(endingPoint) / distanceBetweenParticles));
	}

	public static ArrayList<Location> getLinesLimitedPoints(Location startingPoint, Location endingPoint, int amountOfPoints)
	{
		startingPoint = startingPoint.clone();
		Vector vector = endingPoint.toVector().subtract(startingPoint.toVector());
		vector.normalize();
		vector.multiply(startingPoint.distance(endingPoint) / (amountOfPoints + 1D));

		ArrayList<Location> locs = new ArrayList<Location>();
		for (int i = 0; i < amountOfPoints; i++)
		{
			locs.add(startingPoint.add(vector).clone());
		}
		return locs;
	}

	public static ArrayList<Location> getPointsInCircle(Location center, int pointsAmount, double circleRadius)
	{
		ArrayList<Location> locs = new ArrayList<Location>();

		for (int i = 0; i < pointsAmount; i++)
		{
			double angle = ((2 * Math.PI) / pointsAmount) * i;
			double x = circleRadius * Math.cos(angle);
			double z = circleRadius * Math.sin(angle);
			Location loc = center.clone().add(x, 0, z);
			locs.add(loc);
		}
		return locs;
	}

	public static ArrayList<Location> getDistancedCircle(Location center, double pointsDistance, double circleRadius)
	{
		return getPointsInCircle(center, (int) ((circleRadius * Math.PI * 2) / pointsDistance), circleRadius);
	}

	/**
	 * Returns a north/west/east/south block, never a diagonal.
	 */
	public static BlockFace[] getSideBlockFaces(BlockFace facing)
	{
		return getSideBlockFaces(facing, true);
	}

	public static BlockFace[] getSideBlockFaces(BlockFace facing, boolean allowDiagonal)
	{

		int[][] facesXZ;
		allowDiagonal = !allowDiagonal && (facing.getModX() != 0 && facing.getModZ() != 0);

		facesXZ = new int[][]
			{

					new int[]
						{
								allowDiagonal ? facing.getModX() : facing.getModZ(), allowDiagonal ? 0 : -facing.getModX()
						},

					new int[]
						{
								allowDiagonal ? 0 : -facing.getModZ(), allowDiagonal ? facing.getModZ() : facing.getModX()
						}
			};

		BlockFace[] faces = new BlockFace[2];

		for (int i = 0; i < 2; i++)
		{
			int[] f = facesXZ[i];

			for (BlockFace face : BlockFace.values())
			{
				if (face.getModY() == 0)
				{
					if (f[0] == face.getModX() && f[1] == face.getModZ())
					{
						faces[i] = face;
						break;
					}
				}
			}
		}

		if (allowDiagonal && (facing == BlockFace.NORTH_EAST || facing == BlockFace.SOUTH_WEST))
		{
			faces = new BlockFace[]
				{
						faces[1], faces[0]
				};
		}

		return faces;
	}

	public static ArrayList<Block> getDiagonalBlocks(Block block, BlockFace facing, int blockWidth)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();

		if (facing.getModX() == 0 || facing.getModZ() == 0)
		{
			return blocks;
		}

		BlockFace[] faces = getSideBlockFaces(facing);

		for (BlockFace face : faces)
		{
			Location loc = block.getLocation().add(0.5 + (facing.getModX() / 2D), 0, 0.5 + (facing.getModZ() / 2D));

			blocks.add(loc.add(face.getModX() / 2D, 0, face.getModZ() / 2D).getBlock());

			for (int i = 1; i < blockWidth; i++)
			{
				blocks.add(loc.add(face.getModX(), 0, face.getModZ()).getBlock());
			}
		}

		return blocks;
	}

	public static Block[] getSideBlocks(Block b, BlockFace facing)
	{
		BlockFace[] faces = getSideBlockFaces(facing);

		return new Block[]
			{
					b.getRelative(faces[0]), b.getRelative(faces[1])
			};
	}

}
