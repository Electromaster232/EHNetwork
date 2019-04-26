package mineplex.game.clans.clans;

import java.sql.Timestamp;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilWorld;
import mineplex.game.clans.clans.repository.ClanRepository;
import mineplex.game.clans.clans.repository.ClanTerritory;
import mineplex.game.clans.clans.repository.tokens.ClanEnemyToken;
import mineplex.game.clans.clans.repository.tokens.ClanToken;

public class ClansDataAccessLayer 
{
	private ClansManager _manager;
	private ClanRepository _repository;
	
	public ClansDataAccessLayer(ClansManager clans)
	{
		_manager = clans;
		_repository = new ClanRepository(clans.getPlugin(), clans.getServerName());
	}

	public void delete(ClanInfo clan)
	{
		//Territory Unclaim
		for (String cur : clan.getClaimSet())
			_manager.getClaimMap().remove(cur);

		_manager.getClanMap().remove(clan.getName());

		for (String cur : clan.getMembers().keySet())
		{
			_manager.getClanMemberMap().remove(cur);
		}

		//Clean from Others
		for (ClanInfo cur : _manager.getClanMap().values())
		{
			cur.getAllyMap().remove(clan.getName());
			cur.getRequestMap().remove(clan.getName());
		}

		//Save
		_repository.deleteClan(clan.getId());

		//Log
		_manager.log("Deleted [" + clan.getName() + "].");
	}

	public ClanInfo create(String creator, String name, boolean admin) 
	{
		ClanToken token = new ClanToken();
		token.Name = name;
		token.Description = "No Description";
		token.Home = "";
		token.Admin = admin;

		//Create Clan
		ClanInfo clan = new ClanInfo(_manager, token);
		_manager.getClanMap().put(name, clan);

		//Save
		_repository.addClan(clan, token);

		//Log
		_manager.log("[" + clan.getName() + "] with Admin [" + admin + "] created by [" + creator + "].");

		return clan;
	}

	public void join(ClanInfo clan, String player, ClanRole role)
	{
		if (_manager.getClanMemberMap().containsKey(player))
			leave(_manager.getClanUtility().getClanByPlayer(player), player);

		//Update Clan
		clan.getMembers().put(player, role);
		_manager.getClanMemberMap().put(player, clan);
		clan.getInviteeMap().remove(player);
		clan.getInviterMap().remove(player);

		//Save
		_repository.addMember(clan.getId(), player, role.toString());

		//Log
		_manager.log("Added [" + player + "] to [" + clan.getName() + "].");
	}

	public void leave(ClanInfo clan, String player)
	{
		if (clan == null)
			return;

		//Update Clan
		clan.getMembers().remove(player);
		_manager.getClanMemberMap().remove(player);

		//Save
		_repository.removeMember(clan.getId(), player);

		//Log
		_manager.log("Removed [" + player + "] from [" + clan.getName() + "].");
	}

	public void role(ClanInfo clan, String player, ClanRole role)
	{
		//Update Clan
		clan.getMembers().put(player, role);

		//Save
		_repository.updateMember(clan.getId(), player, role.toString());

		//Log
		_manager.log("Removed [" + player + "] from [" + clan.getName() + "].");
	}

	public void invite(ClanInfo clan, String player, String inviter)
	{
		clan.getInviteeMap().put(player, System.currentTimeMillis());
		clan.getInviterMap().put(player, inviter);

		//Log
		_manager.log("Invited [" + player + "] to [" + clan.getName() + "] by [" + inviter + "].");
	}

	public void requestAlly(ClanInfo clan, ClanInfo target, String player)
	{
		clan.getRequestMap().put(target.getName(), System.currentTimeMillis());

		//Log
		_manager.log("Alliance Request to [" + target.getName() + "] from [" + clan.getName() + "] by [" + player + "].");
	}

	public void ally(ClanInfo cA, ClanInfo cB, String player)
	{
		//Remove Requests
		cA.getRequestMap().remove(cB.getName());
		cB.getRequestMap().remove(cA.getName());

		//Update ClansManager
		cA.getAllyMap().put(cB.getName(), false);
		cB.getAllyMap().put(cA.getName(), false);

		//Save
		_repository.addClanRelationship(cA.getId(), cB.getId(), false);
		_repository.addClanRelationship(cB.getId(), cA.getId(), false);

		//Log
		_manager.log("Added Ally for [" + cB.getName() + "] and [" + cA.getName() + "] by [" + player + "].");
	}

	public void enemy(ClanInfo clan, ClanInfo otherClan, String player)
	{
		_repository.addEnemy(clan.getId(), otherClan.getId());
		Timestamp currDate = new Timestamp(System.currentTimeMillis());

		ClanEnemyToken clanEnemyToken = new ClanEnemyToken();
		clanEnemyToken.Initiator = true;
		clanEnemyToken.TimeFormed = currDate;
		clanEnemyToken.EnemyName = otherClan.getName();
		clan.updateEnemy(clanEnemyToken);

		ClanEnemyToken otherClanEnemyToken = new ClanEnemyToken();
		otherClanEnemyToken.Initiator = false;
		otherClanEnemyToken.TimeFormed = currDate;
		otherClanEnemyToken.EnemyName = clan.getName();
		otherClan.updateEnemy(otherClanEnemyToken);

		_manager.log("Added Enemy for [" + clan.getName() + "] and [" + otherClan.getName() + "] by [" + player + "].");
	}

	public boolean trust(ClanInfo ownerClan, ClanInfo otherClan, String player)
	{
		if (!ownerClan.getAllyMap().containsKey(otherClan.getName()))
			return false;

		boolean trust = !ownerClan.getAllyMap().get(otherClan.getName());

		//Memory
		ownerClan.getAllyMap().put(otherClan.getName(), trust);

		//Save
		_repository.updateClanRelationship(ownerClan.getId(), otherClan.getId(), trust);

		//Log
		_manager.log((trust ? "Gave" : "Revoked") + " Trust [" + trust + "] to [" + otherClan.getName() + "] for [" + ownerClan.getName() + "] by [" + player + "].");

		return trust;
	}

	public void neutral(ClanInfo cA, ClanInfo cB, String player, boolean bothClansManager) 
	{
		//Update ClansManager
		cA.getAllyMap().remove(cB.getName());
		cB.getAllyMap().remove(cA.getName());

		//Save
		_repository.removeClanRelationship(cA.getId(), cB.getId());
		_repository.removeClanRelationship(cB.getId(), cA.getId());

		//Log
		_manager.log("Added Neutral between [" + cA.getName() + "] and [" + cB.getName() + "] by [" + player + "].");
	}

	@SuppressWarnings("deprecation")
	public boolean claim(String name, String chunk, String player, boolean safe)
	{
		if (!_manager.getClanMap().containsKey(name))
			return false;

		ClanInfo clan = _manager.getClanMap().get(name);

		//Unclaim
		if (_manager.getClaimMap().containsKey(chunk))
			unclaim(chunk, player, false);

		//Memory
		ClanTerritory claim = new ClanTerritory();
		claim.Owner = name;
		claim.Safe = safe;
		clan.getClaimSet().add(chunk);
		_manager.getClaimMap().put(chunk, claim);

		//Save
		_repository.addTerritoryClaim(clan.getId(), chunk, safe);

		//Visual
		Chunk c = UtilWorld.strToChunk(chunk);
		if (!clan.isAdmin())
			for (int i = 0 ; i < 3 ; i++)
				for (int x=0 ; x < 16 ; x++)
					for (int z=0 ; z < 16 ; z++)
						if (z == 0 || z == 15 || x == 0 || x == 15)
						{
							Block down = UtilBlock.getHighest(c.getWorld(), c.getBlock(x, 0, z).getX(), c.getBlock(x, 0, z).getZ()).getRelative(BlockFace.DOWN);

							if (down.getTypeId() == 1 || down.getTypeId() == 2 || down.getTypeId() == 3 || down.getTypeId() == 12 || down.getTypeId() == 8)
								_manager.getBlockRestore().Add(down, 89, (byte)0, 180000);
						}

		//Log
		_manager.log("Added Claim for [" + name + "] at [" + chunk + "] by [" + player + "].");

		return true;
	}

	public boolean unclaim(String chunk, String player, boolean sql)
	{
		ClanTerritory claim = _manager.getClaimMap().remove(chunk);

		if (claim == null)
		{
			_manager.log("Unclaiming NULL Chunk Failed.");
			return false;
		}

		ClanInfo clan = _manager.getClanMap().get(claim.Owner);

		if (clan == null)
		{
			_manager.log("Unclaiming from NULL Clan Failed.");
			return false;
		}

		//Memory
		clan.getClaimSet().remove(chunk);

		//Save
		_repository.removeTerritoryClaim(clan.getId(), chunk);

		//Register
		// _manager.getUnclaimMap().put(chunk, System.currentTimeMillis());

		//Log
		if (player != null)
			_manager.log("Removed Claim for [" + clan.getName() + "] at [" + chunk + "] by [" + player + "].");

		return true;
	}

	public boolean unclaimSilent(String chunk, boolean sql)
	{
		return unclaim(chunk, null, sql);
	}

	public void home(ClanInfo clan, Location loc, String player)
	{
		//Memory
		clan.setHome(loc);

		//Save
		_repository.updateClan(clan.getId(), clan.getName(), clan.getDesc(), UtilWorld.locToStr(clan.getHome()), clan.isAdmin(), clan.getEnergy(), clan.getLastOnline());

		//Log
		_manager.log("Set Home for [" + clan.getName() + "] to " + UtilWorld.locToStrClean(loc) + " by [" + player + "].");
	}

	public void updateEnemy(ClanInfo clan, ClanInfo otherClan)
	{
		assert clan.getEnemyData() != null && otherClan.getEnemyData() != null;
		assert clan.getEnemyData().getEnemyName() == otherClan.getName() && otherClan.getEnemyData().getEnemyName() == clan.getName();

		ClanInfo initiator = clan.getEnemyData().isInitiator() ? clan : otherClan;
		EnemyData iData = initiator.getEnemyData();
		ClanInfo other = clan == initiator ? otherClan : clan;
		EnemyData oData = other.getEnemyData();
		_repository.updateEnemy(initiator.getId(), other.getId(), iData.getScore(), oData.getScore(), iData.getKills(), oData.getKills());

		//Log
		_manager.log("Updated Enemy Data for [" + clan.getName() + ", " + otherClan.getName() + "]");
	}
	
	public void updateEnergy(ClanInfo clan)
	{
		//Save
		_repository.updateClan(clan.getId(), clan.getName(), clan.getDesc(), UtilWorld.locToStr(clan.getHome()), clan.isAdmin(), clan.getEnergy(), clan.getLastOnline());

		//Log
		_manager.log("Updated Energy for [" + clan.getName() + "] to " + clan.getEnergy() + ".");
	}

	public void safe(ClanTerritory claim, String player)
	{
		//Memory
		claim.Safe = !claim.Safe;

		//Save
		_repository.updateTerritoryClaim(claim.Chunk, claim.Safe);

		//Log
		_manager.log("Safe Zone at [" + claim.Chunk + "] set to [" + claim.Safe + "] by [" + player + "].");
	}

	public ClanRepository getRepository()
	{
		return _repository;
	}
}
