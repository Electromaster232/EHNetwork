package nautilus.game.arcade.managers;

import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.kit.Kit;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

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
