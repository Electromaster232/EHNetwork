package ehnetwork.game.skyclans.clans;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.F;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.game.skyclans.shop.energy.EnergyShop;

public class ClanEnergyManager extends MiniPlugin implements Runnable
{
	private ClansManager _clansManager;
	private EnergyShop _shop;
	private int tickCount;

	public ClanEnergyManager(JavaPlugin plugin, ClansManager clansManager, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("Clan Energy", plugin);
		_clansManager = clansManager;
		_shop = new EnergyShop(clansManager, clientManager, donationManager);

		// Wait 5 seconds and then tick every 60 seconds
		_plugin.getServer().getScheduler().runTaskTimer(_plugin, this, 20 * 5, 20 * 60);
	}

	@Override
	public void run()
	{
		tickCount++;

		for (final ClanInfo clanInfo : _clansManager.getClanMap().values())
		{
			if (clanInfo.isAdmin())
				continue;

			int energyPerMinute = clanInfo.getEnergyCostPerMinute();
			int currentEnergy = clanInfo.getEnergy();

			if (currentEnergy < energyPerMinute)
			{
				for (String chunk : clanInfo.getClaimSet())
				{
					_clansManager.getClanDataAccess().unclaimSilent(chunk, true);
				}
				_clansManager.messageClan(clanInfo, F.main("Clans", "Your clan has ran out of energy. Land claims have been removed"));
			}
			else
			{
				clanInfo.adjustEnergy(-energyPerMinute);
				if (tickCount % 5 == 0 && energyPerMinute > 0)
				{
					runAsync(new Runnable()
					{
						@Override
						public void run()
						{
							_clansManager.getClanDataAccess().updateEnergy(clanInfo);
						}
					});
				}
			}
		}
	}

	public void openShop(Player player)
	{
		_shop.attemptShopOpen(player);
	}

//	@EventHandler
//	public void command(PlayerCommandPreprocessEvent event)
//	{
//		if (event.getMessage().startsWith("/energyshop"))
//		{
//			openShop(event.getPlayer());
//		}
//	}
}
