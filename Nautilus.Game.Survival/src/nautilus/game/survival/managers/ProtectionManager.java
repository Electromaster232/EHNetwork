package nautilus.game.survival.managers;


import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.survival.SurvivalManager;

public class ProtectionManager extends MiniPlugin
{
	private SurvivalManager Manager;
	private NautHashMap<Player, Block> locX = new NautHashMap<Player, Block>();
	private NautHashMap<Player, Block> locY = new NautHashMap<Player, Block>();
	public ProtectionManager(SurvivalManager manager)
	{
		super("Area Protection", manager.getPlugin());
		Manager = manager;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		RegionContainer container = Manager.GetWorldGuard().getRegionContainer();
		RegionManager regions = container.get(event.getBlock().getWorld());
		ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(event.getBlock().getLocation()));
		for (ProtectedRegion region : set) {
			if(region.getMembers().contains(Manager.GetWorldGuard().wrapPlayer(event.getPlayer())) || region.getOwners().contains(Manager.GetWorldGuard().wrapPlayer(event.getPlayer()))){
			}
			else{
				event.setCancelled(true);
				UtilPlayer.message(event.getPlayer(), F.main("Protection", "You cannot build here. Contact the owner of this claim."));
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event){
		RegionContainer container = Manager.GetWorldGuard().getRegionContainer();
		RegionManager regions = container.get(event.getBlock().getWorld());
		ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(event.getBlock().getLocation()));
		for (ProtectedRegion region : set) {
			if(region.getMembers().contains(Manager.GetWorldGuard().wrapPlayer(event.getPlayer())) || region.getOwners().contains(Manager.GetWorldGuard().wrapPlayer(event.getPlayer()))){
			}
			else{
				event.setCancelled(true);
				UtilPlayer.message(event.getPlayer(), F.main("Protection", "You cannot break here. Contact the owner of this claim."));
			}
		}
	}


	@EventHandler
	public void addMember(PlayerCommandPreprocessEvent event){
		if(!event.getMessage().startsWith("/addmember")){
			return;
		}
		event.setCancelled(true);
		RegionContainer container = Manager.GetWorldGuard().getRegionContainer();
		RegionManager regions = container.get(event.getPlayer().getLocation().getWorld());
		ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(event.getPlayer().getLocation().subtract(0, 1, 0)));
		for (ProtectedRegion region : set) {
			if(region.getOwners().contains(Manager.GetWorldGuard().wrapPlayer(event.getPlayer()))){
				DefaultDomain currMembers = region.getMembers();
				try{
					Player playerToAdd = Manager.GetClients().Get(event.getMessage().substring(11)).GetPlayer();
					LocalPlayer localPlayer = Manager.GetWorldGuard().wrapPlayer(playerToAdd);
					currMembers.addPlayer(localPlayer);
					region.setMembers(currMembers);
					UtilPlayer.message(event.getPlayer(), F.main("Protection", "Member successfully added!"));
				}catch(NullPointerException e){
					UtilPlayer.message(event.getPlayer(), F.main("Protection", "That player is not online right now and cannot be added!"));
				}
			}
			else{
				UtilPlayer.message(event.getPlayer(), F.main("Protection", "You do not have permission to do this. Contact the owner of this claim."));
				return;
			}
		}

	}

	@EventHandler
	public void removeMember(PlayerCommandPreprocessEvent event){
		if(!event.getMessage().startsWith("/removemember")){
			return;
		}
		event.setCancelled(true);
		RegionContainer container = Manager.GetWorldGuard().getRegionContainer();
		RegionManager regions = container.get(event.getPlayer().getLocation().getWorld());
		ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(event.getPlayer().getLocation().subtract(0, 1, 0)));
		for (ProtectedRegion region : set) {
			if(region.getOwners().contains(Manager.GetWorldGuard().wrapPlayer(event.getPlayer()))){
				DefaultDomain currMembers = region.getMembers();
				try{
					Player playerToAdd = Manager.GetClients().Get(event.getMessage().substring(14)).GetPlayer();
					LocalPlayer localPlayer = Manager.GetWorldGuard().wrapPlayer(playerToAdd);
					currMembers.removePlayer(localPlayer);
					region.setMembers(currMembers);
					UtilPlayer.message(event.getPlayer(), F.main("Protection", "Member successfully removed!"));
				}catch(NullPointerException e){
					UtilPlayer.message(event.getPlayer(), F.main("Protection", "That player is not online right now and cannot be removed! If this is an emergency and you need this player to be removed immediately, use /a to contact an Admin."));
				}
			}
			else{
				UtilPlayer.message(event.getPlayer(), F.main("Protection", "You do not have permission to do this. Contact the owner of this claim."));
				return;
			}
		}

	}

	@EventHandler
	public void createRegion(PlayerCommandPreprocessEvent event){
		if(!event.getMessage().startsWith("/createregion")){
			return;
		}

		event.setCancelled(true);
		BlockVector min = BukkitUtil.toVector(locX.get(event.getPlayer()));
		BlockVector max = BukkitUtil.toVector(locY.get(event.getPlayer()));
		Random rand = new Random();
		String regionID = event.getPlayer().getName() + rand.nextInt();
		ProtectedRegion region = new ProtectedCuboidRegion(regionID, min, max);
		region.setFlag(DefaultFlag.BUILD, StateFlag.State.ALLOW);
		region.setFlag(DefaultFlag.USE, StateFlag.State.ALLOW);
		DefaultDomain owners = new DefaultDomain();
		LocalPlayer player = Manager.GetWorldGuard().wrapPlayer(event.getPlayer());
		owners.addPlayer(player);
		region.setOwners(owners);


		RegionContainer container = Manager.GetWorldGuard().getRegionContainer();
		RegionManager regions = container.get(event.getPlayer().getWorld());
		regions.addRegion(region);
		UtilPlayer.message(event.getPlayer(), F.main("Protection", "Region successfully created!"));

	}


	@EventHandler
	public void setLocX(PlayerCommandPreprocessEvent event){
		if(event.getMessage().startsWith("/setlocx")){
			event.setCancelled(true);
			Block loc = event.getPlayer().getLocation().subtract(0, 1,0).getBlock();
			locX.remove(event.getPlayer());
			locX.put(event.getPlayer(), loc);
			UtilPlayer.message(event.getPlayer(), F.main("Protection", "X-coordinate has been set."));
		}

	}

	@EventHandler
	public void setLocY(PlayerCommandPreprocessEvent event){
		if(event.getMessage().startsWith("/setlocy")){
			event.setCancelled(true);
			Block loc = event.getPlayer().getLocation().subtract(0, 1,0).getBlock();
			locY.remove(event.getPlayer());
			locY.put(event.getPlayer(), loc);
			UtilPlayer.message(event.getPlayer(), F.main("Protection", "Y-coordinate has been set."));
		}

	}
}
