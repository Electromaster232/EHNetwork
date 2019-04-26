package nautilus.game.arcade.gui.privateServer.page;

import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.gui.privateServer.PrivateServerShop;

/**
 * Created by WilliamTiger.
 * All the code and any API's associated with it
 * are not to be used anywhere else without written
 * consent of William Burns. 2015.
 * 29/07/15
 */
public class WhitelistedPage extends PlayerPage
{
	public WhitelistedPage(ArcadeManager plugin, PrivateServerShop shop, Player player)
	{
		super(plugin, shop, "Whitelisted Players", player);
		buildPage();
	}

	@Override
	public boolean showPlayer(Player player)
	{
		if (getPlugin().GetGameHostManager().getWhitelist().contains(player.getName()))
			return true;
		return false;
	}

	@Override
	public void clicked(int slot, Player player)
	{
		getPlugin().GetGameHostManager().getWhitelist().remove(player.getName());
		getPlugin().GetPortal().sendToHub(player, "You are no longer whitelisted.");
		getPlayer().sendMessage(F.main("Whitelist", "§e" + player.getName() + " §7is no longer whitelisted."));
	}

	@Override
	public String getDisplayString(Player player)
	{
		return "Click to remove from whitelist";
	}
}
