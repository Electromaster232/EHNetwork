package nautilus.game.arcade.game.games.wizards;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;

import org.bukkit.entity.Player;

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
