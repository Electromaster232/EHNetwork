package ehnetwork.hub.tutorial;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;

public class TutorialData 
{
	public Player Player;

	public TutorialPhase Phase;
	public int PhaseStep;
	
	public int TextStep;
	
	public long Sleep;
	
	public TutorialData(Player player, TutorialPhase phase)
	{
		Player = player;
		Phase = phase;
		
		TextStep = 0;
		PhaseStep = 0;
		
		Sleep = System.currentTimeMillis() + 3000;
	}

	public boolean Update() 
	{
		if (!Player.getLocation().equals(Phase.Location))
			Player.teleport(Phase.Location);
		
		if (System.currentTimeMillis() < Sleep)
			return false;
		
		//Next Phase
		if (TextStep >= Phase.Text.length)
		{
			PhaseStep++;
			Sleep = System.currentTimeMillis() + 2000;
			
			return true;
		}
		
		//Display Text
		String text = Phase.Text[TextStep];
		
		UtilPlayer.message(Player, " ");
		UtilPlayer.message(Player, " ");
		UtilPlayer.message(Player, " ");
		UtilPlayer.message(Player, C.cGreen + C.Strike + C.Bold + "========================================");
		UtilPlayer.message(Player, C.cGold + C.Bold + Phase.Header);
		UtilPlayer.message(Player, " ");
		
		for (int i=0 ; i<=TextStep ; i++)
			UtilPlayer.message(Player, "  " + Phase.Text[i]);
		
		for (int i=TextStep ; i<=5 ; i++)
			UtilPlayer.message(Player, " ");
		
		UtilPlayer.message(Player, C.cGreen + C.Strike + C.Bold + "========================================");
		
		if (text.length() > 0)
		{
			Player.playSound(Player.getLocation(), Sound.ORB_PICKUP, 2f, 1.5f);
			Sleep = System.currentTimeMillis() + 1000 + (50*text.length());
		}
		else
		{
			Sleep = System.currentTimeMillis() + 600;
		}
			
		TextStep++;
			
		return false;
	}
	
	public void SetNextPhase(TutorialPhase phase)
	{
		Phase = phase;
		TextStep = 0;
		Player.teleport(Phase.Location);
	}
}
