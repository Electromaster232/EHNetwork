package mineplex.core.preferences;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniDbClientPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.donation.DonationManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.preferences.command.PreferencesCommand;
import mineplex.core.preferences.ui.PreferencesShop;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class PreferencesManager extends MiniDbClientPlugin<UserPreferences>
{
	private PreferencesRepository _repository;
	private PreferencesShop _shop;
	
	private NautHashMap<String, UserPreferences> _saveBuffer = new NautHashMap<String, UserPreferences>();
	
	public boolean GiveItem;
	
	public PreferencesManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("Preferences", plugin, clientManager);
		
		_repository = new PreferencesRepository(plugin);
		_shop = new PreferencesShop(this, clientManager, donationManager);
		
		addCommand(new PreferencesCommand(this));
	}
	
	@Override
	protected UserPreferences AddPlayer(String player)
	{
		return new UserPreferences();
	}

	public void savePreferences(Player caller)
	{
		_saveBuffer.put(caller.getUniqueId().toString(), Get(caller));
	}
	
	@EventHandler
	public void storeBuffer(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		final NautHashMap<String, UserPreferences> bufferCopy = new NautHashMap<String, UserPreferences>();
		
		for (Entry<String, UserPreferences> entry : _saveBuffer.entrySet())
		{
			bufferCopy.put(entry.getKey(), entry.getValue());
		}
		
		_saveBuffer.clear();
		
		getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				_repository.saveUserPreferences(bufferCopy);
			}
		});
	}
	
	@EventHandler
	public void givePlayerItem(PlayerJoinEvent event)
	{
		if (!GiveItem)
			return;
			
		event.getPlayer().getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.REDSTONE_COMPARATOR.getId(), (byte)0, 1, ChatColor.GREEN + "/prefs"));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerInteract(PlayerInteractEvent event)
	{
		if (!GiveItem)
			return;
		
		if (event.getItem() != null && event.getItem().getType() == Material.REDSTONE_COMPARATOR)
		{
			_shop.attemptShopOpen(event.getPlayer());
			
			event.setCancelled(true);
		}
	}

	public void openShop(Player caller)
	{
		_shop.attemptShopOpen(caller);
	}

	@Override
	public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
	{
		Set(playerName, _repository.loadClientInformation(resultSet));
	}

	@Override
	public String getQuery(int accountId, String uuid, String name)
	{
		return "SELECT games, visibility, showChat, friendChat, privateMessaging, partyRequests, invisibility, forcefield, showMacReports, ignoreVelocity, pendingFriendRequests, friendDisplayInventoryUI FROM accountPreferences WHERE accountPreferences.uuid = '" + uuid + "' LIMIT 1;";
	}
}
