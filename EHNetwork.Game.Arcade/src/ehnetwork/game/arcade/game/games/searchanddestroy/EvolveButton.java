package ehnetwork.game.arcade.game.games.searchanddestroy;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.arcade.kit.Kit;

public class EvolveButton implements IButton
{
    private SearchAndDestroy _search;
    private Kit _kit;

    public EvolveButton(SearchAndDestroy arcadeManager, Kit kit)
    {
        _search = arcadeManager;
        _kit = kit;
    }

    @Override
    public void onClick(Player player, ClickType clickType)
    {
        // Make sure this player isn't a spectator
        if (((CraftPlayer) player).getHandle().spectating)
            return;

        _search.SetKit(player, _kit, true);
        _search.onEvolve(player);
        player.closeInventory();
    }
}