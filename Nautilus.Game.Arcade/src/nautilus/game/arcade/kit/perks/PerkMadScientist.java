package nautilus.game.arcade.kit.perks;

import java.util.Arrays;

import mineplex.core.common.util.C;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.common.util.UtilServer;
import mineplex.core.disguise.disguises.DisguiseBase;
import mineplex.core.disguise.disguises.DisguiseBlaze;
import mineplex.core.disguise.disguises.DisguiseCow;
import mineplex.core.disguise.disguises.DisguiseInsentient;
import mineplex.core.disguise.disguises.DisguisePig;
import mineplex.core.disguise.disguises.DisguisePigZombie;
import mineplex.core.disguise.disguises.DisguiseSkeleton;
import mineplex.core.disguise.disguises.DisguiseSlime;
import mineplex.core.disguise.disguises.DisguiseSpider;
import mineplex.core.disguise.disguises.DisguiseZombie;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Perk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PerkMadScientist extends Perk implements IThrown
{

	private NautHashMap<String, Creature> _activeKitHolders = new NautHashMap<String, Creature>();
	
	public PerkMadScientist(ArcadeManager manager)
	{
		super("Mad Scientist", new String[]
		{
				"Recieve 1 Egg every 90 seconds! (Max 3)",
				"Eggs spawn a loyal minion to fight for you",
		});
	}

	@EventHandler
	public void eggSpawn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (Manager.GetGame() == null)
			return;
		
		if (!UtilTime.elapsed(Manager.GetGame().GetStateTime(), 60000))
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;

			if (!Manager.GetGame().IsAlive(player))
				continue;

			if(UtilInv.contains(player, Material.MONSTER_EGG, (byte) 0, 3)) {
				continue;
			}
			
			if (!Recharge.Instance.use(player, "Egg Spawn", 90000, false, false))
			{
				continue;
			}
			
			else
			{
				player.getInventory().addItem(
						ItemStackFactory.Instance.CreateStack(
								Material.MONSTER_EGG, (byte) 0, 1, C.cYellow
										+ C.Bold + "Click To Throw",
								Arrays.asList("")));
				player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 2f,
						1f);
			}
		}
	}

	@EventHandler
	public void throwEgg(PlayerInteractEvent e)
	{

		Player player = e.getPlayer();

		if (player.getItemInHand().getType() != Material.MONSTER_EGG)
		{
			return;
		}

		if (!Kit.HasKit(player))
			return;

		e.setCancelled(true);

		org.bukkit.entity.Item ent = player.getWorld().dropItem(
				player.getEyeLocation(),
				ItemStackFactory.Instance.CreateStack(player.getItemInHand()
						.getType()));
		UtilAction.velocity(ent, player.getLocation().getDirection(), 1.5,
				false, 0, 0.2, 10, false);

		Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true,
				true, false, 0.6f);

		UtilInv.remove(player, Material.MONSTER_EGG, (byte) 0, 1);

	}

	@EventHandler
	public void onTargetzombie(EntityTargetLivingEntityEvent e)
	{

		if (!(e.getEntity() instanceof Zombie))
		{
			return;
		}

		if (!(e.getTarget() instanceof Player))
		{
			return;
		}

		Zombie zombie = (Zombie) e.getEntity();
		Player targetPlayer = (Player) e.getTarget();

		String name = ChatColor
				.stripColor(zombie.getCustomName().split("'")[0]);

		if (targetPlayer.getName().equalsIgnoreCase(name))
		{
			e.setCancelled(true);
		}

	}


	@EventHandler
	public void onOwnerDeath(PlayerDeathEvent e)
	{

		String playerName = e.getEntity().getName();

		if (_activeKitHolders.containsKey(playerName))
		{
			Creature zombie = _activeKitHolders.get(playerName);
			zombie.remove();
			System.out.println("Chicken removed due to owner death");
			_activeKitHolders.remove(playerName);
		}

	}
	
	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{

		if (target != null)
		{
			Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null,
					DamageCause.CUSTOM, 1.5D, true, false, false,
					UtilEnt.getName(data.GetThrower()), GetName());

		}

		spawnMobs(data);
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		spawnMobs(data);

	}

	@Override
	public void Expire(ProjectileUser data)
	{

		spawnMobs(data);
	}

	public void spawnMobs(ProjectileUser data)
	{
		data.GetThrown()
				.getWorld()
				.playSound(data.GetThrown().getLocation(), Sound.EXPLODE, 1f,
						1.6f);

		UtilParticle.PlayParticle(ParticleType.EXPLODE, data.GetThrown()
				.getLocation(), 0F, 0F, 0F, 1F, 5, ViewDist.SHORT, UtilServer
				.getPlayers());

		data.GetThrown().remove();

		Manager.GetGame().CreatureAllowOverride = true;
		
		Zombie zombie = (Zombie) data.GetThrown().getWorld()
				.spawn(data.GetThrown().getLocation(), Zombie.class);
		zombie.setRemoveWhenFarAway(false);
		zombie.setMaxHealth(10.0D);
		zombie.setHealth(10.0D);
		UtilEnt.silence(zombie, true);
		
		//Name
		zombie.setCustomName(C.cAqua + UtilEnt.getName(data.GetThrower()) + "'s Minion");
		zombie.setCustomNameVisible(true);
		
//		double r = Math.random();
//		
//		DisguiseBase disguise;
//		if (r > 0.8)
//		{
//			disguise = new DisguiseZombie(zombie);
//		}
//		else if (r > 0.6)
//		{
//			disguise = new DisguiseBlaze(zombie);
//		}
//		else if (r > 0.4)
//		{
//			disguise = new DisguiseSpider(zombie);
//		}
//		else if (r > 0.2)
//		{
//			disguise = new DisguiseSkeleton(zombie);
//		}
//		else 
//		{
//			disguise = new DisguiseSlime(zombie);
//			((DisguiseSlime)disguise).SetSize(2);
//		}
//		
//		if (disguise instanceof DisguiseInsentient)
//		{
//			if (((Player) data.GetThrower()).getName().toLowerCase().endsWith("s"))
//			{
//				((DisguiseInsentient)disguise).setName(C.cAqua + UtilEnt.getName(data.GetThrower()) + "' Minion");
//			}
//			else
//			{
//				((DisguiseInsentient)disguise).setName(C.cAqua + UtilEnt.getName(data.GetThrower()) + "' Minion");
//			}
//
//			((DisguiseInsentient)disguise).setCustomNameVisible(true);
//		}
//
//		Manager.GetDisguise().disguise(disguise);
		
		Manager.GetGame().CreatureAllowOverride = false;

	}

}
