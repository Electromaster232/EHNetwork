package ehnetwork.game.microgames.kit.perks;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.microgames.kit.Perk;

public class PerkQuickshot extends Perk
{
	private String _name;
	private double _power;
	private long _recharge;
	
	public PerkQuickshot(String name, double power, long recharge) 
	{
		super("Quickshot", new String[] 
				{
				C.cYellow + "Left-Click" + C.cGray + " with Bow to " + C.cGreen + name
				});
		
		_name = name;
		_power = power;
		_recharge = recharge;
	}

	@EventHandler
	public void Leap(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (event.getPlayer().getItemInHand().getType() != Material.BOW)
			return;
		
		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, _name, _recharge, true, true))
			return;
		
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setVelocity(player.getLocation().getDirection().multiply(_power));
		
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(_name) + "."));
	}
}
