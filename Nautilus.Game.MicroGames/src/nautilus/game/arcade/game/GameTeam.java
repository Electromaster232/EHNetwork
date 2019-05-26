package nautilus.game.arcade.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import mineplex.core.account.CoreClient;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.visibility.VisibilityManager;
import nautilus.game.arcade.game.GameTeam.PlayerState;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GameTeam 
{
	private Game Host;

	private double _respawnTime = 0;

	public enum PlayerState
	{
		IN("In", ChatColor.GREEN),
		OUT("Out", ChatColor.RED);

		private String name;
		private ChatColor color;

		private PlayerState(String name, ChatColor color) 
		{
			this.name = name;
			this.color = color;
		}

		public String GetName()
		{
			return name;
		}

		public ChatColor GetColor()
		{
			return color;
		}
	}

	private String _name;
	private String _displayName;
	private ChatColor _color;

	private HashMap<Player, PlayerState> _players = new HashMap<Player, PlayerState>();

	private ArrayList<Location> _spawns;
	
	private Creature _teamEntity = null;

	private HashSet<Kit> _kitRestrict = new HashSet<Kit>();

	private boolean _visible = true;

	private boolean _displayTag;
	
	//Records order players go out in
	protected ArrayList<Player> _places = new ArrayList<Player>();

	public GameTeam(Game host, String name, ChatColor color, ArrayList<Location> spawns, boolean tags)
	{
		Host = host;

		_displayName = name;
		_name = name;
		_color = color;
		_spawns = spawns;
		_displayTag = tags;
	}
	
	public GameTeam(Game host, String name, ChatColor color, ArrayList<Location> spawns)
	{
		this(host, name, color, spawns, false);
	}

	public String GetName()
	{
		return _name;
	}

	public ChatColor GetColor()
	{
		return _color;
	}

	public ArrayList<Location> GetSpawns()
	{
		return _spawns;
	}

	public Location GetSpawn() 
	{	
		//Keep allies together
		if (!Host.IsLive() && Host.SpawnNearAllies)
		{
			//Find Location Nearest Ally
			Location loc = UtilAlg.getLocationNearPlayers(_spawns, GetPlayers(true), Host.GetPlayers(true));
			if (loc != null)
				return loc;
				
			//No allies existed spawned yet
			
			//Spawn near enemies (used for SG)
			if (Host.SpawnNearEnemies)
			{
				loc = UtilAlg.getLocationNearPlayers(_spawns, Host.GetPlayers(true), Host.GetPlayers(true));
				if (loc != null)
					return loc;
			}
			//Spawn away from enemies
			else
			{
				loc = UtilAlg.getLocationAwayFromPlayers(_spawns, Host.GetPlayers(true));
				if (loc != null)
					return loc;
			}			
		}
		else
		{
			//Spawn near players 
			if (Host.SpawnNearEnemies)
			{
				Location loc = UtilAlg.getLocationNearPlayers(_spawns, Host.GetPlayers(true), Host.GetPlayers(true));
				if (loc != null)
					return loc;
			}
			//Spawn away from players
			else
			{
				Location loc = UtilAlg.getLocationAwayFromPlayers(_spawns, Host.GetPlayers(true));
				if (loc != null)
					return loc;
			}
		}

		return _spawns.get(UtilMath.r(_spawns.size()));
	}

	public void AddPlayer(Player player, boolean in)
	{
		_players.put(player, in ? PlayerState.IN : PlayerState.OUT);

		UtilPlayer.message(player, F.main("Team",  _color + C.Bold + "You joined " + _displayName + " Team."));

		VisibilityManager.Instance.refreshPlayerToAll(player);
	}

	public void DisbandTeam() 
	{
		for (Player player : _players.keySet())
		{
			for(Player other : UtilServer.getPlayers())
			{
				other.getScoreboard().getTeam(Host.Manager.GetClients().Get(player).GetRank().Name + _name.toUpperCase()).removePlayer(player);
				other.getScoreboard().getTeam(Host.Manager.GetClients().Get(player).GetRank().Name).addPlayer(player);
			}
			UtilPlayer.message(player, F.main("Team",  _color + C.Bold + _displayName + " Team was disbanded."));
		}
		 
		_players.clear();
	}
	
	public void RemovePlayer(Player player)
	{
		_players.remove(player);
	}

	public Player GetPlayer(String name)
	{
		for (Player player : _players.keySet())
			if (player.getName().equals(name))
				return player;

		return null;
	}

	public boolean HasPlayer(Player player)
	{
		return _players.containsKey(player);
	}

	public boolean HasPlayer(String name, boolean alive)
	{
		for (Player player : _players.keySet())
			if (player.getName().equals(name))
				if (!alive || (alive && _players.get(player) == PlayerState.IN))
					return true;

		return false;
	}

	public int GetSize()
	{
		return _players.size();
	}

	public void SetPlayerState(Player player, PlayerState state) 
	{
		if (player == null)
			return;

		_players.put(player, state);
	}

	public boolean IsTeamAlive()
	{
		for (PlayerState state : _players.values())
			if (state == PlayerState.IN)
				return true;

		return false;
	}

	public ArrayList<Player> GetPlayers(boolean playerIn)
	{
		ArrayList<Player> alive = new ArrayList<Player>();

		for (Player player : _players.keySet())
			if (!playerIn || (_players.get(player) == PlayerState.IN && player.isOnline()))
				alive.add(player);

		return alive;
	}

	public String GetFormattedName() 
	{
		return GetColor() + "§l" + GetName();
	}

	public void SpawnTeleport(Player player) 
	{ 
		player.leaveVehicle();
		player.eject();
		player.teleport(GetSpawn());
	}

	public void SpawnTeleport() 
	{
		SpawnTeleport(true);
	}

	public void SpawnTeleport(boolean aliveOnly) 
	{
		for (Player player : GetPlayers(aliveOnly))
		{
			SpawnTeleport(player);
		}
	}

	public HashSet<Kit> GetRestrictedKits()
	{
		return _kitRestrict;
	}

	public boolean KitAllowed(Kit kit)
	{
		if (kit.GetAvailability() == KitAvailability.Null)
			return false;

		return !_kitRestrict.contains(kit);
	}

	public boolean IsAlive(Player player)
	{
		if (!_players.containsKey(player))
			return false;

		return player.isOnline() && _players.get(player) == PlayerState.IN;
	}

	public void SetColor(ChatColor color) 
	{
		_color = color;
	}

	public void SetName(String name)
	{
		_name = name;
	}
	
	public void setDisplayName(String name)
	{
		_displayName = name;
	}

	public byte GetColorData()
	{
		if (GetColor() == ChatColor.WHITE)			return (byte)0;
		if (GetColor() == ChatColor.GOLD)			return (byte)1;
		if (GetColor() == ChatColor.LIGHT_PURPLE)	return (byte)2;
		if (GetColor() == ChatColor.AQUA)			return (byte)3;
		if (GetColor() == ChatColor.YELLOW)			return (byte)4;
		if (GetColor() == ChatColor.GREEN)			return (byte)5;
		//if (GetColor() == ChatColor.PINK)			return (byte)6;
		if (GetColor() == ChatColor.DARK_GRAY)		return (byte)7;
		if (GetColor() == ChatColor.GRAY)			return (byte)8;
		if (GetColor() == ChatColor.DARK_AQUA)		return (byte)9;
		if (GetColor() == ChatColor.DARK_PURPLE)	return (byte)10;
		if (GetColor() == ChatColor.BLUE)			return (byte)11;
		if (GetColor() == ChatColor.DARK_BLUE)		return (byte)11;
		//if (GetColor() == ChatColor.BROWN)		return (byte)12;
		if (GetColor() == ChatColor.DARK_GREEN)		return (byte)13;
		if (GetColor() == ChatColor.RED)			return (byte)14;
		if (GetColor() == ChatColor.DARK_RED)		return (byte)14;
		else										return (byte)15;
	}

	public Color GetColorBase()
	{
		if (GetColor() == ChatColor.WHITE)			return Color.WHITE;
		if (GetColor() == ChatColor.GOLD)			return Color.ORANGE;
		if (GetColor() == ChatColor.LIGHT_PURPLE)	return Color.PURPLE;
		if (GetColor() == ChatColor.AQUA)			return Color.AQUA;
		if (GetColor() == ChatColor.YELLOW)			return Color.YELLOW;
		if (GetColor() == ChatColor.GREEN)			return Color.GREEN;
		if (GetColor() == ChatColor.DARK_GRAY)		return Color.GRAY;
		if (GetColor() == ChatColor.GRAY)			return Color.GRAY;
		if (GetColor() == ChatColor.DARK_AQUA)		return Color.AQUA;
		if (GetColor() == ChatColor.DARK_PURPLE)	return Color.PURPLE;
		if (GetColor() == ChatColor.BLUE)			return Color.BLUE;
		if (GetColor() == ChatColor.DARK_BLUE)		return Color.BLUE;
		if (GetColor() == ChatColor.DARK_GREEN)		return Color.GREEN;
		if (GetColor() == ChatColor.RED)			return Color.RED;
		else										return Color.WHITE;
	}

	public void SetTeamEntity(Creature ent) 
	{
		_teamEntity = ent;
	}

	public LivingEntity GetTeamEntity()
	{
		return _teamEntity;
	}

	public void SetSpawns(ArrayList<Location> spawns) 
	{
		_spawns = spawns;
	}

	public void SetVisible(boolean b)
	{
		_visible = b;
	}

	public boolean GetVisible()
	{
		return _visible;
	}
	
	public void setDisplayTag(boolean b)
	{
		_displayTag = b;
	}
	
	public boolean GetDisplaytag()
	{
		return _displayTag;
	}

	public void SetRespawnTime(double i)
	{
		_respawnTime = i;
	}

	public double GetRespawnTime()
	{
		return _respawnTime;
	}

	public void SetPlacement(Player player, PlayerState state) 
	{
		if (state == PlayerState.OUT)
		{
			if (!_places.contains(player))
				_places.add(0, player);	
		}
		else
			_places.remove(player);
	}

	public ArrayList<Player> GetPlacements(boolean includeAlivePlayers)
	{
		if (includeAlivePlayers)
		{
			ArrayList<Player> placesClone = new ArrayList<Player>();

			for (Player player : _places)
			{
				placesClone.add(player);
			}

			for (Player player : GetPlayers(true)) 
			{
				placesClone.add(0, player);
			}

			return placesClone;
		}

		return _places;
	}
}
