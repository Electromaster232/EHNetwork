package ehnetwork.game.arcade.managers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.kit.Kit;

public class LobbyEnt 
{
	private Kit _kit;
	private GameTeam _team;
	private Entity _ent;
	private Location _loc;
	
	public LobbyEnt(Entity ent, Location loc, Kit kit)
	{
		_ent = ent;
		_loc = loc;
		_kit = kit;
	}
	
	public LobbyEnt(Entity ent, Location loc, GameTeam team)
	{
		_ent = ent;
		_loc = loc;
		_team = team;
	}
	
	public Kit GetKit()
	{
		return _kit;
	}
	
	public GameTeam GetTeam()
	{
		return _team;
	}
	
	public Entity GetEnt()
	{
		return _ent;
	}
	
	public Location GetLocation()
	{
		return _loc;
	}
}
