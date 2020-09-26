package ehnetwork.game.arcade.game.games.wizards;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;

public class WizardSpellMenuShop extends ShopBase<WizardSpellMenu>
{
	private Wizards _wizards;

	public WizardSpellMenuShop(WizardSpellMenu plugin, CoreClientManager clientManager, DonationManager donationManager,
			Wizards wizard, CurrencyType... currencyTypes)
	{
		super(plugin, clientManager, donationManager, "Kit Evolve Menu", currencyTypes);
		_wizards = wizard;
	}

	@Override
	protected ShopPageBase<WizardSpellMenu, ? extends ShopBase<WizardSpellMenu>> buildPagesFor(Player player)
	{
		return new SpellMenuPage(getPlugin(), this, getClientManager(), getDonationManager(), player, _wizards);
	}

	public void update()
	{
		for (ShopPageBase<WizardSpellMenu, ? extends ShopBase<WizardSpellMenu>> shopPage : getPlayerPageMap().values())
		{
			shopPage.refresh();
		}
	}
}
