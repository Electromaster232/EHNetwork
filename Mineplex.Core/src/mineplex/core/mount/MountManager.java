package mineplex.core.mount;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.disguise.DisguiseManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.mount.types.*;

public class MountManager extends MiniPlugin
{
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private BlockRestore _blockRestore;
	private DisguiseManager _disguiseManager;
	
	private List<Mount<?>> _types;
	private NautHashMap<Player, Mount<?>> _playerActiveMountMap = new NautHashMap<Player, Mount<?>>();
	
	public MountManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, BlockRestore blockRestore, DisguiseManager disguiseManager)
	{
		super("Mount Manager", plugin);
		
		_clientManager = clientManager;
		_donationManager = donationManager;
		_blockRestore = blockRestore;
		_disguiseManager = disguiseManager;
		
		CreateGadgets();
	}

	private void CreateGadgets() 
	{
		_types = new ArrayList<Mount<?>>();
		
		_types.add(new MountUndead(this));
		_types.add(new MountFrost(this));
		_types.add(new MountMule(this));
		_types.add(new MountDragon(this));
		_types.add(new MountSlime(this));
		_types.add(new MountCart(this));
		//_types.add(new MountSheep(this));
	}

	public List<Mount<?>> getMounts()
	{
		return _types;
	}
	
	//Disallows two mounts active
	public void DeregisterAll(Player player) 
	{
		for (Mount<?> mount : _types)
			mount.Disable(player);
	}
	
	@EventHandler
	public void HorseInteract(PlayerInteractEntityEvent event)
	{
		if (!(event.getRightClicked() instanceof Horse))
			return;
		
		boolean found = false;
		for (Mount mount : _playerActiveMountMap.values())
		{
			if (mount.GetActive().containsValue(event.getRightClicked()))
			{
				found = true;
				break;
			}
		}
		
		if (!found)
			return;

		Player player = event.getPlayer();
		Horse horse = (Horse)event.getRightClicked();
		
		if (horse.getOwner() == null || !horse.getOwner().equals(player))
		{
			UtilPlayer.message(player, F.main("Mount", "This is not your Mount!"));
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void LeashDropCancel(ItemSpawnEvent event)
	{
		if (event.getEntity().getItemStack().getType() == Material.LEASH)
			event.setCancelled(true);
	}

	public void DisableAll()
	{
		for (Mount<?> mount : _types)
			for (Player player : UtilServer.getPlayers())
				mount.Disable(player);
	}

	public void DisableAll(Player player){
		for (Mount<?> mount : _types)
				mount.Disable(player);
	}

	public void EnableAll(Player player){
		for (Mount<?> mount : _types)
			mount.Enable(player);
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		_playerActiveMountMap.remove(event.getPlayer());
	}
	
	@EventHandler
	public void death(PlayerDeathEvent event)
	{
		_playerActiveMountMap.remove(event.getEntity());
	}
	
	public void setActive(Player player, Mount<?> mount)
	{
		_playerActiveMountMap.put(player, mount);
	}
	
	public Mount<?> getActive(Player player)
	{
		return _playerActiveMountMap.get(player);
	}
	
	public void removeActive(Player player)
	{
		_playerActiveMountMap.remove(player);
	}

	public CoreClientManager getClientManager()
	{
		return _clientManager;
	}
	
	public DonationManager getDonationManager()
	{
		return _donationManager;
	}

	public BlockRestore getBlockRestore()
	{
		return _blockRestore;
	}
	
	public DisguiseManager getDisguiseManager()
	{
		return _disguiseManager;
	}
}
