package ehnetwork.core.gadget.gadgets;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ItemMelonLauncher extends ItemGadget implements IThrown
{
	private ArrayList<Item> _melon = new ArrayList<Item>();

	public ItemMelonLauncher(GadgetManager manager)
	{
		super(manager, "Melon Launcher", new String[] 
				{
				C.cWhite + "Deliciously fun!",
				C.cWhite + "Eat the melon slices for a",
				C.cWhite + "temporary speed boost!",
				},
				-1,
				Material.MELON_BLOCK, (byte)0,
				1000, new Ammo("Melon Launcher", "100 Melons", Material.MELON_BLOCK, (byte)0, new String[] { C.cWhite + "100 Melons for you to launch!" }, 500, 100));
	}

	@Override
	public void ActivateCustom(Player player)
	{
		//Action
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(Material.MELON_BLOCK));
		UtilAction.velocity(item, player.getLocation().getDirection(),
				1, false, 0, 0.2, 10, false);

		Manager.getProjectileManager().AddThrow(item, player, this, -1, true, true, true, 
				null, 1f, 1f, null, null, 0, UpdateType.TICK, 0.5f);

		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));

		//Effect
		item.getWorld().playSound(item.getLocation(), Sound.EXPLODE, 0.5f, 0.5f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target != null)
		{
			//Push
			UtilAction.velocity(target, 
					UtilAlg.getTrajectory2d(data.GetThrown().getLocation(), target.getLocation()),
					1.4, false, 0, 0.8, 1.5, true);

			//Effect
			target.playEffect(EntityEffect.HURT);
		}
		
		smash(data.GetThrown());
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		smash(data.GetThrown());
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		smash(data.GetThrown());
	}
	
	public void smash(Entity ent)
	{	
		//Effect
		ent.getWorld().playEffect(ent.getLocation(), Effect.STEP_SOUND, Material.MELON_BLOCK);
		
		for (int i=0 ; i<10 ; i++)
		{
			Item item = ent.getWorld().dropItem(ent.getLocation(), ItemStackFactory.Instance.CreateStack(Material.MELON));
			item.setVelocity(new Vector(UtilMath.rr(0.5, true), UtilMath.rr(0.5, false), UtilMath.rr(0.5, true)));
			item.setPickupDelay(30);
			
			_melon.add(item);
		}
		
		//Remove
		ent.remove();
	}

	@EventHandler
	public void pickupMelon(PlayerPickupItemEvent event)
	{
		if (!_melon.remove(event.getItem()))
			return;

		event.getItem().remove();

		event.setCancelled(true);

		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.EAT, 1f, 1f);

		if (!event.getPlayer().hasPotionEffect(PotionEffectType.SPEED))
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1), true);
	}

	@EventHandler
	public void cleanupMelon(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;

		for (Iterator<Item> melonIterator = _melon.iterator(); melonIterator.hasNext();)
		{
			Item melon = melonIterator.next();

			if (melon.isDead() || !melon.isValid() || melon.getTicksLived() > 400)
			{
				melonIterator.remove();
				melon.remove();
			}
		}
		
		while (_melon.size() > 60)
		{
			Item item = _melon.remove(0);
			item.remove();
		}
	}
}
