package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkNeedler extends SmashPerk
{
	private HashMap<Player, Integer> _active = new HashMap<Player, Integer>();
	private HashSet<Arrow> _arrows = new HashSet<Arrow>();
	
	public PerkNeedler() 
	{
		super("Needler", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to use " + C.cGreen + "Needler"
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

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), isSuperActive(player) ? 600 : 1800, !isSuperActive(player), !isSuperActive(player)))
			return;

		_active.put(player, 8);

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

			if (!cur.isBlocking())
			{
				_active.remove(cur);
				continue;
			}

			int count = _active.get(cur) - 1;
			
			
			if (count <= 0)
			{
				_active.remove(cur);
				continue;
			}
			else
			{
				_active.put(cur, count);
			}

			Arrow arrow = cur.getWorld().spawnArrow(cur.getEyeLocation().add(cur.getLocation().getDirection()), 
					cur.getLocation().getDirection(), 1.2f, 6);
			arrow.setShooter(cur);
			_arrows.add(arrow);

			//Sound
			cur.getWorld().playSound(cur.getLocation(), Sound.SPIDER_IDLE, 0.8f, 2f);
		}
	}

	@EventHandler
	public void ArrowDamamge(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;

		if (event.GetDamagerPlayer(true) == null)
			return;

		if (!(event.GetProjectile() instanceof Arrow))
			return;

		Player damager = event.GetDamagerPlayer(true);

		if (!Kit.HasKit(damager))
			return;

		event.SetCancelled("Needler Cancel");
		
		event.GetProjectile().remove();
		
		//Damage Event
		Manager.GetDamage().NewDamageEvent(event.GetDamageeEntity(), damager, null, 
				DamageCause.THORNS, 1.1, true, true, false,
				damager.getName(), GetName());	

		if(Manager.GetGame().GetTeam(event.GetDamageePlayer()) != Manager.GetGame().GetTeam(damager))
		{
			Manager.GetCondition().Factory().Poison(GetName(), event.GetDamageeEntity(), damager, 2, 0, false, false, false);
		}
	}
	
	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Iterator<Arrow> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();) 
		{
			Arrow arrow = arrowIterator.next();
			
			if (arrow.isOnGround() || !arrow.isValid() || arrow.getTicksLived() > 300)
			{
				arrowIterator.remove();
				arrow.remove();
			}
		}
	}
}
