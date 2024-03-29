package ehnetwork.core.reward.rewards;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.pet.repository.token.PetChangeToken;
import ehnetwork.core.pet.repository.token.PetToken;
import ehnetwork.core.reward.RewardData;
import ehnetwork.core.reward.RewardRarity;

/**
 * Created by shaun on 14-09-18.
 */
public class PetReward extends UnknownPackageReward
{
	private InventoryManager _inventoryManager;
	private PetManager _petManager;
	private EntityType _petEntity;

	public PetReward(PetManager petManager, InventoryManager inventoryManager, DonationManager donationManager, String name, String packageName, EntityType petEntity, RewardRarity rarity, int weight)
	{
		super(donationManager, name, packageName, new ItemStack(Material.MONSTER_EGG, 1, petEntity.getTypeId()), rarity, weight);
		
		_petManager = petManager;
		_inventoryManager = inventoryManager;
		_petEntity = petEntity;
	}

	@Override
	protected RewardData giveRewardCustom(Player player)
	{
		PetChangeToken token = new PetChangeToken();
		token.Name = player.getName();
		token.PetType = _petEntity.toString();
		token.PetName = getPackageName();

		PetToken petToken = new PetToken();
		petToken.PetType = token.PetType;

		_petManager.GetRepository().AddPet(token);
//		_petManager.addPetOwnerToQueue(player.getName(), _petEntity);

		_petManager.Get(player).GetPets().put(_petEntity, token.PetName);

		_inventoryManager.addItemToInventory(player, "Pet", _petEntity.toString(), 1);

		return super.giveRewardCustom(player);
	}
}
