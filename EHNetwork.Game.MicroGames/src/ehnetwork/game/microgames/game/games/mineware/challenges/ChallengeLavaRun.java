package ehnetwork.game.microgames.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseMagmaCube;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.mineware.Challenge;
import ehnetwork.game.microgames.game.games.mineware.MineWare;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class ChallengeLavaRun extends Challenge
{
	private long _delay;
	private long _minusDelay = 1000;
	private int _disappearingBlocks = 10;
	private Location _obsidian;

	public ChallengeLavaRun(MineWare host)
	{
		super(host, ChallengeType.LastStanding, "The lava is coming! Stand on the obsidian!");
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

	@EventHandler
	public void onDamage(CustomDamageEvent event)
	{
		if (event.GetDamagerEntity(true) != null)
		{
			return;
		}

		event.AddMod("Ensure Death", null, 9999, false);
	}

	@EventHandler
	public void onTick(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		if (_delay > System.currentTimeMillis())
			return;

		if (_obsidian == null)
		{
			_obsidian = getCenter().add(UtilMath.r(21) - 9.5, 1, UtilMath.r(21) - 9.5);

			for (int x = -10; x <= 10; x++)
			{
				for (int z = -10; z <= 10; z++)
				{
					Block b = getCenter().getBlock().getRelative(x, 1, z);

					b.setType(Material.GLASS);
					addBlock(b);
				}
			}

			_obsidian.getBlock().setType(Material.OBSIDIAN);

			for (Player player : UtilServer.getPlayers())
				player.playSound(player.getLocation(), Sound.NOTE_PIANO, 2f, 0f);

			_delay = System.currentTimeMillis() + _minusDelay;
			_minusDelay -= 100;

			_disappearingBlocks++;
		}
		else
		{
			ArrayList<Block> glassBlocks = new ArrayList<Block>();

			for (int x = -10; x <= 10; x++)
			{
				for (int z = -10; z <= 10; z++)
				{
					Block b = getCenter().getBlock().getRelative(x, 1, z);

					if (b.getType() == Material.GLASS)
					{
						glassBlocks.add(b);
					}
				}
			}

			if (glassBlocks.isEmpty())
			{
				_delay = System.currentTimeMillis() + 1500;
				_obsidian = null;
			}
			else
			{
				final HashMap<Block, Double> distance = new HashMap<Block, Double>();

				for (Block b : glassBlocks)
				{
					distance.put(b, b.getLocation().add(0.5, 0, 0.5).distance(_obsidian));
				}

				Collections.sort(glassBlocks, new Comparator<Block>()
				{

					@Override
					public int compare(Block o1, Block o2)
					{
						return distance.get(o2).compareTo(distance.get(o1));
					}
				});

				for (int i = 0; i < Math.min(_disappearingBlocks, glassBlocks.size()); i++)
				{
					Block b = glassBlocks.remove(0);
					b.setTypeIdAndData(Material.AIR.getId(), (byte) 0, false);
					addBlock(b);
				}
			}
		}
	}

	@Override
	public void cleanupRoom()
	{
	}

	@Override
	public void setupPlayers()
	{
		_delay = System.currentTimeMillis() + 2000;

		for (Player player : getChallengers())
		{
			DisguiseMagmaCube disguise = new DisguiseMagmaCube(player);
			disguise.SetSize(2);
			Host.getArcadeManager().GetDisguise().disguise(disguise);
		}
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
					Block b = block.getRelative(BlockFace.UP);
					b.setType(Material.GLASS);
					addBlock(b);
				}
			}
		}

		_obsidian = getCenter().add(UtilMath.r(21) - 9.5, 1, UtilMath.r(21) - 9.5);

		_obsidian.getBlock().setType(Material.OBSIDIAN);
	}

}
