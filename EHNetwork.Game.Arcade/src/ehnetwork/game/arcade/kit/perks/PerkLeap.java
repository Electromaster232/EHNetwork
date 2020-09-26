package ehnetwork.game.arcade.kit.perks;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.event.PerkLeapEvent;

public class PerkLeap extends Perk
{
	private String _name;
	private double _power;
	private double _heightMax;
	private long _recharge;
	private int _maxUses;
	
	private NautHashMap<String, Integer> _uses = new NautHashMap<String, Integer>();
	
	public PerkLeap(String name, double power, double heightLimit, long recharge) 
	{
		super("Leaper", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + name
				});
		
		_name = name;
		_power = power;
		_heightMax = heightLimit;
		_recharge = recharge;
		_maxUses = 0;
	}
	
	public PerkLeap(String name, double power, double heightLimit, long recharge, int uses) 
	{
		super("Leaper", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + name + C.cGray + "  (" + C.cWhite + uses + " Charges" + C.cGray + ")"
				});
		
		_name = name;
		_power = power;
		_heightMax = heightLimit;
		_recharge = recharge;
		_maxUses = uses;
	}

	@EventHandler
	public void Leap(PlayerInteractEvent event)
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
		
		if (!Kit.HasKit(player))
			return;
		
		//Check Uses
		if (_maxUses > 0)
		{
			if (!_uses.containsKey(player.getName()))
				_uses.put(player.getName(), _maxUses);
			
			if (_uses.get(player.getName()) <= 0)
			{
				UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(_name) + " anymore."));
				return;
			}
		}
		
		//Energy
		if (!Recharge.Instance.use(player, _name, _recharge, true, true))
			return;
		
		//Use Use
		if (_maxUses > 0)
		{
			int count = _uses.get(player.getName());
			count--;
			
			player.setExp(Math.min(0.99f, (float)count/(float)_maxUses));
			
			_uses.put(player.getName(), count);
		}
		
		Entity ent = player;
		
		if (player.getVehicle() != null)
			if (player.getVehicle() instanceof Horse)
				ent = player.getVehicle();
		
		UtilAction.velocity(ent, _power, 0.2, _heightMax, true);
		
		player.setFallDistance(0);
		
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(_name) + "."));
		
		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
		
		PerkLeapEvent leapEvent = new PerkLeapEvent(player);
		UtilServer.getServer().getPluginManager().callEvent(leapEvent);
	}
}
