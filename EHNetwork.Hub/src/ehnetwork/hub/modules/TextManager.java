package ehnetwork.hub.modules;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilBlockText;
import ehnetwork.core.common.util.UtilBlockText.TextAlign;
import ehnetwork.hub.HubManager;
import ehnetwork.hub.HubType;

public class TextManager extends MiniPlugin
{
	public HubManager Manager;
	
	public Location locComp;
	public Location locArcade;
	public Location locSurvival;
	public Location locClassics;
	
	BlockFace faceComp = BlockFace.SOUTH;
	BlockFace faceArcade = BlockFace.WEST;
	BlockFace faceSurvival = BlockFace.NORTH;
	BlockFace faceOther = BlockFace.EAST;
	
	String[] arcadeGames;
	int arcadeIndex = 0;
	
	int smashIndex = 0;
	
	public TextManager(HubManager manager)
	{
		super("Text Creator", manager.getPlugin());
		
		Manager = manager;
		
		locComp = 		manager.GetSpawn().add(40, 10, 0);
		locArcade = 	manager.GetSpawn().add(0, 10, 40);
		locSurvival = 	manager.GetSpawn().add(-40, 10, 0);
		locClassics = 		manager.GetSpawn().add(0, 10, -40);
		
		arcadeGames = new String[]
				{
				"DRAGON ESCAPE",
				"SUPER SPLEEF",
				"SHEEP QUEST",
				};
		
		CreateText();
	}
	
	public void CreateText()
	{
		//Comp
		UtilBlockText.MakeText("CHAMPIONS", locComp, faceComp, 159, (byte)5, TextAlign.CENTER);
		UtilBlockText.MakeText("CHAMPIONS", locComp.clone().add(1, 0, 0), faceComp, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("DOMINATE", locComp.clone().add(15, 14, 0), faceComp, 159, (byte)4, TextAlign.CENTER);
		UtilBlockText.MakeText("DOMINATE", locComp.clone().add(16, 14, 0), faceComp, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("TEAM DEATHMATCH", locComp.clone().add(15, 21, 0), faceComp, 159, (byte)1, TextAlign.CENTER);
		UtilBlockText.MakeText("TEAM DEATHMATCH", locComp.clone().add(16, 21, 0), faceComp, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("CLANS", locComp.clone().add(15, 28, 0), faceComp, 159, (byte)14, TextAlign.CENTER);
		UtilBlockText.MakeText("CLANS", locComp.clone().add(16, 28, 0), faceComp, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		//Arcade
		UtilBlockText.MakeText("ARCADE", locArcade, faceArcade, 159, (byte)5, TextAlign.CENTER);
		UtilBlockText.MakeText("ARCADE", locArcade.clone().add(0, 0, 1), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText(GetArcadeText(0), locArcade.clone().add(0, 14, 15), faceArcade, 159, (byte)4, TextAlign.CENTER);
		UtilBlockText.MakeText(GetArcadeText(0), locArcade.clone().add(0, 14, 16), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText(GetArcadeText(1), locArcade.clone().add(0, 21, 15), faceArcade, 159, (byte)1, TextAlign.CENTER);
		UtilBlockText.MakeText(GetArcadeText(1), locArcade.clone().add(0, 21, 16), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText(GetArcadeText(2), locArcade.clone().add(0, 28, 15), faceArcade, 159, (byte)14, TextAlign.CENTER);
		UtilBlockText.MakeText(GetArcadeText(2), locArcade.clone().add(0, 28, 16), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		 
		//Survival
		UtilBlockText.MakeText("SURVIVAL", locSurvival, faceSurvival, 159, (byte)5, TextAlign.CENTER);
		UtilBlockText.MakeText("SURVIVAL", locSurvival.clone().add(-1, 0, 0), faceSurvival, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("SKYWARS", locSurvival.clone().add(-15, 14, 0), faceSurvival, 159, (byte)4, TextAlign.CENTER);
		UtilBlockText.MakeText("SKYWARS", locSurvival.clone().add(-16, 14, 0), faceSurvival, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("THE BRIDGES", locSurvival.clone().add(-15, 21, 0), faceSurvival, 159, (byte)1, TextAlign.CENTER);
		UtilBlockText.MakeText("THE BRIDGES", locSurvival.clone().add(-16, 21, 0), faceSurvival, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("SURVIVAL GAMES", locSurvival.clone().add(-15, 28, 0), faceSurvival, 159, (byte)14, TextAlign.CENTER);
		UtilBlockText.MakeText("SURVIVAL GAMES", locSurvival.clone().add(-16, 28, 0), faceSurvival, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		//Other
		UtilBlockText.MakeText("CLASSICS", locClassics, faceOther, 159, (byte)5, TextAlign.CENTER);
		UtilBlockText.MakeText("CLASSICS", locClassics.add(0, 0, -1), faceOther, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("SUPER SMASH MOBS", locClassics.clone().add(0, 14, -15), faceOther, 159, (byte)4, TextAlign.CENTER);
		UtilBlockText.MakeText("SUPER SMASH MOBS", locClassics.clone().add(0, 14, -16), faceOther, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("MASTER BUILDERS", locClassics.clone().add(0, 21, -15), faceOther, 159, (byte)1, TextAlign.CENTER);
		UtilBlockText.MakeText("MASTER BUILDERS", locClassics.clone().add(0, 21, -16), faceOther, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilBlockText.MakeText("BLOCK HUNT", locClassics.clone().add(0, 28, -15), faceOther, 159, (byte)14, TextAlign.CENTER);
		UtilBlockText.MakeText("BLOCK HUNT", locClassics.clone().add(0, 28, -16), faceOther, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
	}
	
	/*
	@EventHandler
	public void UpdateArcadeGames(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		UtilText.MakeText(GetArcadeText(0), locArcade.clone().add(0, 14, 15), faceArcade, 159, (byte)4, TextAlign.CENTER);
		UtilText.MakeText(GetArcadeText(0), locArcade.clone().add(0, 14, 16), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilText.MakeText(GetArcadeText(1), locArcade.clone().add(0, 21, 15), faceArcade, 159, (byte)1, TextAlign.CENTER);
		UtilText.MakeText(GetArcadeText(1), locArcade.clone().add(0, 21, 16), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilText.MakeText(GetArcadeText(2), locArcade.clone().add(0, 28, 15), faceArcade, 159, (byte)14, TextAlign.CENTER);
		UtilText.MakeText(GetArcadeText(2), locArcade.clone().add(0, 28, 16), faceArcade, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		arcadeIndex = (arcadeIndex + 3)%arcadeGames.length;
	}
	*/
	
	public String GetArcadeText(int offset)
	{
		int index = (arcadeIndex + offset)%arcadeGames.length;

		return	arcadeGames[index];
	}
	
	/*
	@EventHandler
	public void UpdateNew(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		long startTime = System.currentTimeMillis();
		
		smashIndex = (smashIndex+1)%2;
		
		byte color = 4;
		if (smashIndex == 1)	color = 0;
		
		//UtilText.MakeText("SUPER SMASH MOBS", locOther, faceOther, 159, color, TextAlign.CENTER);
		//UtilText.MakeText("SUPER SMASH MOBS", locOther.clone().add(0, 0, -1), faceOther, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilText.MakeText("SUPER SMASH MOBS", locClassics.clone().add(0, 14, -15), faceOther, 159, color, TextAlign.CENTER);
		UtilText.MakeText("SUPER SMASH MOBS", locClassics.clone().add(0, 14, -16), faceOther, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		
		UtilText.MakeText("DOMINATE", locComp.clone().add(15, 14, 0), faceComp, 159, color, TextAlign.CENTER);
		UtilText.MakeText("DOMINATE", locComp.clone().add(16, 14, 0), faceComp, (Manager.Type == HubType.Halloween) ? 89 : 159, (Manager.Type == HubType.Halloween) ? (byte)0 : (byte)15, TextAlign.CENTER);
		//System.out.println("TextCreator : " + (System.currentTimeMillis() - startTime) + "ms");
	}
	*/
}
