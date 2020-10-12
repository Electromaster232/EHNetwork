package ehnetwork.core.shop.item;

import java.util.List;

import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.IInventory;

import ehnetwork.core.account.CoreClient;

public interface ISalesPackage
{
	String GetName();
    int GetGemCost();
    boolean CanFitIn(CoreClient player);
    List<Integer> AddToCategory(IInventory inventory, int slot);
    void DeliverTo(Player player);
    void PurchaseBy(CoreClient player);
    int ReturnFrom(CoreClient player);
    void DeliverTo(Player player, int slot);
    int GetSalesPackageId();
	boolean IsFree();
}
