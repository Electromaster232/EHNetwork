package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkSuperSquid extends SmashPerk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	
	public PerkSuperSquid() 
	{
		super("Super Squid", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to use " + C.cGreen + "Super Squid",
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
		
		if (isSuperActive(player))
			return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 8000, true, true))
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
			if (!_active.containsKey(cur))
				continue;
			
			if (isSuperActive(cur))
				return;
			
			if (!cur.isBlocking())
			{
				_active.remove(cur);
				continue;
			}
			
			if (UtilTime.elapsed(_active.get(cur), 1100))
			{
				_active.remove(cur);
				continue;
			}

			UtilAction.velocity(cur, 0.6, 0.1, 1, true);
			
			cur.getWorld().playSound(cur.getLocation(), Sound.SPLASH2, 0.2f, 1f);
			cur.getWorld().playEffect(cur.getLocation(), Effect.STEP_SOUND, 8);
		}
	}

	@EventHandler
	public void DamageCancel(CustomDamageEvent event)
	{
		if (_active.containsKey(event.GetDamageeEntity()))
			event.SetCancelled("Super Squid");
	}
}
