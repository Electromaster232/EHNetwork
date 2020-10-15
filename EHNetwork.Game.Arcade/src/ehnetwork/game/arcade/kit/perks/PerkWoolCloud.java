package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import de.robingrether.idisguise.disguise.SheepDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
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
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkWoolCloud extends Perk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	
	public PerkWoolCloud() 
	{
		super("Wooly Rocket", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Spade to " + C.cGreen + "Wooly Rocket"
				});
	}
	
	@EventHandler
	public void Leap(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE"))
			return;
		
		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;
	
		//Recharge
		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return;
		
		UtilAction.velocity(player, new Vector(0,1,0), 1, false, 0, 0, 2, true);
		
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
		
		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
		
		//Allow double jump
		player.setAllowFlight(true);
		
		setWoolColor(player, DyeColor.RED);
		
		_active.put(player, System.currentTimeMillis());
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (Manager.GetGame() == null)
			return;
		
		Iterator<Player> playerIterator = _active.keySet().iterator();
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			
			UtilParticle.PlayParticle(ParticleType.FLAME, player.getLocation(), 0.2f, 0.2f, 0.2f, 0, 4,
					ViewDist.LONGER, UtilServer.getPlayers());
			
			if (!UtilTime.elapsed(_active.get(player), 200))
				continue;
			
			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (player.equals(other))
					continue;
				
				if (UtilMath.offset(player, other) < 2)
				{
					//Damage Event
					Manager.GetDamage().NewDamageEvent(other, player, null, 
							DamageCause.CUSTOM, 8, true, false, false,
							player.getName(), GetName());	
					
					UtilParticle.PlayParticle(ParticleType.EXPLODE, other.getLocation(), 0f, 0f, 0f, 0, 1,
							ViewDist.MAX, UtilServer.getPlayers());
					UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation(), 0.2f, 0.2f, 0.2f, 0, 10,
							ViewDist.MAX, UtilServer.getPlayers());
				}
			}
			
			if (UtilEnt.isGrounded(player) || UtilTime.elapsed(_active.get(player), 1200))
			{
				playerIterator.remove();
				setWoolColor(player, DyeColor.WHITE);
			}
		}
	}
	
	public void setWoolColor(Player player, DyeColor color)
	{
		SheepDisguise disguise = (SheepDisguise) Manager.GetDisguise().createDisguise(EntityType.SHEEP);
		if (disguise != null && disguise instanceof SheepDisguise)
		{
			//DisguiseSheep sheep = (DisguiseSheep)disguise;
			//sheep.setSheared(false);
			disguise.setColor(color);
			
			//sheep.UpdateDataWatcher();
			Manager.GetDisguise().applyDisguise(disguise, player);
		}
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 2.5);
	}
}
