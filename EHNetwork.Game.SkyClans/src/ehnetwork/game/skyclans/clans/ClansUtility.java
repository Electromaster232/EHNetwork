package ehnetwork.game.skyclans.clans;

import java.util.LinkedList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.game.skyclans.clans.repository.ClanTerritory;

public class ClansUtility 
{
	private ClansManager Clans;
	
	public ClansUtility(ClansManager clans)
	{
		Clans = clans;
	}

	public enum ClanRelation
	{
		SELF,
		ALLY,
		ALLY_TRUST,
		NEUTRAL,
		ADMIN,
		SAFE
	}

	public ClanInfo searchClanPlayer(Player caller, String name, boolean inform)
	{
		//CLAN
		LinkedList<ClanInfo> clanMatchList = new LinkedList<ClanInfo>();

		for (ClanInfo cur : Clans.getClanMap().values())
		{
			if (cur.getName().equalsIgnoreCase(name))
				return cur;

			if (cur.getName().toLowerCase().contains(name.toLowerCase()))
				clanMatchList.add(cur);
		}

		if (clanMatchList.size() == 1)
			return clanMatchList.get(0);

		//No / Non-Unique
		String clanMatchString = "None";
		if (clanMatchList.size() > 1)
		{
			for (ClanInfo cur : clanMatchList)
				clanMatchString += cur.getName() + " ";
		}

		//PLAYER
		LinkedList<ClanInfo> playerMatchList = new LinkedList<ClanInfo>();

		for (Entry<String, ClanInfo> clanMemberEntry : Clans.getClanMemberMap().entrySet())
		{
			if (clanMemberEntry.getKey().equalsIgnoreCase(name))
				return clanMemberEntry.getValue();

			if (clanMemberEntry.getKey().toLowerCase().contains(name.toLowerCase()))
				playerMatchList.add(clanMemberEntry.getValue());
		}

		if (playerMatchList.size() == 1)
			return playerMatchList.get(0);

		//No / Non-Unique
		String playerMatchString = "None";
		if (playerMatchList.size() > 1)
		{
			for (ClanInfo cur : playerMatchList)
				playerMatchString += cur.getName() + " ";
		}

		if (inform)
		{
			UtilPlayer.message(caller, F.main("Clan Search", "" +
					C.mCount + (clanMatchList.size() + playerMatchList.size()) +
					C.mBody + " matches for [" +
					C.mElem + name +
					C.mBody + "]."), false);

			UtilPlayer.message(caller, F.desc("Matches via Clan", clanMatchString), false);
			UtilPlayer.message(caller, F.desc("Matches via Player", playerMatchString), false);;
		}

		return null;
	}

	public ClanInfo searchClan(Player caller, String name, boolean inform)
	{
		LinkedList<ClanInfo> matchList = new LinkedList<ClanInfo>();

		for (ClanInfo cur : Clans.getClanMap().values())
		{
			if (cur.getName().equalsIgnoreCase(name))
				return cur;

			if (cur.getName().toLowerCase().contains(name.toLowerCase()))
				matchList.add(cur);
		}

		//No / Non-Unique
		if (matchList.size() != 1)
		{
			if (!inform)
				return null;

			//Inform
			UtilPlayer.message(caller, F.main("Clan Search", "" +
					C.mCount + matchList.size() +
					C.mBody + " matches for [" +
					C.mElem + name +
					C.mBody + "]."), false);

			if (matchList.size() > 0)
			{
				String matchString = "";
				for (ClanInfo cur : matchList)
					matchString += cur.getName() + " ";

				UtilPlayer.message(caller, F.main("Clan Search", "" +
						C.mBody + " Matches [" +
						C.mElem + matchString +
						C.mBody + "]."), false);
			}

			return null;
		}

		return matchList.get(0);
	}

	public ClanInfo getClanByClanName(String clan)
	{
		return Clans.getClan(clan);
	}

	public ClanInfo getClanByPlayer(Player player)
	{
		return getClanByPlayer(player.getName());
	}

	public ClanInfo getClanByPlayer(String name)
	{
		if (!Clans.getClanMemberMap().containsKey(name))
			return null;

		return Clans.getClanMemberMap().get(name);
	}

	public ClanRole getRole(Player player)
	{
		try
		{
			return getClanByPlayer(player).getMembers().get(player.getName());
		}
		catch (Exception e)
		{
			return ClanRole.NONE;
		}	
	}

	public boolean isSafe(Player player)
	{
		if (!UtilTime.elapsed(Clans.getCombatManager().Get(player).GetLastDamaged(), 15000))
			return false;

		return isSafe(player.getLocation());
	}

	public boolean isSafe(Location loc)
	{
		if (!Clans.getClaimMap().containsKey(UtilWorld.chunkToStr(loc.getChunk())))
			return false;

		return Clans.getClaimMap().get(UtilWorld.chunkToStr(loc.getChunk())).Safe;
	}

	public boolean isChunkHome(ClanInfo clan, Chunk chunk)
	{
		if (clan == null)
			return false;

		if (clan.getHome() == null)
			return false;

		return clan.getHome().getChunk().equals(chunk);
	}

	public ClanTerritory getClaim(Location loc)
	{
		String chunk = UtilWorld.chunkToStr(loc.getChunk());
		return Clans.getClaimMap().get(chunk);
	}

	public ClanTerritory getClaim(String chunk)
	{
		return Clans.getClaimMap().get(chunk);
	}

	public ClanInfo getOwner(String chunk)
	{
		ClanTerritory claim = getClaim(chunk);

		if (claim == null)
			return null;

		return getOwner(claim);
	}

	public ClanInfo getOwner(Location loc)
	{
		ClanTerritory claim = getClaim(loc);

		if (claim != null)
			return getOwner(claim);

		return null;
	}

	public ClanInfo getOwner(ClanTerritory claim)
	{
		return getClanByClanName(claim.Owner);
	}

	public String getOwnerString(Location loc)
	{
		ClanInfo owner = getOwner(loc);

		if (owner == null)
			return "Wilderness";

		return owner.getName();
	}

	public String getOwnerStringRel(Location loc, String player)
	{
		ClanRelation rel = relPT(player, UtilWorld.chunkToStr(loc.getChunk()));
		return mRel(rel, getOwnerString(loc), true);
	}

	public boolean isClaimed(Location loc)
	{
		String chunk = UtilWorld.chunkToStr(loc.getChunk());

		return Clans.getClaimMap().containsKey(chunk);
	}

	public boolean isAlliance(String player, Location loc)
	{
		if (!Clans.getClanMemberMap().containsKey(player))
			return false;

		if (!isClaimed(loc))
			return false;

		return getOwner(getClaim(loc)).isAlly(Clans.getClanMemberMap().get(player).getName());
	}

	public boolean isSelf(String player, Location loc)
	{
		if (!Clans.getClanMemberMap().containsKey(player))
			return false;

		if (!isClaimed(loc))
			return false;

		return getOwner(getClaim(loc)).isSelf(Clans.getClanMemberMap().get(player).getName());
	}

	public boolean isAdmin(Location loc)
	{
		if (!isClaimed(loc))
			return false;

		return getOwner(getClaim(loc)).isAdmin();
	}

	public boolean isSpecial(Location loc, String special)
	{
		if (!isClaimed(loc))
			return false;

		if (!isAdmin(loc))
			return false;

		return getOwner(getClaim(loc)).getName().toLowerCase().contains(special.toLowerCase());
	}

	public ClanRelation getAccess(Player player, Location loc)
	{
		ClanInfo owner = getOwner(loc);
		ClanInfo clan = getClanByPlayer(player);

		String mimic = Clans.Get(player).getMimic();
		
		if (mimic.length() != 0)
			clan = Clans.getClanUtility().searchClanPlayer(player, mimic, false);
		
		if (owner == null)
			return ClanRelation.SELF;

		if (owner.equals(clan))
			return ClanRelation.SELF;

		if (clan != null)
			if (owner.getTrust(clan.getName()))
				return ClanRelation.ALLY_TRUST;

		if (clan != null)
			if (owner.isAlly(clan.getName()))
				return ClanRelation.ALLY;

		return ClanRelation.NEUTRAL;
	}

	//Player Player
	public ClanRelation relPP(String pA, String pB) 
	{
		return rel(getClanByPlayer(pA), getClanByPlayer(pB));
	}

	//Clan Clan
	public ClanRelation relCC(String cA, String cB) 
	{
		return rel(searchClan(null, cA, false), searchClan(null, cB, false));
	}

	//Territory Territory
	public ClanRelation relTT(String tA, String tB) 
	{
		return rel(getOwner(tA), getOwner(tB));
	}

	//Player Clan
	public ClanRelation relPC(String pA, String cB) 
	{
		return rel(getClanByPlayer(pA), searchClan(null, cB, false));
	}

	//Player Clan (Object)
	public ClanRelation relPC(String pA, ClanInfo cB) 
	{
		return rel(getClanByPlayer(pA), cB);
	}

	//Player Territory
	public ClanRelation relPT(String pA, String tB) 
	{
		ClanTerritory claim = getClaim(tB);
		if (claim != null)
			if (claim.Safe)
				return ClanRelation.SAFE;

		return rel(getClanByPlayer(pA), getOwner(tB));
	}

	//Clan Territory
	public ClanRelation relCT(String cA, String tB) 
	{
		ClanTerritory claim = getClaim(tB);
		if (claim != null)
			if (claim.Safe)
				return ClanRelation.SAFE;

		return rel(searchClan(null, cA, false), getOwner(tB));
	}

	public ClanRelation rel(ClanInfo cA, ClanInfo cB) 
	{
		if (cA == null || cB == null)				
			return ClanRelation.NEUTRAL;

		//Self
		if (cA.isAdmin() || cB.isAdmin())	
			return ClanRelation.ADMIN;

		if (cA.getName().equals(cB.getName()))	
			return ClanRelation.SELF;

		//Ally
		if (cA.getTrust(cB.getName()))
			return ClanRelation.ALLY_TRUST;

		if (cA.isAlly(cB.getName()))
			return ClanRelation.ALLY;

		//Enemy
		return ClanRelation.NEUTRAL;
	}

	public ChatColor relChatColor(ClanRelation relation, boolean dark)
	{
		if (relation == ClanRelation.SAFE)				return C.xAdmin;
		if (relation == ClanRelation.ADMIN)				return C.xAdmin;

		if (!dark)
		{	
			if (relation == ClanRelation.SELF)			return C.xSelf;
			if (relation == ClanRelation.ALLY_TRUST)	return C.xdAlly;
			if (relation == ClanRelation.ALLY)			return C.xAlly;
			return C.xEnemy;
		}

		if (relation == ClanRelation.SELF)				return C.xdSelf;
		if (relation == ClanRelation.ALLY_TRUST)		return C.xAlly;
		if (relation == ClanRelation.ALLY)				return C.xdAlly;
		return C.xdEnemy;
	}

	public String relColor(ClanRelation relation, boolean dark)
	{
		if (relation == ClanRelation.SAFE)				return C.xAdmin + "(" + C.xSafe + "SAFE" + C.xAdmin + ") ";

		return relChatColor(relation, dark) + "";
	}

	public String mRel(ClanRelation relation, String message, boolean dark)
	{
		return relColor(relation, dark) + message + C.mChat;
	}

	public boolean playerSelf(String pA, String pB)
	{
		ClanInfo cA = getClanByPlayer(pA);
		ClanInfo cB = getClanByPlayer(pB);

		if (cA == null || cB == null)				
			return false;

		return cA.isSelf(cB.getName());
	}

	public boolean playerAlly(String pA, String pB)
	{
		ClanInfo cA = getClanByPlayer(pA);
		ClanInfo cB = getClanByPlayer(pB);

		if (cA == null || cB == null)				
			return false;

		return cA.isAlly(cB.getName());
	}

	public boolean playerEnemy(String pA, String pB)
	{
		ClanInfo cA = getClanByPlayer(pA);
		ClanInfo cB = getClanByPlayer(pB);

		if (cA == null || cB == null)				
			return true;

		return !(cA.isAlly(cB.getName()) || cA.isSelf(cB.getName()));
	}

	public boolean canHurt(Player damagee, Player damager)
	{
		if (damagee == null)
			return false;

		if (isSafe(damagee))
			return false;
		
		if (damager == null)
			return true;		

		if (isSafe(damager))
			return false;

		ClanRelation rel = relPP(damagee.getName(), damager.getName());

		if (rel == ClanRelation.ALLY || rel == ClanRelation.ALLY_TRUST || rel == ClanRelation.SELF)
			return false;

		return true;
	}
	
	public boolean isBorderlands(Location loc)
	{
		return (Math.abs(loc.getX()) > 400 || Math.abs(loc.getZ()) > 400);
	}
}
