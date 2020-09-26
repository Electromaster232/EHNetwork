package ehnetwork.minecraft.game.classcombat.shop;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.minecraft.game.classcombat.Class.ClientClass;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Class.repository.token.CustomBuildToken;
import ehnetwork.minecraft.game.classcombat.shop.page.CustomBuildPage;

public class ClassCombatShop extends ShopBase<ClassShopManager>
{
	private NautHashMap<String, ItemStack[]> _playerInventoryMap = new NautHashMap<String, ItemStack[]>();
	private NautHashMap<String, ItemStack[]> _playerArmorMap = new NautHashMap<String, ItemStack[]>();
	private IPvpClass _gameClass;
	private boolean _takeAwayStuff;
	private boolean _skillsOnly;
	
	public ClassCombatShop(ClassShopManager plugin, CoreClientManager clientManager, DonationManager donationManager, boolean skillsOnly, String name)
	{
		super(plugin, clientManager, donationManager, name, CurrencyType.Gems);
		
		_skillsOnly = skillsOnly;
	}
	
	public ClassCombatShop(ClassShopManager plugin, CoreClientManager clientManager, DonationManager donationManager, boolean skillsOnly, String name, IPvpClass iPvpClass)
	{
		super(plugin, clientManager, donationManager, name, CurrencyType.Gems);
		_gameClass = iPvpClass;
		_takeAwayStuff = true;
		_skillsOnly = skillsOnly;
	}
	
	protected void openShopForPlayer(Player player)
	{ 
		if (_gameClass != null)
			getPlugin().GetClassManager().Get(player).SetGameClass(_gameClass);
		
		if (_takeAwayStuff)
		{
			_playerInventoryMap.put(player.getName(), player.getInventory().getContents());
			_playerArmorMap.put(player.getName(), player.getInventory().getArmorContents());
		}
	}
	
    public boolean attemptShopOpen(Player player)
    {
		if (!getOpenedShop().contains(player.getName()))
		{
			if (!canOpenShop(player))
				return false;

			ShopPageBase<ClassShopManager, ClassCombatShop> page = null;
			
    		if (getPlugin().GetClassManager().Get(player).GetGameClass() == null)
    		{
    			// page = new ArmorPage(_plugin, this, _clientManager, _donationManager, player);
    			UtilPlayer.message(player, F.main(getPlugin().getName(), ChatColor.RED + "You need to have an armor set on to modify class builds."));
    			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, .6f);
    			return false;
    		}
    		else
    			page = new CustomBuildPage(getPlugin(), this, getClientManager(), getDonationManager(), player);
			
			getOpenedShop().add(player.getName());
    		openShopForPlayer(player);

    		if (!getPlayerPageMap().containsKey(player.getName()))
    		{
    			getPlayerPageMap().put(player.getName(), page);
    		}
			
			openPageForPlayer(player, page);
    		
    		return true;
		}
		
		return false;
    }
	
	@Override
	protected void closeShopForPlayer(Player player)
	{
		ClientClass clientClass = getPlugin().GetClassManager().Get(player);
		
		if (clientClass != null && clientClass.IsSavingCustomBuild())
		{
			CustomBuildToken customBuild = clientClass.GetSavingCustomBuild();
			clientClass.SaveActiveCustomBuild();
			clientClass.SetActiveCustomBuild(clientClass.GetGameClass(), customBuild);
			clientClass.EquipCustomBuild(customBuild, false, _skillsOnly);
		}
		
		if (_takeAwayStuff)
		{
			player.getInventory().clear();
			
			player.getInventory().setContents(_playerInventoryMap.remove(player.getName()));
			player.getInventory().setArmorContents(_playerArmorMap.remove(player.getName()));
			
			((CraftPlayer)player).getHandle().updateInventory(((CraftPlayer)player).getHandle().defaultContainer);
		}
	}

	@Override
	protected ShopPageBase<ClassShopManager, ? extends ShopBase<ClassShopManager>> buildPagesFor(Player player)
	{
		return new CustomBuildPage(getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
	
	@EventHandler
	public void clearPlayerFromMaps(PlayerQuitEvent event)
	{
		_playerInventoryMap.remove(event.getPlayer().getName());
		_playerArmorMap.remove(event.getPlayer().getName());
	}
	
	public boolean skillOnly()
	{
		return _skillsOnly;
	}
}
