package ehnetwork.game.arcade.game.games.cards;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;

import ehnetwork.core.common.util.UtilAlg;

public class VoteRoom 
{
	public Location Spawn;
	public Location Question;
	public Location AnswerA;
	public Location AnswerB;
	
	public ItemFrame QuestionEnt;
	public ItemFrame AnswerAEnt;
	public ItemFrame AnswerBEnt;
	
	public Location BoardAnswer;
	public ItemFrame BoardAnswerEnt;
	
	public VoteRoom(Location spawn, ArrayList<Location> questions, ArrayList<Location> answers, ArrayList<Location> board)
	{
		Spawn = spawn;
		
		Question = UtilAlg.findClosest(spawn, questions);
		questions.remove(Question);
		
		AnswerA = UtilAlg.findClosest(Question, answers);
		answers.remove(AnswerA);
		
		AnswerB = UtilAlg.findClosest(Question, answers);
		answers.remove(AnswerB);
		
		BoardAnswer = UtilAlg.findClosest(Question, board);
		board.remove(BoardAnswer);
	}
	
	public void resetFrames(boolean doubleAnswer)
	{
		if (QuestionEnt != null)
		{
			QuestionEnt.remove();
			QuestionEnt = null;
		}
		
		if (AnswerAEnt != null)
		{
			AnswerAEnt.remove();
			AnswerAEnt = null;
		}
		
		if (AnswerBEnt != null)
		{
			AnswerBEnt.remove();
			AnswerBEnt = null;
		}
		
		if (BoardAnswerEnt != null)
		{
			BoardAnswerEnt.remove();
			BoardAnswerEnt = null;
		}
		
		QuestionEnt = Question.getWorld().spawn(Question.getBlock().getLocation(), ItemFrame.class);
		AnswerAEnt = AnswerA.getWorld().spawn(AnswerA.getBlock().getLocation(), ItemFrame.class);
		BoardAnswerEnt = BoardAnswer.getWorld().spawn(BoardAnswer.getBlock().getLocation(), ItemFrame.class);
				
//		if (doubleAnswer)
//			AnswerBEnt = AnswerB.getWorld().spawn(AnswerB, ItemFrame.class);
		
//		i.setFacingDirection(BlockFace.SOUTH);
//		HangingPlaceEvent hEvent = new HangingPlaceEvent(i, player, bodyBlock, BlockFace.NORTH);
//		plugin.getServer().getPluginManager().callEvent(hEvent);
	}

	public void displayAnswer() 
	{
		System.out.println(BoardAnswerEnt == null);
		System.out.println(AnswerAEnt == null);
		BoardAnswerEnt.setItem(AnswerAEnt.getItem());
	}
}
