package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.recharge.RechargedEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkSquidShotgun extends Perk implements IThrown
{
	private HashMap<Firework, Vector> _fireworks = new HashMap<Firework, Vector>();

	public PerkSquidShotgun() 
	{
		super("Ink Shotgun", new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Ink Shotgun"
				});
	}
	
	@EventHandler
	public void Recharge(RechargedEvent event)
	{
		if (!event.GetAbility().equals(GetName()))
			return;
		
		event.GetPlayer().playSound(event.GetPlayer().getLocation(), Sound.NOTE_STICKS, 3f, 1f);
	}

	@EventHandler
	public void Shoot(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 2500, true, true))
			return;

		event.setCancelled(true);

		UtilInv.Update(player);

		//Firework
		FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.GREEN).with(Type.BURST).trail(false).build();

		for (int i=0 ; i<6 ; i++)
		{

			Vector random = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			random.normalize();
			random.multiply(0.3);
			
			try 
			{
				Vector vel = player.getLocation().getDirection().multiply(1.4).add(random);
				//Firework fw = Manager.GetFirework().launchFirework(player.getEyeLocation().subtract(0, 0.5, 0).add(player.getLocation().getDirection()), effect, vel);
				//_fireworks.put(fw, vel);
				
				//Projectile
				//Manager.GetProjectile().AddThrow(fw, player, this, -1, true, true, true, 3d, Manager.GetDisguise());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1f, 0.75f);
	}
	

	
	@EventHandler
	public void FireworkUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Firework> fwIterator = _fireworks.keySet().iterator();
		
		while (fwIterator.hasNext())
		{
			Firework fw = fwIterator.next();
			
			if (!fw.isValid())
			{
				fwIterator.remove();
				continue;
			}
			
			fw.setVelocity(_fireworks.get(fw));
		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		Explode(data);
		
		if (target == null)
			return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.PROJECTILE, 12, false, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());
		
		//Recharge
		if (data.GetThrower() instanceof Player)
		{
			Player player = (Player)data.GetThrower();
			Recharge.Instance.recharge(player, GetName());
			player.playSound(player.getLocation(), Sound.NOTE_STICKS, 3f, 1f);
		}
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Explode(data);
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Explode(data);
	}

	public void Explode(ProjectileUser data)
	{
		if (!(data.GetThrown() instanceof Firework))
		{
			data.GetThrown().remove();
			return;
		}
		
		Firework fw = (Firework)data.GetThrown();
		
		try 
		{
			//Manager.GetFirework().detonateFirework(fw);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
