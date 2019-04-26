package nautilus.game.arcade.kit.perks;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkEggGun extends SmashPerk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	
	public PerkEggGun() 
	{
		super("Egg Blaster", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to use " + C.cGreen + "Egg Blaster"
				});
	}
	
	
	@EventHandler
	public void Activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SWORD"))
			return;
		
		Player player = event.getPlayer();
		
		if (isSuperActive(event.getPlayer()))
			return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 2500, true, true))
			return;
		
		_active.put(player, System.currentTimeMillis());

		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : UtilServer.getPlayers())
		{
			if (!isSuperActive(cur))
			{
				if (!_active.containsKey(cur))
					continue;
				
				if (!cur.isBlocking())
				{
					_active.remove(cur);
					continue;
				}
				
				if (UtilTime.elapsed(_active.get(cur), 750))
				{
					_active.remove(cur);
					continue;
				}
			}
			
			Vector offset = cur.getLocation().getDirection();
			if (offset.getY() < 0)
				offset.setY(0);
			
			Egg egg = cur.getWorld().spawn(cur.getLocation().add(0, 0.5, 0).add(offset), Egg.class);
			egg.setVelocity(cur.getLocation().getDirection().add(new Vector(0,0.2,0)));
			egg.setShooter(cur);
			 
			//Effect
			cur.getWorld().playSound(cur.getLocation(), Sound.CHICKEN_EGG_POP, 0.5f, 1f);
		}
	}
	
	@EventHandler
	public void EggHit(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;
		
		if (!(event.GetProjectile() instanceof Egg))
			return;
		
		if (event.GetDamage() >= 1)
			return;
		
		event.SetCancelled("Egg Blaster");
		
		Egg egg = (Egg)event.GetProjectile();
		
		//Damage Event
		Manager.GetDamage().NewDamageEvent(event.GetDamageeEntity(), (LivingEntity)egg.getShooter(), egg, 
				DamageCause.PROJECTILE, 1, true, true, false,
				UtilEnt.getName((LivingEntity)egg.getShooter()), GetName());
		
		event.GetDamageeEntity().setVelocity(new Vector(0,0,0));
	}
}
