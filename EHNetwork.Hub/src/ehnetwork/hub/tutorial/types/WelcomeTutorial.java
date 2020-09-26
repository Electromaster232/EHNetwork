package ehnetwork.hub.tutorial.types;

import ehnetwork.hub.HubManager;
import ehnetwork.hub.modules.TextManager;
import ehnetwork.hub.tutorial.Tutorial;
import ehnetwork.hub.tutorial.TutorialPhase;

public class WelcomeTutorial extends Tutorial
{
	public WelcomeTutorial(HubManager manager, TextManager text)
	{
		super(manager, "Welcome Tutorial", 5000, "Hub_JoinTutorial");

		double y = -manager.GetSpawn().getY();

		//Welcome
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(-40, y+85, 0), 
				manager.GetSpawn(),
				"Welcome to EHPlex",
				new String[] 
						{
					"Welcome!",
					"",
					"This is a very quick tutorial to help you start.",
					"Seriously, it will only take 30 seconds!!!",
					"",
					"Mineplex has many different games to play.",
					"I will show you them, and tell you how to join!"
						}
				));

		//Arcade
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(0, -3, 13), 
				text.locArcade,
				"Arcade",
				new String[] 
						{
					"This is the " + _elem + "Arcade" + _main + " game mode.",
					"",
					"Servers will rotate through many different games.",
					"So there's no need to quit after each game!",
					"",
					"They are all quick, fun 16 player games!"
						}
				));

		//Bridges
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(-13, -3, 0), 
				text.locSurvival,
				"Survival",
				new String[] 
						{
					"Here are the " + _elem + "Survival" + _main + " game modes.",
					"",
					_elem + "The Bridges" + _main + ", you get 10 minutes to prepare",
					"then the Bridges drop, and battle starts!",
					"",
					_elem + "Survival Games" + _main + " puts you into a dangerous",
					"battle for survival against 23 other tributes!"
						}
				));

		//Pig
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(-9, y+73, 53), 
				manager.GetSpawn().add(-11, y+72.5, 57),
				"???",
				new String[] 
						{
					"",
					"",
					"This is a pig standing on a log."
						}
				));

		//Classics
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(0, -3, -13), 
				text.locClassics,
				"Classics",
				new String[] 
						{
					"Here, you can play our " + _elem + "Classics" + _main + " game modes.",
					"",
					"In " + _elem + "Super Smash Mobs" + _main + " you become a monster,",
					"then fight to the death with other players, using fun skills!",
					"",					
					_elem + "Draw My Thing" + _main + " is a drawing game where players",
					"take turns at drawing and guessing the word.",
					//"",
					//"In " + _elem + "Block Hunt" + _main + " you turn into a block",
					//"and hide from the Hunters until the time is up!",
					
						}
				));

		//Comp
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(13, -3, 0), 
				text.locComp,
				"Champions",
				new String[] 
						{
					"Finally, these are the " + _elem + "Champions" + _main + " games.",
					"These are extremely competitive skill based games.",
					"",
					"Each class can be customised with unlockable skills.",
					"",
					"Fight with others in three different game types!"
						}
				));

		//JOIN
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(0, -3, 19), 
				manager.GetSpawn().add(0, -3.1, 23), 
				"Joining Games",
				new String[] 
						{
					"You can join a game in two ways.",
					"",
					"The easiest way is to walk through the portal.",
					"This will join the best available server!",
					"",
					"Click the " + _elem + "Game NPC" + _main + " to open the " + _elem + "Server Menu" + _main + ".",
					"Here, you can manually pick which server to join!",
						}
				));

		//END
		_phases.add(new TutorialPhase(
				manager.GetSpawn().add(0, -2, 0), 
				manager.GetSpawn().add(0, -2.1, 5), 
				"End",
				new String[] 
						{
					"",
					"Easy huh?",
					"",
					"Thanks for listening! Have fun!",
					"",
						}
				));
	}
}
