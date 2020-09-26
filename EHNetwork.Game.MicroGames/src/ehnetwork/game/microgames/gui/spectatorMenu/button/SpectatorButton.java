package ehnetwork.game.microgames.gui.spectatorMenu.button;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.util.F;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;

/**
 * Created by shaun on 14-09-26.
 */
public class SpectatorButton implements IButton
{
	private MicroGamesManager _microGamesManager;
	private Player _player;
	private Player _target;

	public SpectatorButton(MicroGamesManager microGamesManager, Player player, Player target)
	{
		_microGamesManager = microGamesManager;
		_player = player;
		_target = target;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		// Make sure this player is still a spectator
		if (!((CraftPlayer) player).getHandle().spectating)
			return;

		if (_microGamesManager.IsAlive(_target))
		{
			_player.teleport(_target.getLocation().add(0, 1, 0));
		}
		else
		{
			_player.sendMessage(F.main("Spectate", F.name(_target.getName()) + " is no longer alive."));
		}
	}
}