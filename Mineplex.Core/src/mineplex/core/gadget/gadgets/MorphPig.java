package mineplex.core.gadget.gadgets;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilServer;
import mineplex.core.disguise.disguises.DisguisePig;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.MorphGadget;

public class MorphPig extends MorphGadget
{	
	private HashSet<Player> _double = new HashSet<Player>();
	
	public MorphPig(GadgetManager manager)
	{
		super(manager, "Pig Morph", new String[] 
				{
				C.cWhite + "Oink. Oink. Oink.... Oink?",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Oink",
				C.cYellow + "Collide" + C.cGray + " to use " + C.cGreen + "Pig Bounce",
				" ",
				C.cPurple + "Unlocked with Ultra Rank",
				},
				-1,
				Material.PORK, (byte)0);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguisePig disguise = new DisguisePig(player);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		disguise.setCustomNameVisible(true);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}

	@EventHandler
	public void Snort(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 400, false, false))
			return;
		
		player.getWorld().playSound(player.getLocation(), Sound.PIG_IDLE, 1f, (float)(0.75 + Math.random() * 0.5));
		
	}
	
	@EventHandler
	public void HeroOwner(PlayerJoinEvent event)
	{
		if (Manager.getClientManager().Get(event.getPlayer()).GetRank().Has(Rank.ULTRA))
		{
			Manager.getDonationManager().Get(event.getPlayer().getName()).AddUnknownSalesPackagesOwned(GetName());
		}	
	}
	
	@EventHandler
	public void Collide(PlayerToggleFlightEvent event)
	{
		_double.add(event.getPlayer());
		Recharge.Instance.useForce(event.getPlayer(), GetName() + " Double Jump", 200);
	}
	
	@EventHandler
	public void Collide(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : GetActive())
		{
			//Grounded
			if (_double.contains(player))
				if (UtilEnt.isGrounded(player))
					if (Recharge.Instance.usable(player, GetName() + " Double Jump"))
						_double.remove(player);
			
			double range = 1;
			
			if (_double.contains(player))
				range += 0.5;
			
			if (player.getVehicle() != null)
				continue;
			
			if (!Recharge.Instance.usable(player, GetName() + " Collide"))
				continue;
			
			for (Player other : UtilServer.getPlayers())
			{
				if (other.equals(player))
					continue;
				
				if (other.getVehicle() != null)
					continue;
				
				if (!Recharge.Instance.usable(other, GetName() + " Collide"))
					continue;

				if (UtilMath.offset(player, other) > range)
					continue;
				
				if (Manager.collideEvent(this, other))
					continue; 
				
				//Cooldown
				Recharge.Instance.useForce(other, GetName() + " Collide", 200);
				Recharge.Instance.useForce(player, GetName() + " Collide", 200);
				
				double power = 0.4;
				double height = 0.1;
				if (player.isSprinting())
				{
					power = 0.6;
					height = 0.2;
				}
					
				if (_double.contains(player))
				{
					power = 1;
					height = 0.3;
				}
					
				
				//Velocity
				UtilAction.velocity(player, UtilAlg.getTrajectory2d(other, player), power, false, 0, height, 1, true);
				UtilAction.velocity(other, UtilAlg.getTrajectory2d(player, other), power, false, 0, height, 1, true);
				
				//Sound
				if (_double.contains(player))
				{
					player.getWorld().playSound(player.getLocation(), Sound.PIG_DEATH, (float)(0.8 + Math.random() * 0.4), (float)(0.8 + Math.random() * 0.4));
				}
				else
				{
					player.getWorld().playSound(player.getLocation(), Sound.PIG_IDLE, 1f, (float)(1.5 + Math.random() * 0.5));
				}
			}
		}
	}

	@EventHandler
	public void Clean(PlayerQuitEvent event)
	{
		_double.remove(event.getPlayer());
	}
}
