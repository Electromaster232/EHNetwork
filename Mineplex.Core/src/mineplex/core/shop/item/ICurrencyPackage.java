package mineplex.core.shop.item;

import mineplex.core.common.CurrencyType;
import mineplex.core.donation.repository.GameSalesPackageToken;

public interface ICurrencyPackage
{
	int GetSalesPackageId();
	int GetCost(CurrencyType currencytype);
	boolean IsFree();
	void Update(GameSalesPackageToken token);
}
