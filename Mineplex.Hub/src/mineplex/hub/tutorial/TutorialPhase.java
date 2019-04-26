package mineplex.hub.tutorial;

import mineplex.core.common.util.UtilAlg;

import org.bukkit.Location;

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
