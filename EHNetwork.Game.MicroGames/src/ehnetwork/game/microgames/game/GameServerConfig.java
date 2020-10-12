package ehnetwork.game.microgames.game;

import java.util.ArrayList;

import ehnetwork.game.microgames.GameType;

public class GameServerConfig 
{
	public String ServerType = null;
	public int MinPlayers = -1;
	public int MaxPlayers = -1;
	public ArrayList<GameType> GameList = new ArrayList<GameType>();
	
	//Flags
	public String HostName = "";
	
	public boolean Tournament = false;
	
	public boolean TournamentPoints = false;
	
	public boolean TeamRejoin = false;
	public boolean TeamAutoJoin = true;
	public boolean TeamForceBalance = true;
	
	public boolean GameAutoStart = true;
	public boolean GameTimeout = true;

	public boolean RewardGems = true;
	public boolean RewardItems = true;	
	public boolean RewardStats = true;
	public boolean RewardAchievements = true;
	
	public boolean HotbarInventory = true;
	public boolean HotbarHubClock = true;
	
	public boolean PlayerKickIdle = true;
	
	public boolean PublicServer = true;

	public boolean PlayerServerWhitelist = false;
	
	public boolean IsValid()
	{
		return ServerType != null && MinPlayers != -1 && MaxPlayers != -1;
	}
}
