package ehnetwork.core.shop.item;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.repository.GameSalesPackageToken;

public interface ICurrencyPackage
{
	int GetSalesPackageId();
	int GetCost(CurrencyType currencytype);
	boolean IsFree();
	void Update(GameSalesPackageToken token);
}
