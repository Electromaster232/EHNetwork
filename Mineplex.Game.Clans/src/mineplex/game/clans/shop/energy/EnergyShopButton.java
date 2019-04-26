package mineplex.game.clans.shop.energy;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.common.util.Callback;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.item.IButton;
import mineplex.game.clans.clans.ClanInfo;
import mineplex.game.clans.clans.ClansManager;

public class EnergyShopButton implements IButton
{
	private Player _player;
	private ClansManager _clansManager;
	private EnergyPage _page;
	private ClanInfo _clanInfo;
	private boolean _canPurchaseEnergy;
	private DonationManager _donationManager;

	public EnergyShopButton(Player player, ClansManager clansManager, EnergyPage page, ClanInfo clanInfo, boolean canPurchaseEnergy, DonationManager donationManager)
	{
		_player = player;
		_clansManager = clansManager;
		_clanInfo = clanInfo;
		_canPurchaseEnergy = canPurchaseEnergy;
		_donationManager = donationManager;
		_page = page;
	}


	@Override
	public void onClick(final Player player, ClickType clickType)
	{
		if (_page.hasEnoughGold())
		{
			int playerGold = _page.getPlayerGold();
			final int energy = _page.getEnergyPerHour();
			int goldCost = _page.getGoldPerHour();

			_page.clearPage();
			_page.showLoading();
			_donationManager.RewardGold(new Callback<Boolean>()
			{
				@Override
				public void run(Boolean data)
				{
					player.playSound(player.getEyeLocation(), Sound.ANVIL_USE, 1, 1);
					_clanInfo.adjustEnergy(energy);
					_page.refresh();
				}
			}, "Energy Purchase", player.getName(), _page.getClientManager().Get(player).getAccountId(), -goldCost, true);
		}
	}

}
