package ehnetwork.game.microgames.game.games.cards;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.PlayerPrepareTeleportEvent;
import ehnetwork.game.microgames.game.SoloGame;
import ehnetwork.game.microgames.game.games.GameScore;
import ehnetwork.game.microgames.game.games.cards.kits.KitPlayer;
import ehnetwork.game.microgames.kit.Kit;

public class Cards extends SoloGame 
{
	private CardFactory _cardFactory;
	
	private ArrayList<GameScore> _ranks = new ArrayList<GameScore>();
	
	private NautHashMap<Player, VoteRoom> _voteRooms = new NautHashMap<Player, VoteRoom>();
	
	private Location _questionLoc = null;
	private ItemFrame _questionEnt = null;
	
	private RoundState _roundState = RoundState.View;
	private long _roundStateTime = 0;
	private Player _roundDealer = null;
	
	private String _question = null;
	
	private long _playTime = 16000;
	private long _judgeTime = 16000;
	private long _viewTime = 6000;
	
	private ArrayList<Player> _dealerOrder = new ArrayList<Player>();;
	
	public Cards(MicroGamesManager manager)
	{
		super(manager, GameType.Cards,

				new Kit[]
						{
				new KitPlayer(manager),
						},

						new String[]
								{
				"Be creative and build something",
				"based on the build theme!"
								});

		this.StrictAntiHack = true;
		this.Damage = false;
		this.HungerSet = 20;
		this.HealthSet = 20;

		this.WorldTimeSet = 6000;

		this.PrepareFreeze = false;
		
		_cardFactory = new CardFactory();
	}
	
	@Override
	public void ParseData()
	{
		for (Location loc : WorldData.GetDataLocs("RED"))			loc.getBlock().setType(Material.OBSIDIAN);
		for (Location loc : WorldData.GetDataLocs("ORANGE"))		loc.getBlock().setType(Material.OBSIDIAN);
		for (Location loc : WorldData.GetDataLocs("GREEN"))			loc.getBlock().setType(Material.OBSIDIAN);
		
		
		_questionLoc = WorldData.GetDataLocs("PINK").get(0);
		_questionLoc.getBlock().setType(Material.OBSIDIAN);
	}
	
	@EventHandler
	public void prepare(PlayerPrepareTeleportEvent event)
	{
		for (int i=0 ; i<9 ; i++)
		{
			event.GetPlayer().getInventory().addItem(_cardFactory.getAnswerStack());
		}
		
		_dealerOrder.add(event.GetPlayer());
		
		//Prep Vote Room
		_voteRooms.put(event.GetPlayer(), new VoteRoom(
				WorldData.GetDataLocs("YELLOW").remove(0), 
				WorldData.GetDataLocs("ORANGE"), 
				WorldData.GetDataLocs("GREEN"),
				WorldData.GetDataLocs("RED")));
		
		addScore(event.GetPlayer(), 0);
	}
	
	@EventHandler
	public void roundStateUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;
		
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (_roundState == RoundState.PlaceCards && UtilTime.elapsed(_roundStateTime, _playTime))
		{
			setRoundState(RoundState.Judge);
		}
		else if (_roundState == RoundState.Judge && UtilTime.elapsed(_roundStateTime, _judgeTime))
		{
			setRoundState(RoundState.View);
		}
		else if (_roundState == RoundState.View && UtilTime.elapsed(_roundStateTime, _viewTime))
		{
			setRoundState(RoundState.PlaceCards);
		}
	}
	
	public void setRoundState(RoundState state)
	{
		_roundStateTime = System.currentTimeMillis();
		_roundState = state;
		
		if (state == RoundState.PlaceCards)
		{

			//Get Dealer
			_roundDealer = _dealerOrder.remove(0);
			_dealerOrder.add(_roundDealer);
			
			//New Question
			ItemStack questionStack = _cardFactory.getQuestionStack();
			
			if (_questionEnt != null)
			{
				_questionEnt.remove();
				_questionEnt = null;
			}
				
			_questionEnt = _questionLoc.getWorld().spawn(_questionLoc.getBlock().getLocation(), ItemFrame.class);
			_questionEnt.setItem(questionStack);
			
			//Teleport Players to Card Room
			for (Player player : _voteRooms.keySet())
			{
				VoteRoom room = _voteRooms.get(player);
				
				room.resetFrames(false);
				
				if (player.equals(_roundDealer))
					continue;
				
				player.teleport(room.Spawn);
				
				room.QuestionEnt.setItem(questionStack);
			}
		}
		else if (state == RoundState.Judge)
		{
			GetTeamList().get(0).SpawnTeleport();
			
			int i=0;
			for (VoteRoom room : _voteRooms.values())
			{
				room.displayAnswer();
			}
		}
	}
	
	public void addScore(Player player, double amount)
	{
		for (GameScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				score.Score += amount;
				EndCheck();
				return;
			}
		}

		_ranks.add(new GameScore(player, amount));
		
		sortScores();
	}

	private void sortScores() 
	{
		for (int i=0 ; i<_ranks.size() ; i++)
		{
			for (int j=_ranks.size()-1 ; j>0 ; j--)
			{
				if (_ranks.get(j).Score > _ranks.get(j-1).Score)
				{
					GameScore temp = _ranks.get(j);
					_ranks.set(j, _ranks.get(j-1));
					_ranks.set(j-1, temp);
				}
			}
		}
	}
	
	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		writeScoreboard();
	}
	
	public void writeScoreboard()
	{
		//Wipe Last
		Scoreboard.Reset();

		Scoreboard.WriteBlank();

		if (_roundState == RoundState.PlaceCards)
		{
			Scoreboard.Write(C.cYellow + C.Bold + "Select Time");
			Scoreboard.Write(UtilTime.MakeStr(Math.max(0, _playTime - (System.currentTimeMillis() - _roundStateTime)), 1));
			
			Scoreboard.WriteBlank();
			
			Scoreboard.Write(C.cYellow + C.Bold + "Judge");
			Scoreboard.Write(C.cWhite + _roundDealer.getName());	
		}
		else if (_roundState == RoundState.Judge)
		{	
			Scoreboard.Write(C.cYellow + C.Bold + "Judge Time");
			Scoreboard.Write(UtilTime.MakeStr(Math.max(0, _judgeTime - (System.currentTimeMillis() - _roundStateTime)), 1));
			
			Scoreboard.WriteBlank();
			
			Scoreboard.Write(C.cYellow + C.Bold + "Judge");
			Scoreboard.Write(C.cWhite + _roundDealer.getName());	
		}
		else if (_roundState == RoundState.View)
		{
			Scoreboard.Write(C.cYellow + C.Bold + "View Time");
			Scoreboard.Write(UtilTime.MakeStr(Math.max(0, _viewTime - (System.currentTimeMillis() - _roundStateTime)), 1));
		}
		
		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cYellow + C.Bold + "Round Wins");
		for (GameScore score : _ranks)
		{
			Scoreboard.Write((int)score.Score + " " + C.Bold + score.Player.getName());
		}


		Scoreboard.Draw();
	}
}
