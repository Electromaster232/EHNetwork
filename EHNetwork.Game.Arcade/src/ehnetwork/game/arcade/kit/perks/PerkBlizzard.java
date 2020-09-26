package ehnetwork.game.arcade.kit.perks;

import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkBlizzard extends Perk
{
	private WeakHashMap<Projectile, Player> _snowball = new WeakHashMap<Projectile, Player>();

	public PerkBlizzard() 
	{
		super("Blizzard", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to use " + C.cGreen + "Blizzard"
				});
	}

	@EventHandler
	public void EnergyUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;

			if (player.isBlocking())
				continue;
			
			player.setExp((float) Math.min(0.999, player.getExp()+(1f/60f)));
		}
	}

	@EventHandler
	public void Snow(UpdateEvent event) 
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!player.isBlocking())
				continue;

			if (!Kit.HasKit(player))
				continue;

			//Energy
			if (player.getExp() < 0.1)
				continue;

			player.setExp((float) Math.max(0, player.getExp()-(1f/9f)));

			for (int i=0 ; i<4 ; i++)
			{
				Snowball snow = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), Snowball.class);
				double x = 0.1 - (UtilMath.r(20)/100d);
				double y = UtilMath.r(20)/100d;
				double z = 0.1 - (UtilMath.r(20)/100d);
				snow.setShooter(player);
				snow.setVelocity(player.getLocation().getDirection().add(new Vector(x,y,z)).multiply(2));
				_snowball.put(snow, player);
			}

			//Effect
			player.getWorld().playSound(player.getLocation(), Sound.STEP_SNOW, 0.1f, 0.5f);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void Snowball(CustomDamageEvent event)
	{		
		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile proj = event.GetProjectile();
		if (proj == null)		return;

		if (!(proj instanceof Snowball))
			return;

		if (!_snowball.containsKey(proj))
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		event.SetCancelled("Blizzard");
		damagee.setVelocity(proj.getVelocity().multiply(0.15).add(new Vector(0, 0.15, 0)));

		//Damage Event
		if (damagee instanceof Player)
			if (Recharge.Instance.use((Player)damagee, GetName(), 200, false, false))
			{
				Manager.GetDamage().NewDamageEvent(damagee, event.GetDamagerEntity(true), null, 
						DamageCause.CUSTOM, 1, false, true, false,
						UtilEnt.getName(event.GetDamagerEntity(true)), GetName());
			}
	}

	@EventHandler
	public void SnowballForm(ProjectileHitEvent event)
	{
		if (!(event.getEntity() instanceof Snowball))
			return;

		if (_snowball.remove(event.getEntity()) == null)
			return;

		Manager.GetBlockRestore().Snow(event.getEntity().getLocation().getBlock(), (byte)1, (byte)7, 2000, 250, 0);
	}
}
