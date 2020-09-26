package ehnetwork.game.skyclans.clans;

import java.util.LinkedList;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.skyclans.clans.repository.ClanTerritory;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ClansDisplay extends MiniPlugin
{
	private ClansManager Clans;
	
	public ClansDisplay(JavaPlugin plugin, ClansManager clans)
	{
		super("Clans Display", plugin);
		
		Clans = clans;
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		for (Player cur : UtilServer.getPlayers())
			Update(cur);
	}
	
	public void Update(Player player) 
	{
		if (player.getWorld().getEnvironment() != Environment.NORMAL)
			return;

		ClientClan client = Clans.Get(player);	
		if (client == null)		return;

		//Same Chunk
		if (client.getTerritory().equals(UtilWorld.chunkToStr(player.getLocation().getChunk())))
			return;

		//Update Territory
		client.setTerritory(UtilWorld.chunkToStr(player.getLocation().getChunk()));

		//AutoClaim
		if (client.isAutoClaim())
			Clans.getClanAdmin().claim(player);
		
		//Map
		String owner = "?";
		ClanInfo ownerClan = Clans.getClanUtility().getOwner(player.getLocation());
		if (ownerClan != null)
			owner = ownerClan.getName();

		boolean safe = Clans.getClanUtility().isSafe(player);

		if (!client.isMapOn())
		{
			boolean showChange = false;

			//Owner Change
			if (!client.getOwner().equals(owner))
			{
				client.setOwner(owner);
				showChange = true;
			}

			//Safe Change
			if (safe != client.isSafe())
			{
				client.setSafe(safe);
				showChange = true;
			}

			if (showChange)
				displayOwner(player);
		}		
		else
		{
			displayOwner(player);
			displayMap(player);	
		}
	}

	public void displayOwner(Player player)
	{
		//Name 
		String ownerString = C.xWilderness + "Wilderness";

		ClanTerritory claim = Clans.getClanUtility().getClaim(player.getLocation());
		String append = "";
		if (claim != null)	
		{
			//Relation
			ClansUtility.ClanRelation relation = Clans.getClanUtility().relPT(player.getName(), claim.Chunk);
			
			//Name
			ownerString = Clans.getClanUtility().mRel(relation, claim.Owner, false);

			//Trust
			if (relation == ClansUtility.ClanRelation.ALLY_TRUST)
				append = C.mBody + "(" + C.mElem + "Trusted" + C.mBody + ")";
		}

		UtilPlayer.message(player, F.main("Clans", ownerString + " " + append));
	}

	public int width = 8;
	public int height = 4;

	public void displayMap(Player player)
	{
		if (player.getWorld().getEnvironment().equals(Environment.NETHER))
			return;

		//Get Local
		LinkedList<String> local = mLocalMap(player, player.getLocation().getChunk(), true);

		//Get Home
		LinkedList<String> home = null;

		if (player.getItemInHand().getType() == Material.MAP)
		{
			ClanInfo clan = Clans.getClanUtility().getClanByPlayer(player);
			if (clan != null)
				if (clan.getHome() != null)
					home = mLocalMap(player, clan.getHome().getChunk(), false);
		}

		//Display
		if (home == null || local.size() != home.size())
			UtilPlayer.message(player, local);

		else
			for (int i = 0 ; i < local.size() ; i++)
				UtilPlayer.message(player, local.get(i) + "            " + home.get(i));
	}

	public LinkedList<String> mLocalMap(Player player, Chunk chunk, boolean local)
	{
		if (chunk == null)
			return null;

		LinkedList<String> localMap = new LinkedList<String>();

		for (int i=(chunk.getX()-height) ; i <= (chunk.getX()+height) ; i++)
		{
			String output = C.xNone + "<";

			for (int j=(chunk.getZ()+width) ; j >= (chunk.getZ()-width) ; j--)
			{
				Chunk curChunk = player.getWorld().getChunkAt(i, j);

				//Count Players
				int pCount = 0;
				if (player.getItemInHand().getType() == Material.MAP)
				{
					for (Player cur : UtilServer.getPlayers())
						if (cur.getLocation().getChunk().toString().equals(curChunk.toString()))
							pCount++;
				}

				//Get Data
				ClanInfo curOwner = Clans.getClanUtility().getOwner(UtilWorld.chunkToStr(curChunk));
				ClanTerritory curClaim = Clans.getClanUtility().getClaim(UtilWorld.chunkToStr(curChunk));

				//Add Icon
				if (i == chunk.getX() && j == chunk.getZ())
					output += getMapIcon(Clans.getClanUtility().relPC(player.getName(), curOwner), curClaim, curOwner, curChunk, pCount, true, local);
				else
					output += getMapIcon(Clans.getClanUtility().relPC(player.getName(), curOwner), curClaim, curOwner, curChunk, pCount, false, local);
			}

			output += ">";

			//Send
			localMap.add(output);
		}

		return localMap;
	}

	public String getMapIcon(ClansUtility.ClanRelation relation, ClanTerritory claim, ClanInfo owner, Chunk chunk, int players, boolean mid, boolean local)
	{
		if (players > 9)
			players = 9;

		if (mid && local)
		{
			if (players > 0)	return "" + C.cWhite + players;
			else				return "" + C.cWhite + "X";
		}

		if (owner == null || claim == null)			
		{
			if (players > 0)	return "" + C.xNone + players;
			else				return "" + C.xNone + "-";
		}

		if (claim.Safe)
		{
			if (players > 0)	return "" + C.xSafe + players;
			else				return "" + C.xSafe + "S";
		}

		if (owner.isAdmin())		
		{
			if (players > 0)	return "" + C.xAdmin + players;
			else				return "" + C.xAdmin + "+";
		}


		if (relation == ClansUtility.ClanRelation.SELF)
		{
			if (players > 0)									return "" + C.xSelf + players;
			else if (Clans.getClanUtility().isChunkHome(owner, chunk))	return "" + C.xSelf + "H";
			else												return "" + C.xSelf + "#";
		}

		if (relation == ClansUtility.ClanRelation.ALLY)
		{
			if (players > 0)									return "" + C.xAlly + players;
			else if (Clans.getClanUtility().isChunkHome(owner, chunk))	return "" + C.xAlly + "H";
			else												return "" + C.xAlly + "#";
		}

		if (relation == ClansUtility.ClanRelation.ALLY_TRUST)
		{
			if (players > 0)									return "" + C.xdAlly + players;
			else if (Clans.getClanUtility().isChunkHome(owner, chunk))	return "" + C.xdAlly + "H";
			else												return "" + C.xdAlly + "#";
		}

		if (players > 0)										return "" + C.xEnemy + players;
		else if (Clans.getClanUtility().isChunkHome(owner, chunk))		return "" + C.xEnemy + "H";
		else													return "" + C.xEnemy + "#";
	}

	public void handleInteract(PlayerInteractEvent event) 
	{
		if (event.getPlayer().getItemInHand().getType() != Material.MAP)
			return;

		if (!Recharge.Instance.use(event.getPlayer(), "Clan Map", 500, false, false))
			return;
		
		displayOwner(event.getPlayer());
		displayMap(event.getPlayer());	
	}


}
