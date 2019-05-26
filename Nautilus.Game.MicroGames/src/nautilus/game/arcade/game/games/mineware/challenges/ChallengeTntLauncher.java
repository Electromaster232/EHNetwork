package nautilus.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilShapes;
import mineplex.core.recharge.Recharge;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.game.games.mineware.Challenge;
import nautilus.game.arcade.game.games.mineware.MineWare;

public class ChallengeTntLauncher extends Challenge
{
	private ArrayList<Location> _spawns = new ArrayList<Location>();
	private ArrayList<Entity> _tnt = new ArrayList<Entity>();

	public ChallengeTntLauncher(MineWare host)
	{
		super(host, ChallengeType.LastStanding, "Throw TNT, don't get knocked off!");
	}

	@Override
	public ArrayList<Location> getSpawns()
	{
		return _spawns;
	}

	@Override
	public void cleanupRoom()
	{
		Host.Damage = true;

		for (Entity tnt : _tnt)
		{
			tnt.remove();
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void Update(EntityExplodeEvent event)
	{
		if (!(event.getEntity() instanceof TNTPrimed))
			return;

		if (!_tnt.remove(event.getEntity()))
			return;

		HashMap<Player, Double> players = UtilPlayer.getInRadius(event.getLocation(),
				4 + ((System.currentTimeMillis() - StartTime) / 10000D));

		for (Player player : players.keySet())
		{
			double mult = players.get(player) / 2;
			mult += (System.currentTimeMillis() - StartTime) / 20000D;

			// Knockback
			UtilAction.velocity(player, UtilAlg.getTrajectory(event.getLocation(), player.getLocation()), 3 * mult, false, 0,
					0.5 + 2 * mult, 10, true);
		}

		event.blockList().clear();
	}

	@EventHandler
	public void onDamage(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_EXPLOSION)
			return;

		event.SetCancelled("TNT no damage");
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		UtilInv.Update(player);

		if (!Recharge.Instance.use(player, "TNT", 3000, true, false))
		{
			return;
		}

		TNTPrimed tnt = player.getWorld()
				.spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);
		UtilAction.velocity(tnt, player.getLocation().getDirection(), 0.6, false, 0, 0.2, 1, false);
		tnt.setFuseTicks((int) (60 * (1 - ((System.currentTimeMillis() - StartTime) / 70000))));

		_tnt.add(tnt);
	}

	@Override
	public void setupPlayers()
	{
		for (Player player : getChallengers())
		{
			player.getInventory().setItem(0, new ItemStack(Material.TNT));
		}
	}

	@Override
	public void generateRoom()
	{
		double radius = 6 + (getChallengers().size() / 2D);

		for (Location loc : UtilShapes.getCircle(getCenter(), false, radius))
		{
			Block block = loc.getBlock();

			block.setType(Material.STAINED_CLAY);
			block.setData((byte) UtilMath.r(16));

			addBlock(block);

			if (Math.abs(block.getLocation().add(0.5, 0, 0.5).distance(getCenter().add(0.5, 0, 0.5)) - (radius - 2)) < 1)
			{
				_spawns.add(block.getLocation().add(0.5, 1.1, 0.5));
			}
		}
	}

}
