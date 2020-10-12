package ehnetwork.game.arcade.managers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.timing.TimingManager;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.stats.StatTracker;
import ehnetwork.minecraft.game.core.combat.CombatManager.AttackReason;

public class GameCreationManager implements Listener
{
	ArcadeManager Manager;
	
	private ArrayList<Game> _ended = new ArrayList<Game>();
	
	private GameType _nextGame = null;
	
	private String _lastMap = "";
	private ArrayList<GameType> _lastGames = new ArrayList<GameType>();

	public String MapPref = null;
	public String MapSource = null;
	
	public GameCreationManager(ArcadeManager manager)
	{
		Manager = manager;
		
		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}
	
	public String GetLastMap()
	{
		return _lastMap;
	}

	public void SetLastMap(String file) 
	{
		_lastMap = file;
	}

	@EventHandler
	public void NextGame(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (Manager.GetGameList().isEmpty())
			return;
		
		while (_lastGames.size() > Manager.GetGameList().size() - 1)
			_lastGames.remove(_lastGames.size()-1);
		
		if (Manager.GetGame() == null && _ended.isEmpty())
		{
			CreateGame(null);
		}

		//Archive Game
		if (Manager.GetGame() != null)
		{			
			if (Manager.GetGame().GetState() == GameState.Dead)
			{
				HandlerList.unregisterAll(Manager.GetGame());

				//Schedule Cleanup
				_ended.add(Manager.GetGame());

				//Lobby Display
				Manager.GetLobby().DisplayLast(Manager.GetGame());

				Manager.SetGame(null);
			}
		}

		//Clean Archived Games
		Iterator<Game> gameIterator = _ended.iterator();

		while (gameIterator.hasNext())
		{	
			Game game = gameIterator.next();


			HandlerList.unregisterAll(game);
			
			for (StatTracker tracker : game.getStatTrackers())
				HandlerList.unregisterAll(tracker);
			
			TimingManager.start("GameCreationManager - Attempting Removal - " + game.GetName());
			 
			//Cleaned
			if (game.WorldData == null || game.WorldData.World == null)
			{
				gameIterator.remove(); 
			}
			else
			{ 
				
				boolean removedPlayers = false;
				if (UtilTime.elapsed(game.GetStateTime(), 20000))
				{
					TimingManager.start("GameCreationManager - Kick Players - " + game.GetName());	
						
					for (Player player : game.WorldData.World.getPlayers())	
					{
						System.out.println("Kicking [" + player.getName() + "] with Validity [" + player.isValid() + "] with Online [" + player.isOnline() + "]");
						
						player.remove();
						player.kickPlayer("Dead World");	
					}
						
					removedPlayers = true;
					
					TimingManager.stop("GameCreationManager - Kick Players - " + game.GetName());
				}
				
				//Clean
				if (removedPlayers || game.WorldData.World.getPlayers().isEmpty())
				{
					if (game.WorldData.World.getPlayers().isEmpty())
						System.out.println("World Player Count [" + game.WorldData.World.getPlayers().size() + "]");
					
					TimingManager.start("GameCreationManager - Uninit World - " + game.GetName());
					
					game.WorldData.Uninitialize();
					game.WorldData = null;
					gameIterator.remove();
					
					TimingManager.stop("GameCreationManager - Uninit World - " + game.GetName());
				};
			}
			
			TimingManager.stop("GameCreationManager - Attempting Removal - " + game.GetName());
		}
	}

	private void CreateGame(GameType gameType) 
	{
		//Reset Changes
		Manager.GetDamage().DisableDamageChanges = false;
		Manager.GetCreature().SetDisableCustomDrops(false);
		Manager.GetDamage().SetEnabled(true);
		Manager.GetExplosion().SetRegenerate(false);
		Manager.GetExplosion().SetTNTSpread(true);
//		Manager.GetAntiStack().SetEnabled(true);
		Manager.getCosmeticManager().setHideParticles(false);
		Manager.GetDamage().GetCombatManager().setUseWeaponName(AttackReason.CustomWeaponName);
		Manager.GetChat().setThreeSecondDelay(true);
		ItemStackFactory.Instance.SetUseCustomNames(false);

		HashMap<String, ChatColor> pastTeams = null;
		
		//Chosen Game
		if (_nextGame != null)
		{
			gameType = _nextGame;
			_nextGame = null;
			
			System.out.println("Staff Selected GameType: " + gameType);
		}

		//Pick Game
		if (gameType == null)
		{
			for (int i=0 ; i<50 ; i++)
			{
				gameType = Manager.GetGameList().get(UtilMath.r(Manager.GetGameList().size()));

				if (!_lastGames.contains(gameType))
					break;
			}
		}
		
		//Champions
		Manager.toggleChampionsModules(gameType);
		
		_lastGames.add(0, gameType);

		try
		{
			Game game = gameType.getGameClass().getConstructor(ArcadeManager.class).newInstance(Manager);

			Manager.SetGame(game);
		}
		catch (NoSuchMethodException ex)
		{
			System.err.println("Is the constructor for " + gameType.GetName() + " using only one argument?");
			ex.printStackTrace();
			return;
		}
		catch (InvocationTargetException ex)
		{
			ex.getCause().printStackTrace();
			return;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return;
		}

		if (Manager.GetGame() == null)
		{
			return;
		}

		TimingManager.start("DisplayNext");
		Manager.GetLobby().DisplayNext(Manager.GetGame(), pastTeams);
		TimingManager.stop("DisplayNext");

		TimingManager.start("registerEvents");
		UtilServer.getServer().getPluginManager().registerEvents(Manager.GetGame(), Manager.getPlugin());
		TimingManager.stop("registerEvents");
	}

	public void SetNextGameType(GameType type)
	{
		_nextGame = type;
	}
}
