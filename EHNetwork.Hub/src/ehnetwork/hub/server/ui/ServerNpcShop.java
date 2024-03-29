package ehnetwork.hub.server.ui;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.party.Party;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.hub.server.ServerManager;

public class ServerNpcShop extends ShopBase<ServerManager>
{	
	public ServerNpcShop(ServerManager plugin, CoreClientManager clientManager, DonationManager donationManager, String name)
	{
		super(plugin, clientManager, donationManager, name);
	}

	@Override
	protected ShopPageBase<ServerManager, ? extends ShopBase<ServerManager>> buildPagesFor(Player player)
	{
		return new ServerNpcPage(getPlugin(), this, getClientManager(), getDonationManager(), getName(), player, getName());
	}

	@Override
	protected boolean canOpenShop(Player player)
	{
		Party party = getPlugin().getPartyManager().GetParty(player);
		
		if (party != null && !player.getName().equalsIgnoreCase(party.GetLeader()))
		{
			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, .6f);
			player.sendMessage(F.main("Party", "Only Party Leaders can join games."));
			player.sendMessage(F.main("Party", "Type " + C.cGreen + "/party leave" + C.cGray + " if you wish to leave your party."));
			return false;
		}
		
		return true;
	}
	
	public void UpdatePages()
	{
		for (ShopPageBase<ServerManager, ? extends ShopBase<ServerManager>> page : getPlayerPageMap().values())
		{
			if (page instanceof ServerNpcPage)
			{
				((ServerNpcPage)page).Update();
			}
			else if (page instanceof ServerGameMenu)
			{
				((ServerGameMenu)page).Update();
			}
		}
	}
	
	protected void openShopForPlayer(Player player)
	{ 
		getPlugin().getHubManager().GetVisibility().addHiddenPlayer(player);
	}
	
	protected void closeShopForPlayer(Player player)
	{ 
		getPlugin().getHubManager().GetVisibility().removeHiddenPlayer(player);
	}
}
