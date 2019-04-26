package mineplex.core.pet;

import mineplex.core.common.CurrencyType;
import mineplex.core.pet.repository.token.PetExtraToken;
import mineplex.core.shop.item.SalesPackageBase;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PetExtra extends SalesPackageBase
{
	private String _name;
	private Material _material;
	
	public PetExtra(String name, Material material, int cost)
	{
		super(name, material, (byte)0, new String[] { });
		
		_name = name;
		_material = material;
		CurrencyCostMap.put(CurrencyType.Coins, cost);
		
		KnownPackage = false;
		OneTimePurchaseOnly = false;
	}
	
	public void Update(PetExtraToken token)
	{

	}

	public String GetName()
	{
		return _name;
	}

	public Material GetMaterial()
	{
		return _material;
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{
	}
}
