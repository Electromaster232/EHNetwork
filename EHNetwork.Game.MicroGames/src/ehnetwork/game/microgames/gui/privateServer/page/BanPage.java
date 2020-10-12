package ehnetwork.game.microgames.gui.privateServer.page;

import org.bukkit.entity.Player;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;

public class BanPage extends PlayerPage
{
	public BanPage(MicroGamesManager plugin, PrivateServerShop shop, Player player)
	{
		super(plugin, shop, "Remove Players", player);
		buildPage();
	}

	@Override
	public boolean showPlayer(Player player)
	{
		return !_manager.isAdmin(player, true);
	}

	@Override
	public void clicked(int slot, Player player)
	{
		removeButton(slot);
		_manager.ban(player);
		UtilPlayer.message(getPlayer(), F.main("Server", "You have removed " + F.name(player.getName()) + " from this private server."));
	}

	@Override
	public String getDisplayString(Player player)
	{
		return "Click to Remove Player";
	}
}
