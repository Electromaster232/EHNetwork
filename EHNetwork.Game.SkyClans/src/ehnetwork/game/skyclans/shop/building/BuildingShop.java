package ehnetwork.game.skyclans.shop.building;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.skyclans.clans.ClansManager;

public class BuildingShop extends ShopBase<ClansManager>
{
	public BuildingShop(ClansManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Building Materials");
	}

	@Override
	protected ShopPageBase<ClansManager, ? extends ShopBase<ClansManager>> buildPagesFor(Player player)
	{
		return new BuildingPage(getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
	
	@EventHandler
	public void playerCmd(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().startsWith("/gold"))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Gold", "Your Balance is " + C.cYellow + getDonationManager().Get(event.getPlayer().getName()).getGold() + "g"));
			event.setCancelled(true);
		}
	}
}
