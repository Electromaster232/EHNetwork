package mineplex.mapparser.command;

import org.bukkit.World;
import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.mapparser.MapData;
import mineplex.mapparser.MapParser;

/**
 * Created by Shaun on 8/16/2014.
 */
public class AdminCommand extends BaseCommand
{
	public AdminCommand(MapParser plugin)
	{
		super(plugin, "admin");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (args.length != 1)
		{
			message(player, "Invalid Input. " + F.elem("/admin <Name>"));
			return true;
		}

		World world = player.getWorld();

		if (world.getName().equals("world"))
		{
			message(player, "Cannot change Admin-List for Lobby.");
			return true;
		}

		//Permission
		if (!getPlugin().GetData(world.getName()).HasAccess(player))
		{
			message(player, "You are not on Admin-List for this Map.");
			return true;
		}

		Player other = UtilPlayer.searchOnline(player, args[0], true);

		if (player != null)
		{
			MapData data = getPlugin().GetData(world.getName());

			if (data.AdminList.contains(other.getName()))
			{
				data.AdminList.remove(other.getName());
				data.Write();

				getPlugin().Announce("Admin-List for " + F.elem(world.getName()) + "  (" + other.getName() + " = " + F.tf(false) + ")");
			}
			else
			{
				data.AdminList.add(other.getName());
				data.Write();

				getPlugin().Announce("Admin-List for " + F.elem(world.getName()) + "  (" + other.getName() + " = " + F.tf(true) + ")");
			}
		}
		return true;
	}
}
