package mineplex.hub.queue.ui;

import java.util.Iterator;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.party.Party;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.queue.QueueManager;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class QueueShop extends ShopBase<QueueManager>
{
	public QueueShop(QueueManager plugin, CoreClientManager clientManager,	mineplex.core.donation.DonationManager donationManager,	String name)
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
