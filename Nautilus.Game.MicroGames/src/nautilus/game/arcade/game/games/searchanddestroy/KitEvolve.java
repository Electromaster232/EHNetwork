package nautilus.game.arcade.game.games.searchanddestroy;

import java.util.ArrayList;

import mineplex.core.MiniPlugin;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.games.searchanddestroy.KitManager.UpgradeKit;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KitEvolve extends MiniPlugin
{

    private SearchAndDestroy _arcadeManager;
    private KitEvolveShop _kitEvolve;

    public KitEvolve(JavaPlugin plugin, SearchAndDestroy manager, ArrayList<UpgradeKit> kits)
    {
        super("SnD Kit Evolving", plugin);
        _arcadeManager = manager;
        _kitEvolve = new KitEvolveShop(this, manager, manager.getArcadeManager().GetClients(), manager.getArcadeManager()
                .GetDonation(), kits);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (_arcadeManager.GetState() == GameState.Live && event.getAction().name().contains("RIGHT"))
        {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.LEATHER_CHESTPLATE && item.hasItemMeta()
                    && item.getItemMeta().hasDisplayName())
            {
                _kitEvolve.attemptShopOpen(event.getPlayer());
            }
        }
    }

}
