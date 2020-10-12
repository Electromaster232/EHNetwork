package ehnetwork.hub.modules.parkour;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilPlayer;

public class ParkourData 
{
	public String Name;
	public String[] Desc;
	public int Gems;
	public Location NPC;
	public Location CornerA;
	public Location CornerB;
	public double Distance;
	
	public ParkourData(String name, String[] desc, int gems, Location npc, Location cornerA, Location cornerB)
	{
		Name = name;
		Desc = desc;
		Gems = gems;
		NPC = npc;
		CornerA = cornerA;
		CornerB = cornerB;
	}

	public void Inform(Player player) 
	{
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cDGreen + C.Strike + "=============================================");
		
		UtilPlayer.message(player, "§f§l" + Name + " Course");
		UtilPlayer.message(player, "");
		
		for (String cur : Desc)
		{
			UtilPlayer.message(player, "  " + cur);
		}
		
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cWhite + C.Bold + "You must " + C.cGreen + C.Bold + "Right-Click" + C.cWhite + C.Bold + " the NPC to begin!");
		
		UtilPlayer.message(player, "");
		UtilPlayer.message(player, C.cDGreen + C.Strike + "=============================================");
		
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 2f);
	}
	
	public boolean InBoundary(Location loc)
	{
		return (UtilAlg.inBoundingBox(loc, CornerA, CornerB)); 
	}
	
	public World getWorld()
	{
		return CornerA.getWorld();
	}
}
