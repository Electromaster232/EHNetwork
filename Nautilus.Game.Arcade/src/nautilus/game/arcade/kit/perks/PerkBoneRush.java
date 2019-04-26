package nautilus.game.arcade.kit.perks;

import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkBoneRush extends SmashPerk implements IThrown
{
	private WeakHashMap<Player, Long> _active = new WeakHashMap<Player, Long>();

	private double yLimit = 0.25;

	public PerkBoneRush() 
	{
		super("Bone Rush", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Bone Rush",
				C.cGray + "Crouch to avoid movement with " + C.cGreen + "Bone Rush"
				});
	}

	@EventHandler
	public void Skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE"))
			return;

		Player player = event.getPlayer();

		if (isSuperActive(player))
			return;
		
		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return;

		_active.put(player, System.currentTimeMillis());

		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@Override
	public void addSuperCustom(Player player)
	{
		_active.put(player, System.currentTimeMillis());
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> playerIterator = _active.keySet().iterator();

		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();

			if (!player.isValid() || (UtilTime.elapsed(_active.get(player), 1500) && !isSuperActive(player)))
			{
				playerIterator.remove();
				continue;
			}
		
			//Sound
			player.getWorld().playSound(player.getLocation(), Sound.SKELETON_HURT, 0.4f, (float)(Math.random() + 1));
				
			//Velocity
			Vector dir = player.getLocation().getDirection();
			double limit = isSuperActive(player) ? yLimit + 0.1 : yLimit;
			if (dir.getY() > limit)
				dir.setY(limit);

			//Player
			if (!player.isSneaking())
				UtilAction.velocity(player, dir, 0.6, false, 0, 0.1, 0.3, false);

			//Bones
			for (int i=0 ; i<6 ; i++)
			{
				Item bone = player.getWorld().dropItem(player.getLocation().add(Math.random()*5 - 2.5, Math.random()*3, Math.random()*5 - 2.5), new ItemStack(Material.BONE));
				UtilAction.velocity(bone, dir, 0.6 + 0.3 * Math.random(), false, 0, 0.1 + Math.random() * 0.05, 0.3, false);
				Manager.GetProjectile().AddThrow(bone, player, this, -1, true, true, true, false, 0.5f);
			}
		}		
	}

	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() != null && event.GetReason().contains(GetName()))
			event.AddKnockback(GetName(), 10);
		
		if (event.GetReason() != null && event.GetReason().contains("Bone Storm"))
			event.AddKnockback(GetName(), 6);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		data.GetThrown().remove();

		if (target == null)
			return;
		
		Player damager = (Player)data.GetThrower();
		
		double damage = 0.7;
		String reason = GetName();
		
		if (isSuperActive(damager))	
		{
			damage = 3;
			reason = "Bone Storm";
		}

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, damage, false, true, false,
				UtilEnt.getName(data.GetThrower()), reason);	

		target.setVelocity(data.GetThrown().getVelocity());
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		data.GetThrown().remove();
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		data.GetThrown().remove();
	}
	
	@EventHandler
	public void Clean(PlayerDeathEvent event)
	{
		_active.remove(event.getEntity());
	}
}
