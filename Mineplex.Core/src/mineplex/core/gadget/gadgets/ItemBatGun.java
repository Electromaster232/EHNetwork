package mineplex.core.gadget.gadgets;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.ItemGadget;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class ItemBatGun extends ItemGadget
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	private HashMap<Player, Location> _velocity = new HashMap<Player, Location>();
	private HashMap<Player, ArrayList<Bat>> _bats = new HashMap<Player, ArrayList<Bat>>();
	
	public ItemBatGun(GadgetManager manager)
	{
		super(manager, "Bat Blaster", new String[] 
				{
					C.cWhite + "Launch waves of annoying bats",
					C.cWhite + "at people you don't like!",
				}, 
				-1,  
				Material.IRON_BARDING, (byte)0, 
				5000, new Ammo("Bat Blaster", "50 Bats", Material.IRON_BARDING, (byte)0, new String[] { C.cWhite + "50 Bats for your Bat Blaster!" }, 500, 50));
	}
	
	@Override
	public void DisableCustom(Player player) 
	{
		super.DisableCustom(player);
		
		Clear(player);
	}

	@Override
	public void ActivateCustom(Player player)
	{
		//Start
		_velocity.put(player, player.getEyeLocation());
		_active.put(player, System.currentTimeMillis());
		
		_bats.put(player, new ArrayList<Bat>());
		
		for (int i=0 ; i<16 ; i++)
			_bats.get(player).add(player.getWorld().spawn(player.getEyeLocation(), Bat.class));
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player cur : UtilServer.getPlayers())
		{
			if (!_active.containsKey(cur))
				continue;
			
			if (UtilTime.elapsed(_active.get(cur), 3000))
			{
				Clear(cur);
				continue;
			}
			
			Location loc = _velocity.get(cur);
			
			//Bat Movement
			for (Bat bat : _bats.get(cur))
			{
				if (!bat.isValid())
					continue;
				Vector rand = new Vector((Math.random() - 0.5)/3, (Math.random() - 0.5)/3, (Math.random() - 0.5)/3);
				bat.setVelocity(loc.getDirection().clone().multiply(0.5).add(rand));
				
				for (Player other : UtilServer.getPlayers())
				{
					if (other.equals(cur))
						continue;
					
					if (!Manager.getPreferencesManager().Get(other).HubGames || !Manager.getPreferencesManager().Get(other).ShowPlayers)
						continue;
					
					if (!Recharge.Instance.usable(other, "Hit by Bat"))
						continue;
					
					if (UtilEnt.hitBox(bat.getLocation(), other, 2, null))
					{
						if (Manager.collideEvent(this, other))
							continue;
						
						//Damage Event
						UtilAction.velocity(other, UtilAlg.getTrajectory(cur, other), 0.4, false, 0, 0.2, 10, true);

						//Effect
						bat.getWorld().playSound(bat.getLocation(), Sound.BAT_HURT, 1f, 1f);
						UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, bat.getLocation(), 0, 0, 0, 0, 3,
								ViewDist.NORMAL, UtilServer.getPlayers());
						
						bat.remove();
						
						//Recharge on hit
						Recharge.Instance.useForce(other, "Hit by Bat", 200);
					}
				}
			}
		}
	}
	
	public void Clear(Player player)
	{
		_active.remove(player);
		_velocity.remove(player);
		if (_bats.containsKey(player))
		{
			for (Bat bat : _bats.get(player))
			{
				if (bat.isValid())
					UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, bat.getLocation(), 0, 0, 0, 0, 3,
							ViewDist.NORMAL, UtilServer.getPlayers());
				
				bat.remove();
			}
				
			_bats.remove(player);
		}
	}
}
