package ehnetwork.core.cosmetic.ui.page;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Items;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.cosmetic.CosmeticManager;
import ehnetwork.core.cosmetic.ui.CosmeticShop;
import ehnetwork.core.cosmetic.ui.button.CloseButton;
import ehnetwork.core.cosmetic.ui.button.SelectTagButton;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.pet.Pet;
import ehnetwork.core.pet.PetExtra;
import ehnetwork.core.pet.repository.token.PetChangeToken;
import ehnetwork.core.pet.repository.token.PetToken;
import ehnetwork.core.shop.page.ConfirmationPage;
import ehnetwork.core.shop.page.ShopPageBase;

public class PetTagPage extends ShopPageBase<CosmeticManager, CosmeticShop>
{
	private String _tagName = "Pet Tag";
	private Pet _pet;
	private boolean _petPurchase;
	
    public PetTagPage(CosmeticManager plugin, CosmeticShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player, Pet pet, boolean petPurchase)
    {
        super(plugin, shop, clientManager, donationManager, name, player, 3);
        
        _pet = pet;
        _petPurchase = petPurchase;
        
        buildPage();
        
        getPlayer().setLevel(5);
    }

	@Override
	protected void buildPage()
	{
		inventory.setItem(0, new ItemStack(Items.NAME_TAG));
		
		getButtonMap().put(0, new CloseButton());
		getButtonMap().put(1, new CloseButton());
		getButtonMap().put(2, new SelectTagButton(this));
	}
	
	@Override
	public void playerClosed()
	{
		super.playerClosed();
		
		getPlayer().setLevel(0);
	}

	public void SelectTag()
	{
		_tagName = ChatColor.stripColor(_tagName);
		_tagName = _tagName.replaceAll("[^A-Za-z0-9]", "");

		if (_tagName.length() > 16)
		{
			UtilPlayer.message(getPlayer(), F.main(getPlugin().getName(), ChatColor.RED + "Pet name cannot be longer than 16 characters."));
			playDenySound(getPlayer());
			
			getShop().openPageForPlayer(getPlayer(), new PetPage(getPlugin(), getShop(), getClientManager(), getDonationManager(), "Pets", getPlayer()));
			return;
		}
		
		PetExtra tag = new PetExtra("Rename " + _pet.GetName() + " to " + _tagName, Material.NAME_TAG, 100);
		
		_pet.setDisplayName(C.cGreen + "Purchase " + _tagName);
		
		getShop().openPageForPlayer(getPlayer(), new ConfirmationPage<CosmeticManager, CosmeticShop>(getPlugin(), getShop(), getClientManager(), getDonationManager(), new Runnable()
		{
			public void run()
			{
				PetChangeToken token = new PetChangeToken();
				token.Name = getPlayer().getName();
				token.PetType = _pet.GetPetType().toString();
				token.PetName = _tagName;

				PetToken petToken = new PetToken();
				petToken.PetType = token.PetType;

				if (_petPurchase)
				{
					getPlugin().getPetManager().GetRepository().AddPet(token);
					getPlugin().getPetManager().addPetOwnerToQueue(getPlayer().getName(), _pet.GetPetType());
				}
				else
				{
					getPlugin().getPetManager().GetRepository().UpdatePet(token);
					getPlugin().getPetManager().addRenamePetToQueue(getPlayer().getName(), token.PetName);
				}

				getPlugin().getPetManager().Get(getPlayer()).GetPets().put(_pet.GetPetType(), token.PetName);

				getPlugin().getInventoryManager().addItemToInventory(null, getPlayer(), "Pet", _pet.GetPetType().toString(), 1);
				getShop().openPageForPlayer(getPlayer(), new Menu(getPlugin(), getShop(), getClientManager(), getDonationManager(), getPlayer()));
			}
		}, null, _petPurchase ? _pet : tag, CurrencyType.Coins, getPlayer()));
	}

	public void SetTagName(String tagName)
	{
		_tagName = tagName;
	}
}
