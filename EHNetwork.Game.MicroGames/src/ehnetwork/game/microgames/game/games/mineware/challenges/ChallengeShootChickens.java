package ehnetwork.game.microgames.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.mineware.Challenge;
import ehnetwork.game.microgames.game.games.mineware.MineWare;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class ChallengeShootChickens extends Challenge
{
	private ArrayList<Chicken> _chickens = new ArrayList<Chicken>();
	private HashMap<String, Integer> _killedChickens = new HashMap<String, Integer>();
	private ArrayList<Projectile> _arrows = new ArrayList<Projectile>();

	public ChallengeShootChickens(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Shoot 6 chickens");
	}

	@Override
	public void cleanupRoom()
	{
		for (Chicken chicken : _chickens)
		{
			chicken.remove();
		}

		for (Projectile arrow : _arrows)
		{
			arrow.remove();
		}

		Host.DamagePvE = false;
	}

	@EventHandler
	public void onShoot(ProjectileLaunchEvent event)
	{
		_arrows.add(event.getEntity());
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event)
	{
		event.getDrops().clear();
		event.setDroppedExp(0);
	}

	@EventHandler
	public void Damage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
		{
			event.SetCancelled("Not projectile damage");
			return;
		}

		Player player = event.GetDamagerPlayer(true);

		if (player == null)
			return;

		if (event.isCancelled())
			return;

		LivingEntity ent = event.GetDamageeEntity();

		if (!_chickens.remove(ent))
		{
			event.SetCancelled("Not a chicken");
			return;
		}

		event.AddMod("Ensure Death", null, 10, false);

		int score = _killedChickens.get(player.getName()) + 1;

		Location sloc = player.getEyeLocation();
		sloc.add(UtilAlg.getTrajectory(sloc, ent.getEyeLocation()).multiply(Math.min(7, ent.getLocation().distance(sloc))));

		displayCount(player, sloc, (score >= 6 ? C.cDGreen : score >= 4 ? C.cGreen : score >= 2 ? C.cRed : C.cDRed) + score);

		_killedChickens.put(player.getName(), score);

		if (score == 6)
		{
			SetCompleted(player);
		}
	}

	@EventHandler
	public void onHalfSecond(UpdateEvent event)
	{
		if (!Host.IsLive())
		{
			return;
		}

		if (event.getType() != UpdateType.FAST)
		{
			return;
		}

		Iterator<Chicken> itel = _chickens.iterator();

		while (itel.hasNext())
		{
			Chicken chicken = itel.next();

			if (chicken.isOnGround() || !chicken.isValid())
			{
				chicken.remove();
				itel.remove();
			}
		}

		if (_chickens.size() < 11 + (getChallengers().size() * 2))
		{
			Location loc = getCenter().clone().add(UtilMath.r(20) - 10, 15, UtilMath.r(20) - 10);

			Host.CreatureAllowOverride = true;
			Chicken chicken = (Chicken) loc.getWorld().spawnEntity(loc, EntityType.CHICKEN);
			Host.CreatureAllowOverride = false;
			chicken.setMaxHealth(0.1);
			chicken.setHealth(0.1);

			_chickens.add(chicken);
		}
	}

	@Override
	public void setupPlayers()
	{
		setBorder(-10, 10, 0, 10, -10, 10);

		for (Player player : Host.GetPlayers(true))
		{
			player.getInventory().setItem(0,
					new ItemBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_INFINITE, 1).setUnbreakable(true).build());
			player.getInventory().setItem(9, new ItemStack(Material.ARROW));

			_killedChickens.put(player.getName(), 0);
		}

		Host.DamagePvE = true;
	}

	@Override
	public void generateRoom()
	{
		for (int x = -10; x <= 10; x++)
		{
			for (int z = -10; z <= 10; z++)
			{
				for (int y = 0; y <= 1; y++)
				{
					Block b = getCenter().getBlock().getRelative(x, y, z);

					if (y == 0)
					{
						b.setType(Material.GRASS);
					}
					else
					{
						if (Math.abs(x) == 10 || Math.abs(z) == 10)
						{
							b.setType(Material.FENCE);
						}
						else if (UtilMath.r(4) == 0)
						{
							if (UtilMath.r(8) == 0)
							{
								b.setType(UtilMath.random.nextBoolean() ? Material.YELLOW_FLOWER : Material.RED_ROSE);
							}
							else
							{
								b.setType(Material.LONG_GRASS);
								b.setData((byte) 1);
							}
						}
					}

					if (b.getType() != Material.AIR)
					{
						addBlock(b);
					}
				}
			}
		}
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
}
