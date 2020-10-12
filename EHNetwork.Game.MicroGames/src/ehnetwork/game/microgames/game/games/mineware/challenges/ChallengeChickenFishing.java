package ehnetwork.game.microgames.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilShapes;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.mineware.Challenge;
import ehnetwork.game.microgames.game.games.mineware.MineWare;

public class ChallengeChickenFishing extends Challenge
{
	private ArrayList<Entity> _chickens = new ArrayList<Entity>();
	private ArrayList<Location> _spawns = new ArrayList<Location>();
	private ArrayList<Location> _chickenSpawns = new ArrayList<Location>();

	public ChallengeChickenFishing(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Chicken Fishing");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		return _spawns;
	}

	@EventHandler
	public void onSecond(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
		{
			return;
		}

		Iterator<Entity> itel = _chickens.iterator();

		while (itel.hasNext())
		{
			Entity ent = itel.next();

			// TODO Validate chicken is caught

			if (!ent.isValid())
			{
				itel.remove();
			}
		}

		for (Player player : getChallengers())
		{
			Block block = player.getLocation().getBlock();

			if (block.isLiquid())
			{
				setLost(player);
			}
		}
	}

	@Override
	public void cleanupRoom()
	{
		for (Entity chicken : _chickens)
		{
			chicken.remove();
		}
	}

	@Override
	public void setupPlayers()
	{
		for (Player player : getChallengers())
		{
			player.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
		}
	}

	@Override
	public void generateRoom()
	{
		int size = (getChallengers().size() / 2) + 4;

		for (Location location : UtilShapes.getCircle(getCenter(), true, size))
		{
			Block block = location.getBlock();

			for (int y = 0; y <= 7; y++)
			{
				Block b = block.getRelative(0, y, 0);

				if (y < 3 || (y < 5 && UtilMath.random.nextBoolean()))
				{
					b.setType(Material.STONE);
				}
				else if (y != 7)
				{
					b.setType(Material.DIRT);
				}
				else
				{
					b.setType(Material.GRASS);
				}
			}

			_spawns.add(location.clone().add(0.5, 7.1, 0.5));
		}

		for (Location location : UtilShapes.getCircle(getCenter(), false,
				size - 1))
		{
			_chickenSpawns.add(location.add(0.5, 0.5, 0.5));
		}
	}
}
