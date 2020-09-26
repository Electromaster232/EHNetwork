package ehnetwork.game.microgames.game.games.mineware.challenges;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.games.mineware.Challenge;
import ehnetwork.game.microgames.game.games.mineware.MineWare;

public class ChallengeSpleef extends Challenge
{

	public ChallengeSpleef(MineWare host)
	{
		super(host, ChallengeType.LastStanding,
				"Destroy the blocks beneath other players!");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> spawns = new ArrayList<Location>();

		for (int x = -7; x <= 7; x++)
		{
			for (int z = -7; z <= 7; z++)
			{
				spawns.add(getCenter().clone().add(x + 0.5, 2, z + 0.5));
			}
		}

		return spawns;
	}

	@Override
	public void cleanupRoom()
	{
	}

	@Override
	public void setupPlayers()
	{
	}

	@EventHandler
	public void onBreak(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.LEFT_CLICK_BLOCK)
		{
			return;
		}

		if (UtilPlayer.isSpectator(event.getPlayer()))
		{
			return;
		}

		Block block = event.getClickedBlock();
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND,
				block.getTypeId());
		block.setType(Material.AIR);
	}

	@Override
	public void generateRoom()
	{
		for (int x = -15; x <= 15; x++)
		{
			for (int z = -15; z <= 15; z++)
			{
				Block block = getCenter().getBlock().getRelative(x, 0, z);
				block.setType(Material.LAVA);
				addBlock(block);

				if (Math.abs(x) <= 10 && Math.abs(z) <= 10)
				{
					Block b = block.getRelative(0, 5, 0);

					if (Math.abs(x) == 10 || Math.abs(z) == 10)
					{
						b.setType(Material.IRON_BLOCK);
					}
					else
					{
						b.setType(Material.WOOL);
						block.setData((byte) UtilMath.r(16));
					}

					addBlock(b);
				}
			}
		}
	}

}
