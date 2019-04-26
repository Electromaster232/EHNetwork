package mineplex.game.clans.clans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.core.common.util.UtilWorld;
import mineplex.game.clans.clans.ClansUtility.ClanRelation;
import mineplex.game.clans.clans.repository.tokens.ClanAllianceToken;
import mineplex.game.clans.clans.repository.tokens.ClanEnemyToken;
import mineplex.game.clans.clans.repository.tokens.ClanMemberToken;
import mineplex.game.clans.clans.repository.tokens.ClanTerritoryToken;
import mineplex.game.clans.clans.repository.tokens.ClanToken;

public class ClanInfo
{
	private int _id = -1;
	private String _name = "";
	private String _desc = "";
	private Location _home = null;
	private int _energy = 1440;

	private boolean _admin = false;

	private Timestamp _dateCreated = null;
	private Timestamp _lastOnline = null;

	// Loaded from Client
	private NautHashMap<String, ClanRole> _memberMap = new NautHashMap<String, ClanRole>();
	private NautHashMap<String, Boolean> _allyMap = new NautHashMap<String, Boolean>();
	private HashSet<String> _claimSet = new HashSet<String>();
	private EnemyData _enemyData;

	// Temporary
	private NautHashMap<String, Long> _inviteeMap = new NautHashMap<String, Long>();
	private NautHashMap<String, String> _inviterMap = new NautHashMap<String, String>();
	private List<UUID> _onlinePlayers = new ArrayList<UUID>();

	private NautHashMap<String, Long> _requestMap = new NautHashMap<String, Long>();

	public ClansManager Clans;

	public ClanInfo(ClansManager clans, ClanToken token)
	{
		Clans = clans;

		_id = token.Id;
		_name = token.Name;
		_desc = token.Description;

		try
		{
			_home = UtilWorld.strToLoc(token.Home);
		}
		catch (Exception e)
		{

		}
		
		_energy = token.Energy;
		_admin = token.Admin;

		_dateCreated = token.DateCreated;
		_lastOnline = token.LastOnline;
		
		for (ClanMemberToken memberToken : token.Members)
		{
			_memberMap.put(memberToken.Name, ClanRole.valueOf(memberToken.ClanRole));
		}
		
		for (ClanTerritoryToken territoryToken : token.Territories)
		{
			_claimSet.add(territoryToken.Chunk);
		}
		
		for (ClanAllianceToken allianceToken : token.Alliances)
		{
			_allyMap.put(allianceToken.ClanName, allianceToken.Trusted);
		}

		updateEnemy(token.EnemyToken);
	}

	public void updateEnemy(ClanEnemyToken enemyToken)
	{
		if (enemyToken != null)
		{
			_enemyData = new EnemyData(enemyToken.EnemyName, enemyToken.Initiator, enemyToken.Score, enemyToken.Kills, enemyToken.TimeFormed);
		}
	}

	public int getClaims()
	{
		return getClaimSet().size();
	}

	public int getClaimsMax()
	{
		if (ssAdmin())
			return 1000;

		return 2 + getMembers().size();
	}

	public int getAllies()
	{
		return getAllyMap().size();
	}

	public int getAlliesMax()
	{
		if (ssAdmin())
			return 1000;

		return Math.max(2, 9 - _memberMap.size());
	}

	public boolean isRequested(String clan)
	{
		if (!getRequestMap().containsKey(clan))
			return false;

		if (System.currentTimeMillis() > getRequestMap().get(clan) + (Clans.getInviteExpire() * 60000))
			return false;

		return true;
	}

	public boolean isInvited(String player)
	{
		if (!getInviteeMap().containsKey(player))
			return false;

		if (System.currentTimeMillis() > getInviteeMap().get(player) + (Clans.getInviteExpire() * 60000))
			return false;

		return true;
	}

	public boolean isMember(String other)
	{
		return getMembers().containsKey(other);
	}

	public boolean isAlly(String other)
	{
		return getAllyMap().containsKey(other);
	}

	public boolean isSelf(String other)
	{
		return this.getName().equals(other);
	}

	public boolean isNeutral(String other)
	{
		return (!isAlly(other) && !isSelf(other));
	}

	public long getTimer()
	{
		int penalty = 0;
		return System.currentTimeMillis() + (penalty * 1000);
	}

	public boolean getTrust(String clan)
	{
		if (!getAllyMap().containsKey(clan))
			return false;

		return getAllyMap().get(clan);
	}
	
	public LinkedList<String> mDetails(String caller)
	{
		LinkedList<String> stringList = new LinkedList<String>();

		stringList.add(F.main("Clans",	Clans.getClanUtility().mRel(Clans.getClanUtility().relPC(caller, this), getName() + " Information;", true)));
		// stringList.add(F.value("Desc", _desc));

		// Age
		stringList.add(F.value("Age",
				UtilTime.convertString(System.currentTimeMillis() - _dateCreated.getTime(), 1, TimeUnit.FIT)));

		// Home
		if (Clans.getClanUtility().relPC(caller, this) == ClanRelation.SELF)
			stringList.add(F.value("Home", UtilWorld.locToStrClean(getHome())));

		// Land
		stringList.add(F.value("Territory", getClaims() + "/" + getClaimsMax()));

		// Energy
		int energy = getEnergy();
		int costPerHour = getEnergyCostPerMinute() * 60;
		stringList.add(" ");
		stringList.add(F.value("Clan Energy", "" + energy));
//		stringList.add(F.value("Max Energy", "" + getEnergyMax()));
		stringList.add(F.value("Energy Drain/Hour", "" + costPerHour));
		if (costPerHour > 0)
			stringList.add(F.value("Hours Left", "" +  energy / costPerHour));
		stringList.add(" ");

		// Ally String
		String allySorted = "";
		HashSet<String> allyUnsorted = new HashSet<String>();

		for (String allyName : getAllyMap().keySet())
			allyUnsorted.add(allyName);

		for (String cur : UtilAlg.sortKey(allyUnsorted))
			allySorted += Clans.getClanUtility().mRel(Clans.getClanUtility().relPC(caller, Clans.getClanMap().get(cur)), cur, false)
					+ ", ";

		stringList.add(F.value("Allies", allySorted));

		// Members
		String members = "";
		for (String cur : UtilAlg.sortKey(getMembers().keySet()))
		{
			String name = C.listValueOff + cur;
			if (UtilPlayer.isOnline(cur))
				name = C.listValueOn + cur;

			if (getMembers().get(cur) == ClanRole.LEADER)
				members += C.listValue + "L." + name + C.mBody + ", ";

			if (getMembers().get(cur) == ClanRole.ADMIN)
				members += C.listValue + "A." + name + C.mBody + ", ";

			if (getMembers().get(cur) == ClanRole.MEMBER)
				members += C.listValue + "M." + name + C.mBody + ", ";

			if (getMembers().get(cur) == ClanRole.RECRUIT)
				members += C.listValue + "R." + name + C.mBody + ", ";
		}
		stringList.add(F.value("Members", members));

		// Protected
		stringList.add(F.value("TNT Protection", getProtected()));

		return stringList;
	}

	public LinkedList<String> mEnemy()
	{
		LinkedList<String> stringList = new LinkedList<String>();

		if (_enemyData == null)
		{
			stringList.add(F.main("Clans", "You do not have an enemy!"));
		}
		else
		{
			stringList.add(F.main("Clans", "Enemy Details"));
			String enemyName = _enemyData.getEnemyName();
			EnemyData otherEnemyData = Clans.getClanUtility().getClanByClanName(enemyName).getEnemyData();
			stringList.add(F.value("Enemy", _enemyData.getEnemyName()));
			stringList.add(F.value("Formed", _enemyData.getRelativeTimeFormed()));
			stringList.add(F.value("War Score", _enemyData.getScore() + " : " + otherEnemyData.getScore()));
		}

		return stringList;
	}

	public LinkedList<String> mTerritory()
	{
		LinkedList<String> stringList = new LinkedList<String>();

		stringList.add(F.main("Clans", getName() + " Territory;"));

		for (String cur : getClaimSet())
		{
			stringList.add(cur);
		}

		return stringList;
	}

	public void inform(String message, String ignore)
	{
		for (String cur : getMembers().keySet())
		{
			if (ignore != null && cur.equals(ignore))
				continue;

			Player player = UtilPlayer.searchOnline(null, cur, false);

			if (player == null)
				continue;

			UtilPlayer.message(player, F.main("Clans", message));
			player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
		}
	}

	public String getName()
	{
		return _name;
	}

	public String getDesc()
	{
		return _desc;
	}

	public void setDesc(String desc)
	{
		_desc = desc;
	}

	public NautHashMap<String, ClanRole> getMembers()
	{
		return _memberMap;
	}

	public HashSet<String> getClaimSet()
	{
		return _claimSet;
	}

	public Location getHome()
	{
		return _home;
	}

	public void setHome(Location loc)
	{
		_home = loc;
	}

	public boolean ssAdmin()
	{
		return _admin;
	}

	public void setAdmin(boolean admin)
	{
		_admin = admin;
	}

	public NautHashMap<String, String> getInviterMap()
	{
		return _inviterMap;
	}

	public NautHashMap<String, Long> getInviteeMap()
	{
		return _inviteeMap;
	}

	public NautHashMap<String, Boolean> getAllyMap()
	{
		return _allyMap;
	}

	public NautHashMap<String, Long> getRequestMap()
	{
		return _requestMap;
	}

	public Timestamp getDateCreated()
	{
		return _dateCreated;
	}

	public Timestamp getLastOnline()
	{
		return _lastOnline;
	}

	public void setLastOnline(Timestamp lastOnline)
	{
		_lastOnline = lastOnline;
	}

	public boolean isOnlineNow()
	{
		for (String cur : getMembers().keySet())
			if (UtilPlayer.isOnline(cur))
				return true;

		return false;
	}

	public boolean isOnline()
	{
		for (String cur : getMembers().keySet())
			if (UtilPlayer.isOnline(cur))
				return true;

		return System.currentTimeMillis() - _lastOnline.getTime() < Clans.getOnlineTime();
	}

	public String getProtected()
	{
		for (String cur : getMembers().keySet())
			if (UtilPlayer.isOnline(cur))
				return C.cRed + "No - Clan Members are Online";

		if (System.currentTimeMillis() - _lastOnline.getTime() > Clans.getOnlineTime())
			return C.cGreen + "Yes - Clan Members are Offline";

		return C.cGold
				+ "No, "
				+ UtilTime.convertString(Clans.getOnlineTime() - (System.currentTimeMillis() - _lastOnline.getTime()), 1,
						TimeUnit.FIT) + " to Protection";
	}

	public boolean isAdmin()
	{
		return _admin;
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

	public int getEnergy()
	{
		return _energy;
	}

	public EnemyData getEnemyData()
	{
		return _enemyData;
	}

	public boolean hasEnemy()
	{
		return _enemyData != null;
	}
	
	public void adjustEnergy(int energy)
	{
		_energy += energy;
	}

	public int getClaimCount()
	{
		return _claimSet.size();
	}

	public int getSize()
	{
		return _memberMap.size();
	}

	public int getEnergyMax()
	{
		// 10080 = 7 days of minutes
		return 1440 + (getEnergyCostPerMinute() * 10080);
	}

	public int getEnergyCostPerMinute()
	{
		return (getSize() * getClaimCount());
	}

	public int getEnergyPurchasable()
	{
		int diff = getEnergyMax() - getEnergy();
		return diff > 0 ? diff : 0;
	}

	public List<Player> getOnlinePlayers()
	{
		ArrayList<Player> players = new ArrayList<Player>(_onlinePlayers.size());
		for (UUID uuid : _onlinePlayers)
		{
			players.add(UtilPlayer.searchExact(uuid));
		}
		return players;
	}

	protected void playerOnline(Player player)
	{
		_onlinePlayers.add(player.getUniqueId());
	}

	protected void playerOffline(Player player)
	{
		_onlinePlayers.remove(player.getUniqueId());
	}
}
