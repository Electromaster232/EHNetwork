package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkWitherImage extends SmashPerk
{	
	private HashMap<Player, Skeleton> _images = new HashMap<Player, Skeleton>();
	
	public PerkWitherImage() 
	{
		super("Wither Image", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Wither Image"
				});
	}

	@EventHandler
	public void activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();

		if (isSuperActive(player))
			return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (!_images.containsKey(player))
		{
			if (!Recharge.Instance.use(player, GetName(), 12000, true, true))
				return;
			
			//Spawn
			Manager.GetGame().CreatureAllowOverride = true;
			Skeleton skel = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), Skeleton.class);
			Manager.GetGame().CreatureAllowOverride = false;
			
			skel.setSkeletonType(SkeletonType.WITHER);
			
			skel.getEquipment().setItemInHand(player.getItemInHand());
			skel.setMaxHealth(20);
			skel.setHealth(skel.getMaxHealth());
			
			if (Manager.GetGame().GetTeamList().size() > 1)
				skel.setCustomName(Manager.GetColor(player) + player.getName());
			else
				skel.setCustomName(C.cYellow + player.getName());
			
			skel.setCustomNameVisible(true);
				
			//skel.setLeashHolder(player);
			
			UtilAction.velocity(skel, player.getLocation().getDirection(), 1.6, false, 0, 0.2, 10, true);
			
			_images.put(player, skel);
			
			Recharge.Instance.use(player, "Wither Swap", 500, false, false);

			//Sound
			player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
			
			//Inform
			UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
		}
		else
		{
			if (!Recharge.Instance.use(player, "Wither Swap", 1000, true, false))
				return;
			
			Skeleton skel = _images.get(player);
			
			Location loc = skel.getLocation();
			skel.teleport(player.getLocation());
			player.teleport(loc);
			
			//Sound
			player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 1f, 2f);
			
			//Inform
			UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Wither Swap") + "."));
		}
	}
	
	@EventHandler
	public void entityTarget(EntityTargetEvent event)
	{
		if (_images.containsKey(event.getTarget()))
			if (_images.get(event.getTarget()).equals(event.getEntity()))
				event.setCancelled(true);					
	}
	
	@EventHandler
	public void damage(CustomDamageEvent event)
	{
		Player damagee = event.GetDamageePlayer();
		if (damagee == null) 	return;
		
		if (!_images.containsKey(damagee))
			return;
		
		LivingEntity damager = event.GetDamagerEntity(false);
		if (damager == null)	return;
		
		if (_images.get(damagee).equals(damager))
			event.SetCancelled("Wither Image");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void minionDamageTransfer(CustomDamageEvent event)
	{
		LivingEntity damager = event.GetDamagerEntity(true);
		if (damager == null) 	return;
		
		if (!_images.containsValue(damager))
			return;
		
		for (Player player : _images.keySet())
		{
			if (_images.get(player).equals(damager))
			{
				event.SetDamager(player);
				event.setKnockbackOrigin(damager.getLocation());
				event.AddMod(GetName(), "Wither Image", -5.5, true);
			}
		}
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Iterator<Player> playerIterator = _images.keySet().iterator();
		
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			Skeleton skel = _images.get(player);
					
			if (!player.isValid() || !skel.isValid() || skel.getTicksLived() > 160)
			{
				//Effect
				Manager.GetBlood().Effects(null, skel.getLocation().add(0, 0.5, 0), 12, 0.3, Sound.WITHER_HURT, 1f, 0.75f, Material.BONE, (byte)0, 40, false);
				
				playerIterator.remove();
				skel.remove();
				continue;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void clean(PlayerDeathEvent event)
	{
		Skeleton skel = _images.remove(event.getEntity());
		
		if (skel != null)
		{
			//Effect
			Manager.GetBlood().Effects(null, skel.getLocation().add(0, 0.5, 0), 12, 0.3, Sound.WITHER_HURT, 1f, 0.75f, Material.BONE, (byte)0, 40, false);
			
			skel.remove();
		}
	}
	
}
