package mineplex.core.recharge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.account.event.ClientUnloadEvent;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;

public class Recharge extends MiniPlugin
{
	public static Recharge Instance;
	
	public HashSet<String> informSet = new HashSet<String>();
	public NautHashMap<String, NautHashMap<String, RechargeData>> _recharge = new NautHashMap<String, NautHashMap<String, RechargeData>>();
	
	protected Recharge(JavaPlugin plugin)
	{
		super("Recharge", plugin);
	}
	
	public static void Initialize(JavaPlugin plugin)
	{
		Instance = new Recharge(plugin);
	}
	
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event)
	{
		Get(event.getEntity().getName()).clear();
	}
	
	public NautHashMap<String, RechargeData> Get(String name)
	{
		if (!_recharge.containsKey(name))
			_recharge.put(name, new NautHashMap<String, RechargeData>());
		
		return _recharge.get(name);
	}

	public NautHashMap<String, RechargeData> Get(Player player)
	{
		return Get(player.getName());
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return; 
		
		recharge();
	}
	
	public void recharge()
	{
		for (Player cur : UtilServer.getPlayers())
		{
			LinkedList<String> rechargeList = new LinkedList<String>();

			//Check Recharged
			for (String ability : Get(cur).keySet())
			{
				if (Get(cur).get(ability).Update())
					rechargeList.add(ability);
			}

			//Inform Recharge
			for (String ability : rechargeList)
			{
				Get(cur).remove(ability);
				
				//Event
				RechargedEvent rechargedEvent = new RechargedEvent(cur, ability);
				UtilServer.getServer().getPluginManager().callEvent(rechargedEvent);
				
				if (informSet.contains(ability))
					UtilPlayer.message(cur, F.main("Recharge", "You can use " + F.skill(ability) + "."));
			}
		}
	}
	
	public boolean use(Player player, String ability, long recharge, boolean inform, boolean attachItem)
	{
		return use(player, ability, ability, recharge, inform, attachItem);
	}
	
	public boolean use(Player player, String ability, String abilityFull, long recharge, boolean inform, boolean attachItem)
	{
		return use(player, ability, abilityFull, recharge, inform, attachItem, false);
	}
	
	public boolean use(Player player, String ability, long recharge, boolean inform, boolean attachItem, boolean attachDurability)
	{
		return use(player, ability, ability, recharge, inform, attachItem, attachDurability);
	}
	
	public boolean use(Player player, String ability, String abilityFull, long recharge, boolean inform, boolean attachItem, boolean attachDurability)
	{
		if (recharge == 0)
			return true;

		//Ensure Expirey
		recharge();
		
		//Lodge Recharge Msg
		if (inform && recharge > 1000)
			informSet.add(ability);
	
		//Recharging
		if (Get(player).containsKey(ability))
		{
			if (inform)
			{
				UtilPlayer.message(player, F.main("Recharge", "You cannot use " + F.skill(abilityFull) + " for " + 
						F.time(UtilTime.convertString((Get(player).get(ability).GetRemaining()), 1, TimeUnit.FIT)) + "."));
			}

			return false;
		}

		//Insert
		UseRecharge(player, ability, recharge, attachItem, attachDurability);

		return true;
	}
	
	public void useForce(Player player, String ability, long recharge)
	{
		useForce(player, ability, recharge, false);
	}
	
	public void useForce(Player player, String ability, long recharge, boolean attachItem)
	{
		UseRecharge(player, ability, recharge, attachItem, false);
	}
	
	public boolean usable(Player player, String ability)
	{
		return usable(player, ability, false);
	}
	
	public boolean usable(Player player, String ability, boolean inform)
	{
		if (!Get(player).containsKey(ability))
			return true;
		
		if (Get(player).get(ability).GetRemaining() <= 0)
		{
			return true;
		}
		else
		{
			if (inform)
				UtilPlayer.message(player, F.main("Recharge", "You cannot use " + F.skill(ability) + " for " + 
					F.time(UtilTime.convertString((Get(player).get(ability).GetRemaining()), 1, TimeUnit.FIT)) + "."));
			
			return false;
		}
	}
	
	public void UseRecharge(Player player, String ability, long recharge, boolean attachItem, boolean attachDurability)
	{
		//Event
		RechargeEvent rechargeEvent = new RechargeEvent(player, ability, recharge);
		UtilServer.getServer().getPluginManager().callEvent(rechargeEvent);
		
		Get(player).put(ability, new RechargeData(this, player, ability, player.getItemInHand(), 
				rechargeEvent.GetRecharge(), attachItem, attachDurability));
	}
	
	public void recharge(Player player, String ability)
	{
		Get(player).remove(ability);
	}

	@EventHandler
	public void clearPlayer(ClientUnloadEvent event)
	{
		_recharge.remove(event.GetName());
	}
	
	public void setDisplayForce(Player player, String ability, boolean displayForce)
	{
		if (!_recharge.containsKey(player.getName()))
			return;

		if (!_recharge.get(player.getName()).containsKey(ability))
			return;

		_recharge.get(player.getName()).get(ability).DisplayForce = displayForce;
	}
	
	public void setCountdown(Player player, String ability, boolean countdown)
	{
		if (!_recharge.containsKey(player.getName()))
			return;

		if (!_recharge.get(player.getName()).containsKey(ability))
			return;

		_recharge.get(player.getName()).get(ability).Countdown = countdown; 
	}
	
	public void Reset(Player player) 
	{
		_recharge.put(player.getName(), new NautHashMap<String, RechargeData>());
	}
	
	public void Reset(Player player, String stringContains) 
	{
		NautHashMap<String, RechargeData> data = _recharge.get(player.getName());
		
		if (data == null)
			return;
		
		Iterator<String> rechargeIter = data.keySet().iterator();
		
		while (rechargeIter.hasNext())
		{
			String key = rechargeIter.next();
			
			if (key.toLowerCase().contains(stringContains.toLowerCase()))
			{
				rechargeIter.remove();
			}
		}
	}

	public void debug(Player player, String ability)
	{
		if (!_recharge.containsKey(player.getName()))
		{
			player.sendMessage("No Recharge Map.");
			return;
		}
		
		if (!_recharge.get(player.getName()).containsKey(ability))
		{
			player.sendMessage("Ability Not Found.");
			return;
		}
		
		_recharge.get(player.getName()).get(ability).debug(player);
	}
}
