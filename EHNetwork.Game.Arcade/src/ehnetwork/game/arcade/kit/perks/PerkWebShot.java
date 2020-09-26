package ehnetwork.game.arcade.kit.perks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.arcade.kit.SmashPerk;

public class PerkWebShot extends SmashPerk implements IThrown
{
	public PerkWebShot() 
	{
		super("Spin Web", new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Spin Web"
				});
	}


	@EventHandler
	public void ShootWeb(PlayerInteractEvent event)
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

		if (!Recharge.Instance.use(player, GetName(), isSuperActive(player) ? 1000 : 10000, !isSuperActive(player), !isSuperActive(player)))
			return;

		event.setCancelled(true);

		//Boost
		UtilAction.velocity(player, 1.2, 0.2, 1.2, true);
		
		for (int i=0 ; i<20 ; i++)
		{
			org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getLocation().add(0, 0.5, 0), 
					ItemStackFactory.Instance.CreateStack(Material.WEB, (byte)0, 1, "Web " + player.getName() + " " + i));
			
			Vector random = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			random.normalize();
			random.multiply(0.2);
			
			UtilAction.velocity(ent, player.getLocation().getDirection().multiply(-1).add(random), 1 + Math.random() * 0.4, false, 0, 0.2, 10, false);	
			Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 0.5f);
		}

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.SPIDER_IDLE, 2f, 0.6f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target != null)
		{
			data.GetThrown().remove();

			Manager.GetBlockRestore().Add(target.getLocation().getBlock(), 30, (byte)0, 3000);
			
			//Damage Event
			Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
					DamageCause.PROJECTILE, 6, false, false, false,
					UtilEnt.getName(data.GetThrower()), GetName());
			
			target.setVelocity(new Vector(0,0,0));
			
			return;
		}

		Web(data);
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Web(data);
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Web(data);
	}

	public void Web(ProjectileUser data)
	{
		Location loc = data.GetThrown().getLocation();
		data.GetThrown().remove();

		if (data.GetThrown().getTicksLived() > 3)
			Manager.GetBlockRestore().Add(loc.getBlock(), 30, (byte)0, 2000);
	}
}
