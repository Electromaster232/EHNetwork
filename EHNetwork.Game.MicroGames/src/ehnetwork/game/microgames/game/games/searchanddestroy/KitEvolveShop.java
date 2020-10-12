package ehnetwork.game.microgames.game.games.searchanddestroy;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;

public class KitEvolveShop extends ShopBase<KitEvolve>
{

    private SearchAndDestroy _arcadeManager;
    private ArrayList<KitManager.UpgradeKit> _kits;

    public KitEvolveShop(KitEvolve plugin, SearchAndDestroy arcadeManager, CoreClientManager clientManager,
						 DonationManager donationManager, ArrayList<KitManager.UpgradeKit> kits, CurrencyType... currencyTypes)
    {
        super(plugin, clientManager, donationManager, "Kit Evolve Menu", currencyTypes);
        _arcadeManager = arcadeManager;
        _kits = kits;
    }

    @Override
    protected ShopPageBase<KitEvolve, ? extends ShopBase<KitEvolve>> buildPagesFor(Player player)
    {
        return new KitEvolvePage(getPlugin(), _arcadeManager, this, getClientManager(), getDonationManager(), player, _kits);
    }

    public void update()
    {
        for (ShopPageBase<KitEvolve, ? extends ShopBase<KitEvolve>> shopPage : getPlayerPageMap().values())
        {
            shopPage.refresh();
        }
    }
}
