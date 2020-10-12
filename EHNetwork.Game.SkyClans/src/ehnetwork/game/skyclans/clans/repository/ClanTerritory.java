package ehnetwork.game.skyclans.clans.repository;

import ehnetwork.game.skyclans.clans.repository.tokens.ClanTerritoryToken;

public class ClanTerritory
{
	public ClanTerritory() { }
	
	public ClanTerritory(ClanTerritoryToken territoryToken)
	{
		Owner = territoryToken.ClanName;
		Safe = territoryToken.Safe;
		Chunk = territoryToken.Chunk;
	}
	
	public boolean Safe;
	public String Owner = "";
	public String Chunk = "";
}
