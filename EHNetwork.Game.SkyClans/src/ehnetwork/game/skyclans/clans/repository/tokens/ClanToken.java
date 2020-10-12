package ehnetwork.game.skyclans.clans.repository.tokens;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClanToken
{
	public int Id;
	public String Name;
	public String Description;
	public String Home;
	public boolean Admin;
	public int Energy;
	public Timestamp DateCreated;
	public Timestamp LastOnline;

	public ClanEnemyToken EnemyToken;
	
	public List<ClanMemberToken> Members = new ArrayList<ClanMemberToken>();
	public List<ClanTerritoryToken> Territories = new ArrayList<ClanTerritoryToken>();
	public List<ClanAllianceToken> Alliances = new ArrayList<ClanAllianceToken>();
}
