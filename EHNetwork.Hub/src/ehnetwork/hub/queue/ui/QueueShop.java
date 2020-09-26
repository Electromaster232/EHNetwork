package ehnetwork.hub.queue.ui;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.party.Party;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.hub.queue.QueueManager;

public class QueueShop extends ShopBase<QueueManager>
{
	public QueueShop(QueueManager plugin, CoreClientManager clientManager, DonationManager donationManager, String name)
	{
		super(plugin, clientManager, donationManager, name);
	}

	@Override
	protected ShopPageBase<QueueManager, ? extends ShopBase<QueueManager>> buildPagesFor(Player player)
	{
		return new QueuePage(getPlugin(), this, getClientManager(), getDonationManager(), "          " + ChatColor.UNDERLINE + "Queuer 9001", player);
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
	
	@EventHandler
	public void UpdatePages(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		for (Iterator<ShopPageBase<QueueManager, ? extends ShopBase<QueueManager>>> iterator = getPlayerPageMap().values().iterator(); iterator.hasNext();)
		{
			ShopPageBase<QueueManager, ? extends ShopBase<QueueManager>> page = iterator.next();
			
			if (page instanceof QueuePage)
			{
				((QueuePage)page).Update();
			}
		}
	}
}
