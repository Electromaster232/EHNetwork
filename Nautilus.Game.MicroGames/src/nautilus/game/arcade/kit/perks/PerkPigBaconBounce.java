package nautilus.game.arcade.kit.perks;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilServer;
import mineplex.core.disguise.disguises.DisguiseBase;
import mineplex.core.disguise.disguises.DisguisePigZombie;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkPigBaconBounce extends SmashPerk implements IThrown
{
	public PerkPigBaconBounce() 
	{
		super("Bouncy Bacon", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Bouncy Bacon",
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

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;
		
		float energy = 0.2f;
		
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null && disguise instanceof DisguisePigZombie)
			energy = energy * 0.7f;

		//Energy
		if (player.getExp() < energy)
		{
			UtilPlayer.message(player, F.main("Energy", "Not enough Energy to use " + F.skill(GetName()) + "."));
			return;
		}
			
		//Recharge
		if (!Recharge.Instance.use(player, GetName(), 100, false, false))
			return;
	
		//Use Energy
		player.setExp(Math.max(0f, player.getExp() - energy));
		
		//Launch
		Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(Material.PORK));
		UtilAction.velocity(ent, player.getLocation().getDirection(), 1.2, false, 0, 0.2, 10, false);
		Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 0.4f);
		ent.setPickupDelay(9999);
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.PIG_IDLE, 2f, 1.5f);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
		
		if (target == null)
			return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, 4, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());	
		
		Item item = (Item)data.GetThrown();
		item.setItemStack(new ItemStack(Material.GRILLED_PORK));
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
	}
	
	public void Rebound(LivingEntity player, Entity ent)
	{
		ent.getWorld().playSound(ent.getLocation(), Sound.ITEM_PICKUP, 1f, 0.5f);
		
		double mult = 0.5 + (0.035 * UtilMath.offset(player.getLocation(), ent.getLocation()));
		
		//Velocity
		ent.setVelocity(player.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().add(new Vector(0, 0.4, 0)).multiply(mult));
		
		//Ticks
		if (ent instanceof Item)
			((Item)ent).setPickupDelay(5);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (!Kit.HasKit(event.getPlayer()))
			return;
		
		if (event.getItem().getItemStack().getType() != Material.PORK && event.getItem().getItemStack().getType() != Material.GRILLED_PORK)
			return;
		
		//Remove
		event.getItem().remove();
		
		//Restore Energy
		event.getPlayer().setExp(Math.min(0.999f, event.getPlayer().getExp() + 0.05f));
		
		//Sound
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.EAT, 2f, 1f);
		
		//Heal
		if (event.getItem().getItemStack().getType() == Material.GRILLED_PORK)
		{
			UtilPlayer.health(event.getPlayer(), 1);
			UtilParticle.PlayParticle(ParticleType.HEART, event.getPlayer().getLocation().add(0, 0.5, 0), 0.2f, 0.2f, 0.2f, 0, 4,
					ViewDist.LONG, UtilServer.getPlayers());
		}
	}
}
