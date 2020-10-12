package ehnetwork.game.microgames.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.game.microgames.kit.perks.data.FireflyData;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkFirefly extends SmashPerk
{
	private HashSet<FireflyData> _data = new HashSet<FireflyData>();
	private int _tick = 0;
	
	public PerkFirefly() 
	{
		super("Firefly", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Firefly"
				});
	}

	@EventHandler
	public void Skill(PlayerInteractEvent event)
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
		
		if (isSuperActive(event.getPlayer()))
			return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 12000, true, true))
			return;
		
		_data.add(new FireflyData(player));
		
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@Override
	public void addSuperCustom(Player player)
	{
		_data.add(new FireflyData(player));
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		_tick = (_tick + 1)%3;
		
		Iterator<FireflyData> dataIterator = _data.iterator();
		
		while (dataIterator.hasNext())
		{
			FireflyData data = dataIterator.next();
			
			boolean superActive = isSuperActive(data.Player);
			
			//Teleport
			if (!UtilTime.elapsed(data.Time, 1500) && !superActive)
			{
				data.Player.setVelocity(new Vector(0,0,0));//.teleport(data.Location);	
				data.Player.getWorld().playSound(data.Player.getLocation(), Sound.EXPLODE, 0.2f, 0.6f);
				data.Location = data.Player.getLocation();
				
				if (_tick == 0)
				{
					//Firework
					UtilFirework.playFirework(data.Player.getLocation().add(0, 0.6, 0), Type.BURST, Color.ORANGE, false, false);
				}
				
			}
			//Velocity
			else if (!UtilTime.elapsed(data.Time, 2500) || superActive)
			{
				data.Player.setVelocity(data.Player.getLocation().getDirection().multiply(superActive ? 0.9 : 0.7).add(new Vector(0,0.15,0)));
				//data.Player.setVelocity(data.Location.getDirection().multiply(0.7).add(new Vector(0,0.1,0)));
				data.Player.getWorld().playSound(data.Player.getLocation(), Sound.EXPLODE, 0.6f, 1.2f);
				
				if (_tick == 0)
				{
					//Firework
					UtilFirework.playFirework(data.Player.getLocation().add(0, 0.6, 0), isSuperActive(data.Player) ? Type.BALL : Type.BURST, Color.RED, false, superActive);	
				}	
				
				for (Player other : UtilPlayer.getNearby(data.Player.getLocation(), isSuperActive(data.Player) ? 6 : 4))
				{
					if (other.equals(data.Player))
						continue;
					
					if (!Manager.GetGame().IsAlive(other))
						continue;
					
					other.playEffect(EntityEffect.HURT);
					
					if (_tick == 0)
						if (!data.Targets.contains(other))
						{
							data.Targets.add(other);
							
							//Damage Event
							Manager.GetDamage().NewDamageEvent(other, data.Player, null, 
									DamageCause.CUSTOM, 10, true, true, false,
									data.Player.getName(), isSuperActive(data.Player) ? "Phoenix" : GetName());
							
							UtilPlayer.message(other, F.main("Game", F.elem(Manager.GetColor(data.Player) + data.Player.getName()) + " hit you with " + F.elem(GetName()) + "."));
						}
				}
			}
			else
			{
				dataIterator.remove();
			}
		}		
	}
	
	@EventHandler
	public void FireflyDamage(CustomDamageEvent event)
	{
		if (event.GetDamage() <= 4)
			return;
		
		Iterator<FireflyData> dataIterator = _data.iterator();
		
		while (dataIterator.hasNext())
		{
			FireflyData data = dataIterator.next();
			
			if (!data.Player.equals(event.GetDamageeEntity()))
				continue;
			
			if (!UtilTime.elapsed(data.Time, 1250) && !isSuperActive(data.Player))// && event.GetCause() == DamageCause.PROJECTILE)
			{
				dataIterator.remove();
			}
			else
			{
				event.SetCancelled("Firefly Immunity");
			}
		}
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2);
	}
}
