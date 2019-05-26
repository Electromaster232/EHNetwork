package nautilus.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.HashMap;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.game.games.mineware.Challenge;
import nautilus.game.arcade.game.games.mineware.MineWare;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChallengeHitTargets extends Challenge
{
	private HashMap<String, ArrayList<String>> _targets = new HashMap<String, ArrayList<String>>();
	private int _targetsEach;

	public ChallengeHitTargets(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Hit the chosen players");
	}

	@EventHandler
	public void onDamage(CustomDamageEvent event)
	{
		Player p = event.GetDamagerPlayer(true);

		if (p == null || UtilPlayer.isSpectator(p))
			return;

		if (event.GetDamageePlayer() == null)
			return;

		if (!_targets.containsKey(p.getName()))
			return;

		event.SetCancelled("No damage");

		String name = event.GetDamageePlayer().getName();

		ArrayList<String> targets = _targets.get(p.getName());

		if (!targets.remove(name))
		{
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 1f);

			UtilPlayer.message(p, C.cYellow + "They are not your target! " + getMessage(p));
			return;
		}

		displayCount(p, event.GetDamageeEntity().getEyeLocation().add(0, 0.3, 0), (targets.isEmpty() ? C.cDGreen
				: targets.size() == 1 ? C.cRed : C.cDRed) + (_targetsEach - targets.size()));

		if (targets.isEmpty())
		{
			SetCompleted(p);
		}
	}

	@Override
	public String getMessage(Player player)
	{
		return C.cYellow + "Hit the players " + C.cWhite
				+ StringUtils.join(_targets.get(player.getName()), C.cYellow + ", " + C.cWhite);
	}

	@Override
	public int getMinPlayers()
	{
		return 4;
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		ArrayList<Location> spawns = new ArrayList<Location>();

		for (int x = -8; x <= 8; x++)
		{
			for (int z = -8; z <= 8; z++)
			{
				if (x % 2 == 0 && z % 2 == 0)
				{
					spawns.add(getCenter().clone().add(x + 0.5, 1.1, z + 0.5));
				}
			}
		}

		return spawns;
	}

	@Override
	public void cleanupRoom()
	{
		Host.DamagePvP = false;
	}

	@Override
	public void setupPlayers()
	{
		Host.DamagePvP = true;
		ArrayList<Player> players = getChallengers();

		for (Player player : players)
		{
			ArrayList<String> names = new ArrayList<String>();

			for (Player p : players)
			{
				if (p != player)
				{
					names.add(p.getName());
				}
			}

			while (names.size() > 3)
			{
				names.remove(UtilMath.r(names.size()));
			}

			_targetsEach = names.size();

			_targets.put(player.getName(), names);
		}
	}

	@Override
	public void generateRoom()
	{
		for (int x = -12; x <= 12; x++)
		{
			for (int z = -12; z <= 12; z++)
			{
				Block b = getCenter().clone().add(x, 0, z).getBlock();
				b.setType(Material.SMOOTH_BRICK);
				addBlock(b);

				if (Math.abs(x) > 1 && Math.abs(x) < 8 && Math.abs(z) > 1 && Math.abs(z) < 8)
				{
					for (int y = 1; y < 3; y++)
					{
						Block block = b.getRelative(0, y, 0);
						block.setType(Material.SMOOTH_BRICK);
						block.setData((byte) (UtilMath.r(8) < 7 ? 0 : UtilMath.r(2) + 1));
						addBlock(block);
					}
				}
			}
		}
	}

}
