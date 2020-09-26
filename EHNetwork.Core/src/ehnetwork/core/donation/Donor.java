package ehnetwork.core.donation;

import java.util.ArrayList;
import java.util.List;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.repository.token.CoinTransactionToken;
import ehnetwork.core.donation.repository.token.DonorToken;
import ehnetwork.core.donation.repository.token.TransactionToken;

public class Donor
{
    public int _gems;
    private int _coins;
    private int _gold;
    private boolean _donated;
    private List<Integer> _salesPackagesOwned = new ArrayList<Integer>();
    private List<String> _unknownSalesPackagesOwned = new ArrayList<String>();
    private List<TransactionToken> _transactions = new ArrayList<TransactionToken>();
    private List<CoinTransactionToken> _coinTransactions = new ArrayList<CoinTransactionToken>();
    
    private boolean _update = true;
	
	public Donor() { }
	
	public void loadToken(DonorToken token)
	{
	    _gems = token.Gems;
	    _coins = token.Coins;
	    _donated = token.Donated;
	    
	    _salesPackagesOwned = token.SalesPackages;
	    _unknownSalesPackagesOwned = token.UnknownSalesPackages;
	    _transactions = token.Transactions;
	    _coinTransactions = token.CoinRewards;
	}
	
    public int GetGems()
    {
        return _gems;
    }
    
    public List<Integer> GetSalesPackagesOwned()
    {
        return _salesPackagesOwned;
    }
    
    public List<String> GetUnknownSalesPackagesOwned()
    {
        return _unknownSalesPackagesOwned;
    }

    public boolean Owns(Integer salesPackageId)
    {
        return salesPackageId == -1 || _salesPackagesOwned.contains(salesPackageId);
    }

	public void AddSalesPackagesOwned(int salesPackageId) 
	{
		_salesPackagesOwned.add(salesPackageId);
	}

	public boolean HasDonated() 
	{
		return _donated;
	}
    
    public void DeductCost(int cost, CurrencyType currencyType)
    {
    	switch (currencyType)
    	{
			case Gems:
				_gems -= cost;
				_update = true;
				break;
			case Coins:
				_coins -= cost;
				_update = true;
				break;
			default:
				break;
    	}
    }
	
	public int GetBalance(CurrencyType currencyType)
	{
    	switch (currencyType)
    	{
			case Gems:
				return _gems;
			case Coins:
				return _coins;
			case Tokens:
				return 0;
			default:
				return 0;
    	}
	}

	public void AddGems(int gems)
	{
		_gems += gems;
	}
	
	public boolean OwnsUnknownPackage(String packageName)
	{
		return _unknownSalesPackagesOwned.contains(packageName);
	}
	
	public boolean Updated()
	{
		return _update;
	}

	public void AddUnknownSalesPackagesOwned(String packageName)
	{
		_unknownSalesPackagesOwned.add(packageName);
	}
	
	public List<TransactionToken> getTransactions()
	{
		return _transactions;
	}

	public boolean OwnsUltraPackage()
	{
		for (String packageName : _unknownSalesPackagesOwned)
		{
			if (packageName.contains("ULTRA"))
				return true;
		}
		
		return false;
	}

	public int getCoins()
	{
		return _coins;
	}

	public void addCoins(int amount)
	{
		_coins += amount;
	}
	
	public void addGold(int amount)
	{
		_gold += amount;
	}

	public List<CoinTransactionToken> getCoinTransactions()
	{
		return _coinTransactions;
	}

	public int getGold()
	{
		return _gold;
	}
}
