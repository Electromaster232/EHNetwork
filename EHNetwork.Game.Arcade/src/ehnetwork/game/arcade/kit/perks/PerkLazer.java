package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseSheep;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkLazer extends Perk
{
	private double _range;
	private long _recharge;
	
	private HashSet<Player> _active = new HashSet<Player>();

	public PerkLazer(double range, long recharge) 
	{
		super("Static Lazer", new String[] 
				{
				C.cYellow + "Hold Block" + C.cGray + " with Sword to use " + C.cGreen + "Static Lazer"
				});

		_range = range;
		_recharge = recharge;
	}

	@EventHandler
	public void skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SWORD"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), _recharge, true, true))
			return;
		
		_active.add(player);
	}
	
	@EventHandler
	public void chargeFire(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Player> playerIterator = _active.iterator();
		
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			
			if (player.isBlocking())
			{
				player.setExp(Math.min(0.999f, player.getExp() + 0.035f));
				
				player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 0.25f + player.getExp(), 0.75f + player.getExp());
				
				//Wool
				DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
				if (disguise != null && disguise instanceof DisguiseSheep)
				{
					DisguiseSheep sheep = (DisguiseSheep)disguise;
					if (Math.random() > 0.5)
						sheep.setColor(DyeColor.YELLOW);
					else
						sheep.setColor(DyeColor.BLACK);
					
					sheep.setSheared(false);
					
					sheep.UpdateDataWatcher();
					Manager.GetDisguise().updateDisguise(disguise);
				}
				
				if (player.getExp() >= 0.999f)
				{
					playerIterator.remove();
					fire(player);
				}
			}
			else
			{
				playerIterator.remove();
				fire(player);
			}
		}
	}
	
	public void fire(Player player)
	{	
		if (player.getExp() <= 0.2f)
		{
			setWoolColor(player, DyeColor.WHITE);
			player.setExp(0f);
			return;
		}
		
		double curRange = 0;
		while (curRange <= _range * player.getExp())
		{
			Location newTarget = player.getEyeLocation().add(player.getLocation().getDirection().multiply(curRange));

			//Hit Player
			boolean hitPlayer = false;
			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (other.equals(player))
					continue;
				
				if (UtilMath.offset(newTarget, other.getLocation().add(0, 1, 0)) < 3)
				{
					hitPlayer = true;
					break;
				}
			}
			if (hitPlayer)
				break;
			
			//Hit Block
			if (!UtilBlock.airFoliage(newTarget.getBlock()))
			{
				break;
			}
				
			//Progress Forwards
			curRange += 0.2;

			//Smoke Trail
			UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, newTarget, 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
		}

		//Destination
		Location target = player.getLocation().add(player.getLocation().getDirection().multiply(curRange));
		
		UtilParticle.PlayParticle(ParticleType.EXPLODE, target, 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		//Firework 
		UtilFirework.playFirework(player.getLocation().add(player.getLocation().getDirection().multiply(Math.max(0, curRange - 0.6))), Type.BURST, Color.YELLOW, false, false);
		
		for (LivingEntity other : UtilEnt.getInRadius(target, 5).keySet())
		{
			if (other.equals(player))
				continue;

			//Do from center
			if (UtilMath.offset(target, other.getLocation().add(0, 1, 0)) < 3.5)
			{
				//Damage Event
				Manager.GetDamage().NewDamageEvent(other, player, null, 
						DamageCause.CUSTOM, player.getExp() * 7, true, true, false,
						player.getName(), GetName());	
			}
		}
			
		//Inform
		UtilPlayer.message(player, F.main("Game", "You fired " + F.skill(GetName()) + "."));
		
		//Sound
		player.getWorld().playSound(player.getEyeLocation(), Sound.ZOMBIE_REMEDY, 0.5f + player.getExp(), 1.75f - player.getExp());
		player.getWorld().playSound(player.getLocation(), Sound.SHEEP_IDLE, 2f, 1.5f);
		
		//Wool
		setWoolColor(player, DyeColor.WHITE);
		player.setExp(0f);
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 3);
	}
	
	
	public void setWoolColor(Player player, DyeColor color)
	{
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null && disguise instanceof DisguiseSheep)
		{
			DisguiseSheep sheep = (DisguiseSheep)disguise;
			sheep.setSheared(false);
			sheep.setColor(color);
			
			sheep.UpdateDataWatcher();
			Manager.GetDisguise().updateDisguise(disguise);
		}
	}
}
