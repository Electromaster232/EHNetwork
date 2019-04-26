package mineplex.queuer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mineplex.serverdata.Region;

public class Queuer
{
	private static QueueRepository _repo;
	
	public static void main (String args[])
	{
		Region region = (!new File("eu.dat").exists()) ? Region.US : Region.EU;
		
		_repo = new QueueRepository(region);
		
		HashMap<Integer, Integer> playerVarianceMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Match> playerPrepMatchMap = new HashMap<Integer, Match>();
		Set<Match> matches = new HashSet<Match>();
		
		QueuePartySorter partySorter = new QueuePartySorter();
		
		int matchId = 1;
		
		while (true)
		{
			int matchesMade = 0;
			matchId %= 1500;
			
			List<Integer> assignedMatchIdChecked = new ArrayList<Integer>();
			Map<Integer, QueueParty> queueParties = _repo.getMappedQueueParties();
			
			int matchPlayerCount = 2;
			
			System.out.println("Checking " + queueParties.size() + " queues...");
			for (QueueParty queueParty : queueParties.values())
			{
				int partyId = queueParty.getId();
				int variance = playerVarianceMap.containsKey(partyId) ? playerVarianceMap.get(partyId) : 0;
				variance += 25;
				playerVarianceMap.put(partyId, variance);
				
				if (queueParty.hasAssignedMatch())
				{
					for (Match match : matches)
					{
						if (Math.abs(match.getAverageElo() - queueParty.getAverageElo()) <= variance)
						{
							if (playerPrepMatchMap.containsKey(partyId))
							{
								if (playerPrepMatchMap.get(partyId) == match)
									break;
								
								playerPrepMatchMap.get(partyId).quitQueueParty(queueParty);
							}
							
							match.joinQueueParty(queueParty);
							playerPrepMatchMap.put(partyId, match);
							
							log("Found prep match for '" + queueParty.getId() + "'");
							break;
						}
					}
					
					if (!playerPrepMatchMap.containsKey(partyId))
					{
						Match match = new Match(matchId++, queueParty.getAverageElo(), queueParty);
						
						playerPrepMatchMap.put(partyId, match);
						matches.add(match);
					}
				}
				else if (!assignedMatchIdChecked.contains(queueParty.getAssignedMatch()))
				{
					int assignedMatchId = queueParty.getAssignedMatch();
					log("Checking if match '" + assignedMatchId + "' is ready.");
					//List<String> matchStatuses = _repo.getMatchStatuses(queueRecord.AssignedMatch);
					Collection<QueueParty> joinedParties = _repo.getJoinedQueueParties(assignedMatchId);
					boolean matchReady = true;
					boolean matchDeny = false;
					
					for (QueueParty joinedParty : joinedParties)
					{
						String partyState = joinedParty.getState();
						if (partyState.equalsIgnoreCase("Deny"))
						{
							matchDeny = true;
							matchReady = false;
							break;
						}
						else if (!partyState.equalsIgnoreCase("Ready"))
						{
							matchReady = false;
						}
					}
					
					if (matchReady)
					{
						_repo.startMatch(assignedMatchId);	
						_repo.deleteAssignedParties(assignedMatchId);
						
						System.out.println("Starting match '" + assignedMatchId + "'");
					}
					else if (matchDeny)
					{				
						_repo.deleteMatch(assignedMatchId);
					}
					
					assignedMatchIdChecked.add(assignedMatchId);
				}
			}
			
			System.out.println("Checking " + matches.size() + " matches...");
			
			// Check for and kick off invites for ready matches
			for (Iterator<Match> matchIterator = matches.iterator(); matchIterator.hasNext();)
			{
				Match match = matchIterator.next();
				
				// Don't give me crap about not using iterator...can't cuz of stupid thing.
				Set<QueueParty> partiesToRemove = new HashSet<QueueParty>();
				
				for (QueueParty queueParty : match.getParties())
				{
					if (!queueParties.containsKey(queueParty.getId()))
					{
						log("Removing matchStatus : " + queueParty.getId());
						partiesToRemove.add(queueParty);
						
						if (match.isWaitingForInvites())
						{
							_repo.deleteMatch(match.getId());
							match.setWaitingForInvites(false);
						}
					}
				}
				
				for (QueueParty party : partiesToRemove)
				{
					match.quitQueueParty(party);
				}
				
				if (match.isWaitingForInvites())
				{
					if ((match.getWaitDuration()) > 15000)
					{
						for (QueueParty queueParty : match.getParties())
						{
							if (!queueParty.getState().equalsIgnoreCase("Ready"))
							{
								_repo.deleteQueueParty(queueParty.getId());
							}
						}
						
						_repo.deleteMatch(match.getId());
						match.setWaitingForInvites(false);
					}
					
					continue;
				}
				
				if (match.getPlayerCount() >= matchPlayerCount)
				{
					List<QueueParty> partyList = new ArrayList<QueueParty>(match.getParties());
					Collections.sort(partyList, partySorter);

					int playerCount = 0;
					for (QueueParty party : partyList)
					{
						if (playerCount + party.getPlayerCount() > matchPlayerCount)
						{
							match.quitQueueParty(party);
							playerPrepMatchMap.remove(party.getId());
							log("Oops hit player cap, can't fit you in this match.");
							continue;
						}
						
						playerCount += party.getPlayerCount();
					}

					if (playerCount == matchPlayerCount)
					{
						log("Sent match invites for '" + match.getId() + "'");
						
						for (QueueParty party : match.getParties())
						{
							playerPrepMatchMap.remove(party.getId());
							_repo.assignMatch(party, match);
						}
						
						match.setWaitingForInvites(true);
						matchesMade++;
					}
				}
				else if (match.getPlayerCount() == 0)
				{
					matchIterator.remove();
				}
			}
			
			try
			{
				if (matchesMade > 0)
					System.out.println("Made " + matchesMade + " matches.");
				
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		
	}
	
	private static void log(String message)
	{
		System.out.println(message);
	}
}
