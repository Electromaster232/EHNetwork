package ehnetwork.core.shop.item;


import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.donation.repository.GameSalesPackageToken;

public abstract class SalesPackageBase implements ICurrencyPackage, IDisplayPackage
{
	private Material _displayMaterial;
	private byte _displayData;
	
	protected String Name;
	protected String DisplayName;
	protected String[] Description;
	protected int Quantity;
	
	protected int SalesPackageId;
	protected boolean Free;
	protected NautHashMap<CurrencyType, Integer> CurrencyCostMap;
	protected boolean KnownPackage = true;
	protected boolean OneTimePurchaseOnly = true;
	
	public SalesPackageBase(String name, Material material, String...description)
	{
		this(name, material, (byte)0, description);
	}
	
	public SalesPackageBase(String name, Material material, byte displayData, String[] description)
	{
		this(name, material, (byte)0, description, 0);
	}
	
	public SalesPackageBase(String name, Material material, byte displayData, String[] description, int coins)
	{
		this(name, material, (byte)0, description, coins, 1);
	}

	public SalesPackageBase(String name, Material material, byte displayData, String[] description, int coins, int quantity)
	{
		CurrencyCostMap = new NautHashMap<CurrencyType, Integer>();
		
		Name = name;
		DisplayName = name;
		Description = description;
		_displayMaterial = material;
		_displayData = displayData;
		
		CurrencyCostMap.put(CurrencyType.Coins, coins);
		Quantity = quantity;
	}
	
	public abstract void Sold(Player player, CurrencyType currencyType);
	
	@Override
	public String GetName()
	{
		return Name;
	}
	
	@Override
	public String[] GetDescription()
	{
		return Description;
	}
	
	@Override
	public int GetCost(CurrencyType currencyType)
	{		
		return CurrencyCostMap.containsKey(currencyType) ? CurrencyCostMap.get(currencyType) : 0;
	}

	@Override
	public int GetSalesPackageId()
	{
		return SalesPackageId;
	}

	@Override
	public boolean IsFree()
	{
		return Free;
	}
	
	@Override
	public Material GetDisplayMaterial()
	{
		return _displayMaterial;
	}
	
	@Override
	public byte GetDisplayData()
	{
		return _displayData;
	}
	
	@Override
	public void Update(GameSalesPackageToken token)
	{
		SalesPackageId = token.GameSalesPackageId;
		Free = token.Free;
		
		if (token.Gems > 0)
		{
			CurrencyCostMap.put(CurrencyType.Gems, token.Gems);
		}
	}
	
	public int getQuantity()
	{
		return Quantity;
	}

	public boolean IsKnown()
	{
		return KnownPackage;
	}

	public boolean OneTimePurchase()
	{
		return OneTimePurchaseOnly;
	}

	public String GetDisplayName()
	{
		return DisplayName;
	}
	
	public void setDisplayName(String name)
	{
		DisplayName = name;
	}
}
