package ehnetwork.game.microgames.game.games.wizards.spells;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilShapes;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.wizards.Spell;
import ehnetwork.game.microgames.game.games.wizards.spellinterfaces.SpellClick;

public class SpellIceShards extends Spell implements SpellClick, IThrown
{
	private HashMap<Entity, Location> _lastParticles = new HashMap<Entity, Location>();

	@Override
	public void castSpell(final Player player)
	{
		shoot(player);

		for (int i = 1; i <= getSpellLevel(player); i++)
		{

			Bukkit.getScheduler().scheduleSyncDelayedTask(Wizards.getArcadeManager().getPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					shoot(player);
				}

			}, i * 5);
		}

		charge(player);
	}

	private void shoot(Player player)
	{

		if (Wizards.IsAlive(player))
		{
			// Boost

			org.bukkit.entity.Item ent = player.getWorld().dropItem(
					player.getEyeLocation(),
					ItemStackFactory.Instance.CreateStack(Material.GHAST_TEAR, (byte) 0, 1, "Ice Shard " + player.getName() + " "
							+ System.currentTimeMillis()));

			UtilAction.velocity(ent, player.getLocation().getDirection(), 2, false, 0, 0.2, 10, false);
			Wizards.getArcadeManager().GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 2f);

			player.getWorld().playSound(player.getLocation(), Sound.CLICK, 1.2F, 0.8F);

			_lastParticles.put(ent, ent.getLocation());

		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target != null && target instanceof Player)
		{

			// Damage Event
			Wizards.getArcadeManager()
					.GetDamage()
					.NewDamageEvent(target, data.GetThrower(), null, DamageCause.PROJECTILE, 4 /*+ (timesHit * 2)*/, true, true,
							false, "Ice Shard", "Ice Shard");

		}

		handleShard(data);
	}

	private void handleShard(ProjectileUser data)
	{
		data.GetThrown().remove();
		Location loc = data.GetThrown().getLocation();

		UtilParticle.PlayParticle(ParticleType.BLOCK_CRACK.getParticle(Material.PACKED_ICE, 0), loc, 0.3F, 0.3F, 0.3F, 0, 12,
				ViewDist.LONG, UtilServer.getPlayers());
		loc.getWorld().playSound(loc, Sound.GLASS, 1.2F, 1);

		for (int x = -1; x <= 1; x++)
		{
			for (int y = -1; y <= 1; y++)
			{
				for (int z = -1; z <= 1; z++)
				{
					Block block = loc.clone().add(x, y, z).getBlock();

					if (block.getType() == Material.FIRE)
					{
						block.setType(Material.AIR);
					}
				}
			}
		}

		_lastParticles.remove(data.GetThrown());
	}

	@EventHandler
	public void onTick(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
		{
			return;
		}

		for (Entity entity : _lastParticles.keySet())
		{
			for (Location loc : UtilShapes.getLinesDistancedPoints(_lastParticles.get(entity), entity.getLocation(), 0.3))
			{
				UtilParticle.PlayParticle(ParticleType.BLOCK_CRACK.getParticle(Material.PACKED_ICE, 0), loc, 0, 0, 0, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
			}

			_lastParticles.put(entity, entity.getLocation());
		}
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		handleShard(data);
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		handleShard(data);
	}

}
