package ehnetwork.hub.tutorial;

import org.bukkit.Location;

import ehnetwork.core.common.util.UtilAlg;

public class TutorialPhase 
{
	public Location Location;
	public String Header;
	public String[] Text;

	public TutorialPhase(Location player, Location target, String header, String[] text) 
	{
		Location = player;
		
		Location.setYaw(UtilAlg.GetYaw(UtilAlg.getTrajectory(player, target)));
		Location.setPitch(UtilAlg.GetPitch(UtilAlg.getTrajectory(player, target)));
		
		Header = header;
		Text = text;
	}
}
