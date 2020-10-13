package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import de.robingrether.idisguise.disguise.HorseDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkHorseKick extends SmashPerk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();

	public PerkHorseKick() 
	{
		super("Bone Kick", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Bone Kick"
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

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();
		
		if (isSuperActive(player))
			return;

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 6000, true, true))
			return;

		//Horse Animation
		/*DisguiseBase horse = Manager.GetDisguise().getDisguise(player);
		if (horse != null && horse instanceof DisguiseHorse)
		{
			((DisguiseHorse)horse).kick();
			Manager.GetDisguise().updateDisguise(horse);
		}

		HorseDisguise d1 = (HorseDisguise) Manager.GetDisguise().getDisguise(player);

		 */


		//Animation
		_active.put(player, System.currentTimeMillis());


		//AoE Area
		Location loc = player.getLocation();
		loc.add(player.getLocation().getDirection().setY(0).normalize().multiply(1.5));
		loc.add(0, 0.8, 0);

		for (Entity other : player.getWorld().getEntities())
		{
			if (!(other instanceof LivingEntity))
				continue;

			if (other instanceof Player)
				if (!Manager.GetGame().IsAlive((Player)other))
					continue;
			
			if (other.equals(player))
				continue;

			if (UtilMath.offset(loc, other.getLocation()) > 2.5)
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent((LivingEntity)other, player, null, 
					DamageCause.CUSTOM, 7, true, true, false,
					player.getName(), GetName());
			
			//Sound
			player.getWorld().playSound(player.getLocation(), Sound.SKELETON_HURT, 4f, 0.6f);
			player.getWorld().playSound(player.getLocation(), Sound.SKELETON_HURT, 4f, 0.6f);

			//Inform
			UtilPlayer.message(other, F.main("Skill", F.name(player.getName()) + " hit you with " + F.skill(GetName()) + "."));				
		}

		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
		
		//Slow
		Manager.GetCondition().Factory().Slow(GetName(), player, player, 0.8, 3, false, false, true, false);
	}

	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		//Player
		Iterator<Player> playerIterator = _active.keySet().iterator();
		
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			
			if (!player.isValid() || player.getHealth() <= 0 || UtilTime.elapsed(_active.get(player), 1000))
			{
				playerIterator.remove();
				/*
				//Horse Animation
				DisguiseBase horse = Manager.GetDisguise().getDisguise(player);
				if (horse != null && horse instanceof DisguiseHorse)
				{
					((DisguiseHorse)horse).stopKick();
					Manager.GetDisguise().updateDisguise(horse);
				}

				 */
				
				Manager.GetCondition().EndCondition(player, null, GetName());
			}
			else
			{
				Location loc = player.getLocation();
				loc.add(player.getLocation().getDirection().setY(0).normalize().multiply(1.5));
				loc.add(0, 0.8, 0);
				
				UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, loc, 0.3f, 0.3f, 0.3f, 0, 2,
						ViewDist.LONG, UtilServer.getPlayers());
			}
		}
	}

	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || (!event.GetReason().contains(GetName()) && !event.GetReason().contains("Flame Kick")))
			return;

		event.AddKnockback(GetName(), 4);
	}
}
