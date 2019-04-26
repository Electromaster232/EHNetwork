package mineplex.hub.tutorial.types;

import org.bukkit.ChatColor;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.hub.HubManager;
import mineplex.hub.tutorial.Tutorial;
import mineplex.hub.tutorial.TutorialPhase;

public class PartyTutorial extends Tutorial
{
	public PartyTutorial(HubManager manager) 
	{
		super(manager, "Party Tutorial", 1000, "Hub_PartyTutorial");

		double y = -manager.GetSpawn().getY();
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 10), 
				manager.GetSpawn().add(81, y+68.5, 10),
				"Parties",
				new String[] 
						{
					"Hi there!",
					"",
					"This tutorial will teach you about Parties.",
					"",
					"Parties are used to group with other players",
					"in order to easily play the same game together."
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 9), 
				manager.GetSpawn().add(81, y+68.5, 9),
				"Creating a Party",
				new String[] 
						{
					"To create a Party with someone;",
					"",
					"Type " + F.link("/party <Player>"),
					"",
					"This will create a party, and invite them to it!",
					"They will receive a notification on how to join.",
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 9), 
				manager.GetSpawn().add(81, y+68.5, 9),
				"Inviting and Suggesting Players",
				new String[] 
						{
					"To invite/suggest more players to a Party;",
					"",
					"Type " + F.link("/party <Player>"),
					"",
					"Invitations last for 60 seconds."
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 9), 
				manager.GetSpawn().add(81, y+68.5, 9),
				"Leaving Parties",
				new String[] 
						{
					"To leave your current Party;",
					"",
					"Type " + F.link("/party leave"),
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 9), 
				manager.GetSpawn().add(81, y+68.5, 9),
				"Kicking Players from Party",
				new String[] 
						{
					"To kick players from your current Party;",
					"",
					"Type " + F.link("/party kick <Player>"),
					"",
					"Only the Party Leader can do this."
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 9), 
				manager.GetSpawn().add(81, y+68.5, 9),
				"Joining Games Together",
				new String[] 
						{
					"Only the Party Leader can join games.",
					"",
					"The game must have enough slots for",
					"all Party Members to fit.",
					"",
					"All members will be connected to the game."
					
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(84, y+69, 9), 
				manager.GetSpawn().add(81, y+68.5, 9),
				"Party Chat",
				new String[] 
						{
					"To send a message to your Party;",
					"",
					"Type " + F.link("@Hey guys, how are you?"),
					"",
					"They will see; ",
					C.cDPurple + C.Bold + "Party " + C.cWhite + C.Bold + "YourName " + ChatColor.RESET + C.cPurple + "Hey guys, how are you?"
						}
				));
		
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(0, -2, 0), 
				manager.GetSpawn().add(0, -2.1, 5), 
				"End",
				new String[] 
						{
					"",
					"",
					"Thanks for doing the party tutorial!",
					"",
					"",
						}
				));


	}
}
