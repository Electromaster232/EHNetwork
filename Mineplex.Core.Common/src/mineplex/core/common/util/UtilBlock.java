package mineplex.core.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.inventory.ItemStack;

public class UtilBlock
{
	
	public static void main(String[] args) {
		
		for (Material m : Material.values()) {
		
			boolean thisSolid = fullSolid(m.getId());
			boolean solid = m.isSolid();
			if (thisSolid != solid) {
				StringBuilder sb = new StringBuilder();
				sb.append("Failed: ");
				sb.append(m.name());
				int amount = 40 - sb.length();
				for (int i = 0; i < amount; i++) {
					sb.append(" ");
				}
				sb.append(thisSolid);
				System.out.println(sb);
			}
				
		}
		System.out.println("done!");
		
	}
	
	
    /**
     * A list of blocks that are usable
     */
    public static HashSet<Byte> blockUseSet = new HashSet<Byte>();
    /**
     * A list of blocks that are always solid and can be stood on
     */
    public static HashSet<Byte> fullSolid = new HashSet<Byte>();
    /**
     * A list of blocks that are non-solid, but offer resistance. Eg lily, fence gate, portal
     */
    public static HashSet<Byte> blockPassSet = new HashSet<Byte>();
    /**
     * A list of blocks that offer zero resistance (long grass, torch, flower)
     */
    public static HashSet<Byte> blockAirFoliageSet = new HashSet<Byte>();
    
    static {
        blockAirFoliageSet.add((byte)0);    
        blockAirFoliageSet.add((byte)6);    
        blockAirFoliageSet.add((byte)31);   
        blockAirFoliageSet.add((byte)32);   
        blockAirFoliageSet.add((byte)37);   
        blockAirFoliageSet.add((byte)38);   
        blockAirFoliageSet.add((byte)39);   
        blockAirFoliageSet.add((byte)40);   
        blockAirFoliageSet.add((byte)51);   
        blockAirFoliageSet.add((byte)59);   
        blockAirFoliageSet.add((byte)104);  
        blockAirFoliageSet.add((byte)105);  
        blockAirFoliageSet.add((byte)115);  
        blockAirFoliageSet.add((byte)141);  
        blockAirFoliageSet.add((byte)142);
        

        blockPassSet.add((byte)0);
        blockPassSet.add((byte)6);
        blockPassSet.add((byte)8);
        blockPassSet.add((byte)9);
        blockPassSet.add((byte)10);
        blockPassSet.add((byte)11);
        blockPassSet.add((byte)26);
        blockPassSet.add((byte)27);
        blockPassSet.add((byte)28);
        blockPassSet.add((byte)30);
        blockPassSet.add((byte)31);
        blockPassSet.add((byte)32);
        blockPassSet.add((byte)37);
        blockPassSet.add((byte)38);
        blockPassSet.add((byte)39);
        blockPassSet.add((byte)40);
        blockPassSet.add((byte)50);
        blockPassSet.add((byte)51);
        blockPassSet.add((byte)55);
        blockPassSet.add((byte)59);
        blockPassSet.add((byte)63);
        blockPassSet.add((byte)64);
        blockPassSet.add((byte)65);
        blockPassSet.add((byte)66);
        blockPassSet.add((byte)68);
        blockPassSet.add((byte)69);
        blockPassSet.add((byte)70);
        blockPassSet.add((byte)71);
        blockPassSet.add((byte)72);
        blockPassSet.add((byte)75);
        blockPassSet.add((byte)76);
        blockPassSet.add((byte)77);
        blockPassSet.add((byte)78);
        blockPassSet.add((byte)83);
        blockPassSet.add((byte)90);
        blockPassSet.add((byte)92);
        blockPassSet.add((byte)93);
        blockPassSet.add((byte)94);
        blockPassSet.add((byte)96);
        blockPassSet.add((byte)101);
        blockPassSet.add((byte)102);
        blockPassSet.add((byte)104);
        blockPassSet.add((byte)105);
        blockPassSet.add((byte)106);
        blockPassSet.add((byte)107);
        blockPassSet.add((byte)111);
        blockPassSet.add((byte)115);
        blockPassSet.add((byte)116);
        blockPassSet.add((byte)117);
        blockPassSet.add((byte)118);
        blockPassSet.add((byte)119);
        blockPassSet.add((byte)120);    
        blockPassSet.add((byte)171);
        
        fullSolid.add((byte)1); //
        fullSolid.add((byte)2); //
        fullSolid.add((byte)3); //
        fullSolid.add((byte)4); //
        fullSolid.add((byte)5); //
        fullSolid.add((byte)7); //
        fullSolid.add((byte)12);    //
        fullSolid.add((byte)13);    //
        fullSolid.add((byte)14);    //
        fullSolid.add((byte)15);    //
        fullSolid.add((byte)16);    //
        fullSolid.add((byte)17);    //
        fullSolid.add((byte)19);    //
        fullSolid.add((byte)20);    //
        fullSolid.add((byte)21);    //
        fullSolid.add((byte)22);    //
        fullSolid.add((byte)23);    //
        fullSolid.add((byte)24);    //
        fullSolid.add((byte)25);    //
        fullSolid.add((byte)29);    //
        fullSolid.add((byte)33);    //
        fullSolid.add((byte)35);    //
        fullSolid.add((byte)41);    //
        fullSolid.add((byte)42);    //
        fullSolid.add((byte)43);    //
        fullSolid.add((byte)44);    //
        fullSolid.add((byte)45);    //
        fullSolid.add((byte)46);    //
        fullSolid.add((byte)47);    //
        fullSolid.add((byte)48);    //
        fullSolid.add((byte)49);    //
        fullSolid.add((byte)56);    //
        fullSolid.add((byte)57);    //
        fullSolid.add((byte)58);    //
        fullSolid.add((byte)60);    //
        fullSolid.add((byte)61);    //
        fullSolid.add((byte)62);    //
        fullSolid.add((byte)73);    //
        fullSolid.add((byte)74);    //
        fullSolid.add((byte)79);    //
        fullSolid.add((byte)80);    //
        fullSolid.add((byte)82);    //
        fullSolid.add((byte)84);    //
        fullSolid.add((byte)86);    //
        fullSolid.add((byte)87);    //
        fullSolid.add((byte)88);    //
        fullSolid.add((byte)89);    //
        fullSolid.add((byte)91);    //
        fullSolid.add((byte)95);    //
        fullSolid.add((byte)97);    //
        fullSolid.add((byte)98);    //
        fullSolid.add((byte)99);    //
        fullSolid.add((byte)100);   //
        fullSolid.add((byte)103);   //
        fullSolid.add((byte)110);   //
        fullSolid.add((byte)112);   //
        fullSolid.add((byte)121);   //
        fullSolid.add((byte)123);   //
        fullSolid.add((byte)124);   //
        fullSolid.add((byte)125);   //
        fullSolid.add((byte)126);   //
        fullSolid.add((byte)129);   //
        fullSolid.add((byte)133);   //
        fullSolid.add((byte)137);   //
        fullSolid.add((byte)138);   //
        fullSolid.add((byte)152);   //
        fullSolid.add((byte)153);   //
        fullSolid.add((byte)155);   //
        fullSolid.add((byte)158);   //
        
        blockUseSet.add((byte)23);  //Dispenser
        blockUseSet.add((byte)26);  //Bed
        blockUseSet.add((byte)33);  //Piston
        blockUseSet.add((byte)47);  //Bookcase
        blockUseSet.add((byte)54);  //Chest
        blockUseSet.add((byte)58);  //Workbench
        blockUseSet.add((byte)61);  //Furnace
        blockUseSet.add((byte)62);  //Furnace
        blockUseSet.add((byte)64);  //Wood Door
        blockUseSet.add((byte)69);  //Lever
        blockUseSet.add((byte)71);  //Iron Door
        blockUseSet.add((byte)77);  //Button
        blockUseSet.add((byte)85);  //Fence (stupid minecraft)
        blockUseSet.add((byte)93);  //Repeater
        blockUseSet.add((byte)94);  //Repeater
        blockUseSet.add((byte)96);  //Trapdoor
        blockUseSet.add((byte)107); //Fence Gate
        blockUseSet.add((byte)113); //Nether Fence (stupid minecraft)
        blockUseSet.add((byte)116); //Enchantment Table
        blockUseSet.add((byte)117); //Brewing Stand
        blockUseSet.add((byte)130); //Ender Chest
        blockUseSet.add((byte)145); //Anvil
        blockUseSet.add((byte)146); //Trapped Chest
        blockUseSet.add((byte)154); //Hopper
        blockUseSet.add((byte)158); //Dropper
        
        blockUseSet.add((byte)184); //Fences/Gates
        blockUseSet.add((byte)185); //Fences/Gates
        blockUseSet.add((byte)186); //Fences/Gates
        blockUseSet.add((byte)187); //Fences/Gates
        blockUseSet.add((byte)188); //Fences/Gates
        blockUseSet.add((byte)189); //Fences/Gates
        blockUseSet.add((byte)190); //Fences/Gates
        blockUseSet.add((byte)191); //Fences/Gates
        blockUseSet.add((byte)192); //Fences/Gates
        
        blockUseSet.add((byte)193); //Wood Doors
        blockUseSet.add((byte)194); //Wood Doors
        blockUseSet.add((byte)195); //Wood Doors
        blockUseSet.add((byte)196); //Wood Doors
        blockUseSet.add((byte)197); //Wood Doors
    }
	
	public static boolean solid(Block block)
	{
		if (block == null)			return false;
		return solid(block.getTypeId());
	}
	public static boolean solid(int block)
	{
		return solid((byte)block);
	}
	public static boolean solid(byte block)
	{
		return !blockPassSet.contains(block);
	}

	public static boolean airFoliage(Block block)
	{
		if (block == null)			return false;
		return airFoliage(block.getTypeId());
	}
	public static boolean airFoliage(int block)
	{
		return airFoliage((byte)block);
	}
	public static boolean airFoliage(byte block)
	{
		return blockAirFoliageSet.contains(block);
	}

	public static boolean fullSolid(Block block)
	{
		if (block == null)
			return false;

		return fullSolid(block.getTypeId());
	}
	public static boolean fullSolid(int block)
	{
		return fullSolid((byte)block);
	}
	public static boolean fullSolid(byte block)
	{
		return fullSolid.contains(block);
	}

	public static boolean usable(Block block)
	{
		if (block == null)
			return false;

		return usable(block.getTypeId());
	}
	public static boolean usable(int block)
	{
		return usable((byte)block);
	}
	public static boolean usable(byte block)
	{
		return blockUseSet.contains(block);
	}

	public static HashMap<Block, Double> getInRadius(Location loc, double dR)
	{
		return getInRadius(loc, dR, 9999);
	}

	public static HashMap<Block, Double> getInRadius(Location loc, double dR, double maxHeight)
	{
		HashMap<Block, Double> blockList = new HashMap<Block, Double>();
		int iR = (int)dR + 1;

		for (int x=-iR ; x <= iR ; x++)
			for (int z=-iR ; z <= iR ; z++)
				for (int y=-iR ; y <= iR ; y++)
				{
					if (Math.abs(y) > maxHeight)
						continue;

					Block curBlock = loc.getWorld().getBlockAt((int)(loc.getX()+x), (int)(loc.getY()+y), (int)(loc.getZ()+z));

					double offset = UtilMath.offset(loc, curBlock.getLocation().add(0.5, 0.5, 0.5));;

					if (offset <= dR)
						blockList.put(curBlock, 1 - (offset/dR));
				}

		return blockList;
	}


	public static HashMap<Block, Double> getInRadius(Block block, double dR)
	{
		return getInRadius(block, dR, false);
	}

	public static HashMap<Block, Double> getInRadius(Block block, double dR, boolean hollow)
	{
		HashMap<Block, Double> blockList = new HashMap<Block, Double>();
		int iR = (int)dR + 1;

		for (int x=-iR ; x <= iR ; x++)
			for (int z=-iR ; z <= iR ; z++)
				for (int y=-iR ; y <= iR ; y++)
				{
					Block curBlock = block.getRelative(x, y, z);

					double offset = UtilMath.offset(block.getLocation(), curBlock.getLocation());

					if (offset <= dR && !(hollow && offset < dR - 1))
					{
						blockList.put(curBlock, 1 - (offset / dR));
					}
				}

		return blockList;
	}

	public static ArrayList<Block> getInSquare(Block block, double dR)
	{
		ArrayList<Block> blockList = new ArrayList<Block>();
		int iR = (int)dR + 1;

		for (int x=-iR ; x <= iR ; x++)
			for (int z=-iR ; z <= iR ; z++)
				for (int y=-iR ; y <= iR ; y++)
				{
					blockList.add(block.getRelative(x, y, z));
				}

		return blockList;
	}

	public static boolean isBlock(ItemStack item) 
	{
		if (item == null)
			return false;

		return item.getTypeId() > 0 && item.getTypeId() < 256;
	}

	public static Block getHighest(World world, int x, int z)
	{
		return getHighest(world, x, z, null);
	}

	public static Block getHighest(World world, int x, int z, HashSet<Material> ignore)
	{
		Block block = world.getHighestBlockAt(x, z);

		//Shuffle Down
		while (block.getY() > 0 && 
				(
						airFoliage(block) || 
						block.getType() == Material.LEAVES || 
						(ignore != null && ignore.contains(block.getType()))
				))
		{
			block = block.getRelative(BlockFace.DOWN);
		}

		return block.getRelative(BlockFace.UP); 
	}

	/**
	 * 
	 * @param location of explosion
	 * @param strength of explosion
	 * @param damageBlocksEqually - Treat all blocks as durability of dirt
	 * @param ensureDestruction - Ensure that the closest blocks are destroyed at least
	 * @return
	 */
	public static ArrayList<Block> getExplosionBlocks(Location location, float strength, boolean damageBlocksEqually)
	{
		ArrayList<Block> toExplode = new ArrayList<Block>();
		WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					if ((i == 0) || (i == 16 - 1) || (j == 0) || (j == 16 - 1) || (k == 0) || (k == 16 - 1))
					{
						double d3 = i / (16 - 1.0F) * 2.0F - 1.0F;
						double d4 = j / (16 - 1.0F) * 2.0F - 1.0F;
						double d5 = k / (16 - 1.0F) * 2.0F - 1.0F;
						double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

						d3 /= d6;
						d4 /= d6;
						d5 /= d6;
						float f1 = strength * (0.7F + UtilMath.random.nextFloat() * 0.6F);

						double d0 = location.getX();
						double d1 = location.getY();
						double d2 = location.getZ();

						for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F)
						{
							int l = MathHelper.floor(d0);
							int i1 = MathHelper.floor(d1);
							int j1 = MathHelper.floor(d2);
							Block block = location.getWorld().getBlockAt(l, i1, j1);

							if (block.getType() != Material.AIR)
							{
								float f3 = (damageBlocksEqually ? Blocks.DIRT : world.getType(block.getX(), block.getY(),
										block.getZ())).a((net.minecraft.server.v1_7_R4.Entity) null);

								f1 -= (f3 + 0.3F) * f2;
							}

							if ((f1 > 0.0F) && (i1 < 256) && (i1 >= 0))
							{
								toExplode.add(block);
							}

							d0 += d3 * f2;
							d1 += d4 * f2;
							d2 += d5 * f2;
						}
					}
				}
			}
		}
		
		return toExplode;
	}

	public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) 
	{
		ArrayList<Block> blocks = new ArrayList<Block>();

		if (diagonals)
		{
			for (int x=-1 ; x<= 1 ; x++)
				for (int y=-1 ; y<= 1 ; y++)
					for (int z=-1 ; z<= 1 ; z++)
					{
						if (x == 0 && y == 0 && z == 0)
							continue;

						blocks.add(block.getRelative(x, y, z));
					}
		}
		else
		{
			blocks.add(block.getRelative(BlockFace.UP));
			blocks.add(block.getRelative(BlockFace.DOWN));
			blocks.add(block.getRelative(BlockFace.NORTH));
			blocks.add(block.getRelative(BlockFace.SOUTH));
			blocks.add(block.getRelative(BlockFace.EAST));
			blocks.add(block.getRelative(BlockFace.WEST));
		}

		return blocks;
	}

	public static boolean isVisible(Block block)
	{
		for (Block other : UtilBlock.getSurrounding(block, false))
		{
			if (!other.getType().isOccluding()) 
			{
				return true;
			}
		}

		return false;
	}
	public static ArrayList<Block> getInBoundingBox(Location a,	Location b)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (int x=Math.min(a.getBlockX(), b.getBlockX()) ; x<=Math.max(a.getBlockX(), b.getBlockX()) ; x++)
			for (int y=Math.min(a.getBlockY(), b.getBlockY()) ; y<=Math.max(a.getBlockY(), b.getBlockY()) ; y++)
				for (int z=Math.min(a.getBlockZ(), b.getBlockZ()) ; z<=Math.max(a.getBlockZ(), b.getBlockZ()) ; z++)
				{
					Block block = a.getWorld().getBlockAt(x,y,z);
					
					if (block.getType() != Material.AIR)
						blocks.add(block);
				}
		
		return blocks;
	}
}
