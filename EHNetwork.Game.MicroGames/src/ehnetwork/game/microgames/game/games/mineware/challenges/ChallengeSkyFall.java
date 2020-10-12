package ehnetwork.game.microgames.game.games.mineware.challenges;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.mineware.Challenge;
import ehnetwork.game.microgames.game.games.mineware.MineWare;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class ChallengeSkyFall extends Challenge
{
	public ChallengeSkyFall(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Land on the bottom pad, knock players around with snowballs!");

	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> locations = new ArrayList<Location>();

		for (int x = -3; x <= 3; x++)
		{
			for (int z = -3; z <= 3; z++)
			{
				locations.add(getCenter().add(x + 0.5, 201.5, z + 0.5));
			}
		}

		return locations;
	}

	@Override
	public void cleanupRoom()
	{
		Host.DamageEvP = false;
	}

	@EventHandler
	public void onSnowballDamage(CustomDamageEvent event)
	{
		if (event.GetDamageePlayer() == null)
		{
			return;
		}

		if (!IsCompleted(event.GetDamageePlayer()))
		{
			return;
		}

		event.SetCancelled("Has completed already");
	}

	@Override
	public void setupPlayers()
	{
		setBorder(-50, 50, 0, 254, -50, 50);

		Host.DamageEvP = true;

		for (Player player : getChallengers())
		{
			player.getInventory().setItem(0, new ItemStack(Material.SNOW_BALL, 64));
		}
	}

	@EventHandler
	public void onTick(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		for (Player player : getChallengers())
		{
			if (IsCompleted(player))
				continue;

			double y = player.getLocation().getY();

			if (player.isOnGround() && y < 3)
			{
				SetCompleted(player);
			}
		}
	}

	@Override
	public void generateRoom()
	{
		for (int x = -3; x <= 3; x++)
		{
			for (int z = -3; z <= 3; z++)
			{
				Block block = getCenter().getBlock().getRelative(x, 200, z);
				block.setType(Material.STAINED_GLASS);
				addBlock(block);
			}
		}

		for (int x = -1; x <= 1; x++)
		{
			for (int z = -1; z <= 1; z++)
			{
				Block block = getCenter().getBlock().getRelative(x, 0, z);
				block.setType(Material.STAINED_CLAY);
				block.setData((byte) UtilMath.r(16));
				addBlock(block);
			}
		}

		for (int i = 0; i < 70; i++)
		{
			int y = UtilMath.r(160) + 10;

			if (UtilMath.r(y) > 50)
				continue;

			Block b = getCenter().getBlock().getRelative(UtilMath.r(16) - 8, y, UtilMath.r(16) - 8);

			b.setType(Material.CARPET);
			b.setData((byte) UtilMath.r(16));

			addBlock(b);
		}
	}
}
