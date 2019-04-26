package mineplex.core.pet;

import mineplex.core.common.CurrencyType;
import mineplex.core.pet.repository.token.PetSalesToken;
import mineplex.core.shop.item.SalesPackageBase;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Pet extends SalesPackageBase
{
	private String _name;
	private EntityType _petType;
	
	public Pet(String name, EntityType petType, int cost)
	{
		super(name, Material.MONSTER_EGG, (byte)petType.getTypeId(), new String[] {});
		
		_name = name;
		_petType = petType;
		CurrencyCostMap.put(CurrencyType.Coins, cost);
		
		KnownPackage = false;
	}
	
	public EntityType GetPetType()
	{
		return _petType;
	}

	public void Update(PetSalesToken petToken)
	{
		_name = petToken.Name;
	}

	public String GetPetName()
	{
		return _name;
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{

	}
}
