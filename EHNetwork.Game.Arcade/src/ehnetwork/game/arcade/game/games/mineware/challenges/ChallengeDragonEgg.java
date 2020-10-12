package ehnetwork.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.arcade.game.games.mineware.Challenge;
import ehnetwork.game.arcade.game.games.mineware.MineWare;

public class ChallengeDragonEgg extends Challenge
{
	private HashMap<String, Integer> _smashedEggs = new HashMap<String, Integer>();
	private ArrayList<Entity> _dragonEggs = new ArrayList<Entity>();

	public ChallengeDragonEgg(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Whack a dragon egg 10 times");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> spawns = new ArrayList<Location>();

		for (int x = -14; x < 15; x++)
		{
			for (int z = -14; z < 15; z++)
			{
				if (x % 2 == 0 && z % 2 == 0)
				{
					spawns.add(getCenter().clone().add(x, 1.1, z));
				}
			}
		}

		return spawns;
	}

	@EventHandler
	public void onBlockHit(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.LEFT_CLICK_BLOCK
				&& event.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}

		if (UtilPlayer.isSpectator(event.getPlayer()))
		{
			return;
		}

		Block block = event.getClickedBlock();

		if (block.getType() != Material.DRAGON_EGG)
		{
			return;
		}

		event.setCancelled(true);

		block.setType(Material.AIR);

		UtilParticle.PlayParticle(ParticleType.PORTAL,
				block.getLocation().add(0.5, 0.5, 0.5), 0.5F, 0.5F, 0.5F, 0,
				11, ViewDist.MAX, UtilServer.getPlayers());

		Host.CreatureAllowOverride = true;

		for (int i = 0; i < 10; i++)
		{
			Block b = getCenter().clone()
					.add(UtilMath.r(30) - 15, 1, UtilMath.r(30) - 15)
					.getBlock();

			if (b.getType() == Material.AIR)
			{
				Entity entity = getCenter().getWorld().spawnFallingBlock(
						b.getLocation().add(0.5, 2, 0.5), Material.DRAGON_EGG,
						(byte) 0);

				_dragonEggs.add(entity);

				for (int y = 0; y <= 2; y++)
				{
					addBlock(b.getRelative(0, y, 0));
				}

				break;
			}
		}

		Host.CreatureAllowOverride = false;

		Player player = event.getPlayer();

		int score = _smashedEggs.get(player.getName()) + 1;

		displayCount(player, block.getLocation().add(0.5, 1, 0.5),
				(score >= 10 ? C.cDGreen : score >= 7 ? C.cGreen
						: score >= 4 ? C.cRed : C.cDRed)
						+ score);

		_smashedEggs.put(player.getName(), score);

		if (score == 10)
		{
			SetCompleted(player);
		}
	}

	@Override
	public void cleanupRoom()
	{
		for (Entity ent : _dragonEggs)
		{
			ent.remove();
		}
	}

	@Override
	public void setupPlayers()
	{
		setBorder(-18, 18, 0, 20, -18, 18);

		for (Player player : getChallengers())
		{
			_smashedEggs.put(player.getName(), 0);
			player.getInventory().setItem(
					0,
					new ItemBuilder(Material.IRON_AXE).setTitle(
							C.cWhite + "Egg Smasher").build());
		}
	}

	@Override
	public void generateRoom()
	{
		for (int x = -15; x <= 15; x++)
		{
			for (int z = -15; z <= 15; z++)
			{
				Block b = getCenter().getBlock().getRelative(x, 0, z);
				b.setType(Material.WOOL);
				b.setData((byte) UtilMath.r(16));

				addBlock(b);
			}
		}

		for (int i = 0; i < 9; i++)
		{
			Block b = getCenter().getBlock().getRelative(UtilMath.r(30) - 15,
					1, UtilMath.r(30) - 15);
			b.setType(Material.DRAGON_EGG);
			addBlock(b);
		}
	}

}
